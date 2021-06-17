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
					String error = "[Json-Format!]";
					if (Controller.getInstance().datenkurve != null) {
						try {
							JSONObject add = new JSONObject(new String(message.getPayload()));
							Controller.getInstance().datenkurve.addData(topic, add);
						} catch (JSONException e) {
							error = "[kein Json-Format]";
						}
					}
					Controller.getInstance().addMessageConsole(topic, message , error);
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
