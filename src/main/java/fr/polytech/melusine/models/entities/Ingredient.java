package fr.polytech.melusine.models.entities;

import fr.polytech.melusine.models.enums.IngredientType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Ingredient {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String name;

    private long price;

    private long quantity;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    @Column(columnDefinition = "LONGBLOB")
    private String image;

    private boolean isDeleted;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}