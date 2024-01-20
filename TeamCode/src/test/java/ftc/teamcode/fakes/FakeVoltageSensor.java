package ftc.teamcode.fakes;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class FakeVoltageSensor implements VoltageSensor {
    @Override
    public double getVoltage() {
        return 12; // normal voltage
    }

    @Override
    public Manufacturer getManufacturer() {
        return HardwareDevice.Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "Fake Voltage Sensor";
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
