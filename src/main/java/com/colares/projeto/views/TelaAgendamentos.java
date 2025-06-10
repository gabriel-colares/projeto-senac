package com.colares.projeto.views;

import com.colares.projeto.dao.ClientDAO;
import com.colares.projeto.dao.ProfessionalDAO;
import com.colares.projeto.dao.ScheduleDAO;
import com.colares.projeto.dao.ServiceDAO;
import com.colares.projeto.dao.UserDAO;
import com.colares.projeto.models.Client;
import com.colares.projeto.models.Professional;
import com.colares.projeto.models.Schedule;
import com.colares.projeto.models.Service;
import com.colares.projeto.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class TelaAgendamentos extends JFrame {
  private JTable tabelaAgendamentos;
  private DefaultTableModel tableModel;
  private ScheduleDAO scheduleDAO;
  private ClientDAO clientDAO;
  private ProfessionalDAO professionalDAO;
  private ServiceDAO serviceDAO;
  private UserDAO userDAO;
  private User loggedUser; // ADICIONADO

  private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
  private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

  public TelaAgendamentos(User loggedUser) { // RECEBE O USUÁRIO LOGADO
    this.loggedUser = loggedUser;

    setTitle("Agendamentos");
    setSize(900, 600);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    scheduleDAO = new ScheduleDAO();
    clientDAO = new ClientDAO();
    professionalDAO = new ProfessionalDAO();
    serviceDAO = new ServiceDAO();
    userDAO = new UserDAO();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JLabel lblTitulo = new JLabel("Gerenciamento de Agendamentos");
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    mainPanel.add(lblTitulo, BorderLayout.NORTH);

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblDataHora = new JLabel("Data/Hora:");
    lblDataHora.setFont(FONT_LABEL);
    SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
    JSpinner spinnerDataHora = new JSpinner(dateModel);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerDataHora, "dd/MM/yyyy HH:mm");
    spinnerDataHora.setEditor(editor);

    JLabel lblCliente = new JLabel("Cliente:");
    lblCliente.setFont(FONT_LABEL);
    JComboBox<String> comboCliente = new JComboBox<>();
    carregarClientes(comboCliente);

    JLabel lblProfissional = new JLabel("Profissional:");
    lblProfissional.setFont(FONT_LABEL);
    JComboBox<String> comboProfissional = new JComboBox<>();
    carregarProfissionais(comboProfissional);

    JLabel lblServico = new JLabel("Serviço:");
    lblServico.setFont(FONT_LABEL);
    JComboBox<String> comboServico = new JComboBox<>();
    carregarServicos(comboServico);

    JButton btnAdicionar = new JButton("Adicionar Agendamento");
    btnAdicionar.setBackground(Color.BLACK);
    btnAdicionar.setForeground(Color.WHITE);
    btnAdicionar.setFont(FONT_BUTTON);
    btnAdicionar.setOpaque(true);
    btnAdicionar.setBorderPainted(false);
    btnAdicionar.setFocusPainted(false);
    btnAdicionar.addActionListener(e -> {
      Date dataSelecionada = (Date) spinnerDataHora.getValue();
      LocalDateTime datetime = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

      String clienteNome = (String) comboCliente.getSelectedItem();
      String profissionalNome = (String) comboProfissional.getSelectedItem();
      String servicoNome = (String) comboServico.getSelectedItem();

      if (clienteNome == null || profissionalNome == null || servicoNome == null) {
        JOptionPane.showMessageDialog(this, "Selecione todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      int clientId = clientDAO.getClientIdByName(clienteNome);
      int professionalId = professionalDAO.getProfessionalIdByName(profissionalNome);
      int serviceId = serviceDAO.getServiceIdByName(servicoNome);

      if (clientId == -1 || professionalId == -1 || serviceId == -1) {
        JOptionPane.showMessageDialog(this, "Cliente, Profissional ou Serviço não encontrado!", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      int durationMinutes = serviceDAO.getServiceDurationById(serviceId);
      LocalDateTime start = datetime;
      LocalDateTime end = datetime.plusMinutes(durationMinutes + 15);

      boolean conflito = scheduleDAO.existsOverlappingSchedule(professionalId, start, end);
      if (conflito) {
        JOptionPane.showMessageDialog(this, "Este profissional já possui um agendamento no mesmo horário.", "Erro",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      Schedule schedule = new Schedule();
      schedule.setDatetime(datetime);
      schedule.setClient(clientDAO.getById(clientId));
      schedule.setProfessional(professionalDAO.getById(professionalId));
      schedule.setService(serviceDAO.getById(serviceId));
      schedule.setUser(loggedUser); // ADICIONADO

      try {
        scheduleDAO.add(schedule);
        tableModel.addRow(new Object[] {
            schedule.getId(),
            datetime.toString(),
            clienteNome,
            profissionalNome,
            servicoNome
        });
        JOptionPane.showMessageDialog(this, "Agendamento adicionado com sucesso!");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao adicionar agendamento: " + ex.getMessage(), "Erro",
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
    formPanel.add(lblDataHora, gbc);
    gbc.gridx = 1;
    formPanel.add(spinnerDataHora, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(lblCliente, gbc);
    gbc.gridx = 1;
    formPanel.add(comboCliente, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(lblProfissional, gbc);
    gbc.gridx = 1;
    formPanel.add(comboProfissional, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(lblServico, gbc);
    gbc.gridx = 1;
    formPanel.add(comboServico, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    formPanel.add(btnAdicionar, gbc);

    gbc.gridy = 5;
    formPanel.add(btnVoltar, gbc);

    mainPanel.add(formPanel, BorderLayout.WEST);

    tableModel = new DefaultTableModel(new Object[] { "ID", "Data/Hora", "Cliente", "Profissional", "Serviço" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tabelaAgendamentos = new JTable(tableModel);
    tabelaAgendamentos.removeColumn(tabelaAgendamentos.getColumnModel().getColumn(0));
    tabelaAgendamentos.setFillsViewportHeight(true);
    tabelaAgendamentos.setFont(FONT_LABEL);
    tabelaAgendamentos.setRowHeight(25);
    tabelaAgendamentos.setSelectionBackground(new Color(220, 220, 220));
    tabelaAgendamentos.setSelectionForeground(Color.BLACK);

    JScrollPane scrollPane = new JScrollPane(tabelaAgendamentos);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBackground(Color.WHITE);
    tablePanel.add(scrollPane, BorderLayout.CENTER);

    JButton btnRemover = new JButton("Remover Agendamento");
    btnRemover.setBackground(Color.BLACK);
    btnRemover.setForeground(Color.WHITE);
    btnRemover.setFont(FONT_BUTTON);
    btnRemover.setOpaque(true);
    btnRemover.setBorderPainted(false);
    btnRemover.setFocusPainted(false);
    btnRemover.addActionListener(e -> {
      int selectedRow = tabelaAgendamentos.getSelectedRow();
      if (selectedRow != -1) {
        int scheduleId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
          scheduleDAO.delete(scheduleId);
          tableModel.removeRow(selectedRow);
          JOptionPane.showMessageDialog(this, "Agendamento removido com sucesso!");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Erro ao remover agendamento: " + ex.getMessage(), "Erro",
              JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "Selecione um agendamento para remover.", "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(Color.WHITE);
    btnPanel.add(btnRemover);

    tablePanel.add(btnPanel, BorderLayout.SOUTH);

    mainPanel.add(tablePanel, BorderLayout.CENTER);

    add(mainPanel);

    carregarAgendamentos();
  }

  private void carregarClientes(JComboBox<String> comboCliente) {
    List<Client> clientes = clientDAO.getAll();
    for (Client client : clientes) {
      comboCliente.addItem(client.getName());
    }
  }

  private void carregarProfissionais(JComboBox<String> comboProfissional) {
    List<Professional> profissionais = professionalDAO.getAll();
    for (Professional profissional : profissionais) {
      comboProfissional.addItem(profissional.getName());
    }
  }

  private void carregarServicos(JComboBox<String> comboServico) {
    List<Service> servicos = serviceDAO.getAll();
    for (Service servico : servicos) {
      comboServico.addItem(servico.getName());
    }
  }

  private void carregarAgendamentos() {
    List<Schedule> agendamentos = scheduleDAO.getAll();
    for (Schedule schedule : agendamentos) {
      String clienteNome = schedule.getClient().getName();
      String profissionalNome = schedule.getProfessional().getName();
      String servicoNome = schedule.getService().getName();

      tableModel.addRow(new Object[] {
          schedule.getId(),
          schedule.getDatetime().toString(),
          clienteNome,
          profissionalNome,
          servicoNome
      });
    }
  }
}
