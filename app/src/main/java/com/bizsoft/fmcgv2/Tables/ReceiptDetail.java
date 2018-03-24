package com.bizsoft.fmcgv2.Tables;

/**
 * Created by GopiKing on 01-03-2018.
 */

public  class ReceiptDetail {
     Long Id;
            Long  ReceiptId;
            Long  LedgerId;
             Double Amount;
            String Particulars;
            Long RefLedgerId;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getReceiptId() {
        return ReceiptId;
    }

    public void setReceiptId(Long receiptId) {
        ReceiptId = receiptId;
    }

    public Long getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(Long ledgerId) {
        LedgerId = ledgerId;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public Long getRefLedgerId() {
        return RefLedgerId;
    }

    public void setRefLedgerId(Long refLedgerId) {
        RefLedgerId = refLedgerId;
    }
}
