package br.com.Sistema.dal.ModuloConexao;//DAL(DATA ACCESS LAYER) CAMADA PARA ACESSO DE DADOS

import java.sql.*;

public class ModuloConexao {

	public static Connection conector() {
		java.sql.Connection conexao = null;
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/SISTEMAOS";
		String user = "root";
		String password = "";

		try {
			Class.forName(driver);
			conexao = DriverManager.getConnection(url, user, password);
			return conexao;
		} catch (Exception e) {
			System.out.println(e);
			return null;

		}

	}

}
