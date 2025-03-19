package personnages;

public class FabriqueIDPersonnage {
    private static int cpt = 0;
    private static FabriqueIDPersonnage instance = new FabriqueIDPersonnage();

    public static int nextID(){
        cpt ++;
        return cpt;
    }
}
