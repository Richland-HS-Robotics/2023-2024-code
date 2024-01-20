package org.firstinspires.ftc.greenTeamCode.vision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.greenTeamCode.util.VisionSelection;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@Config
public class TeamPropColorProcessor implements VisionProcessor {


    public static int CENTER_LINE_START_X = 55;
    public static int CENTER_LINE_START_Y = 80;
    public static int CENTER_LINE_END_X = 119;
    public static int CENTER_LINE_END_Y = 80;

    public static int LEFT_LINE_START_X = 8;
    public static int LEFT_LINE_START_Y = 102;
    public static int LEFT_LINE_END_X = 31;
    public static int LEFT_LINE_END_Y = 80;

    public static int RIGHT_LINE_START_X = 168;
    public static int RIGHT_LINE_START_Y = 103;
    public static int RIGHT_LINE_END_X = 145;
    public static int RIGHT_LINE_END_Y = 82;

    // The area that will be sensed.
    private Rect sensingRect;

    private Rect rectLeft;
    private Rect rectMiddle;
    private Rect rectRight;

    private VisionSelection selection = VisionSelection.CENTER;

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

        int yOffset = height / 3;
        int xOffset = width / 3;


        sensingRect = new Rect(0,yOffset,width,height-yOffset);


        int selectionWidth = sensingRect.width / 3;

//        rectLeft = new Rect(sensingRect.x,sensingRect.y,selectionWidth,sensingRect.height);
//        rectMiddle = new Rect(sensingRect.x + selectionWidth,sensingRect.y,selectionWidth,sensingRect.height);
//        rectRight = new Rect(sensingRect.x + (2*selectionWidth),sensingRect.y,selectionWidth,sensingRect.height);
        rectLeft = new Rect(0,55,38,38);
        rectMiddle = new Rect(70,50,38,38);
        rectRight = new Rect(140, 82, 30, 30);



//        int outerSpacing = 10;
//        int innerSpacing = 5;

        // **[]*[]*[]**

//        int selectionWidth = width - (2*outerSpacing);

        //int selectionHeight = (height-2*outerSpacing-2*innerSpacing)/3;


//        rectTop = new Rect(outerSpacing,outerSpacing,selectionWidth,selectionHeight);
//        rectMiddle = new Rect(outerSpacing,selectionHeight+outerSpacing+innerSpacing,selectionWidth,selectionHeight);
//        rectBottom = new Rect(outerSpacing,2*selectionHeight + 2*innerSpacing + outerSpacing,selectionWidth,selectionHeight);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame,hsvMat,Imgproc.COLOR_RGB2HSV);


        double satRectLeft = getAvgSaturation(hsvMat, rectLeft);
        double satRectMiddle = getAvgSaturation(hsvMat,rectMiddle);
        double satRectRight = getAvgSaturation(hsvMat, rectRight);


//        if(satRectLeft > satRectMiddle){
//            return Selected.LEFT;
//        }else if(satRectMiddle > satRectLeft){
//            return Selected.MIDDLE;
//        }else{
//            return Selected.RIGHT;
//        }

        if( (satRectLeft > satRectMiddle) && (satRectLeft > satRectRight)){
            return VisionSelection.LEFT;
        }else if ((satRectMiddle > satRectLeft) && (satRectMiddle > satRectRight)){
            return VisionSelection.CENTER;
        }else if ((satRectRight > satRectLeft) && (satRectRight > satRectMiddle)){
            return VisionSelection.RIGHT;
        }
        return VisionSelection.CENTER;
    }


    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
//        canvas.setMatrix(new Matrix.);
//        canvas.setMatrix(cvMat2Matrix(output));
// ----------------------------------------------
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(scaleCanvasDensity * 8);
        canvas.drawLine(LEFT_LINE_START_X*scaleBmpPxToCanvasPx,LEFT_LINE_START_Y*scaleBmpPxToCanvasPx,LEFT_LINE_END_X*scaleBmpPxToCanvasPx,LEFT_LINE_END_Y*scaleBmpPxToCanvasPx,linePaint);
        canvas.drawLine(CENTER_LINE_START_X*scaleBmpPxToCanvasPx,CENTER_LINE_START_Y*scaleBmpPxToCanvasPx,CENTER_LINE_END_X*scaleBmpPxToCanvasPx,CENTER_LINE_END_Y*scaleBmpPxToCanvasPx,linePaint);
        canvas.drawLine(RIGHT_LINE_START_X*scaleBmpPxToCanvasPx,RIGHT_LINE_START_Y*scaleBmpPxToCanvasPx,RIGHT_LINE_END_X*scaleBmpPxToCanvasPx,RIGHT_LINE_END_Y*scaleBmpPxToCanvasPx,linePaint);

        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);


        Paint nonSelectedPaint = new Paint();
        nonSelectedPaint.setColor(Color.argb(100,0,255,0));

        Paint sensingPaint = new Paint();
        sensingPaint.setColor(Color.BLUE);
        sensingPaint.setStyle(Paint.Style.STROKE);
        sensingPaint.setStrokeWidth(scaleCanvasDensity * 8);
        android.graphics.Rect drawRectangleSensing = makeGraphicsRect(sensingRect,scaleBmpPxToCanvasPx);
        canvas.drawRect(drawRectangleSensing,sensingPaint);


        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectLeft,scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle,scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectRight,scaleBmpPxToCanvasPx);

        selection = (VisionSelection) userContext;
        switch (selection){
            case LEFT:
                canvas.drawRect(drawRectangleLeft,selectedPaint);
                canvas.drawRect(drawRectangleMiddle,nonSelectedPaint);
                canvas.drawRect(drawRectangleRight,nonSelectedPaint);
                break;
            case CENTER:
                canvas.drawRect(drawRectangleLeft,nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle,selectedPaint);
                canvas.drawRect(drawRectangleRight,nonSelectedPaint);
                break;
            case RIGHT:
                canvas.drawRect(drawRectangleLeft,nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle,nonSelectedPaint);
                canvas.drawRect(drawRectangleRight,selectedPaint);
                break;
//            case NONE:
//                canvas.drawRect(drawRectangleLeft,nonSelectedPaint);
//                canvas.drawRect(drawRectangleMiddle,nonSelectedPaint);
//                canvas.drawRect(drawRectangleRight,nonSelectedPaint);
//                break;
        }
    }


    public VisionSelection getSelection(){
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
