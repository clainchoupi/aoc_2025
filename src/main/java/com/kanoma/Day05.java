package com.kanoma;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Range;

public class Day05 {
    private static final Logger logger = LogManager.getLogger(Day05.class);
    public static void main(String[] args){
        logger.info("Day 05");
        var startTimer = Instant.now();
        String day = "05sample";
        day = "05";
        //day = "05tu";
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
    * Premiere partie : trouver le nombre d'ingrédients frais
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            long score =0;
            ArrayList<Range<Long>> ranges = new ArrayList<>();
            ArrayList<Long> ingredients = new ArrayList<>();
            
            boolean firstBlockDone = false;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                
                if (line.isEmpty()) {
                    // Ligne vide = passage au deuxième bloc
                    firstBlockDone = true;
                    continue;
                }
                
                if (!firstBlockDone) {
                    // Premier bloc de lignes
                    ranges.add(Range.closed(
                        Long.parseLong(line.split("-")[0]),
                        Long.parseLong(line.split("-")[1])
                    ));
                } else {
                    // Deuxième bloc de lignes
                    ingredients.add(Long.parseLong(line));
                }
            }
            
            //Traitement des ingrédients
            for (Long ingredient : ingredients) {
                boolean isFresh = false;
                for (Range<Long> range : ranges) {
                    if (range.contains(ingredient.longValue())) {
                        isFresh = true;
                        break;
                    }
                }
                if (isFresh) {
                    score++;
                }
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    
    /**
    * Seconde partie : trouver le nombre d'ingrédients dans les plages d'ID
    * @param reader
    */
    private static void partTwo(Scanner reader) {
        try {
            long score =0;
            ArrayList<Range<Long>> ranges = new ArrayList<>();
            ArrayList<Long> ingredients = new ArrayList<>();
            
            boolean firstBlockDone = false;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                
                if (line.isEmpty()) {
                    // Ligne vide = passage au deuxième bloc
                    firstBlockDone = true;
                    continue;
                }
                
                if (!firstBlockDone) {
                    // Premier bloc de lignes
                    String[] parts = line.split("-");
                    ranges.add(Range.closed(
                        Long.parseLong(parts[0]),
                        Long.parseLong(parts[1])
                    ));
                } else {
                    // Deuxième bloc de lignes
                    ingredients.add(Long.parseLong(line));
                }
            }
            
            //Traitement des ingrédients
            //tri des ranges
            ranges.sort((r1, r2) -> Long.compare(r1.lowerEndpoint(), r2.lowerEndpoint()));
            //fusion des ranges
            ArrayList<Range<Long>> mergedRanges = new ArrayList<>();
            Range<Long> currentRange = null;
            for (Range<Long> range : ranges) {
                if (currentRange == null) {
                    currentRange = range;
                } else {
                    if (currentRange.isConnected(range)) {
                        currentRange = currentRange.span(range);
                    } else {
                        mergedRanges.add(currentRange);
                        currentRange = range;
                    }
                }
            }
            if (currentRange != null) {
                mergedRanges.add(currentRange);
            } 

            //Compter le nombre d'ID à l'interieur des plages fusionnées
            for (Range<Long> range : mergedRanges) {
                score += (range.upperEndpoint() - range.lowerEndpoint() + 1);
            }
            
            logger.info("PartTwo result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partTwo", e);
        }
    }
    
}
