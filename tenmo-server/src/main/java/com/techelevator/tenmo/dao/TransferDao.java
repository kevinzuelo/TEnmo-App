package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int id);

    Transfer createTransfer(Transfer transfer);

    String getTransferStatus(int id);
}
