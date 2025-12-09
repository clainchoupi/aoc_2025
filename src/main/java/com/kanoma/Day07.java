package com.kanoma;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day07 {
    
    private static final Logger logger = LogManager.getLogger(Day07.class);
    public static void main(String[] args){
        logger.info("Day 07");
        var startTimer = Instant.now();
        String day = "07sample";
        day = "07";
        //day = "07tu";
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
            ArrayList<char[]> lines = new ArrayList<>();
            HashSet<Integer> currentPositions = new HashSet<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                lines.add(line.toCharArray());
            }

            //Traitement des lignes
            //Premiere ligne : récupérer la position de la lettre 'S'
            char[] firstLine = lines.get(0);
            for (int i=0; i<firstLine.length; i++){
                if (firstLine[i]=='S'){
                    currentPositions.add(i);
                }
            }

            //Parcourir les lignes suivantes
            for (int i=1; i<lines.size(); i++){
                char[] line = lines.get(i);
                HashSet<Integer> newPositions = new HashSet<>();

                //Si line[i] est un '.', on peut avancer
                for (Integer pos : currentPositions){
                    if (line[pos]=='.'){
                        newPositions.add(pos);
                    }
                    //Sinon si c'est un '^' on ajoute 1 au score et on ajoute deux positions à -1 et +1
                    else if (line[pos]=='^'){
                        score ++;
                        newPositions.add(pos-1);
                        newPositions.add(pos+1);
                    }
                }
                currentPositions = newPositions;
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
}
