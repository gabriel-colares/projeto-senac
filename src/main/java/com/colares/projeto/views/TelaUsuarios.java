package com.colares.projeto.views;

import com.colares.projeto.dao.UserDAO;
import com.colares.projeto.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaUsuarios extends JFrame {
  private JTable tabelaUsuarios;
  private DefaultTableModel tableModel;
  private UserDAO userDAO;

  public TelaUsuarios() {
    setTitle("Usuários");
    setSize(800, 600);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    userDAO = new UserDAO();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Gerenciamento de Usuários");
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
    lblNome.setFont(new Font("Arial", Font.PLAIN, 14));
    JTextField txtNome = new JTextField(20);

    JLabel lblLogin = new JLabel("Login:");
    lblLogin.setFont(new Font("Arial", Font.PLAIN, 14));
    JTextField txtLogin = new JTextField(20);

    JLabel lblSenha = new JLabel("Senha:");
    lblSenha.setFont(new Font("Arial", Font.PLAIN, 14));
    JPasswordField txtSenha = new JPasswordField(20);

    JLabel lblPapel = new JLabel("Papel:");
    lblPapel.setFont(new Font("Arial", Font.PLAIN, 14));
    String[] papeis = { "ADMIN", "RECEPCIONISTA", "PROFISSIONAL" };
    JComboBox<String> comboPapel = new JComboBox<>(papeis);

    JButton btnAdicionar = new JButton("Adicionar Usuário");
    btnAdicionar.setBackground(Color.BLACK);
    btnAdicionar.setForeground(Color.WHITE);
    btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
    btnAdicionar.setOpaque(true);
    btnAdicionar.setBorderPainted(false);
    btnAdicionar.setFocusPainted(false);
    btnAdicionar.addActionListener(e -> {
      String nome = txtNome.getText().trim();
      String login = txtLogin.getText().trim();
      String senha = new String(txtSenha.getPassword()).trim();
      String papel = (String) comboPapel.getSelectedItem();

      if (!nome.isEmpty() && !login.isEmpty() && !senha.isEmpty()) {
        User user = new User();
        user.setName(nome);
        user.setUsername(login);
        user.setPassword(senha);
        user.setRole(User.Role.valueOf(papel));

        userDAO.add(user);

        tableModel.addRow(new Object[] { nome, login, papel });
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
      } else {
        JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
      }
    });

    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setBackground(Color.GRAY);
    btnVoltar.setForeground(Color.WHITE);
    btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
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
    formPanel.add(lblLogin, gbc);
    gbc.gridx = 1;
    formPanel.add(txtLogin, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(lblSenha, gbc);
    gbc.gridx = 1;
    formPanel.add(txtSenha, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(lblPapel, gbc);
    gbc.gridx = 1;
    formPanel.add(comboPapel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    formPanel.add(btnAdicionar, gbc);

    gbc.gridy = 5;
    formPanel.add(btnVoltar, gbc);

    mainPanel.add(formPanel, BorderLayout.WEST);

    tableModel = new DefaultTableModel(new Object[] { "Nome", "Login", "Papel" }, 0);
    tabelaUsuarios = new JTable(tableModel);
    tabelaUsuarios.setFillsViewportHeight(true);
    tabelaUsuarios.setFont(new Font("Arial", Font.PLAIN, 14));
    tabelaUsuarios.setRowHeight(25);
    tabelaUsuarios.setSelectionBackground(new Color(220, 220, 220));
    tabelaUsuarios.setSelectionForeground(Color.BLACK);

    JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBackground(Color.WHITE);
    tablePanel.add(scrollPane, BorderLayout.CENTER);

    JButton btnRemover = new JButton("Remover Usuário");
    btnRemover.setBackground(Color.BLACK);
    btnRemover.setForeground(Color.WHITE);
    btnRemover.setFont(new Font("Arial", Font.BOLD, 14));
    btnRemover.setOpaque(true);
    btnRemover.setBorderPainted(false);
    btnRemover.setFocusPainted(false);
    btnRemover.addActionListener(e -> {
      int selectedRow = tabelaUsuarios.getSelectedRow();
      if (selectedRow != -1) {
        String login = (String) tableModel.getValueAt(selectedRow, 1);
        List<User> users = userDAO.getAll();
        for (User user : users) {
          if (user.getUsername().equals(login)) {
            userDAO.delete(user.getId());
            break;
          }
        }
        tableModel.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!");
      } else {
        JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(Color.WHITE);
    btnPanel.add(btnRemover);

    tablePanel.add(btnPanel, BorderLayout.SOUTH);

    mainPanel.add(tablePanel, BorderLayout.CENTER);

    add(mainPanel);

    carregarUsuarios();
  }

  private void carregarUsuarios() {
    List<User> usuarios = userDAO.getAll();
    for (User user : usuarios) {
      tableModel.addRow(new Object[] { user.getName(), user.getUsername(), user.getRole().name() });
    }
  }
}
