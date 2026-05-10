package com.wedding.controller;

import com.wedding.model.Booking;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {


    private static List<Booking> bookingList = new ArrayList<>();

    public BookingController() {
        if (bookingList.isEmpty()) {
            loadBookingsFromFile();
        }
    }


    @GetMapping("/booking")
    public String showHomePage(Model model) {
        model.addAttribute("booking", new Booking());
        return "index";
    }


    @PostMapping("/save")
    public String saveBooking(@ModelAttribute Booking booking, Model model) {

        if (!isAvailable(booking.getVendorName(), booking.getWeddingDate())) {
            model.addAttribute("error", "Sorry! This vendor is already booked for the selected date.");
            model.addAttribute("booking", booking);
            return "index";
        }

        // දත්ත සුරැකීම
        booking.setStatus("Pending");
        booking.saveToFile();
        bookingList.add(booking);

        model.addAttribute("message", "Booking Request Sent Successfully!");
        model.addAttribute("booking", new Booking());
        return "index";
    }


    @GetMapping("/vendor-panel")
    public String showVendorPanel(Model model) {
        model.addAttribute("allBookings", bookingList);
        return "vendor-panel";
    }


    @GetMapping("/client-history")
    public String showClientHistory(Model model) {
        model.addAttribute("allBookings", bookingList);
        return "client-history";
    }


    @PostMapping("/update-status")
    public String updateStatus(@RequestParam String id) {
        for (Booking b : bookingList) {
            if (b.getBookingID().equals(id)) {
                b.setStatus("Confirmed");
                break;
            }
        }
        return "redirect:/vendor-panel";
    }


    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam String id) {
        bookingList.removeIf(b -> b.getBookingID().equals(id));

        return "redirect:/client-history";
    }

    // --- Helper Methods (Abstraction Applied) ---


    private boolean isAvailable(String vendor, String date) {
        for (Booking b : bookingList) {
            if (b.getVendorName().equals(vendor) && b.getWeddingDate().equals(date)) {
                return false;
            }
        }
        return true;
    }


    private void loadBookingsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    Booking b = new Booking();
                    b.setBookingID(data[0]);
                    b.setClientName(data[1]);
                    b.setVendorName(data[2]);
                    b.setWeddingDate(data[3]);
                    b.setStatus(data.length > 4 ? data[4] : "Pending");
                    bookingList.add(b);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing bookings found to load.");
        }
    }
}
