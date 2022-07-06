package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.RowSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransfer(int transferId) {

        Transfer transfer = null;

        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> listTransfers(int id) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "JOIN account ON transfer.account_from = account.account_id AND transfer.account_to = account.account_id " +
                "WHERE account.account_id = ?; ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }



    @Override
    public Transfer createTransfer(Transfer transfer){

        // create a new transfer w/ unique id in transfer table
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getTransferAmount());

        // update "sending" account balance to subtract amount to transfer
        sql = "UPDATE account " +
                "SET balance = balance - (SELECT amount FROM transfer WHERE transfer_id = ?) " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferId, transfer.getFromAccountId());

        //update "receiving" account balance to add amount to transfer
        sql = "UPDATE account " +
                "SET balance = balance + (SELECT amount FROM transfer WHERE transfer_id = ?) " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferId, transfer.getToAccountId());

        return getTransfer(transferId);
    }



    @Override
    public String getTransferStatus(int transferId){

        String sql = "SELECT transfer_status_desc " +
                "FROM transfer_status " +
                "WHERE transfer_status_id = ?;";

        return jdbcTemplate.queryForObject(sql, String.class, transferId);

    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {

        Transfer transfer = new Transfer();

        transfer.setId(rs.getLong("transfer_id"));
        transfer.setToAccountId(rs.getInt("account_to"));
        transfer.setFromAccountId(rs.getInt("account_from"));
        transfer.setTransferAmount(rs.getBigDecimal("amount"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        return transfer;
    }

}
