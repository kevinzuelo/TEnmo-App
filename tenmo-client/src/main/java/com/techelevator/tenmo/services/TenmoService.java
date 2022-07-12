package com.techelevator.tenmo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.ErrorMessage;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import io.cucumber.java.sl.In;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpServerErrorException;
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

    public Transfer sendMoney(Transfer transfer) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        Transfer newTransfer = null;

        try {
           newTransfer = restTemplate.postForObject(API_BASE_URL  + "/send", entity, Transfer.class);
        }
        catch (HttpServerErrorException.InternalServerError e ) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                ErrorMessage errorResponseBody = mapper.readValue(e.getResponseBodyAsString(),
                        ErrorMessage.class);
                System.out.println(errorResponseBody.getMessage());
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }

        }
        catch ( RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return newTransfer;
    }

    public Transfer requestMoney(Transfer transfer) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        Transfer newTransfer = null;
        try {
            newTransfer = restTemplate.postForObject(API_BASE_URL  + "/request", entity, Transfer.class);

        }
        catch (HttpServerErrorException.InternalServerError e ) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ErrorMessage errorResponseBody = mapper.readValue(e.getResponseBodyAsString(),
                        ErrorMessage.class);
                System.out.println(errorResponseBody.getMessage());
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }

        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newTransfer;
    }

    public void updateTransfer(Transfer transfer, int transferStatusID) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        try {
            restTemplate.put(API_BASE_URL  + "/update/" + transferStatusID, entity);
        }
        catch (HttpServerErrorException.InternalServerError e ) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                ErrorMessage errorResponseBody = mapper.readValue(e.getResponseBodyAsString(),
                        ErrorMessage.class);
                System.out.println(errorResponseBody.getMessage());
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public Transfer getTransferByID(long transferID) {
        try {
            return restTemplate.getForObject(API_BASE_URL  + "/transfer/" + transferID, Transfer.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
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

    public String getUserNameFromAccountId(int userId) {
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
            ID = restTemplate.getForObject(API_BASE_URL + "account/" + accountID, Integer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return ID;
    }

    public int getAccountIDFromUserID(long userID) {
        int ID = 0;
        try {
            ID = restTemplate.getForObject(API_BASE_URL + "users/account/" + userID, Integer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return ID;
    }



}
