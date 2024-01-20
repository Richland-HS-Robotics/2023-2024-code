package org.firstinspires.ftc.greenTeamCode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.greenTeamCode.components.AirplaneLauncher;
import org.firstinspires.ftc.greenTeamCode.components.Controller;
import org.firstinspires.ftc.greenTeamCode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.greenTeamCode.util.SimplerHardwareMap;

@TeleOp
public class AirplaneTestOpMode extends OpMode {
    AirplaneLauncher airplaneLauncher;
    Controller controller;

    SimplerHardwareMap simplerHardwareMap;

    @Override
    public void init() {
        simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);
        airplaneLauncher = new AirplaneLauncher(simplerHardwareMap,telemetry);

        controller = new Controller(telemetry);
    }

    @Override
    public void loop() {
        controller.update(gamepad1,gamepad2);

        if(controller.launchAirplane()){
            airplaneLauncher.fire();
        }else{
            airplaneLauncher.unfire();
        }
    }
}
