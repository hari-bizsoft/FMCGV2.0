package com.bizsoft.fmcgv2.Tables;

/**
 * Created by GopiKing on 28-02-2018.
 */

public class SalesReturnDetails {

    Long Id;
    Long SRId;
    Long SDId;
    int ProductId;
    int UOMId;
    float Quantity;
    Double UnitPrice;
    Double DiscountAmount;
    Double GSTAmount;
    Double Amount;
    boolean IsResale;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getSRId() {
        return SRId;
    }

    public void setSRId(Long SRId) {
        this.SRId = SRId;
    }

    public Long getSDId() {
        return SDId;
    }

    public void setSDId(Long SDId) {
        this.SDId = SDId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getUOMId() {
        return UOMId;
    }

    public void setUOMId(int UOMId) {
        this.UOMId = UOMId;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        UnitPrice = unitPrice;
    }

    public Double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public Double getGSTAmount() {
        return GSTAmount;
    }

    public void setGSTAmount(Double GSTAmount) {
        this.GSTAmount = GSTAmount;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public boolean isResale() {
        return IsResale;
    }

    public void setResale(boolean resale) {
        IsResale = resale;
    }
}
