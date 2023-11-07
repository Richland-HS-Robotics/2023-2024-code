package ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Arm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ftc.teamcode.fakes.FakeExtendedDcMotor;
import ftc.teamcode.fakes.FakeHardwareMap;
import ftc.teamcode.fakes.FakeServo;

public class LinearSlideTest {

    private Servo leftServo;
    private Servo rightServo;
    private DcMotorEx motor;
    private FakeHardwareMap hardwareMap;

    private Arm arm;

    private Telemetry telemetry;


    @Before
    public void before(){

        leftServo = new FakeServo();
        rightServo = new FakeServo();
        motor = new FakeExtendedDcMotor();
        hardwareMap = new FakeHardwareMap();

        hardwareMap.addDevice("armMotor",motor);
        hardwareMap.addDevice("armLeftServo",leftServo);
        hardwareMap.addDevice("armRightServo",rightServo);


        arm = new Arm(hardwareMap,telemetry);
    }


    @Test
    public void testMovingArm(){
        arm.setSlidePosition(1);
        Assert.assertEquals(Arm.MAX_TICKS,arm.getCurrentTargetTicks());
        Assert.assertEquals(Arm.MAX_TICKS,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());


        arm.setSlidePosition(0);
        Assert.assertEquals(0,arm.getCurrentTargetTicks());
        Assert.assertEquals(0,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());


        arm.setSlidePosition(-5);
        Assert.assertEquals(0,arm.getCurrentTargetTicks());
        Assert.assertEquals(0,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());



        arm.setSlidePosition(103);
        Assert.assertEquals(Arm.MAX_TICKS,arm.getCurrentTargetTicks());
        Assert.assertEquals(Arm.MAX_TICKS,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());
    }

}
