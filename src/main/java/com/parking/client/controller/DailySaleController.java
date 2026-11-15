package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.model.DailySale;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class DailySaleController {

    @FXML
    private TableView<DailySale> saleTable;
    @FXML
    private TableColumn<DailySale, String> bayColumn;
    @FXML
    private TableColumn<DailySale, Integer> saleColumn;
    @FXML
    private TextField totalField;

    private final ParkingApi api = new ParkingApi();

    @FXML
    private void initialize() {
        bayColumn.setCellValueFactory(new PropertyValueFactory<>("parkingBay"));
        saleColumn.setCellValueFactory(new PropertyValueFactory<>("sale"));
        try {
            List<DailySale> sales = api.getDailySales();
            if (!sales.isEmpty()) {
                saleTable.getItems().setAll(sales);
                int total = 0;
                for (DailySale sale : sales) {
                    total += sale.getSale();
                }
                totalField.setText(String.valueOf(total));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
