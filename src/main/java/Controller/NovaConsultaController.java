package Controller;

import dao.ConsultaDAO;
import dao.PacienteDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Consulta;
import model.Paciente;

public class NovaConsultaController {

    @FXML
    private ComboBox<Paciente> comboPacientes;
    @FXML
    private DatePicker campoData;
    @FXML
    private TextField campoHora;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private TextArea campoObservacoes;
    @FXML
    private Button botaoSalvar;
    @FXML
    private Button botaoCancelar;

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();

    // Formato brasileiro para o DatePicker
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Método público para selecionar paciente ao abrir a tela
    public void selecionarPaciente(Paciente paciente) {
        carregarPacientes();
        comboPacientes.getSelectionModel().select(paciente);
    }

    @FXML
    public void initialize() {
        // Carrega pacientes no ComboBox
        carregarPacientes();

        // Configura o ComboBox de status
        comboStatus.getItems().addAll("Agendada", "Realizada", "Cancelada");
        comboStatus.getSelectionModel().selectFirst();

        // Configura o DatePicker para aceitar datas no formato brasileiro
        campoData.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return (string != null && !string.isEmpty())
                            ? LocalDate.parse(string, dateFormatter)
                            : null;
                } catch (Exception e) {
                    return null;
                }
            }
        });

        // Exibe o calendário ao clicar no campo
        campoData.setPromptText("dd/MM/yyyy");

        // Configura os botões
        botaoSalvar.setOnAction(e -> salvarConsulta());
        botaoCancelar.setOnAction(e -> fecharJanela());
    }

    private void carregarPacientes() {
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        comboPacientes.setItems(FXCollections.observableArrayList(pacientes));
        // Exibe nome do paciente no ComboBox
        comboPacientes.setCellFactory(lv -> new ListCell<Paciente>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        comboPacientes.setButtonCell(new ListCell<Paciente>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
    }

    private void salvarConsulta() {
        if (!validarDados()) return;

        Consulta consulta = criarConsulta();

        if (consultaDAO.inserir(consulta)) {
            mostrarAlerta("Sucesso", "Consulta agendada com sucesso!", Alert.AlertType.INFORMATION);
            fecharJanela();
        } else {
            mostrarAlerta("Erro", "Falha ao salvar consulta", Alert.AlertType.ERROR);
        }
    }

    private boolean validarDados() {
        if (comboPacientes.getValue() == null) {
            mostrarAlerta("Erro", "Selecione um paciente", Alert.AlertType.ERROR);
            return false;
        }
        if (campoData.getValue() == null) {
            mostrarAlerta("Erro", "Selecione uma data", Alert.AlertType.ERROR);
            return false;
        }
        if (campoHora.getText().isEmpty() || parseHora(campoHora.getText()) == null) {
            mostrarAlerta("Erro", "Formato de hora inválido (use HH:mm)", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private Consulta criarConsulta() {
        Paciente paciente = comboPacientes.getValue();
        LocalDate data = campoData.getValue();
        LocalTime hora = parseHora(campoHora.getText());
        String status = comboStatus.getValue();
        String observacoes = campoObservacoes.getText();

        Consulta consulta = new Consulta();
        consulta.setPacienteId(paciente.getId());
        consulta.setData(LocalDateTime.of(data, hora));
        consulta.setStatus(status != null ? status : "Agendada");
        consulta.setObservacoes(observacoes);

        return consulta;
    }

    private LocalTime parseHora(String horaStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(horaStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) botaoCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
