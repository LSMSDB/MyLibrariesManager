package mylibrariesmanager;


import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LibraryCatalogView extends TableView<Book> {
  private ObservableList<Book> observableBookList;
  
  
  public LibraryCatalogView() {
    super();
    
    observableBookList = FXCollections.observableArrayList();
    
    TableColumn<Book, String> column1 = new TableColumn<>("Title");
    column1.setCellValueFactory(new PropertyValueFactory<>("title"));
    
    TableColumn<Book, String> column2 = new TableColumn<>("Author");
    column2.setCellValueFactory(new PropertyValueFactory<>("author"));
    
    TableColumn<Book, String> column3 = new TableColumn<>("Genre");
    column2.setCellValueFactory(new PropertyValueFactory<>("genre"));
    
    TableColumn<Book, String> column4 = new TableColumn<>("Availability");
    column2.setCellValueFactory(new PropertyValueFactory<>("available"));
    
    
    this.setPlaceholder(new Label("No rows to display"));
    
    this.getColumns().add(column1);
    this.getColumns().add(column2);
    this.getColumns().add(column3);
    this.getColumns().add(column4);
    
    this.setItems(observableBookList);
  }
  
  public void updateBookList(List<Book> bookList) {    
    observableBookList.clear();
    
    ObservableList<Book> data = FXCollections.observableArrayList(bookList);
    
    observableBookList.addAll(data);
  }
}
