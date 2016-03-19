package com.jdc.phoneshop.controller.warehouse;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.NavigationHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.utils.ApplicationException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WareHouseHome implements Initializable {

	@FXML
	private MenuBar menuBar;
	@FXML
	private StackPane view;
	@FXML
	private Label message;

	public static void show() {

		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(WareHouseHome.class.getResource("WareHouseHome.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("Phone Shop Warehouse");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationException ex = new ApplicationException(e);
			MessageHandler.handle(ex);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			// init menu handler
			NavigationHandler.setView(view);

			// init message handler
			MessageHandler.setMESSAGE(message);

			// remove menus by role
			class LocalData {
				Menu removeMenu = null;
				MenuItem removeItem = null;
			}

			LocalData removeResources = new LocalData();

			menuBar.getMenus().forEach(menu -> {

				Menu m = (Menu) menu;

				// select remove menu
				if (Role.BUYER.equals(SecurityManager.getEmployee().getRole())) {
					if (m.getText().startsWith("Store")) {
						removeResources.removeMenu = menu;
					}
				} else {
					if (m.getText().startsWith("Buyer")) {
						removeResources.removeMenu = menu;
					}
				}
				// select remove item
				menu.getItems().forEach(item -> {
					if (item instanceof SeparatorMenuItem == false) {
						if (Role.BUYER.equals(SecurityManager.getEmployee().getRole())) {
							if (item.getText().startsWith("Store")) {
								removeResources.removeItem = item;
							}
						} else {
							if (item.getText().startsWith("Buyer")) {
								removeResources.removeItem = item;
							}
						}
					}
				});
			});

			menuBar.getMenus().remove(removeResources.removeMenu);
			menuBar.getMenus().forEach(menu -> {
				menu.getItems().remove(removeResources.removeItem);
			});

			// set menu action handler
			menuBar.getMenus().forEach(menu -> {
				menu.getItems().forEach(item -> {
					item.setOnAction(event -> NavigationHandler.load(item.getText(), getClass()));
				});
			});

			// load home
			String home = (Role.BUYER.equals(SecurityManager.getEmployee().getRole())) ? "Buyer Home" : "Store Home";
			NavigationHandler.load(home, getClass());
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}

	}

}
