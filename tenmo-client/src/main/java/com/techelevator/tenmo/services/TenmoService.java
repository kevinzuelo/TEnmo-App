package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import io.cucumber.java.sl.In;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TransferQueue;


public class TenmoService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();


    public User[] listAllUsers() {
        User[] users = null;
        try {
            users = restTemplate.getForObject(API_BASE_URL + "users", User[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
            return users;
    }

    public BigDecimal getBalance(Long id) {
        BigDecimal balance = null;
        try {
            balance = restTemplate.getForObject(API_BASE_URL + id + "/balance", BigDecimal.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    public Transfer[] listTransferHistory (Long id) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.getForObject(API_BASE_URL + id + "/past_transfers", Transfer[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public String getUserNameFromId(int userId) {
        String username = "";
        try {
            username = restTemplate.getForObject(API_BASE_URL + "users/" + userId, String.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    public int getUserIDFromAccount(int accountID) {
        int ID = 0;
        try {
            ID = restTemplate.getForObject(API_BASE_URL + "account/" + accountID, int.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return ID;
    }

}
