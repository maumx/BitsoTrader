package com.maumx.bitsotrader.BitsoEntities;

import java.math.BigDecimal;

/**
 * Created by Mauricio on 03/04/2017.
 */

public  class Balance
{

    private String currency ;
    private BigDecimal total ;
    private BigDecimal locked ;
    private BigDecimal available ;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getLocked() {
        return locked;
    }

    public void setLocked(BigDecimal locked) {
        this.locked = locked;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }
}


