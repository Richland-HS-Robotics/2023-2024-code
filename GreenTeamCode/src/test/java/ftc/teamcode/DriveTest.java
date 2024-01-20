package ftc.greenTeamCode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import ftc.greenTeamCode.fakes.FakeDcMotor;
import ftc.greenTeamCode.fakes.FakeExtendedDcMotor;
import ftc.greenTeamCode.fakes.FakeHardwareMap;
import ftc.greenTeamCode.fakes.FakeIMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.components.DriveTrain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DriveTest {

    private FakeExtendedDcMotor leftRear;
    private FakeExtendedDcMotor leftFront;
    private FakeExtendedDcMotor rightRear;
    private  FakeExtendedDcMotor rightFront;
    private FakeHardwareMap hardwareMap;
    private FakeIMU imu;
    private Telemetry telemetry;

    private DriveTrain drivetrain;
    @Before
    public void before(){
        hardwareMap = new FakeHardwareMap();
        leftRear = new FakeExtendedDcMotor();
        leftFront = new FakeExtendedDcMotor();
        rightRear = new FakeExtendedDcMotor();
        rightFront = new FakeExtendedDcMotor();
        imu = new FakeIMU();

        hardwareMap.addDevice("leftRear",leftRear);
        hardwareMap.addDevice("leftFront",leftFront);
        hardwareMap.addDevice("rightFront",rightFront);
        hardwareMap.addDevice("rightRear",rightRear);
        hardwareMap.addDevice("imu",imu);


        drivetrain = new DriveTrain(hardwareMap,telemetry);
    }


    @Test
    public void testLocalDriveForward(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(1,0),0));

        Assert.assertEquals(1,leftFront.getPower(),0.01);
        Assert.assertEquals(1,rightFront.getPower(),0.01);
        Assert.assertEquals(1,leftRear.getPower(),0.01);
        Assert.assertEquals(1,rightRear.getPower(),0.01);
    }


    @Test
    public void testLocalDriveBackward(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(-1,0),0));

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);
    }

    @Test
    public void testLocalDriveRight(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(0,1),0));

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);
    }

    @Test
    public void testLocalDriveLeft(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(0,-1),0));


        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);
    }



    @Test
    public void testRotateLeft(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(0,0),-1));

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);


        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(0,0),-1));
        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);

    }


    @Test
    public void testRotateRight(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(0,0),1));

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);


        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(0,0),1));

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);
    }


    @Test
    public void testNoMovement(){
        drivetrain.localDrive(new PoseVelocity2d(new Vector2d(0,0),0));

        Assert.assertEquals(0,leftRear.getPower(),0.1);
        Assert.assertEquals(0,rightRear.getPower(),0.1);
        Assert.assertEquals(0,leftFront.getPower(),0.1);
        Assert.assertEquals(0,rightFront.getPower(),0.1);


        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(0,0),0));
        Assert.assertEquals(0,leftRear.getPower(),0.1);
        Assert.assertEquals(0,rightRear.getPower(),0.1);
        Assert.assertEquals(0,leftFront.getPower(),0.1);
        Assert.assertEquals(0,rightFront.getPower(),0.1);
    }





    @Test
    public void testGlobalMoveForward(){
        imu.setYaw(0);
        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(1,0),0));

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);

        imu.setYaw(Math.PI);
        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(1,0),0));

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);


        imu.setYaw(Math.PI/2);
        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(1,0),0));

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);


        imu.setYaw(-Math.PI/2);
        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(1,0),0));

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);

        imu.setYaw(Math.PI/4);
        drivetrain.globalDrive(new PoseVelocity2d(new Vector2d(1,0),0));
        Assert.assertEquals(0,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(0,rightFront.getPower(),0.1);

    }





}
