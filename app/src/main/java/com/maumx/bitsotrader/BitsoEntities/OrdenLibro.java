package com.maumx.bitsotrader.BitsoEntities;

/**
 * Created by Mauricio on 06/04/2017.
 */

import java.util.List;
public class OrdenLibro {



    private List<PosturaLibro> bids ;
    private List<PosturaLibro> asks;
    private String sequence ;

    public List<PosturaLibro> getBids() {
        return bids;
    }

    public void setBids(List<PosturaLibro> bids) {
        this.bids = bids;
    }

    public List<PosturaLibro> getAsks() {
        return asks;
    }

    public void setAsks(List<PosturaLibro> asks) {
        this.asks = asks;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
