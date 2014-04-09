package net.piotrl.mvcrud.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.piotrl.mvcrud.model.UsersManager;
import net.piotrl.mvcrud.model.dto.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;

public class UsersController implements Initializable {
    @FXML
    private Text notification;

    @FXML
    private TextField userField;

    @FXML
    private ListView<User> usersList;

    private ObservableList<User> observableUsers;

    private User activeUserLabel;

    private UsersManager usersManager = new UsersManager();

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        List<User> users = null;

        try {
            users = usersManager.getAllUsers();
        } catch (SQLException ex) {
            notification.setText("Problem with loading users from database");
        }

        observableUsers = FXCollections.observableArrayList(users);
        usersList.setItems(observableUsers);

        usersList.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<User>() {
                @Override
                public void changed(ObservableValue<? extends User> ov, User oldVal, User newVal) {
                    if (newVal != null) {
                        activeUserLabel = newVal;
                        userField.setText(activeUserLabel.getName());
                    } else {
                        activeUserLabel = null;
                        userField.clear();
                    }
                }
            }
        );
    }

    @FXML
    protected void handleAddUser() {
        if (userField.getText().length() > 0) try {
            User user = new User(userField.getText());
            usersManager.add(user);
            observableUsers.add(user);

            usersList.getSelectionModel().clearSelection();
            notification.setText("Added user: " + userField.getText());
        } catch (SQLException ex) {
            notification.setText("Problem with adding user: " + userField.getText());
        }
        else {
            notification.setText("Can't add user with empty name");
        }
    }

    @FXML
    protected void handleEditUser() {
        int activeUserIndex = observableUsers.indexOf(activeUserLabel);

        try {
            activeUserLabel.setName(userField.getText());
            usersManager.edit(activeUserLabel);
            observableUsers.set(activeUserIndex, activeUserLabel);

            notification.setText("Updated user: " + userField.getText());
        } catch (SQLException ex) {
            notification.setText("Can't update user: " + activeUserLabel.getName());
        } catch (NullPointerException ex) {
            notification.setText("User isn't selected in the list");
        }

        // updating ObservableArrayList didn't fire event, so we need to refresh list manually
        forceListRefresh();
        usersList.getSelectionModel().clearSelection();
    }

    @FXML
    protected void handleRemoveUser() {
        try {
            usersManager.remove(activeUserLabel);
            notification.setText("Deleted user: " + activeUserLabel.getName());
            observableUsers.remove(activeUserLabel);
        } catch (SQLException ex) {
            notification.setText("Problem with delete user: " + activeUserLabel.getName());
        } catch (NullPointerException ex) {
            notification.setText("Nie zaznaczono użytkownika na liście.");
        }

        usersList.getSelectionModel().clearSelection();
    }

    private void forceListRefresh() {
        ObservableList<User> tmpItems = usersList.getItems();
        usersList.setItems(null);
        usersList.setItems(tmpItems);
    }

}
