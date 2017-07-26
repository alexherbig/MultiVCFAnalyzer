/**
 * 
 * @author Alexander Herbig
 *
 */
public class SNPeffSNP extends SNP
{
	//Position	Reference	Change	Gene_ID	Gene_name	Effect	old_AA/new_AA	Old_codon/New_codon	Codon_Num(CDS)	CDS_size
	public char from;
	public char to;
	public String geneID;
	public String geneName;
	public String effect;
	public String aaChange;
	public String codonChange;
	public String codonNum;
	public String cdsSize;
	
	public SNPeffSNP(int pos, char from, char to, String geneID, String geneName, String effect, String aaChange, String codonChange, String codonNum, String cdsSize)
	{
		super(pos);
		this.from = from;
		this.to = to;
		this.geneID = geneID;
		this.geneName = geneName;
		this.effect = effect;
		this.aaChange = aaChange;
		this.codonChange = codonChange;
		this.codonNum = codonNum;
		this.cdsSize = cdsSize;
	}
	
	
}
