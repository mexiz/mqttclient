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

public class MqttConnection extends Thread {

	/*
	 * 
	 * tes
	 * 
	 */

	private MqttClient client;
//	JSONParser parser;

	public MqttConnection() {
		try {
			unverschluesselt();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void unverschluesselt() throws MqttException {

		client = new MqttClient("tcp://test.mosquitto.org:1883", UUID.randomUUID().toString());

		MqttConnectOptions options = new MqttConnectOptions();
		MqttToken token = (MqttToken) client.connectWithResult(options);
		if (client.isConnected()) {
			System.out.println("Sub-Client connected");
		} else {
			System.out.println("couldnt sub");

		}

	}

	public void verschluesselt() throws MqttException {

		client = new MqttClient("tcp://test.mosquitto.org:1883", UUID.randomUUID().toString());

		MqttConnectOptions options = new MqttConnectOptions();
		MqttToken token = (MqttToken) client.connectWithResult(options);
		if (client.isConnected())
			System.out.println("Sub-Client connected");
	}

	public MqttClient getMqttClient() {
		return client;
	}

//Nachrichten kommen hier an 
	@Override
	public void run() {
		client.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				Controller.getInstance().addMessageConsole(topic, message);
				if (Controller.getInstance().datenkurve != null) {
					Controller.getInstance().datenkurve.addData(topic,
							new JSONObject(new String(message.getPayload())));
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}

			@Override
			public void connectionLost(Throwable cause) {

			}
		});

	}
}
