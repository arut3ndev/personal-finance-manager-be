package org.example.personalfinancemanagerbe.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.models.CategoryModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {
    @NotBlank
    @Size(min = 3, max = 32)
    private String name;
    @Size(max = 256)
    private String description;

    public CategoryModel toModel(){
        return new CategoryModel(null, name, description, null);
    }
}
