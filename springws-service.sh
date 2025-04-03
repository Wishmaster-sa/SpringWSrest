#!/bin/sh


function installService() {
	echo 'Додаю сервіс до автозапуску...'
 	#modify service daemon for current user
	autostartFile="./springws.service"
 	currentuser=$(stat -c "%G" .)
	sed -i "s/User=sa/User=$currentuser/g" $autostartFile
	sudo mkdir /opt/SpringWS
	sudo mkdir /opt/SpringWS/config
	sudo cp ./target/SpringWS-0.0.1-SNAPSHOT.jar /opt/SpringWS
        sudo cp -R config/* /opt/SpringWS/config 
	#sudo cp ./webservice.settings /opt/SpringWS
	sudo cp ./springws.service /etc/systemd/system
	sudo systemctl daemon-reload
	sudo systemctl enable springws.service
	sudo systemctl start springws.service
	sudo systemctl status springws.service
}

function removeService() {
	echo "Видаляю SpringWS сервіс..."
	sudo systemctl stop springws.service
	sudo systemctl disable springws.service
	sudo rm /etc/systemd/system/springws.service
	sudo sudo systemctl daemon-reload
	
	sudo rm -R /opt/SpringWS
}

function startService() {
	echo "Запускаю SpringWS сервіс..."
	sudo systemctl start springws.service
	sudo systemctl status springws.service
}

function stopService() {
	echo "Зупиняю SpringWS сервіс..."
	sudo systemctl stop springws.service
	sudo systemctl status springws.service
}


if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	PS3='Будь-ласка, зробіть вибір: '
	select option in "Додати сервіс до автозапуску" "Запустити сервіс" "Зупинити сервіс" "Видалити сервіс" "Вихід"
	do
	    case $option in
		"Додати сервіс до автозапуску")
		    installService
		    break
		    ;;
		"Запустити сервіс")
		    startService
		    break
		    ;;
		"Зупинити сервіс")
		    stopService
		    break
		    ;;
		"Видалити сервіс")
		    removeService
		    break
		    ;;
		"Вихід")
		    break
		    ;;
		*)
		    echo 'Invalid option.'
		    ;;
	    esac
	done	
	exit 1
fi


case $1 in
    'install')
        installService
        ;;
    'start')
        startService
        ;;
    'stop')
        stopService
        ;;
    'remove')
        removeService
        ;;
    *)
        echo 'Usage: bash springws-service.sh install|start|stop|remove'
        ;;
esac

