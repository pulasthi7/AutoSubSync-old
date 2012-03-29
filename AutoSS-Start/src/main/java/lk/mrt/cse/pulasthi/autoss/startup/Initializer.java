package lk.mrt.cse.pulasthi.autoss.startup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * This Class extract the words from the subtitle file to generate vocabulary model
 * @author Pulasthi Mahawithana <pulasthi7@gmail.com>
 *
 */
public class Initializer {

	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("Usage error: Use AutoSS-start <srt file path>");
		}
		Initializer initializer = new Initializer();
		initializer.start(args[0]);
	}
	
	/**
	 * Extract the words from the subtitle file to generate vocabulary file.
	 * @param subFile The URI of the subtitle file
	 */
	public void start(String subFile) {	
		try {
			File srtFile = new File(new URI(subFile));	
			Scanner sc = new Scanner(srtFile);
			File freqFile = new File("freq.txt");
			System.out.println(freqFile.getPath());
			freqFile.createNewFile();
			PrintWriter pw = new PrintWriter(freqFile);
			System.out.println("Writing To File: "+freqFile.getAbsolutePath());
			System.out.println("Using sub File:"+srtFile.getAbsolutePath());
			while (sc.hasNextLine()) {
				StringBuilder textChunk = new StringBuilder();
				String lineRead = sc.nextLine();	//ignored : the incrementing identifier
				lineRead = sc.nextLine();	//ignored : the timestamp
				while(!(lineRead=sc.nextLine()).equals("")){
					textChunk.append(lineRead);
				}
				//remove the unnecessary punctuation characters
				String[] words = textChunk.toString().
						replaceAll("[#$%&()*+\\-,./:;<=>?@[\\\\]]", " ").
						replaceAll("\\s+", " ").
						split("\\s+");
				for (String word : words) {
					pw.println(word);
				}
			}
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(".srt file specified not found");			
		} catch (IOException e) {
			System.out.println("Can't write to a temparory file");
		} catch (URISyntaxException e) {
			System.out.println("Malformed URI");
		} 
	}

}
