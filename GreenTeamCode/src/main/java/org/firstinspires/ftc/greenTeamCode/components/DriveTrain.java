package org.firstinspires.ftc.greenTeamCode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.HolonomicController;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.MotorFeedforward;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Rotation2dDual;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.greenTeamCode.driveclasses.DriveLocalizer;
import org.firstinspires.ftc.greenTeamCode.driveclasses.Localizer;
import org.firstinspires.ftc.greenTeamCode.driveclasses.Params;
import org.firstinspires.ftc.greenTeamCode.driveclasses.PoseMessage;
import org.firstinspires.ftc.greenTeamCode.driveclasses.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.greenTeamCode.util.HelperFunctions;
import org.firstinspires.ftc.greenTeamCode.util.PIDFController;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;
import org.firstinspires.ftc.greenTeamCode.util.Triple;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Config
public class DriveTrain {
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private  DcMotorEx leftRear;
    private DcMotorEx rightRear;

    private VoltageSensor voltageSensor;

    private Telemetry telemetry;
    private IMU imu;
    private boolean disabled = false;

    private static final String LOG_TAG = "DriveTrain";
    private static final double STRAFE_COEFFICIENT = 1.1;
    private static final double MAX_POWER_MAG = 1;
    private final Params PARAMS = new Params();

    public enum DriveMode {
        FIELD_CENTRIC,
        ROBOT_CENTRIC,
        SNAP_TO_POINT,
    }

    public final MecanumKinematics kinematics = new MecanumKinematics(
            PARAMS.inPerTick * PARAMS.trackWidthTicks,
            PARAMS.inPerTick / PARAMS.lateralInPerTick
    );

    public final MotorFeedforward feedforward = new MotorFeedforward(
            PARAMS.kS,
            PARAMS.kV / PARAMS.inPerTick,
            PARAMS.kA / PARAMS.inPerTick
    );


    public static double HEADING_GAIN = 0;
    public static double HEADING_VEL_GAIN = 0;

    public PIDFController headingController = new PIDFController(
        new PIDFController.PIDCoefficients(HEADING_GAIN,0, HEADING_VEL_GAIN),
            PARAMS.kV,
            PARAMS.kA,
            PARAMS.kS
    );


    public final Localizer localizer;

    LinkedList<Pose2d> poseHistory = new LinkedList<Pose2d>();

    public Pose2d pose;


    public DriveTrain(@NonNull SimplerHardwareMap hardwareMap, @NonNull Telemetry telemetry ){
        try{
            this.leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
            this.rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
            this.leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
            this.rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

            this.imu = hardwareMap.get(IMU.class, "imu");
        } catch (Throwable T){
            Log.e(LOG_TAG,"Cannot find all drive motors and IMU. Disabling drivetrain.");
            this.disabled = true;
        }

        this.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        this.leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
//        this.leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        this.localizer = new ThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick);
//        this.localizer = new DriveLocalizer(
//                new OverflowEncoder(new RawEncoder(this.leftFront)),
//                new OverflowEncoder(new RawEncoder(this.leftRear)),
//                new OverflowEncoder(new RawEncoder(this.rightFront)),
//                new OverflowEncoder(new RawEncoder(this.rightRear)),
//                this.imu
//        );

        this.headingController.setInputBounds(-Math.PI, Math.PI);
        voltageSensor = hardwareMap.voltageSensor().iterator().next();

        this.telemetry = telemetry;
    }

    public void setPose(Pose2d pose){
        this.pose  = pose;
    }


    public void update(){
        this.localizer.update();
    }

    public void teleOpDrive(PoseVelocity2d input,DriveMode driveMode,double slownessFactor){

        switch (driveMode){
            case ROBOT_CENTRIC:
//                localDrive(input.x*slownessFactor,input.y*slownessFactor,input.z*slownessFactor);
                localDrive(
                        new PoseVelocity2d(input.linearVel.times(slownessFactor),
                                input.angVel*slownessFactor));
                break;
            case FIELD_CENTRIC:
                globalDrive(input);
                break;
            case SNAP_TO_POINT:

                break;
        }
    }


    /**
     * Drive the robot using local coordinate system.
     * @param pose - The relative pose. Y is forward, X is right.
     */
    public void localDrive(PoseVelocity2d pose){
        if(disabled){return;}

        // See https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
        // for mecanum drive guide.

        // restrict inputs between -1 and 1
//        double y = HelperFunctions.clamp(pose.linearVel.y,-1,1);
//        double x = HelperFunctions.clamp(pose.linearVel.x,-1,1) * STRAFE_COEFFICIENT; // This helps fix issues with wheels slipping
//        double rot = HelperFunctions.clamp(pose.angVel,-1,1);
//
//        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot),1);
//
//        double leftFrontPower = (y + x + rot) / denominator;
//        double leftRearPower = (y - x + rot) / denominator;
//        double rightFrontPower = (y - x - rot) / denominator;
//        double rightRearPower = (y + x - rot) / denominator;
//
//        this.leftFront.setPower(leftFrontPower);
//        this.leftRear.setPower(leftRearPower);
//        this.rightFront.setPower(rightFrontPower);
//        this.rightRear.setPower(rightRearPower);
        mecanumDriveSetDrivePowers(pose);
    }


    /**
     * Drive the robot using a field-centric coordinate system.
     * @param pose - The relative pose. Y is forward, X is right.
     */
    public void globalDrive(PoseVelocity2d pose){
        if(disabled){return;}
        this.updatePoseEstimate();

        double y = HelperFunctions.clamp(pose.linearVel.y,-1,1);
        double x = HelperFunctions.clamp(pose.linearVel.x,-1,1);
        double rot = HelperFunctions.clamp(pose.angVel,-1,1);

//        double heading = this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double heading = this.pose.heading.log();


        PoseVelocity2d newPose = new PoseVelocity2d(
                new Vector2d(
                    Math.cos(-heading)*pose.linearVel.x - Math.sin(-heading)*pose.linearVel.y ,
                        Math.sin(-heading)*pose.linearVel.x + Math.cos(-heading)*pose.linearVel.y
                ),
                pose.angVel
        );

        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

//        mecanumDriveSetDrivePowers(pose);
        localDrive(newPose);
    }

    public void snapToAngle(){
//        Pose2dDual<Time> txWorldTarget = new Pose2dDual<>(
//                new Vector2dDual<>(new DualNum<Time>(new double[]{0,0,0}),new DualNum<Time>(new double[]{0,0,0})),
//                new Rotation2dDual<>(new DualNum<Time>(new double[]{0,0,0}),new DualNum<Time>(new double[]{0,0,0}))
//        );

        Pose2dDual<Time> txWorldTarget = Pose2dDual.constant(new Pose2d(0,0,0),3);


        PoseVelocity2d robotVelRobot = updatePoseEstimate();
PoseVelocity2dDual<Time> command = new HolonomicController(
                PARAMS.axialGain,PARAMS.lateralGain,PARAMS.headingGain,
                PARAMS.axialVelGain,PARAMS.lateralVelGain,PARAMS.headingVelGain
        ).compute(txWorldTarget, pose, robotVelRobot);

        MecanumKinematics.WheelVelocities<Time> wheelVels = kinematics.inverse(command);

        double voltage = voltageSensor.getVoltage();

        leftFront.setPower(feedforward.compute(wheelVels.leftFront) / voltage);
        leftRear.setPower(feedforward.compute(wheelVels.leftBack) / voltage);
        rightRear.setPower(feedforward.compute(wheelVels.rightBack) / voltage);
        rightFront.setPower(feedforward.compute(wheelVels.rightFront) / voltage);
    }

    public void snapToAngleBetter(PoseVelocity2d input){
        FtcDashboard dash = FtcDashboard.getInstance();


        double xTarget = 0;

        Vector2d target = new Vector2d(0,0);


        Vector2d fieldCentricInput = rotated(input.linearVel,-pose.heading.log());


        double xDifference = xTarget - pose.position.x;


        Vector2d difference = target.minus(pose.position);
        double theta = difference.angleCast().log();

        double thetaFF = -rotated(fieldCentricInput,-Math.PI/2).dot(difference) / difference.sqrNorm();

        headingController.targetPosition = theta;

        double headingInput = (headingController.update(pose.heading.log()) * PARAMS.kV + thetaFF)
                * PARAMS.trackWidthTicks;


        PoseVelocity2d driveInput = new PoseVelocity2d(fieldCentricInput,headingInput);

        mecanumDriveSetDrivePowers(driveInput);
    }


    @Deprecated
    private PoseVelocity2d normalizeVelocity(PoseVelocity2d vel){
        return new PoseVelocity2d(vel.linearVel.div(vel.linearVel.norm()),0);
    }


    private Vector2d rotated(Vector2d v,double angle){
        return new Vector2d(
                Math.cos(angle)*v.x - Math.sin(angle)*v.y,
                Math.sin(angle)*v.x + Math.sin(angle)*v.y
        );
    }


    public void mecanumDriveSetDrivePowers(PoseVelocity2d powers){
        MecanumKinematics.WheelVelocities<Time> wheelVelocities = new MecanumKinematics(1)
                .inverse(PoseVelocity2dDual.constant(powers,1));

        double maxPowerMag = 1;

        for(DualNum<Time> power : wheelVelocities.all()){
            maxPowerMag = Math.max(maxPowerMag,power.value());
        }



        leftFront.setPower(wheelVelocities.leftFront.get(0) / maxPowerMag);
        leftRear.setPower(wheelVelocities.leftBack.get(0) / maxPowerMag);
        rightFront.setPower(wheelVelocities.rightFront.get(0) / maxPowerMag);
        rightRear.setPower(wheelVelocities.rightBack.get(0) / maxPowerMag);
    }




    public final class FollowTrajectoryAction implements Action{
        public final TimeTrajectory timeTrajectory;
        private double beginTs = -1;
        private final double[] xPoints, yPoints;


        public FollowTrajectoryAction(TimeTrajectory t){
            timeTrajectory = t;

            List<Double> disps = com.acmerobotics.roadrunner.Math.range(
                    0,
                    t.path.length(),
                    (int) Math.ceil(t.path.length() / 2)
            );
            xPoints = new double[disps.size()];
            yPoints = new double[disps.size()];
            for(int i=0;i<disps.size();i++){
                Pose2d p = t.path.get(disps.get(i),1).value();
                xPoints[i] = p.position.x;
                yPoints[i] = p.position.y;
            }
        }


        @Override
        public boolean run(@NonNull TelemetryPacket p){
            // first we find the time
            double t;
            if(beginTs < 0){
                beginTs = Actions.now();
                t=0;
            }else{
                t = Actions.now() - beginTs;
            }

            Pose2dDual<Time> txWorldTarget = timeTrajectory.get(t);

            PoseVelocity2d robotVelRobot = updatePoseEstimate();
            Pose2d error = txWorldTarget.value().minusExp(pose);


            // if we have gone over the duration of the trajectory, stop the robot.
            if((t >= timeTrajectory.duration && error.position.norm() < 2 && robotVelRobot.linearVel.norm() < 0.5)
            || t - 5 >= timeTrajectory.duration){
                leftFront.setPower(0);
                leftRear.setPower(0);
                rightFront.setPower(0);
                rightRear.setPower(0);

                return false;
            }



            PoseVelocity2dDual<Time> command = new HolonomicController(
                PARAMS.axialGain, PARAMS.lateralGain,PARAMS.headingGain, PARAMS.axialVelGain,PARAMS.lateralVelGain,PARAMS.headingVelGain
            ).compute(txWorldTarget, pose, robotVelRobot);

            MecanumKinematics.WheelVelocities<Time> wheelVelocities = kinematics.inverse(command);
            double voltage = voltageSensor.getVoltage();
            leftFront.setPower(feedforward.compute(wheelVelocities.leftFront) / voltage);
            leftRear.setPower(feedforward.compute(wheelVelocities.leftBack) / voltage);
            rightRear.setPower(feedforward.compute(wheelVelocities.rightBack) / voltage);
            rightFront.setPower(feedforward.compute(wheelVelocities.rightFront) / voltage);

            /* Some other drawing and telemetry stuff*/

            return true;
        }


        @Override
        public void preview(Canvas c){
            c.setStroke("#4CAF507A");
            c.setStrokeWidth(1);
            c.strokePolyline(xPoints,yPoints);
        }
    }


    public final class TurnAction implements Action {
        private final TimeTurn turn;

        private double beginTs = -1;

        public TurnAction(TimeTurn turn) {
            this.turn = turn;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            double t;
            if (beginTs < 0) {
                beginTs = Actions.now();
                t = 0;
            } else {
                t = Actions.now() - beginTs;
            }

            if (t >= turn.duration) {
                leftFront.setPower(0);
                leftRear.setPower(0);
                rightRear.setPower(0);
                rightFront.setPower(0);

                return false;
            }

            Pose2dDual<Time> txWorldTarget = turn.get(t);

            PoseVelocity2d robotVelRobot = updatePoseEstimate();

            PoseVelocity2dDual<Time> command = new HolonomicController(
                    PARAMS.axialGain, PARAMS.lateralGain, PARAMS.headingGain,
                    PARAMS.axialVelGain, PARAMS.lateralVelGain, PARAMS.headingVelGain
            )
                    .compute(txWorldTarget, pose, robotVelRobot);

            MecanumKinematics.WheelVelocities<Time> wheelVels = kinematics.inverse(command);
            double voltage = voltageSensor.getVoltage();
            leftFront.setPower(feedforward.compute(wheelVels.leftFront) / voltage);
            leftRear.setPower(feedforward.compute(wheelVels.leftBack) / voltage);
            rightRear.setPower(feedforward.compute(wheelVels.rightBack) / voltage);
            rightFront.setPower(feedforward.compute(wheelVels.rightFront) / voltage);

            FlightRecorder.write("TARGET_POSE", new PoseMessage(txWorldTarget.value()));

            Canvas c = p.fieldOverlay();
            drawPoseHistory(c);

            c.setStroke("#4CAF50");
            drawRobot(c, txWorldTarget.value());

            c.setStroke("#3F51B5");
            drawRobot(c, pose);

            c.setStroke("#7C4DFFFF");
            c.fillCircle(turn.beginPose.position.x, turn.beginPose.position.y, 2);

            return true;
        }

        @Override
        public void preview(Canvas c) {
            c.setStroke("#7C4DFF7A");
            c.fillCircle(turn.beginPose.position.x, turn.beginPose.position.y, 2);
        }
    }


    public PoseVelocity2d updatePoseEstimate(){
        Twist2dDual<Time> twist = localizer.update();
        pose = pose.plus(twist.value());


        // add pose to the pose history, then delete enough so
        // poseHistory is less than 100.
        poseHistory.add(pose);
        while(poseHistory.size() > 100){
            poseHistory.removeFirst();
        }

        FlightRecorder.write("ESTIMATED_POSE",new PoseMessage(pose));

        return twist.velocity().value();
    }


    private void drawPoseHistory(Canvas c) {
        double[] xPoints = new double[poseHistory.size()];
        double[] yPoints = new double[poseHistory.size()];

        int i = 0;
        for (Pose2d t : poseHistory) {
            xPoints[i] = t.position.x;
            yPoints[i] = t.position.y;

            i++;
        }

        c.setStrokeWidth(1);
        c.setStroke("#3F51B5");
        c.strokePolyline(xPoints, yPoints);
    }


    private static void drawRobot(Canvas c, Pose2d t) {
        final double ROBOT_RADIUS = 9;

        c.setStrokeWidth(1);
        c.strokeCircle(t.position.x, t.position.y, ROBOT_RADIUS);

        Vector2d halfv = t.heading.vec().times(0.5 * ROBOT_RADIUS);
        Vector2d p1 = t.position.plus(halfv);
        Vector2d p2 = p1.plus(halfv);
        c.strokeLine(p1.x, p1.y, p2.x, p2.y);
    }

    public TurnConstraints defaultTurnConstraints = new TurnConstraints(
            PARAMS.maxAngVel,-PARAMS.maxAngAccel,PARAMS.maxAngAccel
    );
    public VelConstraint defaultVelConstraint =
            new MinVelConstraint(Arrays.asList(
                kinematics.new WheelVelConstraint(PARAMS.maxWheelVel),
                    new AngularVelConstraint(PARAMS.maxAngVel)
            ));
    public AccelConstraint defaultAccelConstraint =
            new ProfileAccelConstraint(PARAMS.minProfileAccel,PARAMS.maxProfileAccel);


    public TrajectoryActionBuilder actionBuilder(Pose2d beginPose){
        return new TrajectoryActionBuilder(
                TurnAction::new,
                FollowTrajectoryAction::new,
                beginPose,
                1e-6,
                0.0,
                defaultTurnConstraints,
                defaultVelConstraint,
                defaultAccelConstraint,
                0.25,
                0.1
        );
    }

}
