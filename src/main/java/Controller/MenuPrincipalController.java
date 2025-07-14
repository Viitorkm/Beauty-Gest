/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author VitorKm
 */
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuPrincipalController {
    
    @FXML
    private Button botaoCadastro;
    
    @FXML
    private Button botaoHistorico;
    
    @FXML
    private void abrirCadastroCliente() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/cadastroCliente.fxml"));
        Stage stage = (Stage) botaoCadastro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("BeautyGest - Cadastro de Cliente");
    }
    
    @FXML
    private void abrirHistoricoConsultas() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/historicoConsultas.fxml"));
        Stage stage = (Stage) botaoHistorico.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("BeautyGest - Histórico de Consultas");
    }
}
