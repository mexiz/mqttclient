# mqttclient

CLIENT KEY WIRD GENERIERT 

openssl genrsa -out client.key 2048
openssl req -out client.csr -key client.key -new

HIER DIE CLIENT CSR EINFÜGEN
https://test.mosquitto.org/ssl/

DIE CSR IN CRT UMSCHREIBEN
openssl x509 -req -in C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.csr -signkey C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.key -out C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\server.crt

CA VOM TEST.MOSQUITTO.ORG SERVER
https://test.mosquitto.org/ssl/mosquitto.org.crt

mosquitto_pub -p 8883 --cafile C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\ca\ca.crt --cert C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.crt --key C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.key -h test.mosquitto.org -m test123 -t Temperature/TEST
mosquitto_sub -p 8883 --cafile C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\ca\ca.crt --cert C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.crt --key C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.key -h test.mosquitto.org -t /test


CRT to PEM
openssl x509 -in C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\ca\ca.crt -out C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\ca.pem -outform PEM
openssl x509 -in C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.crt -out C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client.pem -outform PEM

KEY to PEM
openssl rsa -in C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\client\client.key -text > C:\Users\markus\Documents\GitHub\zertifikate_mosquitto\clientkey.pem
