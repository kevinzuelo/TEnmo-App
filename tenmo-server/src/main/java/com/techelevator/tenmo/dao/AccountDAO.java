package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {
    public BigDecimal getBalance(int id);
}
