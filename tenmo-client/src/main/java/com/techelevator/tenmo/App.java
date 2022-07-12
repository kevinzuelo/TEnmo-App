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
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%36s", "Users\n\n");
        System.out.printf("%25s %15s", "ID", "Name\n");
        System.out.println("---------------------------------------------------------------");

        for (User user : tenmoService.listAllUsers()) {
            if(user.getUsername().equals(currentUser.getUser().getUsername())) {
                System.out.print("");
            } else {
                System.out.printf("%25d %15s", user.getId(), user.getUsername());
                System.out.println();
            }
        }
        System.out.println("---------------------------------------------------------------");
    }

	private void viewTransferHistory() {
        String sent = "Sent to: ";
        String received = "Received from: ";


        System.out.println("---------------------------------------------------------------");
        System.out.printf("%36s", "Transfers\n\n");
        System.out.printf("%-10s %16s %-12s $%-10s %10s", "ID", "From/To", "User", "Amount", "Status\n");
        System.out.println("---------------------------------------------------------------");

        for (Transfer transfer : tenmoService.listTransferHistory(currentUser.getUser().getId())) {
            if (transfer.getTransferStatusId() != 1) {

                if(currentUser.getUser().getId() == tenmoService.getUserIDFromAccount(transfer.getFromAccountId())) {
                    System.out.printf("%-10d %16s %-12s $%-10.2f %10s", transfer.getId(), sent,
                            tenmoService.getUserNameFromAccountId(transfer.getToAccountId()),
                            transfer.getTransferAmount(), transfer.printStatusName(transfer));
                            System.out.println();

                }
                else {
                    System.out.printf("%-10d %16s %-12s $%-10.2f %10s", transfer.getId(), received,
                            tenmoService.getUserNameFromAccountId(transfer.getFromAccountId()),
                            transfer.getTransferAmount(), transfer.printStatusName(transfer));
                            System.out.println();
                }
            }
        }
        System.out.println("---------------------------------------------------------------");

        int viewTransferChoice = consoleService.promptForInt("\nPlease enter transfer ID to view details (0 to cancel): ");
        for (Transfer transfer : tenmoService.listTransferHistory(currentUser.getUser().getId())) {
            String fromUsername = tenmoService.getUserNameFromAccountId(transfer.getFromAccountId());
            String toUsername = tenmoService.getUserNameFromAccountId(transfer.getToAccountId());
            if (viewTransferChoice == transfer.getId()){
                System.out.println("-------------------------\n       Transfer Details       \n-------------------------");
                System.out.println("ID: " + transfer.getId());
                System.out.println("From: " + fromUsername);
                System.out.println("To: " + toUsername);
                System.out.println("Type: " + transfer.printTypeName(transfer));
                System.out.println("Status: " + transfer.printStatusName(transfer));
                System.out.println("Amount: $" + transfer.getTransferAmount());
            }
        }
	}

	private void viewPendingRequests() {
        String sent = "Sent to: ";
        String received = "Received from: ";

        System.out.println("---------------------------------------------------------------");
        System.out.printf("%42s", "Pending Transfers\n\n");
        System.out.printf("%-10s %16s %-12s $%-10s %10s", "ID", "From/To", "User", "Amount", "Status\n");
        System.out.println("---------------------------------------------------------------");
        for (Transfer transfer : tenmoService.listTransferHistory(currentUser.getUser().getId())) {
            if (transfer.getTransferStatusId() == 1) {
                if(currentUser.getUser().getId() == tenmoService.getUserIDFromAccount(transfer.getFromAccountId())) {

                    System.out.printf("%-10d %16s %-12s $%-10.2f %10s", transfer.getId(), sent,
                            tenmoService.getUserNameFromAccountId(transfer.getToAccountId()),
                            transfer.getTransferAmount(), transfer.printStatusName(transfer));
                            System.out.println();
                }
                else {
                    System.out.printf("%-10d %16s %-12s $%-10.2f %10s", transfer.getId(), received,
                            tenmoService.getUserNameFromAccountId(transfer.getFromAccountId()),
                            transfer.getTransferAmount(), transfer.printStatusName(transfer));
                            System.out.println();
                }
            }
            else if (transfer.getTransferStatusId() != 1){
                continue;
            }
            else {
                System.out.println("You have no pending transfers.");
            }

        }
        int pendingIDChoice = consoleService.promptForInt("\nPlease enter transfer ID to approve/reject (0 to cancel): ");
        if(pendingIDChoice != 0) {
            consoleService.printAcceptMenu();
            int acceptTransferChoice = consoleService.promptForInt("Please choose an option: ");

            if(currentUser.getUser().getId() != tenmoService.getUserIDFromAccount(tenmoService.getTransferByID(pendingIDChoice).getFromAccountId())) {
                if(acceptTransferChoice == 1) {
                    tenmoService.updateTransfer(tenmoService.getTransferByID(pendingIDChoice), 2);
                }
                else if(acceptTransferChoice == 2) {
                    tenmoService.updateTransfer(tenmoService.getTransferByID(pendingIDChoice), 3);
                }
            }
            else {
                System.out.println("You don't have permission to do this.");
            }
        }
	}

	private void sendBucks() {
        listUsers();

        int toUserID = consoleService.promptForInt("Please Enter a User ID: ");
        if(toUserID == currentUser.getUser().getId()) {
            toUserID = consoleService.promptForInt("Please Enter a User ID other than your own: ");
        }
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount you would like to send: ");
        int fromAccount = tenmoService.getAccountIDFromUserID(currentUser.getUser().getId());
        int toAccount = tenmoService.getAccountIDFromUserID(toUserID);

        Transfer transfer = new Transfer(toAccount, fromAccount, amount, 2);
        transfer.setTransferStatusId(2);
        tenmoService.sendMoney(transfer);
	}

	private void requestBucks() {
        listUsers();

        int toUserID = consoleService.promptForInt("Please Enter a User ID: ");
        if(toUserID == currentUser.getUser().getId()) {
            toUserID = consoleService.promptForInt("Please Enter a User ID other than your own: ");
        }
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount you would like to request: ");
        int fromAccount = tenmoService.getAccountIDFromUserID(currentUser.getUser().getId());
        int toAccount = tenmoService.getAccountIDFromUserID(toUserID);

        Transfer transfer = new Transfer(toAccount, fromAccount, amount, 1);
        transfer.setTransferStatusId(1);
        tenmoService.requestMoney(transfer);
		
	}

}
