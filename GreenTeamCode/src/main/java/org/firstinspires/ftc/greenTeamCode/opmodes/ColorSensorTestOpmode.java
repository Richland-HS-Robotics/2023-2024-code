package org.firstinspires.ftc.greenTeamCode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous
public class ColorSensorTestOpmode extends OpMode {
    ColorSensor color;

    @Override
    public void init() {
        color = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    @Override
    public void loop() {
        telemetry.addData("Red",color.red());
        telemetry.addData("Green",color.green());
        telemetry.addData("Blue",color.blue());
    }
}
