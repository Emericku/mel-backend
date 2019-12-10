package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.Item;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class OrderRequest {

    @NonNull
    @Size(min = 1)
    private String name;

    private String userId;

    @NonNull
    private List<Item> items;

}
