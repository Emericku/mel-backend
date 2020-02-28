package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.Category;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ProductRequest {

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    private Category category;

    private Double price;

    private List<String> ingredients;

    private boolean isOriginal;

    private String image;

}
