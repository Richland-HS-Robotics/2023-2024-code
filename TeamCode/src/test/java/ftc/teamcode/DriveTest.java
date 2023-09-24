package ftc.teamcode;

import ftc.teamcode.fakes.FakeDcMotor;
import ftc.teamcode.fakes.FakeHardwareMap;
import ftc.teamcode.fakes.FakeIMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DriveTest {

    private FakeDcMotor leftRear;
    private FakeDcMotor leftFront;
    private FakeDcMotor rightRear;
    private  FakeDcMotor rightFront;
    private FakeHardwareMap hardwareMap;
    private FakeIMU imu;
    private Telemetry telemetry;

    private DriveTrain drivetrain;
    @Before
    public void before(){
        hardwareMap = new FakeHardwareMap();
        leftRear = new FakeDcMotor();
        leftFront = new FakeDcMotor();
        rightRear = new FakeDcMotor();
        rightFront = new FakeDcMotor();
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
        drivetrain.localDrive(1,0,0);

        Assert.assertEquals(1,leftFront.getPower(),0.01);
        Assert.assertEquals(1,rightFront.getPower(),0.01);
        Assert.assertEquals(1,leftRear.getPower(),0.01);
        Assert.assertEquals(1,rightRear.getPower(),0.01);
    }


    @Test
    public void testLocalDriveBackward(){
        drivetrain.localDrive(-1,0,0);

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);
    }

    @Test
    public void testLocalDriveRight(){
        drivetrain.localDrive(0,1,0);

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);
    }

    @Test
    public void testLocalDriveLeft(){
        drivetrain.localDrive(0,-1,0);


        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);
    }



    @Test
    public void testRotateLeft(){
        drivetrain.localDrive(0,0,-1);

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);


        drivetrain.globalDrive(0,0,-1);
        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);

    }


    @Test
    public void testRotateRight(){
        drivetrain.localDrive(0,0,1);

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);


        drivetrain.globalDrive(0,0,1);

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);
    }


    @Test
    public void testNoMovement(){
        drivetrain.localDrive(0,0,0);

        Assert.assertEquals(0,leftRear.getPower(),0.1);
        Assert.assertEquals(0,rightRear.getPower(),0.1);
        Assert.assertEquals(0,leftFront.getPower(),0.1);
        Assert.assertEquals(0,rightFront.getPower(),0.1);


        drivetrain.globalDrive(0,0,0);
        Assert.assertEquals(0,leftRear.getPower(),0.1);
        Assert.assertEquals(0,rightRear.getPower(),0.1);
        Assert.assertEquals(0,leftFront.getPower(),0.1);
        Assert.assertEquals(0,rightFront.getPower(),0.1);
    }





    @Test
    public void testGlobalMoveForward(){
        imu.setYaw(0);
        drivetrain.globalDrive(1,0,0);

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);

        imu.setYaw(Math.PI);
        drivetrain.globalDrive(1,0,0);

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);


        imu.setYaw(Math.PI/2);
        drivetrain.globalDrive(1,0,0);

        Assert.assertEquals(-1,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(-1,rightFront.getPower(),0.1);


        imu.setYaw(-Math.PI/2);
        drivetrain.globalDrive(1,0,0);

        Assert.assertEquals(1,leftRear.getPower(),0.1);
        Assert.assertEquals(-1,rightRear.getPower(),0.1);
        Assert.assertEquals(-1,leftFront.getPower(),0.1);
        Assert.assertEquals(1,rightFront.getPower(),0.1);

        imu.setYaw(Math.PI/4);
        drivetrain.globalDrive(1,0,0);
        Assert.assertEquals(0,leftRear.getPower(),0.1);
        Assert.assertEquals(1,rightRear.getPower(),0.1);
        Assert.assertEquals(1,leftFront.getPower(),0.1);
        Assert.assertEquals(0,rightFront.getPower(),0.1);

    }





}
