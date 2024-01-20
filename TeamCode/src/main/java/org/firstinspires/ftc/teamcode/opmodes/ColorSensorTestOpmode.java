package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous
public class ColorSensorTestOpmode extends OpMode {
    ColorRangeSensor color;


    @Override
    public void init() {
        color = hardwareMap.get(ColorRangeSensor.class, "colorSensor");
    }

    @Override
    public void loop() {
        telemetry.addData("Red",color.red());
        telemetry.addData("Green",color.green());
        telemetry.addData("Blue",color.blue());
    }
}
