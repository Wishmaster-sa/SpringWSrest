#!/bin/bash

if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	echo "Сінтакс: sudo bash add_cert_to_keystore.sh <alias_cert> <cert_file>"
	echo "Де:"
	echo "<alias_cert> - будь яка назва сертифіката в сховище (повинна бути унікальною)"
	echo "<cert_file> - шлях та ім'я файла сертифіката який додається в сховище"
	echo ""
	echo "Приклад:"
	echo "sudo bash add_cert_to_keystore.sh myalias mycert.pem"
	exit 1
fi

sudo keytool -trustcacerts -keystore "/usr/lib/jvm/java-21-openjdk-arm64/lib/security/cacerts" -storepass changeit -importcert -alias $1 -file $2

