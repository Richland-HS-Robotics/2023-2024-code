package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@TeleOp(name = "AprilTag Positioning")
public class AprilTagPositioning extends LinearOpMode {

    /**
     * If true, use an external webcam.  If false, use the pohone camera.
     */
    private static final boolean USE_WEBCAM = false;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;


    @Override
    public void runOpMode(){
        setupAprilTags();

        waitForStart();

        if (opModeIsActive()){
            while(opModeIsActive()){
                //processAprilTags();
//                findDistanceToAprilTag();
                findDeltaPosition();
                telemetry.update();
            }
        }

    }


    /**
     * Set up AprilTag detection by creating a new AprilTag processor and new VisionPortal.
     */
    private void setupAprilTags(){
        // create the AprilTag processor
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawTagOutline(true)
                .setDrawTagID(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();


        VisionPortal.Builder builder = new VisionPortal.Builder();

        if (USE_WEBCAM){
            builder.setCamera(hardwareMap.get(WebcamName.class,"Webcam 1"));
        }else{
            builder.setCamera(BuiltinCameraDirection.BACK);
        }



        builder.addProcessor(aprilTag);

        visionPortal = builder.build();
    }


    private void findDeltaPosition(){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        for(AprilTagDetection d : currentDetections){
            // We use abs here because we only care about the difference.  We get the negative from the yaw.
            double camX = Math.abs(d.ftcPose.x);
            double camY = d.ftcPose.y;
            double camYaw = -Math.toRadians(d.ftcPose.yaw);


            double hyp = Math.sqrt((camX*camX)+(camY*camY));
            double alpha = Math.asin(camY/hyp);
            double beta = (Math.PI/2) - (alpha - camYaw);

            double deltaY = hyp * Math.cos(beta);
            double deltaX = hyp * Math.sin(beta);

            telemetry.addLine(String.format(Locale.ENGLISH,"(dX,dY): (%f,%f)",deltaX,deltaY));

        }
    }


    private void findDistanceToAprilTag(){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();


        for (AprilTagDetection detection : currentDetections){
            telemetry.addLine(
                    String.format(Locale.ENGLISH,
                            "(X,Y,Z,): (%f,%f,%f)\n(xRot,yRot,zRot): (%f,%f,%f)",
                            detection.ftcPose.x,
                            detection.ftcPose.y,
                            detection.ftcPose.z,
                            detection.ftcPose.pitch,
                            detection.ftcPose.roll,
                            detection.ftcPose.yaw
                    )
            );
        }


//        if (currentDetections.size()>-1){
//            AprilTagDetection detection = currentDetections.get(-1);
//
//            double distance = Math.sqrt((detection.ftcPose.x * detection.ftcPose.x)
//                            + (detection.ftcPose.y * detection.ftcPose.y)
//                            + (detection.ftcPose.z * detection.ftcPose.z));
//
//            telemetry.addLine(String.format("Distance: %f",distance));
//        }
    }


    private void processAprilTags(){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        double xPos;
        double yPos;

        List<Double> possibleXPositions = new ArrayList<Double>();
        List<Double> possibleYPositions = new ArrayList<Double>();

        for(AprilTagDetection detection : currentDetections){
            double aprilX = 0;
            double aprilY = 0;
            if(detection.id == 586){
                aprilX = -13;
                aprilY = 48;
            }else if (detection.id == 585){
                aprilX = 14;
                aprilY = 48;
            }

            possibleXPositions.add(aprilX - detection.ftcPose.x);
            possibleXPositions.add(aprilY - detection.ftcPose.y);
        }

        xPos = averageOfList(possibleXPositions);
        yPos = averageOfList(possibleYPositions);

        telemetry.addLine(String.format("(X,Y): (%f,%f)",xPos,yPos));

    }


    private double averageOfList(List<Double> list){
        double total = 0;
        for (double i: list){
            total += i;
        }
        return total / list.size();
    }







}
