package primeiraAtividade01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class biblioteca {
    private Connection conexao;
    public biblioteca(){
        this.livros = new ArrayList<>();
        this.conexao = ConexaoBD.conectar();
        if(conexao != null){
            this.livros = carregarLivros();
        }else{
            System.out.println("Erro ao conectar com o banco de dados.");
        }
    }
    private List<Livro> livros;
    public List<Livro> carregarLivros(){
            List<Livro> livrosCarregados = new ArrayList<>();

            try(PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM livros"); 
                ResultSet rs = stmt.executeQuery()){
                    while (rs.next()) {
                        String titulo = rs.getString("titulo");
                        String autor = rs.getString("autor");
                        String isbn = rs.getString("isbn");
                        int quantidade = rs.getInt("quantidade");

                        Livro livro = new Livro(titulo, autor, isbn, quantidade);
                        livrosCarregados.add(livro);
                        
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }

            return livrosCarregados;
        }

    public void cadastrarLivro(String titulo, String autor, String isbn, int quantidade){
        Livro livro = new Livro(titulo, autor, isbn, quantidade);
        livros.add(livro);
        System.out.println("Livro cadastrado com sucesso!");
        primeiraAtividade01.gravarLivros.cadastrarLivro(titulo, autor, isbn, quantidade);
        
    }

    public void emprestarLivro(String isbn){
        for(Livro livro : livros){
            if(livro.getIsbn().equals(isbn)){
                if(livro.isDisponivel()){
                    livro.emprestar();
                    atualizarQuantidade(livro);
                    System.out.println("Livro emprestado com sucesso.");
                }else{
                    System.out.println("Livro indisponível.");
                }
                return;
            }
        }
        System.out.println("Livro não encontrado.");
    }

    public void atualizarQuantidade(Livro livro){//Aqui nesse public void ele atualizar tudo que o usuário pediu.
    try(PreparedStatement stmt = conexao.prepareStatement("UPDATE livros SET quantidade = ? WHERE isbn = ?")){//Nesse trecho, estamos preparando um comando SQL, usando a conexão, como podemos ver acima.
        stmt.setInt(1, livro.getQuantidade());//Nesse trecho, estou fazendo o seguinte, estou pegando o primeiro "?" e estou mandando ele receber o valor da variável "livro". Logo, esse setInt(1) ele vai substituir ? por um numero inteiro, ou string, dependendo da situação, nesse caso, a "quantidade", que está sendo reduzida. 
        stmt.setString(2, livro.getIsbn());
        stmt.executeUpdate();//Aqui ele vai executar um update, que serve para atualizações no banco de dados. Caso você queira fazer select de algo, utilize um executeQuery. Que ele serve para select.
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void devolverLivro(String isbn){
        for(Livro livro : livros){
            if(livro.getIsbn().equals(isbn)){
                if(livro.getQuantidade() > 10){
                    System.out.println("O livro já está na sua capacidade máxima.");
                }else{
                    livro.devolver();
                    System.out.println("Livro devolvido com sucesso");
                    atualizarQuantidade(livro);
                }
                return;
            }
        }
        System.out.println("Livro não encontrado."); 
    }

    public void listarLivros(){
        if(livros.isEmpty()){
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        System.out.println("Lista de Livros: ");
        for(Livro livro : livros){
            System.out.println("\n|" + livro);
        }
    }
    
}

class Livro{
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidade;

    public Livro(String titulo, String autor, String isbn, int quantidade){
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidade = quantidade;
    }
    
    public String getTitulo(){
        return titulo;
    }
    public String getAutor(){
        return autor;
    }
    public String getIsbn(){
        return isbn;
    }
    public int getQuantidade(){
        return quantidade;
    }
    public boolean isDisponivel(){
        return quantidade > 0;
    }
    public void emprestar(){
        if(quantidade > 0){
            quantidade --;
        };
    }
    public void devolver(){
        quantidade ++;
    }
    @Override
    public String toString(){
        String status = quantidade > 0 ? " Disponível" : " Indisponível";
        return titulo + " - " + autor + " (ISBN: " + isbn + ") -" + " Quantidade: " + quantidade + " - Status: " +
        status ;
    }
}
