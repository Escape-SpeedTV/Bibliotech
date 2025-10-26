package primeiraAtividade01;
import java.util.Random;
import java.util.Scanner;
import primeiraAtividade01.*;
import primeiraAtividade01.atualizarouDeletar.Pausar;

public class Main {
    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);
        int opcao = 0;

        System.out.print("\nSeja Bem Vindo(a) a Biblioteca Sakuras do Japão");
        biblioteca b = new biblioteca();
        logineSenha logineSenha = new logineSenha();

        do {
            try{
                System.out.print("\nMenu da Biblioteca:\n|1 - Cadastrar Livro\n|2 - Emprestar Livro\n|3 - Devolver Livro\n|4 - Listar Livros\n|5 - Atualizar livros\n|0 - Sair\nOpção: ");
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1:
                        boolean acessoAutorizado = false;
                        int tentativas = 3;

                        while(!acessoAutorizado) {
                            System.out.print("\nDigite o nome de usuário ADM:  ");
                            String login = sc.nextLine();

                            System.out.print("\nDigite a senha: ");
                            int senha = sc.nextInt();
                            sc.nextLine();

                            boolean autorizado = logineSenha.login(login, senha);

                            if(autorizado){
                                acessoAutorizado = true;
                            }else{
                                tentativas --;

                                System.out.println("Numero de tentativas restantes: " + tentativas);
                                if(tentativas <= 0){
                                    System.out.println("Numero de tentativas excedida, tente novamente mais tarde.");
                                    return;
                                }
                            }
                            
                        }

                        System.out.print("\nTitulo do Livro: ");
                        String titulo = sc.nextLine();

                        System.out.print("\nAutor desse livro: ");
                        String autor = sc.nextLine();

                        Random rand = new Random();
                        StringBuilder isbn = new StringBuilder();

                        for(int i = 0; i < 13; i++){
                            int indice = rand.nextInt(10);
                            isbn.append(indice);
                        }

                        int quantidade = 0;
                        while (true){
                            try{
                                quantidade = 0;
                                System.out.print("\nQuantos livros deseja colocar em estoque? ");
                                quantidade = sc.nextInt();
                                sc.nextLine();

                                if(quantidade <= 0 || quantidade > 10){
                                    System.out.println("Erro! A quantidade de livros não pode ser inferior a 0 e nem superior a 10");
                                }else{
                                    break;
                                }

                            }catch(Exception e){
                                System.out.println("Erro! Por favor, insira apenas numeros inteiros, tente novamente.");
                                sc.nextLine();
                            }
                        }            

                        gravarLivros.cadastrarLivro(titulo, autor, isbn.toString(), quantidade);
                        break;

                    case 2:
                        int cadastradoOuNao = 0;
                        while(true){
                            try{
                                System.out.print("\n1 - Não possuo cadastro\n2 - Já tenho cadastro\nOpção: ");
                                cadastradoOuNao = sc.nextInt();
                                sc.nextLine();
                                break;
                            }catch(Exception e){
                                System.out.println("Opção inválida! Tente novamente.");
                                sc.nextLine();
                            }
                        }

                        if(cadastradoOuNao == 1){
                            logineSenha.controleUsuario(sc, cadastradoOuNao, null);
                            continue;
                        }else{
                            String isbn_Emprestar = "";
                            b.listarLivros();
                            atualizarouDeletar.Pausar.pausar(sc);
                            
                            boolean isbn_e_IntEncontrado = false;
                            while (!isbn_e_IntEncontrado){
                                System.out.print("\nDigite o ISBN do livros que você deseja pegar: ");
                                isbn_Emprestar = sc.nextLine();
                                if(!isbn_Emprestar.matches("\\d+")){
                                    System.out.println("ISBN inválido, digite apenas números.");
                                }else{ 
                                    try{
                                        System.out.print("Digite o seu ID: ");
                                        int id = sc.nextInt();
                                        sc.nextLine();
                                        boolean usuarioVerdadeiroOuFalso = logineSenha.controleUsuario(sc, cadastradoOuNao, id);
                                        
                                        if(usuarioVerdadeiroOuFalso){
                                            b.emprestarLivro(isbn_Emprestar);
                                            atualizarouDeletar.usuarioEmprestimo(id, isbn_Emprestar, 1);
                                            isbn_e_IntEncontrado = true;
                                        }else{
                                            break;
                                        }
                                    }catch(Exception e){
                                        System.out.println("Erro! Digite apenas numeros inteiros, tente novamente.");
                                        sc.nextLine();
                                    }
                                }
                            }

                        }
                            
                        break;

                    case 3:
                        String isbnLivro = "";
                        while(true){
                            System.out.print("\nNo verso do seu livro existe um código de 13 linhas, chamado ibsn. Digite ele aqui para fazer a devolução: ");
                            isbnLivro = sc.nextLine();
                            if(!isbnLivro.matches("\\d+")){
                                System.out.println("ISBN inválido, digite apenas números;");
                            }else{
                                break;
                            }
                        }
                        System.out.print("\nDigite o seu primeiro nome: ");
                        String nome = sc.nextLine();

                        System.out.print("\nDigite o seu sobrenome: ");
                        String sobrenome = sc.nextLine();

                        int id = 0;
                        while(true){
                            try{
                                System.out.print("\nDigite o seu ID: ");
                                id = sc.nextInt();
                                break;
                            }catch(Exception e){
                                System.out.println("\nErro! Por favor, insira apenas numeros inteiros, tente novamente;");
                            }
                        }
                        
                        boolean confirmarDevolucao = logineSenha.devolucao(id, nome, sobrenome, isbnLivro);

                        if(confirmarDevolucao){
                            b.devolverLivro(isbnLivro);
                            atualizarouDeletar.usuarioEmprestimo(id, null, 0);
                        }
                        break;

                    case 4:
                        b.listarLivros();
                        Pausar.pausar(sc);
                        break;

                    case 5:
                        int opcaoDeEscolha = 0;
                        int tentativa = 3;
                        boolean login_e_SenhaConfirmado = false;
                        while(!login_e_SenhaConfirmado){
                            try{
                                System.out.print("Digite o login de adm: ");
                                String login = sc.nextLine();

                                System.out.print("Senha: ");
                                int senha = sc.nextInt();
                                sc.nextLine();
                                logineSenha.login(login, senha);

                                if (logineSenha.login(login, senha)){
                                    login_e_SenhaConfirmado = true;
                                }else{
                                    tentativa --;
                                    System.out.println("Numero de tentativas restante: " + tentativa);
                                    if(tentativa <= 0){
                                        System.out.println("Numero de tentativa excedido. Tente novamente mais tarde.");
                                        return;
                                    }
                                }

                            }catch(Exception e){
                                System.out.println("Erro: " + e);
                                sc.nextLine();
                            }

                        }
                        while(true){
                            try{
                                System.out.print("\nO que você deseja atualizar?\n|1 - Atualizar Titulo ou Autor\n|2 - Deletar livro\n|0 - Retornar\nOpção ");
                                opcaoDeEscolha = sc.nextInt();
                                sc.nextLine();

                                if(opcaoDeEscolha != 1 && opcaoDeEscolha != 2 && opcaoDeEscolha != 0){
                                    System.out.println("Opção inválida, tente novamente.");
                                }else{
                                    break;
                                }

                            }catch(Exception e){
                                System.out.println("Erro. Por favor, insira apenas numeros, tente novamente.");
                                sc.nextLine();
                            }
                        }

                        if(opcaoDeEscolha == 0){
                            break;
                        }

                        b.listarLivros();
                        atualizarouDeletar.Pausar.pausar(sc);
                        String ISBN = "";
                        while(true){
                            System.out.print("\nDigite o ISBN do livro que deseja fazer a atualização: ");
                            ISBN = sc.nextLine();
                            if(!ISBN.matches("\\d+")){
                                System.out.println("ISBN inválido, digite apenas numeros.");
                            }else{
                                break;
                            }
                        }
                    
                        if(opcaoDeEscolha == 1){
                            atualizarouDeletar.atualizar(sc, ISBN);
                        }else{
                            atualizarouDeletar.Delete.deletar(sc, ISBN);
                        }
                        break;

                    case 0:
                        System.out.println("Volte sempre a biblioteca Sakuras do Japão🌸");
                        break;
                
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                        break;
                }

            }catch(Exception e){
                System.out.println("Erro! Opção inválida, tente novamente.");
                e.printStackTrace();
                sc.nextLine();
            }
            
        } while (opcao != 0);

        sc.close();
    }
    
}
