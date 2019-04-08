package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Parser {
	public static final int INTEGER = 1;
	public static final int BOOLEAN = 2;
	public static final int STRING = 3;
	public static final int CHAR = 4;

	private OptionMap optionMap;

	public Parser() {
		optionMap = new OptionMap();
	}

	public void add(String option_name, String shortcut, int value_type) {
		optionMap.store(option_name, shortcut, value_type);
	}

	public void add(String option_name, int value_type) {
		optionMap.store(option_name, "", value_type);
	}

	public int getInteger(String option) {
		String value = getString(option);
		int type = getType(option);
		int result;
		switch (type) {
		case INTEGER:
			try {
				result = Integer.parseInt(value);
			} catch (Exception e) {
		        try {
		            new BigInteger(value);
		        } catch (Exception e1) {
		            result = 0;
		        }
		        result = 0;
		    }
			break;
		case BOOLEAN:
			if (getBoolean(option) == false) {
				result = 0;
			} else {
				result = 1;
			}
			break;
		case STRING:
		    int power = 1;
		    result = 0;
		    char c;
		    for (int i = value.length() - 1; i >= 0; --i){
		        c = value.charAt(i);
		        if (!Character.isDigit(c)) return 0;
		        result = result + power * (c - '0');
		        power *= 10;
		    }
		    break;
		case CHAR:
			result = (int)getChar(option);
			break;
		default:
			result = 0;
		}
		return result;
	}

	public boolean getBoolean(String option) {
		String value = getString(option);
		boolean result;
		if (value.toLowerCase().equals("false") || value.equals("0") || value.equals("")) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}

	public String getString(String option) {
		String result = optionMap.getValue(option);
		return result;
	}

	public char getChar(String option) {
		String value = getString(option);
		char result;
		if (value.equals("")) {
			result = '\0';
		} else {
			result = value.charAt(0);
		}
		return result;
	}

	 public List<Integer> getIntegerList(String option){
	    String value = getString(option);
	    //Returned ordered integer list
	    List<Integer> result = new ArrayList<Integer>();
	    //Empty list to return when option is invalid or not given a value
	    List<Integer> empty = new ArrayList<Integer>();

	    //Track previous integer for ranges
	    int previous = 0;
	    //Used to add integers in ranges
	    int front, back;
	    //Flag to check if there is a previous integer stored for ranges
	    boolean checker = false;

	    //Loop through all characters in String option
	    for (int i=0; i<value.length(); i++) {
	      //Get each character per iteration
	      char c = value.charAt(i);
	      //Check if character is hyphen
	      if (c == '-') {
	        //Return empty list if hyphen is last character in String option
	        if (!(i+1 < value.length())) {
	          return empty;
	        }

	        //TWO HYPHENS - CHECK IF NEXT IS +VE/-VE
	        //Check if next character in the String is a hyphen followed by a digit/integer
	        if ((i+2) < value.length() && value.charAt(i+1) == '-' && Character.isDigit(value.charAt(i+2))) {
	          //Check if there is a previous number stored
	          if (checker) {
	            //To store integer (could be more than a single digit) to parse
	            String number =  "";
	            //Get each digit from String option to add to String number to parse
	            char digit;
	            //Loop through all digits after hyphen
	            for (int j=i+2; j<value.length(); j++) {
	              //If it is a digit, add it to String number to parse as integer later
	              if (Character.isDigit(value.charAt(j))) {
	                digit = value.charAt(j);
	                number = number+digit;
	              }
	              //Else break out of loop if number ends (no more digits)
	              else {
	                checker = false;
	                break;
	              }
	            }
	            //Set front to previously stored integer and Set flag for previous number to false
	            front = previous;
	            checker = false;
	            //Parse String number to integer and since it is a hyphen after hyphen, it is negative
	            back = Integer.parseInt(number) * (-1);
	            //Set temporary integer to front to get range of numbers between front and back
	            //Add numbers in range to result integer array
	            int temp = front;
	            if (front >= back) {
	              temp -= 1;
	              while (temp >= back) {
	                result.add(temp);
	                temp--;
	              }
	            }
	            else {
	              temp += 1;
	              while (temp < back) {
	                result.add(temp);
	                temp++;
	              }
	            }
	          }
	        }

	        //NUMBER TO NUMBER
	        //Check if next character in the String is integer
	        else if (Character.isDigit(value.charAt(i+1)) && (i+1) < value.length()) {
	          //Check if there is a previous number stored
	          if (checker) {
	            //To store integer (could be more than a single digit) to parse
	            String number =  "";
	            //Get each digit from String option to add to String number to parse
              char digit;
              //Loop through all digits after hyphen
              for (int j=i+1; j<value.length(); j++) {
                //If it is a digit, add it to String number to parse as integer later
                if (Character.isDigit(value.charAt(j))) {
                  digit = value.charAt(j);
                  number = number+digit;
                }
                //Else break out of loop if number ends (no more digits)
                else {
                  checker = false;
                  break;
                }
              }
              //Set front to previously stored integer and Set flag for previous number to false
              front = previous;
              checker = false;
              //Parse String number to integer and since it is after hyphen
              back = Integer.parseInt(number);
              //Set temporary integer to front to get range of numbers between front and back
              //Add numbers in range to result integer array
              int temp = front;
              if (front >= back) {
                temp -= 1;
                while (temp >= back) {
                  result.add(temp);
                  temp--;
                }
              }
              else {
                temp += 1;
                while (temp <= back) {
                  result.add(temp);
                  temp++;
                }
              }
	          }
	          //Else NEGATIVE BIG NUMBER
	          else {
	            //NEGATIVE BIG NUMBER
	            //Get each digit from String option to add to String number to parse
	            String number =  "";
	            //Get each digit from String option to add to String number to parse
	            char digit;
	            //Loop through all digits after hyphen
	            for (int j=i+1; j<value.length(); j++) {
	              //If it is a digit, add it to String number to parse as integer later
	              if (Character.isDigit(value.charAt(j))) {
	                digit = value.charAt(j);
	                number = number+digit;
	              }
	              //If it is a hyphen then update flag for previous number (true)
	              else if (value.charAt(j) == '-') {
	                checker = true;
	                break;
	              }
	              //Else break out of loop if number ends (no more digits)
	              else {
	                checker = false;
	                break;
	              }
	            }
	            //Add integer to result (negative because digit is after hyphen) and store integer as previous value
	            result.add(Integer.parseInt(number) * (-1));
	            previous = Integer.parseInt(number) * (-1);
	          }
	        }
			    //Return empty list since
	        else {
            return empty;
	        }
	      }


	      //BIG NUMBER
	      //Check if character is a digit
	      else if (Character.isDigit(c)) {
	        //If there is a character before that in the String and if it not a hyphen and it is not a number
	        if ((i-1) > 0 && (value.charAt(i-1) != '-' && !Character.isDigit(value.charAt(i-1)))) {
	          //Get each digit from String option to add to String number to parse
	          String number =  "";
	          //Get each digit from String option to add to String number to parse
            char digit;
            //Loop through all digits
            for (int j=i; j<value.length(); j++) {
              //If it is a digit, add it to String number to parse as integer later
              if (Character.isDigit(value.charAt(j))) {
                digit = value.charAt(j);
                number = number+digit;
              }
              //If it is a hyphen then update flag for previous number (true)
              else if (value.charAt(j) == '-') {
                checker = true;
                break;
              }
              //Else break out of loop if number ends (no more digits)
              else {
                checker = false;
                break;
              }
            }
            //Add integer to result and store integer as previous value
            result.add(Integer.parseInt(number));
            previous = Integer.parseInt(number);
	        }

	        //If character is the first character in String
	        else if (i == 0) {
	          //Get each digit from String option to add to String number to parse
	          String number =  "";
	          //Get each digit from String option to add to String number to parse
            char digit;
            //Loop through all digits
            for (int j=i; j<value.length(); j++) {
              //If it is a digit, add it to String number to parse as integer later
              if (Character.isDigit(value.charAt(j))) {
                digit = value.charAt(j);
                number = number+digit;
              }
              //If it is a hyphen then update flag for previous number (true)
              else if (value.charAt(j) == '-') {
                checker = true;
                break;
              }
              //Else break out of loop if number ends (no more digits)
              else {
                checker = false;
                break;
              }
            }
            //Add integer to result and store integer as previous value
            result.add(Integer.parseInt(number));
            previous = Integer.parseInt(number);
	        }
	      }
	    }

	    //Sort integers in list in ascending order and return sorted list
	    Collections.sort(result);
	    return result;
	}

	public int parse(String command_line_options) {
		if (command_line_options == null) {
			return -1;
		}
		int length = command_line_options.length();
		if (length == 0) {
			return -2;
		}

		int char_index = 0;
		while (char_index < length) {
			char current_char = command_line_options.charAt(char_index);

			while (char_index < length) {
				current_char = command_line_options.charAt(char_index);
				if (current_char != ' ') {
					break;
				}
				char_index++;
			}

			boolean isShortcut = true;
			String option_name = "";
			String option_value = "";
			if (current_char == '-') {
				char_index++;
				current_char = command_line_options.charAt(char_index);
				if (current_char == '-') {
					isShortcut = false;
					char_index++;
				}
			} else {
				return -3;
			}
			current_char = command_line_options.charAt(char_index);

			while (char_index < length) {
				current_char = command_line_options.charAt(char_index);
				if (Character.isLetterOrDigit(current_char) || current_char == '_') {
					option_name += current_char;
					char_index++;
				} else {
					break;
				}
			}

			boolean hasSpace = false;
			if (current_char == ' ') {
				hasSpace = true;
				while (char_index < length) {
					current_char = command_line_options.charAt(char_index);
					if (current_char != ' ') {
						break;
					}
					char_index++;
				}
			}

			if (current_char == '=') {
				char_index++;
				current_char = command_line_options.charAt(char_index);
			}
			if ((current_char == '-'  && hasSpace==true ) || char_index == length) {
				if (getType(option_name) == BOOLEAN) {
					option_value = "true";
					if (isShortcut) {
						optionMap.setValueWithOptioShortcut(option_name, option_value);
					} else {
						optionMap.setValueWithOptionName(option_name, option_value);
					}
				} else {
					return -3;
				}
				continue;
			} else {
				char end_symbol = ' ';
				current_char = command_line_options.charAt(char_index);
				if (current_char == '\'' || current_char == '\"') {
					end_symbol = current_char;
					char_index++;
				}
				while (char_index < length) {
					current_char = command_line_options.charAt(char_index);
					if (current_char != end_symbol) {
						option_value = option_value + current_char;
						char_index++;
					} else {
						break;
					}
				}
			}

			if (isShortcut) {
				optionMap.setValueWithOptioShortcut(option_name, option_value);
			} else {
				optionMap.setValueWithOptionName(option_name, option_value);
			}
			char_index++;
		}
		return 0;
	}

	private int getType(String option) {
		int type = optionMap.getType(option);
		return type;
	}

	@Override
	public String toString() {
		return optionMap.toString();
	}

}
