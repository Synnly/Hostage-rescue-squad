package testObservable;

import carte.Case;
import carte.Objectif;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEnvironnement {
    private Environnement env = null;
    private Operateur op = null;

    @Before
    public void setup(){
        env = new Environnement(10, 10, 1, 1);
        op = env.getOperateurActif();
    }

    @After
    public void teardown(){
        env = null;
    }

    // ==================== Constructeur ====================

    @Test
    @DisplayName("Creer un environnement avec une taille correcte fonctionne")
    public void testCreerEnvironnementAvecTailleCorrecteFonctionne(){
        env = new Environnement(10, 10, 1, 1);
        assertNotNull(env);
        assertEquals(10, env.getLargeur());
        assertEquals(10, env.getHauteur());
    }

    @Test
    @DisplayName("Creer un environnement avec une largeur à 1 fonctionne")
    public void testCreerEnvironnementAvecLargeurAUnFonctionne(){
        env = new Environnement(1, 10, 1, 1);
        assertNotNull(env);
        assertEquals(1, env.getLargeur());
        assertEquals(10, env.getHauteur());
    }

    @Test
    @DisplayName("Creer un environnement avec une largeur à 0 lance une erreur")
    public void testCreerEnvironnementAvecLargeurAZeroLanceUneErreur(){
        assertThrows(AssertionError.class, () -> new Environnement(0, 10, 1, 1));
    }

    @Test
    @DisplayName("Creer un environnement avec une largeur à -1 lance une erreur")
    public void testCreerEnvironnementAvecLargeurAMoinsUnLanceUneErreur(){
        assertThrows(AssertionError.class, () -> new Environnement(-1, 10, 1, 1));
    }

    @Test
    @DisplayName("Creer un environnement avec une hauteur à 1 fonctionne")
    public void testCreerEnvironnementAvecHauteurAUnFonctionne(){
        env = new Environnement(10, 1, 1, 1);
        assertNotNull(env);
        assertEquals(10, env.getLargeur());
        assertEquals(1, env.getHauteur());
    }

    @Test
    @DisplayName("Creer un environnement avec une hauteur à 0 lance une erreur")
    public void testCreerEnvironnementAvecHauteurAZeroLanceUneErreur(){
        assertThrows(AssertionError.class, () -> new Environnement(10, 0, 1, 1));
    }

    @Test
    @DisplayName("Creer un environnement avec une hauteur à -1 lance une erreur")
    public void testCreerEnvironnementAvecHauteurAMoinsUnLanceUneErreur(){
        assertThrows(AssertionError.class, () -> new Environnement(10, -1, 1, 1));
    }

    @Test
    @DisplayName("Creer un environnement avec une proba deplacement à 0 fonctionne")
    public void testCreerEnvironnementAvecProbaSuccessDeplacementAZeroFonctionne(){
        env = new Environnement(10, 10, 0, 1);
        assertNotNull(env);
        assertEquals(10, env.getLargeur());
        assertEquals(10, env.getHauteur());
        assertEquals(0, env.getOperateurActif().getDeplacement().probaSucces);
    }

    @Test
    @DisplayName("Creer un environnement avec une proba deplacement à -1 lance une erreur")
    public void testCreerEnvironnementAvecProbaSuccessDeplacementAMoinsUnFonctionne(){
        assertThrows(AssertionError.class, () -> new Environnement(10, 10, -1, 1));
    }

    @Test
    @DisplayName("Creer un environnement avec une proba tir à 0 fonctionne")
    public void testCreerEnvironnementAvecProbaSuccessTirAZeroFonctionne(){
        env = new Environnement(10, 10, 1, 0);
        assertNotNull(env);
        assertEquals(10, env.getLargeur());
        assertEquals(10, env.getHauteur());
        assertEquals(0, env.getOperateurActif().getTir().probaSucces);
    }

    @Test
    @DisplayName("Creer un environnement avec une proba tir à -1 lance une erreur")
    public void testCreerEnvironnementAvecProbaSuccessTirAMoinsUnFonctionne(){
        assertThrows(AssertionError.class, () -> new Environnement(10, 10, 1, -1));
    }


    // ==================== getCase ====================

    @Test
    @DisplayName("Recuperer case d'indice correct fonctionne")
    public void testRecupererCaseAvecIndicesCorrectsFonctionne(){
        env = new Environnement(10, 10, 1, 1);
        Case c = env.getCase(5, 5);
        assertNotNull(c);
        assertEquals(5, c.x);
        assertEquals(5, c.y);
    }

    @Test
    @DisplayName("Recuperer case d'indice x à 0 fonctionne")
    public void testRecupererCaseAvecIndiceXAZeroFonctionne(){
        env = new Environnement(10, 10, 1, 1);
        Case c = env.getCase(0, 5);
        assertNotNull(c);
        assertEquals(0, c.x);
        assertEquals(5, c.y);
    }

    @Test
    @DisplayName("Recuperer case d'indice x > largeur lance une erreur")
    public void testRecupererCaseAvecIndiceXSuperieurALargeurLanceUneErreur(){
        env = new Environnement(10, 10, 1, 1);
        assertThrows(AssertionError.class, () -> env.getCase(12, 5));
    }

    @Test
    @DisplayName("Recuperer case d'indice x = largeur lance une erreur")
    public void testRecupererCaseAvecIndiceXEgalALargeurLanceUneErreur(){
        env = new Environnement(10, 10, 1, 1);
        assertThrows(AssertionError.class, () -> env.getCase(10, 5));
    }

    @Test
    @DisplayName("Recuperer case d'indice y à 0 fonctionne")
    public void testRecupererCaseAvecIndiceYAZeroFonctionne(){
        env = new Environnement(10, 10, 1, 1);
        Case c = env.getCase(5, 0);
        assertNotNull(c);
        assertEquals(5, c.x);
        assertEquals(0, c.y);
    }

    @Test
    @DisplayName("Recuperer case d'indice y > hauteur lance une erreur")
    public void testRecupererCaseAvecIndiceXSuperieurAHauteurLanceUneErreur(){
        env = new Environnement(10, 10, 1, 1);
        assertThrows(AssertionError.class, () -> env.getCase(5, 12));
    }

    @Test
    @DisplayName("Recuperer case d'indice y = hauteur lance une erreur")
    public void testRecupererCaseAvecIndiceXEgalAHauteurLanceUneErreur(){
        env = new Environnement(10, 10, 1, 1);
        assertThrows(AssertionError.class, () -> env.getCase(5, 10));
    }

    // ==================== initPlateau ====================

    @Test
    @DisplayName("Recréer le plateau de taille correcte fonctionne")
    public void testRecreerPlateauDeTailleCorrecteFonctionne(){
        List<Case> plateau = new ArrayList<>(env.getPlateau());
        env.initPlateau(8,8);
        for (Case c:plateau) {
            assertFalse(env.getPlateau().contains(c));
        }
    }

    @Test
    @DisplayName("Recréer le plateau de largeur = 1 fonctionne")
    public void testRecreerPlateauDeLargeurAUnFonctionne(){
        List<Case> plateau = new ArrayList<>(env.getPlateau());
        env.initPlateau(1, 8);
        for (Case c:plateau) {
            assertFalse(env.getPlateau().contains(c));
        }
    }

    @Test
    @DisplayName("Recréer le plateau de largeur = 0 lance une erreur")
    public void testRecreerPlateauDeLargeurAZeroLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.initPlateau(0, 8));
    }

    @Test
    @DisplayName("Recréer le plateau de largeur = -1 lance une erreur")
    public void testRecreerPlateauDeLargeurAMoinsUnLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.initPlateau(-1, 8));
    }

    @Test
    @DisplayName("Recréer le plateau de hauteur = 1 fonctionne")
    public void testRecreerPlateauDeHauteurAUnFonctionne(){
        List<Case> plateau = new ArrayList<>(env.getPlateau());
        env.initPlateau(8, 1);
        for (Case c:plateau) {
            assertFalse(env.getPlateau().contains(c));
        }
    }

    @Test
    @DisplayName("Recréer le plateau de hauteur = 0 lance une erreur")
    public void testRecreerPlateauDeHauteurAZeroLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.initPlateau(8, 0));
    }

    @Test
    @DisplayName("Recréer le plateau de hauteur = -1 lance une erreur")
    public void testRecreerPlateauDeHauteurAMoinsUnLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.initPlateau(8, -1));
    }

    // ==================== tourEnnemi ====================


    // ==================== choisirCase ====================

    @Test
    @DisplayName("Choisir une case valide quand le deplacement est actif fonctionne")
    public void testChoisirCaseValideQuandDeplacementActifFonctionne(){
        op.setX(5); op.setY(5);
        env.choisirCase(5, 6);
        assertEquals(5, op.getX());
        assertEquals(6, op.getY());
    }

    @Test
    @DisplayName("Choisir une case de coordonnée x = 1 quand le deplacement est actif fonctionne")
    public void testChoisirCaseDeCoordonneeXEgaleAUnQuandDeplacementActifFonctionne(){
        op.setX(2); op.setY(5);
        env.choisirCase(1, 5);
        assertEquals(1, op.getX());
        assertEquals(5, op.getY());
    }

    @Test
    @DisplayName("Choisir une case de coordonnée x = 0 quand le deplacement est actif fonctionne")
    public void testChoisirCaseDeCoordonneeXEgaleAZeroQuandDeplacementActifFonctionne(){


        op.setX(1); op.setY(5);
        int nbPa = op.getPointsAction();
        assertEquals(nbPa, op.getPointsAction());

        env.choisirCase(0, 5);
        assertEquals(0, op.getX());
        assertEquals(5, op.getY());
        assertEquals(nbPa - op.getDeplacement().cout, op.getPointsAction());
    }

    @Test
    @DisplayName("Choisir une case de coordonnée x = -1 lance une erreur")
    public void testChoisirCaseDeCoordonneeXEgaleAMoinsUnLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.choisirCase(-1, 5));
    }

    @Test
    @DisplayName("Choisir une case de coordonnée x > largeur lance une erreur")
    public void testChoisirCaseDeCoordonneeXSuperieurALargeurLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.choisirCase(env.getLargeur() + 2, 5));
    }

    @Test
    @DisplayName("Choisir une case de coordonnée x = largeur lance une erreur")
    public void testChoisirCaseDeCoordonneeXEgaleALargeurLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.choisirCase(env.getLargeur(), 5));
    }

    @Test
    @DisplayName("Choisir une case de coordonnée y = 1 quand le deplacement est actif fonctionne")
    public void testChoisirCaseDeCoordonneeYEgaleAUnQuandDeplacementActifFonctionne(){
        op.setX(2); op.setY(5);
        env.choisirCase(1, 6);
        assertEquals(1, op.getX());
        assertEquals(6, op.getY());
    }

    @Test
    @DisplayName("Choisir une case de coordonnée y = 0 quand le deplacement est actif fonctionne")
    public void testChoisirCaseDeCoordonneeYEgaleAZeroQuandDeplacementActifFonctionne(){
        op.setX(7); op.setY(1);
        env.choisirCase(7, 0);
        assertEquals(7, op.getX());
        assertEquals(0, op.getY());
    }

    @Test
    @DisplayName("Choisir une case de coordonnée y = -1 lance une erreur")
    public void testChoisirCaseDeCoordonneeYEgaleAMoinsUnLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.choisirCase(5, -1));
    }

    @Test
    @DisplayName("Choisir une case de coordonnée y > hauteur lance une erreur")
    public void testChoisirCaseDeCoordonneeYSuperieurAHauteurLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.choisirCase(5, env.getLargeur() + 2));
    }

    @Test
    @DisplayName("Choisir une case de coordonnée y = hauteur lance une erreur")
    public void testChoisirCaseDeCoordonneeXEgaleAHauteurLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.choisirCase(5, env.getLargeur()));
    }

    @Test
    @DisplayName("Choisir une case où se situe une ennemi quand le deplacement est actif ne fait rien")
    public void testChoisirCaseAvecEnnemiQuandDeplacementActifNeFaitRien(){
        Terroriste ennemi = env.getEnnemis().get(0);
        int nbPa = op.getPointsAction();

        op.setX(ennemi.getX());
        op.setY(ennemi.getY() - 1);
        assertEquals(ennemi.getX(), op.getX());
        assertEquals(ennemi.getY() - 1, op.getY());
        assertEquals(nbPa, op.getPointsAction());

        env.choisirCase(ennemi.getX(), ennemi.getY());
        assertEquals(ennemi.getX(), op.getX());
        assertEquals(ennemi.getY() - 1, op.getY());
        assertEquals(nbPa, op.getPointsAction());
    }

    @Test
    @DisplayName("Choisir une case où se situe un objectif quand le deplacement est actif fonctionne")
    public void testChoisirCaseAvecObjectifQuandDeplacementActifFonctionne() {
        Objectif objectif = null;
        int nbPa = op.getPointsAction();
        for (Case c : env.getPlateau()) {
            if (c.estObjectif) {
                objectif = (Objectif) c;
                break;
            }
        }

        assertNotNull(objectif);

        op.setX(objectif.x);
        op.setY(objectif.y - 1);
        assertEquals(objectif.x, op.getX());
        assertEquals(objectif.y - 1, op.getY());
        assertEquals(nbPa, op.getPointsAction());
        assertFalse(op.possedeObjectif());

        env.choisirCase(objectif.x, objectif.y);
        assertEquals(objectif.x, op.getX());
        assertEquals(objectif.y, op.getY());
        assertEquals(nbPa - op.getDeplacement().cout, op.getPointsAction());
        assertTrue(op.possedeObjectif());
        assertNotEquals(objectif, env.getCase(objectif.x, objectif.y));
    }

    @Test
    @DisplayName("Choisir une case valide sans ennemis quand le tir est actif ne fait rien")
    public void testChoisirCaseValideSansEnnemiQuandTirActifNeFaitRien(){
        List<Terroriste> ennemis = env.getEnnemis();
        int nbEnnemis = ennemis.size();
        int nbPa = op.getPointsAction();
        int x = op.getX();
        int y = op.getY();
        Case cible = null;
        for(Case c:env.getPlateau()){
            if((c.x == op.getX()) != (c.y == op.getY())){ // XOR
                if(!env.aEnnemisSurCase(c)){
                    cible = c;
                    break;
                }
            }
        }

        assertNotNull(cible);

        assertEquals(x, op.getX());
        assertEquals(y, op.getY());
        assertEquals(nbEnnemis, ennemis.size());
        assertEquals(nbPa, op.getPointsAction());

        env.setTirActionActive();
        env.choisirCase(cible.x, cible.y);
        assertEquals(x, op.getX());
        assertEquals(y, op.getY());
        assertEquals(nbEnnemis, ennemis.size());
        assertEquals(nbPa, op.getPointsAction());
    }

    @Test
    @DisplayName("Choisir une case valide avec des ennemis quand le tir est actif tue les ennemis")
    public void testChoisirCaseValideAvecDesEnnemisQuandTirActifTueLesEnnemis(){
        List<Terroriste> ennemis = env.getEnnemis();
        int nbEnnemis = ennemis.size();
        int nbSurCase = 0;
        int nbPa = op.getPointsAction();
        Case cible = null;
        for(Case c:env.getPlateau()){
            if(env.aEnnemisSurCase(c)){
                cible = c;
                for (Terroriste e:ennemis) {
                    if(e.getX() == c.x && e.getY() == c.y){
                        nbSurCase ++;
                    }
                }
                break;
            }
        }

        assertNotNull(cible);

        if(cible.x >= 0){
            op.setX(cible.x + 1);
        }
        else{
            op.setX(cible.x - 1);
        }
        op.setY(cible.y);
        int x = op.getX();
        int y = op.getY();

        assertEquals(x, op.getX());
        assertEquals(y, op.getY());
        assertEquals(nbEnnemis, ennemis.size());
        assertEquals(nbPa, op.getPointsAction());

        env.setTirActionActive();
        env.choisirCase(cible.x, cible.y);
        assertEquals(x, op.getX());
        assertEquals(y, op.getY());
        assertEquals(nbEnnemis - nbSurCase, ennemis.size());
        assertEquals(nbPa - op.getTir().cout, op.getPointsAction());
    }

    @Test
    @DisplayName("Choisir une case pas dans la ligne de mire quand le tir est actif ne fait rien")
    public void testChoisirCasePasDansLigneMireQuandTirActifNeFaitRien(){
        List<Terroriste> ennemis = env.getEnnemis();
        int nbEnnemis = ennemis.size();
        int nbPa = op.getPointsAction();
        Case cible = env.getCase(6, 6);
        op.setX(5); op.setY(5);

        assertNotNull(cible);

        assertEquals(5, op.getX());
        assertEquals(5, op.getY());
        assertEquals(nbEnnemis, ennemis.size());
        assertEquals(nbPa, op.getPointsAction());

        env.setTirActionActive();
        env.choisirCase(cible.x, cible.y);
        assertEquals(5, op.getX());
        assertEquals(5, op.getY());
        assertEquals(nbEnnemis, ennemis.size());
        assertEquals(nbPa, op.getPointsAction());
    }

    // ==================== recupereObjectif ====================

    @Test
    @DisplayName("Recuperer un objectif à un opérateur sur un objectif fonctionne")
    public void testRecupererObjectifOperateurSurObjectifFonctionne(){
        Objectif objectif = null;
        for (Case c : env.getPlateau()) {
            if (c.estObjectif) {
                objectif = (Objectif) c;
                break;
            }
        }

        assertNotNull(objectif);

        op.setX(objectif.x);
        op.setY(objectif.y);
        assertFalse(op.possedeObjectif());

        env.recupereObjectif(objectif, op);
        assertTrue(op.possedeObjectif());
        assertNotEquals(objectif, env.getCase(objectif.x, objectif.y));
    }

    @Test
    @DisplayName("Recuperer un objectif à un opérateur qui n'est pas sur l'objectif lance une erreur")
    public void testRecupererObjectifOperateurQuandOperateurPasSurObjectifLanceUneErreur(){
        Objectif temp = null;
        for (Case c : env.getPlateau()) {
            if (c.estObjectif) {
                temp = (Objectif) c;
                break;
            }
        }
        final Objectif objectif = temp;

        assertNotNull(objectif);

        op.setX(objectif.x-1);
        op.setY(objectif.y);
        assertFalse(op.possedeObjectif());

        assertThrows(AssertionError.class, () -> env.recupereObjectif(objectif, op));
    }

    @Test
    @DisplayName("Recuperer un objectif à un opérateur null lance une erreur")
    public void testRecupererObjectifOperateurQuandOperateurNullLanceUneErreur(){
        Objectif temp = null;
        for (Case c : env.getPlateau()) {
            if (c.estObjectif) {
                temp = (Objectif) c;
                break;
            }
        }
        final Objectif objectif = temp;

        assertNotNull(objectif);
        assertThrows(AssertionError.class, () -> env.recupereObjectif(objectif, null));
    }

    @Test
    @DisplayName("Recuperer un objectif null lance une erreur")
    public void testRecupererObjectifNullLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.recupereObjectif(null, env.getOperateurActif()));
    }

    // ==================== tuerEnnemis ====================

    @Test
    @DisplayName("Tuer un ennemi sur une case fonctionne")
    public void testTuerUnEnnemiSurCaseFonctionne(){
        List<Terroriste> ennemis = env.getEnnemis();
        Terroriste t = ennemis.get(0);
        int nbEnnemis = ennemis.size();

        assertEquals(nbEnnemis, ennemis.size());
        env.tuerEnnemis(env.getCase(t.getX(), t.getY()));
        assertEquals(nbEnnemis - 1, ennemis.size());
        assertFalse(ennemis.contains(t));
    }

    @Test
    @DisplayName("Tuer un ennemi sur une case avec aucun ennemi ne fait rien")
    public void testTuerUnEnnemiSurCaseAvecAucunEnnemiNeFaitRien(){
        List<Terroriste> ennemis = env.getEnnemis();
        int nbEnnemis = ennemis.size();
        Case cible = null;
        for(Case c:env.getPlateau()) {
            if(!env.aEnnemisSurCase(c)){
                cible = c;
                break;
            }
        }

        assertEquals(nbEnnemis, ennemis.size());
        env.tuerEnnemis(cible);
        assertEquals(nbEnnemis, ennemis.size());
    }

    @Test
    @DisplayName("Tuer un ennemi sur une case null lance une erreur")
    public void testTuerUnEnnemiSurCaseNullLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.tuerEnnemis(null));
    }


    // ==================== aEnnemisSurCase ====================


    @Test
    @DisplayName("Verifier si un ennemi est sur la case sur une case avec plusieurs ennemis fonctionne")
    public void testAEnnemiSurCaseAvecPlusieursEnnemisFonctionne(){
        Terroriste t1 = env.getEnnemis().get(0);
        Terroriste t2 = env.getEnnemis().get(1);

        t2.setX(t1.getX());
        t2.setY(t1.getY());

        assertTrue(env.aEnnemisSurCase(env.getCase(t2.getX(), t2.getY())));
    }

    @Test
    @DisplayName("Verifier si un ennemi est sur la case sur une case avec un ennemi fonctionne")
    public void testAEnnemiSurCaseAvecUnEnnemiFonctionne(){
        Terroriste t1 = env.getEnnemis().get(0);
        Terroriste t2 = env.getEnnemis().get(1);

        t1.setX(5); t1.setY(5);
        t1.setX(6); t1.setY(6);

        assertTrue(env.aEnnemisSurCase(env.getCase(t2.getX(), t2.getY())));
    }

    @Test
    @DisplayName("Verifier si un ennemi est sur la case sur une case avec un ennemi fonctionne")
    public void testAEnnemiSurCaseAvecAucunEnnemiFonctionne(){
        Terroriste t1 = env.getEnnemis().get(0);
        Terroriste t2 = env.getEnnemis().get(1);

        t1.setX(5); t1.setY(5);
        t1.setX(6); t1.setY(6);

        assertFalse(env.aEnnemisSurCase(env.getCase(t2.getX() + 1, t2.getY() + 1)));
    }

    @Test
    @DisplayName("Verifier si un ennemi est sur la case sur une case null lance une erreur")
    public void testAEnnemiSurCaseNullLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.aEnnemisSurCase(null));
    }


    // ==================== getNombresAleatoires ====================
    @Test
    @DisplayName("Generer plusieurs nombres aleatoires fonctionne")
    public void testGenererPlusieursNombresAleatoiresFonctionne(){
        List<Double> nombres = env.getNombresAleatoires(5);

        assertEquals(5, nombres.size());
        for(int i = 0; i < 5; i++){
            for(int j = i+1; j < 5; j++){
                assertNotEquals(nombres.get(i), nombres.get(j));
            }
        }
    }

    @Test
    @DisplayName("Generer un nombre aleatoire fonctionne")
    public void testGenererUnNombreAleatoireFonctionne(){
        List<Double> nombres = env.getNombresAleatoires(1);

        assertEquals(1, nombres.size());
    }

    @Test
    @DisplayName("Generer zero nombres aleatoires lance une erreur")
    public void testGenererZeroNombresAleatoiresLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.getNombresAleatoires(0));
    }

    @Test
    @DisplayName("Generer -1 nombres aleatoires lance une erreur")
    public void testGenererMoinsUnNombresAleatoiresLanceUneErreur(){
        assertThrows(AssertionError.class, () -> env.getNombresAleatoires(-1));
    }
}
