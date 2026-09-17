package org.example.personalfinancemanagerbe.dtos;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.models.CategoryModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;

    public CategoryDTO(CategoryModel categoryModel){
        this.id = categoryModel.getId();
        this.name = categoryModel.getName();
        this.description = categoryModel.getDescription();
    }

    public CategoryModel toModel(){
        return new CategoryModel(id, name, description, null);
    }
}
