package testCarte;

import carte.cases.Objectif;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestObjectif {

    private Objectif objectif = null;
    private Environnement envMock = null;

    @Before
    public void setup(){
        envMock = createMock(Environnement.class);
    }

    @After
    public void teardown(){
        objectif = null;
        envMock = null;
    }

    @Test
    @DisplayName("Creer un objectif avec des coordonnees correctes fonctionne")
    public void testCreerObjectifAvecCoordonneesCorrectesFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        objectif = new Objectif(envMock, 5, 5);
        assertEquals(5, objectif.x);
        assertEquals(5, objectif.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee x = 1 fonctionne")
    public void testCreerObjectifAvecCoordonneeXEgaleAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        objectif = new Objectif(envMock, 1, 5);
        assertEquals(1, objectif.x);
        assertEquals(5, objectif.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee x = 0 fonctionne")
    public void testCreerObjectifAvecCoordonneeXEgaleAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        objectif = new Objectif(envMock, 0, 5);
        assertEquals(0, objectif.x);
        assertEquals(5, objectif.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee x = -1 lance une erreur")
    public void testCreerObjectifAvecCoordonneeXEgaleAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        replay(envMock);
        assertThrows(AssertionError.class, () -> new Objectif(envMock, -1, 5));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee x > largeur lance une erreur")
    public void testCreerObjectifAvecCoordonneeXSuperieurALargeurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new Objectif(envMock, 12, 5));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee x = largeur lance une erreur")
    public void testCreerObjectifAvecCoordonneeXEgaleALargeurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new Objectif(envMock, 10, 5));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee y = 1 fonctionne")
    public void testCreerObjectifAvecCoordonneeYEgaleAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        objectif = new Objectif(envMock, 5, 1);
        assertEquals(5, objectif.x);
        assertEquals(1, objectif.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee y = 0 fonctionne")
    public void testCreerObjectifAvecCoordonneeYEgaleAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        objectif = new Objectif(envMock, 5, 0);
        assertEquals(5, objectif.x);
        assertEquals(0, objectif.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee y = -1 lance une erreur")
    public void testCreerObjectifAvecCoordonneeYEgaleAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);
        assertThrows(AssertionError.class, () -> new Objectif(envMock, 5, -1));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee y > hauteur lance une erreur")
    public void testCreerObjectifAvecCoordonneeYSuperieurAHauteurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new Objectif(envMock, 5, 12));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer un objectif avec coordonnee y = hauteur lance une erreur")
    public void testCreerObjectifAvecCoordonneeYEgaleAHauteurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new Objectif(envMock, 5, 10));
        verify(envMock);
    }
}
