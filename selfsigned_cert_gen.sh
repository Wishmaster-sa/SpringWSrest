#!/bin/bash

rootCAkey=rootCA.key 
rootCApem=rootCA.pem
serverkey=server.key
serverpem=server.pem
servercsr=server.csr
clientkey=client.key
clientcsr=client.csr
clientpem=client.pem
keystore=keystore.p12
truststore=truststore.p12

service_ip=127.0.0.1
service_Country=UA
service_State=Ukraine
service_City=Kyiv
service_Organization=eGA

echo "******************************************************************************
*                  Создаємо власний Certificate Authority
******************************************************************************"

if [ -f ./$rootCAkey ]; then
	echo "власний Certificate Authority вже існує"
else 
	openssl genrsa -des3 -out $rootCAkey 2048
fi

echo "******************************************************************************
*                  Створюємо відкритий сертифікат до Certificate Authority
******************************************************************************"
if [ -f ./$rootCApem ]; then
	echo "відкритий сертифікат до Certificate Authority вже існує"
else 
	openssl req -x509 -new -nodes -key $rootCAkey -sha256 -days 3650 -out $rootCApem
fi

echo "******************************************************************************
* Вітаємо! Тепер ви є центром сертифікації та можете підписувати сертифікати для 
* інших  суб'єктів. Суб'єкт, що запитує сертифікат у центру сертифікації, повинен
* надати CA CSR, що містить інформацію про запит
******************************************************************************"

echo "******************************************************************************
*                  Створюємо закритий ключ до нашого сервіса
******************************************************************************"
if [ -f ./$serverkey ]; then
	echo "закритий ключ до нашого сервіса вже існує"
else 
	openssl genrsa -des3 -out $serverkey 2048
fi

echo "******************************************************************************
*                  Створюємо запит на сертифікат к СА для нашого сервіса
******************************************************************************"
if [ -f ./$servercsr ]; then
	echo "запит на сертифікат к СА для нашого сервіса вже існує"
else 
	read -p "Введіть IP сервіса: " service_ip
	read -p "Введіть Код країни сервіса ($service_Country): " service_Country
	read -p "Введіть Країну сервіса ($service_State): " service_State
	read -p "Введіть Місто сервіса ($service_City): " service_City
	read -p "Введіть Організацію властника сервіса ($service_Organization): " service_Organization

	echo "[req]
default_bits = 2048
default_md = sha256
prompt = no
distinguished_name = req_distinguished_name
x509_extensions = v3_ca
req_extensions = v3_req
 
[req_distinguished_name]
C = $service_Country
ST = $service_State
L = $service_City
O = $service_Organization
CN = $service_ip
 
[v3_ca]
subjectKeyIdentifier = hash
authorityKeyIdentifier = keyid:always,issuer:always
basicConstraints = critical, CA:true, pathlen:0
keyUsage = critical, digitalSignature, cRLSign, keyCertSign
 
[v3_req]
subjectKeyIdentifier = hash
basicConstraints = critical, CA:false
nsCertType = server
keyUsage = digitalSignature, nonRepudiation, keyEncipherment
extendedKeyUsage = serverAuth
subjectAltName = IP:$service_ip" | tee server.conf >/dev/null
#	openssl req -new -sha256 -addext "subjectAltName = IP:10.211.55.5" -key $serverkey -out $servercsr
	openssl req -new -sha256 -config server.conf -key $serverkey -out $servercsr
fi


echo "******************************************************************************
*                  Створюємо відкритий сертифікат до нашого сервіса
******************************************************************************"
if [ -f ./$serverpem ]; then
	echo "відкритий сертифікат до нашого сервіса вже існує"
else 
	echo "[ v3_ext ]
subjectAltName = IP:$service_ip" | tee req.conf >/dev/null
	openssl x509 -req -in $servercsr -CA $rootCApem -CAkey $rootCAkey -CAcreateserial -out $serverpem -days 3650 -sha256 -extensions v3_ext -extfile req.conf
fi

echo "******************************************************************************
*                  Створюємо закритий ключ до клієнта
******************************************************************************"
if [ -f ./$clientkey ]; then
	echo "закритий ключ до клієнта вже існує"
else 
	openssl genrsa -des3 -out $clientkey 2048
fi

echo "******************************************************************************
*                  Створюємо запит на сертифікат к СА для клієнта
******************************************************************************"
if [ -f ./$clientcsr ]; then
	echo "запит на сертифікат к СА для клієнта вже існує"
else 
	openssl req -new -sha256 -key $clientkey -out $clientcsr
fi


echo "******************************************************************************
*                  Створюємо відкритий сертифікат до нашого клієнта
******************************************************************************"
if [ -f ./$clientpem ]; then
	echo "відкритий сертифікат до нашого клієнта вже існує"
else 
	openssl x509 -req -in $clientcsr -CA $rootCApem -CAkey $rootCAkey -CAcreateserial -out $clientpem -days 3650 -sha256
fi

echo "******************************************************************************
*                  Створюємо сховище ключей сервіса
******************************************************************************"
if [ -f ./$keystore ]; then
	echo "сховище ключей сервіса вже існує"
else 
	openssl pkcs12 -export -in $serverpem -out $keystore -name server -nodes -inkey $serverkey
fi


echo "******************************************************************************
*                  Створюємо сховище довіри
******************************************************************************"
if [ -f ./$truststore ]; then
	echo "сховище довіри вже існує"
else 
	keytool -import -file $rootCApem -alias rootCA -keystore $truststore
fi



