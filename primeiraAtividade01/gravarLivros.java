package primeiraAtividade01;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class gravarLivros {
    public static void cadastrarLivro(String titulo, String autor, String isbn, int quantidade){
        Connection conexao = ConexaoBD.conectar();
        if(conexao == null) return;

        String sql = "INSERT INTO livros (titulo, autor, isbn, quantidade) VALUES (?, ?, ?, ?)";
        String verificar = "SELECT titulo, autor FROM livros WHERE titulo = ? AND autor = ?";

        try{
            PreparedStatement stmtverificar = conexao.prepareStatement(verificar);
            stmtverificar.setString(1, titulo);
            stmtverificar.setString(2, autor);
            ResultSet rs = stmtverificar.executeQuery();

            if(rs.next()){//Aqui ele vai verificar se algo bate com o titulo ou autor, caso não tenha, ele pula para o else, caso contário, ele retorna uma impressão com uma mensagem dizendo "livro existente..." 
                System.out.println("Livro já existente, tente novamente.");
            }else{
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, titulo);
                stmt.setString(2, autor);
                stmt.setString(3, isbn);
                stmt.setInt(4, quantidade);

                stmt.executeUpdate();
                System.out.println("Livro cadastrado com sucesso!");
            }
            
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Usuario cadastrarUsuario(String nome, String sobrenome, String telefone, int emprestimo, String login, Integer senha, String isbn){
        Connection conexao = ConexaoBD.conectar();
        if(conexao == null) return null;
        String sql = "INSERT INTO usuarios (nome, sobrenome, telefone, emprestimo, login, senha, isbn) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nome);
            stmt.setString(2, sobrenome);
            stmt.setString(3, telefone);
            stmt.setInt(4, emprestimo);
            stmt.setString(5, login);
            stmt.setInt(6, senha);
            if (senha == null) stmt.setNull(6, java.sql.Types.INTEGER); else stmt.setInt(6, senha);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int novoId = 0;
            if(rs.next()){
                novoId = rs.getInt(1);
                System.out.println("Usuário cadastrado com sucesso! Seu novo ID é: " + novoId);
            }
            return new Usuario(nome, sobrenome, telefone, novoId, emprestimo, login, senha, isbn);

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    
    }
    
}
