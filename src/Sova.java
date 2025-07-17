import java.util.Random;
class Sova extends Personagem {
    private Random random;
    private static final String[] FRASES_SOVA = {"JAVA", "CODE", "BUG", "TESTE", "IDE"}; // Frases menores pro Sova

    public Sova(String nomeJogador) {
        super(nomeJogador, "Sova", "Russo, usa um arco", 30, "Tem frases menores para codificar", "Sova");
        this.random = new Random();
    }

    @Override
    public String modificaRegra(){
        System.out.println(nomePersonagem + ": Sua frase para codificar é menor!");
        return FRASES_SOVA[random.nextInt(FRASES_SOVA.length)];
    }
}