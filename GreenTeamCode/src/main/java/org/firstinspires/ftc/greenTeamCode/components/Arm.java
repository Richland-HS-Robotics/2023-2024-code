package org.firstinspires.ftc.greenTeamCode.components;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;

@Config
public class Arm {

    private Servo armServo;
    private Servo clawServo;

    private boolean disabled = false;

    private static final String LOG_TAG = "Arm";

    private Telemetry telemetry;

    public static double CLAW_CLOSE_POSITION = 0.45;
    public static double CLAW_OPEN_POSITION = 1;

    public static double ARM_DOWN_POSITION = 1;
    public static double ARM_MEDIUM_POSITION = 0.75;
    public static double ARM_UP_POSITION = 0;

    public enum Position{
        DOWN,
        UP,
        FOLDED
    }
    public Arm(SimplerHardwareMap hardwareMap, Telemetry telemetry){
        try{
            armServo = hardwareMap.get(Servo.class,"armServo");
            clawServo = hardwareMap.get(Servo.class, "clawServo");
        }catch (Throwable T){
            String message ="Cannot find all motors and servos. Disabling Arm";
            Log.e(LOG_TAG, message);
            telemetry.addData(LOG_TAG,message);
            this.disabled = true;
        }

        this.telemetry = telemetry;
    }


    public void setClawGrab(boolean grab){
        if(!disabled){
            if(grab){
                clawServo.setPosition(CLAW_CLOSE_POSITION);
            }else{
                clawServo.setPosition(CLAW_OPEN_POSITION);
            }
        }
    }


    public void setArm(Position pos){
        if(!disabled){
            switch (pos){
                case DOWN:
                    armServo.setPosition(ARM_DOWN_POSITION);
                    break;
                case UP:
                    armServo.setPosition(ARM_MEDIUM_POSITION);
                    break;
                case FOLDED:
                    armServo.setPosition(ARM_UP_POSITION);
                    break;
            }
        }
    }
}
