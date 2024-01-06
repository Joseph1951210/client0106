
import java.util.ArrayList;
import java.util.List;

import client.ClientService;
import dtu.ws.fastmoney.User;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;


public class UserClientService {
    private ClientService clientService;
    private Jsonb jsonb = JsonbBuilder.create();

    public UserClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public String registerUser(User user) {
        String url = "http://localhost:8080/users";
        String json = jsonb.toJson(user);
        return clientService.sendRequest(url, json, "POST");
    }
    
    public User getUser(String userId) {
        String url = "http://localhost:8080/users/" + userId;
        String response = clientService.sendRequest(url, "", "GET");
        return jsonb.fromJson(response, User.class);
    }

    /*public String updateUser(User user) {
        String url = "http://localhost:8080/users/" + user.getId();
        String json = jsonb.toJson(user);
        return clientService.sendRequest(url, json, "PUT");
    }*/

    public String deleteUser(String userId) {
        String url = "http://localhost:8080/users/" + userId;
        return clientService.sendRequest(url, "", "DELETE");
    }

    public List<User> getAllUsers() {
        String url = "http://localhost:8080/users";
        String response = clientService.sendRequest(url, "", "GET");
        return jsonb.fromJson(response, new ArrayList<User>(){}.getClass().getGenericSuperclass());
    }

    
}
