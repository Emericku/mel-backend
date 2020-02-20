package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.enums.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CategoryResponse {

    private Category name;

    private String icon;

    private String color;

}
