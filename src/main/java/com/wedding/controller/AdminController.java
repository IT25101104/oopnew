package com.wedding.controller;

import com.wedding.model.Client;
import com.wedding.util.ClientManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model, 
                                 @RequestParam(value = "success", required = false) String success) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) {
            return "redirect:/login";
        }
        
        try {
            List<Client> clients = ClientManager.getAllClients();
            model.addAttribute("clients", clients);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (success != null) {
            model.addAttribute("success", success);
        }
        
        return "admin_dashboard";
    }

    @GetMapping("/client/new")
    public String newClient(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) return "redirect:/login";
        return "admin_client_form";
    }

    @PostMapping("/client/create")
    public String createClient(@RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String partnerName,
                               @RequestParam String weddingDate,
                               @RequestParam double budget,
                               @RequestParam String phoneNumber,
                               @RequestParam String weddingLocation,
                               @RequestParam int guestCount,
                               HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) return "redirect:/login";
        
        try {
            String userId = java.util.UUID.randomUUID().toString();
            Client client = new Client(userId, email, password, "Client", partnerName, weddingDate, budget, phoneNumber, weddingLocation, guestCount);
            ClientManager.saveClient(client);
            return "redirect:/admin/dashboard?success=Client created successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?error=Error creating client";
        }
    }

    @GetMapping("/client/view")
    public String viewClient(@RequestParam String userId, HttpSession session, Model model) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) return "redirect:/login";
        
        try {
            Client client = ClientManager.getClientById(userId);
            if (client != null) {
                model.addAttribute("client", client);
                return "admin_client_view";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard?error=Client not found";
    }

    @GetMapping("/client/edit")
    public String editClient(@RequestParam String userId, HttpSession session, Model model) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) return "redirect:/login";
        
        try {
            Client client = ClientManager.getClientById(userId);
            if (client != null) {
                model.addAttribute("client", client);
                return "admin_client_form";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard?error=Client not found";
    }

    @PostMapping("/client/update")
    public String updateClient(@RequestParam String userId,
                               @RequestParam String partnerName,
                               @RequestParam String weddingDate,
                               @RequestParam double budget,
                               @RequestParam String phoneNumber,
                               @RequestParam String weddingLocation,
                               @RequestParam int guestCount,
                               HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) return "redirect:/login";
        
        try {
            Client client = ClientManager.getClientById(userId);
            if (client != null) {
                client.updateWeddingDetails(weddingDate, budget, weddingLocation, guestCount);
                client.setPartnerName(partnerName);
                client.setPhoneNumber(phoneNumber);
                ClientManager.updateClient(client);
                return "redirect:/admin/dashboard?success=Client updated successfully";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard?error=Error updating client";
    }

    @PostMapping("/delete")
    public String deleteClient(@RequestParam String userId, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) {
            return "redirect:/login";
        }
        
        try {
            ClientManager.deleteClient(userId);
            return "redirect:/admin/dashboard?success=Client account successfully deleted";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?error=Failed to delete client";
        }
    }
}
