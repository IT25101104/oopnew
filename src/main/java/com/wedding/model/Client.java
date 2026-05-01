package com.wedding.model;

public class Client extends User {
    private String partnerName;
    private String weddingDate;
    private double budget;
    private String phoneNumber;
    private String weddingLocation;
    private int guestCount;

    public Client() {
        super();
    }

    public Client(String userId, String email, String password, String userType, String partnerName, String weddingDate, double budget, String phoneNumber, String weddingLocation, int guestCount) {
        super(userId, email, password, userType);
        this.partnerName = partnerName;
        this.weddingDate = weddingDate;
        this.budget = budget;
        this.phoneNumber = phoneNumber;
        this.weddingLocation = weddingLocation;
        this.guestCount = guestCount;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(String weddingDate) {
        this.weddingDate = weddingDate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWeddingLocation() {
        return weddingLocation;
    }

    public void setWeddingLocation(String weddingLocation) {
        this.weddingLocation = weddingLocation;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    // Specific methods for updating wedding details
    public void updateWeddingDetails(String newDate, double newBudget, String newLocation, int newGuestCount) {
        this.weddingDate = newDate;
        this.budget = newBudget;
        this.weddingLocation = newLocation;
        this.guestCount = newGuestCount;
    }
    
    // To format for text file
    @Override
    public String toString() {
        return getUserId() + "," + getEmail() + "," + getPassword() + "," + getUserType() + "," + partnerName + "," + weddingDate + "," + budget + "," + phoneNumber + "," + weddingLocation + "," + guestCount;
    }
}
