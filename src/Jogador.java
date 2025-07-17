import java.util.*;

public class Jogador {
    protected String nome;
    protected int pontuacao;
    protected Personagem personagemEscolhido;
    protected ArrayList<Character> letrasAcertadas;
    protected int tentativasRestantes;
    protected static final int MAX_TENTATIVAS = 6;
    protected Random random;

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
        this.tentativasRestantes = MAX_TENTATIVAS;
        this.random = new Random();
    }

    public void resetTentativas() {
        this.tentativasRestantes = MAX_TENTATIVAS;
    }

    public boolean temTentativas() {
        return tentativasRestantes > 0;
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
            // Verifica se o personagem existe e está disponível.
            if (!personagensDisponiveis.containsKey(idPersonagem) || !personagensDisponiveis.get(idPersonagem)) {
                if (!personagensDisponiveis.containsKey(idPersonagem)) {
                    throw new IllegalArgumentException("Personagem com ID " + idPersonagem + " não existe!");
                } else {
                    throw new IllegalArgumentException("Personagem já foi escolhido por outro jogador!");
                }
            }

            // Instancia o personagem com base no ID.
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

            // Marca o personagem como indisponível após a escolha.
            personagensDisponiveis.put(idPersonagem, false);
            System.out.println(this.nome + " escolheu " + this.personagemEscolhido.getNomePersonagem() + "!");
            this.personagemEscolhido.contarHistoria(); // Exibe a história do personagem.
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public Minigames pensarEstrategia(Scanner scanner, Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta) {
        boolean todasLetrasDesbloqueadas = true;
        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadasGlobal.contains(c)) {
                todasLetrasDesbloqueadas = false;
                break;
            }
        }

        if (todasLetrasDesbloqueadas) {
            System.out.println("Parabéns! Você já desbloqueou todas as letras da palavra secreta!");
            System.out.println("Não há mais letras para desbloquear através dos minijogos Codificador ou Decifrador.");
            return null;
        }

        System.out.println("\nEscolha o minijogo para tentar desbloquear mais letras ou ganhar pontos:");
        System.out.println("1 - Codificador (Foca em uma letra específica da palavra secreta)");
        System.out.println("2 - Decifrador (Também foca em uma letra específica da palavra secreta)");
        System.out.println("3 - Roleta Especial (Chance de ganhar pontos ou bônus, independente das letras)");
        System.out.print("Opção: ");

        int opcaoMinijogo;
        try {
            opcaoMinijogo = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número inteiro entre 1 e 3.");
            scanner.nextLine();
            return null;
        }

        switch (opcaoMinijogo) {
            case 1:
                char letraCod = escolherLetraParaDesbloquear(letrasDesbloqueadasGlobal, palavraSecreta);
                if (letraCod == '\0') {
                    System.out.println("Você já desbloqueou todas as letras da palavra principal");
                    return null;
                }
                Codificador codificador = new Codificador(this, random);
                codificador.setLetraAlvo(letraCod); // Define a letra que o Codificador tentará desbloquear.
                return codificador;
            case 2:
                char letraDec = escolherLetraParaDecifrar(letrasDesbloqueadasGlobal, palavraSecreta);
                if (letraDec == '\0') {
                    System.out.println("Você já desbloqueou todas as letras da palavra principal");
                    return null;
                }
                return new Decifrador(this, random, letraDec);
            case 3:
                return new RoletaEspecial(this, random);
            default:
                System.out.println("Opção inválida! Por favor, escolha um número entre 1 e 3.");
                return null; // Retorna null para uma opção de minijogo inválida.
        }
    }

    private char escolherLetraParaDesbloquear(Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta) {
        List<Character> letrasDisponiveis = new ArrayList<>();

        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadasGlobal.contains(c)) {
                letrasDisponiveis.add(c);
            }
        }

        if (letrasDisponiveis.isEmpty()) {
            System.out.println("Todas as letras da palavra secreta já foram desbloqueadas para este tipo de minijogo!");
            return '\0';
        }

        return letrasDisponiveis.get(random.nextInt(letrasDisponiveis.size()));
    }

    private char escolherLetraParaDecifrar(Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta) {
        return escolherLetraParaDesbloquear(letrasDesbloqueadasGlobal, palavraSecreta);
    }


    public void aplicarEstrategia(Minigames minigame, Set<Character> letrasDesbloqueadasGlobal) {
        if (minigame != null) {
            if (minigame.verificarVitoria()) {
                this.adicionarPontos(minigame.getRecompensa());
                if (minigame instanceof Codificador) {
                    char letraGanha = ((Codificador)minigame).getLetraAlvo();
                    letrasDesbloqueadasGlobal.add(letraGanha);
                    this.letrasAcertadas.add(letraGanha);
                    System.out.println("✅ " + this.nome + " ganhou " + minigame.getRecompensa() +
                            " pontos e desbloqueou a letra '" + letraGanha + "'!");
                }
                else if (minigame instanceof Decifrador) {
                    char letraGanha = ((Decifrador)minigame).getLetraAlvo();
                    letrasDesbloqueadasGlobal.add(letraGanha);
                    this.letrasAcertadas.add(letraGanha);
                    System.out.println("✅ " + this.nome + " ganhou " + minigame.getRecompensa() +
                            " pontos e desbloqueou a letra '" + letraGanha + "'!");
                } else {
                    // Para minigames que não desbloqueiam letras
                    System.out.println("✅ " + this.nome + " ganhou " + minigame.getRecompensa() + " pontos!");
                }
            } else {
                System.out.println("⚠️ " + this.nome + " não conseguiu completar o minijogo!");
            }
            System.out.println("Pontuação atual: " + this.pontuacao);
        }
    }

    public void reduzirTentativa() {
        if (this.tentativasRestantes > 0) {
            this.tentativasRestantes--;
        }
    }

    public String getLetrasDesbloqueadasFormatadas() {
        if (letrasAcertadas.isEmpty()) {
            return "Nenhuma letra desbloqueada";
        }
        return letrasAcertadas.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", "");
    }

    public int getTentativasRestantes() {
        return this.tentativasRestantes;
    }

    public ArrayList<Character> getLetrasAcertadas() {
        return letrasAcertadas;
    }

    public void acertarLetra(char letra) {
        letrasAcertadas.add(letra);
        System.out.println("🎉 " + this.nome + " desbloqueou a letra '" + letra + "'!");
    }

    public void removerLetra(char letra) {
        if (letrasAcertadas.remove(Character.valueOf(letra))) { // Usa Character.valueOf para remover o objeto correto.
            System.out.println("❌ " + this.nome + " perdeu a letra '" + letra + "'!");
        } else {
            System.out.println(this.nome + " não tinha a letra '" + letra + "' para perder.");
        }
    }
}