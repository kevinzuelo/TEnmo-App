package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {

    public List<Account> findAll();

    public BigDecimal getBalance(int id);

    public int getUserId(int fromAccountID);

    public int getAccountId(int userID);
}
