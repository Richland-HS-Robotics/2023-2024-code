package org.firstinspires.ftc.greenTeamCode.util;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

public class FieldPositions {
    public enum Alliance {
        RED, BLUE,
    }

    public enum StartSide{
        NEAR,
        FAR,
    }


    public static Vector2d boardPosition(Alliance alliance, VisionSelection visionState) {
        switch (alliance) {
            case RED:
                switch (visionState) {
                    case LEFT:
                        return new Vector2d(48, -24 - 5);
                    case CENTER:
                        return new Vector2d(48, -24 - 5 - 6);
                    case RIGHT:
                        return new Vector2d(48, -24 - 5 - 6 - 6);
                }
            case BLUE:
                switch (visionState) {
                    case LEFT:
                        return new Vector2d(48, 24 + 5 + 6 + 6);
                    case CENTER:
                        return new Vector2d(48, 24 + 5 + 6);
                    case RIGHT:
                        return new Vector2d(48, 24 + 5);
                }
        }
        return null; // should be unreachable
    }


    public static Pose2d startPosition(Alliance alliance, StartSide startSide){
        switch (alliance){
            case RED:
                switch (startSide){
                    case NEAR:
                        return new Pose2d(12, -60, PI / 2);
                    case FAR:
                        return new Pose2d(-24 - 12, -60, PI / 2);
                }
            case BLUE:
                switch (startSide){
                    case NEAR:
                        return new Pose2d(12,60,3*PI/2);
                    case FAR:
                        return new Pose2d(-24-12,60,3*PI/2);
                }
        }
        return null; // Should be unreachable
    }


    public static Vector2d inFrontOfBoard(Alliance alliance){
        switch (alliance){
            case RED:
                return new Vector2d(30,-36);
            case BLUE:
                return new Vector2d(30,36);
        }
        return null;
    }


    public static Vector2d tapeCenterPosition(Alliance alliance, StartSide startSide){
        switch (alliance){
            case RED:
                switch (startSide){
                    case NEAR:
                        return new Vector2d(12,-36);
                    case FAR:
                        return new Vector2d(-36,-36);
                }
            case BLUE:
                switch (startSide){
                    case NEAR:
                        return new Vector2d(12,36);
                    case FAR:
                        return new Vector2d(-36,36);
                }
        }
        return null;
    }

    public static double tangent(Alliance alliance){
        switch (alliance){
            case RED:
                return PI/2;
            case BLUE:
                return -PI/2;
        }
        return 0; // should not happen
    }



    public static final Pose2d ORIGIN = new Pose2d(0, 0, 0);



    public static final Vector2d FAR_RED_TAPE_LEFT_POSITION = new Vector2d(-48, -32);
    public static final Vector2d FAR_RED_TAPE_CENTER_POSITION = new Vector2d(-36, -24);
    public static final Vector2d FAR_RED_TAPE_RIGHT_POSITION = new Vector2d(-24, -32);

    public static final Vector2d NEAR_RED_TAPE_LEFT_POSITION = new Vector2d(0, -32);
    public static final Vector2d NEAR_RED_TAPE_CENTER_POSITION = new Vector2d(12, -24);
    public static final Vector2d NEAR_RED_TAPE_RIGHT_POSITION = new Vector2d(24, -32);

    public static final Vector2d FAR_BLUE_TAPE_LEFT_POSITION = new Vector2d(-24, 32);
    public static final Vector2d FAR_BLUE_TAPE_CENTER_POSITION = new Vector2d(-36, 24);
    public static final Vector2d FAR_BLUE_TAPE_RIGHT_POSITION = new Vector2d(-48, 32);


    public static final Vector2d nearBlueTapeLeftPosition = new Vector2d(24, 32);
    public static final Vector2d nearBlueTapeCenterPosition = new Vector2d(12, 24);
    public static final Vector2d nearBlueTapeRightPosition = new Vector2d(0, 32);

}
