package br.com.db1.bol.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.db1.bol.IPasswordBOL;
import br.com.db1.enums.MyPatternsEnum;
import br.com.db1.model.PasswordAdditionsModel;
import br.com.db1.model.PasswordDeductionsModel;
import br.com.db1.model.PasswordModel;
import br.com.db1.util.Constantes;

/**
 * 
 * Faz validações de senha
 * 
 * @author Victor Gil Coronel
 *
 */
public class PasswordBOL implements IPasswordBOL {

	private boolean passMinimumCharsLength = false;

	@Override
	public PasswordModel validate(String password) {
		if (password == null) {
			return null;
		}

		PasswordModel model = new PasswordModel();
		PasswordAdditionsModel additions = getAdditions(password);
		PasswordDeductionsModel deductions = getDeductions(password, additions);

		model.setStrength(sumAdditionsBonus(additions) - sumDeductionsBonus(deductions));

		// Acima de 100, é 100
		if (model.getStrength() > Constantes.MAX_STRENGTH) {
			model.setStrength(Constantes.MAX_STRENGTH);
		}

		return model;
	}

	private int getOccurrences(String textToFind, String currentPattern) {
		return getOccurrences(textToFind, currentPattern, null);
	}

	private int getOccurrences(String textToFind, String currentPattern, Integer flag) {
		Matcher m;
		if (flag != null) {
			m = Pattern.compile(currentPattern, flag).matcher(textToFind);
		} else {
			m = Pattern.compile(currentPattern).matcher(textToFind);
		}
		int count = 0;
		while (m.find()) {
			count++;
		}

		return count;
	}

	private int getOccurrencesConsecutive(String textToFind, String currentPattern) {
		int toReturn = 0;
		Matcher m;
		m = Pattern.compile(currentPattern).matcher(textToFind);
		int count = 0;
		while (m.find()) {
			toReturn += m.group(count).length() - 1;
			count++;

		}

		return toReturn;
	}

	/**
	 * Adições a força da senha
	 * 
	 * @param password
	 * @return pontos bons da senha
	 */
	private PasswordAdditionsModel getAdditions(String password) {
		PasswordAdditionsModel additions = new PasswordAdditionsModel();

		// Total length
		additions.setTotalLength(password.length());
		// Bonus
		additions.setTotalLengthBonus(getNumberCharactersBonus(additions.getTotalLength()));

		// Upper
		additions.setTotalUpper(getOccurrences(password, MyPatternsEnum.LETTER_UPPER.getRegexToFind()));
		// Bonus
		additions.setTotalUpperBonus(getUppercaseLettersBonus(additions.getTotalUpper(), additions.getTotalLength()));

		// Lower
		additions.setTotalLower(getOccurrences(password, MyPatternsEnum.LETTER_LOWER.getRegexToFind()));
		// Bonus
		additions.setTotalLowerBonus(getLowerCaseLettersBonus(additions.getTotalLower(), additions.getTotalLength()));

		// Numbers
		additions.setTotalNumber(getOccurrences(password, MyPatternsEnum.NUMBER.getRegexToFind()));
		// Bonus
		additions.setTotalNumberBonus(getNumberCharactersBonus(additions.getTotalNumber()));

		// Symbols
		additions.setTotalSymbols(getOccurrences(password, MyPatternsEnum.SYMBOL.getRegexToFind()));
		// Bonus
		additions.setTotalSymbolsBonus(getSymbolBonus(additions.getTotalSymbols()));

		// Numbers and Symbols in middle
		additions.setTotalNumberSymbolsMiddle(getOccurrences(password.substring(1, password.length() - 1),
				MyPatternsEnum.NUMBERS_SYMBOLS_MIDDLE.getRegexToFind()));
		// Bonus
		additions.setTotalNumberSymbolsMiddleBonus(
				getMiddleNumbersOrSymbolsBonus(additions.getTotalNumberSymbolsMiddle()));

		// Total requirements
		additions.setTotalRequirements(getTotalRequirements(password, additions.getTotalUpper(),
				additions.getTotalLower(), additions.getTotalNumber(), additions.getTotalSymbols()));

		additions.setTotalRequirementsBonus(
				getTotalRequirementsBonus(additions.getTotalLength(), additions.getTotalRequirements()));

		return additions;
	}

	/**
	 * Reduções a força da senha
	 * 
	 * @param password
	 * @param additions
	 * @return pontos ruins da senha
	 */
	private PasswordDeductionsModel getDeductions(String password, PasswordAdditionsModel additions) {
		PasswordDeductionsModel deductions = new PasswordDeductionsModel();
		// Letters only
		if (additions.getTotalLength() == additions.getTotalUpper() + additions.getTotalLower()) {
			deductions.setTotalLettersOnly(additions.getTotalLength());
		}

		// Numbers only
		if (additions.getTotalLength() == additions.getTotalNumber()) {
			deductions.setTotalNumbersOnly(additions.getTotalLength());
		}

		// Repeat Characters
		deductions.setTotalRepeated(
				getOccurrences(password, MyPatternsEnum.REPEAT_CHARACTERS.getRegexToFind(), Pattern.CASE_INSENSITIVE));

		// Sequential Upper
		deductions.setTotalSequentialUpper(
				getOccurrencesConsecutive(password, MyPatternsEnum.SEQUENTIAL_UPPER.getRegexToFind()));
		// bonus
		deductions.setTotalSequentialUpperBonus(getDeductionBonusConsecutives(deductions.getTotalSequentialUpper()));

		// Sequential Lower
		deductions.setTotalSequentialLower(
				getOccurrencesConsecutive(password, MyPatternsEnum.SEQUENTIAL_LOWER.getRegexToFind()));
		// bonus
		deductions.setTotalSequentialLowerBonus(getDeductionBonusConsecutives(deductions.getTotalSequentialLower()));

		// Sequential Number
		deductions.setTotalSequentialNumber(
				getOccurrencesConsecutive(password, MyPatternsEnum.SEQUENTIAL_NUMBER.getRegexToFind()));
		// bonus
		deductions.setTotalSequentialNumberBonus(getDeductionBonusConsecutives(deductions.getTotalSequentialNumber()));

		// Sequential Letters (3+)
		deductions.setTotalSequentialLetterMoreThree(
				getMoreThanThreeRepetitions(password.toLowerCase(), MyPatternsEnum.LETTER_LOWER));
		// bonus
		deductions.setTotalSequentialLetterMoreThreeBonus(
				getDeductionBonusMoreThanThree(deductions.getTotalSequentialLetterMoreThree()));

		// Sequential Numbers (3+)
		deductions.setTotalSequentialNumberMoreThree(getMoreThanThreeRepetitions(password, MyPatternsEnum.NUMBER));
		// bonus
		deductions.setTotalSequentialNumberMoreThreeBonus(
				getDeductionBonusMoreThanThree(deductions.getTotalSequentialNumberMoreThree()));

		// Sequential Symbols (3+)
		deductions.setTotalSequentialSymbolsMoreThree(getMoreThanThreeRepetitions(password, MyPatternsEnum.SYMBOL));
		// bonus
		deductions.setTotalSequentialSymbolsMoreThreeBonus(
				getDeductionBonusMoreThanThree(deductions.getTotalSequentialSymbolsMoreThree()));

		return deductions;
	}

	/**
	 * Soma as adições
	 * 
	 * @param additions
	 * @return
	 */
	private int sumAdditionsBonus(PasswordAdditionsModel additions) {
		return

		additions.getTotalLengthBonus() + additions.getTotalLowerBonus() + additions.getTotalNumberBonus()
				+ additions.getTotalNumberSymbolsMiddleBonus() + additions.getTotalRequirementsBonus()
				+ additions.getTotalSymbolsBonus() + additions.getTotalUpperBonus();
	}

	/**
	 * Soma as deduções
	 * 
	 * @param deductions
	 * @return
	 */
	private int sumDeductionsBonus(PasswordDeductionsModel deductions) {
		return deductions.getTotalLettersBonus() + deductions.getTotalNumbersBonus()
				+ deductions.getTotalRepeatedBonus() + deductions.getTotalSequentialLetterMoreThreeBonus()
				+ deductions.getTotalSequentialLowerBonus() + deductions.getTotalSequentialNumberBonus()
				+ deductions.getTotalSequentialNumberMoreThreeBonus()
				+ deductions.getTotalSequentialSymbolsMoreThreeBonus() + deductions.getTotalSequentialUpperBonus();
	}

	private int getMoreThanThreeRepetitions(String password, MyPatternsEnum type) {
		int totalRepetition = 0;
		int currentNumberRepetition = 1;
		char last = 0;
		for (int i = 0; i < password.length(); i++) {
			char current = password.charAt(i);

			// Character.isDigit(current)
			int result = getOccurrences(current + "", type.getRegexToFind(), null);

			if (result > 0 && last + 1 == current) {
				currentNumberRepetition++;
				if (currentNumberRepetition >= 3) {
					totalRepetition++;
				}
			} else {
				currentNumberRepetition = 1;
			}

			last = current;
		}

		return totalRepetition;
	}

	private int getTotalRequirements(String password, int totalUpper, int totalLower, int totalNumber,
			int totalSymbols) {
		passMinimumCharsLength = false;
		int totalRequirements = 0;
		if (totalUpper > 0) {
			totalRequirements++;
		}

		if (totalLower > 0) {
			totalRequirements++;
		}

		if (totalNumber > 0) {
			totalRequirements++;
		}

		if (totalSymbols > 0) {
			totalRequirements++;
		}

		if (password.length() >= Constantes.MINIMUM_QUANTITY_CHARACTERS) {
			passMinimumCharsLength = true;
			totalRequirements++;
		}

		return totalRequirements;
	}

	private int getNumberCharactersBonus(int numberOfChars) {
		int total = 0;
		if (numberOfChars > 0) {
			total = (numberOfChars * 4);
		}
		return total;
	}

	private int getUppercaseLettersBonus(int numberOfChars, int totalLength) {
		int total = 0;
		if (numberOfChars > 0) {
			total = ((totalLength - numberOfChars) * 2);
		}
		return total;
	}

	private int getLowerCaseLettersBonus(int numberOfChars, int totalLength) {
		int total = 0;
		if (numberOfChars > 0) {
			total = ((totalLength - numberOfChars) * 2);
		}
		return total;
	}

	private int getSymbolBonus(int numberOfChars) {
		int total = 0;
		if (numberOfChars > 0) {
			total = (numberOfChars * 6);
		}
		return total;
	}

	private int getMiddleNumbersOrSymbolsBonus(int numberOfChars) {
		int total = 0;
		if (numberOfChars > 0) {
			total = (numberOfChars * 2);
		}
		return total;
	}

	private int getTotalRequirementsBonus(int numberOfChars, int totalRequirements) {
		int total = 0;
		if (passMinimumCharsLength && totalRequirements >= Constantes.MINIMUM_REQUIREMENTS_ITEMS) {
			total = (numberOfChars * 2);
		}
		return total;
	}

	private int getDeductionBonusConsecutives(int total) {
		return total * 2;
	}

	private int getDeductionBonusMoreThanThree(int total) {
		return total * 3;
	}
}
