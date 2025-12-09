package com.kanoma;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day08 {
    
    private static final Logger logger = LogManager.getLogger(Day08.class);
    public static void main(String[] args){
        logger.info("Day 08");
        var startTimer = Instant.now();
        String day = "08sample";
        //day = "08";
        //day = "08tu";
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
    * Premiere partie : faire la somme des opérations
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            long score =0;
            // Liste de points en 3 dimensions
            ArrayList<int[]> points = new ArrayList<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                String[] parts = line.split(",");
                points.add(new int[] {
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
                });
                
            }
            
            //Traitement des lignes
            // Pour chaque point de la liste, on calcule la distance avec les autres points
            HashMap<Integer, ArrayList<Double>> distancesMap = new HashMap<>();
            for (int i = 0; i < points.size(); i++) {
                ArrayList<Double> distances = new ArrayList<>();
                
                int[] p1 = points.get(i);
                for (int j = 0; j < points.size(); j++) {
                    int[] p2 = points.get(j);
                    double dist = distance(p1, p2);
                    distances.add(dist);
                }
                distancesMap.put(i, distances);
            }
            logger.debug("Distances calculées");
            
            //
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    /**
    * Fonction de calcul de la distance entre deux points
    * @param p1
    * @param p2
    * @return
    */    
    private static double distance(int[] p1, int[] p2) {
        return Math.sqrt(
            Math.pow(p1[0]-p2[0], 2) +
            Math.pow(p1[1]-p2[1], 2) +
            Math.pow(p1[2]-p2[2], 2)
        );
    }
    
}
