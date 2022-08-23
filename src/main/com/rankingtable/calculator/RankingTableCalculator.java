package com.rankingtable.calculator;

import java.io.IOException;
import java.util.List;

/**
 * represents the contract that should agree
 * every RankingTable implementation , to extract the information from a source
 * and the process that information and return a result, the result should be
 * a RankingTable (String)
 */
public interface RankingTableCalculator {
	
	
	List<String> getFileLinesList(String filePath) throws IOException;
	
	String getRankingTableInfo(List<String> filelines);
	
	

}
