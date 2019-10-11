package mylibrariesmanager;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class BookStatisticsView extends BarChart<String, Number> {
	private static ObservableList<XYChart.Data<String,Number>> observableStatisticsList;

	public BookStatisticsView() {
		super(new CategoryAxis(), new NumberAxis());
		this.getXAxis().setLabel("Books");
		this.getXAxis().setLabel("Number of Borrowings");
		this.setTitle("Most borrowed books");
	}
	
	public void updateStatisticsList(List<BookStatistic> bookStatistics) {
		observableStatisticsList.clear();
		for(int i=0 ; i < bookStatistics.size() ; i++) {
			observableStatisticsList.add(new XYChart.Data<>(bookStatistics.get(i).getName(),bookStatistics.get(i).getNumberOfBorrowings()));
		}
	}
}
