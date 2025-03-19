package outils;

public class FabriqueIdentifiant {
    private static FabriqueIdentifiant instance =  new FabriqueIdentifiant();
    private static int idPersonnage = 0;
    private static int idCase = 0;
    public static FabriqueIdentifiant getInstance(){
        return instance;
    }
    public static int nextIdPersonnage(){
        return idPersonnage++;
    }
    public static int nextIdCase() {return idCase ++;}
    public static void resetIdPersonnage(){
        idPersonnage = 0;
    }
}
