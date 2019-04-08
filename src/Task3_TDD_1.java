import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import st.Parser;

import org.junit.Before;

public class Task3_TDD_1 {

  private Parser parser;
  
  @Before
  public void set_up() {
    parser = new Parser();
  }
  
  //Test parser is not null
  @Test
  public void notNull() {
    assertNotNull(parser);
  }

  //Test to return fullname and not shortcut if both have same name
  @Test
  public void searchOrder() {
    parser.add("l", Parser.STRING);
    parser.parse("--l=5,6,7");
    parser.add("list", "l", Parser.STRING);
    parser.parse("-l=1,2,3,4,8");
    assertEquals(Arrays.asList(5,6,7), parser.getIntegerList("l"));
  }
  
  //Test to return empty list if value not assigned to parser
  @Test
  public void noValue() {
    parser.add("empty", Parser.STRING);
    assertEquals(Arrays.asList(), parser.getIntegerList("empty"));
  }
  
  //Test to parse spaces in String and only return list of ordered integers
  @Test
  public void spaceSeparate() {
    parser.add("space", Parser.STRING);
    parser.parse("--space=1,2 4");
    assertEquals(Arrays.asList(1,2,4), parser.getIntegerList("space"));
  }
  
  //Test to parse a point in string similarly to how comma is parsed and only return list of ordered integers
  @Test
  public void pointSeparate() {
    parser.add("point", Parser.STRING);
    parser.parse("--point=1,2.4");
    assertEquals(Arrays.asList(1,2,4), parser.getIntegerList("point"));
  }
  
  //Test to make sure other characters are filtered like commas and only return list of ordered integers
  @Test
  public void otherCharsSeparate() {
    parser.add("list", Parser.STRING);
    parser.parse("--list={}1<2>4({)");
    assertEquals(Arrays.asList(1,2,4), parser.getIntegerList("list"));
  }
  
  //Test to return ordered integer list when given positive hyphen range with other numbers
  @Test
  public void hyphenAscending() {
    parser.add("list", Parser.STRING);
    parser.parse("--list=1,2,4-7,8");
    assertEquals(Arrays.asList(1,2,4,5,6,7,8), parser.getIntegerList("list"));
  }
  
  //Test to return ordered integer list when given a decresing positive hyphen range with other numbers
  @Test
  public void hyphenDescending() {
    parser.add("list", Parser.STRING);
    parser.parse("--list=1,2,7-4,8");
    assertEquals(Arrays.asList(1,2,4,5,6,7,8), parser.getIntegerList("list"));
  }
  
  //Test to return ordered Unary String with range from negative to negative integers
  @Test
  public void unaryNegative() {
    parser.add("list", Parser.STRING);
    parser.parse("--list=-7--5");
    assertEquals(Arrays.asList(-7,-6,-5), parser.getIntegerList("list"));
  }
  
  //Test to return ordered Unary String with range from negative to positive integers
  @Test
  public void unaryNegativeToPositive() {
    parser.add("list", Parser.STRING);
    parser.parse("--list=-2-1");
    assertEquals(Arrays.asList(-2,-1,0,1), parser.getIntegerList("list"));
  }
  
  //Test to return empty list when hyphen is used as suffix in String
  @Test
  public void noSuffix() {
    parser.add("list", Parser.STRING);
    parser.parse("--list=1,2,3-");
    assertEquals(Arrays.asList(), parser.getIntegerList("list"));
  }
}
