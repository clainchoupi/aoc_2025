package com.kanoma;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day11 {
    
    
    private static final Logger logger = LogManager.getLogger(Day11.class);
    public static void main(String[] args){
        logger.info("Day 11");
        var startTimer = Instant.now();
        String day = "11sample";
        //day = "11";
        //day = "11tu";
        try {
            try (Scanner s1 = Utils.getScannerForDay(day)) {
                partOne(s1);
            }
            
            try (Scanner s2 = Utils.getScannerForDay(day)) {
                //partTwo(s2);
            }
        } catch (FileNotFoundException e) {
            logger.error("Resource for day {} not found", day);
        }
        
        var endTimer = Instant.now();
        logger.info("Execution time: " + Duration.between(startTimer, endTimer).toMillis() + " ms");
    }
    
    
    /**
    * Premiere partie : trouver les chemins de 'you' à 'out'
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            long maxArea =0;
            HashMap<String, String[]> map = new HashMap<>();
            ArrayList<String> results = new ArrayList<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                //Split de la ligne : 
                //Exemple : "bbb: ddd eee" 
                //Avant le : c'est la clé
                //Après le : ce sont les valeurs associées à la clé, séparées par des espaces
                //Cela représente un arbre de connexions
                String[] parts = line.split(":");
                String key = parts[0].trim(); 
                String[] values = parts[1].trim().split(" ");
                map.put(key, values);
            }
            
            // 'you' est la racine de l'arbre
            String[] startValues = map.get("you");
            for (String sv : startValues){
                String path = "you-" + sv;
                explorePath(map, sv, path, results);
            }
            
            
            logger.info("PartOne result --> Score: " + results.size());
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    
}
