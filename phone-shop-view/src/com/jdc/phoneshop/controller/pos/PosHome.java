package com.jdc.phoneshop.controller.pos;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jdc.phoneshop.controller.pos.CustomerDialog.CustomerSelectHandler;
import com.jdc.phoneshop.controller.pos.MakerTab.ItemSelectHandler;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.controller.utils.SecurityManager;
import com.jdc.phoneshop.pos.model.BillService;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.pos.model.ItemModel.ItemVO;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PosHome implements Initializable, ItemSelectHandler, CustomerSelectHandler {

	@FXML
	private HBox btnBar;
	@FXML
	private TabPane itemPane;
	@FXML
	private Label message;
	@FXML
	private TableView<BillItemVO> table;

	@FXML
	private TableColumn<BillItemVO, String> colId;
	@FXML
	private TableColumn<BillItemVO, String> colItem;
	@FXML
	private TableColumn<BillItemVO, String> colCount;
	@FXML
	private TableColumn<BillItemVO, String> colPrice;

	@FXML
	private Label total;
	@FXML
	private Label userName;

	@FXML
	private TextField txtSubTotal;
	@FXML
	private TextField txtTax;
	@FXML
	private TextField txtTotal;
	@FXML
	private TextField txtPaid;
	@FXML
	private TextField txtRemainance;
	
	@FXML
	private TextField inName;
	@FXML
	private TextField inPhone;
	@FXML
	private TextField inEmail;
	@FXML
	private TextField inAddress;

	public static void show() {

		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(PosHome.class.getResource("PosHome.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			MessageHandler.handle(new ApplicationException(e));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MessageHandler.setMESSAGE(message);

		try {
			userName.setText(SecurityManager.getEmployee().getName());
			
			setDefaultBtns();
			VBox mobileBtn = btnBar.getChildren().stream().filter(a -> a instanceof VBox)
					.filter(a -> a.getId().equals("mobile")).map(a -> (VBox) a).findFirst().get();

			if (null != mobileBtn) {
				mobileBtn.getStyleClass().add("btn-selected");
			}

			this.loadCategory("mobile");

			// define table
			defineTable();

			txtPaid.textProperty().addListener((a, b, c) -> setPrices());

			clearInputs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void defineTable() {
		// define table columns
		colId.setCellValueFactory(p -> {
			if (null != p) {
				BillItemVO vo = p.getValue();
				return new SimpleStringProperty(String.valueOf(vo.getItem().getId()));
			}
			return null;
		});
		colItem.setCellValueFactory(p -> {
			if (null != p) {
				BillItemVO vo = p.getValue();
				return new SimpleStringProperty(vo.getItem().getModel());
			}
			return null;
		});
		colCount.setCellValueFactory(p -> {
			if (null != p) {
				BillItemVO vo = p.getValue();
				return new SimpleStringProperty(String.valueOf(vo.getBillItem().getCount()));
			}
			return null;
		});
		colPrice.setCellValueFactory(p -> {
			if (null != p) {
				BillItemVO vo = p.getValue();
				StringProperty props = new SimpleStringProperty();
				props.bind(vo.totalProperty().asString());
				return props;
			}
			return null;
		});

		// edit columns
		colCount.setCellFactory(TextFieldTableCell.forTableColumn());
		colCount.setOnEditCommit(event -> {
			try {
				BillItemVO vo = event.getRowValue();
				vo.getBillItem().setCount(Integer.parseInt(event.getNewValue()));
				vo.setBillItem(vo.getBillItem());
				setPrices();
			} catch (NumberFormatException e) {
				MessageHandler.showDialogMessage("Please enter digit only!");
			}
		});

		table.setEditable(true);

		// delete row
		MenuItem delete = new MenuItem("delete");
		delete.setOnAction(event -> {
			table.getItems().remove(table.getSelectionModel().getSelectedItem());
			setPrices();
		});
		table.setContextMenu(new ContextMenu(delete));
	}

	public void loadCategory(MouseEvent e) {
		setDefaultBtns();
		VBox box = (VBox) e.getSource();
		box.getStyleClass().add("btn-selected");

		// todo
		loadCategory(box.getId());
	}

	public void btnMouseOver(MouseEvent e) {
		VBox box = (VBox) e.getSource();
		box.getStyleClass().add("btn-mouse-o");
	}

	public void btnMouseExit(MouseEvent e) {
		VBox box = (VBox) e.getSource();
		box.getStyleClass().remove("btn-mouse-o");
	}

	public void loadUsers(MouseEvent e) {
		try {
			CustomerDialog.showDialog(this);
		} catch (ApplicationException ex) {
			MessageHandler.handle(ex);
		}
	}

	private void loadCategory(String string) {
		int catId = 0;
		if (string.equals("mobile")) {
			catId = 1;
		} else if (string.equals("tablet")) {
			catId = 2;
		} else if (string.equals("accessory")) {
			catId = 3;
		}

		Map<Maker, List<ItemVO>> map = ItemModel.getModel().findByCategory(catId);

		this.itemPane.getTabs().clear();
		map.keySet().stream().forEach(a -> {
			MakerTab tab = new MakerTab(a.getMaker(), map.get(a), this);
			this.itemPane.getTabs().add(tab);
		});
	}

	private void setDefaultBtns() {
		btnBar.getChildren().stream().filter(a -> a instanceof VBox)
				.filter(a -> a.getStyleClass().contains("header-btn")).forEach(a -> {
					a.getStyleClass().remove("btn-selected");
					a.getStyleClass().remove("btn-default");
					a.getStyleClass().add("btn-default");
				});
	}

	@Override
	public void selectItem(ItemVO item) {
		try {
			BillItem billItem = new BillItem();
			billItem.setItemId(item.getItem().getId());
			billItem.setCount(1);
			billItem.setUnitPrice(item.getPrice());
			BillItemVO vo = new BillItemVO();
			vo.setBillItem(billItem);
			vo.setItem(item.getItem());

			table.getItems().add(vo);

			setPrices();
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	private void setPrices() {
		Double subTotal = table.getItems().stream()
				.mapToDouble(a -> a.getBillItem().getCount() * a.getBillItem().getUnitPrice()).sum();
		Double tax = subTotal * 0.05;
		Double total = subTotal + tax;

		this.total.setText(String.valueOf(total.intValue()));
		this.txtSubTotal.setText(String.valueOf(subTotal.intValue()));
		this.txtTax.setText(String.valueOf(tax.intValue()));
		this.txtTotal.setText(String.valueOf(total.intValue()));

		String strPaid = txtPaid.getText();
		if (!StringUtils.isEmptyString(strPaid)) {
			Double paid = Double.valueOf(strPaid);
			Double remainance = paid - total;

			txtPaid.setText(String.valueOf(paid.intValue()));
			txtRemainance.setText(String.valueOf(remainance.intValue()));
		} else {
			txtRemainance.clear();
		}
	}

	public void clear(MouseEvent e) {
		table.getItems().clear();
		this.clearInputs();
	}

	private void clearInputs() {
		
		this.selectdCustomer = null;
		
		total.setText("0");
		txtPaid.clear();
		txtRemainance.clear();
		txtSubTotal.clear();
		txtTax.clear();
		txtTotal.clear();
		
		this.inName.clear();
		this.inPhone.clear();
		this.inEmail.clear();
		this.inAddress.clear();
		
		setCustomerStatus();
	}
	
	public void paid() {
		try {
			// check 
			if(table.getItems().size() == 0) {
				throw new ApplicationException(ErrorType.Warning, "Please select Items to card!");
			}
			
			// check paid price
			if(!StringUtils.isEmptyString(txtRemainance.getText())) {
				int rem = Integer.parseInt(txtRemainance.getText());
				if(rem < 0) {
					throw new ApplicationException(ErrorType.Warning, "Please check Paid Price!");
				}
			} else {
				throw new ApplicationException(ErrorType.Warning, "Please check Paid Price!");
			}
			
			// get customer data
			Customer c = getCustomer();

			// check customer name
			if(StringUtils.isEmptyString(c.getName())) {
				throw new ApplicationException(ErrorType.Warning, "Please enter Customer Name!");
			}
			// check customer phone
			if(StringUtils.isEmptyString(c.getPhone())) {
				throw new ApplicationException(ErrorType.Warning, "Please enter Customer Phone!");
			}

			// create bill service
			BillService service = BillService.getService(c, SecurityManager.getEmployee());
			// get bill items
			service.addItem(table.getItems().stream().map(a -> a.getBillItem()).collect(Collectors.toSet()));
			
			service.create();
			this.clear(null);
			MessageHandler.showDialogMessage("Your operation has been done successfully!");
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	}

	private Customer getCustomer() {
		if(null != selectdCustomer) {
			return selectdCustomer;
		}
		Customer c = new Customer();
		c.setName(inName.getText());
		c.setPhone(inPhone.getText());
		c.setEmail(inEmail.getText());
		c.setAddress(inAddress.getText());
		return c;
	}

	@Override
	public void select(Customer customer) {
		selectdCustomer = customer;
		inName.setText(selectdCustomer.getName());
		inPhone.setText(selectdCustomer.getPhone());
		inEmail.setText(selectdCustomer.getEmail());
		inAddress.setText(selectdCustomer.getAddress());
		setCustomerStatus();
	}
	
	private void setCustomerStatus() {
		this.inName.setEditable(selectdCustomer == null);
		this.inPhone.setEditable(selectdCustomer == null);
		this.inEmail.setEditable(selectdCustomer == null);
		this.inAddress.setEditable(selectdCustomer == null);
	}
	
	private Customer selectdCustomer;
	
}
