package org.firstinspires.ftc.teamcode.driveclasses;

public final class Params {
    // drive model parameters

   // 165 inches over 105447.5 ticks
    public double inPerTick = 0.001564759715;
    public double lateralInPerTick = 0.013718265422800798;
    public double trackWidthTicks = 283.11119590089027;

    // feedforward parameters (in tick units)
    public double kS = 0.6712553335877338;
    public double kV = 0.0007835737129009071;
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
