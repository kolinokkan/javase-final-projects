package com.jdc.phoneshop.controller.utils;

import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;

public class MessageHandler {
	
	private static Label MESSAGE;
	
	public static void setMESSAGE(Label mESSAGE) {
		MESSAGE = mESSAGE;
	}
	
	public static void handle(ApplicationException e) {
		
		if(e.getType().equals(ErrorType.Warning)) {
			showDialogMessage(e.getMessage());
		} else {
			MESSAGE.setText(e.getMessage());
		}
		
	}
	
	public static void showDialogMessage(String message) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setContentText(message);
		dialog.setTitle("Warning Message");
		ButtonType button = new ButtonType("OK", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(button);
		dialog.show();
	}
	
	public static void clear() {
		MESSAGE.setText("");
	}

}
