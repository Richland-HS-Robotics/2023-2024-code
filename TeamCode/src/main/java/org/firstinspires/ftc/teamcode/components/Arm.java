package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Pair;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@Config
public class Arm {

    private Telemetry telemetry;

    private DcMotorEx motor;
    private Servo leftServo;
    private Servo rightServo;


    private static final String LOG_TAG = "Arm";

    private boolean disabled = false;

    public static double LEFT_SERVO_OPEN_POSITION=0.5;
    public static double RIGHT_SERVO_OPEN_POSITION=0.1;
    public static double LEFT_SERVO_CLOSED_POSITION=0.0;
    public static double RIGHT_SERVO_CLOSED_POSITION=0.3;


    public Arm(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry){

        try{
            motor = hardwareMap.get(DcMotorEx.class,"armMotor");
            leftServo = hardwareMap.get(Servo.class, "armLeftServo");
            rightServo = hardwareMap.get(Servo.class, "armRightServo");
        }catch(Throwable T){
            Log.e(LOG_TAG,"Cannot find all motors and servos. Disabling arm");
            this.disabled = true;
        }

        this.telemetry = telemetry;
    }



    public void tick(double direction, Pair<Boolean,Boolean> release){
        if(direction > 0){
            telemetry.addLine("Moving arm up");
        } else if (direction < 0) {
            telemetry.addLine("Moving arm down");
        }

        boolean releaseLeftServo = release.x;
        boolean releaseRightServo = release.y;

        if(releaseLeftServo) {
            telemetry.addLine("Releasing left pixel");
        }
        if(releaseRightServo){
            telemetry.addLine("Releasing right pixel");
        }
    }



    public void doMotor(double dir){
        motor.setPower(dir);
    }


    public void doPixels(Pair<Boolean,Boolean> openClosed){
        if(openClosed.x){
            leftServo.setPosition(LEFT_SERVO_OPEN_POSITION);
        }else{
            leftServo.setPosition(LEFT_SERVO_CLOSED_POSITION);
        }

        if(openClosed.y){
            rightServo.setPosition(RIGHT_SERVO_OPEN_POSITION);
        }else{
            rightServo.setPosition(RIGHT_SERVO_CLOSED_POSITION);
        }

    }
}


