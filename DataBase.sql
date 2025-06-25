-- Crie o banco de dados (opcional)
CREATE DATABASE IF NOT EXISTS BeutyGest;
USE BeutyGest;

-- Tabela de Pacientes (Clientes)
CREATE TABLE Paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    alergias TEXT
);

-- Tabela de Consultas (Histórico de Consultas dos Pacientes)
CREATE TABLE Consulta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    data DATETIME NOT NULL,
    status ENUM('agendada', 'realizada', 'cancelada') NOT NULL DEFAULT 'agendada',
    observacoes TEXT,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id)
);
