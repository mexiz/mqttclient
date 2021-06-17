package vorlagen;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TopicTable extends DefaultTableModel {
	
	/*
	 * 
	 * In der Gui werden die Topics in Form eines Tables angezeigt.
	 * Eventuell für manuelle in der Gui eingetragene Topics.
	 * 
	 */

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


//	public Class getColumnClass(int columnIndex) {
//		return String.class;
//	}
	
	public void addTopictoTable(String topic) {
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}