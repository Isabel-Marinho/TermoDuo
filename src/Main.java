import java.util.*;

public class Main {
    public static void main(String[] args) {

        Jogo jogo = new Jogo();
        String palavraSecreta = "TERMO";
        jogo.definirPalavraSecreta(palavraSecreta);
        ArrayList<Jogador> jogadores = jogo.iniciarJogo();
        while (!jogo.jogoAcabou()) {
            for (Jogador j : jogadores) {
                jogo.jogarTurno(j);
            }
        }
        jogo.pontuacao();
        }
}
