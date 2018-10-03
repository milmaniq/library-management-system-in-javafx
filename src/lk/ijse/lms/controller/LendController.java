/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.lms.db.DbConnection;
import lk.ijse.lms.model.LendTM;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class LendController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<LendTM> tblLend;
    @FXML
    private ImageView imgBack;
    @FXML
    private Label lblMemberName;
    @FXML
    private Label lblMemberContact;
    @FXML
    private JFXComboBox<String> cmbIsbn;
    @FXML
    private Label lblBookName;
    @FXML
    private Label lblBookAuthor;
    @FXML
    private JFXButton btnLend;
    @FXML
    private JFXComboBox<String> cmbNic;
    
    private int memberId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblMemberName.setText("");
        lblMemberContact.setText("");
        lblBookName.setText("");
        lblBookAuthor.setText("");
        
        tblLend.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblLend.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblLend.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblLend.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("publisher"));
        tblLend.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("lendDate"));
        tblLend.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("returned"));
        
        loadAllNic();
        
        tblLend.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                int column = tblLend.getSelectionModel().getSelectedCells().get(0).getColumn();
                if (column == 5){
                    try {
                        Connection connection = DbConnection.getInstance().getConnection();
                        try{
                            int bookId = getBookId(tblLend.getSelectionModel().getSelectedItem().getIsbn());
                            int memberId = getMemberId(cmbNic.getValue());
                            
                            connection.setAutoCommit(false);
                            Statement stm = connection.createStatement();
                            ResultSet rst = stm.executeQuery("SELECT returnId FROM tblReturn WHERE bookId="+bookId+" AND memberId="+memberId+" AND returnDate IS NULL");
                            if(rst.next()){
                                int returnId = rst.getInt(1);
                                PreparedStatement pstm = connection.prepareStatement("UPDATE tblReturn SET returnDate='"+LocalDate.now()+"' WHERE returnId = "+returnId+"");
                                boolean transaction = pstm.executeUpdate()>0;
                                if (transaction){
                                    PreparedStatement pstm2 = connection.prepareStatement("UPDATE tblBook SET bookStatus='Available' WHERE bookId = "+bookId+"");
                                    boolean transaction2 = pstm2.executeUpdate()>0;
                                    if (transaction2){
                                        connection.commit();
                                        loadAllIsbn();
                                        System.out.println("Working");
                                        loadNotReturnedBooks();
                                    }
                                }
                            }
                            connection.rollback();
                        }
                        finally{
                            connection.setAutoCommit(true);
                        }
                    }   catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }
            }
        });
    }    

    private void loadAllNic(){
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT memberNic FROM tblMember");
            ArrayList<String> nicList = new ArrayList<>();
            while(rst.next()){
                nicList.add(rst.getString(1));
            }
            cmbNic.setItems(FXCollections.observableArrayList(nicList));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void cmbNicOnAction(ActionEvent event) {
        try {
            cmbIsbn.getSelectionModel().clearSelection();
            lblBookName.setText("");
            lblBookAuthor.setText("");
            String selectedNic = cmbNic.getValue();
            memberId = getMemberId(selectedNic);
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT memberName, memberContactNum FROM tblMember WHERE memberId = "+memberId+"");
            if(rst.next()){
                lblMemberName.setText(rst.getString(1));
                lblMemberContact.setText(rst.getString(2));
            }
            loadAllIsbn();
            loadNotReturnedBooks();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
     private int getMemberId(String memberNic) {
        int idOfMember = 0;
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT memberId FROM tblMember WHERE memberNic='" + memberNic + "'");
            if (rst.next()) {
                idOfMember = rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idOfMember;

    }
    
    
    private void loadAllIsbn(){
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT bookIsbn FROM tblBook");
            ArrayList<String> isbnList = new ArrayList<>();
            while (rst.next()){
                isbnList.add(rst.getString(1));
            }
            Statement stm2 = connection.createStatement();
            ResultSet rst2 = stm2.executeQuery("SELECT bookIsbn FROM tblBook b, tblReturn r WHERE returnDate IS NULL AND memberId="+memberId+" AND r.bookId=b.bookId");
            while (rst2.next()){
                isbnList.remove(rst2.getString(1));
            }
            Statement stm3 = connection.createStatement();
            ResultSet rst3 = stm3.executeQuery("SELECT bookIsbn FROM tblBook WHERE bookStatus='Not Available'");
            while (rst3.next()){
                isbnList.remove(rst3.getString(1));
            }
            cmbIsbn.setItems(FXCollections.observableArrayList(isbnList));
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadNotReturnedBooks(){
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT bookIsbn, bookName, bookAuthor, bookPublisher, lendDate FROM tblBook b, tblLend l, tblReturn r WHERE r.returnDate IS NULL AND r.memberId="+memberId+" AND r.returnId = l.lendId AND r.bookId = b.bookId");
            ArrayList<LendTM> borrowedList = new ArrayList<>();
            
            while (rst.next()){
                LendTM lendTM = new LendTM(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), "Accept Return");
                borrowedList.add(lendTM);
            }
            
                tblLend.setItems(FXCollections.observableArrayList(borrowedList));
                    System.out.println("Wfdasfdorking");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
   
    
    
    @FXML
    private void cmbIsbnOnAction(ActionEvent event) {
        int bookId = getBookId(cmbIsbn.getValue());
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT bookName, bookAuthor FROM tblBook WHERE bookId = "+bookId+"");
            if (rst.next()){
                lblBookName.setText(rst.getString(1));
                lblBookAuthor.setText(rst.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

     private int getBookId(String bookIsbn){
        int bookId = 0;
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT bookId FROM tblBook WHERE bookIsbn = '"+bookIsbn+"'");
            if(rst.next()){
                bookId = rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bookId;
    }

    @FXML
    private void btnLendOnMouseClicked(MouseEvent event) {
        
            try {
                
                Connection connection = DbConnection.getInstance().getConnection();
                try
                {
                    connection.setAutoCommit(false);
                    PreparedStatement pstm = connection.prepareStatement("INSERT INTO tblLend(memberId, bookId, lendDate) VALUES(?,?,?)");
                    pstm.setObject(1, memberId);
                    pstm.setObject(2, getBookId(cmbIsbn.getValue()));
                    pstm.setObject(3, java.sql.Date.valueOf(LocalDate.now()));  
                    boolean transaction = pstm.executeUpdate()>0;
                    if (transaction){
                        Statement stm = connection.createStatement();
                        ResultSet rst = stm.executeQuery("SELECT lendId FROM tblLend ORDER by lendId DESC LIMIT 1");
                        if (rst.next()){
                            int lendId = rst.getInt(1);
                            PreparedStatement pstm2 = connection.prepareStatement("INSERT INTO tblReturn(returnId, bookId, memberId) VALUES(?,?,?)");
                            pstm2.setObject(1, lendId);
                            pstm2.setObject(2, getBookId(cmbIsbn.getValue()));
                            pstm2.setObject(3, memberId);
                            boolean transaction2 = pstm2.executeUpdate()>0;
                            if(transaction2){
                                PreparedStatement pstm3 = connection.prepareStatement("UPDATE tblBook SET bookStatus='Not Available' WHERE bookId=?");
                                pstm3.setObject(1, getBookId(cmbIsbn.getValue()));
                                boolean transaction3 = pstm3.executeUpdate()>0;
                                if (transaction3){
                                    connection.commit();
                                    lblBookName.setText("");
                                    lblBookAuthor.setText("");
                                    loadAllIsbn();
                                    loadNotReturnedBooks();
                                }
                                
                            }
                        }
                        
                    }
                    connection.rollback();
                }
                finally{
                    connection.setAutoCommit(true);
                }
                
 
            } catch (ClassNotFoundException ex) {   
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }   
         
        
    }

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
