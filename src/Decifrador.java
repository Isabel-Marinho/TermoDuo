import java.util.*;

public class Decifrador extends Minigames {
    private int[] codigoSecreto;
    private int tamanhoCodigo;
    private Scanner scanner;
    private long tempoLimite;
    private char letraEscolhida;

    public Decifrador(char letra) {
        super("Decifrador");
        this.letraEscolhida = letra;
        this.scanner = new Scanner(System.in);
        this.tempoLimite = 30000; // 30 segundos
        determinarDificuldade();
    }

    private void determinarDificuldade() {
        if ("AEIOU".indexOf(letraEscolhida) != -1) {
            tamanhoCodigo = 6;
        } else if ("BCDFGHJKLMNPQRSTVWXYZ".indexOf(letraEscolhida) != -1) {
            tamanhoCodigo = 5;
        } else {
            tamanhoCodigo = 4;
        }

        gerarCodigoSecreto();
    }

    private void gerarCodigoSecreto() {
        codigoSecreto = new int[tamanhoCodigo];
        for (int i = 0; i < tamanhoCodigo; i++) {
            codigoSecreto[i] = InterfaceJogo.criarNumeroAleatorio(10);
        }
    }

    @Override
    public void exibirRegras() {
        System.out.println("\n=== REGRAS DO DECIFRADOR ===");
        System.out.println("- Descubra o código numérico correto");
        System.out.println("- Vogais: 6 dígitos | Consoantes especiais: 5 dígitos | Outras: 4 dígitos");
        System.out.println("- Você tem 30 segundos para acertar");
        System.out.println("- A cada tentativa, será informado quantos dígitos estão corretos");
        System.out.println("- Letra escolhida: " + letraEscolhida + " (Código de " + tamanhoCodigo + " dígitos)");
    }

    @Override
    public void iniciar() {
        exibirRegras();
        System.out.println("\nIniciando o Decifrador...");

        long tempoInicio = System.currentTimeMillis();

        while (System.currentTimeMillis() - tempoInicio < tempoLimite) {
            System.out.print("Digite o código (" + tamanhoCodigo + " dígitos): ");
            String entrada = scanner.nextLine();

            if (entrada.length() != tamanhoCodigo) {
                System.out.println("Digite exatamente " + tamanhoCodigo + " dígitos!");
                continue;
            }

            try {
                int digitosCorretos = 0;
                for (int i = 0; i < tamanhoCodigo; i++) {
                    if (Character.getNumericValue(entrada.charAt(i)) == codigoSecreto[i]) {
                        digitosCorretos++;
                    }
                }

                if (digitosCorretos == tamanhoCodigo) {
                    concluido = true;
                    recompensa = 15;
                    System.out.println("🎉 Código correto! Parabéns!");
                    return;
                } else {
                    System.out.println("Dígitos corretos: " + digitosCorretos);
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números!");
            }
        }

        System.out.println("⏰ Tempo esgotado!");
        System.out.println("O código era: " + Arrays.toString(codigoSecreto));
        concluido = false;
        recompensa = 0;
    }

    @Override
    public boolean verificarVitoria() {
        return concluido;
    }
}