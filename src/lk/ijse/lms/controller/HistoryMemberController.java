/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.lms.db.DbConnection;
import lk.ijse.lms.model.HistoryMemberTM;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class HistoryMemberController implements Initializable {

    @FXML
    private TableView<HistoryMemberTM> tblHistory;
    @FXML
    private ImageView imgClose;
    @FXML
    private AnchorPane root;

    private int memberId;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblHistory.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblHistory.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblHistory.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblHistory.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("publisher"));
        tblHistory.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        tblHistory.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        
        
    }    

    void initVariable(int memberId) {
        
        this.memberId = memberId;
        getHistory();
    }
    
    void getHistory(){
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT bookIsbn, bookName, bookAuthor, bookPublisher, lendDate, returnDate  FROM tblBook b, tblReturn r, tblLend l WHERE l.memberId = "+this.memberId+" AND l.bookId=b.bookId AND l.lendId = r.returnId ORDER BY r.returnDate");
            ArrayList<HistoryMemberTM> historyList = new ArrayList<>();
            while (rst.next()){
                HistoryMemberTM historyMemberTM = new HistoryMemberTM(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
                historyList.add(historyMemberTM);
            }
            tblHistory.setItems(FXCollections.observableArrayList(historyList));
        } catch (SQLException ex) {
            Logger.getLogger(HistoryMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HistoryMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void imgCloseOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    
}
