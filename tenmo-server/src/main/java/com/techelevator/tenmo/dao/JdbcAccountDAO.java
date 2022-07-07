package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT * FROM account " +
                "WHERE account.user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if(result.next()) {
           return mapRowToAccount(result).getBalance();
        }

        return null;
    }

    @Override
    public int getAccountId(int fromAccountID) {
        int accountID = 0 ;
        String sql = "SELECT * FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, fromAccountID);
        return mapRowToAccount(result).getAccountID();

    }

    @Override
    public int getUserId(int fromAccountID) {
        int userID = 0;
        String sql = "SELECT * FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, fromAccountID);
        return mapRowToAccount(result).getUserID();
    }

    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountID(result.getInt("account_id"));
        account.setUserID(result.getInt("user_id"));
        return account;
    }


}
