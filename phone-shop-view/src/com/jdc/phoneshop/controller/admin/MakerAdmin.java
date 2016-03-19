package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.phoneshop.controller.utils.CardView;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class MakerAdmin implements Initializable, CardView.SaveHandler<Maker> {
		
	@FXML
	private FlowPane flowPane;
	@FXML
	private TextField inName;
	
	private MasterDataModel<Maker> model;
	
	@Override
	public void save(Maker t, String newValue) {
		try {
			model.update(t, newValue);
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = MasterDataModel.getModel(Maker.class);
		search();
	}
	
	public void clear() {
		inName.clear();
	}
	
	public void save() {
		try {
			Maker mk = new Maker();
			mk.setMaker(inName.getText());
			model.add(mk);
			inName.clear();
			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	private void search() {
		try {
			flowPane.getChildren().clear();
			model.find(null).forEach(a -> {
				CardView<Maker> view = new CardView<>(a);
				view.setSaveHandler(this);
				flowPane.getChildren().add(view);
			});
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

}
