package org.monakhov.poland;

import lombok.Value;

@Value
public class Position {
    int accountId, positionId;
    double lmv, smv;
    String asset_type;

}
