package ftc.greenTeamCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.components.LinearSlide;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ftc.greenTeamCode.fakes.FakeExtendedDcMotor;
import ftc.greenTeamCode.fakes.FakeHardwareMap;
import ftc.greenTeamCode.fakes.FakeServo;

public class LinearSlideTest {

    private Servo leftServo;
    private Servo rightServo;
    private DcMotorEx motor;
    private FakeHardwareMap hardwareMap;

    private LinearSlide linearSlide;

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


        linearSlide = new LinearSlide(hardwareMap,telemetry);
    }


    @Test
    public void testMovingArm(){
        linearSlide.setSlidePosition(1);
        Assert.assertEquals(LinearSlide.MAX_TICKS, linearSlide.getCurrentTargetTicks());
        Assert.assertEquals(LinearSlide.MAX_TICKS,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());


        linearSlide.setSlidePosition(0);
        Assert.assertEquals(0, linearSlide.getCurrentTargetTicks());
        Assert.assertEquals(0,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());


        linearSlide.setSlidePosition(-5);
        Assert.assertEquals(0, linearSlide.getCurrentTargetTicks());
        Assert.assertEquals(0,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());



        linearSlide.setSlidePosition(103);
        Assert.assertEquals(LinearSlide.MAX_TICKS, linearSlide.getCurrentTargetTicks());
        Assert.assertEquals(LinearSlide.MAX_TICKS,motor.getTargetPosition());
        Assert.assertEquals(DcMotor.RunMode.RUN_TO_POSITION,motor.getMode());
    }

}
