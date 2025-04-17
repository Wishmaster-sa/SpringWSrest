### Ви можете скористуватися автоматичним скріптом 
Виконайте команду
```bash
sudo bash docker.sh 
```
Або налаштувати все вручну за допомогою наступної інструкції

### Встановлення Docker

Спочатку оновіть наявний список пакетів:
```bash
sudo apt update 
```
Далі встановіть кілька необхідних пакетів, які дозволяють aptвикористовувати пакети через HTTPS:
```bash
sudo apt install apt-transport-https ca-certificates curl software-properties-common
```
Потім додайте ключ GPG для офіційного репозиторію Docker у вашу систему:
```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```
Додайте репозиторій Docker до джерел APT:
```bash
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```
Знову оновіть існуючий список пакетів, щоб додавання було розпізнано:
```bash
sudo apt update 
```
Нарешті, встановіть Docker:
```bash
sudo apt install docker-ce
```
Тепер має бути встановлено Docker, запущено демон і ввімкнено процес для запуску під час завантаження. Перевірте, чи працює:
```bash
sudo systemctl status docker
```
### Створення контейнера springrestservice
```bash
sudo docker build -t springrestservice:latest .
```
### Налаштування контейнера springrestservice
Відредагуйте файл з параметрами ./service.env та встановить свої налаштування

```bash
sudo nano ./service.env
```

### Запуск контейнера springrestservice
```bash
sudo docker run -it --rm -p 8080:8080 --env-file ./service.env springrestservice:latest
```

Программа буде доступна за адресою: http://localhost:8080.
