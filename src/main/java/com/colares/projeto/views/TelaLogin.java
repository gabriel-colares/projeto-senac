package com.colares.projeto.views;

import com.colares.projeto.dao.UserDAO;
import com.colares.projeto.models.User;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
  private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
  private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

  private final UserDAO userDAO = new UserDAO();

  public TelaLogin() {
    setTitle("Login");
    setSize(400, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setLayout(new GridBagLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel loginPanel = new RoundedPanel(20);
    loginPanel.setBackground(Color.WHITE);
    loginPanel.setLayout(new GridBagLayout());
    loginPanel.setPreferredSize(new Dimension(350, 300));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 10, 8, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.weightx = 1;

    JLabel lblTitulo = new JLabel("Login");
    lblTitulo.setFont(TITLE_FONT);
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridy = 0;
    loginPanel.add(lblTitulo, gbc);

    JLabel lblSubtitulo = new JLabel("Entre com suas credenciais para acessar o sistema");
    lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
    lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridy = 1;
    loginPanel.add(lblSubtitulo, gbc);

    JLabel lblUsuario = new JLabel("Usuário");
    lblUsuario.setFont(LABEL_FONT);
    gbc.gridy = 2;
    loginPanel.add(lblUsuario, gbc);

    JTextField txtUsuario = new JTextField();
    txtUsuario.setPreferredSize(new Dimension(200, 30));
    gbc.gridy = 3;
    loginPanel.add(txtUsuario, gbc);

    JLabel lblSenha = new JLabel("Senha");
    lblSenha.setFont(LABEL_FONT);
    gbc.gridy = 4;
    loginPanel.add(lblSenha, gbc);

    JPasswordField txtSenha = new JPasswordField();
    txtSenha.setPreferredSize(new Dimension(200, 30));
    gbc.gridy = 5;
    loginPanel.add(txtSenha, gbc);

    JButton btnEntrar = new JButton("Entrar");
    btnEntrar.setPreferredSize(new Dimension(100, 30));
    btnEntrar.setBackground(Color.BLACK);
    btnEntrar.setForeground(Color.WHITE);
    btnEntrar.setOpaque(true);
    btnEntrar.setBorderPainted(false);
    btnEntrar.setFocusPainted(false);
    btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
    btnEntrar.addActionListener(e -> {
      String username = txtUsuario.getText().trim();
      String password = new String(txtSenha.getPassword()).trim();

      if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Campos obrigatórios",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      User user = userDAO.authenticate(username, password);

      if (user != null) {
        JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
        new TelaDashboard(user).setVisible(true); // PASSANDO O USER COMPLETO
        dispose();
      } else {
        JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
      }
    });
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.CENTER;
    loginPanel.add(btnEntrar, gbc);

    mainPanel.add(loginPanel);
    add(mainPanel);
  }

  static class RoundedPanel extends JPanel {
    private final int cornerRadius;

    public RoundedPanel(int radius) {
      super();
      this.cornerRadius = radius;
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Dimension arcs = new Dimension(cornerRadius, cornerRadius);
      int width = getWidth();
      int height = getHeight();
      Graphics2D graphics = (Graphics2D) g;
      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      graphics.setColor(getBackground());
      graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

      graphics.setColor(new Color(220, 220, 220));
      graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
  }
}
