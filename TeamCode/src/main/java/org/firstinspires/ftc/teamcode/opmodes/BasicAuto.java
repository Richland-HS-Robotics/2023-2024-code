package org.firstinspires.ftc.teamcode.opmodes;


import static java.lang.Math.PI;

import android.util.Size;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.driveclasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.VisionSelection;
import org.firstinspires.ftc.teamcode.vision.TeamPropColorProcessor;
import org.firstinspires.ftc.vision.VisionPortal;






@Autonomous
public class BasicAuto extends LinearOpMode {

    public static final Pose2d ORIGIN = new Pose2d(0,0,0);

    public static final Pose2d BLUE_NEAR_START_POSITION = new Pose2d(12,60,3*PI/2);
    public static final Pose2d BLUE_FAR_START_POSITION = new Pose2d(-24-12,60,3*PI/2);
    public static final Pose2d RED_NEAR_START_POSITION = new Pose2d(12,-60,PI/2);
    public static final Pose2d RED_FAR_START_POSITION = new Pose2d(-24-12,-60,PI/2);

    public static final Pose2d BLUE_SCORE_LEFT_POSITION = new Pose2d(48,24+5+6+6,0);
    public static final Pose2d BLUE_SCORE_CENTER_POSITION = new Pose2d(48,24+5+6,0);
    public static final Pose2d BLUE_SCORE_RIGHT_POSITION = new Pose2d(48,24+5,0);

    public static final Pose2d RED_SCORE_LEFT_POSITION = new Pose2d(48,-24-5,0);
    public static final Pose2d RED_SCORE_CENTER_POSITION = new Pose2d(48,-24-5-6,0);
    public static final Pose2d RED_SCORE_RIGHT_POSITION = new Pose2d(48,-24-5-6-6,0);



    public static final Pose2d FAR_RED_TAPE_LEFT_POSITION = new Pose2d(-48,-32,PI/2);
    public static final Pose2d FAR_RED_TAPE_CENTER_POSITION = new Pose2d(-36,-24,PI/2);
    public static final Pose2d FAR_RED_TAPE_RIGHT_POSITION = new Pose2d(-24,-32,PI/2);

    public static final Pose2d NEAR_RED_TAPE_LEFT_POSITION = new Pose2d(0,-32,PI/2);
    public static final Pose2d NEAR_RED_TAPE_CENTER_POSITION = new Pose2d(12,-24,PI/2);
    public static final Pose2d NEAR_RED_TAPE_RIGHT_POSITION = new Pose2d(24,-32,PI/2);

    public static final Pose2d FAR_BLUE_TAPE_LEFT_POSITION = new Pose2d(-24,32,3*PI/2);
    public static final Pose2d FAR_BLUE_TAPE_CENTER_POSITION = new Pose2d(-36,24,3*PI/2);
    public static final Pose2d FAR_BLUE_TAPE_RIGHT_POSITION = new Pose2d(-48,32,3*PI/2);


    public static final Pose2d NEAR_BLUE_TAPE_LEFT_POSITION = new Pose2d(24,32,3*PI/2);
    public static final Pose2d NEAR_BLUE_TAPE_CENTER_POSITION = new Pose2d(12,24,3*PI/2);
    public static final Pose2d NEAR_BLUE_TAPE_RIGHT_POSITION = new Pose2d(0,32,3*PI/2);



    private VisionPortal visionPortal;
    private TeamPropColorProcessor teamPropProcessor;

    private MecanumDrive driveTrain;
    //private RealSimplerHardwareMap simplerHardwareMap;



    private SimplerHardwareMap simplerHardwareMap;

    LinearSlide linearSlide;

    private enum Location{
        FAR_RED,
        NEAR_RED,
        FAR_BLUE,
        NEAR_BLUE
    }


    private Location location = Location.NEAR_RED;

    Pose2d startPosition;


    @Override
    public void runOpMode() throws InterruptedException {
        startPosition = ORIGIN;
        switch (location){
            case FAR_RED:
                startPosition = RED_FAR_START_POSITION;
                break;
            case NEAR_RED:
                startPosition = RED_NEAR_START_POSITION;
                break;
            case FAR_BLUE:
                startPosition = BLUE_FAR_START_POSITION;
                break;
            case NEAR_BLUE:
                startPosition = BLUE_NEAR_START_POSITION;
                break;
        }





        simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);
        teamPropProcessor = new TeamPropColorProcessor();
        driveTrain = new MecanumDrive(hardwareMap,RED_FAR_START_POSITION);
        linearSlide = new LinearSlide(simplerHardwareMap,telemetry);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class,"Webcam 1"))
                .setCameraResolution(new Size(176,144))
                .addProcessor(teamPropProcessor)
                .build();


        linearSlide.setGrabClaw(true);

        // Let it init
        sleep(1000);

        VisionSelection selection = teamPropProcessor.getSelection();
        Action traj = createTapeTrajectory(selection);

        waitForStart();

        visionPortal.stopStreaming();

        Actions.runBlocking(traj);
        sleep(500);
        linearSlide.setGrabClaw(false);

        Action traj2 = createBoardTrajectory(selection,driveTrain.pose);
        Actions.runBlocking(traj2);
    }

    /**
     * Create a trajectory to the tape.
     * @param selection The randomization result
     * @return The trajectory.
     */
    private Action createTapeTrajectory(VisionSelection selection){

        TrajectoryActionBuilder builder = driveTrain.actionBuilder(startPosition);

        switch (location){
            case FAR_RED:
                switch (selection){
                    case LEFT:
                        builder = builder.splineTo(FAR_RED_TAPE_LEFT_POSITION.position,FAR_RED_TAPE_LEFT_POSITION.heading);
                        break;
                    case CENTER:
                        builder = builder.splineTo(FAR_RED_TAPE_CENTER_POSITION.position,FAR_RED_TAPE_CENTER_POSITION.heading);
                        break;
                    case RIGHT:
                        builder = builder.splineTo(FAR_RED_TAPE_RIGHT_POSITION.position,FAR_RED_TAPE_RIGHT_POSITION.heading);
                        break;
                }
                break;
            case NEAR_RED:
                switch (selection){
                    case LEFT:
                        builder = builder.splineTo(NEAR_RED_TAPE_LEFT_POSITION.position,NEAR_RED_TAPE_LEFT_POSITION.heading);
                        break;
                    case CENTER:
                        builder = builder.splineTo(NEAR_RED_TAPE_CENTER_POSITION.position,NEAR_RED_TAPE_CENTER_POSITION.heading);
                        break;
                    case RIGHT:
                        builder = builder.splineTo(NEAR_RED_TAPE_RIGHT_POSITION.position,NEAR_RED_TAPE_RIGHT_POSITION.heading);
                        break;
                }
                break;
            case FAR_BLUE:
                switch (selection){
                    case LEFT:
                        builder = builder.splineTo(FAR_BLUE_TAPE_LEFT_POSITION.position,FAR_BLUE_TAPE_LEFT_POSITION.heading);
                        break;
                    case CENTER:
                        builder = builder.splineTo(FAR_BLUE_TAPE_CENTER_POSITION.position,FAR_BLUE_TAPE_CENTER_POSITION.heading);
                        break;
                    case RIGHT:
                        builder = builder.splineTo(FAR_BLUE_TAPE_RIGHT_POSITION.position,FAR_BLUE_TAPE_RIGHT_POSITION.heading);
                        break;
                }
                break;
            case NEAR_BLUE:
                switch (selection){
                    case LEFT:
                        builder = builder.splineTo(NEAR_BLUE_TAPE_LEFT_POSITION.position,NEAR_BLUE_TAPE_LEFT_POSITION.heading);
                        break;
                    case CENTER:
                        builder = builder.splineTo(NEAR_BLUE_TAPE_CENTER_POSITION.position,NEAR_BLUE_TAPE_CENTER_POSITION.heading);
                        break;
                    case RIGHT:
                        builder = builder.splineTo(NEAR_BLUE_TAPE_RIGHT_POSITION.position,NEAR_BLUE_TAPE_RIGHT_POSITION.heading);
                        break;
                }
                break;
        }

        return builder
                .build();
    }

    /**
     * Create a trajectory from the tape to the board.
     * @param selection The random selection
     * @param pose The last pose
     * @return The new trajectory
     */
    private Action createBoardTrajectory(VisionSelection selection,Pose2d pose){
        TrajectoryActionBuilder builder = driveTrain.actionBuilder(pose);

        switch (location){
            case FAR_RED:
                switch (selection){
                    case LEFT:
                        builder = builder
                                .splineTo(ORIGIN.position,0)
                                .splineTo(RED_SCORE_LEFT_POSITION.position,0);
                        break;
                    case CENTER:
                        builder = builder
                                .splineTo(ORIGIN.position,0)
                                .splineTo(RED_SCORE_CENTER_POSITION.position,0);
                        break;
                    case RIGHT:
                        builder = builder
                                .splineTo(ORIGIN.position,0)
                                .splineTo(RED_SCORE_RIGHT_POSITION.position,0);
                        break;
                }
                break;
            case NEAR_RED:
                switch (selection){
                    case LEFT:
                        builder = builder
                                .splineTo(RED_SCORE_LEFT_POSITION.position,0);
                        break;
                    case CENTER:
                        builder = builder
                                .splineTo(RED_SCORE_CENTER_POSITION.position,0);
                        break;
                    case RIGHT:
                        builder = builder
                                .splineTo(RED_SCORE_RIGHT_POSITION.position,0);
                        break;
                }
                break;
            case FAR_BLUE:
                switch (selection){
                    case LEFT:
                        builder = builder
                                .splineTo(ORIGIN.position,0)
                                .splineTo(BLUE_SCORE_LEFT_POSITION.position,0);
                        break;
                    case CENTER:
                        builder = builder
                                .splineTo(ORIGIN.position,0)
                                .splineTo(BLUE_SCORE_CENTER_POSITION.position,0);
                        break;
                    case RIGHT:
                        builder = builder
                                .splineTo(ORIGIN.position,0)
                                .splineTo(BLUE_SCORE_RIGHT_POSITION.position,0);
                        break;
                }
                break;
            case NEAR_BLUE:
                switch ( selection){
                    case LEFT:
                        builder = builder
                                .splineTo(BLUE_SCORE_LEFT_POSITION.position,0);
                        break;
                    case CENTER:
                        builder = builder
                                .splineTo(BLUE_SCORE_CENTER_POSITION.position,0);
                        break;
                    case RIGHT:
                        builder = builder
                                .splineTo(BLUE_SCORE_RIGHT_POSITION.position,0);
                        break;
                }
                break;
        }

        return builder.build();
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
