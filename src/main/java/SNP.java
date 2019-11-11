/**
 * 
 * @author Alexander Herbig
 *
 */
public class SNP
{
	private int pos;
	public boolean keepMe;
	
	public SNP(int pos)
	{
		this.pos = pos;
		keepMe=true;
	}
	
	public int getPos()
	{
		return pos;
	}
}
