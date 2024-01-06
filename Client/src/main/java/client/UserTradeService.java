
package main.java.client;

import client.ClientService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;


public class UserTradeService {
    private ClientService clientService;
    private Jsonb jsonb = JsonbBuilder.create();

    public UserTradeService(ClientService clientService) {
        this.clientService = clientService;
    }

    // public String createTrade(Trade trade) {
    //     String url = "http://localhost:8080/trades";
    //     String json = jsonb.toJson(trade);
    //     return clientService.sendRequest(url, json, "POST");
    // }
}
