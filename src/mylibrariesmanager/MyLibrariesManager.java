package mylibrariesmanager;


import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;


public class MyLibrariesManager extends Application {
    private LibraryCatalogView libraryCatalog;
    private UserBorrowingsView userBorrowings;
    private BookStatisticsView bookStatistics;
    private Map<String, ComboBox> selectionMenu;
    private Map<String, TextField> createAccountField;
    private Map<String, Button> actionButton;
    private Map<String, Label> descriptiveLabel;

    
    private VBox buildCreateAccountSection(){
        actionButton.put("Create account", new Button("Create account"));
        String[] fieldNames = new String[]{"Name", "Surname", "Address", "Phone", "Email"};
        
        for(String label : fieldNames){
            TextField newField = new TextField();
            newField.promptTextProperty().set(label);
            newField.setMinSize(200,10);
            createAccountField.put(label, newField);
        }
        
        
        VBox createAccountVbox = new VBox(10);  
        createAccountVbox.setAlignment(Pos.CENTER);
        createAccountVbox.getChildren().addAll(new HBox(5, createAccountField.get("Name"), createAccountField.get("Surname")),
                                               createAccountField.get("Address"),
                                               createAccountField.get("Phone"), 
                                               createAccountField.get("Email"),
                                               actionButton.get("Create account"));
        
        return createAccountVbox;
    }
    
    private VBox buildUserAccountSection(){
        selectionMenu.put("Select user", new ComboBox());
        actionButton.put("Delete account", new Button("Delete account"));
        descriptiveLabel.put("Select user", new Label("Select a user"));
        
        VBox userAccountVBox = new VBox(30);
        userAccountVBox.setAlignment(Pos.BASELINE_RIGHT);
        userAccountVBox.getChildren().addAll(new HBox(20, descriptiveLabel.get("Select user"), selectionMenu.get("Select user")),
                                             actionButton.get("Delete account"));
        
        return userAccountVBox;
    }
    
    private HBox buildMyBorrowingsSection(){
        userBorrowings = new UserBorrowingsView();
        actionButton.put("Renew book", new Button("Renew the selected book"));
        actionButton.put("Return book", new Button("Return the selected book"));
        
        VBox renewReturnVBox = new VBox(30);
        renewReturnVBox.setAlignment(Pos.CENTER);
        renewReturnVBox.getChildren().addAll(actionButton.get("Renew book"), actionButton.get("Return book"));
        
        return new HBox(renewReturnVBox);
    }
    
    private HBox buildLibraryCatalogSection(){
        libraryCatalog = new LibraryCatalogView();
        selectionMenu.put("Select library", new ComboBox());
        descriptiveLabel.put("Select library", new Label("Select a library"));
        actionButton.put("Borrow book", new Button("Borrow the selected book"));
        
        VBox selectBorrowVBox = new VBox(15);
        selectBorrowVBox.setAlignment(Pos.CENTER);
        selectBorrowVBox.getChildren().addAll(descriptiveLabel.get("Select library"),
                                              selectionMenu.get("Select library"),
                                              actionButton.get("Borrow book"));
        
        return new HBox(selectBorrowVBox);
    }
    
    private HBox buildStatisticsSection(){
        bookStatistics = new BookStatisticsView();
        descriptiveLabel.put("Most borrowed books", new Label("Most borrowed books"));
        selectionMenu.put("Select genre", new ComboBox());
        descriptiveLabel.put("Select genre", new Label("Select a genre"));
            
        VBox selectGenreVBox = new VBox(15);
        selectGenreVBox.setAlignment(Pos.CENTER);
        selectGenreVBox.getChildren().addAll(descriptiveLabel.get("Select genre"),
                                             selectionMenu.get("Select genre"));
        
        return new HBox(150,bookStatistics, selectGenreVBox);
    }
    
    private void setCreateAccountEvent(){
        actionButton.get("Create account").setOnAction((ActionEvent event)->{
            int missingValues = 0;
            
            for(TextField textField : createAccountField.values()){
                if (textField.getText().isEmpty()){
                    textField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    missingValues++;
                } else
                    textField.setStyle(null);
            }
            
            if (missingValues == 0){
                User newUser = new User(0, 
                                        createAccountField.get("Name").getText(), 
                                        createAccountField.get("Surname").getText(),
                                        createAccountField.get("Address").getText(),
                                        createAccountField.get("Email").getText(),
                                        createAccountField.get("Phone").getText(),
                                        null);
                //if (!LibrariesArchive.addUser(newUser))
                //        errorMessage("An error occurred while creating the account. Please try again");
            }
        });
    }
    
    private void setSelectUserEvent(){
        selectionMenu.get("Select user").setOnShowing((Event event)->{
            selectionMenu.get("Select user").setItems(null); // Used to make available the OnAction event every time the ComboBox is opened
            selectionMenu.get("Select user").setItems(FXCollections.observableArrayList(/*LibrariesArchive.retrieveUsers()*/));
        });
         
        selectionMenu.get("Select user").setOnAction((Event event)->{
            User selectedUser = (User) selectionMenu.get("Select user").getValue();
            //userBorrowings.updateBorrowingList(selectedUser.getBorrowings());
         });   
    }
    
    private void setDeleteAccountEvent(){
        actionButton.get("Delete account").setOnAction((ActionEvent event)->{
            User selectedUser = (User) selectionMenu.get("Select user").getValue();
            
            if (selectedUser != null ){
                //LibrariesArchive.deleteUser(selectedUser);
                // CHECK FOR ERRORS
            }else
                 errorMessage("Please select your account from the list");
        });
    }
    
    private void setRenewBookEvent(){
        actionButton.get("Renew book").setOnAction((ActionEvent event)->{
            User selectedUser = (User) selectionMenu.get("Select user").getValue();
            Borrowing selectedBorrowing = null; // = userBorrowings.getSelectionModel().getSelectedItem();
            
            if (selectedUser != null && selectedBorrowing != null){
                if (!selectedBorrowing.getReturnDate().isEmpty())
                    return;
                
                LocalDate newExpirationDate = LocalDate.parse(selectedBorrowing.getExpirationDate(), 
                                                              DateTimeFormatter.ofPattern("dd-MM-yy"));
                newExpirationDate = newExpirationDate.plus(1, ChronoUnit.WEEKS);
                        
                String expirationDate = newExpirationDate.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                Borrowing renewedBorrowing = new Borrowing(selectedBorrowing.getId(),
                                                           selectedBorrowing.getBook(),
                                                           selectedBorrowing.getBorrowingDate(),
                                                           selectedBorrowing.getReturnDate(),
                                                           expirationDate);
                
                //LibrariesArchive.renewBorrowing(renewedBorrowing);
                // CHECK ERRORS
                // IF NO ERRORS, UPLOAD BORROWINGS TABLE TO SHOW RENEWED BORROWING
            }
        });
    }
    
    private void setReturnBookEvent(){
        actionButton.get("Return book").setOnAction((ActionEvent event)->{
            User selectedUser = (User) selectionMenu.get("Select user").getValue();
            Borrowing selectedBorrowing = null; // = userBorrowings.getSelectionModel().getSelectedItem();
            
            if (selectedUser != null && selectedBorrowing != null){
                if (!selectedBorrowing.getReturnDate().isEmpty())
                    return;
                
                LocalDate currentDate = LocalDate.now();
                String returnDate = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                Borrowing expiredBorrowing = new Borrowing(selectedBorrowing.getId(),
                                                           selectedBorrowing.getBook(),
                                                           selectedBorrowing.getBorrowingDate(),
                                                           returnDate,
                                                           selectedBorrowing.getExpirationDate());
                
                //LibrariesArchive.endBorrowing(expiredBorrowing);
                // CHECK ERRORS
                // IF NO ERRORS, UPLOAD BORROWINGS TABLE TO SHOW RETURNED BORROWING
            }
        });
    }
    
    private void setSelectLibraryEvent(){
        selectionMenu.get("Select library").setOnShowing((Event event)->{
            selectionMenu.get("Select library").setItems(null); // Used to make available the OnAction event every time the ComboBox is opened
            selectionMenu.get("Select library").setItems(FXCollections.observableArrayList(/*LibrariesArchive.retrieveLibraries()*/));
        });
         
        selectionMenu.get("Select library").setOnAction((Event event)->{
            Library selectedLibrary = (Library) selectionMenu.get("Select library").getValue();
            //libraryCatalog.updateBookList(selectedLibrary.getCatalog());
         }); 
    }
    
    private void setBorrowBookEvent(){
        actionButton.get("Borrow book").setOnAction((ActionEvent event)->{
            User selectedUser = (User) selectionMenu.get("Select user").getValue();
            Library selectedLibrary = (Library) selectionMenu.get("Select library").getValue();
            Book selectedBook = null; // = libraryCatalog.getSelectionModel().getSelectedItem();
            
            if (selectedUser != null && selectedLibrary != null && selectedBook != null){
                if (!selectedBook.getAvailable())
                    return;
                
                LocalDate currentDate = LocalDate.now();
                LocalDate currentDatePlusTwoWeeks = LocalDate.now().plus(2, ChronoUnit.WEEKS);
                
                String borrowingDate = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                String expirationDate = currentDatePlusTwoWeeks.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                
                Borrowing newBorrowing = new Borrowing(0, selectedBook, borrowingDate, "", expirationDate);
                //LibrariesArchive.addBorrowing(selectedUser, newBorrowing);
                // CHECK ERRORS
                // IF NO ERRORS, UPLOAD BORROWINGS TABLE TO SHOW THE NEW BORROWING
            }
        });
    }
    
    private void setSelectGenreEvent(){
        selectionMenu.get("Select genre").setOnShowing((Event event)->{
            Library selectedLibrary = (Library) selectionMenu.get("Select library").getValue();
            selectionMenu.get("Select genre").setItems(null); // Used to make available the OnAction event every time the ComboBox is opened
            
            if (selectedLibrary != null)
                selectionMenu.get("Select genre").setItems(FXCollections.observableArrayList(/*LibrariesArchive.retrieveGenres(selectedLibrary)*/));      
        });
         
        selectionMenu.get("Select genre").setOnAction((Event event)->{
            Library selectedLibrary = (Library) selectionMenu.get("Select library").getValue();
            Genre selectedGenre = (Genre) selectionMenu.get("Select genre").getValue();
            
            if (selectedLibrary != null)
                bookStatistics.updateStatisticsList(null);//LibrariesArchive.retrieveMostBorrowedBooks(selecteLibrary, selectedGenre);
         }); 
    }
    
    private void setInterfaceEvents(){
        setCreateAccountEvent();
        setSelectUserEvent();
        setDeleteAccountEvent();
        setRenewBookEvent();
        setReturnBookEvent();
        setSelectLibraryEvent();
        setBorrowBookEvent();
        setSelectGenreEvent();
    }
    
    private void setComponentsLayout(){
        for(Button button : actionButton.values()){
            button.setMinSize(20,30);
            button.setStyle("-fx-font-weight: bold;");
        }
        
        for (Label label : descriptiveLabel.values()){
            label.setStyle("-fx-font-weight: bold;");
            if (label.getText().equals("Most borrowed books"))
                label.setStyle("-fx-font-size: 20px");
        }
        
        for (ComboBox box : selectionMenu.values())
            box.setMinSize(100, 10);
    }
    
    private void errorMessage(String message){
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }
    
    public void start(Stage stage){
        // LocalConfigurationParameters.retrieveLocalConfiguration();
        // LibrariesArchive.initializeConnection();
        // CHECK IF AN ERROR OCCURRED IN BOTH THE FUNCTIONS --> IF SO, ABORT THE APP
              
        VBox interfaceVBox = new VBox(30);
        interfaceVBox.setPadding(new Insets(20, 10, 10, 20));
        
        selectionMenu = new HashMap<>();
        createAccountField = new HashMap<>();
        actionButton = new HashMap<>();
        descriptiveLabel = new HashMap<>();
        
        interfaceVBox.getChildren().addAll(new HBox(150, buildCreateAccountSection(), buildUserAccountSection()),
                                           buildMyBorrowingsSection(),
                                           buildLibraryCatalogSection(),
                                           buildStatisticsSection());
        
        setComponentsLayout();
        setInterfaceEvents();
                       
        ScrollPane scrollRoot = new ScrollPane(new Group(interfaceVBox));
        stage.setScene(new Scene(scrollRoot));
        stage.setTitle("MyLibrariesManager");
        stage.setMaximized(true);
        stage.show();        
    }
    
}
