import java.util.*;

public class Jogador {
    protected String nome;
    protected int pontuacao;
    protected Personagem personagemEscolhido;
    protected ArrayList<Character> letrasAcertadas; // Para o jogo principal

    // Para controlar personagens já escolhidos
    private static Map<Integer, Boolean> personagensDisponiveis = new HashMap<>();

    static {
        personagensDisponiveis.put(1, true); // Sova
        personagensDisponiveis.put(2, true); // Beatrix
        personagensDisponiveis.put(3, true); // Lucky
    }

    public Jogador(String nome) {
        this.nome = nome;
        this.pontuacao = 0;
        this.letrasAcertadas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }

    public Personagem getPersonagemEscolhido() {
        return personagemEscolhido;
    }

    public boolean setPersonagem(int idPersonagem) {
        try {
            if (!personagensDisponiveis.containsKey(idPersonagem) || !personagensDisponiveis.get(idPersonagem)) {
                if (!personagensDisponiveis.containsKey(idPersonagem)) {
                    throw new IllegalArgumentException("Personagem com ID " + idPersonagem + " não existe!");
                } else {
                    throw new IllegalArgumentException("Personagem já foi escolhido por outro jogador!");
                }
            }

            switch (idPersonagem) {
                case 1:
                    this.personagemEscolhido = new Sova(this.nome);
                    break;
                case 2:
                    this.personagemEscolhido = new Beatrix(this.nome);
                    break;
                case 3:
                    this.personagemEscolhido = new Lucky(this.nome);
                    break;
                default:
                    throw new IllegalArgumentException("ID de personagem desconhecido.");
            }

            personagensDisponiveis.put(idPersonagem, false);
            System.out.println(this.nome + " escolheu " + this.personagemEscolhido.getNomePersonagem() + "!");
            this.personagemEscolhido.contarHistoria();
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public Minigames pensarEstrategia(Scanner scanner, Random random, Set<Character> letrasDesbloqueadasGlobal) {
        System.out.println("\n--- Escolha um minijogo para jogar neste turno ---");
        System.out.println("Digite (1) para escolher a Roleta Especial!");
        System.out.println("Digite (2) para escolher o Codificador!");
        System.out.println("Digite (3) para escolher o Decifrador!");
        System.out.print("Sua escolha: ");

        int minijogoEscolhido = -1;
        try {
            minijogoEscolhido = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine(); // Consome a entrada inválida
            return null;
        }

        Minigames minigameAtual = null;
        char letraParaDecifrador = ' ';

        switch (minijogoEscolhido) {
            case 1: // Roleta Especial
                minigameAtual = new RoletaEspecial(this, random);
                break;
            case 2: // Codificador
                minigameAtual = new Codificador(this, random);
                break;
            case 3: // Decifrador
                Set<Character> todasAsLetrasBloqueadas = new TreeSet<>();
                for (char c = 'A'; c <= 'Z'; c++) {
                    if (!letrasDesbloqueadasGlobal.contains(c)) {
                        todasAsLetrasBloqueadas.add(c);
                    }
                }
                boolean letraValida = false;
                while (!letraValida) {
                    System.out.println("\n--- Jogo Decifrador ---");
                    System.out.println("Letras bloqueadas disponíveis para tentar desbloquear: " + todasAsLetrasBloqueadas.toString());
                    System.out.print("Qual letra você quer tentar desbloquear (A-Z)? ");
                    String Letra = scanner.nextLine().toUpperCase().trim();

                    if (Letra.length() == 1 && Character.isLetter(Letra.charAt(0))) {
                        char c = Letra.charAt(0);
                        if (todasAsLetrasBloqueadas.contains(c)) {
                            letraParaDecifrador = c;
                            letraValida = true;
                        } else if (letrasDesbloqueadasGlobal.contains(c)) {
                            System.out.println("Erro: A letra '" + c + "' já está desbloqueada globalmente. Por favor, escolha uma letra bloqueada.");
                        } else {
                            System.out.println("Erro: A letra '" + c + "' não está na lista de letras bloqueadas disponíveis. Por favor, escolha uma das letras listadas.");
                        }
                    } else {
                        System.out.println("Entrada inválida. Digite apenas uma letra (A-Z).");
                    }
                }
                // Instancia o Decifrador com a letra escolhida pelo jogador
                minigameAtual = new Decifrador(this, random, letraParaDecifrador);
                break;
            default:
                System.out.println("Opção inválida! Por favor, escolha 1, 2 ou 3.");
                return null;
        }

        if (minigameAtual != null) {
            minigameAtual.iniciar();
        }
        return minigameAtual;
    }


    public void aplicarEstrategia(Minigames minigame) {
        if (minigame != null) {
            this.adicionarPontos(minigame.getRecompensa());
            System.out.println(">> " + this.getNome() + " ganhou " + minigame.getRecompensa() + " pontos neste minijogo.");
            System.out.println(">> Pontuação total de " + this.getNome() + ": " + this.getPontuacao());
        }
    }

    public ArrayList<Character> getLetrasAcertadas() {
        return letrasAcertadas;
    }

    public void acertarLetra(char letra) {
        letrasAcertadas.add(Character.valueOf(letra));
        System.out.println(this.nome + " desbloqueou uma letra!");
        }

    public void removerLetra(char letra) {
        if (letrasAcertadas.remove(Character.valueOf(letra))) {
            System.out.println(this.nome + " perdeu a letra '" + letra + "'!");
        } else {
            System.out.println(this.nome + " não tinha a letra '" + letra + "' para perder.");
        }
    }
}