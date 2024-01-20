package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.components.Controller;
import org.firstinspires.ftc.teamcode.util.RealSimplerHardwareMap;
import org.firstinspires.ftc.teamcode.util.SimplerHardwareMap;

@TeleOp
public class IntakeOuttakeTest extends OpMode {


    Controller controller;
//    IntakeOutake io;
    LinearSlide linearSlide;

    SimplerHardwareMap simplerHardwareMap;

    @Override
    public void init() {
        this.simplerHardwareMap = new RealSimplerHardwareMap(hardwareMap);
        this.controller = new Controller(telemetry);
        this.linearSlide = new LinearSlide(simplerHardwareMap,telemetry);
        //this.io=new IntakeOutake(simplerHardwareMap,telemetry,controller);
    }

    @Override
    public void loop() {
        if(controller.linearSlideInOut() < 0){
            linearSlide.setSlidePosition(0);
        }else if(controller.linearSlideInOut() > 0){
            linearSlide.setSlidePosition(1);
        }

    }
}
