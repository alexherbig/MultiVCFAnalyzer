

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads a multiple FASTA file containing DNA sequences in FASTA format
 * and stores the entries as FASTAEntry objects.
 * 
 * @author Alexander Herbig
 *
 */
public class FASTAParser 
{
	/**
	 * Reads a multiple FASTA file containing DNA sequences in FASTA format.
	 * @param br	the BufferedReader which reads from the multiple FASTA file
	 * @return	a list containing the resulting FASTAEntry objects
	 * @throws Exception
	 */
	public static Map<String,String> parseDNA(String filename) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		Map<String,String> fastaEntries = new HashMap<String, String>();
		
		String tmpID;
		StringBuffer tmpSeqString = new StringBuffer();
		String tmpLine = br.readLine();
		
		//jump to first header line
		while(tmpLine!=null&&(tmpLine.charAt(0)!='>' || tmpLine.length()==0))
		{
			tmpLine=br.readLine();
		}
		tmpID=tmpLine;
		
		tmpLine=br.readLine();
		while(tmpLine!=null)
		{
			if(tmpLine.length()!=0)
			{
				if(tmpLine.charAt(0)=='>')
				{
					fastaEntries.put(toID(tmpID), tmpSeqString.toString());
					
					tmpSeqString = new StringBuffer();
					
					tmpID = tmpLine;
				}
				else
				{
					tmpSeqString.append(tmpLine);
				}
			}
			tmpLine = br.readLine();
		}
		fastaEntries.put(toID(tmpID), tmpSeqString.toString());
		
		br.close();
		
		return fastaEntries;
	}
	
	private static String toID(String fastaID)
	{
		return(fastaID.substring(1));
	}

}
