package ftc.greenTeamCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.opmodes.BasicTeleOp;
import org.firstinspires.ftc.greenTeamCode.register.CustomOpMode;
import org.firstinspires.ftc.greenTeamCode.register.CustomTeleOp;
import org.firstinspires.ftc.greenTeamCode.register.TestClass;
import org.junit.Before;
import org.junit.Test;

import ftc.greenTeamCode.fakes.FakeExtendedDcMotor;
import ftc.greenTeamCode.fakes.FakeHardwareMap;
import ftc.greenTeamCode.fakes.FakeIMU;
import ftc.greenTeamCode.fakes.FakeServo;
import ftc.greenTeamCode.fakes.FakeTelemetry;

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
