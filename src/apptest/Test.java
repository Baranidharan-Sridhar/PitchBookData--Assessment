package apptest;

import java.util.ArrayList;

import app.App;
import junit.framework.TestCase;

public class Test extends TestCase  {
	
	public void setUp(){
		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * test cases to validate the getHREF function
	 * 
	 */
	
	public void testHref(){
		App app = new App();
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("<link rel='dns-prefetch' href='//maxcdn.bootstrapcdn.com' />");
		testList.add("<link rel='stylesheet' id='c16ss-style-css'  href='https://www.mkyong.com/wp-content/plugins/c16-social-share/css/c16ss.css?ver=4.6.2' type='text/css' media='all' />");
		testList.add("<a title=\"Android\" href=\"http://www.mkyong.com/tutorials/android-tutorial/\">Android</a></li><li id=\"menu-item-11047\" class=\"menu-item menu-item-type-custom menu-item-object-custom menu-item-has-children menu-item-11047 dropdown\">");
		testList.add("<link rel='dns-prefetch' href='//maxcdn.bootstrapcdn.com' />");
		ArrayList<String> expectedOutputList = new ArrayList<String>();
		expectedOutputList.add( "'//maxcdn.bootstrapcdn.com'");
		expectedOutputList.add("'https://www.mkyong.com/wp-content/plugins/c16-social-share/css/c16ss.css?ver=4.6.2'");
		expectedOutputList.add("\"http://www.mkyong.com/tutorials/android-tutorial/\"");
		expectedOutputList.add("'//maxcdn.bootstrapcdn.com'");
						
		ArrayList<String> result= app.getHref(testList);
		for(int i=0;i<result.size();i++){
			assertEquals(expectedOutputList.get(i), result.get(i));
		}
		
	}

	/*
	 * test cases to validate the getTagList function
	 * 
	 */
	
	public void testTags(){
		App app = new App();
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("<input value=’>’>");
		testList.add("<a href=\"/\" class=\"navbar-brand\">");
		testList.add("<input value=\" id='test'>");
		testList.add("<input value=\"\" id='test'>");
		ArrayList<String> expectedOutputList = new ArrayList<String>();
		expectedOutputList.add("<input>");
		expectedOutputList.add("<a>");
		expectedOutputList.add("<input>");
		expectedOutputList.add("<input>");
						
		ArrayList<String> result= app.getTagList(testList);
		for(int i=0;i<result.size();i++){
			assertEquals(expectedOutputList.get(i), result.get(i));
		}
	}
	
	
	/*
	 * test cases to validate the getSequences function
	 * 
	 */
	
	
	public void testSequences(){
		App app = new App();
		String test1= " <p> !Mkyong.com is created, written by, and maintained by Yong Mook Kim, aka Mkyong. It is built on <a href=\"https://wordpress.org/\">";
		String test2= "<p> My 8 Kong </p>";
		ArrayList<String> expectedRes1= new ArrayList<String>();
		expectedRes1.add("Yong Mook Kim");
		expectedRes1.add("Mkyong It");
		ArrayList<String> result1= app.getSequences(test1);
		ArrayList<String> result2= app.getSequences(test2);
				
		for(int i=0;i<result1.size();i++){
			assertEquals(result1.get(i), expectedRes1.get(i));
		}
		for(int i=0;i<result2.size();i++){
			assertEquals(result2.get(i), "");
		}
	}
	
	public void tearDown(){
		try {
			super.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
