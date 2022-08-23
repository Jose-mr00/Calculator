package com.rankingtable.calculator.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Team {
	
	private String name;
	private Integer points;
	private Integer goals;

	
	@Override
	public String toString(){
		return "Team :" + name + "[ points : " + points + " , goals : " + goals + "]";
				 
	}
	
	public String outPutToString(){
		return name + ", " + points;
				 
	}

	public boolean equals(Team other)
	{
	    return name.equals(other.name);
	}
	
	
}
