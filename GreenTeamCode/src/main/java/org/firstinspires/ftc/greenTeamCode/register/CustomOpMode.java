package org.firstinspires.ftc.greenTeamCode.register;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.components.AirplaneLauncher;
import org.firstinspires.ftc.greenTeamCode.components.LinearSlide;
import org.firstinspires.ftc.greenTeamCode.components.Claw;
import org.firstinspires.ftc.greenTeamCode.components.Controller;
import org.firstinspires.ftc.greenTeamCode.components.DriveTrain;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;

/**
 * A custom opmode. This
 */
public abstract class CustomOpMode {
    public final DriveTrain driveTrain;
    public final LinearSlide linearSlide;
    public final Claw claw;

    public final AirplaneLauncher airplaneLauncher;
    public final Telemetry telemetry;
    public final Controller controller;

    public final SimplerHardwareMap hardwareMap;

    public CustomOpMode(SimplerHardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;
        this.driveTrain = new DriveTrain(hardwareMap,telemetry);
        this.linearSlide = new LinearSlide(hardwareMap,telemetry);
        this.claw = new Claw(hardwareMap,telemetry);
        this.airplaneLauncher = new AirplaneLauncher(hardwareMap,telemetry);
        this.controller = new Controller(telemetry);
        this.hardwareMap = hardwareMap;
    }


    public abstract void init();

    public abstract void loop();


}
