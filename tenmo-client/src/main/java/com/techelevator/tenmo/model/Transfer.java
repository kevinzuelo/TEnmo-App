package com.techelevator.tenmo.model;


import java.math.BigDecimal;

public class Transfer {

    private Long id;

    private BigDecimal transferAmount;
    private int toAccountId;
    private int fromAccountId;
    private int transferTypeId;
    private int transferStatusId = 2;

    public Transfer(){}

    public Transfer(int toAccountId, int fromAccountId, BigDecimal transferAmount, int transferTypeId) {
        this.toAccountId = toAccountId;
        this.fromAccountId = fromAccountId;
        this.transferAmount = transferAmount;
        this.transferTypeId = transferTypeId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

}
