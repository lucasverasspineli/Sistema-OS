package br.com.sistemaOS.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import net.miginfocom.swing.MigLayout;

import java.sql.*; //BIBLIOTECA DO BANCO DE DADOS SQL
import java.util.EventListener;

import br.com.Sistema.dal.ModuloConexao.*;// PACKAGE DA CLASSE DA CONEXÃO COM BANCO
import net.proteanit.sql.DbUtils; /*A LINHA ABAIXO IMPORTA RECURSOS DA BIBLIOTECA rs2xml.jar 
									BIBLIOTECA REFERENTE AO PEGAR AS INFORMAÇÕES DA TABELA*/

public class TelaOs extends JInternalFrame {
	// Classes derivadas de acções!
	private PesquisarKey pesquisarKey = new PesquisarKey();
	private MouseClicar mouseClicar = new MouseClicar();

	private AdicionarAction adicionarAction = new AdicionarAction();
	private PesquisarAction pesquisarAction = new PesquisarAction();
	private AlterarAction alterarAction = new AlterarAction();
	private DeletarAction deletarAction = new DeletarAction();

	private RdOrcamAction rdOrcam = new RdOrcamAction();
	private RdOrdemServAction rdOS = new RdOrdemServAction();

	private JTextField txtOs;
	private JTextField txtData;

	JComboBox combSitua;
	JRadioButton rdBt1;
	JRadioButton rdBt2;

	JTextField txtPesq;
	JTextField txtId;
	JTable tbCli;

	JTextField txtEquip;
	JTextField txtDef;
	JTextField txtServ;
	JTextField txtTec;
	JTextField txtVal;

	JButton btnSalvar;
	JButton btnPesquisar;
	JButton btnAlterar;
	JButton btnDeletar;
	JButton btnImp;

	Connection conexao = null;
	PreparedStatement prep = null;
	ResultSet resul = null;
	private String tipo;

	public TelaOs() {
		super("Ordem de Serviço");
		formulario();
		conexao = ModuloConexao.conector();
		JanelaInternalFrameOpened(null);
	}

	private void pesquisar() {
		String sql = "Select  idcli as id, nomecli as nome, fonecli as fone from CLIENTE Where nomecli like ?";

		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, txtPesq.getText() + "%");
			resul = prep.executeQuery();
			tbCli.setModel(DbUtils.resultSetToTableModel(resul));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	private void setarTabela() {
		int setar = tbCli.getSelectedRow();
		// (Linha,Coluna)
		txtId.setText(tbCli.getModel().getValueAt(setar, 0).toString());

	}

	private class RdOrcamAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			tipo = "Orçamento";
			System.out.println(tipo);
		}

	}

	private class RdOrdemServAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			tipo = "O.S";
			System.out.println(tipo);

		}
	}

	private class PesquisarKey extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			pesquisar();
		}

	}

	private class MouseClicar extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			setarTabela();
		}

	}

	private void emitir_OS() {
		String sql = "Insert into OS(tipo,situacao,equipamento,defeito,servico,tecnico,valor," + "idcli) "
				+ "values(?,?,?,?,?,?,?,?)";
		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, tipo);
			prep.setString(2, String.valueOf(combSitua.getSelectedItem()));
			prep.setString(3, txtEquip.getText());
			prep.setString(4, txtDef.getText());
			prep.setString(5, txtServ.getText());
			prep.setString(6, txtTec.getText());
			prep.setString(7, txtVal.getText().replace(",", "."));
			prep.setString(8, txtId.getText());
			if ((txtId.getText().isEmpty()) || (txtEquip.getText().isEmpty()) || (txtDef.getText().isEmpty())) {
				JOptionPane.showMessageDialog(null, "Por Favor, Preencha os Campos Obrigatórios!");

			} else {
				int adicionado = prep.executeUpdate();
				if (adicionado > 0) {
					JOptionPane.showMessageDialog(null, "OS emitida com Sucesso!");
					// LIMPANDO O CASH
					txtEquip.setText(null);
					txtDef.setText(null);
					txtServ.setText(null);
					txtTec.setText(null);
					txtVal.setText("0");
					txtId.setText(null);

				}

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}

	}

	private class AdicionarAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			emitir_OS();
		}
	}

	private class PesquisarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			pesquisar_Os();
		}

	}

	private class DeletarAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			excluir_OS();
		}
	}

	private void JanelaInternalFrameOpened(java.awt.event.ActionEvent event) {
		rdBt1.setSelected(true);
		tipo = "Orçamento";
		txtOs.setEnabled(false);
		txtData.setEnabled(false);
	}

	private void pesquisar_Os() {
		String num_Os = JOptionPane.showInputDialog("Número da O.S");

		String sql = "Select * from OS where idos = " + num_Os;
		try {
			prep = conexao.prepareStatement(sql);
			resul = prep.executeQuery();
			if (resul.next()) {
				txtOs.setText(resul.getString(1));
				txtData.setText(resul.getString(2));
				String rdBut = resul.getString(3);
				if (rdBut.equals("Ordem de Serviço")) {
					rdBt2.setSelected(true);
					tipo = "O.S";

				} else {
					rdBt1.setSelected(true);
					tipo = "Orçamento";
				}
				combSitua.setSelectedItem(resul.getString(4));
				txtEquip.setText(resul.getString(5));
				txtDef.setText(resul.getString(6));
				txtServ.setText(resul.getString(7));
				txtTec.setText(resul.getString(8));
				txtVal.setText(resul.getString(9));
				txtId.setText(resul.getString(10));

				txtPesq.setEnabled(false);
				btnSalvar.setEnabled(false);
				tbCli.setVisible(false);

			} else {
				JOptionPane.showMessageDialog(null, "O.S Não Encontrada!");

			}
		} catch (java.sql.SQLSyntaxErrorException e) {
			JOptionPane.showMessageDialog(null, "O.S INVÁLIDA");
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, e2);

		}

	}

	private void alterar_OS() {
		String sql = "Update OS Set tipo =?, situacao = ?, equipamento =?, defeito =?, servico =?,"
				+ "tecnico =?, valor =?" + "where idos = ?";
		try {
			prep = conexao.prepareStatement(sql);
			prep.setString(1, tipo);
			prep.setString(2, String.valueOf(combSitua.getSelectedItem()));
			prep.setString(3, txtEquip.getText());
			prep.setString(4, txtDef.getText());
			prep.setString(5, txtServ.getText());
			prep.setString(6, txtTec.getText());
			prep.setString(7, txtVal.getText().replace(",", "."));
			prep.setString(8, txtOs.getText());
			if ((txtId.getText().isEmpty()) || (txtEquip.getText().isEmpty()) || (txtDef.getText().isEmpty())) {
				JOptionPane.showMessageDialog(null, "Por Favor, Preencha os Campos Obrigatórios!");

			} else {
				int adicionado = prep.executeUpdate();
				if (adicionado > 0) {
					JOptionPane.showMessageDialog(null, "OS Alterada com Sucesso!");
					txtOs.setText(null);
					txtData.setText(null);
					txtEquip.setText(null);
					txtDef.setText(null);
					txtServ.setText(null);
					txtTec.setText(null);
					txtVal.setText("0");
					txtId.setText(null);
					btnSalvar.setEnabled(true);
					txtPesq.setEnabled(true);
					tbCli.setVisible(true);

				}

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}
	}

	private void excluir_OS() {
		// FAZER A VALIDAÇÃO DO COMANDO SQL, VALIDAÇÃO!
		int conf = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja Remover Esta O.S?", "Atenção!",
				JOptionPane.YES_NO_OPTION);

		if (conf == JOptionPane.YES_OPTION) {
			/*
			 * OUTRO JEITO DE EXCLUIR UMA O.S if(JOptionPane.showConfirmDialog(null,
			 * "Deseja excluir a OS ?", "Confirmação", 0)==0){
			 */
			// se for igual a 0 executa oque está dentro do if

			String sql = "DELETE from OS " + "Where idos =?";
			try {
				prep = conexao.prepareStatement(sql);
				prep.setString(1, txtOs.getText());
				int apagado = prep.executeUpdate();
				// AQUI É PARA VALIDAR SE FOI EXECUTADO O COMANDO SQL
				if (apagado > 0) {
					JOptionPane.showMessageDialog(null, "O.S Removida com Sucesso!");
					// LIMPANDO O CASH
					txtOs.setText(null);
					txtData.setText(null);
					txtEquip.setText(null);
					txtDef.setText(null);
					txtServ.setText(null);
					txtTec.setText(null);
					txtVal.setText("0");
					txtId.setText(null);
					// HABILITANDO OS OBJETOS
					btnSalvar.setEnabled(true);
					txtPesq.setEnabled(true);
					tbCli.setVisible(true);
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);

			}
		}

	}

	private class AlterarAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			alterar_OS();

		}

	}

	private void formulario() {

		this.setMaximizable(false);
		this.setIconifiable(true);
		this.setResizable(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		this.setSize(624, 427);
		this.setVisible(true);
		// SETANDO O LAYOUT DO JINTERNALFRAME
		this.setLayout(new BorderLayout());
		// this.addInternalFrameListener(inernalAction);

		// this.setLayout(new BorderLayout());
		// JPanel panelEsq = new JPanel();
		// panelEsq.setBackground(Color.GRAY);
		// panelEsq.setLayout(new MigLayout());
		// panelEsq.setPreferredSize(new Dimension(270,200));
		JPanel panelCent = new JPanel();
		// panelCent.setBackground(Color.GREEN);
		panelCent.setLayout(new MigLayout("wrap 2"));

		JPanel panelCon = new JPanel();
		// panelCon.setPreferredSize(new Dimension(180,200));
		panelCon.setLayout(new MigLayout());
		// panelCon.setBackground(Color.GREEN);
		/*
		 * CRIANDO UMA BORDA SIMPLES COM UM TÍTULO
		 * BorderFactory.createTitledBorder("QUALQUER TÍTULO EM STRING")
		 * setBorder(BorderFactory.createTitledBorder("O.S"));
		 */
		panelCon.setBorder(BorderFactory.createTitledBorder("O.S"));
		JLabel lblOs = new JLabel("N° OS");
		JLabel lblData = new JLabel("Data");

		txtOs = new JTextField(5);

		txtData = new JTextField(15);
		txtData.setFont(new Font("Tahoma", Font.BOLD, 10));
		// CRIANDO UM ARRY'S OU UMA LIST PARA COLOCAR NO JCOMBOBOX
		String[] Lista = { "Na Bancada", "Orçamento REPROVADO", "Aguardando Aprovação", "Aguardando Peças",
				"Abandonado pelo Cliente", "Entrega OK", "Retornou" };
		combSitua = new JComboBox(Lista);
		JLabel lblSitua = new JLabel("Situação");
		// CRIANDO UM PANEL PARA COLOCAR OS DOIS COMPONENTES
		JPanel panelSitua = new JPanel();
		panelSitua.add(lblSitua);
		panelSitua.add(combSitua);

		// CRIANDO OS JRADIOBUTTON
		rdBt1 = new JRadioButton("Orçamento");
		rdBt1.addActionListener(rdOrcam);

		rdBt2 = new JRadioButton("Ordem de Serviço");
		rdBt2.addActionListener(rdOS);
		// CRIANDO UM PANEL PARA CONTER OS COMPONENTES DO CLIENTE!
		JPanel panelCli = new JPanel();
		panelCli.setLayout(new MigLayout("wrap 4"));// DIZENDO QUE SÓ VAI TER 4 COLUNAS NESTE PANEL!
		panelCli.setBorder(BorderFactory.createTitledBorder("Cliente"));
		// panelCli.setPreferredSize(new Dimension(400,850));

		txtPesq = new JTextField(20);
		txtPesq.addKeyListener(pesquisarKey);
		JLabel lblIcone = new JLabel();
		lblIcone.setIcon(new ImageIcon(getClass().getResource("/br/com/SistemaOS/icones/search.png")));
		JLabel lblId = new JLabel("*Id");
		txtId = new JTextField(6);
		txtId.setEnabled(false);

		String[] coluna = { "Id", "Nome", "Fone" };

		Object[][] dados = { { "", "", "" }, { "", "", "" }, { "", "", "" }, { "", "", "" }, };

		tbCli = new JTable(dados, coluna);
		tbCli.setPreferredScrollableViewportSize(new Dimension(320, 63));// y 60 x 350
		tbCli.setFillsViewportHeight(true);
		// CRIANDO UMA TIPO DE PANEL PARA SCROOL E ADICIONANDO A TABELA A ELA!
		JScrollPane panelScrol = new JScrollPane(tbCli);
		tbCli.addMouseListener(mouseClicar);
		// panelCli.add(new JLabel(),"wrap");
//			JPanel panelLabel = new JPanel(new MigLayout());k
//			panelLabel.add(new JLabel("t"),"gap 0 0 0 0");
//			panelLabel.add(new JLabel("T"),"gap 0 0 0 0");

		panelCli.add(txtPesq);
		panelCli.add(lblIcone);
		panelCli.add(lblId);
		panelCli.add(txtId);
		panelCli.add(panelScrol, "spanx 4");
		// CRIANDO UM PANEL PARA CONTER O FORMULARIO ORDEM DE SERVIÇO
		JPanel panelForm = new JPanel();
		panelForm.setLayout(new MigLayout());
		// CRIANDO AS JLABEL'S
		JLabel lblEquip = new JLabel("*Equipamento");
		JLabel lblDefeito = new JLabel("*Defeito");
		JLabel lblServ = new JLabel(" Serviço");
		JLabel lblTec = new JLabel("Técnico");
		JLabel lblTotal = new JLabel("Valor Total");

		// CRIANDO OS JTEXTFIELD'S
		txtEquip = new JTextField(60);
		txtDef = new JTextField(60);
		txtServ = new JTextField(60);
		txtTec = new JTextField(15);
		txtVal = new JTextField(15);
		txtVal.setText("0");
		// CRIANDO UM PANEL PARA ARMAZENAR OS CAMPOS TECNICO E VALORTOTAL
		JPanel panelCamp = new JPanel(new MigLayout());
		// TODOS JÁ ESTÃO LATERALMENTE! PELO DEFAULT DO MIGLAYOUT!
		panelCamp.add(new JLabel(), "gap 0 25 0 0");
		panelCamp.add(lblTec);
		panelCamp.add(txtTec, "gap 0 85 0 0");
		panelCamp.add(lblTotal, "gap 0 5 0 0");
		panelCamp.add(txtVal);

		panelForm.add(lblEquip);
		panelForm.add(txtEquip, "wrap");
		panelForm.add(lblDefeito, "gap 33 0 0 0");
		panelForm.add(txtDef, "wrap");
		panelForm.add(lblServ, "gap 33 0 0 0");
		panelForm.add(txtServ, "wrap -3");
		panelForm.add(panelCamp, "span");

		// CRIANDO OS BOTÕES ONDE FICARAM AS OPERAÇÕES DO CRUD DO SISTEMA JAVA COM MYSQL

		btnSalvar = new JButton();
		btnSalvar.addActionListener(adicionarAction);
		btnPesquisar = new JButton();
		btnPesquisar.addActionListener(pesquisarAction);
		btnAlterar = new JButton();
		btnAlterar.addActionListener(alterarAction);
		btnDeletar = new JButton();
		btnDeletar.addActionListener(deletarAction);
		btnImp = new JButton();

		// btnDeletar.addActionListener(deletarAction);
		// SETANDO O TAMANHO DOS BOTÕES
		// btnSalvar.setPreferredSize(new Dimension(80,80));
		// btnPesquisar.setPreferredSize(new Dimension(80,80));
		// btnAlterar.setPreferredSize(new Dimension(80,80));
		// btnDeletar.setPreferredSize(new Dimension(80,80));

		btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Adicionar.png")));
		btnSalvar.setBorder(new BordaRedonda(10));
		JPanel panelBtn = new JPanel(new MigLayout());
		// panelBtn.setBackground(Color.ORANGE);
		panelBtn.setPreferredSize(new Dimension(0, 100));
		// panelBtn.setPreferredSize(new Dimension(639,140));
		btnPesquisar
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Pesquisar.png")));
		btnPesquisar.setBorder(new BordaRedonda(10));

		btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Editar.png")));
		btnAlterar.setBorder(new BordaRedonda(10));

		btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/Deletar.png")));
		btnDeletar.setBorder(new BordaRedonda(10));

		btnImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/impressora.png")));
		btnImp.setBorder(new BordaRedonda(10));

		// CRIANDO UMA HINT/DICA! QUANDO O MOUSE FICA SOBRE O BOTÃO OU QUALQUER
		// COMPONENTE

		btnSalvar.setToolTipText("Adicionar");

		btnPesquisar.setToolTipText("Pesquisar");

		btnAlterar.setToolTipText("Modificar");

		btnDeletar.setToolTipText("Deletar");

		btnImp.setToolTipText("Imprimir");
		// MUDANDO O FORMATO DO MOUSE QUANDO FICAR SOBRE ESTES COMPONENTES!

		btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnPesquisar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAlterar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeletar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnImp.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// ADICIONANDO OS COMPONENTES AO PAINEL

		panelBtn.add(btnSalvar, "gap 20 10 0 0");
		panelBtn.add(btnPesquisar, "gap 0 10  0 0");
		panelBtn.add(btnAlterar, "gap 0 10 0 0");
		panelBtn.add(btnDeletar, "gap 0 10 0 0");
		panelBtn.add(btnImp);

		/*
		 * CRIANDO UM GRUPO DE BOTÕES! E COLOCANDO OS RADIOBUTTONS NELE PARA AGRUPAR
		 * FAZENDO ISSO ELE SÓ PODE SELECIONAR UM RADIOBOTTON DE CADA VEZ!
		 */
		ButtonGroup grupBtn = new ButtonGroup();
		grupBtn.add(rdBt1);
		grupBtn.add(rdBt2);

		panelCon.add(lblOs, "gap 0 10 0 0");
		panelCon.add(lblData, "wrap");
		panelCon.add(txtOs);
		panelCon.add(txtData, "wrap 10");
		panelCon.add(rdBt1, "gap 0 0 0 0");
		panelCon.add(rdBt2, "gap 3 0 0 0");
		// COLOCANDO UM JLABEL PARA DAR ESPAÇAMENTO NO PANELESQUERDO!
		// panelEsq.add(new JLabel(),"wrap 10");

		// panelEsq.add(panelCon,"wrap 10 ");
		// panelEsq.add(panelSitua);
		panelCent.add(panelCon);
		panelCent.add(panelCli, "spany 2");// wrap 10
		/// panelCent.add(new JLabel(),"wrap 10");
		panelCent.add(panelSitua);
		// panelCent.add(,"split 3");//PanelLabel
//			panelCent.add(new JLabel("C"),"gap 0 0 -10 0");
//			panelCent.add(new JLabel("E"),"wrap 10");

		panelCent.add(panelForm, "span");
		this.add(panelCent, BorderLayout.CENTER);
		this.add(panelBtn, BorderLayout.SOUTH);

	}
	/*
	 * Não Adianta Chamar no método por que ele não é componente de 1 Grandeza, e
	 * sim de Segunda! public static void main(String[] args) { new
	 * TelaOs().setVisible(true); }
	 */

}
