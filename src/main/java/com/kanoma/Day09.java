package com.kanoma;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day09 {

    //Inner class for coordinates
    private static class Coordinate {
        int x;
        int y;
        
        Coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }
        
    }
    
    private static final Logger logger = LogManager.getLogger(Day09.class);
    public static void main(String[] args){
        logger.info("Day 09");
        var startTimer = Instant.now();
        String day = "09sample";
        day = "09";
        //day = "09tu";
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
    * Premiere partie : chercher la plus grande aire entre deux points
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            long maxArea =0;
            ArrayList<Coordinate> points = new ArrayList<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                points.add(new Coordinate(x, y));
            }

            // Pour chaque paire de points, calculer l'aire et garder la plus grande
            for (int i = 0; i < points.size(); i++) {
                for (int j = i + 1; j < points.size(); j++) {
                    long area = new Day09().calculateArea(points.get(i), points.get(j));
                    if (area > maxArea) {
                        maxArea = area;
                    }
                }
            }

            logger.info("PartOne result --> Score: " + maxArea);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }

    public long calculateArea(Coordinate a, Coordinate b) {
        long longueur = Math.abs(a.x - b.x) + 1L;
        long largeur = Math.abs(a.y - b.y) + 1L;  

        return longueur * largeur;
    }

}
