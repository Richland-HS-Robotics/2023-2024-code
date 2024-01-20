package org.firstinspires.ftc.greenTeamCode.opmodes;

import static java.lang.Math.PI;

import android.util.Size;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.greenTeamCode.components.DriveTrain;
import org.firstinspires.ftc.greenTeamCode.components.LinearSlide;
import org.firstinspires.ftc.greenTeamCode.components.PixelRelease;
import org.firstinspires.ftc.greenTeamCode.util.FieldPositions;
import org.firstinspires.ftc.greenTeamCode.util.PoseStorage;
import org.firstinspires.ftc.greenTeamCode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;
import org.firstinspires.ftc.greenTeamCode.util.VisionSelection;
import org.firstinspires.ftc.greenTeamCode.vision.TeamPropColorProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous
public class BasicAutoOrigin extends LinearOpMode {

    private SimplerHardwareMap simplerHardwareMap;
    private VisionPortal visionPortal;
    private TeamPropColorProcessor teamPropProcessor;
    private DriveTrain driveTrain;
    private PixelRelease pixelRelease;
    private LinearSlide linearSlide;

    private Pose2d startPos;

    Pose2d beginPose;

    @Override
    public void runOpMode() throws InterruptedException {
        beginPose = PoseStorage.currentPose;

        simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);
        teamPropProcessor = new TeamPropColorProcessor();
        driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
        driveTrain.setPose(beginPose);
        linearSlide = new LinearSlide(simplerHardwareMap,telemetry);
        pixelRelease = new PixelRelease(simplerHardwareMap,telemetry);
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(176,144))
                .addProcessor(teamPropProcessor)
                .build();

        waitForStart();

        VisionSelection selection =  VisionSelection.CENTER;//teamPropProcessor.getSelection();
        visionPortal.stopStreaming();



        Vector2d endPosition = FieldPositions.boardPosition(PoseStorage.alliance,selection);



        Action trajectory = driveTrain.actionBuilder(beginPose)
                .splineTo(FieldPositions.inFrontOfBoard(PoseStorage.alliance),0)
                .afterTime(0.5,new SequentialAction(
                        linearSlide.raiseSubArm(),
                        linearSlide.setSlideToPosition(0.40)
                ))
                .splineTo(endPosition,0)
                .stopAndAdd(linearSlide.openClaw())
                .lineToX(endPosition.x - 3)
                .build();


        //Actions.runBlocking(
        //        new SequentialAction(
        //                linearSlide.closeClaw(),
        //                trajectory,
        //                linearSlide.lowerSubArm(),
        //                linearSlide.setSlideToPosition(0.0)
        //        )
        //);
        Actions.runBlocking(
                new SequentialAction(
                        linearSlide.closeClaw(),
                        linearSlide.lowerSubArm(),
                        depositToTape(PoseStorage.alliance, PoseStorage.startSide, selection),
                        depositToBoard(PoseStorage.alliance, PoseStorage.startSide,selection,startPos)
                )
        );
    }


    // private TrajectoryActionBuilder depositPixel(TrajectoryActionBuilder builder,
    //                                              FieldPositions.Alliance alliance,
    //                                              FieldPositions.StartSide startSide,
    //                                              VisionSelection selection){
    //     TrajectoryActionBuilder newBuilder = builder
    //             .splineTo(FieldPositions.tapeCenterPosition(alliance,startSide),PI/2)
    //             .turn(-PI/2);
    //     switch (selection){
    //         case LEFT:
    //             newBuilder = newBuilder.lineToX(10);
    //             break;
    //         case RIGHT:
    //             break;
    //         case CENTER:
    //             break;
    //     }


    //     return builder;
    // }


    private Action depositToBoard(FieldPositions.Alliance alliance,
                                  FieldPositions.StartSide startSide,
                                  VisionSelection selection,Pose2d startPose){

        Action traj = driveTrain.actionBuilder(startPose)
                .splineTo(FieldPositions.boardPosition(alliance,selection),0)
                .build();

        Action traj2 = driveTrain.actionBuilder(new Pose2d(FieldPositions.boardPosition(alliance,selection),0))
                .lineToX(FieldPositions.boardPosition(alliance,selection).x - 5)
                .build();

        return new SequentialAction(
                linearSlide.raiseSubArm(),
                traj,
                linearSlide.openClaw(),
                traj2,
                linearSlide.closeClaw()
        );
    }


    private Action depositToTape(FieldPositions.Alliance alliance,
                                 FieldPositions.StartSide startSide,
                                 VisionSelection selection){
        Vector2d distanceToAdd = new Vector2d(0,4);
        if(selection == VisionSelection.CENTER){
            distanceToAdd = new Vector2d(0,12);
        }

        Vector2d centerPose =  FieldPositions.tapeCenterPosition(alliance,startSide).plus(distanceToAdd);

        TrajectoryActionBuilder partialTraj = driveTrain.actionBuilder(beginPose)
                .splineTo(centerPose,FieldPositions.tangent(alliance))
                .turnTo(0);



        VisionSelection modifiedSelection = selection;
        if(alliance == FieldPositions.Alliance.BLUE){
            switch (selection){
                case LEFT:
                    modifiedSelection = VisionSelection.RIGHT;
                    break;
                case RIGHT:
                    modifiedSelection = VisionSelection.LEFT;
                    break;
                case CENTER:
                    modifiedSelection = VisionSelection.CENTER;
                    break;
            }
        }



        Vector2d finalPos = new Vector2d(0,0);
        switch (modifiedSelection){
            case LEFT:
                finalPos = centerPose.plus(new Vector2d(-3,0));
                partialTraj = partialTraj.lineToX(centerPose.x - 3);
                break;
            case RIGHT:
                partialTraj = partialTraj.lineToX(centerPose.x + 18.5);
                finalPos = centerPose.plus(new Vector2d(18.5,0));
                break;
            case CENTER:
                finalPos = centerPose.plus(new Vector2d(5,0));
                partialTraj = partialTraj.lineToX(centerPose.x + 5);
                break;
        }

        startPos = new Pose2d(finalPos,0);

        Action traj = partialTraj
                //.lineToX(centerPose.x + offset)
                .build();



        return new SequentialAction(
                new InstantAction(() -> pixelRelease.hold()),
                traj,
                new InstantAction(() -> pixelRelease.release()),
                new SleepAction(5)
        );
    }
}
