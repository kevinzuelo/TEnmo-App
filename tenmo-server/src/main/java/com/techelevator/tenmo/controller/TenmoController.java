package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountException;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TenmoController {

    private AccountDAO accountDAO;
    private TransferDao transferDao;
    private UserDao userDao;

    public TenmoController(AccountDAO accountDAO, TransferDao transferDao, UserDao userDao) {
        this.accountDAO = accountDAO;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable("id") int userId) {

        return accountDAO.getBalance(userId);
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferByID (@PathVariable("id") int transferID) {
        return transferDao.getTransfer(transferID);
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public String getUserName(@PathVariable("id") int fromID) {
        List<Account> accountList = accountDAO.findAll();
        List<User> userList = userDao.findAll();
        int from = 0;
        for(Account account : accountList) {
            if(account.getAccountID() == fromID){
                from = account.getUserID();
            }
        }

        for(User user : userList) {
            if(user.getId() == from) {
                return user.getUsername();
            }
        }
        return null;
    }

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public int getUserIDByAccountID (@PathVariable("id") int accountID) {
        return accountDAO.getUserId(accountID);
    }

    @RequestMapping(path = "users/account/{id}", method = RequestMethod.GET)
    public int getAccountIDByUserID (@PathVariable("id") int userID) {
        return accountDAO.getAccountId(userID);
    }

    @RequestMapping(path = "/{id}/past_transfers", method = RequestMethod.GET)
    public List<Transfer> getPastTransfers(@PathVariable("id") int userId) {

        return transferDao.listTransfers(userId);
    }

    @RequestMapping(path = "/{id}/pending_requests", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable("id") int userId) {
        List<Transfer> transfers = new ArrayList<>();

        return transfers;
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public void sendMoney(@Valid @RequestBody Transfer transfer) throws InvalidAccountException, InsufficientFundsException {
        transfer.setTransferStatusId(2);
        transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public void requestMoney(@Valid @RequestBody Transfer transfer) throws InvalidAccountException, InsufficientFundsException {
        transfer.setTransferStatusId(1);
        transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    public void updateTransfer(@Valid @RequestBody Transfer transfer, @PathVariable("id") int statusID) throws InvalidAccountException {
        transferDao.updateTransfer(transfer, statusID);
    }


}
