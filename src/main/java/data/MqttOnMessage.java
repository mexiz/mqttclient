package data;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class MqttOnMessage extends Thread{
	@Override
	public void run() {
		 Controller.getInstance().mqttclient.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					if (Controller.getInstance().datenkurve != null) {
						try {
							JSONObject add = new JSONObject(new String(message.getPayload()));
							//Only when message is a JsonObject
							Controller.getInstance().datenkurve.addData(topic, add);
							Controller.getInstance().addMessageConsole(topic, message);
						} catch (JSONException e) {
						}
					}
					
				}
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}

				@Override
				public void connectionLost(Throwable cause) {
					//disconnect when connection lost
					Controller.getInstance().disconnect();

				}
			});
	}

}
