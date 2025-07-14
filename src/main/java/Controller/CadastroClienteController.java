/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author VitorKm
 */
import dao.PacienteDAO;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Paciente;
import java.time.LocalDate;

public class CadastroClienteController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoCpf;
    @FXML
    private DatePicker campoDataNascimento;
    @FXML
    private TextField campoTelefone;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextArea campoAlergias;
    @FXML
    private Button botaoSalvar;
    @FXML
    private Button botaoCancelar;
    @FXML
    private Button botaoVoltar;

    private PacienteDAO pacienteDAO = new PacienteDAO();

    @FXML
    public void initialize() {
        botaoSalvar.setOnAction(e -> salvarPaciente());
        botaoCancelar.setOnAction(e -> limparCampos());
    }

    private void salvarPaciente() {
        try {
            Paciente paciente = new Paciente();
            paciente.setNome(campoNome.getText());
            paciente.setCpf(campoCpf.getText());
            paciente.setDataNascimento(campoDataNascimento.getValue());
            paciente.setTelefone(campoTelefone.getText());
            paciente.setEmail(campoEmail.getText());
            paciente.setAlergias(campoAlergias.getText());

            boolean sucesso = pacienteDAO.inserir(paciente);
            if (sucesso) {
                mostrarAlerta("Sucesso", "Paciente cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                limparCampos();
            } else {
                mostrarAlerta("Erro", "Erro ao cadastrar paciente.", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            mostrarAlerta("Erro", "Preencha todos os campos obrigatórios corretamente.", Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        campoNome.clear();
        campoCpf.clear();
        campoDataNascimento.setValue(null);
        campoTelefone.clear();
        campoEmail.clear();
        campoAlergias.clear();
    }

    @FXML
    private void voltarParaMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menuPrincipal.fxml"));
        Stage stage = (Stage) botaoVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("BeautyGest - Menu Principal");
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
