package org.firstinspires.ftc.teamcode;

public final class Params {
    // drive model parameters

    // 122 inches over 6632.75 ticks
    public double inPerTick = 0.01839357732;
    // 120 inches over 5953.5 ticks
    public double lateralInPerTick = 0.02015621063;
    public double trackWidthTicks = 1043.335391094545;

    // feedforward parameters (in tick units)
    public double kS = 0.7013313423791585;
    public double kV = 0.004233626947138038;
    public double kA = 0.001;

    // path profile parameters (in inches)
    public double maxWheelVel = 50;
    public double minProfileAccel = -30;
    public double maxProfileAccel = 50;

    // turn profile parameters (in radians)
    public double maxAngVel = Math.PI; // shared with path
    public double maxAngAccel = Math.PI;

    // path controller gains
    public double axialGain = 0.0;
    public double lateralGain = 0.0;
    public double headingGain = 0.0; // shared with turn

    public double axialVelGain = 0.0;
    public double lateralVelGain = 0.0;
    public double headingVelGain = 0.0; // shared with turn
}
