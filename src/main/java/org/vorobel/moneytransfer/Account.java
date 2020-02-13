package org.vorobel.moneytransfer;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NonNull
    private String balance;
}
