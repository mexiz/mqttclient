package data;

import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnection {

	/*
	 * 
	 * 
	 * 
	 * 
	 */

	public static MqttClient client;
	private String clientid;

//	private String stndcapath = "zertifikate/HWSBroker-certificate/ca-cert.pem";
//	private String stndclientkeypath ="zertifikate/HWSBroker-certificate/mosq-client-key.pem";
//	private String stndclientpath = "zertifikate/HWSBroker-certificate/mosq-client-pub.pem";

//	private String stndcapath = "zertifikate/mosquitto_certificate/ca.pem";
//	private String stndclientkeypath ="zertifikate/mosquitto_certificate/clientkey.pem";
//	private String stndclientpath = "zertifikate/mosquitto_certificate/client.pem";

	final public String stndcapath = "C:\\Users\\markus\\Documents\\certificates\\ca-cert.pem";
	final public String stndclientkeypath = "C:\\Users\\markus\\Documents\\certificates\\mosq-client-key.pem";
	final public String stndclientpath = "C:\\Users\\markus\\Documents\\certificates\\mosq-client-pub.pem";

	public String capath;
	public String clientkeypath;
	public String clientpath;

	public MqttConnection() {
		clientid = "markus_client";

		capath = stndcapath;
		clientkeypath = stndclientkeypath;
		clientpath = stndclientpath;

	}

	public boolean connectionunverschluesselt(String server, String port, String username, String passwort) {

		String ip = "tcp://" + server + ":" + port;

		if (port.matches("1883")) {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			try {
				MemoryPersistence persistence = new MemoryPersistence();
				client = new MqttClient(ip, clientid, persistence);
				options.setConnectionTimeout(10);
				client.connect(options);
			} catch (MqttException e1) {
				System.err.println("connectionunverschluesselt: MqttConnection: 1883");
				JOptionPane.showMessageDialog(null,
						"Keine Verbindungsaufbau möglich!\nERROR: " + e1.getCause().toString(), "Connection",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} else if (port.matches("1884")) {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			if (!username.matches("") && !passwort.matches("")) {
				options.setUserName(username);
				options.setPassword(passwort.toCharArray());
			} else {
				return false;
			}
			try {
				MemoryPersistence persistence = new MemoryPersistence();
				client = new MqttClient(ip, clientid, persistence);
				client.connect(options);
			} catch (MqttException e1) {
				System.err.println("connectionunverschluesselt: MqttConnection: 1884");
				return false;
			}
		}

		if (client.isConnected()) {
			Controller.getInstance().startmsg();
		}
		return client.isConnected();
	}

	public boolean connectionverschluesselt(String server, String port, String username, String passwort) {

		String ip = "ssl://" + server + ":" + port;

		if (port.matches("8883")) {

			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);

			try {
				SSLSocketFactory socketFactory = Controller.getInstance().ssl.getSocketFactory(capath, clientpath,
						clientkeypath, "");
				options.setSocketFactory(socketFactory);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
			try {
				MemoryPersistence persistence = new MemoryPersistence();
				client = new MqttClient(ip, clientid, persistence);
				client.connect(options);

			} catch (MqttException e2) {
				System.err.println("3.5Class: MqttConnection: " + e2.getMessage());
				return false;
			}
		}

		if (client.isConnected()) {
			Controller.getInstance().startmsg();
		}
		return client.isConnected();
	}

}
