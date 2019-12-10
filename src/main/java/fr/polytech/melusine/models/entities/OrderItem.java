package fr.polytech.melusine.models.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private int quantity;

    private String orderId;

    private boolean delivered;

    private boolean cancelled;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}
