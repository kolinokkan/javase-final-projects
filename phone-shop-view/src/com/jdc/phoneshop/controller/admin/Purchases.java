package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.jdc.phoneshop.admin.model.EmployeeModel;
import com.jdc.phoneshop.admin.model.PurchaseAdminModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.vo.PurchaseItemVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class Purchases implements Initializable{

	@FXML
	private ComboBox<PurchaseStatus> schStatus;
	@FXML
	private ComboBox<Employee> schEmployee;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			// load data to combo box
			schStatus.getItems().addAll(PurchaseStatus.values());
			schStatus.setValue(PurchaseStatus.Apply);
			schEmployee.getItems().addAll(EmployeeModel.getModel().getAll());
			
			// define table columns of Purchase Table
			colDate.setCellValueFactory(new PropertyValueFactory<>("refDate"));
			colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
			colEmployee.setCellValueFactory(param -> {
				if(null != param) {
					Purchase p = param.getValue();
					int employeeId = p.getUser();
					Employee emp = EmployeeModel.getModel().findById(employeeId);
					if(null != emp) {
						return new SimpleStringProperty(emp.getName());
					}
				}
				return null;
			});
			
			// define table columns of Purchase Item VO
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
				
				this.checkStatus();
				
				try {
					int newVal = Integer.parseInt(event.getNewValue());
					int rowPosition = event.getTablePosition().getRow();
					PurchaseItemVO vo = event.getTableView().getItems().get(rowPosition);
					vo.getItem().setCount(newVal);
					vo.setUpdated(true);
				} catch (NumberFormatException e) {
					throw new ApplicationException(e);
				}
			});
			
			piTable.setEditable(true);

			// add search event
			schStatus.valueProperty().addListener((a, b, c) -> search());
			schEmployee.valueProperty().addListener((a, b, c) -> search());
			pcTable.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> selectPurchase());
			
			// load data to Purchase Table
			search();			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}

	}
	
	public void setDefault() {
		selectPurchase();
	}
	
	public void saveAndConfirm() {
		try {
			
			Purchase p = pcTable.getSelectionModel().getSelectedItem();
			
			if(null == p) {
				throw new ApplicationException(ErrorType.Warning, "Please select purchase to do this operation!");
			}
			
			Set<PurchaseItemVO> items = piTable.getItems().stream().filter(a -> a.isUpdated())
					.collect(Collectors.toSet());
			
			PurchaseAdminModel.getModel().udpateAndConfirm(items, p);
			
			MessageHandler.showDialogMessage("Your operation has been done Successfully!");
			search();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	public void cancel() {
		try {
			Purchase p = pcTable.getSelectionModel().getSelectedItem();
			if(null == p) {
				throw new ApplicationException(ErrorType.Warning, "Please select Purchase to Cancel!");
			}
			search();
			PurchaseAdminModel.getModel().cancel(p);
			
			MessageHandler.showDialogMessage("Cancel Process has been done Successfuly!");
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}
	
	private void selectPurchase() {
		try {
			piTable.getItems().clear();
			Purchase p = pcTable.getSelectionModel().getSelectedItem();
			if(null != p) {
				piTable.getItems().addAll(PurchaseAdminModel.getModel().getPurchaseItems(p).stream().map(a -> {
					PurchaseItemVO vo = new PurchaseItemVO();
					vo.setItem(a);
					return vo;
				}).collect(Collectors.toList()));
			}
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
	
	private void search() {
		try {
			
			PurchaseStatus status = schStatus.getValue();
			Employee employee = schEmployee.getValue();
			
			Set<Purchase> result = PurchaseAdminModel.getModel().getPurchaseList(status, employee);
			pcTable.getItems().clear();
			pcTable.getItems().addAll(result);
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
		
	}

}
