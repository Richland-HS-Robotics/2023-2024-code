package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

public class Hanger {
    private static final String LOG_TAG = "Hanger";

    private DcMotorEx armMotor;
    private DcMotorEx winchMotor;

    private Telemetry telemetry;

    private boolean disabled = false;



    public Hanger(@NonNull SimplerHardwareMap hardwareMap,@NonNull Telemetry telemetry){
        try{
            armMotor = hardwareMap.get(DcMotorEx.class,"hangArmMotor");
            winchMotor = hardwareMap.get(DcMotorEx.class,"hangWinchMotor");
            armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }catch(Throwable T){
            Log.e(LOG_TAG,"Cannot find all motors and servos. Disabling hanger");
            disabled = true;
        }
    }


    /**
     * Set the winch power.
     * @param power A power from -1 to 1
     */
    public void setWinchPower(double power){
        if(!disabled){
            winchMotor.setPower(power);
        }
    }

    /**
     * Set the arm power.
     * @param power A power from -1 to 1
     */
    public void setArmPower(double power){
        if(!disabled){
            armMotor.setPower(power);
        }
    }
}
