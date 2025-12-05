package com.kanoma;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day04 {
    private static final Logger logger = LogManager.getLogger(Day04.class);
    public static void main(String[] args){
        logger.info("Day 04");
        var startTimer = Instant.now();
        String day = "04sample";
        day = "04";
        //day = "04tu";
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
    * Premiere partie : trouver le nombre de cases éligibles dans la grille
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            
            long score =0;
            ArrayList<char[]> grid = new ArrayList<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                
                char[] charLine = line.toCharArray();
                grid.add(charLine); 
            }
            
            //Parcours de la grille pour trouver les cases éligibles
            int rows = grid.size();
            int cols = grid.get(0).length;
            for (int i = 0; i < rows; i++){
                for (int j = 0; j < cols; j++){
                    char currentChar = grid.get(i)[j];
                    if (isEligiblePartOne(grid, i, j, currentChar)){
                        score++;
                    }
                }
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    /**
    * Vérifie si une case est éligible selon les règles de la première partie.
    * @param reader
    */
    private static boolean isEligiblePartOne(ArrayList<char[]> grid, int row, int col, char currentChar) {
        int rows = grid.size();
        int cols = grid.get(0).length;
        
        if (currentChar == '.') {
            return false; 
        }else {
            //Vérifier si la case est entourée d'un maximum de 3 cases avec un '@'
            int atCount = 0;
            for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
                for (int j = Math.max(0, col - 1); j <= Math.min(cols - 1, col + 1); j++) {
                    if (i == row && j == col) {
                        continue;
                    }
                    if (grid.get(i)[j] == '@') {
                        atCount++;
                    }
                }
            }
            return atCount <= 3;
        }
    }
    
    /**
    * Seconde partie : si une case est éligible selon les règles de la seconde partie, alors on l'enlève et on reboucle sur la grille
    * @param reader
    */
    private static void partTwo(Scanner reader) {
        try {
            
            long score =0;
            long previousScore = -1;
            ArrayList<char[]> grid = new ArrayList<>();
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                
                char[] charLine = line.toCharArray();
                grid.add(charLine); 
            }
            
            //Parcours de la grille pour trouver les cases éligibles
            int rows = grid.size();
            int cols = grid.get(0).length;
            while (true) {
                previousScore = score;
                for (int i = 0; i < rows; i++){
                    for (int j = 0; j < cols; j++){
                        char currentChar = grid.get(i)[j];
                        if (isEligiblePartOne(grid, i, j, currentChar)){
                            score++;
                            grid.get(i)[j] = '.'; //On enlève la case éligible
                            logger.info("PartTwo result --> TempScore: " + score);
                        }
                    }
                }
                if (score == previousScore) {
                    break; // Aucune case éligible trouvée, on sort de la boucle
                }
            }
            logger.info("PartTwo result --> Final Score: " + score);
        } catch (Exception e) {
            logger.error("Error while processing partTwo", e);
        }
    }
    
    
}
