package com.maumx.bitsotrader.BitsoEntities;

import java.util.List;

/**
 * Created by Mauricio on 10/04/2017.
 */

public class Fee {


    private List<FeeValue> fees;

    public List<FeeValue> getFees() {
        return fees;
    }

    public void setFees(List<FeeValue> fees) {
        this.fees = fees;
    }
}
