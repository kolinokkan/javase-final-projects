package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.jdc.phoneshop.admin.model.SellPriceAdminModel;
import com.jdc.phoneshop.admin.model.vo.SellPriceVO;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice.PriceStatus;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

public class SellPrice implements Initializable{
	
	@FXML
	private CheckBox notConfirm;
	@FXML
	private CheckBox confirmed;
	@FXML
	private CheckBox cancel;
	
	@FXML
	private TableView<SellPriceVO> table;
	@FXML
	private TableColumn<SellPriceVO, String> colCategory;
	@FXML
	private TableColumn<SellPriceVO, String> colMaker;
	@FXML
	private TableColumn<SellPriceVO, String> colItem;
	@FXML
	private TableColumn<SellPriceVO, String> colRefDate;
	@FXML
	private TableColumn<SellPriceVO, String> colStatus;
	@FXML
	private TableColumn<SellPriceVO, String> colCount;
	@FXML
	private TableColumn<SellPriceVO, String> colBuyPrice;
	@FXML
	private TableColumn<SellPriceVO, String> colSellPrice;
	
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnApprove;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// define table columns
		colCategory.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				Item item = vo.getItem();
				int catId = item.getCategory();
				Category cat = MasterDataModel.getModel(Category.class).findByID(catId);
				return new SimpleStringProperty(cat.getName());
			}
			return null;
		});
		
		colMaker.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				Item item = vo.getItem();
				int catId = item.getMaker();
				Maker cat = MasterDataModel.getModel(Maker.class).findByID(catId);
				return new SimpleStringProperty(cat.getMaker());
			}
			return null;
		});
		
		colItem.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				Item item = vo.getItem();
				return new SimpleStringProperty(item.getModel().concat(" - ").concat(item.getSpecifications()));
			}
			return null;
		});
		
		colRefDate.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				PurchaseItem item = vo.getPurchaseItem();
				int id = item.getPurchase();
				Purchase p = PurchaseModel.getModel().findById(id);
				return new SimpleStringProperty(p.getRefDate().toString());
			}
			return null;
		});
		
		colStatus.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				ItemPrice item = vo.getItemPrice();
				return new SimpleStringProperty(item.getStatus().toString());
			}
			return null;
		});
		
		colCount.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				PurchaseItem item = vo.getPurchaseItem();
				return new SimpleStringProperty(String.valueOf(item.getCount()));
			}
			return null;
		});
		
		colBuyPrice.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				PurchaseItem item = vo.getPurchaseItem();
				return new SimpleStringProperty(String.valueOf(item.getPrice()));
			}
			return null;
		});

		colSellPrice.setCellValueFactory(param -> {
			if(null != param) {
				SellPriceVO vo = param.getValue();
				ItemPrice item = vo.getItemPrice();
				return new SimpleStringProperty(String.valueOf(item.getPrice()));
			}
			return null;
		});
		
		// set to editable columns
		colSellPrice.setCellFactory(TextFieldTableCell.forTableColumn());
		colSellPrice.setOnEditCommit(event -> {
			try {
				
				SellPriceVO vo = event.getRowValue();
				
				if(vo.getItemPrice().getStatus().equals(PriceStatus.NotConfirm)) {
					int newPrice = Integer.parseInt(event.getNewValue());
					vo.getItemPrice().setPrice(newPrice);
					SellPriceAdminModel.getModel().updatePrice(vo.getItemPrice(), newPrice);
				} else {
					MessageHandler.showDialogMessage("You can't update the status of CANCEL or CONFIRMED");
				}
			} catch(NumberFormatException e) {
				
			} catch(ApplicationException e) {
				MessageHandler.handle(e);
			}
		});
		
		// set table abilities
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.setEditable(true);
		
		// add events to check boxes
		clear();
		notConfirm.selectedProperty().addListener((a, b, c) -> search());
		confirmed.selectedProperty().addListener((a, b, c) -> search());
		cancel.selectedProperty().addListener((a, b, c) -> search());
		
		// load data to table
		search();
	}
	
	public void clear() {
		notConfirm.setSelected(true);
		cancel.setSelected(false);
		confirmed.setSelected(false);
	}
	
	public void cancel() {
		try {
			// get view data
			Set<ItemPrice> updateData = getTargetData();
			if(updateData.size() > 0) {
				// update operation
				SellPriceAdminModel.getModel().cancel(updateData);
				// show message
				MessageHandler.showDialogMessage("Cancel Operation has been done successfully!");
			} else {
				MessageHandler.showDialogMessage("You have no data to cancel. Please check the status!");
			}

			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	public void approve() {
		try {
			// get view data
			Set<ItemPrice> updateData = getTargetData();
			if(updateData.size() > 0) {
				// update operation
				SellPriceAdminModel.getModel().confirm(updateData, SecurityManager.getEmployee().getId());
				// show message
				MessageHandler.showDialogMessage("Approve Operation has been done successfully!");
			} else {
				MessageHandler.showDialogMessage("You have no data to approve. Please check the status!");
			}
			
			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	private Set<ItemPrice> getTargetData() {
		return table.getSelectionModel().getSelectedItems()
				.stream()
				.filter(vo -> vo.getItemPrice().getStatus().equals(PriceStatus.NotConfirm))
				.map(vo -> vo.getItemPrice())
				.collect(Collectors.toSet());
	}
	
	private void search() {
		try {
			PriceStatus [] statuses = getSelectedStatus();
			Set<SellPriceVO> result = SellPriceAdminModel.getModel().getSellPriceList(statuses);
			table.getItems().clear();
			table.getItems().addAll(result);
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	private PriceStatus[] getSelectedStatus() {
		List<PriceStatus> list = new ArrayList<>();
		
		if(notConfirm.isSelected()) {
			list.add(PriceStatus.NotConfirm);
		}
		
		if(confirmed.isSelected()) {
			list.add(PriceStatus.Confirmed);
		}
		
		if(cancel.isSelected()) {
			list.add(PriceStatus.Cancel);
		}
		
		return list.toArray(new PriceStatus[list.size()]);
	}

}
