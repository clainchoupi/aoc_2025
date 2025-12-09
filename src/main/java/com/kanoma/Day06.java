package com.kanoma;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day06 {
    public static class InnerDay06 {
        // Classes internes si nécessaire
        String operator;
        ArrayList<Long> values;
        public InnerDay06(ArrayList<Long> values) {
            this.values = values;
        }
        
        public void setOperator(String operator) {
            this.operator = operator;
        }
    }
    
    private static final Logger logger = LogManager.getLogger(Day06.class);
    public static void main(String[] args){
        logger.info("Day 06");
        var startTimer = Instant.now();
        String day = "06sample";
        //day = "06";
        //day = "06tu";
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
    * Premiere partie : faire la somme des opérations
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            long score =0;
            
            HashMap<Integer,InnerDay06> operations = new HashMap<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                // Split de la ligne avec nombre variable d'espaces
                String[] parts = line.trim().split("\\s+");
                
                // Pour chaque part, l'ajouter au i ème InnerDay06 de la map
                for (int i = 0; i < parts.length; i++) {
                    InnerDay06 inner = operations.get(i);
                    if (inner == null) {
                        ArrayList <Long> values = new ArrayList<>();
                        values.add(Long.parseLong(parts[i]));
                        inner = new InnerDay06(values);
                        operations.put(i, inner);
                    } else {
                        //Si c'est un opérateur
                        if (parts[i].equals("+") || parts[i].equals("*")) {
                            inner.setOperator(parts[i]);
                        } else {
                            inner.values.add(Long.parseLong(parts[i]));
                        }
                    }
                }
            }
            
            //Traitement des opérations
            for (Integer key : operations.keySet()) {
                InnerDay06 inner = operations.get(key);
                long result = inner.values.get(0);
                for (int i = 1; i < inner.values.size(); i++) {
                    if (inner.operator.equals("+")) {
                        result += inner.values.get(i);
                    } else if (inner.operator.equals("*")) {
                        result *= inner.values.get(i);
                    }
                }
                score += result;   
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    /**
    * Seconde partie : faire la somme des opérations en concaténant les chiffres unités, dizaines, centaines, etc.
    * @param reader
    */
    private static void partTwo(Scanner reader) {
        try {
            long score =0;

            // WARNING : dans cette partie, les espaces sont importants ()
            logger.info("PartTwo result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partTwo", e);
        }
    }
    
    
}
