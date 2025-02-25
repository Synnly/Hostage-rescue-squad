package testPersonnages;

import actions.Deplacement;
import actions.Tir;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import personnages.Terroriste;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTerroriste {

    private Environnement envMock = null;
    private Deplacement deplMock = null;
    private Tir tirMock = null;
    private Terroriste terro = null;

    @Before
    public void setup(){
        envMock = createMock(Environnement.class);
        tirMock = createMock(Tir.class);
        deplMock = createMock(Deplacement.class);
    }

    @After
    public void teardown(){
        envMock = null;
        tirMock = null;
        deplMock = null;
        terro = null;
    }

    // ==================== Constructeur ====================

    @Test
    @DisplayName("Creer un terroriste avec coordonnée 0 < x < largeur fonctionne")
    public void testCreerTerroristeAvecCoordonneeXCorrecteFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,5, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée x à 1 fonctionne")
    public void testCreerTerroristeAvecCoordonneeXAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,1, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée x à 0 fonctionne")
    public void testCreerTerroristeAvecCoordonneeXAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée x à -1 lance une erreur")
    public void testCreerTerroristeAvecCoordonneeXAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,-1, 0, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée x > largeur lance une erreur")
    public void testCreerTerroristeAvecCoordonneeXSuperieureALargeurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,11, 0, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée x == largeur lance une erreur")
    public void testCreerTerroristeAvecCoordonneeXEgalALargeurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,10, 0, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée 0 < y < largeur fonctionne")
    public void testCreerTerroristeAvecCoordonneeYCorrecteFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 5, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée y à 1 fonctionne")
    public void testCreerTerroristeAvecCoordonneeYAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 1, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée y à 0 fonctionne")
    public void testCreerTerroristeAvecCoordonneeYAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée y à -1 lance une erreur")
    public void testCreerTerroristeAvecCoordonneeYAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,0, -1, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée y == hauteur lance une erreur")
    public void testCreerTerroristeAvecCoordonneeYEgalAHauteurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,0, 10, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec coordonnée y > hauteur lance une erreur")
    public void testCreerTerroristeAvecCoordonneeYSuperieureAHauteurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,0, 11, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec nombre de PA > 1 fonctionne")
    public void testCreerTerroristeAvecNombrePointsActionsSuperieurAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec nombre de PA à 1 fonctionne")
    public void testCreerTerroristeAvecNombrePointsActionsAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 0, 1, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec nombre de PA à 0 fonctionne")
    public void testCreerTerroristeAvecNombrePointsActionsAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Terroriste(envMock,0, 0, 0, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec nombre de PA < 0 fonctionne")
    public void testCreerTerroristeAvecNombrePointsActionsAMoinsUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        terro = new Terroriste(envMock,0, 0, -1, deplMock, tirMock);
        assertEquals(0, terro.getPointsAction());

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec action deplacement null lance une erreur")
    public void testCreerTerroristeAvecActionDeplacementNullLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,0, 0, 5, null, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un terroriste avec action tir null lance une erreur")
    public void testCreerTerroristeAvecActionTirNullLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Terroriste(envMock,0, 0, 5, deplMock, null));

        verify(envMock);
    }


    // ==================== removePointsAction ====================

    @Test
    @DisplayName("Retirer un PA retire 0 PA")
    public void testRetirerUnPointDActionRetireAucunPointDAction(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        terro = new Terroriste(envMock,0, 0, 5, deplMock, tirMock);
        assertEquals(0, terro.getPointsAction());
        terro.removePointsAction(1);
        assertEquals(0, terro.getPointsAction());

        verify(envMock);
    }

    @Test
    @DisplayName("Retirer un PA quand nombre de PA à 0 retire 0 PA")
    public void testRetirerUnPointDActionQuandLeNombreDePointsDActionEstZeroRetireZeroPointDAction(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        terro = new Terroriste(envMock,0, 0, 0, deplMock, tirMock);
        assertEquals(0, terro.getPointsAction());
        terro.removePointsAction(0);
        assertEquals(0, terro.getPointsAction());

        verify(envMock);
    }
}
