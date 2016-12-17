package co.gbyte.weightlog;

import org.junit.Test;
import static org.junit.Assert.*;
import co.gbyte.weightlog.model.Weight;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WeightTest {

    private Weight w = null;


    @Test
    public void testWeightHeightAndWeightNotSet() {
        w = new Weight() ;
        Weight.resetHeight();

        assertEquals(0, w.getWeight());
        assertEquals(0.0, w.bmi(), 0.00000001);
    }

    @Test
    public void testWeightHeightNotSet() {
        w = new Weight() ;
        Weight.resetHeight();

        w.setWeight(85000);
        assertEquals(85000, w.getWeight());
        assertEquals(0.0, w.bmi(), 0.00000001);

        w.setWeight(69000);
        assertEquals(69000, w.getWeight());
        assertEquals(0.0, w.bmi(), 0.00000001);
    }

    @Test
    public void testWeightWeightNotSet() {
        w = new Weight() ;
        Weight.setHeight(1690);
        assertEquals(0.0, w.bmi(), 0.00000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHeightLessThanMinimum() {
        w = new Weight() ;
        Weight.setHeight(699);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHeightGreaterThanMaximum() {
        w = new Weight() ;
        Weight.setHeight(2401);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWeightLessThanMinimum() {
        w = new Weight() ;
        Weight.resetHeight();
        w.setWeight(1999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWeightGreaterThanMaximum() {
        w = new Weight() ;
        Weight.resetHeight();
        w.setWeight(640001);
    }

    @Test
    public void testBmi() {
        w = new Weight() ;
        Weight.setHeight(1730);

        w.setWeight(85000);
        assertEquals(28.40, w.bmi(), 0.005);

        w.setWeight(78000);
        assertEquals(26.06, w.bmi(), 0.005);

        w.setWeight(75000);
        assertEquals(25.06, w.bmi(), 0.005);

        w.setWeight(74900);
        assertEquals(25.03, w.bmi(), 0.005);

        w.setWeight(74800);
        assertEquals(24.99, w.bmi(), 0.005);

        w.setWeight(72000);
        assertEquals(24.06, w.bmi(), 0.005);

        w.setWeight(70000);
        assertEquals(23.39, w.bmi(), 0.005);

        w.setWeight(69000);
        assertEquals(23.05, w.bmi(), 0.005);
    }

    @Test
    public void testBmwBoundaryValues() {
        w = new Weight() ;
        Weight.setHeight(700);
        w.setWeight(2000);
        assertEquals(4.08, w.bmi(), 0.005);

        w.setWeight(640000);
        assertEquals(1306.12, w.bmi(), 0.005);

        Weight.setHeight(2400);
        assertEquals( 111.11, w.bmi(), 0.005);

        w.setWeight(2000);
        assertEquals(0.35, w.bmi(), 0.005);
    }
}