package org.firstinspires.ftc.greenTeamCode.opmodes;

import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.greenTeamCode.util.FieldPositions;
import org.firstinspires.ftc.greenTeamCode.util.PoseStorage;
import org.firstinspires.ftc.greenTeamCode.vision.TeamPropColorProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous
public class InitAuto extends LinearOpMode {

    //private enum Color{
    //    RED,
    //    BLUE,
    //}

    //private enum Distance{
    //    NEAR,
    //    FAR,
    //}

    private FieldPositions.Alliance alliance;
    private FieldPositions.StartSide startSide;

    VisionPortal visionPortal;
    private TeamPropColorProcessor teamPropProcessor;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        teamPropProcessor = new TeamPropColorProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(176,144))
                .addProcessor(teamPropProcessor)
                .build();


        telemetry.addLine("Adjust the camera so the drawn lines match the tape.");
        telemetry.addLine("(Go to three dots, then Camera Stream; tap picture to update)");
        telemetry.update();
        sleep(500);

        // 1. Align the camera with the tape lines
        // 2. Set the position of the robot

        waitForStart();
        visionPortal.stopStreaming();

        telemetry.addLine("Are you red or blue?");
        telemetry.update();

        while(opModeIsActive() && !(gamepad1.x || gamepad1.b)){ }
        if(gamepad1.x && !gamepad1.b) {
            alliance = FieldPositions.Alliance.BLUE;
        }else if(gamepad1.b && !gamepad1.x){
            alliance = FieldPositions.Alliance.RED;
        }
        telemetry.addLine("Are you near or far?");
        telemetry.update();
        while(opModeIsActive() && !(gamepad1.a || gamepad1.y)){ }
        if(gamepad1.y && !gamepad1.a) {
            startSide = FieldPositions.StartSide.FAR;
        }else if(gamepad1.a && !gamepad1.y){
            startSide = FieldPositions.StartSide.NEAR;
        }

        telemetry.addLine(String.format("Setting the position to %s %s", alliance, startSide));
        telemetry.update();


        PoseStorage.alliance = alliance;
        PoseStorage.startSide = startSide;

        PoseStorage.currentPose = FieldPositions.startPosition(alliance,startSide);


        sleep(3000);
    }
}
