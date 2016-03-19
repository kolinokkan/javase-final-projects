package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.admin.model.EmployeeModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeAdmin implements Initializable{
	
	private Role [] roles = Role.values();
	@FXML
	private ComboBox<Role> schRoles;
	@FXML
	private TextField schName;
	
	@FXML
	private ComboBox<Role> inRoles;
	@FXML
	private TextField inName;
	@FXML
	private TextField inLoginId;
	@FXML
	private TextField inPhoneNumber;
	
	@FXML
	private TableView<Employee> table;
	@FXML
	private TableColumn<Employee, String> colName;
	@FXML
	private TableColumn<Employee, String> colRole;
	@FXML
	private TableColumn<Employee, String> colLoginId;
	@FXML
	private TableColumn<Employee, String> colPhone;

	private EmployeeModel model;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inRoles.getItems().addAll(roles);
		schRoles.getItems().addAll(roles);
		
		model = EmployeeModel.getModel();
		
		schName.textProperty().addListener((a, b, c) -> {search();});
		schRoles.valueProperty().addListener((a, b, c) -> {search();});
		
		// Columns Configurations
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
		colLoginId.setCellValueFactory(new PropertyValueFactory<>("loginId"));
		colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		
		// dismiss
		MenuItem menu = new MenuItem("Un-Activate");
		menu.setOnAction(event -> {
			unActivate();
		});
		ContextMenu ctx = new ContextMenu(menu);
		table.setContextMenu(ctx);
		
		search();
	}
	
	private void unActivate() {
		try {
			model.unactivate(table.getSelectionModel().getSelectedItem());
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	public void clear() {
		
		inRoles.setValue(null);
		inName.clear();
		inLoginId.clear();
		inPhoneNumber.clear();
	}
	
	public void clearSearch() {
		schName.clear();
		schRoles.setValue(null);
	}
	
	public void addEmployee() {
		try {
			Employee emp = new Employee();
			emp.setLoginId(inLoginId.getText());
			emp.setName(inName.getText());
			emp.setPassword("password");
			emp.setPhone(inPhoneNumber.getText());
			emp.setRole(inRoles.getValue());
			
			model.create(emp);
			
			clear();
			clearSearch();

			search();

			throw new ApplicationException(ErrorType.Info, "Password has been set as 'password'");
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	private void search() {
		try {
			
			table.getItems().clear();
			table.getItems().addAll(model.find(schRoles.getValue(), schName.getText()));
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
		
	}

}
