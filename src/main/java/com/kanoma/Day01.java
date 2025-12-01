package com.kanoma;

import java.io.FileNotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day01 {
    private static final Logger logger = LogManager.getLogger(Day01.class);
    public static void main(String[] args){
        logger.info("Day 01");
        var startTimer = Instant.now();
        //String day = "01sample";
        String day = "01";
        try {
            try (Scanner s1 = Utils.getScannerForDay(day)) {
                partOne(s1);
            }
            
            try (Scanner s2 = Utils.getScannerForDay(day)) {
                partTwo(s2);
            }
        } catch (FileNotFoundException e) {
            logger.error("Resource for day {} not found", day);
        }
        
        var endTimer = Instant.now();
        logger.info("Execution time: " + Duration.between(startTimer, endTimer).toMillis() + " ms");
    }
    
    /**
    * Premiere partie : compte le nombre de fois où on revient à la position 0
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            List<Map.Entry<Character,Integer>> list = new ArrayList<>();
            int nbFoisZero = 0;
            int position = 50;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                //Pour chaque ligne récupérer le premier caractère pour avoir la direction
                // puis les nombres associés
                char direction = line.charAt(0);
                int value = Integer.parseInt(line.substring(1).trim());
                list.add(Map.entry(direction, value));
            }
            
            //Traitement des données
            for (Map.Entry<Character,Integer> entry : list) {
                char direction = entry.getKey();
                int value = entry.getValue();   
                if (direction == 'L') {
                    position = Math.floorMod(position - value, 100);
                } else if (direction == 'R') {
                    position = Math.floorMod(position + value, 100);
                }
                
                if (position == 0) {
                    nbFoisZero++;
                }
                
            }
            
            logger.info("PartOne result --> NbZero: " + nbFoisZero);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
        
    }
    
    
    /**
    * Deuxième partie : compte le nombre de fois où on passe par la position 0
    * @param reader
    */
    private static void partTwo(Scanner reader) {
        try {
            List<Map.Entry<Character,Integer>> list = new ArrayList<>();
            int nbPassagesZero = 0;
            int position = 50;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                //Pour chaque ligne récupérer le premier caractère pour avoir la direction
                // puis les nombres associés
                char direction = line.charAt(0);
                int value = Integer.parseInt(line.substring(1).trim());
                list.add(Map.entry(direction, value));
            }
            
            //Traitement des données
            for (Map.Entry<Character,Integer> entry : list) {
                char direction = entry.getKey();
                int value = entry.getValue();   
                
                for (int i = 0; i < value; i++) {
                    if (direction == 'L') {
                        position = Math.floorMod(position - 1, 100);
                    } else if (direction == 'R') {
                        position = Math.floorMod(position + 1, 100);
                    }
                    
                    if (position == 0) {
                        nbPassagesZero++;
                    }
                }
                
            }
            
            logger.info("PartTwo result --> NbPassagesZero: " + nbPassagesZero);
            
        } catch (Exception e) {
            logger.error("Error while processing partTwo", e);
        }
        
    }

}
