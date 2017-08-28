package br.com.db1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordAdditionsModel {
	private int totalLength;
	private int totalLengthBonus;
		
	//Symbols
	private int totalSymbols;
	private int totalSymbolsBonus;
	
	//Upper
	private int totalUpper;
	private int totalUpperBonus;
	
	//Lower
	private int totalLower;
	private int totalLowerBonus;
	
	//Numbers
	private int totalNumber;
	private int totalNumberBonus;
	
	//Numbers and Symbols in middle
	private int totalNumberSymbolsMiddle;
	private int totalNumberSymbolsMiddleBonus;
	
	//Total requirements
	private int totalRequirements;
	private int totalRequirementsBonus;
}
