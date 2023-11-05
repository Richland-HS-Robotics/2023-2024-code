package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@Config
public class Intake {
    private Telemetry telemetry;
    private DcMotorEx theMotor;
    private Servo servo;

    private static final String LOG_TAG = "Intake";
    public static double SERVO_CLOSE_POSITION = 0.5;
    public static double SERVO_OPEN_POSITION = 0.2;

    private boolean disabled = false;


    public Intake(@NonNull SimplerHardwareMap hardwareMap,@NonNull Telemetry telemetry){
        try{
            theMotor = hardwareMap.get(DcMotorEx.class,"intakeMotor");
            servo = hardwareMap.get(Servo.class,"clawServo");
        }catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find all motors and servos. Disabling intake");
            this.disabled = true;
        }
        this.telemetry = telemetry;

        if(!disabled){
            theMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }


    public void openClaw(){
        if(!disabled){
            servo.setPosition(SERVO_OPEN_POSITION);
        }
    }

    public void closeClaw(){
        if(!disabled){
            servo.setPosition(SERVO_CLOSE_POSITION);
        }
    }


    public void moveMotor(double direction){

        if(!disabled){
            telemetry.addData("Controller test: ",direction);
            theMotor.setPower(direction/3.0);
        }
    }


}
