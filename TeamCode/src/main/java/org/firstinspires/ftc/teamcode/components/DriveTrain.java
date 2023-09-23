package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveTrain {
    private DcMotor leftFront;
    private DcMotor rightFront;
    private  DcMotor leftRear;
    private DcMotor rightRear;

    private Telemetry telemetry;

    private static final String LOG_TAG = "DriveTrain";
    private static final double STRAFE_COEFFICIENT = 1.1;

    public DriveTrain(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry ){
        try{
            this.leftFront = hardwareMap.get(DcMotor.class, "leftFront");
            this.rightFront = hardwareMap.get(DcMotor.class, "rightFront");
            this.leftRear = hardwareMap.get(DcMotor.class, "leftRear");
            this.rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        } catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find all drive motors. Disabling drivetrain.");
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
        // See https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
        // for mecanum drive guide.

        // Set y, restricting it between -1 and 1.
        double y = forward;
        if(y>1){ y=1; }
        else if(y<-1){ y=-1;}

        // Set x, restricting it between -1 and 1
        double x = strafe;
        if (x>1){ x=1; }
        else if(x<-1){ x = -1; }
        x *= STRAFE_COEFFICIENT; // This helps fix issues with wheels slipping

        double rot = turn;
        if(rot > 1){rot=1;}
        else if(rot < -1){rot=-1;}

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

}
