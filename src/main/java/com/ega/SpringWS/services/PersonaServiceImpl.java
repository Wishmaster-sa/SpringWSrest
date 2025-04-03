/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS.services;

import com.ega.SpringWS.HttpRequestUtils;
import com.ega.SpringWS.interfaces.PersonaInterface;
import com.ega.SpringWS.models.Answer;
import com.ega.SpringWS.models.LogRecord;
import com.ega.SpringWS.models.Persona;
import com.ega.SpringWS.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;



/**
 * Це реалізація сервіса. 
 * 
 */
 //Ця анотація вказує SPRING що даний клас використовується як сервіс
@Service
//Ця анотація відноситься до компоненту Lombok. Вона допомогає створити усі конструктори класів та змінних, які відносяться до даного класу.
//Тут він потрібен для того, щоб ініціалізувати PersonaRepository repository і, таким чином, включити його в область видимості фреймворка SPRING
//(дивись в документації до фреймворку "впровадження залежностей через конструктор")
@AllArgsConstructor
//@Primary
public class PersonaServiceImpl implements PersonaInterface{

    //задаємо константу, яка належить до інтерфейсу репозіторія та дозволяє працювати з ним.
    private final PersonaRepository repository;
    private final LogRecordService logService;
    //Ця анотація означає, що нам потрібно перевизначити цю процедуру/функцію
    //Спочатку дана функція описується в інтерфейсі класу, тут ми її перевизначаємо та реалізуємо.
    //Докладніше https://www.baeldung.com/java-override
    @Override
    //Функція, яка повертає усіх користувачів з бази даних.
    public Answer showAllPersons() {
        // спочатку создаємо клас Відповідь - який буде містити в собі результати запиту.
          Answer ans;
          //це конструктор класу за допомогою бібліотекі Lombok
          ans = Answer.builder().status(Boolean.FALSE).descr("Unknown error").build();
          
          //реалізацію функції обгортаємо в try/catch
          try{
            var result = repository.findAll();      //визиваемо SELECT з бази даних
            
            //якщо виклик функції не перервався помилкою, то вважаємо його успішним, та записуємо в Статус відповіді
            if(result.size()>0){
                ans.setStatus(Boolean.TRUE);            
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
            }else{
                ans.setStatus(Boolean.FALSE);            
                ans.setDescr("Персон за запитом не знайдено");   //В описі відповіді зазначаємо, що запит успішний.
            }
                   
            JSONArray arr = new JSONArray();
            for(int i = 0;i<result.size();i++){
                JSONObject jsData=new JSONObject();
                Persona persona = result.get(i);
                jsData = persona.toJSON();
                arr.put(jsData);
            }
            ans.setResult(arr.toString());       //Тут результат відповіді.

          }catch (Exception ex){                    //якщо помилка
              ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }

          //записуємо лог
          writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    //Ця анотація означає, що нам потрібно перевизначити цю процедуру/функцію
    //Спочатку дана функція описується в інтерфейсі класу, тут ми її перевизначаємо та реалізуємо.
    //Докладніше https://www.baeldung.com/java-override
    @Override
    //Функція додає нового користувача до бази даних
    public Answer addPersona(Persona persona) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Unknown error").build();
          try{
            Persona result = repository.save(persona);
            //якщо виклик функції не перервався помилкою, то вважаємо його успішним, та записуемо в Статус відповіді
            ans.setStatus(Boolean.TRUE);            
            ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
            ans.setResult(result.toJSON().toString());       //Тут результат відповіді.
          }catch (Exception ex){                    //якщо помилка
              ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    //Ця анотація означає, що нам потрібно перевизначити цю процедуру/функцію
    //Спочатку дана функція описується в інтерфейсі класу, тут ми її перевизначаємо та реалізуємо.
    //Докладніше https://www.baeldung.com/java-override
    @Override
    //Функція знаходить та повертає користувача за його rnokpp
    public Answer find(String rnokpp) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            var result = repository.findByRnokpp(rnokpp);
            //якщо виклик функції не перервався помилкою, то вважаємо його успішним, та записуемо в Статус відповіді
            if(result==null){
                ans.setDescr("Персон з РНОКПП: "+rnokpp+" за запитом не знайдено");   //В описі відповіді зазначаємо, що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(result.toJSON().toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    //Ця анотація означає, що нам потрібно перевизначити цю процедуру/функцію
    //Спочатку дана функція описується в інтерфейсі класу, тут ми її перевизначаємо та реалізуємо.
    //Докладніше https://www.baeldung.com/java-override
    @Override
    //@Transactional
    //Функція оновлює дані користувача
    public Answer updatePersona(Persona persona) {
          Answer ans;
          Persona updatedPersona;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
            try {
                String rnokpp = persona.getRnokpp();
                updatedPersona = repository.findByRnokpp(rnokpp);
                if(persona==null){
                    ans.setStatus(Boolean.FALSE);
                    ans.setDescr("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");   //В описі відповіді зазначаємо, що запит успішний.
                    ans.setResult("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");  //Тут результат відповіді.
                }else{
                
                    BeanUtils.copyProperties(persona, updatedPersona,"id");
                    repository.save(updatedPersona);
                    ans.setStatus(Boolean.TRUE);
                    ans.setDescr("Персону з РНОКПП "+persona.getRnokpp()+" було успішно оновлено");
                    ans.setResult(updatedPersona.toString());
                }
            }catch(Exception ex){
                ans.setDescr("Помилка: "+ex.getMessage());
            }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
}

    //Ця анотація означає, що нам потрібно перевизначити цю процедуру/функцію
    //Спочатку дана функція описується в інтерфейсі класу, тут ми її перевизначаємо та реалізуємо.
    //Докладніше https://www.baeldung.com/java-override
    @Override
    //Видалення запису з БД повинно бути транзакційним. Ця анотація робить це.
    @Transactional
    //Видаляє користувача за його РНОКПП
    public Answer deletePersona(String rnokpp) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
              //спочатку шукаємо персону чи є вона в базі
                Persona persona = repository.findByRnokpp(rnokpp);
                if(persona==null){
                    ans.setStatus(Boolean.FALSE);
                    ans.setDescr("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");   //В описі відповіді зазначаємо, що запит успішний.
                    ans.setResult("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");  //Тут результат відповіді.
                }else{
                    repository.deleteByRnokpp(rnokpp);
                    //якщо виклик функції не перервався помилкою, то вважаємо його успішним, та записуемо в Статус відповіді
                    ans.setStatus(Boolean.TRUE);
                    ans.setDescr("Персону з РНОКПП "+rnokpp+" було видалено!");   //В описі відповіді зазначаємо, що запит успішний.
                    ans.setResult("Персону з РНОКПП "+rnokpp+" було видалено!");  //Тут результат відповіді.
                }
              
            }catch (Exception ex){                    //якщо помилка
                ans.setDescr(ex.getMessage());        //надаємо опис помилки
            }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }
    
    @Override
    //Функція проставляє признак isChecked для користувача
    public Answer checkup(String rnokpp) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            Persona persona = repository.findByRnokpp(rnokpp);
            ans.setDescr("");   //В описі відповіді вказуемо що запит успішний.
            if(persona == null){
                ans.setStatus(Boolean.FALSE);            
                ans.setResult("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");  //Тут результат відповіді.
                ans.setDescr("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");  //Тут результат відповіді.
            }else{
                persona.setIsChecked(Boolean.TRUE);
                repository.save(persona);
                //якщо визов функції не перервався помилкою, то вважаємо його успішним, та записуемо в Статус відповіді
                ans.setResult("Персоні встановлено статус: Перевірена!");       //Тут результат відповіді.
                
            }
          }catch (Exception ex){                    //якщо помилка
              ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    @Override
    //Функція перевіряє поточний статус користувача
    public Answer checkPersona(String rnokpp) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            Persona persona = repository.findByRnokpp(rnokpp);
            ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
            if(persona == null){
                ans.setResult("Персону з РНОКПП "+rnokpp+" не було знайдено в БД!");  //Тут результат відповіді.
            }else if(persona.getIsChecked()==true){
                //якщо виклик функції не перервався помилкою, то вважаємо його успішним, та записуемо в Статус відповіді
                ans.setStatus(Boolean.TRUE);            
                ans.setResult("Персона перевірена!");       //Тут результат відповіді.
                
            }else {
                ans.setStatus(Boolean.TRUE);            
                ans.setDescr("");            
                LocalDateTime dt = LocalDateTime.now();
                LocalDateTime dr = persona.getCheckedRequest();
                if((dr==null)||(dr.getYear()==1)){
                    persona.setCheckedRequest(dt);
                    repository.save(persona);
                    ans.setResult("Запит про перевірку персони обробляється");       //Тут результат відповіді.
                }else{
                    dr = dr.plusMinutes(5);
                    if (dt.isAfter(dr)){                    
                        persona.setIsChecked(Boolean.TRUE);
                        repository.save(persona);
                        ans.setResult("Персона перевірена!");       //Тут результат відповіді.
                    }else{
                        ans.setResult("Перевірка персони ще триває!");       //Тут результат відповіді.
                    }
                    
                }
            }
          }catch (Exception ex){                    //якщо помилка
              ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
          //запис лога
          writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    //запис лога
    private void writeLog(Answer ans){
    
        LogRecord log = new LogRecord();
        
        log.setIp(HttpRequestUtils.getClientIpAddress());
        log.setHttpMethod(HttpRequestUtils.getHttpMethod());
        log.setHeaders(HttpRequestUtils.getHeaders());
        log.setError(!ans.getStatus());
        log.setResource(HttpRequestUtils.getPath());
        log.setResult(ans);
        log.setDescr(ans.getDescr());
        log.setBody(HttpRequestUtils.getBody());
        
        
        logService.addRecord(log);
        
    }

    @Override
    //Пошук всіх користувачів по їх імені
    public Answer findByFirstName(String firstName) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            List<Persona> result = repository.findAllByFirstName(firstName);
            //якщо виклик функції не перервався помилкою, то вважаємо його успішним, та записуемо в Статус відповіді
            if(result.isEmpty()){
                ans.setDescr("Персон з іменем: "+firstName+" не знайдено в БД");   //В описі відповіді вказуемо що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(Persona.listToJSON(result).toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
          //записуем лог
          writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }
    
    @Override
    //пошук всіх користувачів по початку їх прізвища
    public Answer findByLastNameWith(String lastName) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            List<Persona> result = repository.findByLastNameStartingWith(lastName);
            if(result.isEmpty()){
                ans.setDescr("Персон з прізвищем: "+lastName+" не знайдено в БД");   //В описі відповіді зазначаємо, що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(Persona.listToJSON(result).toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
          //записуем лог
          writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }


    @Override
    //пошук всіх користувачів, які в імені або прізвищі мають зазначені символи
    public Answer findAllFirstNameContains(String firstName) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            List<Persona> result = repository.findAllByFirstNameContaining(firstName);
            if(result.isEmpty()){
                ans.setDescr("Персон з іменем "+firstName+" не знайдено в БД");   //В описі відповіді зазначаємо, що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(Persona.listToJSON(result).toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
          //записуем лог
          writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    @Override
    //пошук всіх користувачів, вік яких входить до діапазону
    public Answer findByAgeRange(Integer startAge, Integer endAge) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            LocalDate startDate = LocalDate.now().minusYears(endAge);
            LocalDate endDate = LocalDate.now().minusYears(startAge);
            
            List<Persona> result = repository.findByBirthDateBetween(startDate, endDate);
            if(result.isEmpty()){
                ans.setDescr("Персон з віком з "+startAge+" по "+endAge+ " років не знайдено в БД");   //В описі відповіді вказуемо що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(Persona.listToJSON(result).toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
          //записуем лог
          writeLog(ans);

          return ans;           //повертаємо результат до контролера.
    }

    @Override
    public Answer findByBirthDate(String birthDate) {
          Answer ans;
          LocalDate dt = LocalDate.parse(birthDate);
          
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            List<Persona> result = repository.findByBirthDate(dt);
            if(result.isEmpty()){
                ans.setDescr("Персону з датою народження: "+birthDate+" не знайдено в БД");   //В описі відповіді вказуемо що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(Persona.listToJSON(result).toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
          //записуем лог
          writeLog(ans);
          return ans;           //повертаємо результат до контролера.
   }

    @Override
    public Answer findByPasport(String pasport) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            var result = repository.findByPasport(pasport);
            if(result==null){
                ans.setDescr("Персону з паспортом: "+pasport+" не знайдено в БД");   //В описі відповіді зазначаємо, що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(result.toJSON().toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }

    @Override
    public Answer findByUnzr(String unzr) {
          Answer ans;
          ans = Answer.builder().status(Boolean.FALSE).descr("Якась незрозуміла помилка").build();
          try{
            var result = repository.findByUnzr(unzr);
            if(result==null){
                ans.setDescr("Персону з УНЗР: "+unzr+" не знайдено в БД");   //В описі відповіді зазначаємо, що запит успішний.
            }else{
                ans.setStatus(Boolean.TRUE);
                ans.setDescr("");   //В описі відповіді зазначаємо, що запит успішний.
                ans.setResult(result.toJSON().toString());       //Тут результат відповіді.
            }
          }catch (Exception ex){                    //якщо помилка
            ans.setStatus(Boolean.FALSE);            
            ans.setDescr(ex.getMessage());        //надаємо опис помилки
          }
          
           //записуємо лог
           writeLog(ans);
          return ans;           //повертаємо результат до контролера.
    }


}
