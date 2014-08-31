package preyreaderwriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PreyTimeReaderWriter {
	
	
	public static void main(String [] args){
		PreyTimeReaderWriter readerWriter = new PreyTimeReaderWriter();
		
		try {
			readerWriter.writeAndRead();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public void writeAndRead() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("preytimes.txt"));
		PrintWriter asr1Xwriter = null;
		PrintWriter asr2Xwriter = null; 
		try {
			int mothCounter = 1; 
			int dayCounter = 1;
			asr1Xwriter = new PrintWriter( "/preytimes/asr1x/" + mothCounter+ ".xml", "UTF-8");
			asr1Xwriter.println("<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"yes\"?>");
			asr1Xwriter.println("<Records>");
			asr1Xwriter.println("<Row A=\"Dato\" B=\"Fajr\" C=\"Soloppgang\" D=\"Dhuhr\" E=\"Asr\" F=\"Maghrib\" G=\"Isha\"/>");
			asr2Xwriter = new PrintWriter("/preytimes/asr2x/" + mothCounter + ".xml", "UTF-8");
			asr2Xwriter.println("<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"yes\"?>");
			asr2Xwriter.println("<Records>");
			asr2Xwriter.println("<Row A=\"Dato\" B=\"Fajr\" C=\"Soloppgang\" D=\"Dhuhr\" E=\"Asr\" F=\"Maghrib\" G=\"Isha\"/>");
			
			String line = br.readLine();

			while (line != null) {
				if(!line.contains("#sep")){
					asr1Xwriter.write(getAsr1xLine(line,dayCounter));
					asr2Xwriter.write(getAsr2xLine(line,dayCounter));
					dayCounter++;
				}else {
					dayCounter = 1; 
					mothCounter++;
					asr1Xwriter.println("</Records>");
					asr2Xwriter.println("</Records>");
					asr1Xwriter.close();
					asr2Xwriter.close();
					
					asr1Xwriter = new PrintWriter( "/preytimes/asr1x/" + mothCounter+ ".xml", "UTF-8");
					asr1Xwriter.println("<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"yes\"?>");
					asr1Xwriter.println("<Records>");
					asr1Xwriter.println("<Row A=\"Dato\" B=\"Fajr\" C=\"Soloppgang\" D=\"Dhuhr\" E=\"Asr\" F=\"Maghrib\" G=\"Isha\"/>");
					
					asr2Xwriter = new PrintWriter("/preytimes/asr2x/" + mothCounter + ".xml", "UTF-8");
					asr2Xwriter.println("<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"yes\"?>");
					asr2Xwriter.println("<Records>");
					asr2Xwriter.println("<Row A=\"Dato\" B=\"Fajr\" C=\"Soloppgang\" D=\"Dhuhr\" E=\"Asr\" F=\"Maghrib\" G=\"Isha\"/>");
					
					
					
				}
				line = br.readLine();
			}
		} finally {
			asr1Xwriter.close();
			asr2Xwriter.close();
			br.close();
		}
	}
	
	
	/*<Records>
  
    <Row A="Dato" B="Fajr" C="Soloppgang" D="Dhuhr" E="Asr" F="Maghrib" G="Isha"/>
  
  
    <Row
      A="1"
      B="5:39:00 AM"
      C="7:38:00 AM"
      D="12:06:00 PM"
      E="2:24:00 PM"
      F="4:25:00 PM"
      G="6:20:00 PM"
    />
  
	 * 
	 * 
	 */
	
	
	
	private String getAsr1xLine(String textLine, int day) {
		String[] vals = textLine.split("[\\s]+");
		DateTimeFormatter builder = DateTimeFormat.forPattern("kk:mm");
		DateTimeFormatter builderAmPm = DateTimeFormat.forPattern("hh:mm:ss a"); 
		
		String line =    "<Row A=\"day\" B=\"fajr\" C=\"soloppgang\" D=\"duhr\" E=\"asr1\" F=\"magribh\" G=\"isha\"/>";
		
		line = line.replace("day",""+day);

		String fajr  = builder.parseDateTime(vals[0]).toString(builderAmPm);
		line = line.replace("fajr",fajr);
		String soloppgang  = builder.parseDateTime(vals[1]).toString(builderAmPm);	
		line = line.replace("soloppgang",soloppgang);
		String duhr = builder.parseDateTime(vals[2]).toString(builderAmPm);
		line = line.replace("duhr",duhr);
		String asr1 = builder.parseDateTime(vals[3]).toString(builderAmPm);
		line = line.replace("asr1",asr1);
		String magribh = builder.parseDateTime(vals[5]).toString(builderAmPm);
		line = line.replace("magribh",magribh);
		String isha = builder.parseDateTime(vals[6]).toString(builderAmPm);
		line = line.replace("isha",isha);
		
		return line;
		
	}
	
	private String getAsr2xLine(String textLine, int day) {
		String[] vals = textLine.split("[\\s]+");
		DateTimeFormatter builder = DateTimeFormat.forPattern("kk:mm");
		DateTimeFormatter builderAmPm = DateTimeFormat.forPattern("hh:mm:ss a"); 
		
		String line =    "<Row A=\"day\" B=\"fajr\" C=\"soloppgang\" D=\"duhr\" E=\"asr2\" F=\"magribh\" G=\"isha\"/>";
		
		line = line.replace("day",""+day);
		String fajr  = builder.parseDateTime(vals[0]).toString(builderAmPm);
		line = line.replace("fajr",fajr);
		String soloppgang  = builder.parseDateTime(vals[1]).toString(builderAmPm);	
		line = line.replace("soloppgang",soloppgang);
		String duhr = builder.parseDateTime(vals[2]).toString(builderAmPm);
		line = line.replace("duhr",duhr);
		String asr2 = builder.parseDateTime(vals[4]).toString(builderAmPm);
		line = line.replace("asr2",asr2);
		String magribh = builder.parseDateTime(vals[5]).toString(builderAmPm);
		line = line.replace("magribh",magribh);
		String isha = builder.parseDateTime(vals[6]).toString(builderAmPm);
		line = line.replace("isha",isha);
		
		return line;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
