/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.lms.db.DbConnection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class ReportController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imgBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnGetMemberReportOnClicked(MouseEvent event) {
        try {
            InputStream report = getClass().getResourceAsStream("/lk/ijse/lms/reports/lmsMemberReport.jasper");
            HashMap map = new HashMap();
            JasperPrint print = JasperFillManager.fillReport(report, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(print, false);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnGetBookReportOnClicked(MouseEvent event) {
        try {
            InputStream report = getClass().getResourceAsStream("/lk/ijse/lms/reports/lmsBookReport.jasper");
            HashMap map = new HashMap();
            JasperPrint print = JasperFillManager.fillReport(report, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(print, false);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
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
