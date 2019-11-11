/**
 * 
 * @author Alexander Herbig
 *
 */
public class Gene
{
	public String name;
	public String anno;
	
	public int start;
	public int end;
	public char strand;
	public String idAsParent;
	
	
	public Gene(String name, String anno, int start, int end, char strand, String idAsParent) {
		super();
		this.name = name;
		this.anno = anno;
		this.start = start;
		this.end = end;
		this.strand = strand;
		this.idAsParent = idAsParent;
	}

	public Gene(String name, String anno)
	{
		super();
		this.name = name;
		this.anno = anno;
	}
	
	public Gene(String name, String anno, int length)
	{
		this(name,anno);
	}
	
	public int length()
	{
		return Math.abs(end-start)+1;
	}
}
