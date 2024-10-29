/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS.interfaces;

import com.ega.SpringWS.models.Answer;
import com.ega.SpringWS.models.Persona;

/**
 * Це інтерфейс - клас який задає, які процедури та функції буде експорувати наша програма.
 */
public interface PersonaInterface {
   public Answer showAllPersons();
   public Answer addPersona(Persona persona);
   public Answer find(String rnokpp);
   public Answer checkup(String rnokpp);
   public Answer checkPersona(String rnokpp);
   public Answer updatePersona(Persona persona);
   public Answer deletePersona(String rnokpp);
   public Answer findByBirthDate(String birthDate);
   public Answer findByPasport(String pasport);
   public Answer findByUnzr(String unzr);
   public Answer findByFirstName(String firstName);
   public Answer findByLastNameWith(String firstName);
   public Answer findAllFirstNameContains(String firstName);
   public Answer findByAgeRange(Integer startAge,Integer endAge);
   
  
}
