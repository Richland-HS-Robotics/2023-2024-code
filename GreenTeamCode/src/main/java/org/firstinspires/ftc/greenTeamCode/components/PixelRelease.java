package org.firstinspires.ftc.greenTeamCode.components;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;

@Config
public class PixelRelease {
    private Servo servo;

    private Telemetry telemetry;

    private static final String LOG_TAG = "Pixel Release";


    public static double HOLD_POSITION = 1;
    public static double OPEN_POSITION = 0.5;


    private boolean disabled = false;

    public PixelRelease(SimplerHardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;

        try{
            servo = hardwareMap.get(Servo.class,"pixelReleaseServo");
        }catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find servo. Disabling airplane launcher.");
            telemetry.addLine("Cannot find servo. Disabling airplane launcher");
            disabled = true;
        }
    }


    public void release(){
        if(!disabled){
            this.servo.setPosition(OPEN_POSITION);
        }
    }

    public void hold(){
        if (!disabled){
            this.servo.setPosition(HOLD_POSITION);
        }
    }
}
