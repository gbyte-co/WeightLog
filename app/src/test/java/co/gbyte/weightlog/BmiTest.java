package co.gbyte.weightlog;

import org.junit.Test;

import co.gbyte.weightlog.model.Bmi;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BmiTest {

    @Test
    public void testBmi_calc_normalArguments() {
        assertEquals(28.40, Bmi.calc(1730, 85000), 0.005);
        assertEquals(26.06, Bmi.calc(1730, 78000), 0.005);
        assertEquals(25.06, Bmi.calc(1730, 75000), 0.005);
        assertEquals(25.03, Bmi.calc(1730, 74900), 0.005);
        assertEquals(24.99, Bmi.calc(1730, 74800), 0.005);
        assertEquals(24.06, Bmi.calc(1730, 72000), 0.005);
        assertEquals(23.39, Bmi.calc(1730, 70000), 0.005);
        assertEquals(23.05, Bmi.calc(1730, 69000), 0.005);
    }

    @Test
    public void testBmw_calc_boundaryArguments() {
        assertEquals( 102.04, Bmi.calc( 700,  50000), 0.005);
        assertEquals(   8.00, Bmi.calc(2500,  50000), 0.005);
        assertEquals(   0.70, Bmi.calc(1690,   2000), 0.005);
        assertEquals( 224.08, Bmi.calc(1690, 640000), 0.005);
        assertEquals(1306.12, Bmi.calc( 700, 640000), 0.005);
        assertEquals( 102.40, Bmi.calc(2500, 640000), 0.005);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_heightLessThanMinimum() {
        Bmi.calc( 699, 85000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_heightEqualsZero() {
        Bmi.calc(   0, 85000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_heightLesserThanZero() {
        Bmi.calc(  -1000 , 85000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_heightGreaterThanMax() {
        Bmi.calc(  24001, 85000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_weightLesserThanMin() {
        Bmi.calc(  1690, 1999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_weightGreaterThanMin() {
        Bmi.calc(  1690, 640001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_weightEqualsZero() {
        Bmi.calc(  1690, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBmi_calc_weightLessThanZero() {
        Bmi.calc(  1690, -100);
    }
}