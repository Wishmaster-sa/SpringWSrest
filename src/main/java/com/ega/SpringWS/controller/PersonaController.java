/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS.controller;

import com.ega.SpringWS.HttpRequestUtils;
import com.ega.SpringWS.models.Persona;
import com.ega.SpringWS.interfaces.PersonaInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ega.SpringWS.models.Answer;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PatchMapping;

/**
 *
 * @author sa
 */
//Ця анотація говорить SPRING, що це контроллер, його потрібно включити до контексту SPRING (щоб він мав можливість керувати ним)
//Також ця анотація автоматично перетворює будь-яку відповідь до JSON формату.
@RestController

//Ця анотація задає шлях до кінцевої точки виклику (ендпоінту) сервісу.
@RequestMapping("/api/v1/persons")

//Ця анотація відноситься до компоненту Lombok. Вона допомогає створити усі конструктори класів та змінних, які відносяться до даного класу.
//Тут він потрібен для того, щоб ініціалізувати PersonaService service і, таким чином, включити його в область видимості фреймворка SPRING
//(дивись в документації до фреймворку "впровадження залежностей через конструктор")
@AllArgsConstructor
public class PersonaController {
    //Визначаємо, що в нашому контролері буде використовуватись сервіс service з інтерфейсом PersonaInterface. 
    //В цей сервіс, який вже реалізує нашу бізнес-логіку, ми будемо передавати вхідні дані запиту для обробки.
    private final PersonaInterface service;
    //private final LogRecordService logService;

//Ця анотація задає шлях до кінцевої точки виклику (ендпоінту) сервісу. Тут треба зайти на http://localhost:8080/api/v1/persons/show
//щоб отримати весь перелік інформаційних об'єктів - користувачів. Також тут зазначається, що метод виклику ендпоінту повинен бути HTTP GET
    @GetMapping("list")
    public Answer showAllPersons(){
        Answer ans;
        
        System.out.println("List persons");
        ans = service.showAllPersons();
        
        return ans;
    }
    
    
    @GetMapping("test")
    public Answer test(){
        
        //повертаємо користувачу результат тесту, який показує, що сервіс отримав запит і опрацював його.
        Answer ans = Answer.builder().status(true).descr("request successful").result("Поточний час: "+LocalDateTime.now()).build();
        return ans;
    }
    
    
//Анотація GetMapping говорить, що описується обробка запиту типу HTTP GET
//Тут ми говоримо, що шлях  http://localhost:8080/api/v1/persons/find/[РНОКПП] буде використовуватись як сервіс для пошуку користувача із заданим РНОКПП.
//Де [РНОКПП] - це параметр який потрібно замінити на існуючий рнокпп.

    @GetMapping("find/{rnokpp}")
    //Анотація @PathVariable говорить SPRINGу що параметр rnokpp потрібно прийняти через адресну строку
    public Answer find(@PathVariable String rnokpp){
        Answer ans;
        System.out.println("Searching for person by rnokpp: "+rnokpp);
        System.out.println("ip: "+HttpRequestUtils.getClientIpAddress()
                +"; Method: "+HttpRequestUtils.getHttpMethod()
                +"; Path: "+HttpRequestUtils.getPath()
        );
        
        
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.find(rnokpp);
        return ans;
    }

    @GetMapping("find/firstname/{firstName}")
    //Анотація @PathVariable говорить SPRINGу що параметр rnokpp потрібно прийняти через адресну строку
    public Answer findByFirstName(@PathVariable String firstName){
        Answer ans;
        
        
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.findByFirstName(firstName);
        return ans;
    }
    
    @GetMapping("find/lastname/{lastName}")
    //Анотація @PathVariable говорить SPRINGу що параметр rnokpp потрібно прийняти через адресну строку
    public Answer findLastName(@PathVariable String lastName){
        Answer ans;
        
        
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.findByLastNameWith(lastName);
        return ans;
    }

    @GetMapping("find/range/age/{startAge}/{endAge}")
    //Анотація @PathVariable говорить SPRINGу що параметри startAge та endAge потрібно прийняти через адресну строку
    public Answer findLastName(@PathVariable Integer startAge, @PathVariable Integer endAge){
        Answer ans;
        
        
        //формуємо та повертаємо користувачу результат, який був опрацьований в сервісі.
        ans = service.findByAgeRange(startAge,endAge);
        return ans;
    }
    
//Анотація PostMapping говорить, що описується обробка запиту типу HTTP POST
//Тут ми говоримо, що шлях http://localhost:8080/api/v1/persons буде використовуватись для додавання користувача до бази даних.
//Параметри запиту повинні додаватись у тілі запиту в JSON форматі.
    @PostMapping("add")
    public Answer addPersona(@RequestBody Persona persona){
        Answer ans = Persona.isValid(persona);
        
        if(!ans.getStatus())return ans;
        
        System.out.println("==============================================================================================");
        System.out.println("Додана персона: "+persona.toString());
    
        //формуємо та повертаємо користувачу результат, який був опрацьований в сервісі.
        ans =  service.addPersona(persona);
        
        return ans;
    }

    
//Анотація PutMapping говорить, що описується обробка запиту типу HTTP PUT
//Тут ми говоримо, що шлях http://localhost:8080/api/v1/persons буде використовуватись для оновлення користувача в базі даних.
//зіставлення користувача буде виконуватись за ID
//Параметри запиту повинні додаватись у тілі запиту в JSON форматі.
    @PatchMapping("update")
   //Анотація @RequestBody говорить SPRINGу що тіло запиту треба перетворити з JSON в обʼєкт класу Persona
    public Answer updatePersona(@RequestBody Persona persona){
        Answer ans;
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.updatePersona(persona);

        return ans;
    }

//Анотація DeleteMapping говорить, що описується обробка запиту типу HTTP DELETE
//Тут ми говоримо, що шлях http://localhost:8080/api/v1/persons/[РНОКПП] буде використовуватись для видалення користувача з бази даних.
//зіставлення користувача буде виконуватись за РНОКПП
    @DeleteMapping("delete/{rnokpp}")
    //Анотація @PathVariable говорить SPRINGу що це параметр, який потрібно прийняти через адресну строку
    public Answer deletePersona(@PathVariable String rnokpp){
        Answer ans;
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.deletePersona(rnokpp);

        return ans;
    }

    @GetMapping("checkup/{rnokpp}")
    //Анотація @PathVariable говорить SPRINGу що це параметр, який потрібно прийняти через адресну строку
    public Answer checkupPersona(@PathVariable String rnokpp){
        Answer ans;
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans =  service.checkup(rnokpp);

        return ans;
    }

    @GetMapping("check/{rnokpp}")
    //Анотація @PathVariable говорить SPRINGу що це параметр, який потрібно прийняти через адресну строку
    public Answer checkPersona(@PathVariable String rnokpp){
        Answer ans;
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.checkPersona(rnokpp);

        return ans;
    }

    @GetMapping("find/unzr/{unzr}")
    //Анотація @PathVariable говорить SPRINGу що це параметр, який потрібно прийняти через адресну строку
    public Answer findByUnzr(@PathVariable String unzr){
        Answer ans;
        System.out.println("find by UNZR: "+unzr);
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.findByUnzr(unzr);

        return ans;
    }

    @GetMapping("find/pasport/{pasport}")
    //Анотація @PathVariable говорить SPRINGу що це параметр, який потрібно прийняти через адресну строку
    public Answer findByPasport(@PathVariable String pasport){
        Answer ans;

        System.out.println("find by Pasport: "+pasport);
        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.findByPasport(pasport);

        return ans;
    }

    @GetMapping("find/birthDate/{birthDate}")
    //Анотація @PathVariable говорить SPRINGу що це параметр, який потрібно прийняти через адресну строку
    public Answer findByBirthDate(@PathVariable String birthDate){
        Answer ans;
        System.out.println("find by birth date: "+birthDate);

        //формуємо та повертаємо результат, який був опрацьований в сервісі.
        ans = service.findByBirthDate(birthDate);

        return ans;
    }
    
}

