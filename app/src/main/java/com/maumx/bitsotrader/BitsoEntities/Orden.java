package com.maumx.bitsotrader.BitsoEntities;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Mauricio on 07/04/2017.
 */

public class Orden {

       private String book;
    private BigDecimal original_amount;
    private BigDecimal unfilled_amount;
    private BigDecimal original_value;
    private BigDecimal price;
    private Date created_at;
    private Date updated_at;

    private String oid;
    private String side;
    private String status;
    private String type;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public BigDecimal getOriginal_amount() {
        return original_amount;
    }

    public void setOriginal_amount(BigDecimal original_amount) {
        this.original_amount = original_amount;
    }

    public BigDecimal getUnfilled_amount() {
        return unfilled_amount;
    }

    public void setUnfilled_amount(BigDecimal unfilled_amount) {
        this.unfilled_amount = unfilled_amount;
    }

    public BigDecimal getOriginal_value() {
        return original_value;
    }

    public void setOriginal_value(BigDecimal original_value) {
        this.original_value = original_value;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
