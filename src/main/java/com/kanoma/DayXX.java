/**
 * Cette classe est un template pour les jours de l'avent de code.
 * 
 * 
 * 
 * 
 * 
 */


package com.kanoma;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DayXX {
    private static final Logger logger = LogManager.getLogger(DayXX.class);
    public static void main(String[] args){
        var startTimer = Instant.now();

        // remplacer XX par le numéro du jour
        String day = "XX";
        File inputFile = new File("src/main/resources/DAY/"+day+".txt");
        partOne(inputFile);

        var endTimer = Instant.now();
        logger.info("Execution time: " + Duration.between(startTimer, endTimer).toMillis() + " ms");
    }
    
    private static void partOne(File file) {
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNext()){
                String line = reader.nextLine();
                System.out.println(line);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
        
    }

    
}
