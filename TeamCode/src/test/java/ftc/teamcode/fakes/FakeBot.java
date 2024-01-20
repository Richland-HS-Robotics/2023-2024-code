package ftc.teamcode.fakes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class FakeBot {
    FakeHardwareMap fakeHardwareMap;


    FakeExtendedDcMotor leftFront = new FakeExtendedDcMotor();
    FakeExtendedDcMotor rightFront = new FakeExtendedDcMotor();
    FakeExtendedDcMotor leftRear = new FakeExtendedDcMotor();
    FakeExtendedDcMotor rightRear = new FakeExtendedDcMotor();
    FakeIMU imu = new FakeIMU();

    Pose2d pose;

    public FakeBot(Pose2d pose){
        fakeHardwareMap = new FakeHardwareMap();
        fakeHardwareMap.addDevice("leftFront",leftFront);
        fakeHardwareMap.addDevice("rightFront",rightFront);
        fakeHardwareMap.addDevice("leftRear",leftRear);
        fakeHardwareMap.addDevice("rightRear",rightRear);

        fakeHardwareMap.addDevice("imu",imu);

        this.pose = pose;

    }
    public FakeBot(){
        this(new Pose2d(0,0,0));
    }


    public void update(){
        double dt = 0.1;

        leftFront.tick(dt);
        leftRear.tick(dt);
        rightFront.tick(dt);
        rightRear.tick(dt);

        double dx = (rightRear.getPower() - rightFront.getPower()) / 2;
        double dy = (leftRear.getPower() + rightRear.getPower()) / 2;
        double dr = (leftRear.getPower() - rightFront.getPower()) / 2;

        pose.position.plus(new Vector2d(dx,dy));
        pose.heading.plus(dr);
    }





}
