/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Click https://projectlombok.org/features/ to view all features of lombok
import lombok.Data;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Цей клас задає модель інформаційного об'єкта - користувача, та його поля (ім'я, прізвище, дату народження, вік тощо)
 */
//Ця анотація надає можливість автоматично формувати конструктор класу, та реалізує встановлення та отримання всіх полів класу.
@Data
// The @Builder annotation produces complex builder APIs for your classes.
// Builder lets you automatically produce the code required to have your class be instantiable with code
//@Builder

//Анотація @Entity говорить SPRINGу що цю сутність потрібно включити до бази даних.
//Так як у нас анотація відноситься до всього класу - це означає, що на базі класу буде створена таблиця в БД
//Докладніше https://www.baeldung.com/jpa-entities
@Entity

//Задає назву таблиці. Якщо ця анотація не задана, то назва таблиці буде дублювати назву класу.
@Table(name="Persona")
public class Persona {
    //Ця анотація зазначає, що наступне поле класу буде виконувати роль ідентіфікатора в БД    
    @Id
    //Говорить, що ідентіфікатор наступного поля класу буде генеруватись автоматично
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;
    private String pasport;
    private String gender;
    //Говорить, що наступне поле потрібно зробити унікальним в БД
    @Column(unique = true)
    private String unzr;                    //УНЗР
    private Boolean isChecked;              //ознака опрацювання запису оператором вручну (для прикладу асинхронних запитів-відповідей)
    private LocalDateTime CheckedRequest;   //коли був запит на перевірку користувача
    
    //Говорить, що наступне поле потрібно зробити унікальним в БД
    @Column(unique = true)
    @NonNull
    private String rnokpp;
    //Говорить, що значення цього поля буде розраховуватись автоматично
    @Transient
    private int age;
    
    public Persona(){
        this.firstName = "";
        this.lastName = "";
        this.patronymic = "";
        this.birthDate = LocalDate.of(1, 1, 1);
        this.pasport = "";
        this.unzr = "01010001-00001";
        this.isChecked = false;
        this.CheckedRequest = LocalDateTime.of(1, 1, 1,0,0,0);
    }
    
    // перевизначення функції отримання віку. Отримуємо вік як різницю кількості років між
    // поточним роком та роком народження.
    public int getAge(){
        if((birthDate == null)||(birthDate==LocalDate.of(1, 1, 1))){
            return 0;
        }else return LocalDate.now().getYear() - birthDate.getYear();
    }

    //метод, який перетворює об'єкт класу користувача на JSON об'єкт
    public JSONObject toJSON(){
        JSONObject jsData=new JSONObject();
        //Persona persona = result.get(i);
        jsData.put("id",getId());
        jsData.put("firstName",getFirstName());
        jsData.put("lastName",getLastName());
        jsData.put("patronymic",getPatronymic());
        jsData.put("gender",getGender());
        jsData.put("unzr",getUnzr());
        jsData.put("rnokpp",getRnokpp());
        jsData.put("pasport",getPasport());
        jsData.put("age",getAge());
        jsData.put("birthDate",getBirthDate());
        jsData.put("isChecked",getIsChecked());
        jsData.put("CheckedRequest",getCheckedRequest());
        
        return jsData;
    }  

    @Override
    public String toString(){
        JSONObject jsData=new JSONObject();
        //Persona persona = result.get(i);
        jsData.put("id",getId());
        jsData.put("firstName",getFirstName());
        jsData.put("lastName",getLastName());
        jsData.put("patronymic",getPatronymic());
        jsData.put("gender",getGender());
        jsData.put("unzr",getUnzr());
        jsData.put("rnokpp",getRnokpp());
        jsData.put("pasport",getPasport());
        jsData.put("age",getAge());
        jsData.put("birthDate",getBirthDate());
        jsData.put("isChecked",getIsChecked());
        jsData.put("CheckedRequest",getCheckedRequest());
        
        return jsData.toString();
    }  
    
    //метод, який перетворює список користувачів на масив JSON
    public static JSONArray listToJSON(List <Persona> personsList){
        JSONArray persons = new JSONArray(personsList);
        
        return persons;
    } 
    
    //Метод, який перевіряє об'єкт користувача на правильність заповнення усіх полів
    public static Answer isValid(Persona persona){
        //создаємо об'єкт класу відповідь
        Answer ret = Answer.builder().status(true).build();
        
        //Валідуємо ім'я (має містити тількі велики та маленькі літери, апостроф та дефіс)
        ret = isValidStr(persona.getFirstName(),"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ",0);
        if(!ret.getStatus())return ret;
        
        //Валідуємо призвіще (має містити тількі велики та маленькі літери, апостроф та дефіс)
        ret = isValidStr(persona.getLastName(),"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ",0);
        if(!ret.getStatus())return ret;

        //Валідуємо по батькові (має містити тількі велики та маленькі літери, апостроф та дефіс)
        ret = isValidStr(persona.getPatronymic(),"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ",0);
        if(!ret.getStatus())return ret;
        
        //Валідуємо РНОКПП (має містити тількі 10 цифр)
        if(persona.getRnokpp().length()!=10){
            ret.setResult("РНОКПП має бути заповнено та містити 10 цифр!");
            ret.setStatus(Boolean.FALSE);
            return ret;
        }
        ret = isValidStr(persona.getRnokpp(),"0123456789",10);
        if(!ret.getStatus())return ret;

        //Валідуємо паспорт (може бути або цифровий, або тільки перши 2 літери + 6 цифр)
        ret = isValidPasport(persona.getPasport());
        if(!ret.getStatus())return ret;

        //Валідуємо УНЗР (може бути двох варіантів: або тільки цифри, або 8 цифр + дефіс + ще 5 цифр)
        ret = isValidUnzr(persona.getUnzr());
        
        return ret;
    }

    private static Answer isValidPasport(String checkStr){
        Answer ret = Answer.builder().status(true).build();

        if((checkStr==null)||(checkStr==""))return ret;
        
        switch(checkStr.length()){
            case 9-> {
                String allowedStr = "0123456789";

                for(int i=0;i < checkStr.length();i++){
                    if(!allowedStr.contains(checkStr.subSequence(i, i+1))){
                        ret.setStatus(false);
                        ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                        return ret;
                    }
                }
            }
            case 8-> {
                String allowedStr = "abcdefghijklmnopqrstuvwxyzабвгґдеєжзиіїйклмнопрстуфхцчшщьюя";

                for(int i=0;i < 2;i++){
                    if(!allowedStr.contains(checkStr.toLowerCase().subSequence(i, i+1))){
                        ret.setStatus(false);
                        ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                        return ret;
                    }
                }
                
                allowedStr = "0123456789";
                
                for(int i=2;i < checkStr.length();i++){
                    if(!allowedStr.contains(checkStr.subSequence(i, i+1))){
                        ret.setStatus(false);
                        ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                        return ret;
                    }
                }
            }
            default -> {
                ret.setStatus(false);
                ret.setDescr(checkStr+" містить неприпустиму кількість символів '"+checkStr.length()+"'");
            }
            
        }
        
        return ret;
    }
    
    private static Answer isValidUnzr(String checkStr){
        Answer ret = Answer.builder().status(true).build();

        if((checkStr==null)||(checkStr==""))return ret;
 
        switch(checkStr.length()){
            case 13-> {
                String allowedStr = "0123456789";

                for(int i=0;i < checkStr.length();i++){
                    if(!allowedStr.contains(checkStr.subSequence(i, i+1))){
                        ret.setStatus(false);
                        ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                        return ret;
                    }
                }
            }
            case 14-> {
                String allowedStr = "0123456789";

                for(int i=0;i < 8;i++){
                    if(!allowedStr.contains(checkStr.toLowerCase().subSequence(i, i+1))){
                        ret.setStatus(false);
                        ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                        return ret;
                    }
                }
                
                allowedStr = "0123456789";
                
                for(int i=9;i < checkStr.length();i++){
                    if(!allowedStr.contains(checkStr.subSequence(i, i+1))){
                        ret.setStatus(false);
                        ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                        return ret;
                    }
                }
            }
            default -> {
                ret.setStatus(false);
                ret.setDescr(checkStr+" містить неприпустиму кількість символів '"+checkStr.length()+"'");
            }
            
        }
        
        return ret;
    }
    
    private static Answer isValidStr(String checkStr, String allowedStr, int maxChars){
        Answer ret = Answer.builder().status(true).build();

        if((checkStr==null)||(checkStr==""))return ret;
        
        if(maxChars == 0){
            maxChars = checkStr.length();
        }else{
            if(checkStr.length()!=maxChars){
                ret.setStatus(false);
                ret.setDescr(checkStr+" містить неприпустиму кількість символів '"+checkStr.length()+"' повинно бути "+maxChars);
                return ret;
            }
        }
        
        for(int i=0;i < maxChars;i++){
            if(!allowedStr.contains(checkStr.subSequence(i, i+1))){
                ret.setStatus(false);
                ret.setDescr(checkStr+" містить неприпустимі символи! '"+checkStr.subSequence(i, i+1)+"'");
                return ret;
            }
        }
                
        return ret;
    }
}

