package br.com.db1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDeductionsModel {
	//Letters only 
		private int totalLettersOnly = 0;
		private int totalLettersBonus = 0;
		
		//Numbers only
		private int totalNumbersOnly = 0;
		private int totalNumbersBonus = 0;
		
		//	Repeat Characters
		private int totalRepeated;
		private int totalRepeatedBonus;
		
		// Sequential Upper
		private int totalSequentialUpper;
		private int totalSequentialUpperBonus;
		
		// Sequential Lower
		private int totalSequentialLower;
		private int totalSequentialLowerBonus;
		
		// Sequential Number
		private int totalSequentialNumber;
		private int totalSequentialNumberBonus;
		
		//Sequential Letters (3+)
		private int totalSequentialLetterMoreThree;
		private int totalSequentialLetterMoreThreeBonus;
		
		//Sequential Numbers (3+)
		private int totalSequentialNumberMoreThree;
		private int totalSequentialNumberMoreThreeBonus;
		
		//Sequential Symbols (3+)
		private int totalSequentialSymbolsMoreThree;
		private int totalSequentialSymbolsMoreThreeBonus;
}
