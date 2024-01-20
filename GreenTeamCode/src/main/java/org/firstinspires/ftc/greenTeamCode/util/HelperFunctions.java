package org.firstinspires.ftc.greenTeamCode.util;

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


    /**
     * Check if two values are equal to a certain threshold
     * @param a The first value
     * @param b The second value
     * @param threshold The threshold value
     * @return Whether the values should be considered equal.
     */
   public static boolean compare(double a, double b,double threshold){
       return Math.abs(a-b)<threshold;
   }


    /**
     * Return whether an input is greater or equal to a threshold, within some error.
     * @param input The input to compare
     * @param threshold The threshold to compare the input to
     * @param err The allowed error
     * @return Whether the input is greater than the threshold, or equal to it
     * within err.
     */
    public static boolean ge_threshold(double input,double threshold,double err){
        return (input > threshold) || compare(input,threshold,err);
    }

    /**
     * Return whether an input is less than or equal to a threshold, within some error.
     * @param input The input to compare
     * @param threshold The threshold to compare the input to
     * @param err The allowed error
     * @return Whether the input is less than the threshold, or equal to it
     * within err.
     */
    public static boolean le_threshold(double input, double threshold, double err){
        return (input < threshold) || compare(input,threshold,err);
    }

}
