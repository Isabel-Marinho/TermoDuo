import java.util.*;

public class Decifrador extends Minigames {
    private Jogador jogadorAtual;
    private Random random;
    private HashMap<Integer, Integer> codigoSecretoMap;
    private int tamanhoCodigo;
    private Scanner scanner;
    private long tempoLimite; // Em milissegundos
    private char letraEscolhida;
    private boolean venceuMinigame;

    public Decifrador(Jogador jogador, Random random, char letra) {
        super("Decifrador");
        this.jogadorAtual = jogador;
        this.random = random;
        this.letraEscolhida = Character.toUpperCase(letra);
        this.scanner = new Scanner(System.in);
        this.tempoLimite = 120000; // 120 segundos em milissegundos
        this.venceuMinigame = false;

        determinarDificuldade();
    }

    public char getLetraAlvo() {
        return letraEscolhida;
    }

    private void determinarDificuldade() {
        if ("AEIOU".indexOf(letraEscolhida) != -1) {
            // Vogais tem 6 dígitos.
            tamanhoCodigo = 6;
            System.out.println("Dificuldade: VOGAL ('" + letraEscolhida + "') - Código de " + tamanhoCodigo + " dígitos.");
        } else{
            // Consoantes tem 5 dígitos.
            tamanhoCodigo = 5;
            System.out.println("Dificuldade: CONSOANTE ('" + letraEscolhida + "') - Código de " + tamanhoCodigo + " dígitos.");
        }
        gerarCodigoSecreto();
    }

    private void gerarCodigoSecreto() {
        codigoSecretoMap = new HashMap<>();
        for (int i = 0; i < tamanhoCodigo; i++) {
            codigoSecretoMap.put(i, random.nextInt(10));
        }
    }

    @Override
    public void exibirRegras() {
        System.out.println("\n=== REGRAS DO DECIFRADOR ===");
        System.out.println("- Descubra o código numérico correto para desbloquear a letra.");
        System.out.println("- A dificuldade varia: Vogais (6 dígitos), Consoantes (5 dígitos)");
        System.out.println("- Você tem " + (tempoLimite / 1000) + " segundos para acertar o código.");
        System.out.println("- A cada tentativa, será informado quantos dígitos estão corretos e na posição correta.");
        System.out.println("Sua recompensa por acertar será de 75 pontos e o desbloqueio da letra! (10 pontos se o tempo esgotar).");
    }

    @Override
    public void iniciar() {
        exibirRegras();
        System.out.println("Pressione enter para começar o jogo:");
        scanner.nextLine();
        System.out.println("\nIniciando o Decifrador...");

        long tempoInicio = System.currentTimeMillis();

        if ("Beatrix".equals(jogadorAtual.personagemEscolhido.getTipoPersonagem())) {
            Beatrix beatrix = (Beatrix) jogadorAtual.personagemEscolhido;
            beatrix.modificaRegra(this.codigoSecretoMap, this.tamanhoCodigo);
        }

        while (System.currentTimeMillis() - tempoInicio < tempoLimite) {
            long tempoDecorrido = (System.currentTimeMillis() - tempoInicio) / 1000;
            System.out.print("\nTempo restante: " + (tempoLimite / 1000 - tempoDecorrido) + "s. Digite o código (" + tamanhoCodigo + " dígitos): ");
            String entrada = scanner.nextLine().trim();

            if (entrada.length() != tamanhoCodigo) {
                System.out.println("ERRO: Digite exatamente " + tamanhoCodigo + " dígitos!");
                continue;
            }

            try {
                int digitosCorretos = 0;
                for (int i = 0; i < tamanhoCodigo; i++) {
                    if (Character.getNumericValue(entrada.charAt(i)) == codigoSecretoMap.get(i)) {
                        digitosCorretos++;
                    }
                }

                if (digitosCorretos == tamanhoCodigo) {
                    this.venceuMinigame = true;
                    this.setConcluido(true);
                    this.setRecompensa(75);
                    System.out.println("\nCódigo correto! Letra '" + letraEscolhida + "' desbloqueada!");
                    return;
                } else {
                    System.out.println("Dígitos corretos na posição correta: " + digitosCorretos);
                }
            } catch (NumberFormatException e) {
                System.out.println("ERRO: Digite apenas números!");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado na entrada. Tente novamente.");
            }
        }

        // Se o tempo esgotou
        System.out.println("\n⏰ Tempo esgotado!");
        String codigoParaExibir = "[";
        for (int i = 0; i < tamanhoCodigo; i++) {
            codigoParaExibir += codigoSecretoMap.get(i);
        }
        codigoParaExibir += "]"; // Fecha o colchete

        System.out.println("O código era: " + codigoParaExibir);
        this.venceuMinigame = false;
        this.setConcluido(true);
        this.setRecompensa(10);
    }

    @Override
    public boolean verificarVitoria() {
        return this.venceuMinigame;
    }
}