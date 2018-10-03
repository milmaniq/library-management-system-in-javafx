/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.lms.db.DbConnection;
import lk.ijse.lms.model.BookTM;

/**
 *
 * @author Ilman Iqbal
 */
public class EditBookController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imgClose;
    @FXML
    private JFXTextField txtIsbn;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtAuthor;
    @FXML
    private JFXTextField txtPublisher;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;

    private BookController bookControllerAddress;
    private int bookId;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void initVariable(BookTM bookTM, BookController bookController){
        bookControllerAddress = bookController;
        
        bookId = bookControllerAddress.getBookId(bookTM);
        
        txtIsbn.setText(Integer.toString(bookTM.getIsbn()));
        txtName.setText(bookTM.getName());
        txtAuthor.setText(bookTM.getAuthor());
        txtPublisher.setText(bookTM.getPublisher());
    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE tblBook SET bookIsbn=?, bookName=?, bookAuthor=?, bookPublisher=? WHERE bookId=?");
            pstm.setObject(1, Integer.parseInt(txtIsbn.getText()));
            pstm.setObject(2, txtName.getText());
            pstm.setObject(3, txtAuthor.getText());
            pstm.setObject(4, txtPublisher.getText());
            pstm.setObject(5, bookId);
            int value = pstm.executeUpdate();
            if (value>0){
                bookControllerAddress.getAllBooks();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK);
                a.show();
                imgCloseMouseClicked(null);
            }else{
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Insert", ButtonType.OK);
                a.show();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EditBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnClearOnAction(ActionEvent event) {
        txtIsbn.setText("");
        txtName.setText("");
        txtAuthor.setText("");
        txtPublisher.setText("");
    }


    @FXML
    private void imgCloseMouseClicked(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
    
}
