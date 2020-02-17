package org.vorobel.moneytransfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "transfers")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({ "id", "balance" })
public class Transfer extends PanacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Getter
    @Setter
    @NonNull
    @JsonProperty(required = true)
    private long sourceId, destinationId;
    @Getter
    @Setter
    @NonNull
    @JsonProperty(required = true)
    private String amount;

    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }
}
