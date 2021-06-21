package certificate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

public class SslUtil {

	/*
	 * 
	 * 
	 * 
	 */

	public SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile,final  String keyFile, 
			final String password)
			throws Exception {
			
		System.out.println(caCrtFile);
		System.out.println(crtFile);
		System.out.println(keyFile);
		
		Security.addProvider(new BouncyCastleProvider());
		
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

// "ca" Zertifikate wird in x509 caCert geladen
		 FileInputStream fis = new FileInputStream(caCrtFile);
	     BufferedInputStream bis = new BufferedInputStream(fis);
	     cf = CertificateFactory.getInstance("X.509");
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(caCrtFile));
		
		X509Certificate caCert = null;
		while (bis.available() > 0) {
			caCert = (X509Certificate) cf.generateCertificate(bis);
		}

// client Zertifikate wird in x509 caCert geladen
		bis = new BufferedInputStream(new FileInputStream(crtFile));
		
		X509Certificate cert = null;
		while (bis.available() > 0) {
			cert = (X509Certificate) cf.generateCertificate(bis);
		}

// clientkey wird geladen
		
		PEMParser pemParser = new PEMParser(new FileReader(keyFile));
		Object object = pemParser.readObject();
		PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		
		KeyPair key;
		if (object instanceof PEMEncryptedKeyPair) {
			System.out.println("Encrypted key");
			key = converter.getKeyPair(((PEMEncryptedKeyPair) object)
					.decryptKeyPair(decProv));
		} else {
			System.out.println("Unencrypted key - no password needed");
			key = converter.getKeyPair((PEMKeyPair) object);
		}
		pemParser.close();

// CA certificate wird benötigt um den Server zu Authentifizierung
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(caKs);

// client key und das clientzertifikat werden benötigt um den Client zu verifizieren
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
				new java.security.cert.Certificate[] { cert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());

// SSL Socket wird hier erzeugt dafür wird der Keymanager und Trustmanager benötigt
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();

	}

}
