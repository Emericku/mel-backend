package fr.polytech.melusine.models.charts;

public interface ChartPoint<R, T> {

    R getOrdinate();

    T getAbscissa();

}
