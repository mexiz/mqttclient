package utils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TopicMessage {
	
	/*
	 * 
	 *  Diese Klasse übernimmt die Speicherung des Topics und der Nachricht.
	 *
	 * 
	 */
	
	
	MqttMessage nachricht;
	String topic;
	
	public TopicMessage(String topic, MqttMessage nachricht) {
		this.topic = topic;
		this.nachricht = nachricht;
	}
	
	public String getTopic() {
		
		return this.topic;
	}
	public MqttMessage getnachricht() {
		
		return this.nachricht;
	}

}
