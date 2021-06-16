package data;

import java.util.UUID;

import javax.net.ssl.SSLSocketFactory;

import org.bouncycastle.util.Properties;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConnection extends Thread {

	/*
	 * 
	 * 
	 * 
	 * 
	 */

//	static MqttAsyncClient client;
	static MqttClient client;
	String clientid;

	String cacert    = "C:\\Users\\markus\\Documents\\GitHub\\zertifikate_mosquitto\\ca.pem";
	String clientkey = "C:\\Users\\markus\\Documents\\GitHub\\zertifikate_mosquitto\\clientkey.pem";
	String clientpem = "C:\\Users\\markus\\Documents\\GitHub\\zertifikate_mosquitto\\client.pem";
	
	public MqttConnection() {
//		clientid = UUID.randomUUID().toString();
		clientid = "markusseinclient";
	}
	public boolean connectionunverschluesselt(String server, String port, String username, String passwort)
			throws MqttException {
		
		String ip = "tcp://" + server + ":" + port;
		client = new MqttClient(ip, clientid);
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		if(port.matches("1883")) {
			try {
				client.connect(options);
			} catch (MqttException e) {
				System.err.println("Couldn´t connect client");
			}
		}
		if(port.matches("1884")) {
			System.out.println(port);
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
				try {
					client.connect(options);
				} catch (MqttException e) {
					System.err.println("Couldn´t connect client");
				}
			}
		}
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
			Controller.getInstance().gui.txt.append("Client connected \n");
		}
		
		return client.isConnected();
	}
	public boolean connectionverschluesselt(String server, String port, String username, String passwort)
			throws MqttException {
		
		String ip = "ssl://" + server + ":" + port;
		client = new MqttClient(ip, clientid);
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		if(port.matches("8883")) {
			System.out.println(port);
			try {
				options.setSocketFactory(Controller.getInstance().ssl.getSocketFactory(cacert, clientpem, clientkey, ""));
				System.out.println("DURCH!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				client.connect(options);
			} catch (MqttException e) {
				System.err.println(e.getMessage());
				System.err.println("Couldn´t connect client");
			}
		}else if(port.matches("8884")) {
			System.out.println(port);
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
				try {
					client.connect(options);
				} catch (MqttException e) {
					System.err.println("Couldn´t connect client");
				}
			}
		}
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
			Controller.getInstance().gui.txt.append("Client connected \n");
		}
		return client.isConnected();
	}
}
