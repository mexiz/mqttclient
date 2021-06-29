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

	final public String stndcapath = "zertifikate/HWSBroker-certificate/ca-cert.pem";
	final public String stndclientkeypath ="zertifikate/HWSBroker-certificate/mosq-client-key.pem";
	final public String stndclientpath = "zertifikate/HWSBroker-certificate/mosq-client-pub.pem";

//	final public String stndcapath = "zertifikate/mosquitto_certificate/ca.pem";
//	final public String stndclientkeypath ="zertifikate/mosquitto_certificate/clientkey.pem";
//	final public String stndclientpath = "zertifikate/mosquitto_certificate/client.pem";

//	final public String stndcapath = "C:\\Users\\markus\\Documents\\certificates\\ca-cert.pem";
//	final public String stndclientkeypath = "C:\\Users\\markus\\Documents\\certificates\\mosq-client-key.pem";
//	final public String stndclientpath = "C:\\Users\\markus\\Documents\\certificates\\mosq-client-pub.pem";

	public String capath;
	public String clientkeypath;
	public String clientpath;
	
	SSLSocketFactory socketFactory;

	public MqttConnection() {

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
				client = new MqttClient(ip, MqttClient.generateClientId(), persistence);
				options.setConnectionTimeout(10);
				client.connect(options);
			} catch (MqttException e1) {
				JOptionPane.showMessageDialog(null,
						"No Connection!\nERROR: " + e1.getCause().toString(), "Connection",
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
				client = new MqttClient(ip, MqttClient.generateClientId(), persistence);
				client.connect(options);
			} catch (MqttException e1) {
				JOptionPane.showMessageDialog(null,
						"No Connection!\nERROR: " + e1.getCause().toString(), "Connection",
						JOptionPane.ERROR_MESSAGE);
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
		MqttConnectOptions options = new MqttConnectOptions();
		options.setHttpsHostnameVerificationEnabled(false);
		if (port.matches("8883")) {
			
			options.setCleanSession(true);

			try {
				
				socketFactory =
						Controller.getInstance().ssl.getSocketFactory(capath, clientpath,
						clientkeypath, "");
				
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
			
			try {
				MemoryPersistence persistence = new MemoryPersistence();
				client = new MqttClient(ip, MqttClient.generateClientId(), persistence);
				options.setSocketFactory(socketFactory);
				client.connect(options);
			} catch (MqttException e2) {
//				System.err.println("4-Class: MqttConnection: " + e2.getMessage());
				JOptionPane.showMessageDialog(null,
						"No Connection!\nERROR: " + e2.getCause().toString(), "Connection",
						JOptionPane.ERROR_MESSAGE);
//				e2.printStackTrace();
				return false;
			}
		}

		if (client.isConnected()) {
			Controller.getInstance().startmsg();
		}else {
			System.out.println("not connected");
		}
		return client.isConnected();
	}

}
