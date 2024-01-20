package ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.BasicTeleOp;
import org.firstinspires.ftc.teamcode.register.CustomOpMode;
import org.firstinspires.ftc.teamcode.register.CustomTeleOp;
import org.firstinspires.ftc.teamcode.register.TestClass;
import org.junit.Before;
import org.junit.Test;

import ftc.teamcode.fakes.FakeExtendedDcMotor;
import ftc.teamcode.fakes.FakeHardwareMap;
import ftc.teamcode.fakes.FakeIMU;
import ftc.teamcode.fakes.FakeServo;
import ftc.teamcode.fakes.FakeTelemetry;

public class OpmodeTest {
    FakeExtendedDcMotor leftFront = new FakeExtendedDcMotor();
    FakeExtendedDcMotor leftRear = new FakeExtendedDcMotor();
    FakeExtendedDcMotor rightFront = new FakeExtendedDcMotor();
    FakeExtendedDcMotor rightRear = new FakeExtendedDcMotor();
    FakeIMU imu = new FakeIMU();

    FakeExtendedDcMotor armMotor = new FakeExtendedDcMotor();
    FakeServo armLeftServo = new FakeServo();
    FakeServo armRightServo = new FakeServo();

    FakeExtendedDcMotor clawMotor = new FakeExtendedDcMotor();
    FakeServo clawServo = new FakeServo();

    FakeServo airplaneServo = new FakeServo();



    FakeHardwareMap hardwareMap;
    Telemetry telemetry;

    @Before
    public void before(){
        hardwareMap = new FakeHardwareMap();
        hardwareMap.addDevice("leftFront",leftFront);
        hardwareMap.addDevice("leftRear",leftRear);
        hardwareMap.addDevice("rightFront",rightFront);
        hardwareMap.addDevice("rightRear",rightRear);
        hardwareMap.addDevice("imu",imu);
        
        hardwareMap.addDevice("armMotor",armMotor);
        hardwareMap.addDevice("armLeftServo",armLeftServo);
        hardwareMap.addDevice("armRightServo",armRightServo);

        hardwareMap.addDevice("clawMotor",clawMotor);
        hardwareMap.addDevice("clawServo",clawServo);

        hardwareMap.addDevice("airplaneServo",airplaneServo);

        this.telemetry = new FakeTelemetry();
    }


    @Test
    public void testThing(){
        CustomOpMode opMode = new TestClass(hardwareMap,telemetry);

        opMode.init();
        opMode.loop();
    }



}
