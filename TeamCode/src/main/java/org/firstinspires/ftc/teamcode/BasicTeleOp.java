package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.AirplaneLauncher;
import org.firstinspires.ftc.teamcode.components.Arm;
import org.firstinspires.ftc.teamcode.components.Controller;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.Triple;

@TeleOp(name = "Basic TeleOp")
public class BasicTeleOp extends OpMode {

    private SimplerHardwareMap simplerHardwareMap;
    private DriveTrain driveTrain;

    private Controller controller;
    private Arm arm;
    private Intake intake;
    private AirplaneLauncher airplaneLauncher;

    private DriveTrain.DriveMode driveMode;

    @Override
    public void init() {
        this.simplerHardwareMap = new RealSimplerHardwareMap(this.hardwareMap);

        this.intake = new Intake(simplerHardwareMap,telemetry);
        this.arm = new Arm(simplerHardwareMap,telemetry);
        this.airplaneLauncher = new AirplaneLauncher(simplerHardwareMap,telemetry);
        this.driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
        this.controller = new Controller(telemetry);

        this.driveMode = DriveTrain.DriveMode.ROBOT_CENTRIC;

    }

    @Override
    public void loop() {

        controller.update(gamepad1,gamepad2);
        drive();
        intake();
        airplane();
        arm();
    }


    private void arm(){
        arm.doMotor(controller.linearSlideInOut());

        arm.doPixels(controller.releasePixels());
    }


    private void intake(){
        intake.moveMotor(controller.intakeUpDown());

        if(controller.clawGrab()){
            intake.closeClaw();
        }else{
            intake.openClaw();
        }
    }


    private void airplane(){
        if(controller.launchAirplane()){
            airplaneLauncher.fire();
        }else{
            airplaneLauncher.unfire();
        }
    }


    private void drive(){
        driveTrain.teleOpDrive(controller.movementControl(),this.driveMode, controller.movementSlownessFactor());
    }
}
