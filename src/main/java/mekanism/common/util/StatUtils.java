package mekanism.common.util;

import java.util.Random;

import static java.lang.Math.*;

public class StatUtils {

    public static Random rand = new Random();

    public static int inversePoisson(double mean) {
        double r = rand.nextDouble() * exp(mean);
        int m = 0;
        double p = 1;
        double stirlingValue = mean * E;
        double stirlingCoeff = 1 / sqrt(2 * PI);
        while ((p < r) && (m < 3 * ceil(mean))) {
            m++;
            p += stirlingCoeff / sqrt(m) * pow(stirlingValue / m, m);
        }
        return m;
    }

    public static double min(double... vals) {
        double min = vals[0];
        for (int i = 1; i < vals.length; i++) {
            min = Math.min(min, vals[i]);
        }
        return min;
    }

    public static double max(double... vals) {
        double max = vals[0];
        for (int i = 1; i < vals.length; i++) {
            max = Math.max(max, vals[i]);
        }
        return max;
    }

    public static float wrapDegrees(float angle) {
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
}
