package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.teamcode.vision.TeamPropColorProcessor;
import org.firstinspires.ftc.teamcode.vision.TeamPropProcessor;
import org.firstinspires.ftc.vision.VisionPortal;


@Autonomous
public class OpenCVTestOpmode extends OpMode {
    private TeamPropColorProcessor visionProcessor;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        visionProcessor = new TeamPropColorProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(BuiltinCameraDirection.BACK)
                //.setCameraResolution(new Size(640,480))
                .setCameraResolution(new Size(176,144))
                .addProcessor(visionProcessor)
                .build();

    }

    @Override
    public void loop() {

    }

    @Override
    public void start(){
        visionPortal.stopStreaming();
    }
}
