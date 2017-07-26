

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Alexander Herbig
 *
 */
public class Read 
{

	
	
	
	public static List<SNPeffSNP> readSNPeffSNPs(String filepath) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		
		List<SNPeffSNP> res = new LinkedList<SNPeffSNP>();
		
		
		String line;
		String[] cells;
		SNPeffSNP snp;
		
		int pos;
		char from;
		char to;
		String geneID;
		String geneName;
		String effect;
		String aaChange;
		String codonChange;
		String codonNum;
		String cdsSize;
		
		
		while((line=br.readLine())!=null)
		{
			if(line.startsWith("#"))
				continue;
			
			cells = line.split("\t",-1);
			
			pos = Integer.parseInt( cells[ 1]);
			from = 					cells[ 2].charAt(0);
			to = 					cells[ 3].charAt(0);
			geneID = 				cells[ 9];
			geneName = 				cells[10];
			effect = 				cells[15];
			aaChange = 				cells[16];
			codonChange = 			cells[17];
			codonNum = 				cells[18];
			cdsSize =  				cells[20];
			
			snp = new SNPeffSNP(pos, from, to, geneID, geneName, effect, aaChange, codonChange, codonNum, cdsSize);
			
			res.add(snp);
		}
		
		
		br.close();
		return res;
	}
	
	/**
	 * Parses a file in protein table format which contains information about
	 * protein coding regions.
	 *
	 * @param filename	the name of the file which contains the protein table
	 * @return	a list containing the resulting Gene objects
	 * @throws Exception
	 */
	public static List<Gene> parseProteinTable(String filename) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		LinkedList<Gene> genes = new LinkedList<Gene>();
		
		String name;
		String[] startEndStr;
		int start;
		int end;
		char strand;
		String anno;
		
		String line;
		String[] lineFields;
		
		line=br.readLine();
		while(line!=null)
		{
			line=line.trim();
			
			//empty?
			if(line.length()==0)
			{
				line=br.readLine();
				continue;
			}
			
			if(line.charAt(0)=='#')
			{
				line=br.readLine();
				continue;
			}
			
			lineFields = line.split("\t");
			
			//is entry?
			if(lineFields.length<9)
			{
				line=br.readLine();
				continue;
			}
			
			startEndStr=lineFields[0].split("\\.\\.");
			if(startEndStr.length!=2)
			{
				line=br.readLine();
				continue;
			}
			
			try
			{
				start=Integer.parseInt(startEndStr[0]);
				end=Integer.parseInt(startEndStr[1]);
			}
			catch(NumberFormatException e)
			{
				line=br.readLine();
				continue;
			}
			//			
			name=lineFields[5];
			if(name.equalsIgnoreCase("-")) name = lineFields[4];
			
			strand=lineFields[1].charAt(0);
			
			anno = lineFields[8];
			
			genes.add(new Gene(name,anno));
			
			line=br.readLine();
		}
		br.close();
		return genes;
	}
	
	public static List<Gene> parseGFF(String filename) throws Exception
	{
		List<Gene> res = new LinkedList<Gene>();
		
		try {
		
		Map<String,String> locusTag2DescMap = new HashMap<String, String>();
		Map<String,String> parentId2DescMap = new HashMap<String, String>();
		Map<String,String> position2DescMap = new HashMap<String, String>();
		
		BufferedReader r = new BufferedReader(new FileReader(filename));
		
		String line;
		
		String id;
		String type;
		int start;
		int end;
		char strand;
		String desc;
		String source;
		String idAsParent;
		
		Map<String,String> attributes = new HashMap<String,String>();
		String[] cells;
		String[] attcells;
		
		for(line=r.readLine();line!=null;line=r.readLine())
		{
			line=line.trim();
			if(line.length()==0)
				continue;
			if(line.charAt(0)=='#')
				continue;
			
			cells = line.split("[\\t]");
			
			//if(!(cells[2].equalsIgnoreCase("CDS") || cells[2].equalsIgnoreCase("exon")))
				//continue;
			
			//source = 0
			source = cells[0];
			
			//type = 2
			type = cells[2];
			
			// start = 3
			start = Integer.parseInt(cells[3]);
			
			// end = 4
			end = Integer.parseInt(cells[4]);
			
			// strand = 6
			strand = cells[6].charAt(0);
			
			attributes.clear();
			
			if(cells.length>=9)
				for(String s : cells[8].split(";"))
				{
					attcells = s.split("=");
					if(attcells.length==2)
						attributes.put(attcells[0].trim(), attcells[1].trim());
					else if(attcells.length==1)
						attributes.put(attcells[0].trim(), "");					
				}
			
			if(attributes.containsKey("locus_tag"))
				id=attributes.get("locus_tag");
			else if(attributes.containsKey("ID"))
				id=attributes.get("ID");
			else
				id="locus"+start+"-"+end;
			
			if(attributes.containsKey("ID"))
				idAsParent = attributes.get("ID");
			else
				idAsParent = null;
			
			if(attributes.containsKey("product"))
				desc=attributes.get("product");
			else
				desc="";
			
			
			//locus tag to desc mapping
			if(attributes.containsKey("locus_tag"))
			{
				if(attributes.containsKey("product"))
					locusTag2DescMap.put(attributes.get("locus_tag"), attributes.get("product"));
					
				if(attributes.containsKey("pseudo"))
					locusTag2DescMap.put(attributes.get("locus_tag"), "pseudo");
			}
			
			//parent id to desc mapping
			if(attributes.containsKey("Parent"))
			{
				if(attributes.containsKey("product"))
					parentId2DescMap.put(attributes.get("Parent"), attributes.get("product"));
				
				if(attributes.containsKey("pseudo"))
					parentId2DescMap.put(attributes.get("Parent"), "pseudo");
			}
			
			//position to desc map
			if(attributes.containsKey("product"))
				position2DescMap.put(start+"_"+end+"_"+strand, attributes.get("product"));
			
			if(attributes.containsKey("pseudo"))
				position2DescMap.put(start+"_"+end+"_"+strand, "pseudo");
			
			//create gene
			if(type.equalsIgnoreCase("gene"))
				res.add(new Gene(id,desc,start,end,strand,idAsParent));
		}
		r.close();
		
		//set descriptions
		for(Gene g:res)
			if(locusTag2DescMap.containsKey(g.name))
				g.anno = locusTag2DescMap.get(g.name);
			else if(parentId2DescMap.containsKey(g.idAsParent))
				g.anno = parentId2DescMap.get(g.idAsParent);
			else if(position2DescMap.containsKey(g.start+"_"+g.end+"_"+g.strand))
				g.anno = position2DescMap.get(g.start+"_"+g.end+"_"+g.strand);
		
		}catch(Throwable e)
		{
			throw new Exception("A problem occured while parsing the GFF annotation:\n"+filename+"\n"+e.toString(),e);
		}
		
		return res;
	}
	
	public static List<Gene> getCDSfromGFF(String filename) throws Exception
	{
		List<Gene> res = new LinkedList<Gene>();
		
		try {
		
		
		BufferedReader r = new BufferedReader(new FileReader(filename));
		
		String line;
		
		String id;
		String type;
		int start;
		int end;
		char strand;
		String desc;
		String source;
		String idAsParent;
		
		Map<String,String> attributes = new HashMap<String,String>();
		String[] cells;
		String[] attcells;
		
		for(line=r.readLine();line!=null;line=r.readLine())
		{
			line=line.trim();
			if(line.length()==0)
				continue;
			if(line.charAt(0)=='#')
				continue;
			
			cells = line.split("[\\t]");
			
			//if(!(cells[2].equalsIgnoreCase("CDS") || cells[2].equalsIgnoreCase("exon")))
				//continue;
			
			//source = 0
			source = cells[0];
			
			//type = 2
			type = cells[2];
			
			// start = 3
			start = Integer.parseInt(cells[3]);
			
			// end = 4
			end = Integer.parseInt(cells[4]);
			
			// strand = 6
			strand = cells[6].charAt(0);
			
			attributes.clear();
			
			if(cells.length>=9)
				for(String s : cells[8].split(";"))
				{
					attcells = s.split("=");
					if(attcells.length==2)
						attributes.put(attcells[0].trim(), attcells[1].trim());
					else if(attcells.length==1)
						attributes.put(attcells[0].trim(), "");					
				}
			
			if(attributes.containsKey("locus_tag"))
				id=attributes.get("locus_tag");
			else if(attributes.containsKey("ID"))
				id=attributes.get("ID");
			else
				id="locus"+start+"-"+end;
			
			if(attributes.containsKey("ID"))
				idAsParent = attributes.get("ID");
			else
				idAsParent = null;
			
			if(attributes.containsKey("product"))
				desc=attributes.get("product");
			else
				desc="";
			
			
			
			//create gene
			if(type.equalsIgnoreCase("CDS"))
				res.add(new Gene(id,desc,start,end,strand,idAsParent));
		}
		r.close();
		
		
		
		}catch(Throwable e)
		{
			throw new Exception("A problem occured while parsing the GFF annotation:\n"+filename+"\n"+e.toString(),e);
		}
		
		return res;
	}
}
