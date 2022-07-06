package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
public class TenmoController {

    private AccountDAO accountDAO;

    public TenmoController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    @RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {

        return accountDAO.getBalance(id);
    }

    @RequestMapping(path = "/{id}/past_transfers", method = RequestMethod.GET)
    public List<Transfer> getPastTransfers(@PathVariable int id) {
        List<Transfer> transfers = new ArrayList<>();

        return transfers;
    }

    @RequestMapping(path = "/{id}/pending_requests", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int id) {
        List<Transfer> transfers = new ArrayList<>();

        return transfers;

    }

    @RequestMapping(path = "/{from}/send/{to}", method = RequestMethod.POST)
    public void sendMoney(@PathVariable int from, @PathVariable int to) {

    }

    @RequestMapping(path = "/{from}/request/{to}", method = RequestMethod.POST)
    public void requestMoney(@PathVariable int from, @PathVariable int to) {

    }


}