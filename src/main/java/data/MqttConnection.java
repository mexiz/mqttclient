package data;

import java.io.File;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConnection {

	/*
	 * 
	 * 
	 * 
	 * 
	 */

	static MqttClient client;
	String clientid;
	
	public File cacertfile;
	public File clientkeyfile;
	public File clientpemfile;
	
	
	public MqttConnection() {
//		clientid = UUID.randomUUID().toString();
		clientid = "markus_client";
		
		try {
			cacertfile = new File("C:\\Users\\markus\\Documents\\GitHub\\Zertifikate\\mosquitto_certificate\\ca.pem");
			clientkeyfile = new File("C:\\Users\\markus\\Documents\\GitHub\\Zertifikate\\mosquitto_certificate\\clientkey.pem");
			clientpemfile = new File("C:\\Users\\markus\\Documents\\GitHub\\Zertifikate\\mosquitto_certificate\\client.pem");
		} catch (Exception e) {
		}
		
	}
	public boolean connectionunverschluesselt(String server, String port, String username, String passwort){

		MqttConnectOptions options;
		
		if(port.matches("1883")) {
			String ip = "tcp://" + server + ":" + port;
			options = new MqttConnectOptions();
			options.setCleanSession(true);
			try {
				client = new MqttClient(ip, clientid);
				client.connect(options);
			} catch (MqttException e1) {
				System.err.println("1Class: MqttConnection: " + e1.getMessage());
			}
		}else if(port.matches("1884")) {
			String ip = "tcp://" + server + ":" + port;
			options = new MqttConnectOptions();
			options.setCleanSession(true);
			try {
				client = new MqttClient(ip, clientid);
			} catch (MqttException e1) {
				System.err.println("2.5Class: MqttConnection: " + e1.getMessage());
			}
			System.out.println(port);
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
				try {
					client.connect(options);
				} catch (MqttException e) {
					System.err.println("3Class: MqttConnection: " + e.getMessage());
				}
			}
		}
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
		}
		return client.isConnected();
	}
	public boolean connectionverschluesselt(String server, String port, String username, String passwort){
	
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		if(port.matches("8883")) {
			options = new MqttConnectOptions();
			String ip = "ssl://" + server + ":" + port;
			try {
				client = new MqttClient(ip, clientid);
			} catch (MqttException e2) {
				System.err.println("3.5Class: MqttConnection: " + e2.getMessage());
			}
			try {
				options.setSocketFactory(Controller.getInstance().ssl.getSocketFactory(cacertfile.getAbsolutePath(), clientpemfile.getAbsolutePath(), clientkeyfile.getAbsolutePath(), ""));
			} catch (Exception e1) {
				System.err.println("4Class: MqttConnection: " + e1.getMessage());
			}
			try {
				client.connect(options);
			} catch (MqttException e) {
				System.err.println("5Class: MqttConnection: " + e.getMessage());
			}
		}else if(port.matches("8884")) {
			options = new MqttConnectOptions();
			String ip = "ssl://" + server + ":" + port;
			
			try {
				client = new MqttClient(ip, clientid);
			} catch (MqttException e2) {
				System.err.println("3.5Class: MqttConnection: " + e2.getMessage());
			}
			
			try {
				options.setSocketFactory(Controller.getInstance().ssl.getSocketFactory(cacertfile.getAbsolutePath(), clientpemfile.getAbsolutePath(), clientkeyfile.getAbsolutePath(), ""));
			} catch (Exception e1) {
				System.err.println("3.75Class: MqttConnection: " + e1.getMessage());
			}
			
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
			}
			
			try {
				client.connect(options);
			} catch (MqttException e) {
				System.err.println("6Class: MqttConnection: " + e.getMessage());
			}
		}
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
		}
		
		return client.isConnected();
	}
	
}

