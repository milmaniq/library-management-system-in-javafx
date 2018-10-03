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
import javafx.stage.Window;
import lk.ijse.lms.db.DbConnection;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class AddMemberController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imgClose;
    @FXML
    private JFXTextField txtNic;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtContactNum;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;

    private MemberController memberControllerAddress;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    void initVariable(MemberController memberController) {
        memberControllerAddress = memberController;
    }

    @FXML
    private void imgCloseMouseClicked(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO tblMember(memberNic, memberName, memberAddress, memberContactNum) VALUES(?,?,?,?)");
            pstm.setObject(1, txtNic.getText());
            pstm.setObject(2, txtName.getText());
            pstm.setObject(3, txtAddress.getText());
            pstm.setObject(4, txtContactNum.getText());
            int value = pstm.executeUpdate();
            if (value>0){
                memberControllerAddress.getAllMembers();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Inserted Successfully", ButtonType.OK);
                a.show();
                btnClearOnAction(null);
            }
            else
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Insert", ButtonType.OK);
                a.show();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnClearOnAction(ActionEvent event) {
        txtNic.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContactNum.setText("");
    }

    
    
}
