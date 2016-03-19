package com.jdc.phoneshop.controller.pos;

import java.util.List;

import com.jdc.phoneshop.pos.model.ItemModel.ItemVO;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;

public class MakerTab extends Tab {

	public MakerTab(String name, List<ItemVO> values, ItemSelectHandler handler) {
		setText(name);
		ScrollPane scrollPane = new ScrollPane();
		setContent(scrollPane);
		
		FlowPane contents = new FlowPane();
		contents.getStyleClass().add("item-flow-pane");
		scrollPane.setContent(contents);
		
		for (ItemVO vo : values) {
			ItemView view = new ItemView(vo, handler);
			contents.getChildren().add(view);
		}
	}
	
	public static interface ItemSelectHandler {
		void selectItem(ItemVO item);
	}
	
}
