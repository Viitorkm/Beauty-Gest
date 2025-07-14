package Controller;

import dao.PacienteDAO;
import dao.ConsultaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Paciente;
import model.Consulta;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HistoricoConsultasController {

    @FXML private TextField campoBusca;
    @FXML private Button botaoBuscar;
    @FXML private TableView<Paciente> tabelaClientes;
    @FXML private TableColumn<Paciente, String> colunaNome;
    @FXML private TableColumn<Paciente, String> colunaCpf;
    @FXML private TableColumn<Paciente, String> colunaUltimaConsulta;
    @FXML private TableView<Consulta> tabelaConsultas;
    @FXML private TableColumn<Consulta, String> colunaData;
    @FXML private TableColumn<Consulta, String> colunaStatus;
    @FXML private TableColumn<Consulta, String> colunaObservacoes;
    @FXML private Button botaoNovaConsulta;
    @FXML private Button botaoEditarStatus;
    @FXML private Button botaoVoltar;

    private PacienteDAO pacienteDAO = new PacienteDAO();
    private ConsultaDAO consultaDAO = new ConsultaDAO();
    private ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private ObservableList<Consulta> listaConsultas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colunaCpf.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCpf()));
        colunaUltimaConsulta.setCellValueFactory(data -> {
            List<Consulta> consultas = consultaDAO.buscarPorPaciente(data.getValue().getId());
            return new javafx.beans.property.SimpleStringProperty(
                !consultas.isEmpty() ? 
                consultas.get(0).getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : 
                "-"
            );
        });

        colunaData.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ));
        colunaStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        colunaObservacoes.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getObservacoes()));

        tabelaClientes.setItems(listaPacientes);
        tabelaConsultas.setItems(listaConsultas);

        botaoBuscar.setOnAction(e -> buscarClientes());
        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> carregarConsultas(newSel));
        botaoNovaConsulta.setOnAction(e -> novaConsulta());
        botaoEditarStatus.setOnAction(e -> editarStatusConsulta());
        botaoVoltar.setOnAction(e -> voltarParaMenu());

        buscarClientes();
    }

    private void buscarClientes() {
        String termo = campoBusca.getText() != null ? campoBusca.getText().trim() : "";
        List<Paciente> encontrados = termo.isEmpty() ? 
            pacienteDAO.listarTodos() : 
            pacienteDAO.buscarPorNomeCpf(termo);
        listaPacientes.setAll(encontrados);
        listaConsultas.clear();
    }

    private void carregarConsultas(Paciente paciente) {
        if (paciente != null) {
            listaConsultas.setAll(consultaDAO.buscarPorPaciente(paciente.getId()));
        } else {
            listaConsultas.clear();
        }
    }

    private void novaConsulta() {
        try {
            Paciente pacienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
            if (pacienteSelecionado == null) {
                mostrarAlerta("Aviso", "Selecione um paciente primeiro", Alert.AlertType.WARNING);
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/novaConsulta.fxml"));
            Parent root = loader.load();
            NovaConsultaController controller = loader.getController();
            controller.selecionarPaciente(pacienteSelecionado);
            Stage stage = new Stage();
            stage.setTitle("Nova Consulta - " + pacienteSelecionado.getNome());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de nova consulta", Alert.AlertType.ERROR);
        }
    }

    private void editarStatusConsulta() {
        Consulta consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma consulta para editar o status", Alert.AlertType.WARNING);
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            consultaSelecionada.getStatus(),
            "Agendada", "Realizada", "Cancelada"
        );
        dialog.setTitle("Editar Status da Consulta");
        dialog.setHeaderText("Alterando status da consulta");
        dialog.setContentText("Selecione o novo status:");
        dialog.showAndWait().ifPresent(novoStatus -> {
            if (!novoStatus.equals(consultaSelecionada.getStatus())) {
                consultaSelecionada.setStatus(novoStatus);
                if (consultaDAO.atualizar(consultaSelecionada)) {
                    mostrarAlerta("Sucesso", "Status atualizado com sucesso!", Alert.AlertType.INFORMATION);
                    Paciente pacienteAtual = tabelaClientes.getSelectionModel().getSelectedItem();
                    carregarConsultas(pacienteAtual);
                } else {
                    mostrarAlerta("Erro", "Falha ao atualizar o status", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void voltarParaMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/menuPrincipal.fxml"));
            Stage stage = (Stage) botaoVoltar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("BeautyGest - Menu Principal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
