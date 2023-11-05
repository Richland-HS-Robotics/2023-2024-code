package org.firstinspires.ftc.teamcode;


import static java.lang.Math.PI;

import android.util.Size;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;
import org.firstinspires.ftc.teamcode.vision.TeamPropProcessor;
import org.firstinspires.ftc.vision.VisionPortal;



@Autonomous
public class BasicAuto extends LinearOpMode {

    private VisionPortal visionPortal;
    private TeamPropProcessor teamPropProcessor;

    private DriveTrain driveTrain;

    public static Pose2d BLUE_NEAR_BACKDROP_INIT = new Pose2d(12,60,3*PI/2);
    public static Pose2d BLUE_FAR_BACKDROP_INIT = new Pose2d(-36,60,3*PI/2);
    public static Pose2d RED_NEAR_BACKDROP_INIT = new Pose2d(12,-60,PI/2);
    public static Pose2d RED_FAR_BACKDROP_INIT = new Pose2d(-36,-60,PI/2);


    @Override
    public void runOpMode() throws InterruptedException {
        SimplerHardwareMap simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);


        driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
        driveTrain.localDrive(0,0.55,0);
        sleep(3300);
        driveTrain.localDrive(0,0,0);



    }

//    @Override
//    public void init() {
//
//        SimplerHardwareMap simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);
//
//        visionPortal = new VisionPortal.Builder()
//                .setCamera(BuiltinCameraDirection.BACK)
//                //.setCameraResolution(new Size(640,480))
//                .setCameraResolution(new Size(176,144))
//                .addProcessor(teamPropProcessor)
//                .build();
//
//        driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
//    }

//    @Override
//    public void loop() {
//
//
//    }

//    @Override
//    public void start(){
//        visionPortal.stopStreaming();
//
//        driveTrain.localDrive(1,0,0);
//
//
////        TeamPropProcessor.Selected selection = teamPropProcessor.getSelection();
//
////        Trajectory left = new TrajectoryBuilder(new Pose2d(12,60,3*PI/2))
////                .splineTo(new Vector2d(12,38),Math.toRadians(200))
////                .build();
//
////        driv
////
////
////        switch(selection){
////            case LEFT:
////                break;
////            case MIDDLE:
////                break;
////            case RIGHT:
////                break;
////            case NONE:
////                break;
////        }
//    }
}
