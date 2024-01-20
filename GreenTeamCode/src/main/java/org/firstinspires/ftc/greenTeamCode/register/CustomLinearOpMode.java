package org.firstinspires.ftc.greenTeamCode.register;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.components.AirplaneLauncher;
import org.firstinspires.ftc.greenTeamCode.components.Claw;
import org.firstinspires.ftc.greenTeamCode.components.Controller;
import org.firstinspires.ftc.greenTeamCode.components.DriveTrain;
import org.firstinspires.ftc.greenTeamCode.components.LinearSlide;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;

public abstract class CustomLinearOpMode {

    public final DriveTrain driveTrain;
    public final LinearSlide linearSlide;
    public final Claw claw;
    public final AirplaneLauncher airplaneLauncher;
    public final Telemetry telemetry;
    public final Controller controller;
    public final SimplerHardwareMap hardwareMap;
    public CustomLinearOpMode(SimplerHardwareMap hardwareMap,Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.driveTrain = new DriveTrain(hardwareMap,telemetry);
        this.linearSlide = new LinearSlide(hardwareMap,telemetry);
        this.claw = new Claw(hardwareMap,telemetry);
        this.airplaneLauncher = new AirplaneLauncher(hardwareMap,telemetry);
        this.controller = new Controller(telemetry);
    }

    /**
     * Initialize the opmode.
     */
    public abstract void initOpMode();
    public abstract void runOpMode();

}
