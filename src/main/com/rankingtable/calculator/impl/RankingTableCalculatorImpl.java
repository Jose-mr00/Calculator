package com.rankingtable.calculator.impl;

import com.rankingtable.calculator.RankingTableCalculator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation for the interface RankingTableCalculator
 * provides a solution for parsing file lines into matches results
 * also generates a string representation of a ranking table based on the match results.
 * the ranking table is listed on descending order based on the scores and then for same scores
 * the order is based on the ascending order of team names.
 */
public class RankingTableCalculatorImpl implements RankingTableCalculator {

	private static final String EMPTY_STRING = "";
	private static final String BLANK_SEPARATOR = " ";
	private static final String COLON_SEPARATOR = ",";
	
	private static final Logger LOGGER = Logger.getLogger("RankingTableCaclculatorImpl");


	/**
	 * @param filePath where the file to be processed  is located
	 * @return a list of string , every string is a representation of a line in the file
	 * @throws IOException
	 */
	@Override
	public List<String> getFileLinesList(String filePath) throws IOException {
		
		Stream<String> lines = null;
		List<String> linesList = new ArrayList<>();
		try {
			
			lines = Files.lines(Paths.get(filePath));
			linesList =  lines.filter(e->{return StringUtils.isNotEmpty(e.trim());}).collect(Collectors.toList());

		} catch (IOException e) {
			String message = "exception  while reading file " + filePath + " - " + e;
			LOGGER.log(Level.FINER, message);
			throw e;
		}
		finally {
			if (lines != null){
			lines.close();}
		}
		return linesList;
	}

	/**
	 * generates a ranking table based on the filelines that represent results for
	 * matches , the line should contain the following format :
	 *
	 * 	  "team A name" "team A goals(numeric)" , "team b name" "team B goals(numeric)"
	 * 	   SAMPLE : "lions 3 , tigers 3"
	 *
	 * the rules are as follows :
	 *
	 *  the winner receives 3 points , the loser 0
	 *  a tie = 1 point for each team.
	 *
	 * @param fileLines  the lines
	 * @return a string representation of a ranking table
	 */
	@Override
	public String getRankingTableInfo(List<String> fileLines) {
		
		Map<String, Team> teamsInformation = new HashMap<>();
			
		extractLinesInformation(fileLines, teamsInformation);
		
		List<Team> teamsByRankingOrder = teamsInformation.values().stream().collect(Collectors.toList());
		
		teamsByRankingOrder.sort(Comparator.comparing(Team::getPoints, Comparator.reverseOrder())
				.thenComparing(Team::getName));
		
		LOGGER.log(Level.FINER,formatRankingTableInfo(teamsByRankingOrder).toString());
		

		return formatRankingTableInfo(teamsByRankingOrder).toString();
	}


	/**
	 * format the resulting rankingTable
	 * @param result
	 * @return
	 */
	private StringBuilder formatRankingTableInfo(List<Team> result) {
		
		Integer previousPoints = null;
		int lineNumber = 1;
		StringBuilder builder = new StringBuilder();
		
		for (Team team : result){
			
			if( previousPoints == null) {
				previousPoints = team.getPoints();
				builder.append(lineNumber);
				builder.append( ". ");
				builder.append( team.outPutToString());
				builder.append(System.lineSeparator());
				
			}else {
				if (previousPoints == team.getPoints()){
					builder.append(lineNumber);
					builder.append( ". ");
					builder.append( team.outPutToString());
					builder.append(System.lineSeparator());
					
				}else{
					lineNumber++;
					builder.append(lineNumber);
					builder.append( ". ");
					builder.append( team.outPutToString());
					builder.append(System.lineSeparator());
					previousPoints = team.getPoints();
				}
			}
		}
		return builder;
	}

	/**
	 * utility method that extracts the information from the text lines
	 * stores the information in a map.
	 * @param fileLines
	 * @param rankingTable
	 */
	private void extractLinesInformation(List<String> fileLines, Map<String, Team> rankingTable) {
		
		for (String line : fileLines){
			
			if (StringUtils.isNotEmpty(line)) {
				
				//Extracting elements for computation
				// teams names , teams goals per match
				String [] sides = line.split(COLON_SEPARATOR);
				String [] elementsSideA = sides[0].split(BLANK_SEPARATOR);
				String [] elementsSideB = sides[1].split(BLANK_SEPARATOR);
				
				Integer teamAGoals;
				try{
				 teamAGoals =  Integer.valueOf(elementsSideA[(elementsSideA.length)-1]);
				}catch(NumberFormatException ex){
					//if we can not get the goals means the line has wrong information so we skip that line and log error
					LOGGER.log(Level.SEVERE, "line with error : " + line);
					continue;
				}
				elementsSideA[(elementsSideA.length)-1] = EMPTY_STRING;
				String teamAName = String.join(BLANK_SEPARATOR, elementsSideA).trim();
				Integer teamBGoals;
				try{
				 teamBGoals =  Integer.valueOf(elementsSideB[(elementsSideB.length)-1]);
				}catch(NumberFormatException ex){
					//if we can not get the goals means the line has wrong information so we skip that line and log error
					LOGGER.log(Level.SEVERE, "line with error : " + line); 
					continue;
				}
				elementsSideB[(elementsSideB.length)-1] = EMPTY_STRING;
				String teamBName = String.join(BLANK_SEPARATOR, elementsSideB).trim();
		
				Integer teamAPoints = 0;
				Integer teamBPoints = 0;
				
				//defining points for the match
				
				if (teamAGoals == teamBGoals){
					teamAPoints = 1;
					teamBPoints = 1;
				}else if (teamAGoals < teamBGoals){
					teamBPoints = 3;
				}else {
					teamAPoints = 3;
				}
				
				//adding resulting points to every team
				registerTeamDetails(rankingTable, teamAGoals, teamAName, teamAPoints);
				registerTeamDetails(rankingTable, teamBGoals, teamBName, teamBPoints);
				
	
				}
			}
	}

	/**
	 * adds the corresponding details (goals , points ) to teams.
	 *
	 * @param rankingTable
	 * @param teamGoals
	 * @param teamName
	 * @param teamPoints
	 */
	private void registerTeamDetails(Map<String, Team> rankingTable, Integer teamGoals, String teamName,
			Integer teamPoints) {
		Team team = rankingTable.get(teamName);
		if(team != null ){
			team.setGoals(team.getGoals() + teamGoals);
			team.setPoints(team.getPoints() + teamPoints);
		}else {
			rankingTable.put(teamName,Team.builder().name(teamName).goals(teamGoals).points(teamPoints).build());
		
		}
	}


}
