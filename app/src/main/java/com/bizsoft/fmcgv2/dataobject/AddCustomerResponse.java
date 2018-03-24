package com.bizsoft.fmcgv2.dataobject;

/**
 * Created by shri on 9/8/17.
 */

public class AddCustomerResponse {

    Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public boolean isHasError() {
        return HasError;
    }

    public void setHasError(boolean hasError) {
        HasError = hasError;
    }

    boolean HasError;
}
