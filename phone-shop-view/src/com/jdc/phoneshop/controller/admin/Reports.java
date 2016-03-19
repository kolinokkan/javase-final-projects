package com.jdc.phoneshop.controller.admin;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.jdc.phoneshop.admin.model.ReportAdminModel;
import com.jdc.phoneshop.admin.model.vo.ReportVO;
import com.jdc.phoneshop.admin.model.vo.ReportVO.Type;
import com.jdc.phoneshop.controller.utils.MessageHandler;
import com.jdc.phoneshop.utils.ApplicationException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class Reports implements Initializable {
	
	@FXML
	private HBox types;
	@FXML
	private DatePicker dateFrom;
	@FXML
	private DatePicker dateTo;
	@FXML
	private Label totalIncomes;
	@FXML
	private Label totalExpenses;
	@FXML
	private Label totalBalance;
	
	@FXML
	private TableView<ReportVO> table;
	@FXML
	private TableColumn<ReportVO, String> colRefDate;
	@FXML
	private TableColumn<ReportVO, String> colType;
	@FXML
	private TableColumn<ReportVO, String> colCategory;
	@FXML
	private TableColumn<ReportVO, String> colMaker;
	@FXML
	private TableColumn<ReportVO, String> colItem;
	@FXML
	private TableColumn<ReportVO, String> colUnitPrice;
	@FXML
	private TableColumn<ReportVO, String> colCount;
	@FXML
	private TableColumn<ReportVO, String> colIncome;
	@FXML
	private TableColumn<ReportVO, String> colExpense;
	
	private IntegerProperty totalIncomeProperty;
	private IntegerProperty totalExpenseProperty;
	private IntegerProperty totalBalanceProperty;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			
			totalIncomeProperty = new SimpleIntegerProperty(0);
			totalExpenseProperty = new SimpleIntegerProperty(0);
			totalBalanceProperty = new SimpleIntegerProperty(0);
			totalBalanceProperty.bind(totalIncomeProperty.subtract(totalExpenseProperty));
			
			totalIncomes.textProperty().bind(totalIncomeProperty.asString());
			totalExpenses.textProperty().bind(totalExpenseProperty.asString());
			totalBalance.textProperty().bind(totalBalanceProperty.asString());
			
			dateFrom.setValue(getFirstDatOfThisMonth());
			dateTo.setValue(LocalDate.now());
			
			colRefDate.setCellValueFactory(new PropertyValueFactory<>("refDate"));
			colType.setCellValueFactory(new PropertyValueFactory<>("type"));
			colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
			colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
			colCategory.setCellValueFactory(param -> {
				if(null != param) {
					ReportVO vo = param.getValue();
					return new SimpleStringProperty(vo.getCategory().getName());
				}
				return null;
			});
			colMaker.setCellValueFactory(param -> {
				if(null != param) {
					ReportVO vo = param.getValue();
					return new SimpleStringProperty(vo.getMaker().getMaker());
				}
				return null;
			});
			colItem.setCellValueFactory(param -> {
				if(null != param) {
					ReportVO vo = param.getValue();
					return new SimpleStringProperty(vo.getItem().getModel());
				}
				return null;
			});
			colIncome.setCellValueFactory(param -> {
				if(null != param) {
					ReportVO vo = param.getValue();
					if(vo.getType().equals(Type.SELL)) {
						return new SimpleStringProperty(String.valueOf(vo.getCount() * vo.getPrice()));
					}
				}
				return null;
			});
			colExpense.setCellValueFactory(param -> {
				if(null != param) {
					ReportVO vo = param.getValue();
					if(vo.getType().equals(Type.PURCHASE)) {
						return new SimpleStringProperty(String.valueOf(vo.getCount() * vo.getPrice()));
					}
				}
				return null;
			});
			
			types.getChildren().stream().map(a -> (ToggleButton)a).forEach(a -> a.setOnAction(e -> search()));
			dateFrom.valueProperty().addListener((a, b, c) -> search());
			dateTo.valueProperty().addListener((a, b, c) -> search());
			
			search();
			
		} catch (ApplicationException e) {
			MessageHandler.handle(e);
		}
	
	}
	
	private void search() {
		
		// date from
		if(null == dateFrom.getValue()) {
			dateFrom.setValue(getFirstDatOfThisMonth());
		}
		
		// date to
		if(null == dateTo.getValue()) {
			dateTo.setValue(LocalDate.now());
		}
		
		Set<ReportVO> result = ReportAdminModel.getModel().getReport(dateFrom.getValue(), dateTo.getValue(), getSelectType());
		table.getItems().clear();
		table.getItems().addAll(result);
		
		totalIncomeProperty.set(0);
		totalExpenseProperty.set(0);
		
		table.getItems().forEach(vo -> {
			if(vo.getType().equals(Type.PURCHASE)) {
				int val = totalExpenseProperty.get();
				int newVal = vo.getCount() * vo.getPrice();
				totalExpenseProperty.set(val + newVal);
			} else {
				int val = totalIncomeProperty.get();
				int newVal = vo.getCount() * vo.getPrice();
				totalIncomeProperty.set(val + newVal);
			}
		});
	}
	
	private Type getSelectType() {
		List<ToggleButton> list = types.getChildren().stream().filter(a -> a instanceof ToggleButton)
				.map(a -> (ToggleButton)a)
				.collect(Collectors.toList());
		
		for (int i = 0; i < list.size(); i++) {
			if(i > 0 && list.get(i).isSelected()) {
				return Type.values()[i -1]; 
			}
		}
		
		list.get(0).setSelected(true);
		return null;
	}
	
	private LocalDate getFirstDatOfThisMonth() {
		return LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
	}

}
