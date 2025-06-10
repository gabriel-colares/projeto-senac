CREATE DATABASE venust_business;

USE venust_business;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(45) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM(
        'ADMIN',
        'RECEPCIONISTA',
        'PROFISSIONAL'
    ) NOT NULL
);

CREATE TABLE client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE professional (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialist VARCHAR(100),
    cpf VARCHAR(11)
);

CREATE TABLE service (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    duration INT
);

CREATE TABLE schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    datetime DATETIME NOT NULL,
    client_id INT NOT NULL,
    service_id INT NOT NULL,
    professional_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client (id),
    FOREIGN KEY (service_id) REFERENCES service (id),
    FOREIGN KEY (professional_id) REFERENCES professional (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE moneybox (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('ENTRADA', 'SAIDA') NOT NULL,
    description TEXT,
    value DECIMAL(10, 2),
    datetime DATETIME NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

INSERT INTO user (
    name,
    username,
    password,
    role
)
VALUES
    ('admin', 'admin', '1234', 'ADMIN'),
    ('recepcionista', 'recep123', 'senha123', 'RECEPCIONISTA'),
    ('maria', 'pro123', 'senha123', 'PROFISSIONAL');

INSERT INTO client (name, telephone, email)
VALUES
    ('Gabriel Colares', '(92) 91234-5678', 'gabriel@example.com'),
    ('João da Silva', '(92) 99876-5432', 'joao@example.com');

INSERT INTO professional (name, specialist, cpf)
VALUES
    ('Maria Santos', 'Cabelereira', '12345678900'),
    ('Carlos Souza', 'Barbeiro', '98765432100');

INSERT INTO service (
    name,
    description,
    price,
    duration
)
VALUES
    ('Corte Masculino', 'Corte de cabelo tradicional', 30.00, 30),
    ('Pintura de Unhas', 'Serviço de manicure e pintura de unhas', 50.00, 45);

INSERT INTO schedule (
    datetime,
    client_id,
    service_id,
    professional_id,
    user_id
)
VALUES
    ('2025-06-10 10:00:00', 1, 1, 1, 1),
    ('2025-06-11 14:00:00', 2, 2, 2, 1);

INSERT INTO moneybox (
    type,
    description,
    value,
    datetime,
    user_id
)
VALUES
    ('ENTRADA', 'Pagamento corte masculino', 30.00, '2025-06-10 10:05:00', 1),
    ('ENTRADA', 'Pagamento manicure', 50.00, '2025-06-11 14:05:00', 1);
