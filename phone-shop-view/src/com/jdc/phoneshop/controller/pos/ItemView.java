package com.jdc.phoneshop.controller.pos;

import com.jdc.phoneshop.controller.pos.MakerTab.ItemSelectHandler;
import com.jdc.phoneshop.pos.model.ItemModel.ItemVO;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemView extends VBox {
	
	public ItemView(ItemVO vo, ItemSelectHandler handler) {

		buildView(vo);
		
		getStyleClass().addAll("item-view");
		
		// on mouse click
		setOnMouseClicked(e -> {
			handler.selectItem(vo);
		});
		
		// on mouse over
		setOnMouseEntered(e -> {
			getStyleClass().add("item-view-o");
		});
		
		// on mouse exit
		setOnMouseExited(e -> {
			getStyleClass().remove("item-view-o");
		});
	}

	private void buildView(ItemVO vo) {
		
		// title
		HBox title = new HBox();
		title.getStyleClass().add("item-view-title");
		title.getChildren().add(new Label(vo.getItem().getModel()));
		
		// price
		HBox body = new HBox();
		body.getStyleClass().add("item-view-body");
		body.getChildren().add(new Label(String.valueOf(vo.getPrice())));
		
		// specifications
		HBox footer = new HBox();
		footer.getStyleClass().add("item-view-footer");
		footer.getChildren().add(new Label(vo.getItem().getSpecifications()));
		
		getChildren().addAll(title, body, footer);
	}

}
