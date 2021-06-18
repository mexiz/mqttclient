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
	
	public String capath = "zertifikate/mosquitto_certificate/ca.pem";
	public String clientkeypath ="zertifikate/mosquitto_certificate/clientkey.pem";
	public String clientpath = "zertifikate/mosquitto_certificate/client.pem";
	
	
	public MqttConnection() {
//		clientid = UUID.randomUUID().toString();
		clientid = "markus_client";
		
		try {
			cacertfile = new File(capath);
			clientkeyfile = new File(clientkeypath);
			clientpemfile = new File(clientpath);
		} catch (Exception e) {
			System.out.println("not found");
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
				options.setConnectionTimeout(10);
				client.connect(options);
			} catch (MqttException e1) {
				System.err.println("1Class: MqttConnection: " + e1.getMessage());
				return false;
			}
		}else if(port.matches("1884")) {
			String ip = "tcp://" + server + ":" + port;
			options = new MqttConnectOptions();
			options.setCleanSession(true);
			try {
				client = new MqttClient(ip, clientid);
			} catch (MqttException e1) {
				System.err.println("2.5Class: MqttConnection: " + e1.getMessage());
				return false;
			}
			System.out.println(port);
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
				try {
					client.connect(options);
				} catch (MqttException e) {
					System.err.println("3Class: MqttConnection: " + e.getMessage());
					return false;
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
				return false;
			}
			try {
				options.setSocketFactory(Controller.getInstance().ssl.getSocketFactory(cacertfile.getAbsolutePath(), clientpemfile.getAbsolutePath(), clientkeyfile.getAbsolutePath(), ""));
			} catch (Exception e1) {
				System.err.println("4Class: MqttConnection: " + e1.getMessage());
				return false;
			}
			try {
				client.connect(options);
			} catch (MqttException e) {
				System.err.println("5Class: MqttConnection: " + e.getMessage());
				return false;
			}
		}else if(port.matches("8884")) {
			options = new MqttConnectOptions();
			String ip = "ssl://" + server + ":" + port;
			
			try {
				client = new MqttClient(ip, clientid);
			} catch (MqttException e2) {
				System.err.println("3.5Class: MqttConnection: " + e2.getMessage());
				return false;
			}
			
			try {
				options.setSocketFactory(Controller.getInstance().ssl.getSocketFactory(cacertfile.getAbsolutePath(), clientpemfile.getAbsolutePath(), clientkeyfile.getAbsolutePath(), ""));
			} catch (Exception e1) {
				System.err.println("3.75Class: MqttConnection: " + e1.getMessage());
				return false;
			}
			
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
			}
			
			try {
				client.connect(options);
			} catch (MqttException e) {
				System.err.println("6Class: MqttConnection: " + e.getMessage());
				return false;
			}
		}
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
		}
		
		return client.isConnected();
	}
	
}

