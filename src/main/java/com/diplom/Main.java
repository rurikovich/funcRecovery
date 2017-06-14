package com.diplom;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Main extends ApplicationFrame {
    static int m = 10;// на сколько частей разбиваем отрезок
    static double h = 0.2;// шаг для малого разбиения
    //--------------------------------

    static double jStep = 2 * h / m; // шаг по j

    static double[] fDeltaPoints = new double[]{0.5, -0.1, 0.6, -0.05, 0.8, 0.2, 0.95, 0.6, 1.1, 0.75};//это значения "плохой" функции в точках x_i
    //  я их сейчас взял для примера. 10 штук

    static double[] x = new double[m];

    public Main(String title, double[] x1, double[] y1, double[] x2, double[] y2, int m) {
        super(title);
        final XYSeries series = new XYSeries("Bad");
        final XYSeries series2 = new XYSeries("Good");
        for (int i = 0; i < m; i++) {
            series.add(x1[i], y1[i]);
            series2.add(x2[i], y2[i]);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series2);


        final JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Series Demo",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    static void init() {
        //{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        for (int i = 0; i < x.length; i++) {
            x[i] = ((double) i) / m;
        }
    }

    public static void main(String[] args) {
        init();
        double[] f_x = new double[m];

        for (int i = 0; i < m; i++) {
            double sum = 0;
            double start = x[i] - h > x[0] ? x[i] - h : x[0];
            double end = x[i] + h <= x[m - 1] ? x[i] + h : x[m - 1];
            for (double t = start; t < end; t += jStep) {
                sum += fDelta(i, t);
            }
            f_x[i] = 1 / Math.sqrt(2 * h) * (2 * h / m) * sum;
        }

        for (int i = 0; i < f_x.length; i++) {
            System.out.println(String.format("(%s,%s)", x[i], f_x[i]));
        }


        final Main demo = new Main("", x, fDeltaPoints, x, f_x, m);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

    private static double fDelta(int i, double t) {
        if (t < x[i]) {
            return line(x[i - 1], fDeltaPoints[i - 1], x[i], fDeltaPoints[i], t);
        } else {
            return line(x[i], fDeltaPoints[i], x[i + 1], fDeltaPoints[i + 1], t);
        }
    }

    private static double line(double x1, double y1, double x2, double y2, double x) {
        return -((y1 - y2) * x + (x1 * y2 - x2 * y1)) / (x2 - x1);
    }

}
