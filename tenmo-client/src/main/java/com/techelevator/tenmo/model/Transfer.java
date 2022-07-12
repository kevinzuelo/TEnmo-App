package com.techelevator.tenmo.model;


import java.math.BigDecimal;

public class Transfer {

    private Long id;

    private BigDecimal transferAmount;
    private int toAccountId;
    private int fromAccountId;
    private int transferTypeId;
    private int transferStatusId = 2;
    private String message = "";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String printStatusName(Transfer transfer) {
        if(transfer.getTransferStatusId() == 1) {
            return ANSI_BLUE + "\033[3mPending\033[0m" + ANSI_RESET;
        }
        else if(transfer.getTransferStatusId() == 2) {
            return ANSI_GREEN + "Approved" + ANSI_RESET;
        }
        else {return ANSI_RED + "Rejected" + ANSI_RESET; }
    }

    public String printTypeName(Transfer transfer) {
        if(transfer.getTransferTypeId() == 1) {
            return "Request";
        }
        else {return "Send"; }
    }

}
