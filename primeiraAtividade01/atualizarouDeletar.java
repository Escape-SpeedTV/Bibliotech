package primeiraAtividade01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class atualizarouDeletar {
    public static void atualizar(Scanner sc, String isbn){
        biblioteca b = new biblioteca();
        Connection conexao = ConexaoBD.conectar();//Aqui eu estou criando uma conexão com o banco de dados, e claro, fazendo a importação do meu método "ConexaoBD.java".
        if(conexao == null) return;//Se a conexão for nula, ele não conclui e retona. 

        Map<Integer, String> campos = new HashMap<>();
        campos.put(1, "titulo");
        campos.put(2, "autor");
        List<Livro> livrus = b.carregarLivros();
        
        System.out.print("\nEscolha o que deseja atualizar: ");
        for(Map.Entry<Integer, String> entry : campos.entrySet()){
            System.out.printf("\n%d - %s\n", entry.getKey(), entry.getValue());
        }
        int escolha = 0;
        while(true){
            try{
                escolha = 0;
                System.out.print("\nOpção: ");
                escolha = sc.nextInt();
                sc.nextLine();

                if(escolha != 1 && escolha != 2){
                    System.out.println("Opção inexistente, tente novamente.");
                }else{
                    break;
                }

            }catch(Exception e){
                System.out.println("Erro! Por favor, insira apenas numeros inteiros, tente novamente.");
            }
        }

        String campo = campos.get(escolha);

        String novoValor = "";
        boolean encontrado = false;
        for(Livro livros : livrus){
            if(livros.getIsbn().equals(isbn)){
                System.out.println("Valor antigo: " + livros.getAutor() + " - " + livros.getTitulo());
                encontrado = true;
            }
                
        }
        if(!encontrado){
            System.out.println("Erro. IBSN inexistente");
            return;
        }
        while(true){
            if(escolha == 1){
                System.out.print("\nDigite o novo titulo: ");
                novoValor = sc.nextLine();
                if(novoValor.trim().isEmpty()){
                    System.out.println("O valor não pode ser nulo, tente novamente.");
                }else{
                    break;
                }
            }else{
                System.out.print("\nDigite o novo Autor: ");
                novoValor = sc.nextLine();
                if(novoValor.trim().isEmpty()){
                    System.out.println("O valor não pode ser nulo, tente novamente.");
                }else{
                    break;
                }
            }

        }

        for(Livro livro : livrus){
            if(livro.getIsbn().equals(String.valueOf(isbn))){
                atualizarGeral(conexao, campo, novoValor, isbn);
                break;
            }
        }
        
    }

    public static void atualizarGeral(Connection conexao, String campo, String novoValor, String isbn){
        try(PreparedStatement stmt = conexao.prepareStatement("UPDATE livros SET " + campo + " = ? WHERE isbn = ?")){
            stmt.setString(1, novoValor);
            stmt.setString(2, isbn);
            int linhasAfetadas = stmt.executeUpdate();
            if(linhasAfetadas > 0){
                System.out.println("Livro Atualizado com sucesso!");
            }else{
                System.out.println("Nenhum livro encontrado com o ISBN informado.");
            }
        }catch(SQLException e){
            System.out.println("Erro ao Atualizar: " + e.getMessage());
        }
    }

    static class Delete{
        public static void deletar(Scanner sc, String isbn){
            biblioteca b = new biblioteca();
            Connection conexao = ConexaoBD.conectar();
            if(conexao == null) return;

            b.listarLivros();
            Pausar.pausar(sc);

            try(PreparedStatement verificastmt = conexao.prepareStatement("SELECT * FROM livros WHERE isbn = ?")){
                verificastmt.setString(1, isbn);
                var resultado = verificastmt.executeQuery();

                if(!resultado.next()){
                    System.out.println("ISBN não encontrado. Nenhuma exclusão será feita.");
                    return;
                }

            }catch(SQLException e){
                    System.out.println("Erro ao verificar o ISBN: " + e.getMessage());
                    return;
            }

            String sql = "DELETE FROM livros WHERE isbn = ?";
            try(PreparedStatement stmt = conexao.prepareStatement(sql)){
                stmt.setString(1, isbn);

                int linhasAfetadas = stmt.executeUpdate();
                if(linhasAfetadas > 0){
                    System.out.println("Livro deletado com sucesso");
                }else{
                    System.out.println("Livro não encontrado");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void usuarioEmprestimo(int id, String isbn, int numeroEmprestimo){
        Connection conexao = ConexaoBD.conectar();
        if(conexao == null) return;

        String sql = "update usuarios set emprestimo = ?, ISBN = ? where id = ?";
        try{
            PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(3, id);
            stmt.setString(2, isbn);
            stmt.setInt(1, numeroEmprestimo);

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    static class Pausar{
        public static void pausar(Scanner sc){
            System.out.print("\nPressione 'ENTER' para continuar ");
            sc.nextLine();
        }
    }
    
}