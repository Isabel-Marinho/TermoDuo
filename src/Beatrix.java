import java.util.*;
class Beatrix extends Personagem {
    public Beatrix(String nomeJogador) {
        super(nomeJogador, "Beatrix", "Inteligente, baixinha e irritada", 20, "Acerta dois números do minigame de codificação assim que começa o minijogo.", "Beatrix");
    }

    public void modificaRegra(HashMap codigoSecreto, int tamanhoCodigo){
        int penultimoDigito = (Integer) codigoSecreto.get(tamanhoCodigo - 2); // Penúltima posição (índice base 0)
        int ultimoDigito = (Integer) codigoSecreto.get(tamanhoCodigo - 1); // Última posição (índice base 0)

        System.out.println("  > O dígito na posição " + (tamanhoCodigo - 1) + " (penúltima) é: " + penultimoDigito);
        System.out.println("  > O dígito na posição " + (tamanhoCodigo) + " (última) é: " + ultimoDigito);
    }

}