# Налаштування HTTPS

Даний розділ спрямований на налаштування Nginx у режимі reverse proxy для перенаправлення запитів від клієнтів до REST сервісу.

Nginx буде виступати посередником між клієнтом і сервісом. Для захисту трафіку буде використаний самопідписаний SSL сертифікат.

## Загальні передумови
Перед налаштуванням Nginx, необхідно переконатися, що на сервері з вебсервісом встановлені наступні компоненти:

| ПЗ      |  Версія   | Примітка                       |
|:--------|:---------:|--------------------------------|
| Nginx   | **1.18+** |                                |
| OpenSSL |           | Для генерації SSL сертифікатів |                                                                                                                                                                            |


## Встановлення Nginx на Ubuntu

1. Оновити список пакетів:
    ```bash
    sudo apt update
    ```
2. Встановити Nginx:
    ```bash
    sudo apt install nginx
   ```
   ## Налаштування Nginx як Reverse Proxy

1. Перейти до директорії з конфігураціями:
    ```bash
    cd /etc/nginx/sites-available
    ```

2. Створити новий файл конфігурації для REST сервісу. Наприклад, `rest_service`:
    ```bash
    sudo nano /etc/nginx/sites-available/rest_service
    ```
3. Додати наступну конфігурацію для reverse proxy:
    ```nginx
    server {
        listen 80;
        server_name _;
        
        location / {
            proxy_pass http://127.0.0.1:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }

    server {
        listen 443 ssl;
        server_name _;

        ssl_certificate /etc/ssl/certs/nginx-selfsigned.crt;
        ssl_certificate_key /etc/ssl/private/nginx-selfsigned.key;

        location / {
            proxy_pass http://127.0.0.1:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
    ```
4. Створити символічне посилання на конфігурацію в директорії `sites-enabled`:
    ```bash
    sudo ln -s /etc/nginx/sites-available/rest_service /etc/nginx/sites-enabled/
    ```   

5. Переконатись, що символічне посилання створено коректно:
    ```bash
    ls -l /etc/nginx/sites-enabled/
    ```
6. Видалити символічне посилання на файл `default` яке знаходиться у директорії /etc/nginx/sites-enabled

```bash
sudo rm /etc/nginx/sites-enabled/default
``` 

## Генерація самопідписаного SSL сертифіката

Згенерувати SSL сертифікат і приватний ключ можна за допомогою команди:
```bash
    sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/ssl/private/nginx-selfsigned.key -out /etc/ssl/certs/nginx-selfsigned.crt
```

Під час генерації буде запропоновано ввести деякі дані (наприклад, країну, організацію, домен). Для тестових цілей можна ввести будь-які значення.

## Перевірка конфігурації
Щоб переконатися, що конфігурація Nginx правильна, необхідно виконати команду:
 ```bash
sudo nginx -t
 ```
Якщо конфігурація правильна, буде відображено повідомлення `syntax is ok` та `test is successful`.

## Перезапуск Nginx

Після внесення змін необхідно перезавантажити Nginx:
```bash
sudo systemctl restart nginx
 ```
##
Матеріали створено за підтримки проєкту міжнародної технічної допомоги «Підтримка ЄС цифрової трансформації України (DT4UA)».
