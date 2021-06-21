package utils;

import java.util.ArrayList;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONException;
import org.json.JSONObject;

import data.Controller;

public class DataCollection {
	
	/*
	 * 
	 * Die Klasse übernimmt die Werte für die Datenkurve.
	 * Es wird in der Klasse DatenKurve ein Objekt von DataCollection erzeugt um die
	 * TimeSeries und TimeCollection zu speichern.
	 * 
	 */

	ArrayList<TimeSeries> series;
	TimeSeriesCollection collection;

	public DataCollection() {
	}

	public void addDataSeries(Second s, JSONObject json) {
		try {
			int i = 0;
			for (String key : json.keySet()) {
				//Neue Timeseries werden zur Arraylist hinzugefügt wenn keine vorhanden sind
				if (series.size() == i) {
					//first run
					if (i == 0)
						collection.removeAllSeries();
					TimeSeries add = new TimeSeries(key);
					//Es werden nur 10 Werte im Chart angezeigt
					add.setMaximumItemCount(Controller.getInstance().maxSizeMessage);
					series.add(add);
					collection.addSeries(series.get(i));
				}
				//Hier werden die Werte in die Series gesetzt
				series.get(i).add(s, json.getDouble(key));
				i++;
			}
		} catch (JSONException e) {
			System.err.println("Catch DATACOLLECTION: " + e.getMessage());
		}
	}

	public TimeSeriesCollection getCollection() {
		series = new ArrayList<TimeSeries>();
		collection = new TimeSeriesCollection();
		collection.addSeries(new TimeSeries("1"));
		return collection;
	}
}
