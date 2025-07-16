import java.util.*;

interface InterfaceJogo {
    ArrayList<Jogador> iniciarJogo();
    void jogarTurno(Jogador jogador);
    boolean jogoAcabou();

    static int criarNumeroAleatorio(int limite) {
        Random random = new Random();
        return random.nextInt(1, 100);
    }
}
