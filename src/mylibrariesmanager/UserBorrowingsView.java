package mylibrariesmanager;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserBorrowingsView extends TableView<Borrowing> {
  public UserBorrowingsView() {
    super(FXCollections.observableArrayList());
    
    TableColumn<Borrowing, String> column1 = new TableColumn<>("Title");
    column1.setCellValueFactory(new PropertyValueFactory<>("title"));
    
    TableColumn<Borrowing, String> column2 = new TableColumn<>("Author");
    column2.setCellValueFactory(new PropertyValueFactory<>("author"));
    
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
  
  public void updateBookList(List<Borrowing> borrowingList) {
    this.getItems().clear();
    
    ObservableList<Borrowing> data = FXCollections.observableArrayList(borrowingList);
    
    this.setItems(data);
  }

}
