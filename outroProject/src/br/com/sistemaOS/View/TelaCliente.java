package br.com.sistemaOS.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;//CRIANDO A EXCEÇÃO PARA ACEITAR A MASKARA 

import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;

import java.sql.*;
import br.com.Sistema.dal.ModuloConexao.*;

//A LINHA ABAIXO IMPORTA RECURSOS DA BIBLIOTECA rs2xml.jar
import net.proteanit.sql.DbUtils;

import java.awt.event.KeyEvent;

public class TelaCliente extends JInternalFrame {

	private Connection conexao = null;
	private PreparedStatement prep = null;
	private ResultSet resul = null;

	// DECLARAÇÃO DOS EVENTOS
	private AdicionarAction adicionarAction = new AdicionarAction();
	private AlterarAction alterarAction = new AlterarAction();
	private DeletarAction deletarAction = new DeletarAction();
	private PesquisarKey pesquisarKey = new PesquisarKey();
	private ClicarMouse clicarMouse = new ClicarMouse();

	JTable tbCli;

	JTextField txtNome;
	JTextField txtEnd;
	JTextField txtEmail;
	JTextField txtCliPesq;
	JTextField txtId;

	MaskFormatter mask;
	JFormattedTextField formatFone;

	JButton btnAdicionar;
	JButton btnAlterar;
	JButton btnDeletar;

	// É usado try por causa da mascara
	public TelaCliente() throws ParseException {
		super("Cliente");
		formulario();
		conexao = ModuloConexao.conector();

	}

	private void pesquisar() {
		String sql = "SELECT * from CLIENTE " + "where nomecli LIKE ?";
		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtCliPesq.getText() + "%");
			resul = prep.executeQuery();
			tbCli.setModel(DbUtils.resultSetToTableModel(resul));
			btnAdicionar.setEnabled(true);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	private void alterar() {
		String sql = "UPDATE CLIENTE set nomecli=?, endcli=?, fonecli=?, emailcli=? where idCli=?";
		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtNome.getText());
			prep.setString(2, txtEnd.getText());
			prep.setString(3, formatFone.getText());
			prep.setString(4, txtEmail.getText());
			prep.setString(5, txtId.getText());
			String fone = formatFone.getText();

			if ((txtNome.getText().isEmpty()) || (formatFone.getText().trim().length() < 9)) {
				JOptionPane.showMessageDialog(null, "Por Favor, Preencha os Campos Obrigatórios!");
				setarTabela();

			} else {
				int adicionado = prep.executeUpdate();
				System.out.println(adicionado);
				if (adicionado > 0) {

					JOptionPane.showMessageDialog(null, "Dados do Cliente Alterado Com Sucesso!");

					txtNome.setText(null);
					txtEnd.setText(null);
					formatFone.setText(null);
					txtEmail.setText(null);
					btnAdicionar.setEnabled(true);

					pesquisar();

				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void adicionar() {

		String sql = "Insert into CLIENTE(nomecli,endcli,fonecli,emailcli)" + "VALUES(?,?,?,?)";
		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtNome.getText());
			prep.setString(2, txtEnd.getText());
			prep.setString(3, formatFone.getText());
			prep.setString(4, txtEmail.getText());
			if ((txtNome.getText().isEmpty()) || (formatFone.getText().trim().length() < 9)) {

				JOptionPane.showMessageDialog(null, "Por Favor, Preencha os Campos Obrigatórios!");
				txtNome.getText();
				txtEnd.getText();
				formatFone.getText();
				txtEmail.getText();

			} else {

				int condicao = prep.executeUpdate();
				System.out.println(condicao);

				if (condicao > 0) {
					JOptionPane.showMessageDialog(null, "Cliente Adicionado com Sucesso!!!");
					txtNome.setText(null);
					txtEnd.setText(null);
					formatFone.setText(null);
					txtEmail.setText(null);

				}

			}

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e);
		}

	}

	private void deletar() {
		int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja Remover Este Cliente?",
				"Atenção!", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			String sql = "DELETE FROM CLIENTE where idcli =?";

			try {
				prep = conexao.prepareStatement(sql);
				prep.setString(1, txtId.getText());
				int apagado = prep.executeUpdate();

				if (apagado > 0) {
					JOptionPane.showMessageDialog(null, "Cliente Removido com Sucesso!");

					txtNome.setText(null);
					txtEnd.setText(null);
					formatFone.setText(null);
					txtEmail.setText(null);
					pesquisar();
					btnAdicionar.setEnabled(true);
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);

			}
		}

	}

	private class PesquisarKey extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			pesquisar();
		}

	}

	public void setarTabela() {
		int setar = tbCli.getSelectedRow();
		txtId.setText(tbCli.getModel().getValueAt(setar, 0).toString());
		txtNome.setText(tbCli.getModel().getValueAt(setar, 1).toString());
		txtEnd.setText(tbCli.getModel().getValueAt(setar, 2).toString());
		formatFone.setText(tbCli.getModel().getValueAt(setar, 3).toString());
		txtEmail.setText(tbCli.getModel().getValueAt(setar, 4).toString());
		btnAdicionar.setEnabled(false);

	}

	public class ClicarMouse extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			setarTabela();

		}

	}

	private class AdicionarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			adicionar();

		}

	}

	private class AlterarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			alterar();
		}

	}

	private class DeletarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			deletar();

		}

	}

	private void formulario() throws ParseException {

		this.setMaximizable(false);
		this.setIconifiable(true);
		this.setResizable(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		this.setSize(624, 427);
		this.setVisible(true);
		this.setLayout(new BorderLayout());

		JLabel lblCamp = new JLabel("*Campos Obrigatórios");
		lblCamp.setForeground(Color.RED);
		JLabel lblNome = new JLabel("<html>" + "<font color=\"red\">*</font>" + "Nome" + "</html>");

		JLabel lblEnd = new JLabel("Endereço");
		JLabel lblTele = new JLabel("<html>" + "<font color=\"red\">*</font>" + "Telefone" + "</html>");
		JLabel lblEmail = new JLabel("e-mail");
		JLabel lblId = new JLabel("Id");
		// CAMPOS DE TEXTOS!
		txtId = new JTextField(3);
		txtId.setEnabled(false);
		txtNome = new JTextField(40);
		txtEnd = new JTextField(40);
		txtEmail = new JTextField(40);
		txtCliPesq = new JTextField(25);
		txtCliPesq.addKeyListener(pesquisarKey);
		JLabel lblfoto = new JLabel(new ImageIcon(getClass().getResource("/br/com/SistemaOS/icones/search.png")));
		btnAdicionar = new JButton();
		btnAdicionar.addActionListener(adicionarAction);
		btnAlterar = new JButton();
		btnAlterar.addActionListener(alterarAction);
		btnDeletar = new JButton();
		btnDeletar.addActionListener(deletarAction);

		mask = new MaskFormatter("#####-####");
		formatFone = new JFormattedTextField(mask);
		formatFone.setFocusLostBehavior(JFormattedTextField.COMMIT);
		formatFone.setColumns(7);
		mask.setValidCharacters("0123456789");

		JPanel panelBtn = new JPanel(new MigLayout());
		panelBtn.setPreferredSize(new Dimension(639, 110));
		btnAdicionar
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Adicionar.png")));
		btnAdicionar.setBorder(new BordaRedonda(10));

		btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Editar.png")));
		btnAlterar.setBorder(new BordaRedonda(10));

		btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Deletar.png")));
		btnDeletar.setBorder(new BordaRedonda(10));

		// CRIANDO UMA HINT/DICA! QUANDO O MOUSE FICA SOBRE O BOTÃO OU QUALQUER
		// COMPONENTE

		btnAdicionar.setToolTipText("Adicionar");

		btnAlterar.setToolTipText("Alterar");

		btnDeletar.setToolTipText("Deletar");

		// MUDANDO O FORMATO DO MOUSE QUANDO FICAR SOBRE ESTES COMPONENTES!

		btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAlterar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeletar.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPanel panelNort = new JPanel(new MigLayout());

		panelNort.setPreferredSize(new Dimension(624, 40));
		JPanel panelCent = new JPanel(new MigLayout("gap 20 0 0 0"));

		String[] colunasTabela = { "Título 1", "Título 2", "Título 3", "Título 4" };

		Object[][] dados = { { "", "", "", "" }, { "", "", "", "" }, { "", "", "", "" }, { " ", "", "", "" }, };

		tbCli = new JTable(dados, colunasTabela);
		tbCli.setPreferredScrollableViewportSize(new Dimension(500, 50));
		tbCli.setFillsViewportHeight(true);
		JScrollPane paneScrool = new JScrollPane(tbCli);
		tbCli.addMouseListener(clicarMouse);
		panelCent.add(new JLabel(), "wrap");
		panelCent.add(paneScrool, "spanx 2");
		panelCent.add(new JLabel(), "wrap 15");// wrap 15
		panelCent.add(lblId, "gapLeft 30");
		panelCent.add(txtId, "wrap 10"); // wrap 10

		panelCent.add(lblNome, "gapLeft 30");
		panelCent.add(txtNome, "wrap 10");
		panelCent.add(lblEnd, "gapLeft 30");
		panelCent.add(txtEnd, "wrap 10");
		panelCent.add(lblTele, "gapLeft 30");
		panelCent.add(formatFone, "wrap 10");
		panelCent.add(lblEmail, "gapLeft 30");
		panelCent.add(txtEmail, "wrap 10");

		panelBtn.add(btnAdicionar, "gap 100 20 0 0");
		panelBtn.add(btnAlterar, "gap 20 20 0 0");
		panelBtn.add(btnDeletar, "gap 20 20 0 0");

		panelNort.add(txtCliPesq);
		panelNort.add(lblfoto, "gap 10 140 0 0");
		panelNort.add(lblCamp);

		this.add(panelNort, BorderLayout.NORTH);
		this.add(panelCent, BorderLayout.CENTER);
		this.add(panelBtn, BorderLayout.SOUTH);

	}

}
