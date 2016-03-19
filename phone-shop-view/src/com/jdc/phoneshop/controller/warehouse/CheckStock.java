package com.jdc.phoneshop.controller.warehouse;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.NavigationHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.StockModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.vo.StockVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CheckStock implements Initializable {

	@FXML
	private ComboBox<Category> schCategory;
	@FXML
	private ComboBox<Maker> schMaker;
	@FXML
	private TextField schModel;
	@FXML
	private TextField schMaxCount;

	@FXML
	private TextField inPurchaseCount;

	@FXML
	private TableView<StockVO> table;
	@FXML
	private TableColumn<StockVO, String> colId;
	@FXML
	private TableColumn<StockVO, String> colCategory;
	@FXML
	private TableColumn<StockVO, String> colMaker;
	@FXML
	private TableColumn<StockVO, String> colModel;
	@FXML
	private TableColumn<StockVO, String> colSpecification;
	@FXML
	private TableColumn<StockVO, String> colCount;
	@FXML
	private TableColumn<StockVO, String> colSellPrice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			schCategory.getItems().addAll(MasterDataModel.getModel(Category.class).getAll());
			schMaker.getItems().addAll(MasterDataModel.getModel(Maker.class).getAll());

			colId.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					return new SimpleStringProperty(String.valueOf(vo.getItem().getId()));
				}
				return null;
			});

			colCategory.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					return new SimpleStringProperty(vo.getCategory().getName());
				}
				return null;
			});

			colMaker.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					return new SimpleStringProperty(vo.getMaker().getMaker());
				}
				return null;
			});

			colModel.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					return new SimpleStringProperty(vo.getItem().getModel());
				}
				return null;
			});

			colSpecification.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					return new SimpleStringProperty(vo.getItem().getSpecifications());
				}
				return null;
			});

			colCount.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					if (null != vo.getStock())
						return new SimpleStringProperty(String.valueOf(vo.getStock().getCount()));
				}
				return null;
			});

			colSellPrice.setCellValueFactory(param -> {
				if (null != param) {
					StockVO vo = param.getValue();
					if (null != vo.getItemPrice())
						return new SimpleStringProperty(String.valueOf(vo.getItemPrice().getPrice()));
				}
				return null;
			});

			schCategory.valueProperty().addListener((a, b, c) -> search());
			schMaker.valueProperty().addListener((a, b, c) -> search());
			schModel.textProperty().addListener((a, b, c) -> search());
			schMaxCount.textProperty().addListener((a, b, c) -> search());

			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			search();
		} catch (ApplicationException e) {
			e.printStackTrace();
			MessageHandler.handle(e);
		}
	}

	private void search() {
		table.getItems().clear();
		table.getItems().addAll(StockModel.getModel().getStock(schCategory.getValue(), schMaker.getValue(),
				schModel.getText(), schMaxCount.getText()));
	}

	public void clear() {
		schCategory.setValue(null);
		schMaker.setValue(null);
		schModel.clear();
		schMaxCount.clear();
	}

	public void purchase() {
		
		try {
			List<Item> items = table.getSelectionModel().getSelectedItems()
					.stream().map(a -> a.getItem())
					.collect(Collectors.toList());
			
			if(items.size() == 0) {
				throw new ApplicationException(ErrorType.Warning, "Please select Items to purchase");
			}
			
			int count = Integer.parseInt(inPurchaseCount.getText());
			
			PurchaseModel.getModel().purchase(items, count, SecurityManager.getEmployee());
			
			NavigationHandler.load("Reference Purchase", this.getClass());
		} catch (NumberFormatException e) {
			MessageHandler.showDialogMessage("Please enter purchase count only with digit!");
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

}
