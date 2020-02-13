package org.vorobel.moneytransfer;

import lombok.Data;

@Data
public class Transfer {
    private String amount;
    private long sourceId, destinationId;
}
