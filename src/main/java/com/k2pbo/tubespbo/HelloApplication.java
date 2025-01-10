package com.k2pbo.tubespbo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Kelas utama aplikasi Contact Manager
 * Menginisialisasi dan menampilkan landing page saat aplikasi pertama kali dijalankan
 */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Memuat file FXML landing page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("landing-page.fxml"));
            // Membuat scene baru dengan ukuran 900x600
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            // Mengatur judul window
            stage.setTitle("Contact Manager");
            // Memuat file CSS untuk styling
            scene.getStylesheets().add(getClass().getResource("styles-landing.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method main untuk menjalankan aplikasi
     */
    public static void main(String[] args) {
        launch();
    }
}