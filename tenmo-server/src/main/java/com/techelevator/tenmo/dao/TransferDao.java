package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountException;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int id);

    List<Transfer> listTransfers(int id);

    Transfer createTransfer(Transfer transfer) throws InvalidAccountException, InsufficientFundsException;

    String getTransferStatus(int id);

    String getUserNameFromId(int transferId, String currentUser);
}
