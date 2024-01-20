package org.firstinspires.ftc.greenTeamCode.driveclasses;

public final class Params {
    // drive model parameters

   // 119 inches over 77353 ticks
    public double inPerTick = 0.001538401872;
    public double lateralInPerTick = 0.013718265422800798;
    public double trackWidthTicks = 8069.8276117876385;

    // feedforward parameters (in tick units)
    public double kS = 0.510818932703391;
    public double kV = 0.00037;

    public double kA = 0;

    // path profile parameters (in inches)
    public double maxWheelVel = 20; // 50
    public double minProfileAccel = -20; // -30
    public double maxProfileAccel = 20; // 50

    // turn profile parameters (in radians)
    public double maxAngVel = Math.PI; // shared with path
    public double maxAngAccel = Math.PI;

    // path controller gains
    public double axialGain = 10.0;
    public double lateralGain = 30.0;
    public double headingGain = 0.5; // shared with turn

    public double axialVelGain = 0.01;
    public double lateralVelGain = 0.01;
    public double headingVelGain = 0.11; // shared with turn
}
