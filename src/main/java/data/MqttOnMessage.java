package data;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class MqttOnMessage extends Thread{
	@Override
	public void run() {
		 Controller.getInstance().mqttclient.setCallback(new MqttCallback() {

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
