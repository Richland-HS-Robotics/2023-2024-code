package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Controller;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@TeleOp(name = "Basic TeleOp")
public class BasicTeleOp extends OpMode {

    private SimplerHardwareMap simplerHardwareMap;
    private DriveTrain driveTrain;

    private Controller controller;

    @Override
    public void init() {
        this.simplerHardwareMap = new RealSimplerHardwareMap(this.hardwareMap);

        this.driveTrain = new DriveTrain(simplerHardwareMap,telemetry);
        this.controller = new Controller(gamepad1);

    }

    @Override
    public void loop() {
        localDrive();
    }


    private void localDrive(){
        driveTrain.localDrive(
                controller.leftJoystick().x,
                controller.leftJoystick().y,
                controller.rightJoystick().x
        );
    }


    private void globalDrive(){
        driveTrain.globalDrive(
                controller.leftJoystick().x,
                controller.leftJoystick().y,
                controller.rightJoystick().x
        );
    }
}
