package tests;
import observable.Environnement;
import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestEnvironnement {
    @Test
    public void testDimensionPlateau(){
        Environnement mockService = EasyMock.createMock(Environnement.class);

        EasyMock.expect(mockService.getLargeur()).andReturn(7);
        EasyMock.expect(mockService.getHauteur()).andReturn(9);

        EasyMock.replay(mockService);

        int largeur = mockService.getLargeur();
        int hauteur = mockService.getHauteur();

        assertEquals(7, largeur);
        assertEquals(9, hauteur);

        EasyMock.verify(mockService);
    }
    @Test
    public void test(){
        Environnement mockService = EasyMock.createMock(Environnement.class);

        EasyMock.expect(mockService.getLargeur()).andReturn(7);
        EasyMock.expect(mockService.getHauteur()).andReturn(9);

        EasyMock.replay(mockService);

        int largeur = mockService.getLargeur();
        int hauteur = mockService.getHauteur();

        assertEquals(7, largeur);
        assertEquals(9, hauteur);

        EasyMock.verify(mockService);
    }
}
