package ftc.greenTeamCode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.ColumnMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.greenTeamCode.components.Camera;
import org.firstinspires.ftc.greenTeamCode.util.FakeAprilTagProcessor;
import org.firstinspires.ftc.greenTeamCode.util.Vector3;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Point;

public class CameraTest {

    private FakeAprilTagProcessor aprilTag;


    private Camera camera;

    private Telemetry telemetry;
    @Before
    public void before(){
        aprilTag = new FakeAprilTagProcessor();

        camera = new Camera(aprilTag,telemetry);
    }





    @Test
    public void testInFront(){
        aprilTag.addDetection(newDetection(32,"Tag",0,18,0,0,0,0));

        Vector3 out = camera.getAllDeltaPositions().get(0);

        Assert.assertEquals(0,out.getX(),0.00001);
        Assert.assertEquals(18,out.getY(),0.00001);
        Assert.assertEquals(0,out.getZ(),0.00001);
    }




    @Test
    public void testRight(){
        aprilTag.addDetection(newDetection(586,"Apriltatg",0,29,0,0,29*Math.sin(20),0));

        Vector3 out = camera.getAllDeltaPositions().get(0);

        Assert.assertEquals(20,out.getX(),0.0001);
        Assert.assertEquals(21,out.getY(),0.0001);


    }





    private AprilTagDetection newDetection(int id,String name,double x, double y, double z, double pitch, double yaw, double roll){
        return new AprilTagDetection(
                id,
                0,
                1,
                new Point(),
                new Point[0],
                new AprilTagMetadata(id,name,6, DistanceUnit.INCH),
                new AprilTagPoseFtc(x,y,z,pitch,yaw,roll,0,0,0),
                new AprilTagPoseRaw(x,y,z,new ColumnMatrixF(new VectorF((float)x,(float)y,(float)z))),
                0
            );
    }


}
