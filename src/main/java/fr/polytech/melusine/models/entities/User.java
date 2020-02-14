package fr.polytech.melusine.models.entities;

import fr.polytech.melusine.models.enums.Section;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * The model of the client.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String nickName;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Section section;

    private long credit;

    private boolean isMembership;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}
