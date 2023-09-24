package org.firstinspires.ftc.teamcode.util;

public class HelperFunctions {
    /**
     * Clamp a value between a min and a max.
     * @param input The number to clamp.
     * @param min The minimum possible value.
     * @param max The maximum possible value.
     * @return The clamped value
     */
    public static double clamp(double input, double min, double max){
        if(input<min){
            return min;
        }else if(input > max){
            return max;
        }else{
            return input;
        }
    }


}
