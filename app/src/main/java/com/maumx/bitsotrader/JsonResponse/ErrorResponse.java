package com.maumx.bitsotrader.JsonResponse;

/**
 * Created by Mauricio on 09/04/2017.
 */

public class ErrorResponse {

    private  String message;
    private  int code;


    public String getmMessage() {
        return message;
    }

    public void setmMessage(String mMessage) {
        this.message = mMessage;
    }

    public int getmCode() {
        return code;
    }

    public void setmCode(int mCode) {
        this.code = mCode;
    }
}
