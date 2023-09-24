package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.HelperFunctions;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

public class DriveTrain {
    private DcMotor leftFront;
    private DcMotor rightFront;
    private  DcMotor leftRear;
    private DcMotor rightRear;

    private Telemetry telemetry;
    private IMU imu;
    private boolean disabled = false;

    private static final String LOG_TAG = "DriveTrain";
    private static final double STRAFE_COEFFICIENT = 1.1;

    public DriveTrain(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry ){
        try{
            this.leftFront = hardwareMap.get(DcMotor.class, "leftFront");
            this.rightFront = hardwareMap.get(DcMotor.class, "rightFront");
            this.leftRear = hardwareMap.get(DcMotor.class, "leftRear");
            this.rightRear = hardwareMap.get(DcMotor.class, "rightRear");

            this.imu = hardwareMap.get(IMU.class, "imu");
        } catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find all drive motors and IMU. Disabling drivetrain.");
            this.disabled = true;
        }


        this.telemetry = telemetry;
    }


    /**
     * Drive the robot using local coordinate system.
      * @param forward The forward-backward component.  Between -1 and 1.
     * @param strafe - The left-right component.  Between -1 and 1, 1 is right.
     * @param turn - The rotational component. Between -1 and 1.
     */
    public void localDrive(double forward, double strafe, double turn){
        if(disabled){return;}

        // See https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
        // for mecanum drive guide.

        // restrict inputs between -1 and 1
        double y = HelperFunctions.clamp(forward,-1,1);
        double x = HelperFunctions.clamp(strafe,-1,1) * STRAFE_COEFFICIENT; // This helps fix issues with wheels slipping
        double rot = HelperFunctions.clamp(turn,-1,1);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot),1);

        double leftFrontPower = (y + x + rot) / denominator;
        double leftRearPower = (y - x + rot) / denominator;
        double rightFrontPower = (y - x - rot) / denominator;
        double rightRearPower = (y + x - rot) / denominator;

        this.leftFront.setPower(leftFrontPower);
        this.leftRear.setPower(leftRearPower);
        this.rightFront.setPower(rightFrontPower);
        this.rightRear.setPower(rightRearPower);
    }


    /**
     * Drive the robot using a field-centric coordinate system.
     * @param forward The forward-backward component. Between -1 and 1.
     * @param strafe The left-right component. Between -1 and 1.
     * @param turn The rotational component.
     */
    public void globalDrive(double forward, double strafe, double turn){
        if(disabled){return;}

        double y = HelperFunctions.clamp(forward,-1,1);
        double x = HelperFunctions.clamp(strafe,-1,1);
        double rot = HelperFunctions.clamp(turn,-1,1);

        double heading = this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);


        double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

        rotX *= STRAFE_COEFFICIENT;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rot),1);
        double leftFrontPower = (rotY + rotX + rot) / denominator;
        double leftRearPower = (rotY - rotX + rot) / denominator;
        double rightFrontPower = (rotY - rotX - rot) / denominator;
        double rightRearPower = (rotY + rotX - rot) / denominator;


        this.leftFront.setPower(leftFrontPower);
        this.leftRear.setPower(leftRearPower);
        this.rightFront.setPower(rightFrontPower);
        this.rightRear.setPower(rightRearPower);
    }

}
