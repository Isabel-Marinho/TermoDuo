import java.util.*;

public class Main {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        ArrayList<Jogador> jogadores = jogo.iniciarJogo();

        jogo.definirPalavraSecreta("TERMO"); // Exemplo

        boolean jogoAtivo = true;
        int turno = 0;

        while (jogoAtivo) {
            Jogador jogadorAtual = jogadores.get(turno % 2);
            System.out.println("\n=== TURNO " + (turno + 1) + " - " + jogadorAtual.getNome() + " ===");

            jogo.jogarTurno(jogadorAtual);

            if (jogo.jogoAcabou()) {
                jogoAtivo = false;
            } else {
                turno++;
            }
        }

        // Mostrar resultado final
        jogo.mostrarStatus(jogadores.get(0), jogadores.get(1));
        jogo.pontuacao();

        if (jogo.getVencedor() != null) {
            System.out.println("\nParabéns, " + jogo.getVencedor().getNome() + " venceu!");
        } else {
            System.out.println("\nO jogo terminou em empate!");
        }
    }
}