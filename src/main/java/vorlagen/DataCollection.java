package vorlagen;

import java.util.ArrayList;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class DataCollection {
	
	TimeSeries series1;
	TimeSeries series2;
	TimeSeries series3;
	TimeSeriesCollection collection;
	
	public DataCollection() {
		series1 = new TimeSeries("");
		collection = new TimeSeriesCollection();
	}
	
	public void addDataSeries(Second s, double wert) {
		series1.add(s,wert);
	}
	
	public void addDataSeries(Second s, double wert1, double wert2 , double wert3) {
		series2 = new TimeSeries("");
		series3 = new TimeSeries("");
		series1.add(s,wert1);
		series2.add(s,wert2);
		series3.add(s,wert3);
		
	}
	public TimeSeriesCollection getCollection() {
		series1.clear();
		collection.removeAllSeries();
		collection.addSeries(series1);
		
		if(series2 != null) {
			series2.clear();
			collection.addSeries(series2);
		}
		if(series3 != null) {
			series3.clear();
			collection.addSeries(series3);
		}
		
		return collection;
	}
	public void clear() {

		series1.clear();
	}
}
