package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewEmployeesController {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> empIdColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> fatherNameColumn;
    @FXML
    private TableColumn<Employee, String> mobileColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> photoColumn;

    private final ParkingApi api = new ParkingApi();

    @FXML
    private void initialize() {
        empIdColumn.setCellValueFactory(new PropertyValueFactory<>("empId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fatherNameColumn.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNo"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        try {
            employeeTable.getItems().setAll(api.getEmployees());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteEmployee() {
        // not supported yet: the server has no URL to delete an employee
    }
}
