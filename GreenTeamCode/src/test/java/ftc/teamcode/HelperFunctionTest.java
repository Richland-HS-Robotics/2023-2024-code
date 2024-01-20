package ftc.greenTeamCode;

import org.firstinspires.ftc.greenTeamCode.util.HelperFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelperFunctionTest {

    @Test
    public void testCompare(){
        Assert.assertTrue(HelperFunctions.compare(5,5,0.00000001));
        Assert.assertTrue(HelperFunctions.compare(3,3.9,1));
        Assert.assertTrue(HelperFunctions.compare(0,0,0.00000000000000001));


        Assert.assertFalse(HelperFunctions.compare(3,9,1));
        Assert.assertFalse(HelperFunctions.compare(1,2,0.9));
    }






    @Test
    public void testClamp(){
        Assert.assertEquals(2,HelperFunctions.clamp(2,-10,10),0.0001);
        Assert.assertEquals(-2,HelperFunctions.clamp(-2,-10,10),0.0001);
        Assert.assertEquals(-2,HelperFunctions.clamp(-2,-3,10),0.0001);

        Assert.assertEquals(10,HelperFunctions.clamp(55,-10,10),0.0001);
        Assert.assertEquals(-10,HelperFunctions.clamp(-55,-10,10),0.0001);
    }


    @Test
    public void test_ge_threshold(){
        Assert.assertTrue(HelperFunctions.ge_threshold(5,3,0.1));
        Assert.assertTrue(HelperFunctions.ge_threshold(3,3,0.1));
        Assert.assertTrue(HelperFunctions.ge_threshold(3,2.91,0.1));

        Assert.assertFalse(HelperFunctions.ge_threshold(3,5,0));
        Assert.assertFalse(HelperFunctions.ge_threshold(2.9,3,0.01));
    }


    @Test
    public void test_le_threshold(){
        Assert.assertTrue(HelperFunctions.le_threshold(2,7,0));
        Assert.assertTrue(HelperFunctions.le_threshold(2,2,0.1));
        Assert.assertTrue(HelperFunctions.le_threshold(3.05,3,0.1));

        Assert.assertFalse(HelperFunctions.le_threshold(5,3,0));
        Assert.assertFalse(HelperFunctions.le_threshold(3.1,3,0.01));
    }
}
