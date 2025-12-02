package com.kanoma;

import java.io.FileNotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day02 {
    private static final Logger logger = LogManager.getLogger(Day02.class);
    public static void main(String[] args){
        logger.info("Day 02");
        var startTimer = Instant.now();
        String day = "02sample";
        day = "02";
        //day = "02tu";
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
    * Premiere partie : compte le nombre de chiffres composé de deux fois la même séquence de chiffres
    * @param reader
    */
    private static void partOne(Scanner reader) {
        try {
            
            long score =0;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                //Récupération des séquences
                //11-22,95-115,998-1012,1188511880-1188511890
                String[] sequences = line.split(",");
                for (String seq : sequences){
                    String[] bornes = seq.split("-");
                    long start = Long.parseLong(bornes[0]);
                    long end = Long.parseLong(bornes[1]);
                    for (long i = start; i <= end; i++){
                        if(isValidPartOne(i)){
                            score +=i;
                        }
                    }
                }
            }
            
            logger.info("PartOne result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partOne", e);
        }
    }
    
    /**
    * Seconde partie : compte le nombre composés de n fois la même séquence de chiffres
    * @param reader
    */
    private static void partTwo(Scanner reader) {
        try {
            
            long score =0;
            
            //Lecture du fichier
            while (reader.hasNext()){
                String line = reader.nextLine();
                //Récupération des séquences
                //11-22,95-115,998-1012,1188511880-1188511890
                String[] sequences = line.split(",");
                for (String seq : sequences){
                    String[] bornes = seq.split("-");
                    long start = Long.parseLong(bornes[0]);
                    long end = Long.parseLong(bornes[1]);
                    for (long i = start; i <= end; i++){
                        if(isValidPartTwo(i)){
                            score +=i;
                        }
                    }
                }
            }
            
            logger.info("PartTwo result --> Score: " + score);
            
        } catch (Exception e) {
            logger.error("Error while processing partTwo", e);
        }
    }
    
    private static boolean isValidPartOne (long x){
        //Vérifie si x est composé de deux fois la même séquence de chiffres
        String strX = Long.toString(x);
        
        // Si nombre de chiffre impair, retourne faux
        if (strX.length() % 2 != 0){
            return false;
        }
        // Sinon, vérifie si les deux moitiés sont identiques
        String firstHalf = strX.substring(0, strX.length() / 2);
        String secondHalf = strX.substring(strX.length() / 2);
        return firstHalf.equals(secondHalf);
    }
    
    
    private static boolean isValidPartTwo (long x){
        //Vérifie si x est composé de n fois la même séquence de chiffres
        String strX = Long.toString(x);
        int len = strX.length();

        // Teste toutes les longueurs de motifs possibles
        // AABBCCAABBCC
        for (int i = 1; i <= len / 2; i++) {
            // Si la longueur totale est divisible par i
            if (len % i == 0) {
                String pattern = strX.substring(0, i);

                // Crée une chaîne en répétant les caractères du motif
                StringBuilder sb = new StringBuilder(len);
                for (int j = 0; j < len / i; j++) {
                    sb.append(pattern);
                }
                if (sb.toString().equals(strX)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isValidPartTwoReviewed (long x){
        //Vérifie si x est composé de n fois la même séquence de chiffres
        String strX = Long.toString(x);
        int nbChar = strX.length();
        
        // Teste toutes les longueurs de motifs possibles
        for (int i = 1; i <= nbChar / 2; i++) {
            // Si la longueur totale est divisible par i
            if (nbChar % i == 0) {
                // du debut jusqu'à i
                String pattern = strX.substring(0, i);
                
                // Crée une chaîne en répétant le motif
                String repeated = pattern.repeat(nbChar / i);
                if (repeated.equals(strX)) {
                    return true;
                }
            }
        }
        return false;
    }
}
