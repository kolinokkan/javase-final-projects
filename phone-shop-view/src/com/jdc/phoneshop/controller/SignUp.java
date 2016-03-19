package com.jdc.phoneshop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.admin.model.SecurityModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.controller.admin.AdminHome;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.utils.ApplicationException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUp implements Initializable {
	@FXML
	private TextField name;
	@FXML
	private TextField loginId;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField password2;
	@FXML
	private Label message;

	public void signUp() {
		try {
			// sign up
			Employee emp = SecurityModel.getModel().signUp(name.getText(), loginId.getText(), password.getText(),
					password2.getText());
			SecurityManager.setEmployee(emp);
			
			// load admin home
			AdminHome.show();

			// close self window
			loginId.getScene().getWindow().hide();
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
