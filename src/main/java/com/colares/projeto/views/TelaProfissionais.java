package com.colares.projeto.views;

import com.colares.projeto.dao.ProfessionalDAO;
import com.colares.projeto.models.Professional;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaProfissionais extends JFrame {
  private JTable tabelaProfissionais;
  private DefaultTableModel tableModel;
  private ProfessionalDAO professionalDAO;

  private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
  private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

  public TelaProfissionais() {
    setTitle("Profissionais");
    setSize(800, 600);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    professionalDAO = new ProfessionalDAO();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Gerenciamento de Profissionais");
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

    JLabel lblEspecialidade = new JLabel("Especialidade:");
    lblEspecialidade.setFont(FONT_LABEL);
    JTextField txtEspecialidade = new JTextField(20);

    JLabel lblCPF = new JLabel("CPF:");
    lblCPF.setFont(FONT_LABEL);
    JTextField txtCPF = new JTextField(20);

    JButton btnAdicionar = new JButton("Adicionar Profissional");
    btnAdicionar.setBackground(Color.BLACK);
    btnAdicionar.setForeground(Color.WHITE);
    btnAdicionar.setFont(FONT_BUTTON);
    btnAdicionar.setOpaque(true);
    btnAdicionar.setBorderPainted(false);
    btnAdicionar.setFocusPainted(false);
    btnAdicionar.addActionListener(e -> {
      String nome = txtNome.getText().trim();
      String especialidade = txtEspecialidade.getText().trim();
      String cpf = txtCPF.getText().trim();

      if (nome.isEmpty() || especialidade.isEmpty() || cpf.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (nome.length() < 3) {
        JOptionPane.showMessageDialog(this, "O nome deve ter pelo menos 3 caracteres.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (!cpf.matches("\\d{11}")) {
        JOptionPane.showMessageDialog(this, "CPF inválido. Deve conter 11 dígitos numéricos.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (professionalDAO.findByName(nome) != null) {
        JOptionPane.showMessageDialog(this, "Já existe um profissional com este nome.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (professionalDAO.findByCpf(cpf) != null) {
        JOptionPane.showMessageDialog(this, "Já existe um profissional com este CPF.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      Professional professional = new Professional();
      professional.setName(nome);
      professional.setSpecialist(especialidade);
      professional.setCpf(cpf);

      try {
        professionalDAO.add(professional);
        tableModel.addRow(new Object[] { professional.getId(), nome, especialidade, cpf });
        txtNome.setText("");
        txtEspecialidade.setText("");
        txtCPF.setText("");
        JOptionPane.showMessageDialog(this, "Profissional adicionado com sucesso!");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao adicionar profissional: " + ex.getMessage(), "Erro",
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
    formPanel.add(lblEspecialidade, gbc);
    gbc.gridx = 1;
    formPanel.add(txtEspecialidade, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(lblCPF, gbc);
    gbc.gridx = 1;
    formPanel.add(txtCPF, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    formPanel.add(btnAdicionar, gbc);

    gbc.gridy = 4;
    formPanel.add(btnVoltar, gbc);

    mainPanel.add(formPanel, BorderLayout.WEST);

    tableModel = new DefaultTableModel(new Object[] { "ID", "Nome", "Especialidade", "CPF" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tabelaProfissionais = new JTable(tableModel);
    tabelaProfissionais.removeColumn(tabelaProfissionais.getColumnModel().getColumn(0)); // Oculta a coluna ID
    tabelaProfissionais.setFillsViewportHeight(true);
    tabelaProfissionais.setFont(FONT_LABEL);
    tabelaProfissionais.setRowHeight(25);
    tabelaProfissionais.setSelectionBackground(new Color(220, 220, 220));
    tabelaProfissionais.setSelectionForeground(Color.BLACK);

    JScrollPane scrollPane = new JScrollPane(tabelaProfissionais);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBackground(Color.WHITE);
    tablePanel.add(scrollPane, BorderLayout.CENTER);

    JButton btnRemover = new JButton("Remover Profissional");
    btnRemover.setBackground(Color.BLACK);
    btnRemover.setForeground(Color.WHITE);
    btnRemover.setFont(FONT_BUTTON);
    btnRemover.setOpaque(true);
    btnRemover.setBorderPainted(false);
    btnRemover.setFocusPainted(false);
    btnRemover.addActionListener(e -> {
      int selectedRow = tabelaProfissionais.getSelectedRow();
      if (selectedRow != -1) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try {
          professionalDAO.delete(id);
          tableModel.removeRow(selectedRow);
          JOptionPane.showMessageDialog(this, "Profissional removido com sucesso!");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Erro ao remover profissional: " + ex.getMessage(), "Erro",
              JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "Selecione um profissional para remover.", "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(Color.WHITE);
    btnPanel.add(btnRemover);

    tablePanel.add(btnPanel, BorderLayout.SOUTH);

    mainPanel.add(tablePanel, BorderLayout.CENTER);

    add(mainPanel);

    carregarProfissionais();
  }

  private void carregarProfissionais() {
    List<Professional> profissionais = professionalDAO.getAll();
    for (Professional professional : profissionais) {
      tableModel.addRow(new Object[] { professional.getId(), professional.getName(), professional.getSpecialist(),
          professional.getCpf() });
    }
  }
}
