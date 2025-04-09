package testCarte;

import carte.cases.Case;
import carte.cases.CaseNormale;
import carte.Routine;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TestRoutine {
    private Environnement envMock = null;
    private CaseNormale caseNormaleMock1 = null;
    private CaseNormale caseNormaleMock2 = null;
    private CaseNormale caseNormaleMock3 = null;
    private Routine routine = null;

    @Before
    public void setup() {
        envMock = createMock(Environnement.class);
        caseNormaleMock1 = createMock(CaseNormale.class);
        caseNormaleMock2 = createMock(CaseNormale.class);
        caseNormaleMock3 = createMock(CaseNormale.class);
    }

    @After
    public void teardown() {
        envMock = null;
        caseNormaleMock1 = null;
        caseNormaleMock2 = null;
        caseNormaleMock3 = null;
        routine = null;
    }

    // ==================== constructeur ====================

    @Test
    @DisplayName("Creer une routine avec une case normale fonctionne")
    public void testCreerRoutineAvecCaseNormaleFonctionne() {
        routine = new Routine(caseNormaleMock1);
        assertNotNull(routine);
    }

    @Test
    @DisplayName("Creer une routine avec une case null lance une erreur")
    public void testCreerRoutineAvecCaseNullLanceUneErreur() {
        assertThrows(AssertionError.class, () -> new Routine((Case) null));
    }

    // ==================== ajouterCase ====================

    @Test
    @DisplayName("Ajouter une case null lance une erreur")
    public void testAjouterCaseNullLanceUneErreur() {
        routine = new Routine(caseNormaleMock1);
        assertThrows(AssertionError.class, () -> routine.ajouterCase(caseNormaleMock1, null));
    }

    @Test
    @DisplayName("Ajouter une case apres une case null lance une erreur")
    public void testAjouterCaseApresCaseNullLanceUneErreur() {
        routine = new Routine(caseNormaleMock1);
        assertThrows(AssertionError.class, () -> routine.ajouterCase(null, caseNormaleMock1));
    }

    @Test
    @DisplayName("Ajouter une case apres une case fonctionne")
    public void testAjouterCaseApresCaseFonctionne() {
        expect(caseNormaleMock1.getId()).andReturn(1).atLeastOnce();
        expect(caseNormaleMock2.getId()).andReturn(2).atLeastOnce();

        routine = new Routine(caseNormaleMock1);
        routine.ajouterCase(caseNormaleMock1, caseNormaleMock2);

        replay(caseNormaleMock1, caseNormaleMock2);
        assertEquals(caseNormaleMock2, routine.prochaineCase(caseNormaleMock1));
        assertEquals(caseNormaleMock1, routine.prochaineCase(caseNormaleMock2));
        verify(caseNormaleMock1, caseNormaleMock2);
    }

    @Test
    @DisplayName("Ajouter une case deja dans la liste lance une erreur")
    public void testAjouterCaseDejaPresenteLanceUneErreur() {
        routine = new Routine(caseNormaleMock1);
        assertThrows(AssertionError.class, () -> routine.ajouterCase(caseNormaleMock1, caseNormaleMock1));
    }

    @Test
    @DisplayName("Ajouter une case apres une autre non presente dans la liste lance une erreur")
    public void testAjouterCaseApresCaseNonPresenteLanceUneErreur() {
        routine = new Routine(caseNormaleMock1);
        assertThrows(AssertionError.class, () -> routine.ajouterCase(caseNormaleMock3, caseNormaleMock2));
    }

    // ==================== prochaineCase ====================

    @Test
    @DisplayName("Obtenir la prochaine case dans routine de taille 2 fonctionne")
    public void testObtenirProchaineCaseDansRoutineDeTailleDeuxFonctionne() {
        expect(caseNormaleMock1.getId()).andReturn(1).times(3);
        expect(caseNormaleMock2.getId()).andReturn(2).times(3);

        replay(caseNormaleMock1, caseNormaleMock2);
        routine = new Routine(caseNormaleMock1);
        routine.ajouterCase(caseNormaleMock1, caseNormaleMock2);

        assertEquals(caseNormaleMock2, routine.prochaineCase(caseNormaleMock1));
        assertEquals(caseNormaleMock1, routine.prochaineCase(caseNormaleMock2));
        verify(caseNormaleMock1, caseNormaleMock2);
    }

    @Test
    @DisplayName("Obtenir la prochaine case dans routine de taille 1 fonctionne")
    public void testObtenirProchaineCaseDansRoutineDeTailleUnFonctionne() {
        expect(caseNormaleMock1.getId()).andReturn(1).times(2);

        replay(caseNormaleMock1);
        routine = new Routine(caseNormaleMock1);

        assertEquals(caseNormaleMock1, routine.prochaineCase(caseNormaleMock1));
        verify(caseNormaleMock1);
    }

    @Test
    @DisplayName("Obtenir la prochaine case apres null dans routine lance une erreur")
    public void testObtenirProchaineCaseApresNullLanceUneErreur() {
        routine = new Routine(caseNormaleMock1);

        assertThrows(AssertionError.class, () -> routine.prochaineCase(null));
    }

    @Test
    @DisplayName("Obtenir la prochaine case apres une case pas dans routine lance une erreur")
    public void testObtenirProchaineCaseApresCasePasDansRoutineLanceUneErreur() {
        expect(caseNormaleMock1.getId()).andReturn(1);
        expect(caseNormaleMock2.getId()).andReturn(2);

        routine = new Routine(caseNormaleMock1);

        replay(caseNormaleMock1, caseNormaleMock2);

        assertThrows(AssertionError.class, () -> routine.prochaineCase(caseNormaleMock2));

        verify(caseNormaleMock1, caseNormaleMock2);
    }
}
