package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    private Gamepad gamepad;

    public Controller(Gamepad gamepad){
        this.gamepad = gamepad;
    }



    public Vector2d leftJoystick(){
        double y = -this.gamepad.left_stick_y;
        double x = this.gamepad.left_stick_x;

        return new Vector2d(x,y);
    }


    public Vector2d rightJoystick(){
        double y = -this.gamepad.right_stick_y;
        double x = this.gamepad.right_stick_x;

        return new Vector2d(x,y);
    }
}
