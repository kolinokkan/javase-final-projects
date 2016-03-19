package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.controller.utils.CardView;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class CategoryAdmin implements Initializable, CardView.SaveHandler<Category>{

	@FXML
	private FlowPane flowPane;
	@FXML
	private TextField inName;
	
	private MasterDataModel<Category> model;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = MasterDataModel.getModel(Category.class);
		search();	
	}

	@Override
	public void save(Category t, String newValue) {
		try {
			model.update(t, newValue);
			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	public void add() {
		try {
			Category cat = new Category();
			cat.setName(inName.getText());
			model.add(cat);
			inName.clear();
			
			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	public void clearInput() {
		inName.clear();
	}
	
	public void search() {
		try {
			
			flowPane.getChildren().clear();
			model.getAll().forEach(a -> {
				CardView<Category> cv = new CardView<>(a);
				cv.setSaveHandler(this);
				flowPane.getChildren().add(cv);
			});
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

}
