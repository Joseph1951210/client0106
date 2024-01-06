package client;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
public class ClientService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Jsonb jsonb = JsonbBuilder.create(); //  Jsonb 实例
    private BankService bankService;

    public ClientService() {
        BankServiceService bankServiceService = new BankServiceService();
        this.bankService = bankServiceService.getBankServicePort();
    }
    public Account getAccount(String accountId) throws BankServiceException_Exception {
        return bankService.getAccount(accountId);
    }

    // 创建账户
    public String createAccount(User user, BigDecimal balance) throws BankServiceException_Exception {
        return bankService.createAccountWithBalance(user, balance);
    }

    // 转账
    public void transferMoney(String debtorId, String creditorId, BigDecimal amount, String description) throws BankServiceException_Exception {
        bankService.transferMoneyFromTo(debtorId, creditorId, amount, description);
    }
    
    public String createUser(User user) {
        String url = "http://localhost:8080/users";
        String requestBody = jsonb.toJson(user); //
        return sendRequest(url, requestBody, "POST");
    }

    public String createTrade(Trade trade) {
        String url = "http://localhost:8080/trades";
        String requestBody = jsonb.toJson(trade); 
        return sendRequest(url, requestBody, "POST");
    }

    
    public String createMerchant(User merchant) {
        String url = "http://localhost:8080/merchants"; 
        String requestBody = jsonb.toJson(merchant); // 将商户对象转换为 JSON
        return sendRequest(url, requestBody, "POST");
    }

    public String sendRequest(String url, String requestBody, String requestType) {
        HttpRequest request;
        if ("POST".equals(requestType)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } else { // Assume GET for any other type
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
        }

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException e) {
            System.err.println("Error in network communication: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Request interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            if (e instanceof BankServiceException_Exception) {
                handleBankServiceException((BankServiceException_Exception) e);
            }
        }
        return null;
    }

    private void handleBankServiceException(BankServiceException_Exception e) {
        System.err.println("Bank service exception: " + e.getMessage());
    }

    public String createPayment(String paymentData) {
        String url = "http://localhost:8080/payments";
        return sendRequest(url, paymentData, "POST");
    }

    public String getPayments() {
        String url = "http://localhost:8080/payments";
        return sendRequest(url, "", "GET");
    }

}
class Trade {
    private String customerBankAccount;
    private String merchantBankAccount;
    private Double amount;

    public Trade() {}

    public Trade(String customerBankAccount, String merchantBankAccount, Double amount) {
        this.customerBankAccount = customerBankAccount;
        this.merchantBankAccount = merchantBankAccount;
        this.amount = amount;
    }

    // getter 和 setter 方法
    public String getCustomerBankAccount() {
        return customerBankAccount;
    }

    public void setCustomerBankAccount(String customerBankAccount) {
        this.customerBankAccount = customerBankAccount;
    }

    public String getMerchantBankAccount() {
        return merchantBankAccount;
    }

    public void setMerchantBankAccount(String merchantBankAccount) {
        this.merchantBankAccount = merchantBankAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
