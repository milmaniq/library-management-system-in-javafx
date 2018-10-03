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
import javafx.application.Platform;
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
import lk.ijse.lms.model.MemberTM;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class MemberController implements Initializable {

    @FXML
    private TableView<MemberTM> tblMember;
    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgAddMember;
    @FXML
    private JFXTextField txtSearchMembers;

    private MemberController memberControllerAddress;
    @FXML
    private AnchorPane root;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        memberControllerAddress = this;
        
        tblMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        tblMember.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("history"));
        tblMember.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("edit"));
        tblMember.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("remove"));

        getAllMembers();

        tblMember.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                
                if (tblMember.getSelectionModel().getSelectedCells().size() == 0 ){
                    return;
                }
                
                int column = tblMember.getSelectionModel().getSelectedCells().get(0).getColumn();
                
                if (column == 4) {
                    String nic = tblMember.getSelectionModel().getSelectedItem().getNic();
                    int memberId = getMemberId(nic);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/lms/view/HistoryMember.fxml"));
                        Parent parent = loader.load();
                        HistoryMemberController controller = loader.<HistoryMemberController>getController();
                        controller.initVariable(memberId);
                        Scene scene = new Scene(parent);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (column == 5) {
                    MemberTM memberTM = tblMember.getSelectionModel().getSelectedItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/lms/view/EditMember.fxml"));
                        Parent parent = loader.load();
                        EditMemberController controller = loader.<EditMemberController>getController();
                        controller.initVariable(memberTM, memberControllerAddress);
                        Scene scene = new Scene(parent);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (column == 6) {
                    try {
                        String nic = tblMember.getSelectionModel().getSelectedItem().getNic();
                        int memberId = getMemberId(nic);
                        Connection connection = DbConnection.getInstance().getConnection();
                        PreparedStatement pstm = connection.prepareStatement("DELETE FROM tblMember WHERE memberId='" + memberId + "'");
                        int value = pstm.executeUpdate();
                        if (value > 0) {
                            getAllMembers();
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK);
                            a.show();
                        } else {
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Delete", ButtonType.OK);
                            a.show();
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Platform.runLater(()->{
                     tblMember.getSelectionModel().clearSelection();
                });
               
            }
        });

    }

    public void getAllMembers() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM tblMember");
            ArrayList<MemberTM> memberList = new ArrayList<>();
            while (rst.next()) {
                MemberTM memberTM = new MemberTM(rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), "History", "Edit", "Remove");
                memberList.add(memberTM);
            }
            tblMember.setItems(FXCollections.observableArrayList(memberList));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getMemberId(String memberNic) {
        int memberId = 0;
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT memberId FROM tblMember WHERE memberNic='" + memberNic + "'");
            if (rst.next()) {
                memberId = rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return memberId;

    }

    @FXML
    private void imgAddBookOnClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/lms/view/AddMember.fxml"));
            Parent parent = loader.load();
            AddMemberController controller = loader.<AddMemberController>getController();
            controller.initVariable(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void txtSearchMembersOnKeyReleased(KeyEvent event) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM tblMember WHERE memberNic LIKE '" + txtSearchMembers.getText() + "%' OR memberName LIKE '" + txtSearchMembers.getText() + "%'");
            ArrayList<MemberTM> memberList = new ArrayList<>();
            while (rst.next()) {
                MemberTM memberTM = new MemberTM(rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), "History", "Edit", "Remove");
                memberList.add(memberTM);
            }
            tblMember.setItems(FXCollections.observableArrayList(memberList));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
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
