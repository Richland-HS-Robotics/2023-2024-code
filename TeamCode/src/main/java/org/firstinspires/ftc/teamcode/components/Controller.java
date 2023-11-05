package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Pair;
import org.firstinspires.ftc.teamcode.util.Triple;



/*
 1 Gamepad
 */

/*
 2 Gamepads:
 - [X] XY: gamepad1 left_joystick
 - [X] Rotation: gamepad1 right_joystick-x
 - [X] Launch airplane: gamepad1 b
 - [X] field/robot mode swap: gamepad1 a (falling edge, toggle)
 - [ ] slow movement: gamepad1 right_trigger
 - [X] slide up/down: gamepad2 left-joystick-y
 - [X] claw up/down: gamepad2 right-joystick-y
 - [X] release pixel L: gamepad2 y (rising edge)
 - [X] release pixel R: gamepad2 b (rising edge)
 - [ ] Grab pixel: gamepad2 right-trigger
 */


/**
 * A class for managing the gamepads, both 1 and 2 gamepads.
 */
public class Controller {
    private Gamepad currentGamepad1;
    private Gamepad currentGamepad2;
    private Gamepad prevGamepad1;
    private Gamepad prevGamepad2;

    private Telemetry telemetry;

    private ControlMode controlMode;

    public enum ControlMode{
        ONE_DRIVER,
        TWO_DRIVERS
    }
    public Controller(Telemetry telemetry){
        this.currentGamepad1 = new Gamepad();
        this.currentGamepad2 = new Gamepad();

        this.prevGamepad1 = new Gamepad();
        this.prevGamepad2 = new Gamepad();

        this.telemetry = telemetry;

    }


    public void setControlMode(ControlMode mode){
        this.controlMode = mode;
    }

    /**
     * Update the state. This must be called every tick.
     * @param gamepad1 Gamepad 1.
     * @param gamepad2 Gamepad 2
     */
    public void update(Gamepad gamepad1, Gamepad gamepad2){
        boolean g1Exists = gamepad1 != null;
        boolean g2Exists = gamepad2 != null;

        if (g1Exists){
            this.prevGamepad1.copy(this.currentGamepad1);
            this.currentGamepad1.copy(gamepad1);
        }

        if (g2Exists){
            this.prevGamepad2.copy(this.currentGamepad2);
            this.currentGamepad2.copy(gamepad2);
        }

        if (g1Exists && g2Exists){
            this.controlMode = ControlMode.TWO_DRIVERS;
        }
        else if(g1Exists){
            this.controlMode = ControlMode.ONE_DRIVER;
        }

    }


    public Triple<Double, Double, Double> movementControl(){
        return new Triple<>(
                (double) currentGamepad1.left_stick_x,
                (double) -currentGamepad1.left_stick_y,
                (double) currentGamepad1.right_stick_x
        );
    }


    /**
     * Returns the direction the linear slide should go.
     *
     * For 2 drivers it uses the second gamepad left stick y,
     * and for 1 driver it uses the right stick y. (same stick as rotation)
     *
     * @return Direction of linear slide, from -1 to 1
     */
    public double linearSlideInOut(){
        if(controlMode == ControlMode.ONE_DRIVER) {
            return -currentGamepad1.right_stick_y;
        }else if(controlMode == ControlMode.TWO_DRIVERS){
            return -currentGamepad2.left_stick_y;
        }else{
            return 0;
        }
    }

    /**
     * Returns whether the airplane should be launched
     * @return
     */
    public boolean launchAirplane(){
        if(controlMode==ControlMode.TWO_DRIVERS){
            return currentGamepad1.b;
        }else{
            return currentGamepad1.x;
        }
    }


    /**
     * Returns whether either pixel should be released.
     * @return A pair of booleans. The first is left and the second is right.
     */
    public Pair<Boolean, Boolean> releasePixels(){
        if(controlMode == ControlMode.TWO_DRIVERS){
            return new Pair<>(currentGamepad2.y,currentGamepad2.x);
        }else{
            return new Pair<>(currentGamepad1.b,currentGamepad1.a);
        }
    }



    /**
     * Converts 2 button inputs (like the dpad) to a double input.
     * @param b1 The first button. When this is pressed the output will be high.
     * @param b2 The second button. When this is pressed the output will be low.
     * @return An analog output based on both buttons.
     */
    private double twoButtonsToAnalog(boolean b1,boolean b2){
        if (b1 && !b2){
            return 1;
        }
        if(b2 && !b1){
            return -1;
        }
        if(!b1 && !b2){
            return 0;
        }
        if(b1 && b2){
            return 0;
        }
        return 0;
    }



    public double movementSlownessFactor(){
        return 1 - currentGamepad1.right_trigger;
    }


    public double intakeUpDown(){
        if(controlMode == ControlMode.TWO_DRIVERS) {
            telemetry.addData("Two drivers: ",-currentGamepad2.right_stick_y);
            return -currentGamepad2.right_stick_y;
        }else{
            telemetry.addData("One driver: ",twoButtonsToAnalog(currentGamepad1.dpad_up,currentGamepad1.dpad_down));
            return twoButtonsToAnalog(currentGamepad1.dpad_up,currentGamepad1.dpad_down);
        }
    }

    /**
     * Detects if the field/robot centric button has been pressed. This will
     * trigger only once, on the rising edge.
     * @return Whether to toggle modes
     */
    public boolean fieldRobotCentricToggle(){
        // This triggers only on the rising edge.
        return (currentGamepad1.a && !prevGamepad1.a);
    }

    public boolean clawGrab(){
        if(controlMode == ControlMode.TWO_DRIVERS){
            return currentGamepad2.right_trigger > 0.5;

        }else{
            return currentGamepad1.left_trigger > 0.5;
        }
    }
}
