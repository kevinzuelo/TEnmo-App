package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;


    @Override
    public BigDecimal getBalance(int id) {
        String sql = "SELECT balance FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if(result.next()) {
           return mapRowToAccount(result).getBalance();
        }

        return null;
    }

    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }
}
