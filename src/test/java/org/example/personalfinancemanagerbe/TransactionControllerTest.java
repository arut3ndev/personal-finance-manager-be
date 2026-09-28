package org.example.personalfinancemanagerbe;

import org.example.personalfinancemanagerbe.controllers.TransactionController;
import org.example.personalfinancemanagerbe.exceptions.InvalidReferenceException;
import org.example.personalfinancemanagerbe.exceptions.NotFoundException;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.services.TransactionService;
import org.example.personalfinancemanagerbe.util.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    private TransactionModel transaction(Long id, CategoryModel categoryModel) {
        return new TransactionModel(id, new BigDecimal("42.00"), "groceries",
                LocalDate.of(2026, 3, 1), TransactionType.OUTGOING, categoryModel);
    }

    private CategoryModel category(Long id){
        return new CategoryModel(
                id,
                "Food",
                "Food category",
                null
        );
    }

    private final String properJsonWithNoCategory = """
                            {
                                  "amount" : 42.42,
                                  "description" : "desc",
                                  "date" : "2026-04-03",
                                  "type" : "INCOMING"
                            }
                         """;

    private final String properJsonWithCategoryAttached = """
                            {
                                  "amount" : 42.42,
                                  "description" : "desc",
                                  "date" : "2026-04-03",
                                  "type" : "INCOMING",
                                  "categoryId": 1
                            }
                         """;

    // Testing GET /api/transactions

    @Test
    public void testGetAllTransactions_WithTransactions_ReturnsOk() throws Exception {
        when(transactionService.getAll()).thenReturn(List.of(
                transaction(1L, null),
                transaction(2L, null)));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].transactionId", is(1)))
                .andExpect(jsonPath("$[1].transactionId", is(2)));
    }

    @Test
    public void testGetAllTransactions_WithNoTransactions_ReturnsOk() throws Exception {
        when(transactionService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Testing GET /api/transactions/{id}

    @Test
    public void testGetTransactionById_WithNoCategory_ReturnsOk() throws Exception{
        when(transactionService.getTransactionById(1L)).thenReturn(transaction(1L, null));
        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(42.00)));
    }

    @Test
    public void testGetTransactionById_WithCategory_ReturnsOk() throws Exception{
        when(transactionService.getTransactionById(1L)).thenReturn(transaction(1L, category(1L)));
        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId", is(1)))
                .andExpect(jsonPath("$.categoryId", is(1)));
    }

    @Test
    public void testGetTransactionById_ReturnsNotFound() throws Exception{
        when(transactionService.getTransactionById(99L)).thenThrow(new NotFoundException("Transaction", 99L));
        mockMvc.perform(get("/api/transactions/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not found Transaction with id: 99")));
    }

    // TODO: Post tests

    @Test
    public void testPostTransaction_WithNoCategory_ReturnsOk() throws Exception{
        TransactionModel transactionModel = transaction(1L, null);
        when(transactionService.saveTransaction(any(), isNull())).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(properJsonWithNoCategory))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/transactions/1")));
    }

    @Test
    public void testPostTransaction_WithCategory_ReturnsOk() throws Exception{
        TransactionModel transactionModel = transaction(1L, category(1L));
        when(transactionService.saveTransaction(any(), eq(1L))).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(properJsonWithCategoryAttached))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/transactions/1")));
        verify(transactionService).saveTransaction(any(), eq(1L));
    }

    @Test
    public void testPostTransactionWithNegativeAmount_WithNoCategory_Returns400() throws Exception{
        TransactionModel transactionModel = transaction(1L, null);
        when(transactionService.saveTransaction(any(), isNull())).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount" : -42.42,
                                  "description" : "desc",
                                  "date" : "2026-04-03",
                                  "type" : "INCOMING"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/transactions")))
                .andExpect(jsonPath("$.fieldViolationList[0].field", is("amount")))
                .andExpect(jsonPath("$.fieldViolationList[0].message", is("must be greater than 0")));
    }

    @Test
    public void testPostTransactionWithNoAmountField_WithNoCategory_Returns400() throws Exception{
        TransactionModel transactionModel = transaction(1L, null);
        when(transactionService.saveTransaction(any(), isNull())).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description" : "desc",
                                  "date" : "2026-04-03",
                                  "type" : "INCOMING"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/transactions")))
                .andExpect(jsonPath("$.fieldViolationList[0].field", is("amount")))
                .andExpect(jsonPath("$.fieldViolationList[0].message", is("must not be null")));
    }

    @Test
    public void testPostTransactionWithNoDateField_WithNoCategory_Returns400() throws Exception{
        TransactionModel transactionModel = transaction(1L, null);
        when(transactionService.saveTransaction(any(), isNull())).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                  "amount" : 42.42,
                                  "description" : "desc",
                                  "type" : "INCOMING"
                            }
                         """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/transactions")))
                .andExpect(jsonPath("$.fieldViolationList[0].field", is("date")))
                .andExpect(jsonPath("$.fieldViolationList[0].message", is("must not be null")));
    }

    @Test
    public void testPostTransactionWithNoTransactionTypeField_WithNoCategory_Returns400() throws Exception{
        TransactionModel transactionModel = transaction(1L, null);
        when(transactionService.saveTransaction(any(), isNull())).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                  "amount" : 42.42,
                                  "description" : "desc",
                                  "date" : "2026-04-03"
                            }
                         """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/transactions")))
                .andExpect(jsonPath("$.fieldViolationList[0].field", is("type")))
                .andExpect(jsonPath("$.fieldViolationList[0].message", is("must not be null")));
    }

    @Test
    public void testPostTransactionWithMalformedRequest_WithNoCategory_Returns400() throws Exception{
        TransactionModel transactionModel = transaction(1L, null);
        when(transactionService.saveTransaction(any(), isNull())).thenReturn(transactionModel);
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount" : 42.42
                                  "description" : "desc",
                                  "date" : "2026-04-03",
                                  "type" : "INCOMING"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Malformed request body")))
                .andExpect(jsonPath("$.path", is("/api/transactions")));
    }

    @Test
    void postTransaction_withInvalidEnumValue_returns400() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "amount" : 42.42,
                              "description" : "desc",
                              "date" : "2026-04-03",
                              "type" : "WRONG_ENUM"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Malformed request body")))
                .andExpect(jsonPath("$.path", is("/api/transactions")));

        verify(transactionService, never()).saveTransaction(any(), any());
    }

    @Test
    void postTransactionWithCategory_withInvalidReference_returns422() throws Exception {
        when(transactionService.saveTransaction(any(), eq(99L))).thenThrow(new InvalidReferenceException("Category", 99L));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "amount" : 42.42,
                              "description" : "desc",
                              "date" : "2026-04-03",
                              "type" : "INCOMING",
                              "categoryId" : 99
                            }
                            """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message", is("Invalid reference for Category with id: 99")));
    }

    // TODO: Put tests
    // TODO: Delete tests

}
