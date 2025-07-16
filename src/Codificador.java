

public class Codificador extends Minigames {
    private String fraseOriginal;
    private int deslocamento;

    public Codificador(String frase, int deslocamento) {
        super("Codificador");
        this.fraseOriginal = frase;
        this.deslocamento = deslocamento;
    }

    @Override
    public void iniciar() {
        exibirRegras();
        String codificada = codificar(fraseOriginal, deslocamento);
        System.out.println("Frase codificada: " + codificada);
        concluido = true;
        recompensa = 1;
    }

    private String codificar(String texto, int deslocamento) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                resultado.append((char)((c - base + deslocamento + 26) % 26 + base));
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }


    @Override
    public void exibirRegras() {
        System.out.println("\nREGRAS DO CODIFICADOR");
        System.out.println("Codifique a frase usando Cifra de César");
        System.out.println("Desloque cada letra do alfabeto conforme indicado");
        System.out.println("Exemplo: A com deslocamento 1 = B");
        System.out.println("Digite a frase codificada corretamente");
    }

  /*  @Override
    public void iniciar() {
         exibirRegras();
        System.out.println("\nIniciando o Codificador...");
        System.out.println("Frase original: " + fraseOriginal);
        System.out.println("Deslocamento: " + deslocamento + " letras para frente");

        if (jogador.getPersonagem() instanceof Beatrix) {
            System.out.println("🎯 Vantagem da Beatrix: Primeiras 2 letras reveladas!");
            if (fraseCodificada.length() >= 2) {
                System.out.println("Dica: " + fraseCodificada.substring(0, 2) + "...");
            }
        }

        System.out.print("Digite a frase codificada: ");
        String resposta = scanner.nextLine().toUpperCase();

        if (resposta.equals(fraseCodificada)) {
            concluido = true;
            recompensa = 12;
            System.out.println("✅ Codificação correta! Excelente!");
        } else {
            concluido = false;
            recompensa = 0;
            System.out.println("❌ Incorreto! A resposta era: " + fraseCodificada);
        }
    } */

    @Override
    public boolean verificarVitoria() {
        return concluido;
    }
}