package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;



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


}
