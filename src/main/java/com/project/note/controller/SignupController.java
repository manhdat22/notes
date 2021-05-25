package com.project.note.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.project.note.animations.Shaker;
import com.project.note.database.DatabaseHandler;
import com.project.note.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField signUpFirstName;

    @FXML
    private JFXTextField signUpLastName;

    @FXML
    private JFXTextField signUpUsername;

    @FXML
    private JFXCheckBox singUpCheckBoxMale;

    @FXML
    private JFXCheckBox singUpCheckBoxFemale;

    @FXML
    private JFXRadioButton signUpRadioButtonMale;

    @FXML
    private JFXRadioButton signUpRadioButtonFemale;

    @FXML
    private JFXPasswordField signUpPassword;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXButton backButton;

    @FXML
    void initialize() {

        signUpButton.setOnAction(event -> {

            createUser();

        });

        backButton.setOnAction(event -> {

            backToLogin(backButton);

        });

        ToggleGroup genderGroup = new ToggleGroup();

        signUpRadioButtonMale.setToggleGroup(genderGroup);
        signUpRadioButtonFemale.setToggleGroup(genderGroup);

    }

    private void backToLogin(JFXButton button) {
        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/project/note/view/login.fxml"));

        try {
            loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        stage.show();
    }

    private void createUser() {

        String name = signUpFirstName.getText();
        String lastName = signUpLastName.getText();
        String userName = signUpUsername.getText();
        String password = signUpPassword.getText();

        String gender;

        if (signUpRadioButtonFemale.isSelected()) {
            gender = "Female";
        } else {
            gender = "Male";
        }

        if (name.isEmpty() || lastName.isEmpty() || userName.isEmpty() || password.isEmpty() || gender.isEmpty()) {

            Shaker firstNameShaker = new Shaker(signUpFirstName);
            Shaker lastNameShaker = new Shaker(signUpLastName);
            Shaker userNameShaker = new Shaker(signUpUsername);
            Shaker passwordShaker = new Shaker(signUpPassword);

            passwordShaker.shake();
            userNameShaker.shake();
            firstNameShaker.shake();
            lastNameShaker.shake();

            return;
        }

        DatabaseHandler databaseHandler = new DatabaseHandler();
        User user = new User(name, lastName, userName, password, gender);

        databaseHandler.signUpUser(user);

        backToLogin(signUpButton);
    }

}
