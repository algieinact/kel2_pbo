package com.k2pbo.tubespbo.DAO;

import com.k2pbo.tubespbo.Contact;
import com.k2pbo.tubespbo.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private Connection connection;

    public ContactDao() {
        this.connection = DatabaseConnection.getConnection();
    }

    // CREATE: Menambahkan kontak baru
    public boolean addContact(Contact contact) {
        String sql = "INSERT INTO contacts (name, phone, email, address, category, favorite, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhone());
            statement.setString(3, contact.getEmail());
            statement.setString(4, contact.getAddress());
            statement.setString(5, contact.getCategory().name());
            statement.setBoolean(6, contact.isFavorite());
            statement.setString(7, contact.getNotes());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // READ: Mengambil semua kontak
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Contact contact = new Contact(
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("address"),
                        Contact.Category.valueOf(resultSet.getString("category"))
                );
                contact.setFavorite(resultSet.getBoolean("favorite"));
                contact.setNotes(resultSet.getString("notes"));
                contacts.add(contact);
                contact.setId(resultSet.getInt("id"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    // UPDATE: Memperbarui kontak berdasarkan ID
    public boolean updateContact(int id, Contact contact) {
        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ?, address = ?, category = ?, favorite = ?, notes = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhone());
            statement.setString(3, contact.getEmail());
            statement.setString(4, contact.getAddress());
            statement.setString(5, contact.getCategory().name());
            statement.setBoolean(6, contact.isFavorite());
            statement.setString(7, contact.getNotes());
            statement.setInt(8, id);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE: Menghapus kontak berdasarkan ID
public boolean deleteContact(int id) {
    String sql = "DELETE FROM contacts WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0; // Berhasil jika ada baris yang dihapus
    } catch (SQLException e) {
        System.err.println("Error deleting contact: " + e.getMessage());
        return false; // Gagal menghapus
    }
}

}
