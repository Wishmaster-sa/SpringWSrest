/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author sa

 Цей вебсервіс є частиною навчальних матеріалів для розробників системи "Трембіта".
 Вебсервіс, як приклад, логує всі HTTP-заголовки, які отримує з запитами.
 У промисловому сервісі слід передбачити логування службових заголовків системи "Трембіта".
 Перелік службових заголовків системи "Трембіта":
 Uxp-Clien
 Uxp-Service
 Uxp-Queryid
 Uxp-Userid
 Uxp-Purpose-Ids
 Uxp-Subject-Id
 */

//якщо потрібно до кожної відповіді додавати якусь інформацію, то це робиться за допомогою WebFilter.
//@WebFilter("/filter-response-header/*")
//!дослідити! Додаємо до кожного повідомлення з відповіддю HTTP заголовок "Access-Control-Allow-Origin" (щоб браузер не лаявся на CORS)
//Додаємо HTTP заголовок "Cache-Control": "no-store" який вказує, що будь-який кеш будь-якого типу (приватний або загальний) не повинен зберігати відповідь, що містить цей заголовок
//Додаємо HTTP заголовок "Content-Security-Policy": "frame-ancestors 'none'". Цей заголовок використовується для зазначення, що відповідь не може бути укладена в елемент <frame> на вебсторінках.
//Цей заголовок захищає від атак типу Clickjacking
//Додаємо HTTP заголовок "X-Content-Type-Options": "nosniff". Цей заголовок вказує браузеру завжди використовувати тип MIME,
//оголошений у Content-Type заголовку, а не намагатися визначити тип MIME на основі вмісту файлу. 
//Цей заголовок з nosniff значенням не дозволяє браузерам виконувати аналіз MIME та некоректно інтерпретувати відповіді як HTML
//Додаємо HTTP заголовок "X-Frame-Options": "DENY". Цей заголовок використовується для зазначення, що відповідь не може бути укладена в елемент <frame>.
//Для відповіді API не потрібно, щоб вона була вбудована в будь-який з цих елементів на сторонніх ресурсах та вебсторінках.
//Встановлення цього заголовку не дозволяє будь-якому сторонньому домену формувати вебсторінку, що містить дані цього повідомлення-відповіді
//Цей заголовок зі значенням "DENY" захищає від атак Clickjacking
@WebFilter()
public class AddResponseHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(
          "Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader(
          "Cache-Control", "no-store");
        httpServletResponse.setHeader(
          "Content-Security-Policy", "frame-ancestors 'none'");
        httpServletResponse.setHeader(
          "Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");
        httpServletResponse.setHeader(
          "X-Content-Type-Options", "nosniff");
        httpServletResponse.setHeader(
          "X-Frame-Options", "DENY");
        chain.doFilter(request, response);
    }
}
