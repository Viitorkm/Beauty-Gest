/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author VitorKm
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Consulta;
import util.ConexaoBanco;
import java.time.LocalDateTime;

public class ConsultaDAO {

    public boolean inserir(Consulta consulta) {
        String sql = "INSERT INTO Consulta (paciente_id, data, status, observacoes) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, consulta.getPacienteId());
            stmt.setTimestamp(2, Timestamp.valueOf(consulta.getData()));
            stmt.setString(3, consulta.getStatus());
            stmt.setString(4, consulta.getObservacoes());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    consulta.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir consulta: " + e.getMessage());
        }
        return false;
    }

    public boolean atualizar(Consulta consulta) {
        String sql = "UPDATE Consulta SET paciente_id=?, data=?, status=?, observacoes=? WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getPacienteId());
            stmt.setTimestamp(2, Timestamp.valueOf(consulta.getData()));
            stmt.setString(3, consulta.getStatus());
            stmt.setString(4, consulta.getObservacoes());
            stmt.setInt(5, consulta.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar consulta: " + e.getMessage());
        }
        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM Consulta WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover consulta: " + e.getMessage());
        }
        return false;
    }

    public Consulta buscarPorId(int id) {
        String sql = "SELECT * FROM Consulta WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return construirConsulta(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consulta por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Consulta> buscarPorPaciente(int pacienteId) {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Consulta WHERE paciente_id=? ORDER BY data DESC";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(construirConsulta(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consultas do paciente: " + e.getMessage());
        }
        return lista;
    }

    public List<Consulta> listarTodas() {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Consulta ORDER BY data DESC";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(construirConsulta(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
        }
        return lista;
    }

    private Consulta construirConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getInt("id"));
        c.setPacienteId(rs.getInt("paciente_id"));
        c.setData(rs.getTimestamp("data").toLocalDateTime());
        c.setStatus(rs.getString("status"));
        c.setObservacoes(rs.getString("observacoes"));
        return c;
    }
    
}
