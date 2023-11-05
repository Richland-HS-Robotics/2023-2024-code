package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.HolonomicController;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MotorFeedforward;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.driveclasses.DriveLocalizer;
import org.firstinspires.ftc.teamcode.driveclasses.Localizer;
import org.firstinspires.ftc.teamcode.driveclasses.Params;
import org.firstinspires.ftc.teamcode.driveclasses.PoseMessage;
import org.firstinspires.ftc.teamcode.util.HelperFunctions;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.Triple;

import java.util.LinkedList;
import java.util.List;

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
        ROBOT_CENTRIC
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
        this.rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
//        this.leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        this.localizer = new DriveLocalizer(
                new OverflowEncoder(new RawEncoder(this.leftFront)),
                new OverflowEncoder(new RawEncoder(this.leftRear)),
                new OverflowEncoder(new RawEncoder(this.rightFront)),
                new OverflowEncoder(new RawEncoder(this.rightRear)),
                this.imu
        );



        this.telemetry = telemetry;
    }


    public void update(){
        this.localizer.update();
    }

    public void teleOpDrive(Triple<Double,Double,Double> input,DriveMode driveMode,double slownessFactor){

        switch (driveMode){
            case ROBOT_CENTRIC:
                localDrive(input.x*slownessFactor,input.y*slownessFactor,input.z*slownessFactor);
                break;
            case FIELD_CENTRIC:
                globalDrive(input.x,input.y,input.z);
                break;
        }
    }


    /**
     * Drive the robot using local coordinate system.
      * @param forward The forward-backward component.  Between -1 and 1.
     * @param strafe - The left-right component.  Between -1 and 1, 1 is right.
     * @param turn - The rotational component. Between -1 and 1.
     */
    public void localDrive(double forward, double strafe, double turn){
        if(disabled){return;}

        // See https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
        // for mecanum drive guide.

        // restrict inputs between -1 and 1
        double y = HelperFunctions.clamp(forward,-1,1);
        double x = HelperFunctions.clamp(strafe,-1,1) * STRAFE_COEFFICIENT; // This helps fix issues with wheels slipping
        double rot = HelperFunctions.clamp(turn,-1,1);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot),1);

        double leftFrontPower = (y + x + rot) / denominator;
        double leftRearPower = (y - x - rot) / denominator;
        double rightFrontPower = (y - x + rot) / denominator;
        double rightRearPower = (y + x - rot) / denominator;

        this.leftFront.setPower(leftFrontPower);
        this.leftRear.setPower(leftRearPower);
        this.rightFront.setPower(rightFrontPower);
        this.rightRear.setPower(rightRearPower);
    }


    /**
     * Drive the robot using a field-centric coordinate system.
     * @param forward The forward-backward component. Between -1 and 1.
     * @param strafe The left-right component. Between -1 and 1.
     * @param turn The rotational component.
     */
    public void globalDrive(double forward, double strafe, double turn){
        if(disabled){return;}

        double y = HelperFunctions.clamp(forward,-1,1);
        double x = HelperFunctions.clamp(strafe,-1,1);
        double rot = HelperFunctions.clamp(turn,-1,1);

        double heading = this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);


        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

        rotX *= STRAFE_COEFFICIENT;

        localDrive(rotY,rotX,turn);
    }

    public Action snapToAngle(double radians){
        updatePoseEstimate();

        double currentRot = pose.heading.real;

        //double target = 0; // target is 0 degrees for now.

        double angleToTurn = -currentRot; // This works for a target of 0

        return new TurnAction(new TimeTurn(pose,angleToTurn,new TurnConstraints(20,0.1,2)));
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


            // if we have gone over the duration of the trajectory, stop the robot.
            if(t >= timeTrajectory.duration){
                leftFront.setPower(0);
                leftRear.setPower(0);
                rightFront.setPower(0);
                rightRear.setPower(0);

                return false;
            }


            Pose2dDual<Time> txWorldTarget = timeTrajectory.get(t);

            PoseVelocity2d robotVelRobot = updatePoseEstimate();

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

}
