package com.maumx.bitsotrader.JsonResponse;

import java.util.List;

/**
 * Created by Mauricio on 03/04/2017.
 */

public class ResultadoP<T> {



    private  Boolean success;
    private T payload;



    public T getResult() {
        return payload;
    }

    public void setResult(T payLoad) {
        payload = payLoad;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
