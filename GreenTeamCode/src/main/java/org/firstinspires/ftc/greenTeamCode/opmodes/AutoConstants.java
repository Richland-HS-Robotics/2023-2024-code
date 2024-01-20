package org.firstinspires.ftc.greenTeamCode.opmodes;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Pose2d;

public class AutoConstants {
    public static final Pose2d ORIGIN = new Pose2d(0,0,0);

    public static final Pose2d BLUE_NEAR_START_POSITION = new Pose2d(12,60,3*PI/2);
    public static final Pose2d BLUE_FAR_START_POSITION = new Pose2d(-24-12,60,3*PI/2);
    public static final Pose2d RED_NEAR_START_POSITION = new Pose2d(12,-60,PI/2);
    public static final Pose2d RED_FAR_START_POSITION = new Pose2d(-24-12,-60,PI/2);

    public static final Pose2d BLUE_SCORE_LEFT_POSITION = new Pose2d(48,24+5+6+6,0);
    public static final Pose2d BLUE_SCORE_CENTER_POSITION = new Pose2d(48,24+5+6,0);
    public static final Pose2d BLUE_SCORE_RIGHT_POSITION = new Pose2d(48,24+5,0);

    public static final Pose2d RED_SCORE_LEFT_POSITION = new Pose2d(48,-24-5,0);
    public static final Pose2d RED_SCORE_CENTER_POSITION = new Pose2d(48,-24-5-6,0);
    public static final Pose2d RED_SCORE_RIGHT_POSITION = new Pose2d(48,-24-5-6-6,0);



    public static final Pose2d FAR_RED_TAPE_LEFT_POSITION = new Pose2d(-48,-26,PI/2);
    public static final Pose2d FAR_RED_TAPE_CENTER_POSITION = new Pose2d(-36,-18,PI/2);
    public static final Pose2d FAR_RED_TAPE_RIGHT_POSITION = new Pose2d(-24,-26,PI/2);

    public static final Pose2d NEAR_RED_TAPE_LEFT_POSITION = new Pose2d(0,-26,PI/2);
    public static final Pose2d NEAR_RED_TAPE_CENTER_POSITION = new Pose2d(12,-18,PI/2);
    public static final Pose2d NEAR_RED_TAPE_RIGHT_POSITION = new Pose2d(24,-26,PI/2);

    public static final Pose2d FAR_BLUE_TAPE_LEFT_POSITION = new Pose2d(-24,26,3*PI/2);
    public static final Pose2d FAR_BLUE_TAPE_CENTER_POSITION = new Pose2d(-36,18,3*PI/2);
    public static final Pose2d FAR_BLUE_TAPE_RIGHT_POSITION = new Pose2d(-48,26,3*PI/2);


    public static final Pose2d NEAR_BLUE_TAPE_LEFT_POSITION = new Pose2d(24,26,3*PI/2);
    public static final Pose2d NEAR_BLUE_TAPE_CENTER_POSITION = new Pose2d(12,18,3*PI/2);
    public static final Pose2d NEAR_BLUE_TAPE_RIGHT_POSITION = new Pose2d(0,26,3*PI/2);
}
