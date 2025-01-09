package com.k2pbo.tubespbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Ganti dengan URL, username, dan password sesuai dengan pengaturan database Anda
    private static final String URL = "jdbc:mysql://localhost:3306/contact_manager";
    private static final String USER = "root"; // Ganti dengan username Anda
    private static final String PASSWORD = ""; // Ganti dengan password Anda

    // Method untuk mendapatkan koneksi
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }

    // Menambahkan main method untuk test koneksi
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungkan ke database...");

        // Mendapatkan koneksi dengan method getConnection()
        try (Connection connection = getConnection()) {
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
