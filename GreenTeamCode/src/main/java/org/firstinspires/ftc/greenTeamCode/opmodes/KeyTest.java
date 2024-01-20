package org.firstinspires.ftc.greenTeamCode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.greenTeamCode.components.Controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class KeyTest extends OpMode {

    ArrayList<Controller.OneDriverControl> oneDriverKeys;
    ArrayList<Controller.TwoDriverControl> twoDriverKeys;

    @Override
    public void init() {
        oneDriverKeys = getDriverInfo(Controller.OneDriverControl.class);
        twoDriverKeys = getDriverInfo(Controller.TwoDriverControl.class);
    }

    @Override
    public void loop() {

    }


    public <T extends Annotation> ArrayList<T> getDriverInfo(Class<T> annotation){

        ArrayList<T> list = new ArrayList<>();

        Class<Controller> clazz = Controller.class;


        for(Method method : clazz.getDeclaredMethods()){
             T currentAnnotation = method.getAnnotation(annotation);
            if(currentAnnotation != null){
                list.add(currentAnnotation);
            }
        }

        return list;
    }


}
