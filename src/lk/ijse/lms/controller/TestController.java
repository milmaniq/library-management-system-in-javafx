/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.controller;

import com.jfoenix.controls.JFXButton;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.lms.db.DbConnection;
import lk.ijse.lms.model.MemberTM;
import lk.ijse.lms.model.Test;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class TestController implements Initializable {

    @FXML
    private TableView<Test> tblMember;
    @FXML
    private JFXButton btnLoad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("salary"));

          ArrayList<Test> memberList = new ArrayList<>();
         for (int i = 0; i < 100; i++) {
                Test memberTM = new Test();
                memberTM.setId("265165" + i);
                memberTM.setName("kjbj" + i);
                memberTM.setAddress("jjk" + i);
                memberTM.setSalary(4444 + i);
                memberList.add(memberTM);

            }
            tblMember.setItems(FXCollections.observableArrayList(memberList));
    }


  @FXML
    private void btnPrepareReportOnAction(ActionEvent event) {
        try {

            InputStream report = getClass().getResourceAsStream("/lk/ijse/lms/reports/test.jasper");
            HashMap map = new HashMap();
            map.put("Total", 900.00);
            map.put("Tax", 100.00);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, new JRBeanCollectionDataSource(tblMember.getItems(),false));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException ex) {
            Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void btnGetMemberReportOnAction(ActionEvent event) {
        try {
            InputStream report = getClass().getResourceAsStream("/lk/ijse/lms/reports/lmsMemberReport.jasper");
            HashMap map = new HashMap();
            JasperPrint print = JasperFillManager.fillReport(report, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(print);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnGetBookReportOnAction(ActionEvent event) {
        try {
            InputStream report = getClass().getResourceAsStream("/lk/ijse/lms/reports/lmsBookReport.jasper");
            HashMap map = new HashMap();
            JasperPrint print = JasperFillManager.fillReport(report, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(print, false);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnGetMemberBookSubReportOnAction(ActionEvent event) {
        try {
            InputStream report = getClass().getResourceAsStream("/lk/ijse/lms/reports/lmsSubReport.jasper");
            HashMap map = new HashMap();
            JasperPrint print = JasperFillManager.fillReport(report, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(print, false);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(LendController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
