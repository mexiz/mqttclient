package data;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.json.JSONObject;

public class MqttConnection extends Thread{

	/*
	 * 
	 * 
	 * 
	 */

	static MqttClient client;
	String clientid;
	
	public MqttConnection() {
	}

	public boolean connectionunverschluesselt(String server, String port) throws MqttException {
//		if (client.isConnected()) {
//			client.disconnect();
//			System.out.println("Sub-Client connected");
//		}
		String ip = "tcp://" + server + ":" + port;
		clientid = UUID.randomUUID().toString();
		client = new MqttClient(ip, clientid);
		try {
			client.connect();
		} catch (MqttException e) {
			System.err.println("hi");
		}
		
//		MqttConnectOptions options = new MqttConnectOptions();
//		MqttToken token = (MqttToken) client.connectWithResult(options);
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
			System.out.println("Sub-Client connected");	
		}
		return client.isConnected();
	}
	public boolean connectionverschluesselt(String server, String port) throws MqttException {

		String ip = "tcp://" + server + ":" + port;
		clientid = UUID.randomUUID().toString();
		client = new MqttClient(ip, clientid);
		MqttConnectOptions options = new MqttConnectOptions();
		MqttToken token = (MqttToken) client.connectWithResult(options);
		if (client.isConnected()) {
			System.out.println("Sub-Client connected");
		} else {
			System.err.println("Connectui");

		}
		return client.isConnected();
	}
}
