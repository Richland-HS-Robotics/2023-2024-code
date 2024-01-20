package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.HelperFunctions;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

/**
 * A class for managing the intake, including the claw and intake raising/lowering.
 */
@Config
public class Claw {
    private Telemetry telemetry;
    private DcMotorEx clawMotor;
    private Servo clawServo;

    private static final String LOG_TAG = "Claw";
    public static double SERVO_CLOSE_POSITION = 0.5;
    public static double SERVO_OPEN_POSITION = 0.2;

    private boolean disabled = false;

    // Core Hex Motor
    // There are 288 encoder ticks for each rotation of the output shaft.
    public static final int TICKS_PER_ROTATION = 288;

    /**
     * The rotational range of the linearSlide, in radians.
     * Currently at 90 degrees; TODO get a better measurement
     */
    public static final double ARM_ROTATION_RANGE = Math.PI/2;


    /**
     * The current set position of the linearSlide, in radians. A position
     * of 0 means the linearSlide is on the ground, a position of ARM_ROTATION_RANGE means it is
     * fully up.
     */
    private double currentTargetPosition = ARM_ROTATION_RANGE;


    public Claw(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry){
        try{
            clawMotor = hardwareMap.get(DcMotorEx.class,"clawMotor");
            clawServo = hardwareMap.get(Servo.class,"clawServo");
        }catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find all motors and servos. Disabling intake");
            this.disabled = true;
        }
        this.telemetry = telemetry;

        if(!disabled){
            //clawMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            clawMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            clawMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        }

        //telemetry.addData("PID: ",
        //clawMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION));
    }


    /**
     * Open the claw
     */
    public void openClaw(){
        if(!disabled){
            clawServo.setPosition(SERVO_OPEN_POSITION);
        }
    }

    /**
     * Close the claw
     */
    public void closeClaw(){
        if(!disabled){
            clawServo.setPosition(SERVO_CLOSE_POSITION);
        }
    }


    /**
     * Manually move the motor with a raw power.
     * @param direction The power to give the motor
     */
    public void moveMotor(double direction){

        if(!disabled){
            telemetry.addData("Controller test: ",direction);
            clawMotor.setPower(direction/3.0);
        }
    }


    /**
     * Convert an angle to a number of ticks.
     * The input angle and output number of ticks are both <em>relative</em>, not absolute;
     * i.e. they are not a position on the circle, but a length.
     * @param angle The angle, in radians.
     */
    public int angleToTicks(double angle){
        return (int) (angle * TICKS_PER_ROTATION/(2*Math.PI));
    }


    /**
     * Convert ticks to a relative angle. Both the input and output
     * are relative, not absolute. They measure distance, not position.
     * @param ticks The number of motor ticks.
     * @return The angle covered by the ticks.
     */
    public double ticksToAngle(int ticks) {
        return (ticks * 2*Math.PI)/TICKS_PER_ROTATION;
    }

    /**
     * Moves the claw to a fixed position.
     * @param angle An angle in radians, between 0 and ARM_ROTATION_RANGE. 0 represents
     *              the linearSlide fully down, while ARM_ROTATION_RANGE represents it fully up.
     */
    public void moveToPosition(double angle){
        // Make sure the angle is within range
        double correctedAngle = HelperFunctions.clamp(angle,0,ARM_ROTATION_RANGE);

        // 0 ticks represents the linearSlide fully up; the number of ticks increases
        // as the linearSlide goes down.
        int ticks = angleToTicks(ARM_ROTATION_RANGE) - angleToTicks(correctedAngle);

        clawMotor.setTargetPosition(ticks);
        clawMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        clawMotor.setPower(0.5);

        currentTargetPosition = correctedAngle;
    }

    /**
     * Get the current target position
     * @return The current target claw position.
     */
    public double getCurrentTargetPosition(){
        return currentTargetPosition;
    }

    /**
     * Lower the claw all the way.
     */
    public void lowerClaw(){
        moveToPosition(0);
    }

    /**
     * Raise the claw all the way.
     */
    public void raiseClaw(){
        moveToPosition(ARM_ROTATION_RANGE);
    }

    public void setPIDCoefficients(double p, double i, double d,double f){
        clawMotor.setPIDFCoefficients(
                DcMotor.RunMode.RUN_TO_POSITION,
                new PIDFCoefficients(p,i,d,f)
        );
    }


    /**
     * Get the current position of the linearSlide, in radians.
     * @return The position, in radians. 0 means the linearSlide is fully down, while ARM_ROTATION_RANGE
     *         means it is all the way up.
     */
    public double getCurrentPosition(){
        return this.ticksToAngle(clawMotor.getCurrentPosition());
    }



}
