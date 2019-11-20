
import java.io.BufferedWriter;

/**
 * Writes a list of sequences to a file in FASTA format.
 * 
 * @author Alexander Herbig
 *
 */
public class FASTAWriter 
{
	/**
	 * Writes a list of sequences to a file in FASTA format.
	 * 
	 * @param bw	the BufferedWriter writing the FASTA file
	 * @param fastaEntries	list of FASTAEntry objects holding the sequences 
	 * 						which are written to the FASTA file
	 * @throws Exception
	 */
	public static void write(BufferedWriter bw, String genomeID, String sequence) throws Exception
	{	
		int charsInLine;
		String tmpSeqString;
		
		tmpSeqString=sequence;
		bw.write(">"+genomeID);
		bw.newLine();
		
		charsInLine=0;
		for(int i=0; i<tmpSeqString.length();i++)
		{
			bw.write(tmpSeqString.charAt(i));
			charsInLine++;
			
			if(charsInLine==60)
			{
				bw.newLine();
				charsInLine=0;
			}
		}
		if(charsInLine!=0)
		{
			bw.newLine();
		}
		
		bw.flush();
	}
}
