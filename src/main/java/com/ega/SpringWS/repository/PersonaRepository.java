/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS.repository;

import com.ega.SpringWS.models.Persona;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Це інтерфейс репозіторію - сутність, яка вказує, які процедури та функції буде експорувати наша програма
 * для роботи з базою даних. Зверніть увагу, що основні функції SELECT, INSERT, UPDATE, DELETE тут не зазначені
 * так як вони додаються автоматично бібліотекою JpaRepository. В JPA вони називаються find, save, save, delete відповідно.
 * Для додавання інших функцій уважно читайте опис бібліотеки JPA
 * https://ru.hexlet.io/courses/java-spring/lessons/jpa-repository/theory_unit
 */
public interface PersonaRepository extends JpaRepository<Persona, Long>{

    //зверніть увагу, що назва методу і регістр символів має значення, повинно бути не deleteByRNOKPP, а deleteByRnokpp. Інакше JpaRepository не зможе взяти реалізацію цього метода на себе!
    void deleteByRnokpp(String rnokpp);
    
    //приклад пошуку користувача по RNOKPP
    Persona findByRnokpp(String rnokpp);

    //приклад пошуку користувача по UNZR
    Persona findByUnzr(String unzr);

    //приклад пошуку користувача по Pasport
    Persona findByPasport(String pasport);
    
    //приклад пошуку користувача по birthDate
    List <Persona> findByBirthDate(LocalDate birthDate);

    //приклад пошуку користувача по імені
    List <Persona> findAllByFirstName(String firstName);
    
    //приклад пошуку користувача по прізвищу, яке починається з заданого перфікса
    List <Persona> findByLastNameStartingWith(String prefix);

    //приклад пошуку користувача по даті народження
    List <Persona> findByBirthDateBetween(LocalDate startAge, LocalDate endAge);
    
    //приклад кастомного методу, в якому напряму задається що шукати (всі імена, які мають в назві шаблон)
    @Query("SELECT e FROM Persona e WHERE e.firstName LIKE %:firstName%")
    List <Persona> findAllByFirstNameContaining(@Param("firstName") String firstName);
}
