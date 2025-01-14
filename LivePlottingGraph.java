package eecs1021;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LivePlottingGraph {
    static JFrame window = new JFrame();
    private static HashMap<Double,Double> dataMap = new HashMap<>();

    public static void createGraph(){
        window.setTitle("Graph of Soil Moisture Measurement over Time (0 = dry; 100 = very wet)");
        window.setSize(800,600);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        XYSeriesCollection dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createXYLineChart("Moisture over Time","Time (seconds)",
                "Moisture values in Percentage [0=dry; 100= very wet]",dataset, PlotOrientation.VERTICAL,true,true,false);
        NumberAxis yAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        yAxis.setRange(0,100);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);

        JLabel label = new JLabel();
        label.setLocation(550,250);
        label.setFont(new Font("Sans Serif",Font.ITALIC,15));
        window.add(label,BorderLayout.EAST);

        window.setVisible(true);
    }

    public static void updateGraph(double x, double y){

        dataMap.put(x ,y);
        XYSeries series = new XYSeries("Moisture over Time");
        for(Double key : dataMap.keySet()){
            series.add(key, dataMap.get(key));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart("Moisture over Time", "Time (seconds)",
                "Moisture values in % [0=dry; 100 = very wet]", dataset, PlotOrientation.VERTICAL,true,true,false);

        ChartPanel chartPanel = window.getContentPane().getComponentCount() > 0
                ? (ChartPanel) window.getContentPane().getComponent(0)
                : new ChartPanel(chart);

        if (window.getContentPane().getComponentCount() == 0) {
            window.add(chartPanel, BorderLayout.CENTER);
            //window.setVisible(true);
        }

        chartPanel.setChart(chart);
        window.repaint();
    }
}