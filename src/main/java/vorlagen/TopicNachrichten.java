package vorlagen;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TopicNachrichten {
	
	/*
	 * 
	 *  
	 *
	 * 
	 */
	
	
	MqttMessage nachricht;
	String topic;
	String error;
	
	public TopicNachrichten(String topic, MqttMessage nachricht , String error) {
		this.topic = topic;
		this.nachricht = nachricht;
		this.error = error;
	}
	
	public String getTopic() {
		
		return this.topic;
	}
	public String getError() {
		
		return this.error;
	}
	public MqttMessage getnachricht() {
		
		return this.nachricht;
	}

}
