package br.com.sistemaOS.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class TelaSobre extends JFrame {

	public TelaSobre() {
		super("Sobre!");
		formulario();

	}

	private void formulario() {
		this.setSize(320, 196);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());

		JPanel panelSobre = new JPanel();

		panelSobre.setLayout(new MigLayout());

		JPanel panelNort = new JPanel();

		JLabel lblSistema = new JLabel("Sistema para Controle de Ordem de Serviços");
		lblSistema.setFont(new Font("Tahoma Simples", Font.BOLD, 12));
		panelNort.add(lblSistema);
		this.add(panelNort, BorderLayout.NORTH);

		JLabel lblDesenv = new JLabel("Desenvolvido por Lucas Veras");
		lblDesenv.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/GNU_new.png")));

		JLabel lblTest = new JLabel();
		lblTest.setFont(new Font("Tahoma Simples", Font.BOLD, 12));

		JLabel lblTest2 = new JLabel();
		lblTest2.setFont(new Font("Tahoma Simples", Font.BOLD, 12));
		JLabel lblLicenc = new JLabel("Sob a licença GPL");

		panelSobre.add(lblTest, "wrap 20");
		panelSobre.add(lblDesenv, "gapLeft 30");
		panelSobre.add(lblTest2, "wrap -20");
		panelSobre.add(lblLicenc, "gapLeft 30");
		panelSobre.add(lblLogo, "gap 40 0 30 0");

		this.add(panelSobre, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		new TelaSobre().setVisible(true);

	}
}
