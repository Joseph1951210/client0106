package main.java.client;

import java.math.BigDecimal;

import client.ClientService;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;


public class PaymentSteps {
    private ClientService clientService;

    private String testCustomerId;
    private String testMerchantId;
    private double initialCustomerBalance;
    private double initialMerchantBalance;
    private Account testCustomerAccount;
    private Account testMerchantAccount;
    public PaymentSteps() {
        // 初始化客户端服务
        this.clientService = new ClientService();
        this.bankService = new BankServiceService().getBankServicePort();
    }

    @Given("a customer with a bank account with balance {double}")
    public void a_customer_with_a_bank_account_with_balance(Double balance) {
        
        User testCustomer = new User();
        
        testCustomerId = clientService.createUser(testCustomer);
        testCustomerAccount = bankService.createAccountWithBalance(testCustomer, new BigDecimal(balance));
        initialCustomerBalance = balance;
      
    }

    @Given("that the customer is registered with DTU Pay")
    public void that_the_customer_is_registered_with_dtu_pay() {

    }

    @Given("a merchant with a bank account with balance {double}")
    public void a_merchant_with_a_bank_account_with_balance(Double balance) {
        
        User testMerchant = new User();
        
        testMerchantId = clientService.createMerchant(testMerchant);
        initialMerchantBalance = balance;
        
    }

    @Given("that the merchant is registered with DTU Pay")
    public void that_the_merchant_is_registered_with_dtu_pay() {
        
    }

    @Then("the payment is successful")
    public void the_payment_is_successful() {

        BigDecimal amount = BigDecimal.valueOf(100); //instance
        clientService.transferMoney(testCustomerAccount.getId(), testMerchantAccount.getId(), amount, "Test Payment");
 
        
    }

    @Then("the balance of the customer at the bank is {double} kr")
    public void the_balance_of_the_customer_at_the_bank_is_kr(Double expectedBalance) {
        Account account = clientService.getAccount(testCustomerAccount.getId());
        assert account.getBalance().doubleValue() == expectedBalance;
        
    }

    @Then("the balance of the merchant at the bank is {double} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Double expectedBalance) {
        Account account = clientService.getAccount(testMerchantAccount.getId());
        assert account.getBalance().doubleValue() == expectedBalance;
    
        
    }
}
