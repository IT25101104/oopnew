package com.wedding.model;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

    public class Booking {
        private String bookingID;
        private String clientName;
        private String vendorName;
        private String weddingDate;
        private String status = "Pending";

        // Default Constructor
        public Booking() {}

        // Getters and Setters


        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getBookingID() { return bookingID; }
        public void setBookingID(String bookingID) { this.bookingID = bookingID; }

        public String getClientName() { return clientName; }
        public void setClientName(String clientName) { this.clientName = clientName; }

        public String getVendorName() { return vendorName; }
        public void setVendorName(String vendorName) { this.vendorName = vendorName; }

        public String getWeddingDate() { return weddingDate; }
        public void setWeddingDate(String weddingDate) { this.weddingDate = weddingDate; }

        // Save logic
        public void saveToFile() {
            try {

                FileWriter fw = new FileWriter("bookings.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw);

                out.println(bookingID + "," + clientName + "," + vendorName + "," + weddingDate + ",Pending");

                out.close();
                bw.close();
                fw.close();

                System.out.println("Data saved to file successfully!");
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        }
    }


