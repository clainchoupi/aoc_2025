package com.kanoma;

import java.io.FileNotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day03 {
    private static final Logger logger = LogManager.getLogger(Day03.class);
    public static void main(String[] args){
        logger.info("Day 03");
        var startTimer = Instant.now();
        String day = "03sample";
        //day = "03";
        //day = "03tu";
        try {
            try (Scanner s1 = Utils.getScannerForDay(day)) {
                partOne(s1);
            }
            
            try (Scanner s2 = Utils.getScannerForDay(day)) {
                //Expected sample : 3121910778619
                //Mine :            3040370766761
                partTwo(s2);
            }
        } catch (FileNotFoundException e) {
            logger.error("Resource for day {} not found", day);
        }
        
        var endTimer = Instant.now();
        logger.info("Execution time: " + Duration.between(startTimer, endTimer).toMillis() + " ms");
    }
    
    /**
    * Premiere partie : trouver le couple de batteries maximal 
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            
            long score =0;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();

                //Pour chaque ligne, chercher le caractère max de la chaîne (sauf le dernier caractère)
                int maxChar = 0;
                int secondMaxChar = 0;
                int maxCharIndex = -1;

                long lineValue =0;
                for (int i = 0; i < line.length() -1; i++){
                    int currentChar = Character.getNumericValue(line.charAt(i));
                    if (currentChar > maxChar){
                        maxChar = currentChar;
                        maxCharIndex = i;
                    }
                }

                //Chercher le second caractère max de la chaîne situé après maxChar
                for (int i = maxCharIndex +1; i < line.length(); i++){
                    int currentChar = Character.getNumericValue(line.charAt(i));
                    if (currentChar > secondMaxChar){
                        secondMaxChar = currentChar;
                    }
                }


                lineValue= maxChar*10 + secondMaxChar;

                score += lineValue;
                
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }


    /**
    * Seconde partie : trouver les 12nombres de batteries maximal
    * @param reader
    */
    private static void partTwo(Scanner reader) {
        try {
            
            long score =0;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();

                //Pour chaque ligne, chercher le caractère max de la chaîne (sauf les onze derniers caractères)
                int maxChar = 0;
                int maxCharIndex = -1;

                long lineValue =0;
                for (int i = 0; i < line.length() -11; i++){
                    int currentChar = Character.getNumericValue(line.charAt(i));
                    if (currentChar > maxChar){
                        maxChar = currentChar;
                        maxCharIndex = i;
                    }
                }

                //Chercher les onze caractères max de la chaîne situé après maxChar
                // Faire un stream sur la fin de la chaine et récupérer les 11 plus grands caractères
                char[] finChaine = line.substring(maxCharIndex +1).toCharArray();
                
                //Dans le tableau finChaine, récupérer les 11 plus grands caractères sans changer leur ordre
                // 1. Trier une copie pour trouver le seuil des 11 plus grandes valeurs
                char[] sorted = finChaine.clone();
                java.util.Arrays.sort(sorted);
                // Le seuil est la valeur à l'index (length - 11)
                char seuil = sorted[Math.max(0, sorted.length - 11)];
                
                // 2. Parcourir le tableau original et garder les valeurs >= seuil (max 11)
                StringBuilder result = new StringBuilder();
                int count = 0;
                for (char c : finChaine) {
                    if (c >= seuil && count < 11) {
                        result.append(c);
                        count++;
                    }
                }
                
                // result contient maintenant les 11 plus grandes valeurs dans leur ordre d'origine
                String resultString = maxChar + result.toString();
               

                /**
                 * 987654321111 + 811111111119 + 434234234278 + 888911112111 = 3121910778619.
                 */
                score += Long.parseLong(resultString);
                
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    
}
