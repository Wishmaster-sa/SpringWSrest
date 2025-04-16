#!/bin/sh


function buildImage() {
	echo "Створюю Docker образ springrestservice ..."
	sudo docker build -t springrestservice:latest .
}

function setParameters() {
	echo "Налаштовуємо параметри..."
	nano ./docker.env
}

function startImage() {
	echo "Запускаю Docker образ springrestservice ..."
	sudo docker run -it --rm -p 8080:8080 --env-file ./service.env springrestservice:latest
	
}


if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	PS3='Будь-ласка, зробіть вибір: '
	select option in "Створити Docker образ" "Налаштувати Docker образ" "Запустити Docker образ" "Вихід"
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
    *)
        echo 'Usage: bash docker.sh build|start|set'
        ;;
esac

