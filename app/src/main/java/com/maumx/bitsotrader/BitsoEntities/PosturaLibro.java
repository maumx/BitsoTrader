package com.maumx.bitsotrader.BitsoEntities;

import java.math.BigDecimal;

/**
 * Created by Mauricio on 06/04/2017.
 */

public class PosturaLibro {


        private String book;
    private BigDecimal price;
    private BigDecimal amount;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
