package com.jdc.phoneshop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.admin.model.SecurityModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.controller.admin.AdminHome;
import com.jdc.phoneshop.controller.pos.PosHome;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.controller.warehouse.WareHouseHome;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login implements Initializable{
	
	@FXML
	private TextField loginId;
	@FXML
	private PasswordField password;
	@FXML
	private Label message;

	public void login() {
		
		try {
			
			// login
			Employee emp = SecurityModel.getModel().login(loginId.getText(), password.getText());
			SecurityManager.setEmployee(emp);
			
			switch (emp.getRole()) {
			case MANAGER:
				// load admin home
				AdminHome.show();
				// close self window
				loginId.getScene().getWindow().hide();
				break;
			case BUYER:
			case STORE:
				// load ware house home
				WareHouseHome.show();
				// close self window
				loginId.getScene().getWindow().hide();
				break;
			case POS:
				// show pos home
				PosHome.show();
				// close self window
				loginId.getScene().getWindow().hide();
				break;

			default:
				throw new ApplicationException(ErrorType.Warning, 
						"Your account is unvalid account. Please contact with System Administrator.");
			}
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
		
	}
	
	public void cancel() {
		Platform.exit();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MessageHandler.setMESSAGE(message);
	}
}
