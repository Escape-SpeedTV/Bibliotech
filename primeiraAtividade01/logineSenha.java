package primeiraAtividade01;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql. Connection;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.NumberParseException;

public class logineSenha {
    private Connection conexao;
    private List<Usuario> usuario;
    public logineSenha(){
        this.usuario = new ArrayList<>();
        this.conexao = ConexaoBD.conectar();
        if(conexao != null){
            this.usuario = carregarUsuario();
        }else{
            System.out.println("Erro ao conectar com o banco de dados");
        }
    }
    public List<Usuario> carregarUsuario(){
        List<Usuario> usuariosCarregados = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM usuarios"); ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                String nome = rs.getString("nome");//Nesse trecho, o nome das colunas tem que ser igual do banco de dados, se não da um erro.
                String sobrenome = rs.getString("sobrenome");
                String telefone = rs.getString("telefone");
                int id = rs.getInt("id");
                int emprestimo = rs.getInt("emprestimo");
                String login = rs.getString("login");
                Integer senhaAdm = (Integer) rs.getInt("senha");
                String isbn = rs.getString("isbn");

                Usuario u = new Usuario(nome, sobrenome, telefone, id, emprestimo, login, senhaAdm, isbn);
                usuariosCarregados.add(u);

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return usuariosCarregados;
    }

    public boolean login(String login, int senha){
        for(Usuario verificar : usuario){
            if(verificar.getLogin().equals(login) && verificar.getSenhaAdm() == senha){
                System.out.print("\nAcesso autorizado.");
                return true;
            }
        }
        System.out.println("Usuário ou Senha inválidos, tente novamente.");
        return false;
    }

    public boolean controleUsuario(Scanner sc, int escolha, Integer id){
        PhoneNumberUtil verificar = PhoneNumberUtil.getInstance();
        while(true){
            try{  
                if(escolha == 1){
                    System.out.print("\nDigite apenas seu primeiro nome: ");
                    String nome = sc.nextLine();
                    System.out.print("\nDigite o seu sobrenome: ");
                    String sobrenome = sc.nextLine();
                    System.out.print("\nQual o seu numero telefone: ");
                    String telefone = "";
                    boolean valido = false;

                    while(!valido){
                        try{
                            telefone = sc.nextLine();
                            Phonenumber.PhoneNumber confirmar = verificar.parse(telefone, "BR");
                            boolean confirmado = verificar.isValidNumber(confirmar);

                            if(confirmado){
                                System.out.println("Numero válido.");
                                valido = true;
                            }else{
                                System.out.println("Número inválido, tente novamente.");
                            }

                        }catch (NumberParseException e){
                            System.out.println("\nErro! Numero inválido, tente novamente.");
                        }
                    }

                    Usuario novUsuario = gravarLivros.cadastrarUsuario(nome, sobrenome, telefone, 0, null, null,
                    null);
                    usuario.add(novUsuario);

                }else if(escolha == 2){
                    System.out.print("Digite o seu primeiro nome: ");
                    String user = sc.nextLine();

                    System.out.print("Digite o seu sobrenome: ");
                    String sobrenome = sc.nextLine();

                    Usuario encontrado = null;
                    for(Usuario u : usuario){
                        if(u.getId() != null && u.getId().equals(id)){
                            if(u.getNome().equalsIgnoreCase(user) && u.getSobrenome().equalsIgnoreCase(sobrenome)){
                                encontrado = u;
                                break;
                            }
                        }
                    }

                    if(encontrado == null){
                        System.out.println("\nO ID ou Usuário não existe.");
                        return false;
                    }
                    if(encontrado.getEmprestimo() == 1){
                        String nome = (encontrado.getNome() != null) ? encontrado.getNome() :  "(sem nome)";
                        System.out.println("\nAtenção: " + nome + " Você já tem um livro emprestado! Devolva o livro para conseguir pegar outros livros");
                        return false;
                        
                    }else{
                        encontrado.emprestar();
                        return true;
                    }
                        
                }

            }catch(Exception e){
                System.out.println("Erro: " + e);
                sc.nextLine();
            }

        }
        
    }
    public boolean devolucao(Integer id, String nome, String sobrenome, String isbn){
        Usuario encontrado = null;
        for(Usuario u : usuario){
            if(u.getId() != null && u.getId().equals(id)){
                if(u.getNome().equalsIgnoreCase(nome) && u.getSobrenome().equalsIgnoreCase(sobrenome) && u.getIsbn().equals(isbn)){
                    encontrado = u;
                    break;
                }
            }
        }

        if(encontrado == null){
            System.out.println("\nAlgo deu errado! Verifique as informações, e insira novamente.");
            return false;
        }else{
            encontrado.devolver();
            return true;
        }
        
    }
}

class Usuario{
    private String nome;
    private String sobrenome;
    private String telefone;
    private Integer id;
    private int emprestimo; 
    private String login;
    private Integer senhaAdm;
    private String isbn;

    public Usuario(String nome, String sobrenome, String telefone, Integer id, int emprestimo, String login, Integer senhaAdm, String isbn){
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.id = id;
        this.emprestimo = emprestimo;
        this.login = login;
        this.senhaAdm = senhaAdm;
        this.isbn = isbn;
    }
    public String getNome(){
        return nome;
    }
    public String getSobrenome(){
        return sobrenome;
    }
    public String getTelefone(){
        return telefone;
    }
    public Integer getId(){
        return id;
    }
    public int getEmprestimo(){
        return emprestimo;
    }
    public void setEmprestimo(int emprestimo){
        this.emprestimo = emprestimo;
    }
    public String getLogin(){
        return login;
    }
    public Integer getSenhaAdm(){
        return senhaAdm;
    }
    public String getIsbn(){
        return isbn;
    }
    
    public void emprestar(){
        this.emprestimo = 1;
    }
    public void devolver(){
        this.emprestimo = 0;
    }
}
