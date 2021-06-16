package data;

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
				Controller.getInstance().gui.txt.append("Pub-Client connected\n");
//				System.out.println("Pub-Client Connected");
				TopicTable tp = new TopicTable();
				Object[][] a = tp.topics;
				 while (true) {
	
					String acc = "{'acc x': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'"+ ", " +
	        				"'acc y': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + ", " + "'acc z': "
							+ "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + "}";
					String mag = "{'mag x': " + "'" + String.valueOf(Math.round(Math.random() * 1000)) + "'" + ", "
							+ "'mag y': " + "'" + String.valueOf(Math.round(Math.random() * 1000)) + "'" + ", " + "'mag z': "
							+ "'" + String.valueOf(Math.round(Math.random() * 1000)) + "'" + "}";
					String gyro = "{'gyr x': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + ", "
							+ "'gyr y': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + ", " + "'gyr z': "
							+ "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + "}";
					String temp = "{'temp': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + "}";
					String pres = "{'press': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + "}";
					String hum = "{'humidity': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + "}";
					String w = ", " + "'temp': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" ;
					String q = ", " + "'q': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" ;
					String e = ", " + "'e': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" ;
					String t = "{'temp': " + "'" + String.valueOf(Math.round(Math.random() * 100)) + "'" + w + q + e+ "}";
					MqttMessage tempm = new MqttMessage(temp.getBytes());
					MqttMessage presm = new MqttMessage(pres.getBytes());
					MqttMessage humm = new MqttMessage(hum.getBytes());
					MqttMessage accm = new MqttMessage(acc.getBytes());
					MqttMessage gyrom = new MqttMessage(gyro.getBytes());
					MqttMessage magm = new MqttMessage(mag.getBytes());
	
					MqttMessage test = new MqttMessage(t.getBytes());	
					
					pub.publish("Temperatura", test);
					pub.publish(a[0][0].toString(), tempm);
					pub.publish(a[1][0].toString(), presm);
					pub.publish(a[2][0].toString(), humm);
					pub.publish(a[3][0].toString(), accm);
					pub.publish(a[4][0].toString(), gyrom);
					pub.publish(a[5][0].toString(), magm);
					
					pub.publish(a[6][0].toString(), tempm);
					pub.publish(a[7][0].toString(), presm);
					pub.publish(a[8][0].toString(), humm);
					pub.publish(a[9][0].toString(), accm);
					pub.publish(a[10][0].toString(), gyrom);
					pub.publish(a[11][0].toString(), magm);
	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				 }
			}
		} catch (MqttException e1) {
			e1.printStackTrace();
		}

	}

}
