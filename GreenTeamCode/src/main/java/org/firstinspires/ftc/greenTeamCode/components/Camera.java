package org.firstinspires.ftc.greenTeamCode.components;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.greenTeamCode.util.Vector3;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Camera {

    private boolean use_webcam;


    private AprilTagProcessor aprilTag;

    private Telemetry telemetry;

    public Camera(AprilTagProcessor aprilTag,Telemetry telemetry){
        this.aprilTag = aprilTag;
        this.telemetry = telemetry;
    }

//    private void findDeltaPosition(){
//        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//
//        for(AprilTagDetection d : currentDetections){
//            // We use abs here because we only care about the difference.  We get the negative from the yaw.
//            double camX = Math.abs(d.ftcPose.x);
//            double camY = d.ftcPose.y;
//            double camYaw = -Math.toRadians(d.ftcPose.yaw);
//
//
//            double hyp = Math.sqrt((camX*camX)+(camY*camY));
//            double alpha = Math.asin(camY/hyp);
//            double beta = (Math.PI/2) - (alpha - camYaw);
//
//            double deltaY = hyp * Math.cos(beta);
//            double deltaX = hyp * Math.sin(beta);
//
//            telemetry.addLine(String.format(Locale.ENGLISH,"(dX,dY): (%f,%f)",deltaX,deltaY));
//
//        }
//    }


    private double[] deltaXYPositionFromTag(@NonNull AprilTagDetection d){
        // We use abs here because we only care about the difference.  We get the negative from the yaw.
        double camX = Math.abs(d.ftcPose.x);
        double camY = d.ftcPose.y;
        double camYaw = -Math.toRadians(d.ftcPose.yaw);


        double hyp = Math.sqrt((camX*camX)+(camY*camY));
        double alpha = Math.asin(camY/hyp);
        double beta = (Math.PI/2) - (alpha - camYaw);

        double deltaY = hyp * Math.cos(beta);
        double deltaX = hyp * Math.sin(beta);

        return new double[] {deltaX,deltaY};

        // telemetry.addLine(String.format(Locale.ENGLISH,"(dX,dY): (%f,%f)",deltaX,deltaY));
    }


    private double[] deltaZYPositionFromTag(@NonNull AprilTagDetection d){
        // We use abs here because we only care about the difference.  We get the negative from the yaw.
        double camZ = Math.abs(d.ftcPose.z);
        double camY = d.ftcPose.y;
        double camPitch = -Math.toRadians(d.ftcPose.pitch);


        double hyp = Math.sqrt((camZ*camZ)+(camY*camY));
        double alpha = Math.asin(camY/hyp);
        double beta = (Math.PI/2) - (alpha - camPitch);

        double deltaY = hyp * Math.cos(beta);
        double deltaZ = hyp * Math.sin(beta);

        return new double[] {deltaZ,deltaY};

        // telemetry.addLine(String.format(Locale.ENGLISH,"(dX,dY): (%f,%f)",deltaX,deltaY));
    }



    private Vector3 deltaPositionFromTag(@NonNull AprilTagDetection d){
        double[] XY = deltaXYPositionFromTag(d);
        double[] ZY = deltaZYPositionFromTag(d);

        double deltaX = XY[0];
        double deltaY = XY[1];
        double deltaZ = ZY[0];

        return new Vector3(deltaX,deltaY,deltaZ);
    }


    /**
     * Get all the delta positions, one for each detection.
     * @return A list of delta position vectors.
     */
    public List<Vector3> getAllDeltaPositions(){
        List<AprilTagDetection> l = aprilTag.getDetections();

        List<Vector3> deltaPositions = new ArrayList<Vector3>();

        for (AprilTagDetection d : l){
            deltaPositions.add(deltaPositionFromTag(d));
        }


        return deltaPositions;
    }




}
