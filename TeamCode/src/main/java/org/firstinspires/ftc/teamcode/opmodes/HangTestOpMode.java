package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class HangTestOpMode extends OpMode {

    DcMotorEx hangMotor;

    @Override
    public void init() {
        hangMotor = hardwareMap.get(DcMotorEx.class,"hangMotor");
    }

    @Override
    public void loop() {
        hangMotor.setPower(-gamepad1.left_stick_y);
    }
}
