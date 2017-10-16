package com.maumx.bitsotrader.Comun;

import java.math.BigDecimal;

/**
 * Created by Mauricio on 11/04/2017.
 */

public  class Redondeo {


    public static BigDecimal Redondeo(double monto, String tipoMoneda)
    {
        BigDecimal d=BigDecimal.ONE;

        if (tipoMoneda.equals("mxn"))
        {
            double mul=(100.0d);

          double t=  Math.floor(monto*mul)/mul;


            d=   BigDecimal.valueOf(t).setScale(2,BigDecimal.ROUND_FLOOR);

        }else
            {

                int redondeo=0;

                switch ( tipoMoneda )
                {
                    case "xrp":
                        redondeo=6;
                        break;
                    case "btc":
                    case "eth":
                            redondeo=8;
                            break;
                }


                double mul=(100000000.0d);

               double     t= Math.ceil(monto*mul)/mul;

                d=   BigDecimal.valueOf(t).setScale(redondeo
                        ,BigDecimal.ROUND_CEILING);




            }


return d;
    }


}
