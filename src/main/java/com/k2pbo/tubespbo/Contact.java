package com.k2pbo.tubespbo;

public class Contact {
    public enum Category { PERSONAL, WORK, FAMILY, FRIENDS }
    
    private String name;
    private String phone;
    private String email;
    private String address;
    private String notes;
    private Category category;
    private boolean favorite;

    public Contact(String name, String phone, String email, String address, Category category) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.category = category;
        this.favorite = false;
        this.notes = "";
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    @Override
    public String toString() {
        return name; // This helps with display in combo boxes
    }
}
