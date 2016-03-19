package com.jdc.phoneshop.controller.utils;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CardView<T> extends VBox{
	
	private SaveHandler<T> saveHandler;
	
	private T value;
	private Button edit;
	private Button save;
	private Label label;
	private TextField input;
	
	public CardView(T t) {
		this.value = t;
		label = new Label(t.toString());
		input = new TextField(t.toString());
		HBox view = new HBox();
		view.getStyleClass().add("item-view-body");
		
		edit = getButton("edit");
		edit.setOnAction(e -> {
			view.getChildren().remove(label);
			view.getChildren().remove(edit);
			view.getChildren().add(save);
			view.getChildren().add(input);
		});
		
		save = getButton("save");
		save.setOnAction(e -> {
			saveHandler.save(t, input.getText());
			label.setText(input.getText());
			
			view.getChildren().remove(input);
			view.getChildren().remove(save);
			view.getChildren().add(edit);
			view.getChildren().add(label);
		});
		
		
		view.getChildren().addAll(edit, label);
		getChildren().add(view);
		getStyleClass().add("item-view");
	}
	
	public void setSaveHandler(SaveHandler<T> saveHandler) {
		this.saveHandler = saveHandler;
	}
	
	
	private Button getButton(String string) {
		try {
			Button btn = new Button();
			Image image = new Image(getClass().getResourceAsStream(string.concat(".png")));
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(20);
			imageView.setPreserveRatio(true);
			btn.setGraphic(imageView);
			btn.setPadding(Insets.EMPTY);
			return btn;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static interface SaveHandler<T> {
		void save(T t, String newValue);
	}
	
	public T getValue() {
		return value;
	}

}
