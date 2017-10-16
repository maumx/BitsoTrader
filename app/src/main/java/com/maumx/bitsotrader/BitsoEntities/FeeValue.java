package com.maumx.bitsotrader.BitsoEntities;

/**
 * Created by Mauricio on 10/04/2017.
 */

public class FeeValue {

    private String book;
    private float fee_decimal;
    private  float   fee_percent;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public float getFee_decimal() {
        return fee_decimal;
    }

    public void setFee_decimal(float fee_decimal) {
        this.fee_decimal = fee_decimal;
    }

    public float getFee_percent() {
        return fee_percent;
    }

    public void setFee_percent(float fee_percent) {
        this.fee_percent = fee_percent;
    }
}
