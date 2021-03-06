package data;

import javax.swing.UIManager;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.formdev.flatlaf.FlatLightLaf;

import certificate.SslUtil;
import test.MqttPublisher;
import ui.Gui;
import utils.ArrayListMaxSize;
import utils.TopicMessage;

public class Controller {

	static Controller instance;
	public String test;
	public Gui gui;
	public static MqttConnection mqttconnection;
	public MqttPublisher mqttpublisher;
	public MqttClient mqttclient;
	public Chart datenkurve;
	boolean issub;
	public String currentsubscribedtopic;
	public MqttOnMessage onMessage;
	public SslUtil ssl;
	ArrayListMaxSize<TopicMessage> stringnachricht;
	public int maxSizeMessage = 10;
	
	private Controller() {
		stringnachricht = new ArrayListMaxSize<TopicMessage>(maxSizeMessage);
		gui = new Gui();
		mqttconnection = new MqttConnection();
		mqttpublisher = new MqttPublisher();
		datenkurve = new Chart();
		ssl = new SslUtil();
		
		datenkurve.start();
		mqttpublisher.start();
	}
	
	public static void main(String[] args) {
	
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e54) {}
        
		Controller.getInstance();
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
		String vorlageende = "\n";
		
		stringnachricht.add(new TopicMessage(topic, nachricht));
		
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
				System.err.println("1-Class: Controller: " + e.getMessage());
			}
		}
		if (issub == false) {
			if (datenkurve != null) {
				datenkurve.setChart(topic);
			}
			try {
				mqttclient.subscribe(topic,0);
				currentsubscribedtopic = topic;
			} catch (MqttException e) {
				System.err.println("2-Class: Controller: " + e.getMessage());
			}
			
			issub = true;
		}
	}
	
	public void disconnect() {
		try {
			mqttclient.disconnect();
		} catch (MqttException e) {
		}
		if(!mqttclient.isConnected()) {
			
			instance.issub = false;
			instance.currentsubscribedtopic = "";
			instance.stringnachricht = new ArrayListMaxSize<TopicMessage>(maxSizeMessage);
			instance.stopmsg();	
			gui.loginscreeninit();

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
