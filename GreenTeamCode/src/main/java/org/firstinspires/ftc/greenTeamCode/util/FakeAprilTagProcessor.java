package org.firstinspires.ftc.greenTeamCode.util;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class FakeAprilTagProcessor extends AprilTagProcessor {
    private ArrayList<AprilTagDetection> detections;


    public FakeAprilTagProcessor(){
        detections = new ArrayList<AprilTagDetection>();
    }

    public void clearDetections(){
        detections.clear();
    }

    public void addDetection(AprilTagDetection detection){
        detections.add(detection);
    }

    @Override
    public void setDecimation(float decimation) {

    }

    @Override
    public void setPoseSolver(PoseSolver poseSolver) {

    }

    @Override
    public int getPerTagAvgPoseSolveTime() {
        return 0;
    }

    @Override
    public ArrayList<AprilTagDetection> getDetections() {
        return detections;
    }

    @Override
    public ArrayList<AprilTagDetection> getFreshDetections() {
        return null;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
