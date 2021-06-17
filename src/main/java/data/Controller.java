package data;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import gui.Gui;
import vorlagen.ArrayListMaxSize;
import vorlagen.TopicNachrichten;
import zertifikat.SslUtil;

public class Controller {

	/*
	 * 
	 * 
	 * 
	 */


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
	public SslUtil ssl;
	ArrayListMaxSize<TopicNachrichten> stringnachricht;
	public int maxanzahlnachrichten = 10;
	
	private Controller() {
		stringnachricht = new ArrayListMaxSize<TopicNachrichten>(maxanzahlnachrichten);
		gui = new Gui();
		mqttconnection = new MqttConnection();
		mqttpublisher = new MqttPublisher();
		datenkurve = new DatenKurve();
		ssl = new SslUtil();
		
		datenkurve.start();
		mqttpublisher.start();
	}
	
	public static void main(String[] args) {
		Controller.getInstance();
	}

	public static Controller getInstance() {
		if (Controller.instance == null) {
			Controller.instance = new Controller();
		}
		return instance;
	}

	public void addMessageConsole(String topic, MqttMessage nachricht , String error) {
		String text = "";
		String vorlagetopic = "Topic: \"";
		String vorlagenachricht = "\" | Nachricht: \"";
		String vorlageende = "\n";
		
		stringnachricht.add(new TopicNachrichten(topic, nachricht, error));
		
		for (int i = 0; i < stringnachricht.size(); i++) {
			text += vorlagetopic + stringnachricht.get(i).getTopic() + vorlagenachricht
					+ stringnachricht.get(i).getnachricht().toString() + "\" | " + stringnachricht.get(i).getError().toString() + vorlageende;
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
				System.err.println("1Class: Controller: " + e.getMessage());
			}
		}
		if (issub == false) {
			// DATENCHART WIRD GEWECHSELT
			if (datenkurve != null) {
				datenkurve.setChart(topic);
			}
			try {
				mqttclient.subscribe(topic);	
				currentsubscribedtopic = topic;
			} catch (MqttException e) {
				System.err.println("2Class: Controller: " + e.getMessage());
			}
			
			issub = true;
		}
	}
	public void unsubscribetocurrent() {
		if (issub) {
			try {
				mqttclient.unsubscribe(currentsubscribedtopic);
				currentsubscribedtopic = null;
				issub = false;
				stringnachricht.clear();
				gui.txt.setText("");
			} catch (MqttException e) {
				System.err.println("1Class: Controller: " + e.getMessage());
			}
		}
		
	}
	
	public void disconnect() {
		instance.unsubscribetocurrent();
//		instance.gui.rightbottom.removeAll();
//		instance.gui.rightbottom.repaint();
		instance.stopmsg();
		try {
			mqttclient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void startmsg() {
		mqttclient = MqttConnection.client;
		onMessage = new MqttOnMessage();
		onMessage.start();
	}
	@SuppressWarnings("deprecation")
	public void stopmsg() {
		onMessage.stop();
		onMessage = null;
	}
}
