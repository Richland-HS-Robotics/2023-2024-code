package com.example.meepmeeptesting;


import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class MeepMeepTesting {



    private static final FieldPositions.VisionState visionState = FieldPositions.VisionState.RIGHT;
    private static final FieldPositions.StartSide startState = FieldPositions.StartSide.FAR;
    private static final FieldPositions.Alliance alliance = FieldPositions.Alliance.RED;


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);






        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();



        Vector2d endPosition = FieldPositions.boardPosition(FieldPositions.Alliance.RED,visionState);
        Vector2d inFrontOfBoardPosition = FieldPositions.inFrontOfBoard(FieldPositions.Alliance.RED);


        myBot.runAction(myBot.getDrive().actionBuilder(FieldPositions.startPosition(FieldPositions.Alliance.RED, FieldPositions.StartSide.NEAR))
                .splineTo(FieldPositions.tapePosition(FieldPositions.Alliance.RED, FieldPositions.StartSide.NEAR),PI/2)
                .splineTo(inFrontOfBoardPosition,0)
                .splineTo(endPosition,0)
                .build()
        );



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }




    private Action buildTrajectory(){

        return null;
    }
}

