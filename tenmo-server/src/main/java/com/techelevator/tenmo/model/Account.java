package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;

    public Account(){};

    public Account(BigDecimal balance){
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
