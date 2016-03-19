package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.NavigationHandler;
import com.jdc.phoneshop.utils.ApplicationException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminHome implements Initializable{
	
	public static void show() {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(AdminHome.class.getResource("AdminHome.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Phone Shop Admin");
			stage.show();
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		} catch (Exception e) {
			ApplicationException ex = new ApplicationException(e);
			MessageHandler.handle(ex);
		}
	}
	
	@FXML
	private MenuBar menuBar;
	@FXML
	private StackPane view;
	@FXML
	private Label message;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MessageHandler.setMESSAGE(message);
		NavigationHandler.setView(view);
		
		menuBar.getMenus().forEach(menu -> menu.getItems()
				.stream().filter(m -> m instanceof MenuItem)
				.map(m -> (MenuItem)m)
				.forEach(m -> m.setOnAction(event -> {
					MenuItem item = (MenuItem) event.getSource();
					NavigationHandler.load(item.getText(), this.getClass());
				})));
		
		// load report to home
		NavigationHandler.load("Reports", this.getClass());
	}
	
}
