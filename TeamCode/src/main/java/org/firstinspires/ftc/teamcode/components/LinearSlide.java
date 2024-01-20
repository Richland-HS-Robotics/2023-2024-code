package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.HelperFunctions;
import org.firstinspires.ftc.teamcode.util.PIDFController;
import org.firstinspires.ftc.teamcode.util.Pair;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@Config
/**
 * A class for managing the linear slide and pixel cartridge.
 */
public class LinearSlide {

    private Telemetry telemetry;

    private DcMotorEx motor;
    private Servo grabServo;
    private Servo subArmServo;


    private static final String LOG_TAG = "LinearSlide";

    private boolean disabled = false;

    public static double GRAB_SERVO_OPEN_POSITION = 0.09;
    public static double GRAB_SERVO_CLOSED_POSITION = 0.55;
    public static double ARM_SERVO_UP_POSITION = 0.7;
    public static double ARM_SERVO_DOWN_POSITION = 0.0;

    public static double kG = 0;
    public static double kP = 0.001;
    public static double kI = 0;
    public static double kD = 0;


    /**
     * Number of encoder ticks for 1 rotation of the motor(Rev Hex Motor, 20:1).
     * See <a href="https://docs.revrobotics.com/duo-control/sensors/encoders/motor-based-encoders">Rev Documentation</a>
     */
    public static int TICKS_PER_ROTATION = 560;

    /**
     * The maximum number of ticks the linear slide can travel.
     * TODO Get a real value for this
     */
    public static int MAX_TICKS = 4*TICKS_PER_ROTATION;

    private int currentTargetTicks = 0;

    public LinearSlide(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry){

        try{
            motor = hardwareMap.get(DcMotorEx.class,"linearSlideMotor");
            grabServo = hardwareMap.get(Servo.class,"linearSlideGrabServo");
            subArmServo = hardwareMap.get(Servo.class,"linearSlideSubArmServo");
        }catch(Throwable T){
            Log.e(LOG_TAG,"Cannot find all motors and servos. Disabling linearSlide");
            this.disabled = true;
        }


        if(!disabled){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
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
     * Get the slide's current position.
     * @return A percent between 0 and 1. 0 means the slide is all the way down,
     * 1 means it is all the way up.
     */
    public double getSlidePosition(){
       if(disabled){
           return 0;
       }

        return ((double) motor.getCurrentPosition())/MAX_TICKS;
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
    @Deprecated
    public void manualPower(double power){
        if(!disabled){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPower(power);
        }
    }


    /**
     * Manually power the linear slide, but do not go above or below the limits.
     * @param power The power to give the motor
     */
    public void manualPowerClamped(double power){
        // int ticksToAdd = (int) power*100;


        // int ticks = HelperFunctions.clamp(
        //         motor.getCurrentPosition() + ticksToAdd,
        //         0,
        //         MAX_TICKS
        // );


        // motor.setTargetPosition(ticks);
        // motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // motor.setPower(power);

        if(motor.getCurrentPosition() > MAX_TICKS || motor.getCurrentPosition() < 0){
            motor.setTargetPosition(HelperFunctions.clamp(motor.getCurrentPosition(),0,MAX_TICKS));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.5);
        }else{
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPower(power);
        }
    }


    /**
     * Set the grab state of the claw.
     * @param grab Whether to grab or not. true means grab, false means don't grab.
     */
    public void setGrabClaw(boolean grab){
        if(!disabled){
            if(grab){
                grabServo.setPosition(GRAB_SERVO_CLOSED_POSITION);
            }else{
                grabServo.setPosition(GRAB_SERVO_OPEN_POSITION);
            }
        }
    }

    public Action closeClaw(){
        return telemetryPacket -> {
            grabServo.setPosition(GRAB_SERVO_CLOSED_POSITION);
            return false;
        };
    }

    public Action openClaw(){
        return telemetryPacket -> {
            grabServo.setPosition(GRAB_SERVO_OPEN_POSITION);
            return false;
        };
    }

    public Action raiseSubArm(){
        return telemetryPacket -> {
            subArmServo.setPosition(ARM_SERVO_UP_POSITION);
            return false;
        };
    }

    public Action lowerSubArm(){
        return telemetryPacket -> {
            subArmServo.setPosition(ARM_SERVO_DOWN_POSITION);
            return false;
        };
    }


    public Action setSlideToPosition(double position){
        return new MoveSlideAction(position);
    }

    public class MoveSlideAction implements Action {
        private PIDFController pid;
        private ElapsedTime time;

        /**
         * Create a new MoveSlideAction.
         * @param targetPosition The percentage of extension, from 0 to 1
         */
        public MoveSlideAction(double targetPosition){
            pid = new PIDFController(
                    new PIDFController.PIDCoefficients(kP,kI,kD),
                    0,
                    0,
                    0,
                    (pos,vel) -> kG
            );

            pid.targetPosition = targetPosition * MAX_TICKS;
            pid.targetVelocity = 0;
            pid.targetAcceleration = 0;

            time = new ElapsedTime();
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            double power = pid.update(motor.getCurrentPosition());

            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPower(power);


            return !(time.seconds() > 7);
        }
    }

    /**
     * Set the sub arm position.
     * @param up Whether or not the arm should be up.
     */
    public void setSubArm(boolean up){
        if(!disabled){
            if(up){
                subArmServo.setPosition(ARM_SERVO_UP_POSITION);
            }else{
                subArmServo.setPosition(ARM_SERVO_DOWN_POSITION);
            }
        }
    }


}


