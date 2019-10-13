package mylibrariesmanager;

import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class UserBorrowingsView extends TableView<Borrowing> {
  private ObservableList<Borrowing> observableBorrowing;
  
  
  public UserBorrowingsView() {
    super(FXCollections.observableArrayList());
    
    TableColumn<Borrowing, String> column1 = new TableColumn<>("Title");
    
    column1.setCellValueFactory(new Callback<CellDataFeatures<Borrowing, String>, 
        ObservableValue<String>>() {  
      @Override  
      public ObservableValue<String> call(CellDataFeatures<Borrowing, String> data){  
        return data.getValue().getBook().nameProperty();  
      }  
    });
    
    TableColumn<Borrowing, String> column2 = new TableColumn<>("Author");
    
    column2.setCellValueFactory(new Callback<CellDataFeatures<Borrowing, String>, 
        ObservableValue<String>>() {  
      @Override  
      public ObservableValue<String> call(CellDataFeatures<Borrowing, String> data){  
        return data.getValue().getBook().authorProperty();  
      }  
    });
    
    TableColumn<Borrowing, String> column3 = new TableColumn<>("Borrowing Date");
    column2.setCellValueFactory(new PropertyValueFactory<>("borrowingDate"));
    
    TableColumn<Borrowing, String> column4 = new TableColumn<>("Expiration Date");
    column2.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
    
    
    this.setPlaceholder(new Label("No rows to display"));
    
    this.getColumns().add(column1);
    this.getColumns().add(column2);
    this.getColumns().add(column3);
    this.getColumns().add(column4);   
  }
  
  public void updateBorrowingList(List<Borrowing> borrowingList) {
    observableBorrowing.clear();
    
    ObservableList<Borrowing> data = FXCollections.observableArrayList(borrowingList);
    
    observableBorrowing.addAll(data);
  }
}
