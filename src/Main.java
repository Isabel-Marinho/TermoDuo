import java.util.*;

public class Main {
    public static void main(String[] args) {

        Jogo jogo = new Jogo();
        ArrayList<String> palavrasSecretas = new ArrayList<>(Arrays.asList(
                "SOL", "LUZ", "PAO", "MAR", "REDE", "FLOR", "GATO", "CASA", "RIO", "PÃO", "ZEBRA", "FADA", "TREM"
        ));

        String palavraSecreta = Jogo.selecionarPalavraAleatoria(palavrasSecretas);
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
