package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TenmoService tenmoService = new TenmoService();

    private AuthenticatedUser currentUser;



    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 6) {
                listUsers();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println();
        System.out.println("Your current account balance is: " + tenmoService.getBalance(currentUser.getUser().getId()));
	}

    private void listUsers() {
        System.out.println("-------------------------------------------\n" + "Users\n" + "ID     Name\n" + "-------------------------------------------");
        for (User user : tenmoService.listAllUsers()) {
            System.out.println(user.getId() + "    " + user.getUsername());
        }
        System.out.println("-------------------------------------------\n");
    }

	private void viewTransferHistory() {
        System.out.println("-------------------------------------------\n" + "Transfers\n" +"ID     From/To     Amount\n" + "-------------------------------------------");
        for (Transfer transfer : tenmoService.listTransferHistory(currentUser.getUser().getId())) {
            if (transfer.getTransferStatusId() != 1) {
                if(currentUser.getUser().getId() == tenmoService.getUserIDFromAccount(transfer.getFromAccountId())) {
                    System.out.println(transfer.getId() + "   " + "To: " + tenmoService.getUserNameFromId(transfer.getToAccountId()) + "      " +  transfer.getTransferAmount());

                }
                else {
                    System.out.println(transfer.getId() + "   " + "From: " + tenmoService.getUserNameFromId(transfer.getFromAccountId()) + "          " + transfer.getTransferAmount());
                }
            }


        }
        System.out.println("---------\n");
		
	}

	private void viewPendingRequests() {
        System.out.println("-------------------------------------------\n" + "Transfers\n" +"ID     From/To     Amount\n" + "-------------------------------------------");
        for (Transfer transfer : tenmoService.listTransferHistory(currentUser.getUser().getId())) {
            if (transfer.getTransferStatusId() == 1) {
                if(currentUser.getUser().getId() == tenmoService.getUserIDFromAccount(transfer.getFromAccountId())) {
                    System.out.println(transfer.getId() + "   " + "From: " + tenmoService.getUserNameFromId(transfer.getToAccountId()) + "      " +  transfer.getTransferAmount());

                }
                else {
                    System.out.println(transfer.getId() + "   " + "To: " + tenmoService.getUserNameFromId(transfer.getFromAccountId()) + "          " + transfer.getTransferAmount());
                }
            }
            else if (transfer.getTransferStatusId() != 1){
                continue;
            }
            else {
                System.out.println("You have no pending transfers.");
            }

        }
        int pendingIDChoice = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel).");
        consoleService.printAcceptMenu();
        int acceptTransferChoice = consoleService.promptForInt("Please choose an option: ");

        if(acceptTransferChoice == 1) {
            tenmoService.updateTransfer(tenmoService.getTransferByID(pendingIDChoice));
        }
		
	}

	private void sendBucks() {
        listUsers();

        int toUserID = consoleService.promptForInt("Please Enter a User ID");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount you would like to send");
        int fromAccount = tenmoService.getAccountIDFromUserID(currentUser.getUser().getId());
        int toAccount = tenmoService.getAccountIDFromUserID(toUserID);

        Transfer transfer = new Transfer(toAccount, fromAccount, amount, 2);
        transfer.setTransferStatusId(2);
        tenmoService.sendMoney(transfer);
	}

	private void requestBucks() {
        listUsers();

        int toUserID = consoleService.promptForInt("Please Enter a User ID");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount you would like to request");
        int fromAccount = tenmoService.getAccountIDFromUserID(currentUser.getUser().getId());
        int toAccount = tenmoService.getAccountIDFromUserID(toUserID);

        Transfer transfer = new Transfer(toAccount, fromAccount, amount, 1);
        transfer.setTransferStatusId(1);
        tenmoService.requestMoney(transfer);
		
	}

}
