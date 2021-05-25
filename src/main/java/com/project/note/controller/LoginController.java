package com.project.note.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.project.note.animations.Shaker;
import com.project.note.database.DatabaseHandler;
import com.project.note.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    private int userId;

    public static int currentUserId;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField loginUsername;

    @FXML
    private JFXPasswordField loginPassword;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton loginSignupButton;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() {

        databaseHandler = new DatabaseHandler();

        loginButton.setOnAction(event -> {

            String loginText = loginUsername.getText().trim();
            String loginPwd = loginPassword.getText().trim();

            User user = new User();
            user.setUserName(loginText);
            user.setPassword(loginPwd);

            ResultSet userRow = databaseHandler.getUser(user);

            int counter = 0;

            try {

                while (userRow.next()) {
                    counter++;

                    String name = userRow.getString("firstname");
                    userId = userRow.getInt("userid");

                    System.out.println("Welcome! " + name);
                }

                if (counter == 1) {
                    setCurrentUserId(userId);
                    showAddItemScreen();
                } else {
                    shakeForm();
                }

            } catch (Exception e) {
                shakeForm();
            }

        });

        loginSignupButton.setOnAction(event -> {

            // Take users to signup screen
            loginSignupButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/com/project/note/view/signup.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            stage.showAndWait();

        });

    }

    private void showAddItemScreen() {
        // Take users to AddItem screen
        loginSignupButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/com/project/note/view/list.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        LoginController.currentUserId = getCurrentUserId();

        stage.showAndWait();
    }

    private void shakeForm() {
        Shaker userNameShaker = new Shaker(loginUsername);
        Shaker passwordShaker = new Shaker(loginPassword);
        passwordShaker.shake();
        userNameShaker.shake();
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
        System.out.println("CurrentUserId " + this.currentUserId);
    }

    public int getCurrentUserId() {
        return this.currentUserId;
    }
}
