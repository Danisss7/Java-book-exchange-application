package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.errors.ClientNotFound;
import com.example.lab2weblayer.model.Client;
import com.example.lab2weblayer.model.PasswordHasher;
import com.example.lab2weblayer.repos.ClientRepo;
import com.example.lab2weblayer.repos.UserRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ClientController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClientRepo clientRepo;

    @GetMapping(value = "/allClients")
    public @ResponseBody
    Iterable<Client> getAll() {
        return clientRepo.findAll();
    }

    @PostMapping(value = "createClient")
    public String createUser(@RequestBody Client client) {
        String salt = PasswordHasher.generateSalt();
        client.setSalt(salt);
        client.setPassword(PasswordHasher.hashPassword(client.getPassword(), salt));
        clientRepo.save(client);
        return "Client created successfully.";
    }

    @PostMapping(value = "validateClient")
    public Client validateClient(@RequestBody String loginInfo) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(loginInfo, Properties.class);
        String login = properties.getProperty("login");
        String psw = properties.getProperty("password");
        Client client = clientRepo.getClientByLogin(login);
        if (client != null && PasswordHasher.hashPassword(psw, client.getSalt()).equals(client.getPassword())) {
            return client;
        } else {
            return null;
        }
    }

    @PutMapping("updateLatestUser")
    public EntityModel<Client> updateLatestUser(@RequestBody Client updatedClient) {
        Client latestClient = clientRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No clients found"));

        latestClient.setAddress(updatedClient.getAddress());
        latestClient.setBirthDate(updatedClient.getBirthDate());
        latestClient.setClientBio(updatedClient.getClientBio());
        latestClient.setName(updatedClient.getName());
        latestClient.setSurname(updatedClient.getSurname());
        latestClient.setLogin(updatedClient.getLogin());

        if (!updatedClient.getPassword().equals(latestClient.getPassword())) {
            latestClient.setPassword(PasswordHasher.hashPassword(updatedClient.getPassword(), latestClient.getSalt()));
        }

        clientRepo.save(latestClient);

        return EntityModel.of(latestClient,
                linkTo(methodOn(ClientController.class).getClientById(latestClient.getId())).withSelfRel(),
                linkTo(methodOn(ClientController.class).getAll()).withRel("allUsers"));
    }

    @DeleteMapping(value = "deleteLatestClient")
    public String deleteLatestClient() {
        Client latestClient = clientRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No clients found"));

        clientRepo.deleteById(latestClient.getId());
        return "Latest client deleted successfully.";
    }

    @DeleteMapping(value = "deleteUser/{id}")
    @ResponseBody
    public String deleteClient(@PathVariable int id) {
        clientRepo.deleteById(id);
        return "Success";
    }

    @GetMapping("client/{id}")
    EntityModel<Client> getClientById(@PathVariable Integer id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ClientNotFound(id));

        return EntityModel.of(client,
                linkTo(methodOn(ClientController.class).getClientById(id)).withSelfRel(),
                linkTo(methodOn(ClientController.class).getAll()).withRel("allUsers"));
    }
}
