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
    private static final String[] FRASES_SOVA = {"JAVA", "CODE", "BUG", "TESTE", "IDE"}; // Frases menores pro Sova


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
        if (jogadorAtual.personagemEscolhido.equals("Sova")) {
            // A vantagem da Sova é aplicada aqui: uma frase mais curta é selecionada.
            // O método aplicarVantagem() da Sova apenas descreve essa vantagem.
            System.out.println("\nEfeito da vantagem de " + jogadorAtual.personagemEscolhido.getNomePersonagem() + " (Sova): Você receberá uma frase menor para codificar!");
            // Sova também pode 'aplicarVantagem()' aqui se houver alguma lógica de estado na Sova
            ((Sova)jogadorAtual.personagemEscolhido).aplicarVantagem(); // Chama o método para descrever a vantagem
            return FRASES_SOVA[random.nextInt(FRASES_SOVA.length)];
        } else {
            return FRASES_PADRAO[random.nextInt(FRASES_PADRAO.length)];
        }
    }

    @Override
    public void iniciar() {
        exibirRegras();

        if (jogadorAtual.personagemEscolhido.equals("Sova")) {
            fraseOriginal = FRASES_SOVA[random.nextInt(FRASES_SOVA.length)];
            System.out.println("\n" + jogadorAtual.personagemEscolhido.getNomePersonagem() + " (Sova), sua vantagem de ter frases menores para codificar está ativada!");
        } else {
            fraseOriginal = FRASES_PADRAO[random.nextInt(FRASES_PADRAO.length)];
        }

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
            // Verifica se o caractere é uma letra do alfabeto
            if (Character.isLetter(caractere)) {
                char base = 'A'; // A base para a codificação (assumindo letras maiúsculas)
                int offset = caractere - base; // Posição numérica da letra (0 para A, 1 para B, etc.)

                // Aplica o deslocamento. O % 26 garante que o resultado fique dentro do alfabeto (0-25).
                int newOffset = (offset + shift) % 26;

                // Correção para resultados negativos do operador módulo em Java.
                // Ex: (-2 % 26) em Java é -2. Queremos que seja 24 (Z-B = A).
                if (newOffset <