package br.com.db1.enums;

public enum MyPatternsEnum {
	NUMBER("\\d"), LETTER_LOWER("[a-z]"), LETTER_UPPER("[A-Z]"), SYMBOL("\\W"), NUMBERS_SYMBOLS_MIDDLE(
			"(\\W+?|\\d+?)"), REPEAT_CHARACTERS("(.)\\1{1,}"), SEQUENTIAL_UPPER(
					"([A-Z])\\1{1,}"), SEQUENTIAL_LOWER("([a-z])\\1{1,}"), SEQUENTIAL_NUMBER("\\d{2,}");

	private String regexToFind;

	MyPatternsEnum(String regexToFind) {
		this.regexToFind = regexToFind;
	}

	public String getRegexToFind() {
		return regexToFind;
	}
}
