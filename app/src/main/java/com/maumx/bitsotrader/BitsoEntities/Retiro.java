package com.maumx.bitsotrader.BitsoEntities;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Mauricio on 28/04/2017.
 */

public class Retiro {

      private String wid;
    private String currency;
    private String method;
    private BigDecimal amount;
    private String status;
    private Date created_at;


    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
