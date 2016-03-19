package com.jdc.phoneshop.controller.warehouse;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jdc.phoneshop.controller.warehouse.StoreHome.ChartData;
import com.jdc.phoneshop.warehouse.model.WarehouseModel;
import com.jdc.phoneshop.warehouse.model.entity.Item;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BuyerHome implements Initializable{
	
	@FXML
	private HBox btnView;
	@FXML
	private BarChart<String, Number> chart;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnView.getChildren().stream().filter(a -> a instanceof VBox)
			.forEach(view -> view.setOnMouseClicked(event -> {
				String id = view.getId();
				selectButton(id);
				
				switch (id) {
				case "mobile":
					this.loadChart(1);
					break;
				case "tablet":
					this.loadChart(2);
					break;
				default:
					this.loadChart(3);
					break;
				}
			}));
		
		chart.setTitle("Sale Conditions");
		chart.getXAxis().setLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
		loadChart(1);
	}
	
	private void loadChart(int categoryId) {
		
		chart.getData().clear();
		
		Map<Item, Map<LocalDate, Integer>> result = WarehouseModel.getBuyerModel().getCondition(categoryId, 
				LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1), LocalDate.now().plusDays(1));

		List<Map.Entry<Item, Map<LocalDate, Integer>>> dataList = new ArrayList<>(result.entrySet());
		
		List<ChartData> chartDataList = new ArrayList<>();
		
		for(Map.Entry<Item, Map<LocalDate, Integer>> entry : dataList) {

			ChartData chartData = new ChartData(entry.getKey().getModel());
			chartDataList.add(chartData);
			Map<LocalDate, Integer> data = entry.getValue();
			
			for(LocalDate date : data.keySet()) {
				chartData.add(new XYChart.Data<>(date.format(DateTimeFormatter.ofPattern("MMM-dd")), data.get(date)));
			}
		}
		
		Collections.sort(chartDataList);
		
		chartDataList.forEach(a -> chart.getData().add(a.getSeries()));
		chart.getXAxis().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
	}

	private void selectButton(String id) {
		btnView.getChildren().stream().filter(a -> a instanceof VBox)
			.forEach(view -> {
				if(view.getId().equals(id)) {
					view.getStyleClass().remove("home-btn-default");
					view.getStyleClass().add("home-btn-selected");
				} else {
					view.getStyleClass().remove("home-btn-selected");
					view.getStyleClass().add("home-btn-default");
				}
			});
		
	}

}
