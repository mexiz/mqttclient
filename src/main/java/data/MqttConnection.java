package data;

import java.util.UUID;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConnection extends Thread {

	/*
	 * 
	 * 
	 * 
	 */

	static MqttClient client;
	String clientid;

	String cacert = "C:\\Users\\markus\\Documents\\GitHub\\zertifikate\\ca-cert.pem";
	String clientkey = "C:\\Users\\markus\\Documents\\GitHub\\zertifikate\\mosq-client-key.pem";
	String clientpub = "C:\\Users\\markus\\Documents\\GitHub\\zertifikate\\mosq-client-pub.pem";
	
	public MqttConnection() {
		clientid = UUID.randomUUID().toString();
	}

	public boolean connectionunverschluesselt(String server, String port, String username, String passwort)
			throws MqttException {
		
		String ip = "tcp://" + server + ":" + port;
		client = new MqttClient(ip, clientid);
		MqttConnectOptions options = new MqttConnectOptions();
		if (!username.matches("")) {
			options.setUserName(username);
			System.out.println("username" + ": " + username);
		}
		if (!passwort.matches("")) {
			options.setPassword(passwort.toCharArray());
			System.out.println("password" + ": " + passwort);
		}

//		options.setConnectionTimeout(60);
//		options.setKeepAliveInterval(60);
//		options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
		
		try {
			client.connect(options);
		} catch (MqttException e) {
			System.err.println("Couldn´t connect client");
		}
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
			Controller.getInstance().gui.txt.append("Sub-Client connected \n");
		}
		
		return client.isConnected();
	}
	public boolean connectionverschluesselt(String server, String port, String username, String passwort)
			throws MqttException {
		
		String ip = "tcp://" + server + ":" + port;
		client = new MqttClient(ip, clientid);
		MqttConnectOptions options = new MqttConnectOptions();
		if (!username.matches("")) {
			options.setUserName(username);
			System.out.println("username" + ": " + username);
		}
		if (!passwort.matches("")) {
			options.setPassword(passwort.toCharArray());
			System.out.println("password" + ": " + passwort);
		}

//		options.setConnectionTimeout(60);
//		options.setKeepAliveInterval(60);
//		options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
		
		try {
			SSLSocketFactory socketFactory = Controller.getInstance().ssl.getSocketFactory(cacert,
					clientpub, clientkey, "");
			options.setSocketFactory(socketFactory);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			client.connect(options);
		} catch (MqttException e) {
			System.err.println("Couldn´t connect client");
		}

//		MqttToken token = (MqttToken) client.connectWithResult(options);
		if (client.isConnected()) {
			Controller.getInstance().startmsg();
			System.out.println("Sub-Client connected");
		}
		return client.isConnected();
	}
}
