package data;

import java.awt.BorderLayout;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.json.JSONObject;

import vorlagen.DataCollection;
import vorlagen.TopicTable;

public class DatenKurve extends Thread {

	/*
	 * 
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

	public void addData(String topic, JSONObject json) {
		current = (Second) current.next();
		try {
			collectionliste.get(topic).addDataSeries(current, json);
		} catch (Exception e) {
		}
	}

	private void createChartPanel(String topic, String yAchseName, TimeSeriesCollection chartcollection) {
		XYDataset dataset = chartcollection;
		chart = ChartFactory.createTimeSeriesChart(topic, "", "", dataset);
		panelchart = new ChartPanel(chart);
		panelchart.setSize(Controller.getInstance().gui.rightbottom.getSize());
		Controller.getInstance().gui.rightbottom.removeAll();
		Controller.getInstance().gui.rightbottom.add(panelchart, BorderLayout.CENTER);
		Controller.getInstance().gui.rightbottom.repaint();
	}

}
