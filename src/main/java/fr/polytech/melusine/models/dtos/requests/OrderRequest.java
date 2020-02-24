package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class OrderRequest {

    @NotNull
    @Size(min = 1)
    private String name;

    private String userId;

    @NotNull
    private List<String> items;

}
