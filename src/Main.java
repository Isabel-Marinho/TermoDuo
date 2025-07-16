import java.util.*;

public class Main {
    public static void main(String[] args) {

        Jogo jogo = new Jogo();
        ArrayList<Jogador> jogadores = jogo.iniciarJogo();
        while (!jogo.jogoAcabou()) {
            for (Jogador j : jogadores) {
                jogo.jogarTurno(j);
            }
        }
        jogo.pontuacao();
        }
}
