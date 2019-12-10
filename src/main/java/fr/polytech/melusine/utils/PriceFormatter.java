package fr.polytech.melusine.utils;

import java.text.DecimalFormat;

public final class PriceFormatter {

    private PriceFormatter() {
    }

    public static double formatToDouble(long price) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        return Double.valueOf(formatter.format(price / 100));
    }

    public static long formatToLong(double credit) {
        return Math.round(credit * 100);
    }

}
