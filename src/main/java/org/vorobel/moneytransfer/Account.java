package org.vorobel.moneytransfer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({ "id", "balance" })
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    @NonNull
    @JsonProperty(required = true)
    private String balance;

    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }
}
