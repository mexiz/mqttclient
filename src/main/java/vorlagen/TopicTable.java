package vorlagen;

import javax.swing.table.DefaultTableModel;

public class TopicTable extends DefaultTableModel {
	
	/*
	 * 
	 * 
	 * 
	 */

	public static String[] coloum = { "Name" };

	public static Object[][] topics = { { "Temperature" }, { "Pressure" }, { "Humidity" }, { "Accelleration" },
			{ "Gyrodata" }, { "Magdata" } , { "Temperature/TEST" }, { "Pressure/TEST" }, { "Humidity/TEST" }, { "Accelleration/TEST" },
			{ "Gyrodata/TEST" }, { "Magdata/TEST" } ,{"Temperatura"} };
	
//	public static Object[][] topics = { { "Temperature" }, { "Pressure" }, { "Humidity" }, { "Accelleration" },{ "Gyrodata" }, { "Magdata" } };

	public TopicTable(Object[][] data, String[] names) {
		super(data, names);
	}

	public TopicTable() {
		super(topics, coloum);
	}


	public Class getColumnClass(int columnIndex) {
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}