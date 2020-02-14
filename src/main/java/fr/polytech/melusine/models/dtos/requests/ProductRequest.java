package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ProductRequest {

    @NonNull
    @Size(min = 2)
    private String name;

    @NonNull
    private Category category;

    private long price;

    private List<String> ingredients;

    private boolean isOriginal;

    private long quantity;

}
