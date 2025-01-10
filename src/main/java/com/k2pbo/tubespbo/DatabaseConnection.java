package com.k2pbo.tubespbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Kelas untuk menangani koneksi ke database
 * Menggunakan singleton pattern untuk memastikan hanya ada satu koneksi
 */
public class DatabaseConnection {
    /** Instance singleton untuk koneksi database */
    private static DatabaseConnection instance;
    private Connection connection;
    
    /** Konfigurasi database */
    private String url = "jdbc:mysql://localhost:3306/contact_manager";
    private String username = "root";
    private String password = "";

    /** Constructor private untuk singleton pattern */
    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }

    /** Method untuk mendapatkan instance koneksi */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /** Method untuk mendapatkan koneksi ke database */
    public Connection getConnection() {
        return connection;
    }

    // Menambahkan main method untuk test koneksi
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke database...");

        // Mendapatkan koneksi dengan method getConnection()
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            if (connection != null) {
                System.out.println("Koneksi ke database berhasil!");
            } else {
                System.out.println("Koneksi gagal.");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
}
