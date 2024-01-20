package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.components.Controller;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@TeleOp
public class BasicTeleOpWithActions extends LinearOpMode {

    public DriveTrain driveTrain;
    public Controller controller;

    public SimplerHardwareMap simplerHardwareMap;

    @Override
    public void runOpMode() throws InterruptedException {
        this.simplerHardwareMap = new RealSimplerHardwareMap(this.hardwareMap);
        driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
        driveTrain.pose = new Pose2d(0,0,Math.toRadians(45));

        driveTrain.snapToAngle();

//        Actions.runBlocking(turn);

    }
}
