package com.maumx.bitsotrader.JsonResponse;

/**
 * Created by Mauricio on 09/04/2017.
 */

public class GeneralResponse {

    private  int mHttpStatusCode;
    private  String mJson;


    public int getmHttpStatusCode() {
        return mHttpStatusCode;
    }

    public void setmHttpStatusCode(int mHttpStatusCode) {
        this.mHttpStatusCode = mHttpStatusCode;
    }

    public String getmJson() {
        return mJson;
    }

    public void setmJson(String mJson) {
        this.mJson = mJson;
    }
}
