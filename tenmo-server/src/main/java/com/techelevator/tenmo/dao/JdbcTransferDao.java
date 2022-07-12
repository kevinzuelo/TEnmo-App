package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountException;
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

    // Returns transfer by id value
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

    // Returns all transfers by user id
    @Override
    public List<Transfer> listTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * FROM transfer " +
        "JOIN account ON transfer.account_from = account.account_id " +
        "WHERE account_to = (SELECT account_id FROM account WHERE user_id = ?) " +
        "OR account_from = (SELECT account_id FROM account WHERE user_id = ?);";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }


    // Creates transfer
    @Override
    public Transfer createTransfer(Transfer transfer) throws InvalidAccountException, InsufficientFundsException {

         if (transfer.getFromAccountId() == transfer.getToAccountId()) {
             throw new InvalidAccountException();
         }


        if (transfer.getTransferTypeId() == 1 || validateFunds(transfer, transfer.getFromAccountId())) {
            // create a new transfer w/ unique id in transfer table
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

            Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(), transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getTransferAmount());

            if (transfer.getTransferStatusId() == 2) {
                updateFromAccount(transferId, transfer.getFromAccountId());
                updateToAccount(transferId, transfer.getToAccountId());
            }
            return getTransfer(transferId);
        }
        throw new InsufficientFundsException();



    }

    @Override
    public void updateTransfer(Transfer transfer, int statusID) throws InvalidAccountException, InsufficientFundsException {

        if (transfer.getFromAccountId() == transfer.getToAccountId()) {
            throw new InvalidAccountException();
        }

        if(statusID == 2 && validateFunds(transfer, transfer.getToAccountId())) {
            transfer.setTransferStatusId(2);
            transfer.setTransferTypeId(2);

            String sql = "UPDATE transfer SET transfer_type_id = ?, transfer_status_id = ? " +
                            "WHERE transfer_id = ?;";

            jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getId());

            updateFromAccount(transfer.getId(), transfer.getToAccountId());
            updateToAccount(transfer.getId(), transfer.getFromAccountId());

        }
        else if (statusID == 3) {
            transfer.setTransferStatusId(3);

            String sql = "UPDATE transfer SET transfer_status_id = ? " +
                    "WHERE transfer_id = ?;";

            jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getId());

        }
        else {
            throw new InsufficientFundsException();
        }
    }




    @Override
    public String getTransferStatus(int transferId){

        String sql = "SELECT transfer_status_desc " +
                "FROM transfer_status " +
                "WHERE transfer_status_id = ?;";

        return jdbcTemplate.queryForObject(sql, String.class, transferId);

    }

    public void updateToAccount(int transferID, int accountID) {
        //update "receiving" account balance to add amount to transfer
        String sql = "UPDATE account " +
                "SET balance = balance + (SELECT amount FROM transfer WHERE transfer_id = ?) " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferID, accountID);
    }

    public void updateFromAccount(int transferID, int accountID) {
        // update "sending" account balance to subtract amount to transfer
        String sql = "UPDATE account " +
                "SET balance = balance - (SELECT amount FROM transfer WHERE transfer_id = ?) " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferID, accountID);
    }

    public boolean validateFunds(Transfer transfer, int transferAccountID) {
        String sql = "SELECT DISTINCT balance " +
                "FROM account " +
                "WHERE account_id = ?;" ;

        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, transferAccountID);

        if(transfer.getTransferAmount().compareTo(balance) == 1) {
            return false;
        }
        return true;

    }

    public String getUserNameFromId(int transferId, String currentUser) {
        String sql = "SELECT username " +
                        "FROM tenmo_user " +
                        "JOIN account ON tenmo_user.user_id = account.user_id " +
                        "JOIN transfer ON account.account_id = transfer.account_from OR  account.account_id = transfer.account_to " +
                        "WHERE transfer_id = ? AND username <> ?;";
        return jdbcTemplate.queryForObject(sql, String.class, transferId, currentUser);
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {

        Transfer transfer = new Transfer();

        transfer.setId(rs.getInt("transfer_id"));
        transfer.setToAccountId(rs.getInt("account_to"));
        transfer.setFromAccountId(rs.getInt("account_from"));
        transfer.setTransferAmount(rs.getBigDecimal("amount"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        return transfer;
    }

}
