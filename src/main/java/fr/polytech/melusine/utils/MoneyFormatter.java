package fr.polytech.melusine.utils;

public final class MoneyFormatter {

    private MoneyFormatter() {
    }

    public static Double formatToDouble(long price) {
        return price / 100.0;
    }

    public static long formatToLong(double credit) {
        return Math.round(credit * 100);
    }

}
