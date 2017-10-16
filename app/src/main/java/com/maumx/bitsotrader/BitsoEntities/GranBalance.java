package com.maumx.bitsotrader.BitsoEntities;

import com.maumx.bitsotrader.BitsoEntities.Balance;

import  java.util.List;
/**
 * Created by Mauricio on 04/04/2017.
 */

public class GranBalance {

    private List<Balance> balances;

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }
}
