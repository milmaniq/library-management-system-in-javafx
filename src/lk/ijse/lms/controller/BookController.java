/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.lms.db.DbConnection;
import lk.ijse.lms.model.BookTM;
import lk.ijse.lms.observer.Observer;
import lk.ijse.lms.observer.Subject;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class BookController implements Initializable/*, Subject<Observer>*/ {
    
    
    
    @FXML
    private TableView<BookTM> tblBook;
    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgAddBook;
    
    private BookController bookControllerAddress;
    @FXML
    private JFXTextField txtSearchBooks;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookControllerAddress = this;
        
        tblBook.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblBook.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblBook.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblBook.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("publisher"));
        tblBook.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("status"));
        tblBook.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("edit"));
        tblBook.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("remove"));
        
        getAllBooks();
        
        tblBook.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                int column = tblBook.getSelectionModel().getSelectedCells().get(0).getColumn();
                if (column == 5) {
                    BookTM bookTM = tblBook.getSelectionModel().getSelectedItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/lms/view/EditBook.fxml"));
                        Parent p = loader.load();
                        EditBookController controller = loader.<EditBookController>getController();
                        controller.initVariable(bookTM, bookControllerAddress);
                        
                        Scene scene = new Scene(p);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else if (column == 6) {
                    try {
                        BookTM bookTM = tblBook.getSelectionModel().getSelectedItem();
                        int bookId = getBookId(bookTM);
                        Connection connection = DbConnection.getInstance().getConnection();
                        PreparedStatement pstm = connection.prepareStatement("DELETE FROM tblBook WHERE bookId = '" + bookId + "'");
                        int value = pstm.executeUpdate();
                        if (value > 0) {
                            getAllBooks();
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK);
                            a.show();
                        } else {
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Delete", ButtonType.OK);
                            a.show();
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    
    public void getAllBooks() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM tblBook");
            ArrayList<BookTM> bookList = new ArrayList<>();
            while (rst.next()) {
                BookTM bookTM = new BookTM(rst.getInt(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), "Edit", "Remove");
                bookList.add(bookTM);
            }
            tblBook.setItems(FXCollections.observableArrayList(bookList));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getBookId(BookTM bookTM) {
        int bookId = 0;
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT bookId FROM tblBook WHERE bookIsbn = " + bookTM.getIsbn() + "");
            if (rst.next()) {
                bookId = rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bookId;
    }
    
     @FXML
    private void imgAddBookOnClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/lms/view/AddBook.fxml"));
            Parent p = loader.load();
            AddBookController controller = loader.<AddBookController>getController();
            controller.initVariable(this);
            Scene scene = new Scene(p);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @FXML
    private void txtSearchBooksOnKeyReleased(KeyEvent event) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM tblBook WHERE bookName LIKE '" + txtSearchBooks.getText() + "%' OR bookIsbn LIKE '" + txtSearchBooks.getText() + "%'");
            ArrayList<BookTM> bookNames = new ArrayList<>();
            while (rst.next()) {
                BookTM bookTM = new BookTM(rst.getInt(2),rst.getString(3),rst.getString(4),rst.getString(5),rst.getString(6), "Edit", "Remove");
                bookNames.add(bookTM);
            }
            
            tblBook.setItems(FXCollections.observableArrayList(bookNames));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public static ArrayList<Observer> allObservers = new ArrayList<>();
//    @Override
//    public void notyfyall(String message) {
//        for (Observer allObserver : allObservers) {
//            new Thread() {
//                public void run() {
//                    allObserver.update(message);
//                }
//            }.start();
//        }
//    }
//    
//    @Override
//    public void registerObserver(Observer observer) {
//        this.allObservers.add(observer);
//    }
//    
//    @Override
//    public void unregister(Observer observer) {
//        this.allObservers.remove(observer);
//    }

    @FXML
    private void btnBackOnMouseClicked(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/lms/view/Dashboard.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

   
}
