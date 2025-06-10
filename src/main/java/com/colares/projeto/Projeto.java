package com.colares.projeto;

import com.colares.projeto.views.TelaLogin;

import javax.swing.*;

/**
 *
 * @author Gabriel Colares
 * @version 1.1
 */
public class Projeto {
    public static void main(String[] args) {
        // Configurar look and feel (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Erro ao definir Look and Feel: " + e.getMessage());
        }

        // Abrir tela de login
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
