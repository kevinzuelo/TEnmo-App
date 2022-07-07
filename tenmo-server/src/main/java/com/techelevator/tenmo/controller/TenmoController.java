package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
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
    public BigDecimal getBalance(@PathVariable int id) {

        return accountDAO.getBalance(id);
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/{id}/past_transfers", method = RequestMethod.GET)
    public List<Transfer> getPastTransfers(@PathVariable int id) {

        return transferDao.listTransfers(id);
    }

    @RequestMapping(path = "/{id}/pending_requests", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int id) {
        List<Transfer> transfers = new ArrayList<>();

        return transfers;

    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public void sendMoney(@Valid @RequestBody Transfer transfer) throws InvalidAccountException, InsufficientFundsException {
        transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/{from}/request/{to}", method = RequestMethod.POST)
    public void requestMoney(@PathVariable int from, @PathVariable int to) {

    }


}
