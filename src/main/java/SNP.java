
public class SNP
{
	public String chromName;
	private int pos;
	public boolean keepMe;
	
	public SNP(String chromName, int pos)
	{
		this.chromName = chromName;
		this.pos = pos;
		keepMe=true;
	}
	
	public int getPos()
	{
		return pos;
	}
}
