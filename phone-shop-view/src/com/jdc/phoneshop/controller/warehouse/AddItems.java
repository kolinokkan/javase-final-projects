package com.jdc.phoneshop.controller.warehouse;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.NavigationHandler;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AddItems implements Initializable{
	
	@FXML
	private ComboBox<Category> inCategory;
	@FXML
	private ComboBox<Maker> inMaker;
	@FXML
	private TextField inModel;
	@FXML
	private TextArea inSpecification;
	
	@FXML
	private TableView<Item> table;
	@FXML
	private TableColumn<Item, String> colCategory;
	@FXML
	private TableColumn<Item, String> colMaker;
	@FXML
	private TableColumn<Item, String> colModel;
	@FXML
	private TableColumn<Item, String> colSpecification;
	
	
	public void clearInputs() {
		inCategory.setValue(null);
		inMaker.setValue(null);
		inModel.clear();
		inSpecification.clear();
	}
	
	public void addItem() {
		try {
			
			Item item = new Item();
			
			Category cat = inCategory.getValue();
			if(null == cat) {
				throw new ApplicationException(ErrorType.Warning, "Please select one Category!");
			}
			
			Maker mk = inMaker.getValue();
			if(null == mk) {
				throw new ApplicationException(ErrorType.Warning, "Please select one Maker!");
			}
			
			String model = inModel.getText();
			if(null == model || model.isEmpty()) {
				throw new ApplicationException(ErrorType.Warning, "Please enter Item Model!");
			}
			
			item.setCategory(cat.getId());
			item.setMaker(mk.getId());
			item.setModel(model);
			item.setSpecifications(inSpecification.getText());
			
			table.getItems().add(item);
			
			clearInputs();
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	public void clearTable() {
		table.getItems().clear();
	}
	
	public void saveItems() {
		try {
			
			List<Item> list = table.getItems();
			if(null == list || list.size() == 0) {
				throw new ApplicationException(ErrorType.Warning, "Please add item to save!");
			}
			
			// save items to db
			ItemModel.getModel().create(list);
			
			// navigate to Item List View
			NavigationHandler.load("Item List", getClass());
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		MasterDataModel<Category> catModel = MasterDataModel.getModel(Category.class);
		Set<Category> categories = catModel.getAll();
		inCategory.getItems().addAll(categories);
		
		MasterDataModel<Maker> mkModel = MasterDataModel.getModel(Maker.class);
		Set<Maker> makers = mkModel.getAll();
		inMaker.getItems().addAll(makers);
		
		colCategory.setCellValueFactory(param -> {
			if(null != param) {
				Item item = param.getValue();
				Category cat = MasterDataModel.getModel(Category.class).findByID(item.getCategory());
				if(null != cat) {
					return new SimpleStringProperty(cat.getName());
				}
			}
			return null;
		});
		colMaker.setCellValueFactory(param -> {
			if(null != param) {
				Item item = param.getValue();
				Maker cat = MasterDataModel.getModel(Maker.class).findByID(item.getMaker());
				if(null != cat) {
					return new SimpleStringProperty(cat.getMaker());
				}
			}
			return null;
		});
		colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
		colSpecification.setCellValueFactory(new PropertyValueFactory<>("specifications"));
		
	}

}
