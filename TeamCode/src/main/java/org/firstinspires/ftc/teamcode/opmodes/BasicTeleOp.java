package org.firstinspires.ftc.teamcode.opmodes;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.AirplaneLauncher;
import org.firstinspires.ftc.teamcode.components.Hanger;
import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Controller;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Claw;
import org.firstinspires.ftc.teamcode.components.PixelRelease;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "Basic TeleOp")
public class BasicTeleOp extends OpMode {

    private SimplerHardwareMap simplerHardwareMap;
    private DriveTrain driveTrain;

    private Controller controller;
    private LinearSlide linearSlide;
    private Claw intake;
    private AirplaneLauncher airplaneLauncher;
    private Hanger hanger;
    private PixelRelease pixelRelease;

    private DriveTrain.DriveMode driveMode;

    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();



    @Override
    public void init() {
        this.simplerHardwareMap = new RealSimplerHardwareMap(this.hardwareMap);

        this.intake = new Claw(simplerHardwareMap,telemetry);
        this.linearSlide = new LinearSlide(simplerHardwareMap,telemetry);
        this.airplaneLauncher = new AirplaneLauncher(simplerHardwareMap,telemetry);
        this.driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
        this.hanger = new Hanger(simplerHardwareMap,telemetry);
        this.pixelRelease = new PixelRelease(simplerHardwareMap,telemetry);
        this.controller = new Controller(telemetry);

        this.driveMode = DriveTrain.DriveMode.ROBOT_CENTRIC;
        this.driveTrain.setPose(new Pose2d(0,0,0));

    }

    @Override
    public void loop() {

        controller.update(gamepad1,gamepad2);
        drive();
        //intake();
        airplane();
        pixelGrab();
        arm();
        hanger();
        runActions();
    }


    private void arm(){


        if(controller.overridePressed()) {
            linearSlide.manualPower(controller.linearSlideInOut());
        }else{
            linearSlide.manualPowerClamped(controller.linearSlideInOut());
        }
        // if(controller.linearSlideInOut() > 0){
        //     linearSlide.setSlidePosition(0.98);
        // }else{
        //     linearSlide.setSlidePosition(0);
        // }


        linearSlide.setGrabClaw(controller.clawGrab());
        linearSlide.setSubArm(controller.slideSubArm());
    }


    private void intake(){
        //intake.moveMotor(controller.intakeUpDown());

        if(controller.intakeUpDown() > 0){
            intake.raiseClaw();
        }
        if (controller.intakeUpDown() < 0) {
            intake.lowerClaw();
        }

        if(controller.clawGrab()){
            intake.closeClaw();
        }else{
            intake.openClaw();
        }
    }

    private void pixelGrab(){
        if(gamepad1.a){
            pixelRelease.release();
        }else{
            pixelRelease.hold();
        }
    }


    private boolean firedPlane = false;

    private void airplane(){
        if(controller.launchAirplane()){
            airplaneLauncher.fire();
        }else{
            airplaneLauncher.unfire();
        }

        // if (controller.launchAirplane() && !firedPlane){
        //     launchAirplane();
        // }
    }


    private void launchAirplane(){
        Pose2d currentPose = driveTrain.pose;



        Action traj = driveTrain.actionBuilder(currentPose)
                .turnTo(-Math.PI/2)
                //.splineTo(new Vector2d(currentPose.position.x,currentPose.position.y),-Math.PI/2)
                .build();

        runningActions.add(new SequentialAction(
                traj,
                telemetryPacket -> {
                    airplaneLauncher.fire();
                    firedPlane = true;

                    telemetryPacket.put("Position",driveTrain.pose);
                    return false;
                }
        ));
    }


    private void drive(){
        driveTrain.updatePoseEstimate();

        if(controller.snapToPosition()){
            driveTrain.constrainedDrive(controller.movementControl());
            //driveTrain.snapToAngleBetter(controller.movementControl());
        }else{
            driveTrain.teleOpDrive(controller.movementControl(),this.driveMode, controller.movementSpeed());
        }

        if(controller.robotCentric()){
            driveMode = DriveTrain.DriveMode.ROBOT_CENTRIC;
        }
        if(controller.fieldCentric()){
            driveMode = DriveTrain.DriveMode.FIELD_CENTRIC;
        }

    }

    private void hanger(){
        hanger.setArmPower(controller.hangArm());
        hanger.setWinchPower(controller.hangWinch());
    }


    private void runActions(){
        List<Action> newActions = new ArrayList<>();
        TelemetryPacket packet = new TelemetryPacket();

        for (Action action: runningActions){
            action.preview(packet.fieldOverlay());
            if(action.run(packet)){
                newActions.add(action);

            }
        }
        runningActions = newActions;
        dash.sendTelemetryPacket(packet);
    }
}
