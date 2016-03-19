package com.jdc.phoneshop.controller;

import com.jdc.phoneshop.admin.model.SecurityModel;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.utils.ApplicationException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ApplicationMain extends Application {

	private static final String FXML = "%s.fxml";

	@Override
	public void start(Stage stage) throws Exception {
		try {
			String fxml = (SecurityModel.getModel().needToSignUp()) ? String.format(FXML, "SignUp")
					: String.format(FXML, "Login");

			Parent root = FXMLLoader.load(getClass().getResource(fxml));
			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			
			stage.show();

		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
