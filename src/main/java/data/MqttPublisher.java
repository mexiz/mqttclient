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
	MqttClient pub;
	@Override
	public void run() {
		
		try {
			
			pub = new MqttClient("tcp://test.mosquitto.org:1883", "zuba_pub_unencrypted");
//			pub = new MqttClient("tcp://127.0.0.1:1883", "zupa_pub_unencrypted");
			pub.connect();
			if (pub.isConnected()) {
				
//				Controller.getInstance().gui.txt.append("Pub-Client connected\n");
//				System.out.println("Pub-Client Connected");
				TopicTable tp = new TopicTable();
				@SuppressWarnings("static-access")
				Object[][] topic = tp.topics;
				 while (true) {
//					System.err.println("pub ip: " + pub.getCurrentServerURI() + "pub 2: " + pub.getServerURI());
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

					MqttMessage testm = new MqttMessage(t.getBytes());
					
					pub.publish(topic[0][0].toString(), temp.getBytes(), 0, false);
					pub.publish(topic[1][0].toString(), pres.getBytes(), 0, false);
					pub.publish(topic[2][0].toString(), hum.getBytes(), 0, false);
					pub.publish(topic[3][0].toString(), acc.getBytes(), 0, false);
					pub.publish(topic[4][0].toString(), gyro.getBytes(), 0, false);
					pub.publish(topic[5][0].toString(), mag.getBytes(), 0, false);
//					pub.publish(topic[6][0].toString(), testm);
//					pub.publish(topic[7][0].toString(), pres.getBytes(), 0, false);
//					pub.publish(topic[8][0].toString(), hum.getBytes(), 0, false);
//					pub.publish(topic[9][0].toString(), acc.getBytes(), 0, false);
//					pub.publish(topic[10][0].toString(), gyro.getBytes(), 0, false);
//					pub.publish(topic[11][0].toString(), mag.getBytes(), 0, false);


	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				 }
			}
		} catch (MqttException e1) {
			System.err.println("Class: publish: " + e1.getMessage());
		}

	}

}
