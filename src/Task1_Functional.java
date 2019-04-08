import static org.junit.Assert.*;

import org.junit.Test;

import st.Parser;

import org.junit.Before;
import static org.junit.Assert.assertEquals;

public class Task1_Functional {

	private Parser parser;
	
	@Before
	public void set_up() {
		parser = new Parser();
	}
	
	//--------------------------ADD--------------------------//
	//----------OVERRIDING----------//
	//Test for overriding a previously defined option
	@Test
	public void override() {
		parser.add("override", Parser.STRING);
		parser.parse("--override=NotOverriden");
		parser.add("override", Parser.STRING);
		assertNotEquals(parser.getString("override"), "NotOverriden");
	}
	
	//----------NUMBERS----------//
	//Test for invalid options starting with numbers
	@Test
	public void numberStart() {
		try {
			parser.add("1numberStart", Parser.BOOLEAN);
			parser.parse("--1numberStart");
			//Test should never reach this assert, if it did
			//then it would fail as the boolean would be True
			assertFalse(parser.getBoolean("1numberStart"));
		} catch (Exception e) {
			//Check whether this option is set as default
			assertFalse(parser.getBoolean("1numberStart"));
		}
	}
	
	//Test for one number inside option name being valid
	@Test
	public void oneNumber() {
		parser.add("one2Number", Parser.BOOLEAN);
		parser.parse("--one2Number");
		assertTrue(parser.getBoolean("one2Number"));
	}
	
	//Test for many numbers inside option name being valid
	@Test
	public void manyNumbers() {
		parser.add("one23456Numbers", Parser.BOOLEAN);
		parser.parse("--one23456Numbers");
		assertTrue(parser.getBoolean("one23456Numbers"));
	}
	
	//Test for no numbers inside option name being valid
	@Test
	public void noNumbers() {
		parser.add("noNumbers", Parser.BOOLEAN);
		parser.parse("--noNumbers");
		assertTrue(parser.getBoolean("noNumbers"));
	}
	
	//----------UNDERSCORES----------//
	//Test for valid option beginning with underscore
	@Test
	public void underscoreStart() {
		parser.add("_underscoreStart", Parser.BOOLEAN);
		parser.parse("--_underscoreStart");
		assertTrue(parser.getBoolean("_underscoreStart"));
	}
	
	//Test for option name with an underscore within name
	@Test
	public void oneUnderscore() {
		parser.add("one_underscore", Parser.BOOLEAN);
		parser.parse("--one_underscore");
		assertTrue(parser.getBoolean("one_underscore"));
	}
	
	//Test for many underscores within name
	@Test
	public void manyUnderscores() {
		parser.add("many__under_scores_", Parser.BOOLEAN);
		parser.parse("--many__under_scores_");
		assertTrue(parser.getBoolean("many__under_scores_"));
	}
	
	//----------NON-ALPHANUMERIC----------//
	//Test for invalid option name with non-alphanumeric characters
	@Test
	public void nonAlphanumeric() {
		try {
			parser.add("inv@lid", Parser.BOOLEAN);
			parser.parse("--inv@lid");
			//Test should not reach this line, if it does
			//then it should fail as option will be true
			assertFalse(parser.getBoolean("inv@lid"));
		} catch(Exception e) {
			assertFalse(parser.getBoolean("inv@lid"));
		}
	}
	
	//----------CASE----------//
	//Test for case-sensitivity
	@Test
	public void caseSensitive() {
		parser.add("sensitive", Parser.STRING);
		parser.add("SensitivE", Parser.STRING);
		parser.parse("--sensitive=all_lowercase");
		parser.parse("--SensitivE=SomeCapitals");
		assertEquals(parser.getString("sensitive"), "all_lowercase");
		assertEquals(parser.getString("SensitivE"), "SomeCapitals");
	}

	//----------FULL&SHORT----------//
	//Test for assigning values to shortcuts and full-names
	@Test
	public void assignFullandShort() {
		parser.add("assign", Parser.STRING);
		parser.add("assignLong", "assign", Parser.STRING);
		parser.parse("--assign=Fullname");
		parser.parse("-assign=Shortcut");
		assertEquals(parser.getString("assign"), "Fullname");
		assertEquals(parser.getString("assignLong"), "Shortcut");
	}
	
	//----------BOOLEANS----------//
	//Test for checking option true
	@Test
	public void booleanTrue() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=true");
		assertTrue(parser.getBoolean("bool"));
	}
	
	//Test for leaving boolean value empty, giving false
	@Test
	public void booleanEmpty() {
		parser.add("bool", Parser.BOOLEAN);
		assertFalse(parser.getBoolean("bool"));
	}
	
	//Test for true value with inputting 1
	@Test
	public void booleanAny1() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=1");
		assertTrue(parser.getBoolean("bool"));
	}
	
	//Another test for true value with inputting any number
	@Test
	public void booleanAny2() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=-1945632646");
		assertTrue(parser.getBoolean("bool"));
	}
	
	//Test for true value with inputting any string
	@Test
	public void booleanAny3() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=setToTruePlease");
		assertTrue(parser.getBoolean("bool"));
	}
	
	//Test for true when not assigning a value but declaring option
	@Test
	public void booleanDeclare() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool");
		assertTrue(parser.getBoolean("bool"));
	}
	
	//Same test but declaring the shortcut
	@Test
	public void booleanDeclareShort() {
		parser.add("bool", "b", Parser.BOOLEAN);
		parser.parse("-b");
		assertTrue(parser.getBoolean("b"));
	}
	
	//Test for false when declaring option to be false
	@Test
	public void booleanFalse() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=false");
		assertFalse(parser.getBoolean("bool"));
	}
	
	//Same test but False has capital at start
	@Test
	public void booleanFalseCase() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=False");
		assertFalse(parser.getBoolean("bool"));
	}
	
	//Same test but arbitrary capitals
	@Test
	public void booleanFalseCase2() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=FaLsE");
		assertFalse(parser.getBoolean("bool"));
	}
	
	//Test for false when declaring option  to be zero
	@Test
	public void booleanFalseZero() {
		parser.add("bool", Parser.BOOLEAN);
		parser.parse("--bool=0");
		assertFalse(parser.getBoolean("bool"));
	}

	
	
	//--------------------------PARSE--------------------------//
	//----------PREFIXES----------//
	//Test for using -- for full-name
	@Test
	public void fullnamePrefix() {
		parser.add("option", Parser.BOOLEAN);
		parser.add("fullOption", "option", Parser.BOOLEAN);
		parser.parse("--option");
		assertTrue(parser.getBoolean("option"));
		assertFalse(parser.getBoolean("fullOption"));
	}
	
	//Test for using - for shortcut
	@Test
	public void shortcutPrefix() {
		parser.add("option", Parser.BOOLEAN);
		parser.add("fullOption", "option", Parser.BOOLEAN);
		parser.parse("-option");
		assertTrue(parser.getBoolean("fullOption"));
		assertFalse(parser.getBoolean("option"));
	}
	
	//----------EQUALS----------//
	//Test for using equals to assign values
	@Test
	public void useEquals() {
		parser.add("equals", Parser.STRING);
		parser.parse("--equals=Correct");
		assertEquals(parser.getString("equals"), "Correct");
	}
	
	//Test for not using equals to assign values
	@Test
	public void notUseEquals() {
		parser.add("noEquals", Parser.STRING);
		parser.parse("--noEquals Correct");
		assertEquals(parser.getString("noEquals"), "Correct");
	}
	
	//----------QUOTES----------//
	//Test for using single quotes to declare values
	@Test
	public void singleQuotesDeclare() {
		parser.add("single", Parser.STRING);
		parser.parse("--single='Correct'");
		assertEquals(parser.getString("single"), "Correct");
	}
	
	//Test for using double quotes to declare values
	@Test
	public void doubleQuotesDeclare() {
		parser.add("double", Parser.STRING);
		parser.parse("--double=\"Correct\"");
		assertEquals(parser.getString("double"), "Correct");
	}
	
	//Test for not using single or double quotes to declare values
	@Test
	public void noQuotes() {
		parser.add("none", Parser.STRING);
		parser.parse("--none=Correct");
		assertEquals(parser.getString("none"), "Correct");
	}
	
	//----------QUOTES-DECORATING----------//
	//Test for one single decorating quote
	@Test
	public void oneSingleDecoration() {
		parser.add("Quotes", Parser.STRING);
		parser.parse("--Quotes=\"Yee'Haw\"");
		assertEquals(parser.getString("Quotes"), "Yee'Haw");
	}
	
	//Test for many single decorating quotes
	@Test
	public void manySingleDecoration() {
		parser.add("Quotes", Parser.STRING);
		parser.parse("--Quotes=\"Yee'Haw, Matey'\"");
		assertEquals(parser.getString("Quotes"), "Yee'Haw, Matey'");
	}
	
	//Test for one double decorating quote
	@Test
	public void oneDoubleDecoration() {
		parser.add("Quotes", Parser.STRING);
		parser.parse("--Quotes='Howdy\"'");
		assertEquals(parser.getString("Quotes"), "Howdy\"");
	}
	
	//Test for many double decorating quotes
	@Test
	public void manyDoubleDecoration() {
		parser.add("Quotes", Parser.STRING);
		parser.parse("--Quotes='\"Howdy\"'");
		assertEquals(parser.getString("Quotes"), "\"Howdy\"");
	}
	
	//----------MULTIPLE-ASSIGNMENTS----------//
	//Test for verifying option has latest value
	@Test
	public void latestValue() {
		parser.add("latest", Parser.BOOLEAN);
		parser.parse("--latest");
		parser.parse("--latest=0");
		assertFalse(parser.getBoolean("latest"));
	}
	
	//----------MULTIPLE-USES----------//
	//Test for multiple declarations on one parse
	@Test
	public void multipleOne() {
		parser.add("first_option", Parser.STRING);
		parser.add("second_option", Parser.STRING);
		parser.add("third_option", Parser.STRING);
		parser.parse("--first_option=first --second_option=second");
		assertEquals(parser.getString("first_option"), "first");
		assertEquals(parser.getString("second_option"), "second");
		assertEquals(parser.getString("third_option"), "");
	}
	
	//Test for multiple declarations on multiple parses
	@Test
	public void multipleTwo() {
		parser.add("first_option", Parser.STRING);
		parser.add("second_option", Parser.STRING);
		parser.add("third_option", Parser.STRING);
		parser.parse("--first_option=first");
		parser.parse("--second_option=second");
		assertEquals(parser.getString("first_option"), "first");
		assertEquals(parser.getString("second_option"), "second");
		assertEquals(parser.getString("third_option"), "");
	}

	
	
	//--------------------------GET--------------------------//
	//----------DEFAULT----------//
	//Test default values for get, and also if not declaring
	//an option name causes it to default to a value
	
	//Get with full-name
	@Test
	public void getIntegerDefault() {
		parser.add("intDefault", Parser.INTEGER);
		assertEquals(parser.getInteger("intDefault"), 0);
	}
	
	@Test
	public void getStringDefault() {
		parser.add("stringDefault", Parser.STRING);
		assertEquals(parser.getString("stringDefault"), "");
	}
	
	@Test
	public void getBooleanDefault() {
		parser.add("boolDefault", Parser.BOOLEAN);
		assertEquals(parser.getBoolean("boolDefault"), false);
	}
	
	@Test
	public void getCharacterDefault() {
		parser.add("characterDefault", Parser.CHAR);
		assertEquals(parser.getChar("characterDefault"), '\0');
	}
	
	//Get with shortcut
	@Test
	public void getShortIntegerDefault() {
		parser.add("intDefault", "intD", Parser.INTEGER);
		assertEquals(parser.getInteger("intD"), 0);
	}
	
	@Test
	public void getShortStringDefault() {
		parser.add("stringDefault", "stringD", Parser.STRING);
		assertEquals(parser.getString("stringD"), "");
	}
	
	@Test
	public void getShortBooleanDefault() {
		parser.add("boolDefault", "boolD", Parser.BOOLEAN);
		assertEquals(parser.getBoolean("boolD"), false);
	}
	
	@Test
	public void getShortCharacterDefault() {
		parser.add("characterDefault", "characterD", Parser.CHAR);
		assertEquals(parser.getChar("characterD"), '\0');
	}
	
	
	//----------SAME-NAME----------//
	//Test to get full-name option instead of 
	//shortcut with same name
	@Test
	public void shortSameAsFull() {
		parser.add("option", Parser.INTEGER);
		parser.add("longOption", "option", Parser.INTEGER);
		parser.parse("--option=100");
		parser.parse("-option=1");
		assertEquals(parser.getInteger("option"), 100);
	}
	
}
