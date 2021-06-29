package utils;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TopicTable extends DefaultTableModel {
	

	public static String[] coloum = { "Name des Topics" };

//	public static Object[][] topics = { { "Temperature" }, { "Pressure" }, { "Humidity" }, { "Accelleration" },
//			{ "Gyrodata" }, { "Magdata" } , { "Temperature/TEST" }, { "Pressure/TEST" }, { "Humidity/TEST" }, { "Accelleration/TEST" },
//			{ "Gyrodata/TEST" }, { "Magdata/TEST" } ,{"Temperatura"} };
	
	public static Object[][] topics = { { "Temperature" }, { "Pressure" }, { "Humidity" }, { "Accelleration" },{ "Gyrodata" }, { "Magdata" } };

	public TopicTable(Object[][] data, String[] names) {
		super(data, names);
	}

	public TopicTable() {
		super(topics, coloum);
	}
	
	public void addTopictoTable(String topic) {
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}