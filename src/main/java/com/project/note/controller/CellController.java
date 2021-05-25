package com.project.note.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.project.note.database.DatabaseHandler;
import com.project.note.model.Note;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

public class CellController extends JFXListCell<Note> {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ImageView iconImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private Label dateLabel;

    @FXML
    public JFXButton deleteButton;

    // @FXML
    // private ImageView deleteButton;

    // @FXML
    // public ImageView listUpdateButton;

    private FXMLLoader fxmlLoader;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() throws SQLException {

    }

    @Override
    public void updateItem(Note myNote, boolean empty) {

        databaseHandler = new DatabaseHandler(); // main change

        super.updateItem(myNote, empty);

        if (empty || myNote == null) {
            setText(null);
            setGraphic(null);
        } else {

            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/note/view/cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            titleLabel.setText(myNote.getTitle());
            dateLabel.setText(String.format("%1$TD %1$TT", myNote.getDatecreated()));
            contentLabel.setText(myNote.getContent());

            int taskId = myNote.getNoteId();

            setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    FXMLLoader loader = new FXMLLoader();

                    loader.setLocation(getClass().getResource("/com/project/note/view/updateNoteForm.fxml"));

                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);

                    UpdateNoteController updateNoteController = loader.getController();
                    updateNoteController.setTitleField(myNote.getTitle());
                    updateNoteController.setUpdateContentField(myNote.getContent());

                    updateNoteController.updateNoteButton.setOnAction(event1 -> {

                        Calendar calendar = Calendar.getInstance();

                        java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

                        try {

                            System.out.println("taskid " + myNote.getNoteId());

                            databaseHandler.updateNote(timestamp, updateNoteController.getContent(),
                                    updateNoteController.getTitle(), myNote.getNoteId());
                            updateNoteController.updateNoteButton.getScene().getWindow().hide();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    });

                    stage.show();

                }
            });

            // listUpdateButton.setOnMouseClicked(event -> {

            // FXMLLoader loader = new FXMLLoader();
            // loader.setLocation(getClass().getResource("/com/project/note/view/updateNoteForm.fxml"));

            // try {
            // loader.load();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

            // Parent root = loader.getRoot();
            // Stage stage = new Stage();
            // stage.setScene(new Scene(root));
            // stage.setResizable(false);

            // UpdateNoteController updateNoteController = loader.getController();
            // updateNoteController.setNoteField(myNote.getTitle());
            // updateNoteController.setUpdateContentField(myNote.getContent());

            // updateNoteController.updateNoteButton.setOnAction(event1 -> {

            // Calendar calendar = Calendar.getInstance();

            // java.sql.Timestamp timestamp = new
            // java.sql.Timestamp(calendar.getTimeInMillis());

            // try {

            // System.out.println("taskid " + myNote.getNoteId());

            // databaseHandler.updateNote(timestamp, updateNoteController.getContent(),
            // updateNoteController.getTitle(), myNote.getNoteId());

            // // update our listController
            // // updateNoteController.refreshList();
            // } catch (SQLException e) {
            // e.printStackTrace();
            // } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            // }

            // });

            // stage.show();

            // });

            deleteButton.setOnAction(event -> {
                try {

                    databaseHandler.deleteNote(LoginController.currentUserId, taskId);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                getListView().getItems().remove(getItem());

            });

            setText(null);
            setGraphic(rootAnchorPane);

        }
    }

}
