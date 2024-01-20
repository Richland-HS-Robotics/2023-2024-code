package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;


/**
 * An implementation of {@link SimplerHardwareMap}, using the FTC hardwareMap variable.
 */
public class RealSimplerHardwareMap implements SimplerHardwareMap{
    private final HardwareMap realHardwareMap;

    public RealSimplerHardwareMap(HardwareMap hardwareMap){
        this.realHardwareMap = hardwareMap;
    }

    @Override
    public HardwareMap.DeviceMapping<VoltageSensor> voltageSensor() {
        return realHardwareMap.voltageSensor;
    }

    @Override
    public <T> T get(Class<? extends T> classOrInterface, String deviceName) {
        return realHardwareMap.get(classOrInterface,deviceName);
    }

    @Override
    public <T> List<T> getAll(Class<? extends T> classOrInterface) {
        return realHardwareMap.getAll(classOrInterface);
    }

    @Override
    public Set<String> getNamesOf(HardwareDevice device) {
        return realHardwareMap.getNamesOf(device);
    }

    public void getVoltageSensor(){

    }



    @Override
    public int hashCode(){
        return realHardwareMap.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj){
        return realHardwareMap.equals(obj);
    }

    @NonNull
    @Override
    public String toString(){
        return realHardwareMap.toString();
    }
}
