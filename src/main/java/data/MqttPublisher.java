package data;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import vorlagen.TopicTable;

public class MqttPublisher extends Thread {

	/*
	 * 
	 * TEST PUBLISH
	 * 
	 */

	@Override
	public void run() {
		MqttClient pub;
		try {
			pub = new MqttClient("tcp://test.mosquitto.org:1883", MqttClient.generateClientId());
			pub.connect();
			if (pub.isConnected()) {
				System.out.println("Pub-Client Connected");
			}
			TopicTable tp = new TopicTable();
			Object[][] a = tp.topics;
			while (true) {

//				Controller.getInstance().datenkurve.addData("test", j, j+1);
//				j++;

//				String g = String.valueOf(Math.round(Math.random()*10));
//				System.out.println(g);
//				MqttMessage m = new MqttMessage(g.getBytes());
//				pub.publish(a[1][0].toString(), m);
				for (int i = 5; i < a.length; i++) {
//					String g = String.valueOf(Math.round(Math.random()*100));
//					MqttMessage m = new MqttMessage(g.getBytes());
//					pub.publish(a[i][0].toString(), m);
//					System.out.println(a[i][0].toString() + m.toString());
//					System.out.println(g);
				}
				String t = String.valueOf(Math.round(Math.random() * 100));
//				String t = "{\"temp\": " + "\"" + String.valueOf(Math.round(Math.random()*100) + "\"" +"}");
				
				MqttMessage c = new MqttMessage(t.getBytes());
				pub.publish(a[6][0].toString(), c);
				pub.publish(a[7][0].toString(), c);
				pub.publish(a[8][0].toString(), c);
				pub.publish(a[9][0].toString(), c);
				pub.publish(a[10][0].toString(), c);
				pub.publish(a[11][0].toString(), c);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (MqttException e1) {
			e1.printStackTrace();
		}

	}

}
