package fr.polytech.melusine.utils;

import java.text.DecimalFormat;

public final class PriceFormatter {

    private PriceFormatter() {
    }

    public static double formatPrice(long price) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        return Double.valueOf(formatter.format(price / 100));
    }

}
