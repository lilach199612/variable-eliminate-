
import java.util.*;

public class Variable {
	private String Var;
	private String[]Value;
	private String[] Perent;
	private ArrayList <String[]> CPT;


	public void ArrangeCPT() {
		//take all the lines that look like " true,true,0.5,false,0.5"
		//make them to be 2 lines like:
		//"true,true,0.5"
		//"true,false,0.5"

		ArrayList <String[]> temp=new ArrayList();
		String [] templine;
		for(int i=0;i<this.CPT.size();i++) {
			if(this.CPT.get(i).length>3&&Character.isDigit(this.CPT.get(i)[this.CPT.get(i).length-3].charAt(0))) {
				for(int j=1;j<this.Value.length+1;j++) {
					templine=new String[this.CPT.get(i).length-2*(this.Value.length-1)];
					for(int h=0;h<templine.length-2;h++) {
						templine[h]=this.CPT.get(i)[h];
					}
					templine[templine.length-2]=this.CPT.get(i)[this.CPT.get(i).length-2*j];
					templine[templine.length-1]=this.CPT.get(i)[this.CPT.get(i).length-2*j+1];	
					temp.add(templine);
				}
			}
			else {
				temp=this.CPT;
				break;
			}
		}
		int i;
		if(this.CPT.get(0).length>3&&Character.isDigit(this.CPT.get(0)[this.CPT.get(0).length-3].charAt(0))) 
			templine=new String[this.CPT.get(0).length-2*(this.Value.length-1)];
		else
			templine=new String[this.CPT.get(0).length];

		for( i=0;i<this.Perent.length;i++) {
			if(this.Perent.length==1&&this.Perent[0].equals("none"))
				break;
			templine[i]=this.Perent[i];
		}
		templine[i++]=this.Var;
		templine[i++]="=";



		this.CPT=new ArrayList();
		this.CPT.add(templine);
		this.CPT.addAll(temp);
	}
	
	public void setPerents(String[]p) {
		this.Perent=p;
	}

	public boolean cptVarContain(String p) {
		for(int i=0;i<this.CPT.get(0).length;i++) {
			if(this.CPT.get(0)[i].equals(p)){
				return true;
			}
		}
		return false;
	}
	public Variable copyWithoutCPT() {
		String var=this.Var;
		String [] value=new String[this.Value.length];
		for(int i=0;i<this.Value.length;i++)
			value[i]=this.Value[i];
		String [] perents=new String[this.Perent.length];
		for(int i=0;i<this.Perent.length;i++)
			perents[i]=this.Perent[i];

		return new Variable(var,value,perents);	
	}

	public void setCPT(ArrayList <String[]> cpt) {
		this.CPT=cpt;
	}

	public ArrayList <String[]> copyCPT(){
		ArrayList <String[]> newCPT=new ArrayList();
		for(int i=0;i<this.CPT.size();i++) {
			String []temp=new String[this.CPT.get(i).length];
			for(int j=0;j<temp.length;j++) {
				temp[j]=this.CPT.get(i)[j];
			}
			newCPT.add(temp);
		}
		return newCPT;
	}

	public Variable(String var,String[]value,String []perents) {
		this.Var=var;
		this.Value=value;
		this.Perent=perents;
		this.CPT=new ArrayList();
	}

	public Variable(String s) {
		//take all the data
		String[]str=s.split("\n");
		this.Var=str[0].substring(str[0].length()-1);
		this.Value=str[1].substring(8).split(",");
		this.Perent=str[2].substring(9).split(",");	
		this.CPT=new ArrayList();
		for(int i=4;i<str.length;i++) {
			this.CPT.add(str[i].split(","));
		}

		//arrangeCPT- add the last value for the line
		ArrayList<String[]> newcpt=new ArrayList();
		for(int i=0;i<this.CPT.size();i++) {
			String[] temp=this.CPT.get(i);
			for(int j=0;j<temp.length;j++) {//delete "="
				if(temp[j].charAt(0)=='=') 
					temp[j]=temp[j].substring(1);
			}
		}
		ArrayList <String[]> templist=new ArrayList();

		for(int i=0;i<this.CPT.size();i++) {
			double sum=0;
			for(int j=0;j<this.CPT.get(i).length;j++) {
				if(Character.isDigit(this.CPT.get(i)[j].charAt(0)))
					sum+=Double.parseDouble(this.CPT.get(i)[j]);
			}	
			String [] tempArray=new String[this.CPT.get(i).length+2];
			for(int h=0;h<this.CPT.get(i).length;h++) {
				tempArray[h]=this.CPT.get(i)[h];
			}
			tempArray[tempArray.length-2]=this.Value[this.Value.length-1];
			tempArray[tempArray.length-1]=""+(1-sum);
			templist.add(tempArray);
		}
		this.CPT=templist;
	}
	public String getVar() {
		return this.Var;
	}

	public ArrayList <String []> getCPT() {
		return this.CPT;
	}
	public String [] getPerents() {
		return this.Perent;
	}
	public String [] getValue() {
		return this.Value;
	}

	public String toString() {
		String str= "Var: "+this.Var+"\n"+"Value: ";

		for (int i = 0; i < this.Value.length; i++) {
			str+=this.Value[i];
			if(i!=this.Value.length-1)
				str+=",";
		}
		str+="\n"+"Parents: ";
		for (int i = 0; i < this.Perent.length; i++) {
			str+=this.Perent[i];
			if(i!=this.Perent.length-1)
				str+=",";
		}
		str+="\n"+"cpt: "+"\n"+"[";
		for (int i = 0; i < this.CPT.size(); i++) {
			for (int j = 0; j < this.CPT.get(i).length; j++) {
				str+=this.CPT.get(i)[j];	
				if(j!=this.CPT.get(i).length-1)
					str+=",";
			}
			if(i==this.CPT.size()-1)
				str+="]";
			else
				str+="\n";
		}
		return str;


	}

}
