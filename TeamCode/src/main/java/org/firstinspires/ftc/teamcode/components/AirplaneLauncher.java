package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@Config
/**
 * A class for managing the airplane launcher.
 */
public class AirplaneLauncher {
    private Telemetry telemetry;


    private Servo servo;

    private static final String LOG_TAG = "AirplaneLauncher";

    public static double SERVO_RELEASE_POSITION = 0.3;
    public static double SERVO_HOLD_POSITION = 0.6;

    private boolean hasLaunched = false;
    private boolean disabled = false;

    public AirplaneLauncher(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry){
        this.telemetry = telemetry;
        try{
            servo = hardwareMap.get(Servo.class,"airplaneServo");
        }catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find servo. Disabling airplane launcher.");
            telemetry.addLine("Cannot find servo. Disabling airplane launcher");
            disabled = true;
        }


        if(!disabled){
            this.servo.setPosition(SERVO_HOLD_POSITION);
        }
    }


    public void fire(){
        if(!disabled){
            servo.setPosition(SERVO_RELEASE_POSITION);
        }
    }


    public void unfire(){
        if(!disabled){
            servo.setPosition(SERVO_HOLD_POSITION);
        }
    }


}
