package testPersonnages;

import actions.Deplacement;
import actions.Tir;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import personnages.Operateur;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class TestOperateur {

    private Environnement envMock = null;
    private Deplacement deplMock = null;
    private Tir tirMock = null;
    private Operateur op = null;

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
        op = null;
    }

    // ==================== Constructeur ====================

    @Test
    @DisplayName("Creer un opérateur avec coordonnée 0 < x < largeur fonctionne")
    public void testCreerOperateurAvecCoordonneeXCorrecteFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,5, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée x à 1 fonctionne")
    public void testCreerOperateurAvecCoordonneeXAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,1, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée x à 0 fonctionne")
    public void testCreerOperateurAvecCoordonneeXAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée x à -1 lance une erreur")
    public void testCreerOperateurAvecCoordonneeXAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,-1, 0, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée x > largeur lance une erreur")
    public void testCreerOperateurAvecCoordonneeXSuperieureALargeurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,11, 0, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée x == largeur lance une erreur")
    public void testCreerOperateurAvecCoordonneeXEgalALargeurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,10, 0, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée 0 < y < largeur fonctionne")
    public void testCreerOperateurAvecCoordonneeYCorrecteFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 5, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée y à 1 fonctionne")
    public void testCreerOperateurAvecCoordonneeYAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 1, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée y à 0 fonctionne")
    public void testCreerOperateurAvecCoordonneeYAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée y à -1 lance une erreur")
    public void testCreerOperateurAvecCoordonneeYAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,0, -1, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée y == hauteur lance une erreur")
    public void testCreerOperateurAvecCoordonneeYEgalAHauteurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,0, 10, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec coordonnée y > hauteur lance une erreur")
    public void testCreerOperateurAvecCoordonneeYSuperieureAHauteurLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,0, 11, 5, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec nombre de PA > 1 fonctionne")
    public void testCreerOperateurAvecNombrePointsActionsSuperieurAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 0, 5, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec nombre de PA à 1 fonctionne")
    public void testCreerOperateurAvecNombrePointsActionsAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 0, 1, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec nombre de PA à 0 fonctionne")
    public void testCreerOperateurAvecNombrePointsActionsAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        new Operateur(envMock,0, 0, 0, deplMock, tirMock);

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec nombre de PA < 0 lance une erreur")
    public void testCreerOperateurAvecNombrePointsActionsAMoinsUnLanceErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,0, 0, -1, deplMock, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec action deplacement null lance une erreur")
    public void testCreerOperateurAvecActionDeplacementNullLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,0, 0, 5, null, tirMock));

        verify(envMock);
    }

    @Test
    @DisplayName("Creer un opérateur avec action tir null lance une erreur")
    public void testCreerOperateurAvecActionTirNullLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        assertThrows(AssertionError.class, () -> new Operateur(envMock,0, 0, 5, deplMock, null));

        verify(envMock);
    }


    // ==================== removePointsAction ====================

    @Test
    @DisplayName("Retirer un PA quand nombre de PA > 1 retire 1 PA")
    public void testRetirerUnPointDActionQuandLeNombreDePointsDActionEstSuperieurAUnRetireUnPointDAction(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        op = new Operateur(envMock,0, 0, 5, deplMock, tirMock);
        assertEquals(5, op.getPointsAction());
        op.removePointsAction(1);
        assertEquals(4, op.getPointsAction());

        verify(envMock);
    }

    @Test
    @DisplayName("Retirer un PA quand nombre de PA à 1 retire 1 PA")
    public void testRetirerUnPointDActionQuandLeNombreDePointsDActionEstUnRetireUnPointDAction(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        op = new Operateur(envMock,0, 0, 1, deplMock, tirMock);
        assertEquals(1, op.getPointsAction());
        op.removePointsAction(1);
        assertEquals(0, op.getPointsAction());

        verify(envMock);
    }

    @Test
    @DisplayName("Retirer un PA quand nombre de PA à 0 retire 0 PA")
    public void testRetirerUnPointDActionQuandLeNombreDePointsDActionEstZeroRetireZeroPointDAction(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);

        op = new Operateur(envMock,0, 0, 0, deplMock, tirMock);
        assertEquals(0, op.getPointsAction());
        op.removePointsAction(0);
        assertEquals(0, op.getPointsAction());

        verify(envMock);
    }




}
