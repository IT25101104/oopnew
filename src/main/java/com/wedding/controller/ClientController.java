package com.wedding.controller;

import com.wedding.model.Client;
import com.wedding.util.ClientManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

@Controller
public class ClientController {

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "success", required = false) String success,
                            Model model) {
        if (error != null) model.addAttribute("error", error);
        if (success != null) model.addAttribute("success", success);
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        if ("admin@wedding.com".equals(email) && "admin123".equals(password)) {
            session.setAttribute("admin", true);
            return "redirect:/admin/dashboard";
        }
        try {
            Client client = ClientManager.authenticate(email, password);
            if (client != null) {
                session.setAttribute("user", client);
                return "redirect:/dashboard";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/login?error=Invalid email or password";
    }

    @GetMapping("/register")
    public String showRegister(@RequestParam(value = "success", required = false) String success, Model model) {
        if (success != null) model.addAttribute("success", success);
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String partnerName,
                                  @RequestParam String weddingDate,
                                  @RequestParam double budget,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String weddingLocation,
                                  @RequestParam int guestCount) {
        try {
            String userId = UUID.randomUUID().toString();
            Client client = new Client(userId, email, password, "Client", partnerName, weddingDate, budget, phoneNumber, weddingLocation, guestCount);
            ClientManager.saveClient(client);
            return "redirect:/login?success=Registration successful. Please login.";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/register?error=Error during registration";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(value = "success", required = false) String success, HttpSession session, Model model) {
        Client user = (Client) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        if (success != null) model.addAttribute("success", success);
        return "dashboard";
    }

    @GetMapping("/edit-profile")
    public String showEditProfile(HttpSession session, Model model) {
        Client user = (Client) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @PostMapping("/update")
    public String processUpdate(@RequestParam String partnerName,
                                @RequestParam String weddingDate,
                                @RequestParam double budget,
                                @RequestParam String phoneNumber,
                                @RequestParam String weddingLocation,
                                @RequestParam int guestCount,
                                HttpSession session) {
        Client client = (Client) session.getAttribute("user");
        if (client != null) {
            try {
                client.updateWeddingDetails(weddingDate, budget, weddingLocation, guestCount);
                client.setPartnerName(partnerName);
                client.setPhoneNumber(phoneNumber);
                ClientManager.updateClient(client);
                session.setAttribute("user", client);
                return "redirect:/dashboard?success=Profile updated successfully";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String processDelete(HttpSession session) {
        Client client = (Client) session.getAttribute("user");
        if (client != null) {
            try {
                ClientManager.deleteClient(client.getUserId());
                session.invalidate();
                return "redirect:/register?success=Account cancelled successfully";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
