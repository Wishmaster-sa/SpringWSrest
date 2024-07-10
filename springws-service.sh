#!/bin/sh


function installService() {
	echo 'Встановлюю сервіс...'
	sudo mkdir /opt/SpringWS
	sudo cp ./target/SpringWS-0.0.1-SNAPSHOT.jar /opt/SpringWS
	sudo cp ./webservice.settings /opt/SpringWS
	sudo cp ./springws.service /etc/systemd/system
	sudo sudo systemctl daemon-reload
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
	echo "Припиняю SpringWS сервіс..."
	sudo systemctl stop springws.service
	sudo systemctl status springws.service
}


if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	PS3='Будь-ласка, зробіть вибір: '
	select option in "Встановити сервіс" "Запустити сервіс" "Припинити сервіс" "Видалити сервіс" "Вихід"
	do
	    case $option in
		"Встановити сервіс")
		    installService
		    break
		    ;;
		"Запустити сервіс")
		    startService
		    break
		    ;;
		"Припинити сервіс")
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

