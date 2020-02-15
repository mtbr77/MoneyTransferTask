package org.vorobel.moneytransfer;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Getter
    private long id;

    @JsonProperty(required = true)
    @NonNull
    @Getter
    @Setter
    private String balance;
}
