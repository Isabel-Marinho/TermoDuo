import java.util.*;

public class RoletaEspecial {
    public static String girarRoleta() {
        int chance = new Random().nextInt(100);
        if (chance < 30) return "GANHA_LETRA";
        else if (chance < 45) return "TIRA_LETRA";
        else if (chance < 95) return "PERDE_VEZ";
        else return "TIRA_DUAS_LETRAS";
    }
}