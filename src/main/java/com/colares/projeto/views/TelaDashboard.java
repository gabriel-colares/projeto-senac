package com.colares.projeto.views;

import com.colares.projeto.dao.ClientDAO;
import com.colares.projeto.dao.ProfessionalDAO;
import com.colares.projeto.dao.ScheduleDAO;
import com.colares.projeto.models.User;

import javax.swing.*;
import java.awt.*;

public class TelaDashboard extends JFrame {
  private ClientDAO clientDAO;
  private ProfessionalDAO professionalDAO;
  private ScheduleDAO scheduleDAO;
  private User loggedUser; // ADICIONADO

  public TelaDashboard(User loggedUser) {
    this.loggedUser = loggedUser;

    setTitle("Dashboard");
    setSize(900, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    clientDAO = new ClientDAO();
    professionalDAO = new ProfessionalDAO();
    scheduleDAO = new ScheduleDAO();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JPanel sidebar = new JPanel();
    sidebar.setBackground(Color.BLACK);
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
    sidebar.setPreferredSize(new Dimension(250, getHeight()));

    JLabel lblLogo = new JLabel("Venust Business");
    lblLogo.setFont(new Font("Arial", Font.BOLD, 16));
    lblLogo.setForeground(Color.WHITE);
    lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
    sidebar.add(lblLogo);

    JButton btnClientes = createSidebarButton("Clientes");
    JButton btnProfissionais = createSidebarButton("Profissionais");
    JButton btnServicos = createSidebarButton("Serviços");
    JButton btnAgendamentos = createSidebarButton("Agendamentos");
    JButton btnFinanceiro = createSidebarButton("Financeiro");
    JButton btnUsuarios = createSidebarButton("Usuários");

    btnClientes.addActionListener(e -> new TelaClientes().setVisible(true));
    btnProfissionais.addActionListener(e -> new TelaProfissionais().setVisible(true));
    btnServicos.addActionListener(e -> new TelaServicos().setVisible(true));
    btnAgendamentos.addActionListener(e -> new TelaAgendamentos(loggedUser).setVisible(true));
    btnFinanceiro.addActionListener(e -> new TelaFinanceiro(loggedUser).setVisible(true));
    btnUsuarios.addActionListener(e -> new TelaUsuarios().setVisible(true));

    User.Role role = loggedUser.getRole();

    if (role == User.Role.ADMIN) {
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnClientes);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnProfissionais);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnServicos);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnAgendamentos);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnFinanceiro);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnUsuarios);
    } else if (role == User.Role.RECEPCIONISTA) {
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnClientes);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnProfissionais);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnServicos);
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnAgendamentos);
    } else if (role == User.Role.PROFISSIONAL) {
      sidebar.add(Box.createVerticalStrut(24));
      sidebar.add(btnAgendamentos);
    }

    JLabel lblSair = new JLabel("Sair");
    lblSair.setFont(new Font("Arial", Font.BOLD, 14));
    lblSair.setForeground(Color.RED);
    lblSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblSair.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    lblSair.setAlignmentX(Component.LEFT_ALIGNMENT);
    lblSair.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        System.exit(0);
      }
    });

    sidebar.add(Box.createVerticalGlue());
    sidebar.add(lblSair);

    mainPanel.add(sidebar, BorderLayout.WEST);

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    JLabel lblBoasVindas = new JLabel("Bem-vindo(a), " + loggedUser.getName() + "!");
    lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 24));
    lblBoasVindas.setForeground(Color.BLACK);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    contentPanel.add(lblBoasVindas, gbc);

    if (role == User.Role.ADMIN || role == User.Role.RECEPCIONISTA) {
      JLabel lblEstatisticas = new JLabel("Estatísticas do Sistema:");
      lblEstatisticas.setFont(new Font("Arial", Font.BOLD, 18));
      lblEstatisticas.setForeground(new Color(50, 50, 50));
      gbc.gridy++;
      contentPanel.add(lblEstatisticas, gbc);

      int totalClientes = clientDAO.getAll().size();
      int totalProfissionais = professionalDAO.getAll().size();
      int totalAgendamentos = scheduleDAO.getAll().size();

      JLabel lblClientes = new JLabel("Total de Clientes: " + totalClientes);
      lblClientes.setFont(new Font("Arial", Font.PLAIN, 16));
      gbc.gridy++;
      contentPanel.add(lblClientes, gbc);

      JLabel lblProfissionais = new JLabel("Total de Profissionais: " + totalProfissionais);
      lblProfissionais.setFont(new Font("Arial", Font.PLAIN, 16));
      gbc.gridy++;
      contentPanel.add(lblProfissionais, gbc);

      JLabel lblAgendamentos = new JLabel("Total de Agendamentos: " + totalAgendamentos);
      lblAgendamentos.setFont(new Font("Arial", Font.PLAIN, 16));
      gbc.gridy++;
      contentPanel.add(lblAgendamentos, gbc);
    } else if (role == User.Role.PROFISSIONAL) {
      JLabel lblMensagem = new JLabel("Confira seus agendamentos clicando no menu à esquerda.");
      lblMensagem.setFont(new Font("Arial", Font.PLAIN, 16));
      lblMensagem.setForeground(new Color(80, 80, 80));
      gbc.gridy++;
      contentPanel.add(lblMensagem, gbc);
    }

    mainPanel.add(contentPanel, BorderLayout.CENTER);

    add(mainPanel);
  }

  private JButton createSidebarButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.PLAIN, 14));
    button.setForeground(new Color(173, 216, 230));
    button.setBackground(Color.BLACK);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setAlignmentX(Component.LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(180, 40));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(new Color(30, 30, 30));
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(Color.BLACK);
      }
    });
    return button;
  }
}
