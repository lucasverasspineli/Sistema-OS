package br.com.sistemaOS.View;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;

import java.sql.*;
import java.text.ParseException;

import br.com.Sistema.dal.ModuloConexao.*;

public class TelaUsuario extends JInternalFrame {

	public JComboBox box;

	private VisualizarAction visualizarAction = new VisualizarAction();
	private AdicionarAction adicionarAction = new AdicionarAction();
	private AlterarAction alterarAction = new AlterarAction();
	public PesquisarAction pesquisarAction = new PesquisarAction();
	private DeletarAction deletarAction = new DeletarAction();

	JCheckBox boxVisual;

	Connection conexao = null;
	PreparedStatement prep = null;
	ResultSet resul = null;

	JFormattedTextField fone;

	MaskFormatter maskFone;

	JTextField txtId;
	JPasswordField passSenha = new JPasswordField(10);
	JTextField txtNome;
	JTextField txtLogin;

	public TelaUsuario() throws ParseException {
		super("Tela Usuário");
		tela();
		System.out.println("Tô Aqui!!");
		conexao = ModuloConexao.conector();

	}

	private void consultar() {
		String sql = "Select * from USUARIO where iduser =?";
		try {

			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtId.getText());

			resul = prep.executeQuery();
			if (resul.next()) {
				txtNome.setText(resul.getString(2));
				fone.setText(resul.getString(3));
				txtLogin.setText(resul.getString(4));
				passSenha.setText(resul.getString(5));
				box.setSelectedItem(resul.getString(6));

			} else {

				JOptionPane.showMessageDialog(null, "Usuário não Cadastrado!");
				txtNome.setText("");
				fone.setText("");
				txtLogin.setText("");
				passSenha.setText(null);

			}

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e);

		}

	}

	private void adicionar() {
		String sql = "Insert into Usuario(iduser,usuario,fone,login,senha,perfil)" + " values(?,?,?,?,?,?)";
		try {

			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtId.getText());
			prep.setString(2, txtNome.getText());
			prep.setString(3, fone.getText());
			prep.setString(4, txtLogin.getText());
			prep.setString(5, String.valueOf(passSenha.getPassword()));
			prep.setString(6, String.valueOf(box.getSelectedItem()));
			String senha = String.valueOf(passSenha.getPassword());

			if ((txtId.getText().isEmpty()) || (txtNome.getText().isEmpty()) || (txtLogin.getText().isEmpty())
					|| (senha.isEmpty())) {
				JOptionPane.showMessageDialog(null, "Por favor, Preencha todos os Campos Obrigatórios!");
				txtId.getText();
				txtNome.getText();
				txtLogin.getText();
				passSenha.getPassword();

			} else {

				int adicionado = prep.executeUpdate();
				System.out.println(adicionado);
				if (adicionado > 0) {

					JOptionPane.showMessageDialog(null, "Cadastrado com Sucesso!!!");
					txtId.setText(null);
					txtNome.setText(null);
					fone.setText(null);
					txtLogin.setText(null);
					passSenha.setText(null);

				}

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}

	}

	private void alterar() {

		String sql = "UPDATE USUARIO set usuario=?, fone=?, login=?, senha=?, perfil=? where iduser=?";

		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtNome.getText());
			prep.setString(2, fone.getText());
			prep.setString(3, txtLogin.getText());
			prep.setString(5, box.getSelectedItem().toString());
			prep.setString(6, txtId.getText());
			String senha = String.valueOf(passSenha.getPassword());

			String id = String.valueOf(txtId.getText());

			if ((txtId.getText().isEmpty()) || (txtNome.getText().isEmpty()) || (txtLogin.getText().isEmpty())
					|| (senha.isEmpty())) {

				JOptionPane.showMessageDialog(null, "Por favor, Preencha todos os Campos Obrigatórios!");
				if (txtId.getText().isEmpty()) {
					txtId.setText(resul.getString(1));
					consultar();// REFRESH
				} else {
					consultar();// REFRESH
				}

			} else {

				int adicionado = prep.executeUpdate();
				System.out.println(adicionado);
				if (adicionado > 0) {

					JOptionPane.showMessageDialog(null, "Cadastrado com Sucesso!!!");
					txtId.setText(null);
					txtNome.setText(null);
					fone.setText(null);
					txtLogin.setText(null);
					passSenha.setText(null);

				}

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}
	}

	private void deletar() {

		int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este Usuário?", "Atenção!",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			String sql = "Delete from Usuario where iduser=?";
			try {

				prep = conexao.prepareStatement(sql);
				prep.setString(1, txtId.getText());

				int apagado = prep.executeUpdate();
				if (apagado > 0) {
					System.out.println(apagado);
					JOptionPane.showMessageDialog(null, "O Usuário foi Deletado com Sucesso!");

					txtId.setText(null);
					txtNome.setText(null);
					fone.setText(null);
					txtLogin.setText(null);
					passSenha.setText(null);

				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);

			}

		}

	}

	private class DeletarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			deletar();

		}

	}

	private class AlterarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			alterar();
		}

	}

	private class AdicionarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			adicionar();

		}

	}

	private class PesquisarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			consultar();

		}

	}

	private void tela() throws ParseException {

		this.setMaximizable(false);
		this.setIconifiable(true);
		this.setResizable(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		this.setSize(624, 427);
		this.setVisible(true);

		this.setLayout(new BorderLayout());
		JPanel panelNort = new JPanel();

		panelNort.setPreferredSize(new Dimension(624, 20));

		JPanel panelCent = new JPanel();
		panelCent.setLayout(new MigLayout());

		this.add(panelNort, BorderLayout.NORTH);
		JLabel lblId = new JLabel("*Id");
		JLabel lblCamp = new JLabel("*Campos Obrigatórios");
		JLabel lblUser = new JLabel("*Nome");
		JLabel lblFone = new JLabel("Fone");
		JLabel lblLogin = new JLabel("*Login");
		JLabel lblSenha = new JLabel("*Senha");
		JLabel lblPerfil = new JLabel("*Perfil");
		boxVisual = new JCheckBox("Visualizar");
		boxVisual.addActionListener(visualizarAction);

		txtId = new JTextField(10);
		txtNome = new JTextField(30);
		maskFone = new MaskFormatter("#####-####");
		fone = new JFormattedTextField(maskFone);
		fone.setFocusLostBehavior(JFormattedTextField.COMMIT);

		fone.setColumns(7);
		maskFone.setValidCharacters("0123456789");
		txtLogin = new JTextField(20);

		String[] perfil = { "Admin", "User" };
		box = new JComboBox(perfil);

		JPanel panelBaixo = new JPanel(new FlowLayout());
		panelBaixo.add(passSenha);
		panelBaixo.add(boxVisual);

		panelCent.add(new JLabel(), "wrap 30");
		panelCent.add(lblId);
		panelCent.add(txtId);
		panelCent.add(new JLabel());
		panelCent.add(lblCamp, "wrap 20");
		panelCent.add(lblUser);
		panelCent.add(txtNome, "wrap 20");
		panelCent.add(lblFone);
		panelCent.add(fone, "gap 0 15 0 0");
		panelCent.add(lblLogin);
		panelCent.add(txtLogin, "wrap 20");
		panelCent.add(lblSenha);
		panelCent.add(panelBaixo, "gap 0 0 0 0");
		panelCent.add(lblPerfil);

//      
//      
		panelCent.add(box, "wrap 20");

		JButton btnSalvar = new JButton();
		btnSalvar.addActionListener(adicionarAction);
		JButton btnPesquisar = new JButton();
		btnPesquisar.addActionListener(pesquisarAction);
		JButton btnAlterar = new JButton();
		btnAlterar.addActionListener(alterarAction);
		JButton btnDeletar = new JButton();
		btnDeletar.addActionListener(deletarAction);
		btnSalvar.setPreferredSize(new Dimension(80, 80));
		btnPesquisar.setPreferredSize(new Dimension(80, 80));
		btnAlterar.setPreferredSize(new Dimension(80, 80));
		btnDeletar.setPreferredSize(new Dimension(80, 80));

		btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Adicionar.png")));
		btnSalvar.setBorder(new BordaRedonda(10));

		JPanel panelBtn = new JPanel(new MigLayout());
		panelBtn.setPreferredSize(new Dimension(639, 140));
		btnPesquisar
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Pesquisar.png")));
		btnPesquisar.setBorder(new BordaRedonda(10));
		btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Editar.png")));
		btnAlterar.setBorder(new BordaRedonda(10));
		btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Deletar.png")));
		btnDeletar.setBorder(new BordaRedonda(10));

		btnSalvar.setToolTipText("Adicionar");

		btnPesquisar.setToolTipText("Pesquisar");

		btnAlterar.setToolTipText("Modificar");

		btnDeletar.setToolTipText("Deletar");

		btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnPesquisar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAlterar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeletar.setCursor(new Cursor(Cursor.HAND_CURSOR));

		panelBtn.add(btnSalvar, "gap 20 20 0 0");
		panelBtn.add(btnPesquisar, "gap 20 20 0 0");
		panelBtn.add(btnAlterar, "gap 20 20 0 0");
		panelBtn.add(btnDeletar, "gap 20 20 0 0");

		this.add(panelBtn, BorderLayout.SOUTH);

		this.add(panelCent, BorderLayout.CENTER);

	}

	public class VisualizarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (boxVisual.isSelected()) {

				// É para descriptografar a senha

				passSenha.setEchoChar((char) 0);
			} else {
				/*
				 * Isso é para voltar a criptografia em formato padrão do JPasswordField, conta
				 * na ASC II, apertei as teclas do teclado (Ctrl+7)
				 */

				passSenha.setEchoChar('•');
			}

			// }

		}
	}

}
