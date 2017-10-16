package com.maumx.bitsotrader.JsonResponse;

/**
 * Created by Mauricio on 03/04/2017.
 */

public class ResultadoN {



    private  Boolean success;
    private ErrorResponse error;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }
}
