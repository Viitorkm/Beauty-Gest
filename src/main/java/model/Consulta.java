/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author VitorKm
 */
import java.time.LocalDateTime;

public class Consulta {
    private int id;
    private int pacienteId;
    private LocalDateTime data;
    private String status;
    private String observacoes;

    public Consulta() {}

    public Consulta(int pacienteId, LocalDateTime data, String status, String observacoes) {
        this.pacienteId = pacienteId;
        this.data = data;
        this.status = status;
        this.observacoes = observacoes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
