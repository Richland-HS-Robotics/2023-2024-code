package org.firstinspires.ftc.greenTeamCode.register;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.io.IOException;

public class OpModeRegister {
    private enum OpmodeType {
        LINEAR,
        LOOP,
    } ;


    @OpModeRegistrar
    public static void register(OpModeManager manager) {

        ArrayList<Class<? extends CustomOpMode>> loopClasses = new ArrayList<>();
        loopClasses.add(TestClass.class);


        for(Class<? extends CustomOpMode> clazz: loopClasses){
            OpModeMeta.Flavor flavor = OpModeMeta.Flavor.TELEOP;
            if(clazz.isAnnotationPresent(CustomTeleOp.class)){
                flavor = OpModeMeta.Flavor.TELEOP;
            }else if(clazz.isAnnotationPresent(CustomAuto.class)){
                flavor = OpModeMeta.Flavor.AUTONOMOUS;
            }

            OpmodeType type;
            if(clazz.isAnnotationPresent(Linear.class)){
                type = OpmodeType.LINEAR;
            }else{
                type = OpmodeType.LOOP;
            }

            OpModeMeta meta = new OpModeMeta.Builder()
                    .setName(clazz.getSimpleName())
                    .setGroup("All groups")
                    .setFlavor(flavor)
                    .build();

            manager.register(meta,createNewOpmode(clazz,type));
        }



    }


    /**
     * Create a new OpMode from a CustomOpMode.
     * @param clazz The class for the CustomOpMode.
     * @return The new OpMode, which runs the CustomOpMode.
     */
    private static OpMode createNewOpmode(Class<? extends CustomOpMode> clazz, OpmodeType type){
        try{
            Constructor<?> constructor = clazz.getDeclaredConstructor(
                    HardwareMap.class, Telemetry.class
            );
            if( type == OpmodeType.LOOP){
            OpMode op = new OpMode() {
                CustomOpMode opMode;
                @Override
                public void init() {
                    try {
                        this.opMode = (CustomOpMode) constructor.newInstance(hardwareMap,telemetry);
                    } catch (IllegalAccessException | InstantiationException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    opMode.init();
                }

                @Override
                public void loop() {
                    opMode.controller.update(gamepad1,gamepad2);
                    opMode.loop();
                }
            };
            return op;}
            else{
                OpMode op = new LinearOpMode() {
                    CustomOpMode opMode;
                    @Override
                    public void runOpMode() throws InterruptedException {
                        try{
                            this.opMode = (CustomOpMode) constructor.newInstance(hardwareMap,telemetry);
                        } catch (InvocationTargetException | IllegalAccessException |
                                 InstantiationException e) {
                            throw new RuntimeException(e);
                        }
                        opMode.init();

                        waitForStart();
                        opMode.loop();
                    }
                };
                return op;
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Scans all classes accessible from the context class loader which belong to the given package
     * and subpackages.
     * (See <a href="https://stackoverflow.com/a/520344">this stackoverflow answer</a>)
     *
     * @param packageName The base package
     * @return The classes
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     * (See <a href="https://stackoverflow.com/a/520344">this stackoverflow answer</a>)
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }


    private static OpModeMeta metaForClass(Class<? extends OpMode> cls,OpModeMeta.Flavor flavor) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName())
                .setGroup("basicGroup")
                .setFlavor(flavor)
                .build();
    }

}
