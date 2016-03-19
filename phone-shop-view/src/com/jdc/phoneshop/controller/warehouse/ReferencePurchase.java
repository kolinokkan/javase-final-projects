package com.jdc.phoneshop.controller.warehouse;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.jdc.phoneshop.admin.model.EmployeeModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;
import com.jdc.phoneshop.warehouse.vo.PurchaseItemVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class ReferencePurchase implements Initializable {

	@FXML
	private ComboBox<PurchaseStatus> schStatus;

	@FXML
	private TableView<Purchase> pcTable;
	@FXML
	private TableColumn<Purchase, String> colDate;
	@FXML
	private TableColumn<Purchase, String> colStatus;
	@FXML
	private TableColumn<Purchase, String> colEmployee;

	@FXML
	private TableView<PurchaseItemVO> piTable;
	@FXML
	private TableColumn<PurchaseItemVO, String> colItem;
	@FXML
	private TableColumn<PurchaseItemVO, String> colCount;
	@FXML
	private TableColumn<PurchaseItemVO, String> colBuyPrice;
	@FXML
	private TableColumn<PurchaseItemVO, String> colSellPrice;
	
	@FXML
	private Button btnSaveSellPrice;
	@FXML
	private Button btnAddSellPrice;
	@FXML
	private Button btnUpdate;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			// init combo
			schStatus.getItems().addAll(PurchaseStatus.values());
			schStatus.setValue(PurchaseStatus.Apply);

			// init purchase columns
			colDate.setCellValueFactory(new PropertyValueFactory<>("refDate"));
			colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
			colEmployee.setCellValueFactory(param -> {
				if (null != param) {
					Purchase p = param.getValue();
					Employee emp = EmployeeModel.getModel().findById(p.getUser());
					if (null != emp) {
						return new SimpleStringProperty(emp.getName());
					}
				}
				return null;
			});

			// init purchase item columns
			colItem.setCellValueFactory(param -> {
				if (null != param) {
					PurchaseItemVO vo = param.getValue();
					Item item = ItemModel.getModel().search(vo.getItem().getItem());
					if (null != item) {
						return new SimpleStringProperty(item.getModel().concat(" - ").concat(item.getSpecifications()));
					}
				}
				return null;
			});

			colBuyPrice.setCellValueFactory(param -> {
				if (null != param) {
					PurchaseItemVO vo = param.getValue();
					if (null != vo) {
						return new SimpleStringProperty(String.valueOf(vo.getItem().getPrice()));
					}
				}
				return null;
			});
			colBuyPrice.setCellFactory(TextFieldTableCell.forTableColumn());
			colBuyPrice.setOnEditCommit(event -> {
				try {
					this.checkStatus();
					int newVal = Integer.parseInt(event.getNewValue());
					event.getRowValue().getItem().setPrice(newVal);
					event.getRowValue().setUpdated(true);
				} catch (NumberFormatException e) {
					throw new ApplicationException(e);
				}
			});

			colCount.setCellValueFactory(param -> {
				if (null != param) {
					PurchaseItemVO vo = param.getValue();
					if (null != vo) {
						return new SimpleStringProperty(String.valueOf(vo.getItem().getCount()));
					}
				}
				return null;
			});
			colCount.setCellFactory(TextFieldTableCell.forTableColumn());
			colCount.setOnEditCommit(event -> {
				try {
					int newVal = Integer.parseInt(event.getNewValue());
					event.getRowValue().getItem().setCount(newVal);
					event.getRowValue().setUpdated(true);
				} catch (NumberFormatException e) {
					throw new ApplicationException(e);
				}
			});

			colSellPrice.setCellValueFactory(param -> {
				if(param != null) {
					int price = param.getValue().getSellPrice();
					return new SimpleStringProperty(String.valueOf(price));
				}
				return null;
			});
			colSellPrice.setCellFactory(TextFieldTableCell.forTableColumn());
			colSellPrice.setOnEditCommit(event -> {
				try {
					int newVal = Integer.parseInt(event.getNewValue());
					event.getRowValue().setUpdated(true);
					event.getRowValue().setSellPrice(newVal);
				} catch (NumberFormatException e) {
					throw new ApplicationException(e);
				}
			});
			
			piTable.setEditable(true);

			// add listener
			pcTable.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> selectPurchase());
			schStatus.valueProperty().addListener((a, b, c) -> search());

			// search
			search();

		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}

	}

	public void setDefault() {
		this.selectPurchase();
	}

	public void update() {
		
		try {
			PurchaseModel.getModel().updatePurchase(piTable.getItems().stream().filter(a -> a.isUpdated()).collect(Collectors.toSet()));
			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}

	}
	
	private void checkStatus() {
		Purchase p = pcTable.getSelectionModel().getSelectedItem();
		if(null != p) {
			if(p.getStatus().equals(PurchaseStatus.Cancel) || p.getStatus().equals(PurchaseStatus.Complete)) {
				throw new ApplicationException(ErrorType.Warning, "You can not edit Cancel or Completed Items");
			}
		}
	}
	
	private void selectPurchase() {
		try {
			
			Purchase p = pcTable.getSelectionModel().getSelectedItem();

			Set<PurchaseItem> items = PurchaseModel.getModel().getPurchaseItems(p);

			if (null != items) {
				List<PurchaseItemVO> voList = items.stream().map(a -> {
					PurchaseItemVO vo = new PurchaseItemVO();
					vo.setItem(a);
					Item item = new Item();
					item.setId(a.getItem());
					int sellPrice = ItemModel.getModel().getSellPrice(item, a.getPurchase());
					vo.setSellPrice(sellPrice);
					return vo;
				}).collect(Collectors.toList());

				piTable.getItems().clear();
				piTable.getItems().addAll(voList);

			}

		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	private void search() {
		try {
			piTable.getItems().clear();
			Set<Purchase> purchases = PurchaseModel.getModel().getPurchaseList(schStatus.getValue());
			pcTable.getItems().clear();
			pcTable.getItems().addAll(purchases);
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

}
