package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.model.Admin;
import com.example.lab2weblayer.model.PasswordHasher;
import com.example.lab2weblayer.repos.AdminRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Properties;


@RestController
@RequestMapping(path = "/admins")
public class AdminController {

    @Autowired
    private AdminRepo adminRepo;

    @GetMapping("/allAdmins")
    public Iterable<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }

    @GetMapping("/admin/{id}")
    public Admin getAdminById(@PathVariable Integer id) {
        return adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID " + id));
    }

    @PostMapping("/createAdmin")
    public String createAdmin(@RequestBody Admin admin) {
        admin.setSalt(PasswordHasher.generateSalt());
        admin.setPassword(PasswordHasher.hashPassword(admin.getPassword(), admin.getSalt()));
        adminRepo.save(admin);
        return "Admin created successfully.";
    }

    @PostMapping("/validateAdmin")
    public Admin validateAdmin(@RequestBody String loginInfo) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(loginInfo, Properties.class);
        String login = properties.getProperty("login");
        String psw = properties.getProperty("password");

        Admin admin = adminRepo.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Admin not found with login: " + login));

        String hashedPassword = PasswordHasher.hashPassword(psw, admin.getSalt());
        if (!hashedPassword.equals(admin.getPassword())) {
            throw new RuntimeException("Invalid login credentials");
        }
        return admin;
    }

    @PutMapping("/updateLatestAdmin")
    public Admin updateLatestAdmin(@RequestBody Admin updatedAdmin) {
        Admin latestAdmin = adminRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No admins found"));

        latestAdmin.setName(updatedAdmin.getName());
        latestAdmin.setSurname(updatedAdmin.getSurname());
        latestAdmin.setLogin(updatedAdmin.getLogin());
        latestAdmin.setPassword(PasswordHasher.hashPassword(updatedAdmin.getPassword(), latestAdmin.getSalt()));
        latestAdmin.setPhoneNum(updatedAdmin.getPhoneNum());

        adminRepo.save(latestAdmin);

        return latestAdmin;
    }

    @DeleteMapping("/deleteLatestAdmin")
    public String deleteLatestAdmin() {
        Admin latestAdmin = adminRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No admins found"));

        adminRepo.deleteById(latestAdmin.getId());

        return "Latest admin deleted successfully.";
    }
}
