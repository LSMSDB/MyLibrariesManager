package mylibrariesmanager;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class BookStatisticsView extends BarChart<String, Number> {
	private ObservableList<XYChart.Data<String,Number>> observableStatisticsList;
	
	public BookStatisticsView() {
		super(new CategoryAxis(), new NumberAxis());
		this.getXAxis().setLabel("Books");
		this.getYAxis().setLabel("Number of Borrowings");
		this.setTitle("Most borrowed books");
		
		setObservableStatisticsList(FXCollections.synchronizedObservableList(FXCollections.observableList(new ArrayList<XYChart.Data<String, Number>>())));
		XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
		serie.getData().addAll(observableStatisticsList);
		this.getData().add(serie);
	}
	
	public void updateStatisticsList(List<BookStatistic> bookStatistics) {
		ObservableList<XYChart.Data<String, Number>> intermediate = FXCollections.synchronizedObservableList(FXCollections.observableList(new ArrayList<XYChart.Data<String, Number>>()));
		for(int i=0 ; i < bookStatistics.size() ; i++) {
			intermediate.add(new XYChart.Data<>(bookStatistics.get(i).getName(),bookStatistics.get(i).getNumberOfBorrowings()));
		}
		setObservableStatisticsList(intermediate);
		XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
		serie.getData().addAll(observableStatisticsList);
		this.getData().add(serie);
	}

	public ObservableList<XYChart.Data<String,Number>> getObservableStatisticsList() {
		return observableStatisticsList;
	}

	public void setObservableStatisticsList(ObservableList<XYChart.Data<String,Number>> observableStatisticsList) {
		this.observableStatisticsList = observableStatisticsList;
	}
}
