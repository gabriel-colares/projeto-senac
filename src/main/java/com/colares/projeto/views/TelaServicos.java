package com.colares.projeto.views;

import com.colares.projeto.dao.ServiceDAO;
import com.colares.projeto.models.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaServicos extends JFrame {
  private JTable tabelaServicos;
  private DefaultTableModel tableModel;
  private ServiceDAO serviceDAO;

  private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
  private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

  public TelaServicos() {
    setTitle("Serviços");
    setSize(800, 600);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    serviceDAO = new ServiceDAO();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Gerenciamento de Serviços");
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

    JLabel lblDescricao = new JLabel("Descrição:");
    lblDescricao.setFont(FONT_LABEL);
    JTextField txtDescricao = new JTextField(20);

    JLabel lblPreco = new JLabel("Preço:");
    lblPreco.setFont(FONT_LABEL);
    JTextField txtPreco = new JTextField(20);

    JLabel lblDuracao = new JLabel("Duração (min):");
    lblDuracao.setFont(FONT_LABEL);
    JTextField txtDuracao = new JTextField(20);

    JButton btnAdicionar = new JButton("Adicionar Serviço");
    btnAdicionar.setBackground(Color.BLACK);
    btnAdicionar.setForeground(Color.WHITE);
    btnAdicionar.setFont(FONT_BUTTON);
    btnAdicionar.setOpaque(true);
    btnAdicionar.setBorderPainted(false);
    btnAdicionar.setFocusPainted(false);
    btnAdicionar.addActionListener(e -> {
      String nome = txtNome.getText().trim();
      String descricao = txtDescricao.getText().trim();
      String precoStr = txtPreco.getText().trim();
      String duracaoStr = txtDuracao.getText().trim();

      if (nome.isEmpty() || descricao.isEmpty() || precoStr.isEmpty() || duracaoStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (!nome.matches("^[A-Za-zÀ-ÿ0-9\\s]+$")) {
        JOptionPane.showMessageDialog(this, "Nome inválido! Use apenas letras, números e espaços.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      double preco;
      int duracao;
      try {
        preco = Double.parseDouble(precoStr);
        if (preco < 0)
          throw new NumberFormatException();
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Preço inválido! Use um número positivo.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      try {
        duracao = Integer.parseInt(duracaoStr);
        if (duracao <= 0)
          throw new NumberFormatException();
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Duração inválida! Use um número inteiro positivo.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      List<Service> existingServices = serviceDAO.getAll();
      if (Service.isDuplicateName(existingServices, nome)) {
        JOptionPane.showMessageDialog(this, "Nome já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      Service service = new Service();
      service.setName(nome);
      service.setDescription(descricao);
      service.setPrice(preco);
      service.setDuration(duracao);

      try {
        serviceDAO.add(service);
        tableModel.addRow(new Object[] { service.getId(), nome, descricao, preco, duracao });
        txtNome.setText("");
        txtDescricao.setText("");
        txtPreco.setText("");
        txtDuracao.setText("");
        JOptionPane.showMessageDialog(this, "Serviço adicionado com sucesso!");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao adicionar serviço: " + ex.getMessage(), "Erro",
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
    formPanel.add(lblDescricao, gbc);
    gbc.gridx = 1;
    formPanel.add(txtDescricao, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(lblPreco, gbc);
    gbc.gridx = 1;
    formPanel.add(txtPreco, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(lblDuracao, gbc);
    gbc.gridx = 1;
    formPanel.add(txtDuracao, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    formPanel.add(btnAdicionar, gbc);

    gbc.gridy = 5;
    formPanel.add(btnVoltar, gbc);

    mainPanel.add(formPanel, BorderLayout.WEST);

    tableModel = new DefaultTableModel(new Object[] { "ID", "Nome", "Descrição", "Preço", "Duração (min)" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tabelaServicos = new JTable(tableModel);
    tabelaServicos.removeColumn(tabelaServicos.getColumnModel().getColumn(0)); // Oculta a coluna ID
    tabelaServicos.setFillsViewportHeight(true);
    tabelaServicos.setFont(FONT_LABEL);
    tabelaServicos.setRowHeight(25);
    tabelaServicos.setSelectionBackground(new Color(220, 220, 220));
    tabelaServicos.setSelectionForeground(Color.BLACK);

    JScrollPane scrollPane = new JScrollPane(tabelaServicos);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBackground(Color.WHITE);
    tablePanel.add(scrollPane, BorderLayout.CENTER);

    JButton btnRemover = new JButton("Remover Serviço");
    btnRemover.setBackground(Color.BLACK);
    btnRemover.setForeground(Color.WHITE);
    btnRemover.setFont(FONT_BUTTON);
    btnRemover.setOpaque(true);
    btnRemover.setBorderPainted(false);
    btnRemover.setFocusPainted(false);
    btnRemover.addActionListener(e -> {
      int selectedRow = tabelaServicos.getSelectedRow();
      if (selectedRow != -1) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try {
          serviceDAO.delete(id);
          tableModel.removeRow(selectedRow);
          JOptionPane.showMessageDialog(this, "Serviço removido com sucesso!");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Erro ao remover serviço: " + ex.getMessage(), "Erro",
              JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "Selecione um serviço para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(Color.WHITE);
    btnPanel.add(btnRemover);

    tablePanel.add(btnPanel, BorderLayout.SOUTH);

    mainPanel.add(tablePanel, BorderLayout.CENTER);

    add(mainPanel);

    carregarServicos();
  }

  private void carregarServicos() {
    List<Service> services = serviceDAO.getAll();
    for (Service service : services) {
      tableModel.addRow(new Object[] {
          service.getId(),
          service.getName(),
          service.getDescription(),
          service.getPrice(),
          service.getDuration()
      });
    }
  }
}
