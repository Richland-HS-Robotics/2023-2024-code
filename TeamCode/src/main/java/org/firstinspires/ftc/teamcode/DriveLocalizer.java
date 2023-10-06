package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * A localizer using encoders on a mecanum drivetrain.
 * */
public class DriveLocalizer implements Localizer{

    public final Encoder leftFront, leftRear, rightRear, rightFront;

    private int lastLeftFrontPos, lastLeftRearPos, lastRightRearPos, lastRightFrontPos;
    private Rotation2d lastHeading;

    private IMU imu;

    private Params PARAMS = new Params();

    private MecanumKinematics kinematics = new MecanumKinematics(
            PARAMS.inPerTick * PARAMS.trackWidthTicks,
            PARAMS.inPerTick / PARAMS.lateralInPerTick
    );

    public DriveLocalizer(
            Encoder leftFront,
            Encoder leftRear,
            Encoder rightFront,
            Encoder rightRear,
            IMU imu
    ) {
        this.leftFront = leftFront;//new OverflowEncoder(new RawEncoder(leftFront));
        this.leftRear = leftRear;//new OverflowEncoder(new RawEncoder(leftRear));
        this.rightRear = rightRear;//new OverflowEncoder(new RawEncoder(rightRear));
        this.rightFront = rightFront;//new OverflowEncoder(new RawEncoder(rightFront));
        this.imu = imu;

        lastLeftFrontPos = this.leftFront.getPositionAndVelocity().position;
        lastLeftRearPos = this.leftRear.getPositionAndVelocity().position;
        lastRightRearPos = this.rightRear.getPositionAndVelocity().position;
        lastRightFrontPos = this.rightFront.getPositionAndVelocity().position;

        lastHeading = Rotation2d.exp(this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
    }

    @Override
    public Twist2dDual<Time> update() {
        PositionVelocityPair leftFrontPosVel = leftFront.getPositionAndVelocity();
        PositionVelocityPair leftRearPosVel = leftRear.getPositionAndVelocity();
        PositionVelocityPair rightRearPosVel = rightRear.getPositionAndVelocity();
        PositionVelocityPair rightFrontPosVel = rightFront.getPositionAndVelocity();

        Rotation2d heading = Rotation2d.exp(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        double headingDelta = heading.minus(lastHeading);

        Twist2dDual<Time> twist = kinematics.forward(new MecanumKinematics.WheelIncrements<>(
                new DualNum<Time>(new double[]{
                        (leftFrontPosVel.position - lastLeftFrontPos),
                        leftFrontPosVel.velocity,
                }).times(PARAMS.inPerTick),
                new DualNum<Time>(new double[]{
                        (leftRearPosVel.position - lastLeftRearPos),
                        leftRearPosVel.velocity,
                }).times(PARAMS.inPerTick),
                new DualNum<Time>(new double[]{
                        (rightRearPosVel.position - lastRightRearPos),
                        rightRearPosVel.velocity,
                }).times(PARAMS.inPerTick),
                new DualNum<Time>(new double[]{
                        (rightFrontPosVel.position - lastRightFrontPos),
                        rightFrontPosVel.velocity,
                }).times(PARAMS.inPerTick)
        ));

        lastLeftFrontPos = leftFrontPosVel.position;
        lastLeftRearPos = leftRearPosVel.position;
        lastRightRearPos = rightRearPosVel.position;
        lastRightFrontPos = rightFrontPosVel.position;

        lastHeading = heading;

        return new Twist2dDual<>(
                twist.line,
                DualNum.cons(headingDelta, twist.angle.drop(1))
        );
    }

}
