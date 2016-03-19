package com.jdc.phoneshop.controller.warehouse;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.NavigationHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ItemList implements Initializable {

	@FXML
	private TableView<Item> table;
	@FXML
	private TableColumn<Item, String> colId;
	@FXML
	private TableColumn<Item, String> colCategory;
	@FXML
	private TableColumn<Item, String> colMaker;
	@FXML
	private TableColumn<Item, String> colModel;
	@FXML
	private TableColumn<Item, String> colSpecification;

	@FXML
	private ComboBox<Category> combCategory;
	@FXML
	private ComboBox<Maker> combMaker;
	@FXML
	private TextField textModel;
	@FXML
	private TextField textCount;

	public void search() {
		try {
			Set<Item> items = ItemModel.getModel().search(combCategory.getValue(), combMaker.getValue(),
					textModel.getText());
			table.getItems().clear();
			table.getItems().addAll(items);
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	public void purchase() {
		try {
			List<Item> items = table.getSelectionModel().getSelectedItems();

			String strCnt = textCount.getText();
			if (StringUtils.isEmptyString(strCnt)) {
				throw new ApplicationException(ErrorType.Warning, "Please enter purchase count!");
			}

			int count = Integer.parseInt(strCnt);
			PurchaseModel.getModel().purchase(items, count, SecurityManager.getEmployee());
			NavigationHandler.load("Reference Purchase", getClass());

		} catch (NumberFormatException e) {
			throw new ApplicationException(ErrorType.Warning, "Please enter purchase count with digit only!");
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	public void clear() {
		combMaker.setValue(null);
		combCategory.setValue(null);
		textModel.clear();
	}

	public void addItems() {
		NavigationHandler.load("Add Items", getClass());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// init combos
		MasterDataModel<Category> catModel = MasterDataModel.getModel(Category.class);
		Set<Category> categories = catModel.getAll();
		combCategory.getItems().addAll(categories);

		MasterDataModel<Maker> mkModel = MasterDataModel.getModel(Maker.class);
		Set<Maker> makers = mkModel.getAll();
		combMaker.getItems().addAll(makers);

		// init table columns
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colCategory.setCellValueFactory(param -> {
			if (null != param) {
				Item item = param.getValue();
				Category cat = MasterDataModel.getModel(Category.class).findByID(item.getCategory());
				if (null != cat) {
					return new SimpleStringProperty(cat.getName());
				}
			}
			return null;
		});
		colMaker.setCellValueFactory(param -> {
			if (null != param) {
				Item item = param.getValue();
				Maker cat = MasterDataModel.getModel(Maker.class).findByID(item.getMaker());
				if (null != cat) {
					return new SimpleStringProperty(cat.getMaker());
				}
			}
			return null;
		});
		colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
		colSpecification.setCellValueFactory(new PropertyValueFactory<>("specifications"));

		// add listener
		combCategory.valueProperty().addListener((a, b, c) -> search());
		combMaker.valueProperty().addListener((a, b, c) -> search());
		textModel.textProperty().addListener((a, b, c) -> search());

		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		search();
	}

}
