package br.com.sistemaOS.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//IMPORTANDO A CLASSE DO SQL TODA
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import br.com.Sistema.dal.ModuloConexao.ModuloConexao;
//AQUI IMPORTEI A BIBLIOTECA DA CLASSE QUE EU TINHA CRIADO PARA FAZER A COMUNICAÇÃO, COM O DIRETORIO DO PACKAGE E O NOME DA CLASSE!
import net.miginfocom.swing.MigLayout;

public class TelaSwing extends JFrame {

	// ADICIONAMOS
	private LogarAction logarAction = new LogarAction();
	private FecharAction fecharAction = new FecharAction();

	private JButton btnLogar;
	private JButton btnFechar;
	private JTextField fieldUser;
	private JPasswordField fieldSenha;
	private Connection conexao = null;
	private PreparedStatement prep = null;
	private ResultSet resul = null;

	private JLabel labelIcone;

	public TelaSwing() {
		super("Tela Swing no ECLIPSE EE");
		test();
		conexao = ModuloConexao.conector();
		if (conexao != null) {
			labelIcone.setIcon(new javax.swing.ImageIcon(
					getClass().getResource("/br/com/sistemaOS/icones/iconfinder_accept-database_49574.png")));
		} else {
			labelIcone.setIcon(new javax.swing.ImageIcon(
					getClass().getResource("/br/com/sistemaOS/icones/iconfinder_remove-from-database_49610.png")));

		}

	}

	public void logar() {
		String sql = "Select * from USUARIO where login =? and senha =? ";
		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, fieldUser.getText());
			prep.setString(2, String.valueOf(fieldSenha.getPassword()));
			resul = prep.executeQuery();

			if (resul.next()) {

				String perfil = resul.getString(6);
				System.out.println("Perfil: " + perfil);

				if (perfil.equals("Admin")) {

					SistemaPrincipal sisPrinc = new SistemaPrincipal();
					sisPrinc.setVisible(true);
					SistemaPrincipal.menuItemUsuario.setEnabled(true);
					SistemaPrincipal.menuRelatorio.setEnabled(true);
					SistemaPrincipal.labelUsuar.setText(resul.getString(2));
					SistemaPrincipal.labelUsuar.setForeground(Color.RED);

					this.dispose();
					conexao.close();
				} else {
					SistemaPrincipal sisPrinc = new SistemaPrincipal();
					sisPrinc.setVisible(true);
					SistemaPrincipal.labelUsuar.setText(resul.getString(2));
					this.dispose();
					conexao.close();
				}
			} else {
				JOptionPane.showMessageDialog(null, "O LOGIN OU A SENHA NÃO EXISTE NO NOSSO SISTEMA!");
			}
		} catch (Exception e) {
			System.out.println(e);

		}

	}

	public class LogarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			logar();

		}

	}

	private class FecharAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}

	}

	private void test() {
		this.setSize(530, 260);// (530,260)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		JPanel panelTitulo = new JPanel();
		panelTitulo.setLayout(new FlowLayout());
		panelTitulo.setPreferredSize(new Dimension(400, 50));
		panelTitulo.setBackground(Color.decode("#90ee90"));
		JLabel labelTitulo = new JLabel("Sistema O.S");
		labelTitulo.setFont(new Font("Verdana", Font.PLAIN, 16));
		panelTitulo.add(labelTitulo);
		JPanel panelForm = new JPanel();
		panelForm.setLayout(new MigLayout());
		panelForm.setBackground(Color.decode("#90ee90"));
		JLabel labelUser = new JLabel("Usuário");
		labelUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fieldUser = new JTextField(15);
		// espaço a Esquerda,Direita, Cima,Baixo
		panelForm.add(labelUser, "gap 140 0 10 0");
		panelForm.add(fieldUser, "wrap");
		JLabel labelSenha = new JLabel("Senha");
		labelSenha.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fieldSenha = new JPasswordField(15);
		panelForm.add(labelSenha, "gapleft 140");
		panelForm.add(fieldSenha);

		JPanel panelBtn = new JPanel();
		panelBtn.setPreferredSize(new Dimension(530, 85));
		panelBtn.setBackground(Color.decode("#90ee90"));
		btnLogar = new JButton("Logar");
		btnLogar.setPreferredSize(new Dimension(80, 40));
		btnLogar.setBackground(Color.decode("#DFECEB"));// #b1eceb
		btnLogar.setBorder(new BordaRedonda(10));
		btnLogar.addActionListener(logarAction);
		btnFechar = new JButton("");
		btnFechar.setBackground(Color.decode("#DFECEB"));

		btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/door_out.png")));

		btnFechar.setPreferredSize(new Dimension(80, 40));
		btnFechar.setBorder(new BordaRedonda(10));
		btnFechar.addActionListener(fecharAction);
		labelIcone = new JLabel();

		panelBtn.setLayout(new FlowLayout());

		panelBtn.add(labelIcone);
		panelBtn.add(btnLogar);
		panelBtn.add(btnFechar);

		this.setLayout(new BorderLayout());
		this.add(panelTitulo, BorderLayout.NORTH);

		this.add(panelForm, BorderLayout.CENTER);

		this.add(panelBtn, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		new TelaSwing().setVisible(true);
	}

}
