# Інсталяція сервісу вручну

Також існує можливість встановлення вебсервісу вручну, без застосування скрипта.
Для початку роботи потрібно мати чисту систему Ubuntu, всі необхідні пакети та репозиторії будуть підключені в ході виконання встановлення.

**Для того, щоб встановити даний вебсервіс вручну необхідно:**

### 1. Встановити необхідні пакети:

```bash
sudo apt-get update
```

### 2. Встановити Java JDK 21:

```bash
sudo apt install openjdk-21-jdk
```

### 3. Встановити СУБД PostgreSQL:

```bash
sudo apt install postgresql postgresql-contrib
```

### 4. Запустити PostgreSQL та налаштувати автозапуск:

```bash
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### 5. Перевірити, чи працює PostgreSQL:

```bash
sudo systemctl status postgresql
```

**Примітка** Якщо базу даних запущено, можна переходити до виконання наступних кроків.
Інакше – потрібно перевірити чи не було допущено помилку при виконанні одного з попередніх кроків.

### 6. Створити базу даних та користувача. Для цього необхідно:

6.1. Увійти у консоль PostgreSQL:

```bash
sudo -i -u postgres psql
```

6.2. Створити базу даних:
```sql
CREATE DATABASE your_db_name;
```
де: `your_db_name` - бажана назва БД.

6.3. Створити користувача:
```sql
CREATE ROLE your_db_user LOGIN PASSWORD 'your_db_password';
```
де:
- `your_db_user` – логін користувача БД;
- `your_db_password`– пароль для даного користувача.

6.4. Надати користувачеві повні права на базу даних, замінивши your_db_user на логін створеного на попередньому кроці користувача:
```sql
GRANT ALL PRIVILEGES ON DATABASE your_db_name TO your_db_user;
```

Базу даних та користувача успішно створено.

6.6. Вийти з консолі PostgreSQL:

```bash
/q
```

### 7. Завантажити та встановити середовище розробки Apache NetBeans:

```bash
curl https://archive.apache.org/dist/netbeans/netbeans-installers/19/apache-netbeans_19-1_all.deb --output apache-netbeans_19-1_all.deb
sudo dpkg -i apache-netbeans_19-1_all.deb
```

### 8. Завантажити Github клієнт:

```bash
sudo apt install git
```

### 9. Клонувати репозиторій:

```bash
git clone https://github.com/Wishmaster-sa/SpringWSrest.git
```

### 10. Виконати конфігурацію вебсервісу згідно [настанов з конфігурації](/docs/configuration.md):

### 11. Перейти в каталог з вебсервісом:

```bash
cd SpringWSrest
```

### 12. Зкомпілювати вихідний файл:
 
```bash
/usr/lib/apache-netbeans/java/maven/bin/mvn package
```

Запуск вебсервісу здійснюється за допомогою скрипта `springws-service.sh`. Використання скрипта описано в [настановах з адміністрування вебсервісу](/README.md#Адміністрування-сервісу)

##
Матеріали створено за підтримки проєкту міжнародної технічної допомоги «Підтримка ЄС цифрової трансформації України (DT4UA)».
