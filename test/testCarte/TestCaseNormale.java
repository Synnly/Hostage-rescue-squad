package testCarte;

import carte.cases.CaseNormale;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestCaseNormale {

    private CaseNormale caseNormale = null;
    private Environnement envMock = null;

    @Before
    public void setup(){
        envMock = createMock(Environnement.class);
    }

    @After
    public void teardown(){
        caseNormale = null;
        envMock = null;
    }

    @Test
    @DisplayName("Creer une case normale avec des coordonnees correctes fonctionne")
    public void testCreerCaseNormaleAvecCoordonneesCorrectesFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        caseNormale = new CaseNormale(envMock, 5, 5);
        assertEquals(5, caseNormale.x);
        assertEquals(5, caseNormale.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee x = 1 fonctionne")
    public void testCreerCaseNormaleAvecCoordonneeXEgaleAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        caseNormale = new CaseNormale(envMock, 1, 5);
        assertEquals(1, caseNormale.x);
        assertEquals(5, caseNormale.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee x = 0 fonctionne")
    public void testCreerCaseNormaleAvecCoordonneeXEgaleAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        caseNormale = new CaseNormale(envMock, 0, 5);
        assertEquals(0, caseNormale.x);
        assertEquals(5, caseNormale.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee x = -1 lance une erreur")
    public void testCreerCaseNormaleAvecCoordonneeXEgaleAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        replay(envMock);
        assertThrows(AssertionError.class, () -> new CaseNormale(envMock, -1, 5));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee x > largeur lance une erreur")
    public void testCreerCaseNormaleAvecCoordonneeXSuperieurALargeurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new CaseNormale(envMock, 12, 5));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee x = largeur lance une erreur")
    public void testCreerCaseNormaleAvecCoordonneeXEgaleALargeurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new CaseNormale(envMock, 10, 5));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee y = 1 fonctionne")
    public void testCreerCaseNormaleAvecCoordonneeYEgaleAUnFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        caseNormale = new CaseNormale(envMock, 5, 1);
        assertEquals(5, caseNormale.x);
        assertEquals(1, caseNormale.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee y = 0 fonctionne")
    public void testCreerCaseNormaleAvecCoordonneeYEgaleAZeroFonctionne(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);

        replay(envMock);
        caseNormale = new CaseNormale(envMock, 5, 0);
        assertEquals(5, caseNormale.x);
        assertEquals(0, caseNormale.y);
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee y = -1 lance une erreur")
    public void testCreerCaseNormaleAvecCoordonneeYEgaleAMoinsUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10);
        replay(envMock);
        assertThrows(AssertionError.class, () -> new CaseNormale(envMock, 5, -1));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee y > hauteur lance une erreur")
    public void testCreerCaseNormaleAvecCoordonneeYSuperieurAHauteurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new CaseNormale(envMock, 5, 12));
        verify(envMock);
    }

    @Test
    @DisplayName("Creer une case normale avec coordonnee y = hauteur lance une erreur")
    public void testCreerCaseNormaleAvecCoordonneeYEgaleAHauteurUnLanceUneErreur(){
        expect(envMock.getLargeur()).andReturn(10);
        expect(envMock.getHauteur()).andReturn(10).times(2);

        replay(envMock);
        assertThrows(AssertionError.class, () -> new CaseNormale(envMock, 5, 10));
        verify(envMock);
    }
}
