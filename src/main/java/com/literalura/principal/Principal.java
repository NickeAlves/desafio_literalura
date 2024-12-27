package com.literalura.principal;

import com.literalura.model.*;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LivroRepository;
import com.literalura.service.ConsumoApi;
import com.literalura.service.ConverteDados;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados converteDados = new ConverteDados();
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final String URL_BASE = "https://gutendex.com/books/?search=";

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    \n
                             - LiteraLura -
                    
                    *** Selecione a opção abaixo ***
                    
                    1 - Buscar livro pelo título
                    2 - Buscar autor por nome
                    3 - Listar livros registrados
                    4 - Listar autores registrados
                    5 - Listar livros por um idioma
                    6 - Listar autores vivos em um determinado ano
                    7 - Listar top 10 livros mais baixados
                                        
                    0 - Sair
                    
                    """;
            System.out.println(menu);

            System.out.print("Escolha a opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    buscarAutorPorNome();
                    break;
                case 3:
                    listarLivrosRegistrados();
                    break;
                case 4:
                    listarAutoresRegistrados();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 6:
                    listarAutoresVivosPorAno();
                    break;
                case 7:
                    listarTop10LivrosBaixados();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Selecione uma opção válida.");
            }
        }
    }

    @Transactional
    private void buscarLivroPeloTitulo() {
        System.out.print("Qual o título do livro? ");
        var tituloLivro = scanner.nextLine();

        if (!tituloLivro.isBlank() && !verificarNumero(tituloLivro)) {

            var json = consumoApi.obterDados(URL_BASE
                    + tituloLivro.replace(" ", "%20"));

            var dados = converteDados.obterDados(json, Dados.class);
            Optional<DadosLivro> buscarLivro = dados.results()
                    .stream()
                    .filter(l -> l.title().toLowerCase().contains(tituloLivro.toLowerCase()))
                    .findFirst();

            if (buscarLivro.isPresent()) {
                DadosLivro dadosLivro = buscarLivro.get();

                if (!verificarLivrosExistentes(dadosLivro)) {
                    Livro livro = new Livro(dadosLivro);
                    DadosAutor dadosAutor = dadosLivro.autor().get(0);
                    Optional<Autor> optionalAutor = autorRepository.findByName(dadosAutor.name());

                    if (optionalAutor.isPresent()) {
                        Autor autorExistente = optionalAutor.get();
                        livro.setAutor(autorExistente);
                        autorExistente.getLivros().add(livro);
                        autorRepository.save(autorExistente);
                    } else {
                        Autor novoAutor = new Autor(dadosAutor);
                        livro.setAutor(novoAutor);
                        novoAutor.getLivros().add(livro);
                        autorRepository.save(novoAutor);
                    }

                    livroRepository.save(livro);
                } else {
                    System.out.println("\nLivro já existente no banco de dados.");
                }
            } else {
                System.out.println("\nLivro não encontrado");
            }
        } else {
            System.out.println("Entrada inválida");
        }
    }

    public void buscarAutorPorNome() {
        System.out.print("Digite o nome do autor: ");
        var nomeAutor = scanner.nextLine();

        if (nomeAutor.isBlank()) {
            System.out.println("O nome digitado pode estar vazio");
            return;
        }

        List <Autor> autor = autorRepository.findByNameContainingIgnoreCase(nomeAutor);

        if (!autor.isEmpty()) {
            autor.forEach(System.out::println);
        } else {
            System.out.println("Autor não encontrado.");
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();

        if (!livros.isEmpty()) {
            livros.forEach(System.out::println);
        } else {
            System.out.println("\nNão há livros registrados.");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (!autores.isEmpty()) {
            autores.forEach(System.out::println);
        } else {
            System.out.println("\nNão autores registrados.");
        }
    }

    private void listarLivrosPorIdioma() {
        var opcaoIdioma = -1;
        String idioma = "";

        var idiomasMenu = """
                \n
                1 - Inglês
                2 - Frânces
                3 - Espanhol
                4 - Português
                """;
        System.out.println(idiomasMenu);
        System.out.print("Selecione o idioma para a consuta:");
        opcaoIdioma = scanner.nextInt();

        switch (opcaoIdioma) {
            case 1:
                idioma = "en";
                break;
            case 2:
                idioma = "fr";
                break;
            case 3:
                idioma = "es";
                break;
            case 4:
                idioma = "pt";
                break;
            default:
                System.out.println("\nOpção inválida");
        }

        List<Livro> livros = livroRepository.findBooksByLanguage(idioma);

        if (!livros.isEmpty()) {
            System.out.println("\nLivros registrados pelo idioma " + idioma + ":");
            livros.forEach(System.out::println);
        } else {
            System.out.println("\nNão há livros registrados com esse idioma.");
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.print("\nDigite o ano: ");

        if (scanner.hasNextInt()) {
            var ano = scanner.nextInt();
            scanner.nextLine();
            List<Autor> autores = autorRepository.findAuthorsAlive(ano);

            if (!autores.isEmpty()) {
                System.out.println("\nAutores vivos no ano de " + ano + ":");
                autores.forEach(System.out::println);
            } else {
                System.out.println("\nNenhum resultado encontrado, digite outro ano.");
            }
        } else {
            System.out.println("\nOpção inválida");
            scanner.nextLine();
        }
    }

    public void listarTop10LivrosBaixados() {
        List<Livro> livroTop = livroRepository.findByTop10ByOrderDownloadCountDesc();

        if (!livroTop.isEmpty()) {
            System.out.println("\n***   Top 10 livros mais baixados   ***");
            final int[] rank = {1};
            livroTop.forEach(l -> {System.out.println("\n   - " + rank[0] + "º -   \n" +
                    "Livro: " + l.getTitle() +
                    "\nAutor: " + l.getAutor().getName() +
                    "\nDownloads: " + l.getDownloadCount() + "\n");
                rank[0]++;
            });
        } else {
            System.out.println("Não há livros mais baixados.");
        }
    }

    private boolean verificarNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verificarLivrosExistentes(DadosLivro dadosLivro) {
        Livro livro = new Livro(dadosLivro);
        return livroRepository.verifiedBDExistence(livro.getTitle());
    }
}
