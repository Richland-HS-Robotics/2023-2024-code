package ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.junit.Assert;
import org.firstinspires.ftc.teamcode.components.Claw;
import org.junit.Before;
import org.junit.Test;

import ftc.teamcode.fakes.FakeExtendedDcMotor;
import ftc.teamcode.fakes.FakeHardwareMap;
import ftc.teamcode.fakes.FakeServo;

public class ClawTest {

    private Claw claw;
    private Servo servo;
    private DcMotorEx motor;

    private Telemetry telemetry;


    private FakeHardwareMap hardwareMap;

    @Before
    public void before(){
        motor = new FakeExtendedDcMotor();
        servo = new FakeServo();
        hardwareMap = new FakeHardwareMap();

        hardwareMap.addDevice("clawServo",servo);
        hardwareMap.addDevice("clawMotor",motor);

        claw = new Claw(hardwareMap,telemetry);

    }
    @Test
    public void testAngleToTicks(){
        Assert.assertEquals(288,claw.angleToTicks(2*Math.PI));
        Assert.assertEquals(576,claw.angleToTicks(4*Math.PI));
        Assert.assertEquals(0,claw.angleToTicks(0));
    }



    @Test
    public void testArmMovement(){
        claw.moveToPosition(0);
        Assert.assertEquals(0,claw.getCurrentTargetPosition(),0.0001);
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());
        Assert.assertEquals(claw.angleToTicks(Claw.ARM_ROTATION_RANGE),motor.getTargetPosition());


        claw.moveToPosition(Claw.ARM_ROTATION_RANGE);
        Assert.assertEquals(Claw.ARM_ROTATION_RANGE,claw.getCurrentTargetPosition(),0.0001);
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());
        Assert.assertEquals(0,motor.getTargetPosition());




        claw.moveToPosition(Claw.ARM_ROTATION_RANGE + 50);
        Assert.assertEquals(Claw.ARM_ROTATION_RANGE,claw.getCurrentTargetPosition(),0.0001);
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());
        Assert.assertEquals(0,motor.getTargetPosition());



        claw.moveToPosition(-30.237);
        Assert.assertEquals(0,claw.getCurrentTargetPosition(),0.0001);
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());
        Assert.assertEquals(claw.angleToTicks(Claw.ARM_ROTATION_RANGE),motor.getTargetPosition());
    }



}
