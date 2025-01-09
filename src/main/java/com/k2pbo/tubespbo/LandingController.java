package com.k2pbo.tubespbo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LandingController {
    @FXML
    private void switchToContactView(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("contact-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
