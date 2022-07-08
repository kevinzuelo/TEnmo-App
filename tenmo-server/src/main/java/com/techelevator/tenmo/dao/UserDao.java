package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import exceptions.IdNotFoundException;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

//    String findUsernameById(Long id) throws IdNotFoundException;

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
