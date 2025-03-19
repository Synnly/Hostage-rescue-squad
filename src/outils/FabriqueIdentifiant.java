package outils;

public class FabriqueIdentifiant {
    private static FabriqueIdentifiant instance =  new FabriqueIdentifiant();
    private int identifiant = 0;
    public static FabriqueIdentifiant getInstance(){
        return instance;
    }
    public int getId(){
        return identifiant++;
    }
    public void resetId(){
        identifiant = 0;
    }
}
