package primeiraAtividade01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD{
    public static Connection conectar(){
        String url = "jdbc:mysql://localhost:3306/BibliotecaSakurasJapao";//Aqui você usa o JDBC:... e no fim, coloca o nome do seu banco de dados.
        String usuario = "root";
        String senha = "";

        try{
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            //System.out.println("\nConectado com sucesso");
            return conexao;
        }catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }
}