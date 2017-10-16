package com.maumx.bitsotrader.BitsoEntities;

import com.maumx.bitsotrader.Servicios.DataBaseService;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Mauricio on 14/04/2017.
 */

public class Trade {

    private String book;
    private BigDecimal major;
    private  BigDecimal minor;
    private java.util.Date created_at;
    private  BigDecimal fees_amount;
    private  String fees_currency;
    private  BigDecimal price;
    private  int tid;
    private  String oid;
    private  String  side;


    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public BigDecimal getMajor() {
        return major;
    }

    public void setMajor(BigDecimal major) {
        this.major = major;
    }

    public BigDecimal getMinor() {
        return minor;
    }

    public void setMinor(BigDecimal minor) {
        this.minor = minor;
    }

    public java.util.Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(java.util.Date created_at) {
        this.created_at = created_at;
    }

    public BigDecimal getFees_amount() {
        return fees_amount;
    }

    public void setFees_amount(BigDecimal fees_amount) {
        this.fees_amount = fees_amount;
    }

    public String getFees_currency() {
        return fees_currency;
    }

    public void setFees_currency(String fees_currency) {
        this.fees_currency = fees_currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
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
}
