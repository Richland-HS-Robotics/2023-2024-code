package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class TeamPropColorProcessor implements VisionProcessor {
    private Rect rectTop;
    private Rect rectMiddle;
    private Rect rectBottom;

    private Selected selection = Selected.NONE;

    Mat hsvMat = new Mat();
    Mat submat = new Mat();
    Mat mask = new Mat();

    Mat output = new Mat();


    public enum Selected{
        LEFT,
        MIDDLE,
        RIGHT,
        NONE
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // width - 10 pixels each side divided by 3, 5 pixels between sections

        int outerSpacing = 10;
        int innerSpacing = 5;

        // **[]*[]*[]**

        int selectionWidth = width - (2*outerSpacing);

        int selectionHeight = (height-2*outerSpacing-2*innerSpacing)/3;


        rectTop = new Rect(outerSpacing,outerSpacing,selectionWidth,selectionHeight);
        rectMiddle = new Rect(outerSpacing,selectionHeight+outerSpacing+innerSpacing,selectionWidth,selectionHeight);
        rectBottom = new Rect(outerSpacing,2*selectionHeight + 2*innerSpacing + outerSpacing,selectionWidth,selectionHeight);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame,hsvMat,Imgproc.COLOR_RGB2HSV);


        double satRectLeft = getAvgSaturation(hsvMat, rectTop);
        double satRectMiddle = getAvgSaturation(hsvMat,rectMiddle);
        double satRectRight = getAvgSaturation(hsvMat, rectBottom);



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
//        canvas.setMatrix(new Matrix.);
//        canvas.setMatrix(cvMat2Matrix(output));
// ----------------------------------------------
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);


        Paint nonSelectedPaint = new Paint();
        nonSelectedPaint.setColor(Color.argb(100,0,255,0));


        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectTop,scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle,scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectBottom,scaleBmpPxToCanvasPx);

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
