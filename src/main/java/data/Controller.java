package data;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubComp;

import gui.Gui;
import vorlagen.ArrayListMaxSize;
import vorlagen.TopicNachrichten;

public class Controller {

	/*
	 * 
	 * 
	 * 
	 */

	private Controller() {
		init();
		mqttpublisher.start();
		mqttconnection.start();
	}

	static Controller instance;
	public String test;
	public Gui gui;
	public static MqttConnection mqttconnection;
	public MqttPublisher mqttpublisher;
	public MqttClient mqttclient;
	public DatenKurve datenkurve;
	boolean issub;
	public String currentsubscribedtopic;
	public MqttOnMessage onMessage;

	ArrayListMaxSize<TopicNachrichten> stringnachricht;

	public static void main(String[] args) {
		Controller.getInstance();
	}

	private void init() {
		stringnachricht = new ArrayListMaxSize<TopicNachrichten>(10);
		gui = new Gui();
		mqttconnection = new MqttConnection();
		mqttpublisher = new MqttPublisher();
		datenkurve = new DatenKurve();
		onMessage = new MqttOnMessage();
	}

	public static Controller getInstance() {
		if (Controller.instance == null) {
			Controller.instance = new Controller();
		}
		return instance;
	}

	public void addMessageConsole(String topic, MqttMessage nachricht) {
		String text = "";
		String vorlagetopic = "Topic: \"";
		String vorlagenachricht = "\" | Nachricht: \"";
		String vorlageende = "\"\n";
		
		stringnachricht.add(new TopicNachrichten(topic, nachricht));
		for (int i = 0; i < stringnachricht.size(); i++) {
			text += vorlagetopic + stringnachricht.get(i).getTopic() + vorlagenachricht
					+ stringnachricht.get(i).getnachricht().toString() + vorlageende;
		}
		gui.txt.setText(text);
	}

	public void subscribeto(String topic) {
		if (issub) {
			try {
				mqttclient.unsubscribe(currentsubscribedtopic);
				currentsubscribedtopic = null;
				issub = false;
				stringnachricht.clear();
				gui.txt.setText("");

			} catch (MqttException e) {
				e.printStackTrace();
			}

		}
		if (issub == false) {
			try {
				mqttclient.subscribe(topic);
				currentsubscribedtopic = topic;
				// DATENKURVE WIRD GEWECHSELT
				if (datenkurve != null) {
					datenkurve.setChart(topic);
				}

				issub = true;

			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startmsg() {
		mqttclient = MqttConnection.client;
		onMessage.start();
	}
	@SuppressWarnings("deprecation")
	public void stopmsg() {
		onMessage.stop();
	}
}
