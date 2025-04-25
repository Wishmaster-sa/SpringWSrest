package com.ega.SpringWS.services;

import com.ega.SpringWS.interfaces.LogRecordInterface;
import com.ega.SpringWS.models.LogRecord;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author sa
 */
@Component
@PropertySource("classpath:application.properties")
public class LogRecordService implements LogRecordInterface{
    //ініціалізація файлу налаштувань
    
    @Value("${webservice.settings.logfilename:springWSrest.log}")
    //повний шлях до лог-файлу
    private String logFileName;

    @Value("${webservice.settings.loglevel:0}") 
    //рівень логування.
    private int logLevel;
    
    
    @Override
    public Boolean addRecord(LogRecord record) {
    //    System.out.println("logFileName = "+logFileName+"\nLogLevel = "+logLevel);
        
        switch(logLevel){
            case 0 -> AddRecordLevel0(record); //немає логування
            case 1 -> AddRecordLevel1(record); //тільки помилки
            case 2 -> AddRecordLevel2(record); //всі повідомлення, але без тіла запиту та без тіла відповіді.
            case 3 -> AddRecordLevel3(record); //детальне логування.
        }
        
        return true;
    }

    //Level 0 - Log не потрібен.
    private void AddRecordLevel0(LogRecord record) {
        return;
    }

    //Level 1 - логує тільки помилки.
    private void AddRecordLevel1(LogRecord record) {
        if(record.isError()!=true){
            return;
        }
        WriteDataToLogFile(record.toJSON().toString());
    }

    //Level 2 - логує помилки, запити, але не включає до себе тіло запиту та повну відподідь.
    private void AddRecordLevel2(LogRecord record) {
        record.setBody("");
        record.setResult(null);
        WriteDataToLogFile(record.toJSON().toString());
    }

    //Level 3 - детально логує всі запити та відповіді включно з їх вмістом.
    private void AddRecordLevel3(LogRecord record) {
        WriteDataToLogFile(record.toJSON().toString());
    }

    //Безпосередньо записує лог в файл
    private void WriteDataToLogFile(String toString) {
        BufferedWriter writer;
        try {
            File f = new File(logFileName);
            if(!f.exists() && !f.isDirectory()) {
                f.createNewFile();
            }
                writer = new BufferedWriter(new FileWriter(logFileName, true));
                writer.append(toString+"\n");
                writer.close();

        } catch (IOException ex) {
            System.out.println("Неможливо зробити запис в лог-файл: "+ex.getMessage());
        }

    
    }
    
}
