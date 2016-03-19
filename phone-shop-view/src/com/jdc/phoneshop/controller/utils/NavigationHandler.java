package com.jdc.phoneshop.controller.utils;

import com.jdc.phoneshop.utils.ApplicationException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class NavigationHandler {
	
	private static StackPane VIEW;
	
	public static void setView(StackPane view) {
		NavigationHandler.VIEW = view;
	}
	
	public static void load(String viewName, Class<?> resourseLoader) {
		try {
			
			MessageHandler.clear();
			
			if("Close".equals(viewName)) {
				Platform.exit();
			} else {
				VIEW.getChildren().clear();
				Parent node = FXMLLoader.load(resourseLoader.getResource(viewName.replaceAll(" ", "").concat(".fxml")));
				VIEW.getChildren().add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationException ex = new ApplicationException(e);
			MessageHandler.handle(ex);
		}
	}

}
