package com.k2pbo.tubespbo;

/**
 * Kelas Contact merepresentasikan data kontak dalam aplikasi
 * Menyimpan informasi seperti nama, nomor telepon, email, dll
 */
public class Contact {
    /** Enum untuk kategori kontak */
    public enum Category { PERSONAL, WORK, FAMILY, FRIENDS }
    
    /** Properties untuk menyimpan data kontak */
    private String name;
    private int id;
    private String phone;
    private String email;
    private String address;
    private String notes;
    private Category category;
    private boolean favorite;

    /**
     * Constructor untuk membuat objek Contact baru
     * @param name Nama kontak
     * @param phone Nomor telepon
     * @param email Alamat email
     * @param address Alamat
     * @param category Kategori kontak
     */
    public Contact(String name, String phone, String email, String address, Category category) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.category = category;
        this.favorite = false;
        this.notes = "";
    }

    // Getter dan Setter methods
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Override toString untuk menampilkan nama kontak
     * Digunakan untuk tampilan di combo box
     */
    @Override
    public String toString() {
        return name; // This helps with display in combo boxes
    }
}
