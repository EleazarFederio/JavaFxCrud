package system;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    Database db = new Database();
    @FXML private JFXTextField firstnameField = new JFXTextField();
    @FXML private JFXTextField lastnameField = new JFXTextField();
    @FXML private TableView<People> peopleTable = new TableView<>();
    @FXML private TableColumn<People, String> firstnameCollumn = new TableColumn<>();
    @FXML private TableColumn<People, String> lastnameCollumn = new TableColumn<>();
    int id = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstnameCollumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameCollumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        peopleTable.getColumns().clear();
        peopleTable.setItems(getPeople());
        peopleTable.getColumns().addAll(firstnameCollumn,lastnameCollumn);
    }

    public void addPeople(){
        try {
            db.connect();
            String query = "INSERT INTO `people` (`firstname`, `lastname`) VALUES ('"+firstnameField.getText()+"', '"+lastnameField.getText()+"');";
            System.out.println(query);
            Statement statement = db.connect().createStatement();
            if (statement.executeUpdate(query) > 0){
                JOptionPane.showMessageDialog(null, "Data Added");
            }
            peopleTable.setItems(getPeople());
            db.connect().close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void selectPeople(){
        People people = peopleTable.getSelectionModel().getSelectedItem();
        firstnameField.setText(people.getFirstname());
        lastnameField.setText(people.getLastname());
        id = people.getId();
        System.out.println(id);
    }

    public void updatePeople(){
        try{
            db.connect();
            String query = "UPDATE `people` SET `firstname` = '"+firstnameField.getText()+"', `lastname` = '"+lastnameField.getText()+"' WHERE id = "+id+"; ";
            Statement statement = db.connect().createStatement();
            System.out.println(query);
            if (statement.executeUpdate(query) > 0){
                JOptionPane.showMessageDialog(null, "Data Updated!");
                peopleTable.setItems(getPeople());
            }
            db.connect().close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void deletePeople(){
        try{
            db.connect();
            String query = "DELETE FROM `people` WHERE `id` = "+id+"";
            Statement statement = db.connect().createStatement();
            if (statement.executeUpdate(query) > 0){
                JOptionPane.showMessageDialog(null, "Data Deleted!");
                peopleTable.setItems(getPeople());
            }
            db.connect().close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void clearField(){
        firstnameField.clear();
        lastnameField.clear();
        id = 0;
    }

    public ObservableList<People> getPeople(){
        ObservableList<People> people = FXCollections.observableArrayList();
        try {
            db.connect();
            String query = "SELECT * FROM `people`";
            Statement statement = db.connect().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                people.add(new People(firstname, lastname, resultSet.getInt("id")));
            }
            db.connect().close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return people;
    }

}
