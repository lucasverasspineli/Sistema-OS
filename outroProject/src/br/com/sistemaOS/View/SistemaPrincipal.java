package br.com.sistemaOS.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class SistemaPrincipal extends JFrame {
	private Atl_A_Action a_Action = new Atl_A_Action();
	private JMenuItem menuItemSair;

	public static JLabel labelUsuar;
	public ClienteAction clienteAction = new ClienteAction();
	public static JMenuItem menuItemUsuario;
	public UsuarioAction usuarioAction = new UsuarioAction();
	public OsAction osAction = new OsAction();

	public static JMenu menuRelatorio;

	public JDesktopPane desk;

	private SairAction sair_Action = new SairAction();
	private JLabel labelData;
	private JMenuItem menuItemCliente;

	private AjudaAction ajudaAction = new AjudaAction();

	public int count_cliente = 0, count_os = 0, count_user = 0;

	public SistemaPrincipal() {
		super("SISTEMA DE ORDEM DE SERVIÇO");
		tela();
		criarMenu();
		JanelaActivated(null);
	}

	private class Atl_A_Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Foi apertado o botão do Menu");

		}

	}

	private class ClienteAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				TelaCliente tCli = new TelaCliente();
				tCli.setVisible(true);
				desk.add(tCli);

			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, e);

			}

		}

	}

	private class SairAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			int confirm = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja Sair do Sistema?", "ATENÇÃO",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				System.exit(0);

			}

		}

	}

	private class AjudaAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			TelaSobre tlSobre = new TelaSobre();
			tlSobre.setVisible(true);

		}

	}

	private class UsuarioAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				TelaUsuario tU = new TelaUsuario();
				tU.setVisible(true);
				desk.add(tU);
				// EXEÇÃO ESPECIFICA DO JFORMATTEDTEXTFIELD E MAKSFORMATTER,
			} catch (java.text.ParseException e2) {
				// PILHA DE ERROS, DE EXEÇÃO SE HOUVER!
				e2.printStackTrace();
			}

		}

	}

	private class OsAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			TelaOs tOs = new TelaOs();
			tOs.setVisible(true);
			desk.add(tOs);

		}

	}

	private void criarMenu() {

		JMenu menuCadastro = new JMenu("Cadastro");
		menuItemCliente = new JMenuItem("Cliente ");// KeyEvent.VK_A
		menuItemCliente.addActionListener(clienteAction);

		menuCadastro.add(menuItemCliente);

		JMenuItem menuItemOs = new JMenuItem("OS");
		menuItemOs.addActionListener(osAction);
		menuCadastro.add(menuItemOs);

		menuItemUsuario = new JMenuItem("Usuários");
		menuItemUsuario.setEnabled(false);

		menuItemUsuario.addActionListener(usuarioAction);
		menuCadastro.add(menuItemUsuario);

		menuRelatorio = new JMenu("Relatório");
		menuRelatorio.setEnabled(false);
		JMenuItem menuItemServico = new JMenuItem("Serviços");

		menuRelatorio.add(menuItemServico);

		JMenu menuAjuda = new JMenu("Ajuda");
		JMenuItem menuItemSobre = new JMenuItem("Sobre");
		menuItemSobre.addActionListener(ajudaAction);
		menuAjuda.add(menuItemSobre);

		JMenu menuOpcao = new JMenu("Opções");
		menuItemSair = new JMenuItem("Sair");
		menuItemSair.addActionListener(sair_Action);
		menuOpcao.add(menuItemSair);

		// CRIANDO ITENS DE MENU
		JMenuBar menuBarra = new JMenuBar();
		this.setJMenuBar(menuBarra);

		menuBarra.add(menuCadastro);
		menuBarra.add(menuRelatorio);
		menuBarra.add(menuAjuda);
		menuBarra.add(menuOpcao);
	}

	public void JanelaActivated(java.awt.event.WindowEvent e) {
		Date data = new Date();
		DateFormat formataData = DateFormat.getDateInstance(DateFormat.SHORT);// COM ESSE METÓDO FICA ASSIM! [
																				// 04/03/2021]
		labelData.setText(formataData.format(data));

	}

	private void tela() {
		this.setSize(935, 529);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		desk = new JDesktopPane();
		JPanel panelNort = new JPanel();
		this.setLayout(new BorderLayout());

		panelNort.setPreferredSize(new Dimension(639, 20));

		panelNort.setBackground(Color.decode("#90ee90"));
		desk.setBackground(Color.BLACK);
		this.add(panelNort, BorderLayout.NORTH);
		JPanel panelLest = new JPanel();
		panelLest.setLayout(new MigLayout("wrap 1"));
		panelLest.setBackground(Color.decode("#90ee90"));
		panelLest.setPreferredSize(new Dimension(296, 529));

//	

		labelUsuar = new JLabel("Usuário");
		labelUsuar.setFont(new Font("Verdana", Font.BOLD, 20));

		panelLest.add(labelUsuar, "gap 90 0 70 20");

		labelData = new JLabel("Data");
		labelData.setFont(new Font("Verdana", Font.BOLD, 20));
		panelLest.add(labelData, "gap 90 0 0 30");

		// LOGO DO PROJETO
		JLabel logo = new JLabel();
		logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistemaOS/icones/UserClient.png")));
		panelLest.add(logo, "gap 87 0 20 0");

		// ADICIONANDO TODA O JPANELEAST NO LAYOUT PRINCIPAL DO FRAME
		this.add(panelLest, BorderLayout.EAST);

		JPanel paneSul = new JPanel();
		// paneSul.setBackground(Color.RED);
		paneSul.setBackground(Color.decode("#90ee90"));
		paneSul.setPreferredSize(new Dimension(935, 20));

		this.add(paneSul, BorderLayout.SOUTH);

		this.add(desk);

	}

	public static void main(String[] args) {
		new SistemaPrincipal().setVisible(true);

	}

}
