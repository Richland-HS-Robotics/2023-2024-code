package ftc.teamcode;

import org.firstinspires.ftc.teamcode.util.HelperFunctions;
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
}
