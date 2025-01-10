package com.k2pbo.tubespbo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.k2pbo.tubespbo.Contact;
import com.k2pbo.tubespbo.DatabaseConnection;

public class ContactDao {
    // Hapus field connection karena kita akan menggunakan koneksi fresh setiap kali diperlukan
    
    public ContactDao() {
        // Kosongkan constructor karena tidak perlu menyimpan koneksi
    }

    // CREATE: Menambahkan kontak baru
    public boolean addContact(Contact contact) {
        String sql = "INSERT INTO contacts (name, phone, email, address, category, favorite, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getPhone());
            stmt.setString(3, contact.getEmail());
            stmt.setString(4, contact.getAddress());
            stmt.setString(5, contact.getCategory().toString());
            stmt.setBoolean(6, contact.isFavorite());
            stmt.setString(7, contact.getNotes());
    
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contact.setId(generatedKeys.getInt(1));  // Set ID yang baru
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // READ: Mengambil semua kontak
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Contact contact = new Contact(
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getString("address"),
                    Contact.Category.valueOf(resultSet.getString("category"))
                );
                contact.setId(resultSet.getInt("id"));
                contact.setFavorite(resultSet.getBoolean("favorite"));
                contact.setNotes(resultSet.getString("notes"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    // UPDATE: Memperbarui kontak berdasarkan ID
    public boolean updateContact(int id, Contact contact) {
        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ?, address = ?, category = ?, favorite = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getPhone());
            stmt.setString(3, contact.getEmail());
            stmt.setString(4, contact.getAddress());
            stmt.setString(5, contact.getCategory().toString());
            stmt.setBoolean(6, contact.isFavorite());
            stmt.setString(7, contact.getNotes());
            stmt.setInt(8, id);  // Gunakan ID yang sudah ada di objek contact
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Jika ada baris yang terpengaruh, return true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Jika gagal
    }

    // DELETE: Menghapus kontak berdasarkan ID
    public boolean deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Mengembalikan true jika penghapusan berhasil
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}