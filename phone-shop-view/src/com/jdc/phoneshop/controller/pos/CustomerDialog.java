package com.jdc.phoneshop.controller.pos;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.pos.model.CustomerModel;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.utils.ApplicationException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerDialog implements Initializable{
	
	private CustomerSelectHandler handler;
	@FXML
	private TableView<Customer> table;
	@FXML
	private TableColumn<Customer, String> colName;
	@FXML
	private TableColumn<Customer, String> colPhone;
	@FXML
	private TableColumn<Customer, String> colEmail;
	@FXML
	private TableColumn<Customer, String> colAddress;
	@FXML
	private TextField schName;
	
	public static void showDialog(CustomerSelectHandler handler) {
		try {
			Stage stage = new Stage(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			FXMLLoader loader = new FXMLLoader(CustomerDialog.class.getResource("CustomerDialog.fxml"));
			Parent root = loader.load();
			CustomerDialog cont = loader.getController();
			cont.handler = handler;
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			MessageHandler.handle(new ApplicationException(e));
		}
		
	}

	static interface CustomerSelectHandler {
		void select(Customer customer);
	}
	
	public void select() {
		handler.select(table.getSelectionModel().getSelectedItem());
		close();
	}
	
	public void close() {
		table.getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			colName.setCellValueFactory(new PropertyValueFactory<>("name"));
			colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
			colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
			colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
			
			schName.textProperty().addListener((a, b, c) -> search());
			search();
		} catch (ApplicationException e) {
			table.getScene().getWindow().hide();
			MessageHandler.handle(e);
		}
	}

	private void search() {
		table.getItems().clear();
		table.getItems().addAll(CustomerModel.getModel().search(schName.getText()));
	}
	
}
