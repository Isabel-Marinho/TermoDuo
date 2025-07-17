import java.util.Random;
import java.util.Scanner;

public class Codificador extends Minigames {

    private Jogador jogadorAtual;
    private Random random;
    private String fraseOriginal;
    private int shift; // Valor de deslocamento para a cifra
    private String fraseCodificadaCorreta;
    private boolean venceuMinigame;

    private static final String[] FRASES_PADRAO = {"PROGRAMAR", "DESENVOLVER", "ALGORITMO", "INTELIGENCIA", "ADAPTA", "JAVASCRIPT", "PYTHON", "TECNOLOGIA"};


    public Codificador(Jogador jogador, Random random) {
        super("Codificador");
        this.jogadorAtual = jogador;
        this.random = random;
        this.venceuMinigame = false;
    }

    @Override
    public void exibirRegras() {
        System.out.println("\n--- Regras do Minijogo Codificador ---");
        System.out.println("Sua missão é decifrar a frase secreta! Você receberá uma frase para codificar e um 'deslocamento' de letras.");
        System.out.println("Para codificar, você moverá cada letra da frase original pelo número de posições indicado no alfabeto.");
        System.out.println("Exemplo: 'Caio – 2 para trás' se transformaria em 'Aygm'");
        System.out.println("Se o deslocamento for positivo, as letras avançam no alfabeto (ex: A com +1 vira B).");
        System.out.println("Se o deslocamento for negativo, as letras recuam no alfabeto (ex: B com -1 vira A).");
        System.out.println("Você terá uma única tentativa para acertar. Sua recompensa por codificar corretamente será de 75 pontos e 1 letra!");
    }

    private String FraseParaCodificar() {
        if ("Sova".equals(jogadorAtual.personagemEscolhido.getTipoPersonagem())) {
            return jogadorAtual.personagemEscolhido.modificaRegra(); // Chama o modificaRegra da Sova
        } else {
            return FRASES_PADRAO[random.nextInt(FRASES_PADRAO.length)];
        }
    }

    @Override
    public void iniciar() {
        exibirRegras();
        this.fraseOriginal = FraseParaCodificar();

        do {
            shift = random.nextInt(11) - 5; // Gera um número entre -5 e 5
        } while (shift == 0);

        System.out.println("\n--- DESAFIO: Codificador ---");
        System.out.println("Frase para codificar: '" + fraseOriginal + "'");
        System.out.println("Instrução: Desloque as letras por " + Math.abs(shift) + " posições " + (shift < 0 ? "para trás" : "para frente"));

        // Calcula a frase codificada correta para comparação
        fraseCodificadaCorreta = codificarFraseInterno(fraseOriginal, shift);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite sua resposta codificada: ");
        String respostaJogador = "";
        try {
            // Converte para maiúsculas e remove espaços extras
            respostaJogador = scanner.nextLine().toUpperCase().trim();
        } catch (Exception e) {
            System.out.println("Erro na leitura da sua entrada. O minijogo foi encerrado.");
            this.setConcluido(true);
            this.venceuMinigame = false;
            return;
        }

        // Verifica a resposta do jogador
        if (respostaJogador.equals(fraseCodificadaCorreta)) {
            System.out.println("\n*** VITÓRIA! Sua codificação está correta! ***");
            this.setRecompensa(75);
            this.venceuMinigame = true;
        } else {
            System.out.println("\n--- TENTE NOVAMENTE! ---");
            System.out.println("Sua resposta: '" + respostaJogador + "'");
            System.out.println("A resposta correta era: '" + fraseCodificadaCorreta + "'");
            this.setRecompensa(10);
            this.venceuMinigame = false;
        }
        this.setConcluido(true);
    }

    @Override
    public boolean verificarVitoria() {
        return this.venceuMinigame;
    }

    private String codificarFraseInterno(String frase, int shift) {
        StringBuilder resultado = new StringBuilder();
        for (char caractere : frase.toCharArray()) {
            if (Character.isLetter(caractere)) {
                char base = 'A';
                int offset = caractere - base;
                int newOffset = (offset + shift) % 26;
                if (newOffset < 0) {
                    newOffset += 26;
                }
                resultado.append((char) (base + newOffset));
            } else {
                resultado.append(caractere);
            }
        }
        return resultado.toString();
    }
}