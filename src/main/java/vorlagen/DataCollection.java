package vorlagen;

import java.util.ArrayList;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONException;
import org.json.JSONObject;

import data.Controller;

public class DataCollection {

	ArrayList<TimeSeries> series;
	TimeSeriesCollection collection;

	public DataCollection() {
	}

	public void addDataSeries(Second s, JSONObject json) {
		int i = 0;
		try {
			for (String key : json.keySet()) {
				if (series.size() == i) {
					if (i == 0)
						collection.removeAllSeries();
					TimeSeries add = new TimeSeries(key);
					add.setMaximumItemCount(Controller.getInstance().maxanzahlnachrichten);
					series.add(add);
					collection.addSeries(series.get(i));
				}
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
