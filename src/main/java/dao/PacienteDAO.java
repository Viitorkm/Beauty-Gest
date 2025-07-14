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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import model.Paciente;
import util.ConexaoBanco;

public class PacienteDAO {

    public boolean inserir(Paciente paciente) {
        String sql = "INSERT INTO Paciente (nome, cpf, data_nascimento, telefone, email, alergias) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(4, paciente.getTelefone());
            stmt.setString(5, paciente.getEmail());
            stmt.setString(6, paciente.getAlergias());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir paciente: " + e.getMessage());
        }
        return false;
    }

    public boolean atualizar(Paciente paciente) {
        String sql = "UPDATE Paciente SET nome=?, cpf=?, data_nascimento=?, telefone=?, email=?, alergias=? WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(4, paciente.getTelefone());
            stmt.setString(5, paciente.getEmail());
            stmt.setString(6, paciente.getAlergias());
            stmt.setInt(7, paciente.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
        }
        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM Paciente WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover paciente: " + e.getMessage());
        }
        return false;
    }

    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM Paciente WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return construirPaciente(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Paciente> buscarPorNomeCpf(String termo) {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente WHERE nome LIKE ? OR cpf LIKE ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termo + "%");
            stmt.setString(2, "%" + termo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(construirPaciente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pacientes: " + e.getMessage());
        }
        return lista;
    }

    public List<Paciente> listarTodos() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(construirPaciente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
        }
        return lista;
    }

    private Paciente construirPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCpf(rs.getString("cpf"));
        p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        p.setAlergias(rs.getString("alergias"));
        return p;
    }
}
