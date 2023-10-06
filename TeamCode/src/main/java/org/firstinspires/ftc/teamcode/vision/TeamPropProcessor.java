package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class TeamPropProcessor implements VisionProcessor {
    private Rect rectLeft;// = new Rect(20,42,150,50);
    private Rect rectMiddle;// = new Rect(20,100,150,50);
    private Rect rectRight;// = new Rect(20,160,150,50);

    private Selected selection = Selected.NONE;

    Mat hsvMat = new Mat();
    Mat submat = new Mat();


    public enum Selected{
        LEFT,
        MIDDLE,
        RIGHT,
        NONE
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // width - 10 pixels each side divided by 3, 5 pixels between sections
        int selectionWidth = (width - (10*2) - (2*5)) / 3;

        int selectionHeight = (height - (10*2));

        rectLeft = new Rect(10,10,selectionWidth,selectionHeight);
        rectMiddle = new Rect(10 + selectionWidth + 5,10,selectionWidth,selectionHeight);
        rectRight = new Rect(10 + (2*selectionWidth) + 10,10,selectionWidth,selectionHeight);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame,hsvMat,Imgproc.COLOR_RGB2HSV);

        double satRectLeft = getAvgSaturation(hsvMat,rectLeft);
        double satRectMiddle = getAvgSaturation(hsvMat,rectMiddle);
        double satRectRight = getAvgSaturation(hsvMat,rectRight);


        if( (satRectLeft > satRectMiddle) && (satRectLeft > satRectRight)){
            return Selected.LEFT;
        }else if ((satRectMiddle > satRectLeft) && (satRectMiddle > satRectRight)){
            return Selected.MIDDLE;
        }else if ((satRectRight > satRectLeft) && (satRectRight > satRectMiddle)){
            return Selected.RIGHT;
        }
        return Selected.NONE;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);


        Paint nonSelectedPaint = new Paint();
        nonSelectedPaint.setColor(Color.argb(100,0,255,0));


        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectLeft,scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle,scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectRight,scaleBmpPxToCanvasPx);

        selection = (Selected) userContext;
        switch (selection){
            case LEFT:
                canvas.drawRect(drawRectangleLeft,selectedPaint);
                canvas.drawRect(drawRectangleMiddle,nonSelectedPaint);
                canvas.drawRect(drawRectangleRight,nonSelectedPaint);
                break;
            case MIDDLE:
                canvas.drawRect(drawRectangleLeft,nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle,selectedPaint);
                canvas.drawRect(drawRectangleRight,nonSelectedPaint);
                break;
            case RIGHT:
                canvas.drawRect(drawRectangleLeft,nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle,nonSelectedPaint);
                canvas.drawRect(drawRectangleRight,selectedPaint);
                break;
            case NONE:
                canvas.drawRect(drawRectangleLeft,nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle,nonSelectedPaint);
                canvas.drawRect(drawRectangleRight,nonSelectedPaint);
                break;
        }
    }


    public Selected getSelection(){
        return selection;
    }


    private android.graphics.Rect makeGraphicsRect(Rect rect,float scaleBmpPxToCanvasPx){
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

        return new android.graphics.Rect(left,top,right,bottom);
    }

    protected double getAvgSaturation(Mat input,Rect rect){
        System.out.println(rect);
        submat = input.submat(rect);
        Scalar color = Core.mean(submat);
        return color.val[1];
    }
}
