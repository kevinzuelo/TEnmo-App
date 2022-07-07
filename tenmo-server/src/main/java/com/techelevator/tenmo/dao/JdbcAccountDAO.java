package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account.user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
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
