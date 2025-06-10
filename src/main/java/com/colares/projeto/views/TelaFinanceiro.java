package com.colares.projeto.views;

import com.colares.projeto.dao.MoneyBoxDAO;
import com.colares.projeto.models.MoneyBox;
import com.colares.projeto.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class TelaFinanceiro extends JFrame {
  private JTable tabelaFinanceiro;
  private DefaultTableModel tableModel;
  private MoneyBoxDAO moneyBoxDAO;
  private final User loggedUser; // Adicionado

  public TelaFinanceiro(User loggedUser) {
    this.loggedUser = loggedUser;

    setTitle("Financeiro");
    setSize(900, 600);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    moneyBoxDAO = new MoneyBoxDAO();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Gerenciamento Financeiro");
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    mainPanel.add(lblTitulo, BorderLayout.NORTH);

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblTipo = new JLabel("Tipo:");
    lblTipo.setFont(new Font("Arial", Font.PLAIN, 14));
    String[] tipos = { "Entrada", "Saída" };
    JComboBox<String> comboTipo = new JComboBox<>(tipos);

    JLabel lblDescricao = new JLabel("Descrição:");
    lblDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
    JTextField txtDescricao = new JTextField(20);

    JLabel lblValor = new JLabel("Valor:");
    lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
    JTextField txtValor = new JTextField(20);

    JLabel lblDataHora = new JLabel("Data/Hora:");
    lblDataHora.setFont(new Font("Arial", Font.PLAIN, 14));
    SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
    JSpinner spinnerDataHora = new JSpinner(dateModel);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerDataHora, "dd/MM/yyyy HH:mm");
    spinnerDataHora.setEditor(editor);

    JButton btnAdicionar = new JButton("Adicionar Movimentação");
    btnAdicionar.setBackground(Color.BLACK);
    btnAdicionar.setForeground(Color.WHITE);
    btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
    btnAdicionar.setOpaque(true);
    btnAdicionar.setBorderPainted(false);
    btnAdicionar.setFocusPainted(false);
    btnAdicionar.addActionListener(e -> {
      String tipo = (String) comboTipo.getSelectedItem();
      String descricao = txtDescricao.getText().trim();
      String valorStr = txtValor.getText().trim();
      Date dataSelecionada = (Date) spinnerDataHora.getValue();
      LocalDateTime datetime = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

      if (!descricao.isEmpty() && !valorStr.isEmpty()) {
        try {
          double valor = Double.parseDouble(valorStr);
          MoneyBox moneyBox = new MoneyBox();
          moneyBox.setType(tipo);
          moneyBox.setDescription(descricao);
          moneyBox.setValue(valor);
          moneyBox.setDatetime(datetime);
          moneyBox.setUser(loggedUser); // Setando o usuário responsável

          moneyBoxDAO.add(moneyBox);

          tableModel.addRow(new Object[] { tipo, descricao, valor, datetime });
          txtDescricao.setText("");
          txtValor.setText("");
          JOptionPane.showMessageDialog(this, "Movimentação adicionada com sucesso!");
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
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
    formPanel.add(lblTipo, gbc);
    gbc.gridx = 1;
    formPanel.add(comboTipo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(lblDescricao, gbc);
    gbc.gridx = 1;
    formPanel.add(txtDescricao, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(lblValor, gbc);
    gbc.gridx = 1;
    formPanel.add(txtValor, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(lblDataHora, gbc);
    gbc.gridx = 1;
    formPanel.add(spinnerDataHora, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    formPanel.add(btnAdicionar, gbc);

    gbc.gridy = 5;
    formPanel.add(btnVoltar, gbc);

    mainPanel.add(formPanel, BorderLayout.WEST);

    tableModel = new DefaultTableModel(new Object[] { "Tipo", "Descrição", "Valor", "Data/Hora" }, 0);
    tabelaFinanceiro = new JTable(tableModel);
    tabelaFinanceiro.setFillsViewportHeight(true);
    tabelaFinanceiro.setFont(new Font("Arial", Font.PLAIN, 14));
    tabelaFinanceiro.setRowHeight(25);
    tabelaFinanceiro.setSelectionBackground(new Color(220, 220, 220));
    tabelaFinanceiro.setSelectionForeground(Color.BLACK);

    JScrollPane scrollPane = new JScrollPane(tabelaFinanceiro);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBackground(Color.WHITE);
    tablePanel.add(scrollPane, BorderLayout.CENTER);

    JButton btnRemover = new JButton("Remover Movimentação");
    btnRemover.setBackground(Color.BLACK);
    btnRemover.setForeground(Color.WHITE);
    btnRemover.setFont(new Font("Arial", Font.BOLD, 14));
    btnRemover.setOpaque(true);
    btnRemover.setBorderPainted(false);
    btnRemover.setFocusPainted(false);
    btnRemover.addActionListener(e -> {
      int selectedRow = tabelaFinanceiro.getSelectedRow();
      if (selectedRow != -1) {
        int idToRemove = moneyBoxDAO.getAll().get(selectedRow).getId();
        moneyBoxDAO.delete(idToRemove);
        tableModel.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Movimentação removida com sucesso!");
      } else {
        JOptionPane.showMessageDialog(this, "Selecione uma movimentação para remover.", "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(Color.WHITE);
    btnPanel.add(btnRemover);

    tablePanel.add(btnPanel, BorderLayout.SOUTH);

    mainPanel.add(tablePanel, BorderLayout.CENTER);

    add(mainPanel);

    carregarFinanceiro();
  }

  private void carregarFinanceiro() {
    List<MoneyBox> lista = moneyBoxDAO.getAll();
    for (MoneyBox mb : lista) {
      tableModel.addRow(new Object[] {
          mb.getType(),
          mb.getDescription(),
          mb.getValue(),
          mb.getDatetime()
      });
    }
  }
}