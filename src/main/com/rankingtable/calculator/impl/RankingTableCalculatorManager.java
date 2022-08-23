package com.rankingtable.calculator.impl;


import com.rankingtable.calculator.RankingTableCalculator;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



public class RankingTableCalculatorManager {
	
	private static final RankingTableCalculator rt = new RankingTableCalculatorImpl();
	private static final Logger LOGGER = Logger.getLogger("RankingTableCalculatorManager");
	
	
	public static void printMenu(String[] options){
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }
	
    private static final String[] options = {"1- print rankingtable ",
            "2- enter a new filepath to process",
            "3- Exit",
    };
    
    public static void main(String[] args) {
    	
    
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the filePath for processing: ");
        String filePath = scanner.next();
        System.out.println("[ "+ filePath + "] - filepath has been registered ... ");
        System.out.println();
        int option = 1;
        while (option!=4){
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option){
                    case 1:  executeOption1(filePath); 	break;
                    
                    case 2: {
                    	System.out.print("Enter the new filePath for processing: ");
                    	
                        filePath = scanner.next();
                        System.out.println("[ "+ filePath + "] - filepath has been registered ... ");
                    	System.out.println();
                    }break;
                    case 3: scanner.close(); System.exit(0);
                    default :System.out.println("opcion  no valida");
                }
            }
            catch (Exception ex){
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            }
        }
    }
    
    private static void executeOption1(String filePath) {
    	List<String> lines = null;
		try {
			lines = rt.getFileLinesList(filePath);
			System.out.println(rt.getRankingTableInfo(lines));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "exception  while reading file " + filePath + e.getCause());
		}
		
    }
    
	

}
