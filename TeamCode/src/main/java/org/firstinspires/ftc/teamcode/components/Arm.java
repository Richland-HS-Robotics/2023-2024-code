package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.HelperFunctions;
import org.firstinspires.ftc.teamcode.util.Pair;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@Config
/**
 * A class for managing the linear slide and pixel cartridge.
 */
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


    /**
     * Number of encoder ticks for 1 rotation of the motor(Rev Hex Motor, 20:1).
     * See <a href="https://docs.revrobotics.com/duo-control/sensors/encoders/motor-based-encoders">Rev Documentation</a>
     */
    public static int TICKS_PER_ROTATION = 560;

    /**
     * The maximum number of ticks the linear slide can travel.
     * TODO Get a real value for this
     */
    public static int MAX_TICKS = 3*TICKS_PER_ROTATION;

    private int currentTargetTicks = 0;

    public Arm(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry){

        try{
            motor = hardwareMap.get(DcMotorEx.class,"armMotor");
            leftServo = hardwareMap.get(Servo.class, "armLeftServo");
            rightServo = hardwareMap.get(Servo.class, "armRightServo");
        }catch(Throwable T){
            Log.e(LOG_TAG,"Cannot find all motors and servos. Disabling arm");
            this.disabled = true;
        }


        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;
    }


    /**
     * Set the slide position.
     * @param percent The position to set the slide to, between 0 and 1. 0 means the slide
     *                will be all the way down, 1 means all the way up, and anything in between
     *                means somewhere between up and down.
     */
    public void setSlidePosition(double percent){

        double clampedPercent = HelperFunctions.clamp(percent,0,1);


        int ticks = (int) (clampedPercent * MAX_TICKS);

        motor.setTargetPosition(ticks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.5);


        this.currentTargetTicks = ticks;
    }

    /**
     * Get the current target ticks.
     * @return The current target
     */
    public int getCurrentTargetTicks(){
        return this.currentTargetTicks;
    }



    /**
     * Manually power the linear slide.
     * @param power The power to give the motor
     */
    public void manualPower(double power){
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(power);
    }


    /**
     * Set the position of the left servo.
     * @param open Whether to open or close the servo. True is open, false is closed.
     */
    public void setLeftServo(boolean open){
        if(open){
            leftServo.setPosition(LEFT_SERVO_OPEN_POSITION);
        }else{
            leftServo.setPosition(LEFT_SERVO_CLOSED_POSITION);
        }
    }


    /**
     * Set the position of the right servo.
     * @param open Whether to open or close the servo. True is open, false is closed.
     */
    public void setRightServo(boolean open){
        if(open){
            rightServo.setPosition(RIGHT_SERVO_OPEN_POSITION);
        }else{
            rightServo.setPosition(RIGHT_SERVO_CLOSED_POSITION);
        }
    }


    /**
     * Set both servos based on a pair of booleans.
     * @param openClosed A pair of booleans. The first represents the left servo, and the second
     *                   represents the right.
     * @see Arm#setLeftServo(boolean)
     * @see Arm#setRightServo(boolean)
     */
    public void setBothServos(Pair<Boolean,Boolean> openClosed){
        setLeftServo(openClosed.x);
        setRightServo(openClosed.y);
    }
}


