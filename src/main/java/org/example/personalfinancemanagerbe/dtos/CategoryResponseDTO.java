package org.example.personalfinancemanagerbe.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.models.CategoryModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;

    public CategoryResponseDTO(CategoryModel categoryModel){
        this.id = categoryModel.getId();
        this.name = categoryModel.getName();
        this.description = categoryModel.getDescription();
    }
}
