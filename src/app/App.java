package app;
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
	public static void main(String[] args) throws IOException {
				
		URL url;
		String inputUrl="";
		BufferedWriter writer = null;
		ArrayList<String> tags= new ArrayList<String>();
		ArrayList<String> tagList= new ArrayList<String>();
		ArrayList<String> hrefList=  new ArrayList<String>();
		ArrayList<String> sequenceTextResult =  new ArrayList<String>();
		String outputFile ="";
		/*
		 * Checks if the command line arguments comply with the description.
		 * 
		 * 
		 */
		if(args.length!=2){
			System.out.println("Insufficient Arguments! ");
			
		}
		
		else if(!args[0].contains("://")){
			System.out.println("innn");
			inputUrl= "http://"+args[0];
		}
		
		else if(args[0].split(":")[0].equals("http")|| args[0].split(":")[0].equals("http")){
			inputUrl= args[0];
			System.out.println(" sss");
		}
		
		else if(inputUrl==""){
			System.out.println(args[0].split(":")[0]);
			System.out.println("Error Input File! Input URL should be HTTP or HTTPS ");
		}
		
		try {
			outputFile=args[1];
			System.out.println(inputUrl);
			url = new URL(inputUrl);
			/*
			 * Reading the HTML content from the given URL as a single String
			 * 
			 * 
			 * 
			 */
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			StringBuilder htmlStringBuilder = new StringBuilder();
			String inputString = new String();
			while ((inputString = br.readLine()) != null) {
				htmlStringBuilder.append(inputString);
			}
			App app = new App();
			String htmlString = htmlStringBuilder.toString();
			tags= app.getHTMLTags(htmlString);
			tagList= app.getTagList(tags);
			hrefList= app.getHref(tags);
			sequenceTextResult = app.getSequences(htmlString);
			
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			/*
			 * 
			 * writing it to the output file output.txt using BufferedWriter
			 * 
			 * 
			 * 
			 */
		try{
		    writer = new BufferedWriter(new FileWriter(outputFile));
		    
		    writer.write("[LINKS]");
		    writer.newLine();writer.newLine();
		    for(String s: hrefList){
		    	writer.write(s);
		    	writer.newLine();
		    }
		    writer.newLine();
		    writer.write("[HTML]");
		    writer.newLine();writer.newLine();
		    
		    for(String s: tagList)
				writer.write(s);
		    writer.newLine();writer.newLine();
		    
		    writer.write("[SEQUENCE]");
		    writer.newLine();
		    writer.newLine();
			for(String str: sequenceTextResult){
				writer.write(str);
				writer.newLine();
			}
		} 
		catch (IOException e) {
		   e.printStackTrace();
		} 
		finally{
			writer.close();
		}

	}
	/*
	 * 
	 * getSequences function takes in HTML content and returns all  
	 * two or more words that have the first letter in each word is capitalized
	 * sequence of characters of length greater than two separated by whitespace 
	 * 
	 * 
	 */

	public ArrayList<String> getSequences(String inputLine) {
		ArrayList<String> sequenceTextResult = new ArrayList<String>();
		String noHtml= inputLine.replaceAll("</", " nbsp; </ ").replaceAll("\\<.*?>","");
		String[] sequence= noHtml.split(" ");
		int noOfConsecutiveUpperCaseStrings=0;
		ArrayList<String> sequenceText = new ArrayList<String>();
				
		for(int i=0;i<sequence.length;i++){
			String string= sequence[i];
			if(string.length()>0){
				 if(!Character.isUpperCase(string.charAt(0)) && noOfConsecutiveUpperCaseStrings==1 ){
					 sequenceText.remove(sequenceText.size()-1);
					 noOfConsecutiveUpperCaseStrings=0;
				 }
				 else if(Character.isUpperCase(string.charAt(0))){
					sequenceText.add(string+ " ");
					noOfConsecutiveUpperCaseStrings++;
				 }
				 else if(!Character.isUpperCase(string.charAt(0))&& noOfConsecutiveUpperCaseStrings>1){
					 noOfConsecutiveUpperCaseStrings=0;
					 StringBuilder sbuilder = new StringBuilder();
					for(String str: sequenceText){
						sbuilder.append(str);
					}
					sequenceTextResult.add(sbuilder.toString().trim().replaceAll("[^\\dA-Za-z ]", ""));
					sequenceText.clear();
				 }
			}
		}
		
		return sequenceTextResult;
		
	}
		/*
		 * function all HTML tags from the input HTML content
		 * which is used by both getHTMLTags and getHref fucntions
		 * 
		 */
	

	public ArrayList<String> getHTMLTags(String inputLine) {
		ArrayList<String> tags = new ArrayList<String>(); 
		
		Pattern pattern;
		Matcher matcher;
		String htmlPattern = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
		pattern = Pattern.compile(htmlPattern);
		matcher = pattern.matcher(inputLine);
		while (matcher.find()) {
			String tag= matcher.group(0);
			tags.add(tag);
		}
			return tags;
		}
		
		/*
		 * 
		 * function that returns all HTML tags
		 * 
		 */
	public ArrayList<String> getTagList(ArrayList<String> tags){
		ArrayList<String> tagList = new ArrayList<String>();
		
		for(String tag: tags){
			String tagSplit[] = tag.split(" ");
			if(tagSplit.length==1){
				tagList.add(tag);
			}
			else{
				String tagValidator= tagSplit[0];
				if(tagValidator.matches("<(\"[^\"]*\"|'[^']*'|[^'\">])*>"));
					tagList.add(tagValidator+">");
			}
		}
		return tagList;
	}
	/*
	 * 
	 * return all links in the HTML page
	 */

	public ArrayList<String> getHref(ArrayList<String> tags) {
		ArrayList<String> links = new ArrayList<String>();
		for(String tag: tags){
			if(tag.split(" ").length>1){
				Pattern pattern;
				Matcher matcher;
				String htmlPattern ="\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
				pattern = Pattern.compile(htmlPattern);
				matcher = pattern.matcher(tag);
		
				while (matcher.find()) {
					String link= matcher.group(0);
					String hrefLink= link.split("href\\s*=")[1].trim(); 
					links.add(hrefLink);
				}
			}
		}
		return links;
	}
}