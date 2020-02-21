package fr.polytech.melusine.models.entities;

import fr.polytech.melusine.models.enums.ValidationStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orderItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private long price;

    private long quantity;

    private String orderId;

    @Enumerated(EnumType.STRING)
    private ValidationStatus status;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}
