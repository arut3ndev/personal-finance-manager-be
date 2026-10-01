package org.example.personalfinancemanagerbe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTransaction_thenReadItBack() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 42.42,
                                  "description": "groceries",
                                  "date": "2026-04-03",
                                  "type": "OUTGOING"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String location = created.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(42.42))
                .andExpect(jsonPath("$.description").value("groceries"))
                .andExpect(jsonPath("$.date").value("2026-04-03"))
                .andExpect(jsonPath("$.type").value("OUTGOING"));
    }

    @Test
    void createTransactionWithUnknownCategory_returns422() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 42.42,
                                  "date": "2026-04-03",
                                  "type": "OUTGOING",
                                  "categoryId": 99999
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }
}