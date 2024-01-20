package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Claw;
import org.firstinspires.ftc.teamcode.components.Controller;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@Config
@TeleOp
public class ChangePIDValues extends OpMode {

    private Claw claw;

    private SimplerHardwareMap simplerHardwareMap;
    private Controller controller;

    // P = 10, I = 0.050003, D=0, F=0

    public static double P_VALUE=1.2;
    public static double I_VALUE=0;
    public static double D_VALUE=0.5;
    public static double F_VALUE=0;
    @Override
    public void init() {
        this.controller = new Controller(telemetry);
        this.simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);
        this.claw = new Claw(simplerHardwareMap,telemetry);



    }

    @Override
    public void loop() {
        controller.update(gamepad1,gamepad2);

        if(controller.intakeUpDown() > 0){
            claw.raiseClaw();
        }
        else if(controller.intakeUpDown() < 0){
            claw.lowerClaw();
        }

        if(controller.clawGrab()){
            claw.setPIDCoefficients(P_VALUE,I_VALUE,D_VALUE,F_VALUE);
        }
    }
}
