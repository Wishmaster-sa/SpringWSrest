from faker import Faker
import requests
from datetime import datetime
import random
import sys

if len(sys.argv)>1:
	num_persons = int(sys.argv[1])
else: 	
	num_persons = 1
	print("Usage:\npython3 fakerdb.py XXX\n XXX - number of records that need to be generated to fill the database")
	sys.exit(1)
#print("num=",num_persons)

fake = Faker("uk_UA")

for x in range(num_persons):
	let1 = random.randint(0, 12)
	let2 = random.randint(0, 12)
	sampletext = "ABCEHIKMHOPTX"

	gender = random.randint(0,100)
	
	#url = "http://10.211.55.5:8080/api/v1/persons/list"
	#print(fake.last_name()+" "+fake.first_name()+" "+fake.middle_name())
	if gender<50:
		lastName = fake.last_name_male()
		firstName = fake.first_name_male()
		middleName = fake.middle_name_male()
		sex = "Чоловік"
	else:
		lastName = fake.last_name_female()
		firstName = fake.first_name_female()
		middleName = fake.middle_name_female()
		sex = "Жінка"

	print(lastName+" "+firstName+" "+middleName)
		
	#print(fake.address())

	#x = requests.get(url)
	#print(x.text)
	birthdate = fake.date_between_dates(date_start="-100y",date_end="-16y");
	persona = {"firstName":firstName,
		"patronymic":middleName,
		"lastName":lastName,
		"gender": sex,
		"birthDate":birthdate.strftime('%Y-%m-%d'),
		"rnokpp":fake.numerify(text = '##########'),
		"pasport":sampletext[let1]+sampletext[let2]+fake.bothify(text = '######'),
		"unzr":birthdate.strftime('%Y%m%d')+fake.numerify(text = '-#####')
	}

	#print(persona)
	url = "http://localhost:8080/api/v1/persons/add"
	try:
		x= requests.post(url, json = persona)
		#print("Answer: "+x.text)
	except ConnectionRefusedError:
		print("Error connect to Webservice! 1")	
		sys.exit(-1)
	except ConnectionError:
		print("Error connect to Webservice! 2")
		sys.exit(-1)
	except Exception:
		print("Error connect to Webservice! 3")
		sys.exit(-1)
			
else:
	print("Наповнення бази завершено!")
	
	
