package data;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
		mqttconnection.start();
		mqttpublisher.start();
	}

	static Controller instance;
	public String test;
	public Gui gui;
	public MqttConnection mqttconnection;
	public MqttPublisher mqttpublisher;
	public MqttClient mqttclient;
	public DatenKurve datenkurve;
	boolean issub;
	public String currentsubscribedtopic;

	ArrayListMaxSize<TopicNachrichten> stringnachricht;

	public static void main(String[] args) {
		Controller.getInstance();
	}

	private void init() {
//		message = new ArrayList<TopicNachrichten>();
		stringnachricht = new ArrayListMaxSize<TopicNachrichten>(10);
		gui = new Gui();
		mqttconnection = new MqttConnection();
		mqttpublisher = new MqttPublisher();
		datenkurve = new DatenKurve();

		mqttclient = mqttconnection.getMqttClient();

	}

	public static Controller getInstance() {
		if (Controller.instance == null) {
			Controller.instance = new Controller();
		}
		return instance;
	}

//ADDMESSAGE
	public void addMessageConsole(String topic, MqttMessage nachricht) {
		String text = "";

		stringnachricht.add(new TopicNachrichten(topic, nachricht));
		for (int i = 0; i < stringnachricht.size(); i++) {
			text += "Topic -> " + stringnachricht.get(i).getTopic() + " | Nachricht -> "
					+ stringnachricht.get(i).getnachricht().toString() + "\n";
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
}
