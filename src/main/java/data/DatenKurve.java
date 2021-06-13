package data;

import java.awt.BorderLayout;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Series;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import vorlagen.DataCollection;
import vorlagen.TopicTable;

public class DatenKurve {

	/*
	 * 
	 * 
	 */

	HashMap<String, DataCollection> collectionliste;

	JFreeChart chart;
	ChartPanel panelchart;
	Second current;

	boolean save = false;

	public DatenKurve() {
		collectionliste = new HashMap<String, DataCollection>();
		for (int i = 0; i < TopicTable.topics.length; i++) {
			collectionliste.put(TopicTable.topics[i][0].toString(), new DataCollection());
		}
	}

	public void setChart(String topic) {
		current = new Second();
		createChartPanel(topic, "temp", collectionliste.get(topic).getCollection());
	}

	public void addData(String topic, double werty) {
		current = (Second) current.next();
		collectionliste.get(topic).addDataSeries(current, werty);
		
	}

	private void createChartPanel(String topic, String yAchseName, TimeSeriesCollection chartcollection) {
		XYDataset dataset = chartcollection;
		chart = ChartFactory.createTimeSeriesChart(topic, "time", yAchseName, dataset);
		panelchart = new ChartPanel(chart);
		panelchart.setSize(Controller.getInstance().gui.rightbottom.getSize());
		Controller.getInstance().gui.rightbottom.removeAll();
		Controller.getInstance().gui.rightbottom.add(panelchart, BorderLayout.CENTER);
		Controller.getInstance().gui.rightbottom.repaint();
	}

}
