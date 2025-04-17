#!/bin/sh


function buildImage() {
	echo "Створюю Docker образ springrestservice ..."
	sudo docker build -t springrestservice:latest .
}

function setParameters() {
	echo "Налаштовуємо параметри..."
	nano ./service.env
}

function startImage() {
	echo "Запускаю Docker образ springrestservice ..."
	sudo docker run -it --rm -p 8080:8080 --env-file ./service.env springrestservice:latest
	
}

function removeImage() {
	echo "Видаляю Docker образ springrestservice ..."
	sudo docker rmi springrestservice:latest
}


if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	PS3='Будь-ласка, зробіть вибір: '
	select option in "Створити Docker образ" "Налаштувати Docker образ" "Запустити Docker образ" "Видалити Docker образ" "Вихід"
	do
	    case $option in
		"Створити Docker образ")
		    buildImage
		    break
		    ;;
		"Запустити Docker образ")
		    startImage
		    break
		    ;;
		"Налаштувати Docker образ")
		    setParameters
		    break
		    ;;
		"Видалити Docker образ")
		    removeImage
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
    'build')
        buildImage
        ;;
    'start')
        startImage
        ;;
    'set')
        setParameters
        ;;
    'remove')
        removeImage
        ;;
    *)
        echo 'Usage: bash docker.sh build|start|set|remove'
        ;;
esac

