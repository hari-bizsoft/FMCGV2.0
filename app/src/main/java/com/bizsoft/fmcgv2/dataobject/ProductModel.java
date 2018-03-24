package com.bizsoft.fmcgv2.dataobject;

/**
 * Created by shri on 10/8/17.
 */

public class ProductModel {

    Long ProductId;
    Long UOMId;
    Long Qty;
    double Amount;
    Long Rate;
    double DiscountAmount;
    double TotalAmount;
    double GSTAmount;
    double ExtraAmount;
    boolean IsResale;


    public boolean isResale() {
        return IsResale;
    }

    public void setResale(boolean resale) {
        IsResale = resale;
    }

    public String getReason() {
        return Particulars;
    }

    public void setReason(String reason) {
        this.Particulars = reason;
    }

    String Particulars;

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getGSTAmount() {
        return GSTAmount;
    }

    public void setGSTAmount(double GSTAmount) {
        this.GSTAmount = GSTAmount;
    }

    public double getExtraAmount() {
        return ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        ExtraAmount = extraAmount;
    }

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    public Long getUOMId() {
        return UOMId;
    }

    public void setUOMId(Long UOMId) {
        this.UOMId = UOMId;
    }

    public Long getQty() {
        return Qty;
    }

    public void setQty(Long qty) {
        Qty = qty;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public Long getRate() {
        return Rate;
    }

    public void setRate(Long rate) {
        Rate = rate;
    }
}
