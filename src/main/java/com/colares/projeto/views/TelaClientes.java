package com.colares.projeto.views;

import com.colares.projeto.dao.ClientDAO;
import com.colares.projeto.models.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaClientes extends JFrame {
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    private ClientDAO clientDAO;

    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    public TelaClientes() {
        setTitle("Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        clientDAO = new ClientDAO();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gerenciamento de Clientes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(FONT_LABEL);
        JTextField txtNome = new JTextField(20);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(FONT_LABEL);
        JTextField txtTelefone = new JTextField(20);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(FONT_LABEL);
        JTextField txtEmail = new JTextField(20);

        JButton btnSalvar = new JButton("Adicionar Cliente");
        btnSalvar.setBackground(Color.BLACK);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(FONT_BUTTON);
        btnSalvar.setOpaque(true);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String email = txtEmail.getText().trim();

            if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!nome.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Nome inválido! Use apenas letras e espaços.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!telefone.matches("^\\d{8,15}$")) {
                JOptionPane.showMessageDialog(this, "Telefone inválido! Use apenas números (8 a 15 dígitos).", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(this, "Email inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Client> existingClients = clientDAO.getAll();
            if (Client.isDuplicateName(existingClients, nome)) {
                JOptionPane.showMessageDialog(this, "Nome já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Client.isDuplicateEmail(existingClients, email)) {
                JOptionPane.showMessageDialog(this, "Email já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client client = new Client();
            client.setName(nome);
            client.setTelephone(telefone);
            client.setEmail(email);
            try {
                clientDAO.add(client);
                tableModel.addRow(new Object[] { client.getId(), nome, telefone, email });
                txtNome.setText("");
                txtTelefone.setText("");
                txtEmail.setText("");
                JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar cliente: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(Color.GRAY);
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(FONT_BUTTON);
        btnVoltar.setOpaque(true);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1;
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblTelefone, gbc);
        gbc.gridx = 1;
        formPanel.add(txtTelefone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnSalvar, gbc);

        gbc.gridy = 4;
        formPanel.add(btnVoltar, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new Object[] { "ID", "Nome", "Telefone", "Email" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta
            }
        };
        tabelaClientes = new JTable(tableModel);
        tabelaClientes.removeColumn(tabelaClientes.getColumnModel().getColumn(0)); // oculta a coluna ID
        tabelaClientes.setFillsViewportHeight(true);
        tabelaClientes.setFont(FONT_LABEL);
        tabelaClientes.setRowHeight(25);
        tabelaClientes.setSelectionBackground(new Color(220, 220, 220));
        tabelaClientes.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JButton btnRemover = new JButton("Remover Cliente");
        btnRemover.setBackground(Color.BLACK);
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFont(FONT_BUTTON);
        btnRemover.setOpaque(true);
        btnRemover.setBorderPainted(false);
        btnRemover.setFocusPainted(false);
        btnRemover.addActionListener(e -> {
            int selectedRow = tabelaClientes.getSelectedRow();
            if (selectedRow != -1) {
                int clientId = (int) tableModel.getValueAt(selectedRow, 0);
                try {
                    clientDAO.delete(clientId);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao remover cliente: " + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para remover.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnRemover);

        tablePanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        carregarClientes();
    }

    private void carregarClientes() {
        List<Client> clientes = clientDAO.getAll();
        for (Client client : clientes) {
            tableModel.addRow(
                    new Object[] { client.getId(), client.getName(), client.getTelephone(), client.getEmail() });
        }
    }
}
