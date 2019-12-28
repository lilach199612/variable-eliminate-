//208386052_322525957


import java.io.*;
import java.util.ArrayList;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.print.DocFlavor.STRING;

import java.nio.CharBuffer;

public class Ex1 {
	public static void main(String[] args) throws IOException {


		FileReader fr = new FileReader("input.txt");
		BufferedReader br = new BufferedReader(fr);
		String lineReader;
		lineReader=br.readLine();//Network
		lineReader=br.readLine();//Variable
		lineReader=lineReader.substring(11,lineReader.length());
		String [] s=lineReader.split(",");
		String[] builders =new String[s.length];

		for (int j = 0; j < s.length; j++) 	//init the array because we are doing += and we can't do it for null
			builders[j]="";

		lineReader=br.readLine();//empty line
		for (int j = 0; j < s.length; j++) {//keep all the data for any variable
			lineReader=br.readLine();
			while(lineReader.isEmpty()==false) {
				builders[j]+=lineReader+"\n";
				lineReader=br.readLine();
			}	
		}

		Variable [] v=new Variable[s.length];
		for (int j = 0; j < s.length; j++) //build the variable for every paragraph
			v[j]=new Variable(builders[j]);

		lineReader=br.readLine();//Queries

		lineReader=br.readLine();
		ArrayList <Question> questions=new ArrayList();
		while(lineReader!=null) {//build a question from every line that contain p as it first letter
			if(lineReader.charAt(0)=='P') {
				Question q=new Question(lineReader);
				questions.add(q);
				//	System.out.println(q);
			}

			lineReader=br.readLine();
		}		
		br.close();
		fr.close();

		for (int i=0;i<v.length;i++) {
			v[i].ArrangeCPT();
		}
		ArrayList<Oparation> op=new ArrayList();
		FactorsForQ [] ffq= new FactorsForQ[questions.size()];
		String [] answer=new String[ffq.length];
		for (int i=0;i<ffq.length;i++) {
			Oparation opI=new Oparation();
			ffq[i]=createFactorForQuestion(questions.get(i),v);
			boolean eliminate=true;
			for(int j=0;j<questions.get(i).getHiddens().length;j++) {
				if(j==questions.get(i).getHiddens().length-1)
					eliminate=false;
				ffq[i]=joinAndEliminate(ffq[i],questions.get(i).getHiddens()[j],eliminate,opI,v);
			}
			if(questions.get(i).getHiddens().length>1)
				normalize(ffq[i].getFactors().get(0),opI);
			for(int j=1;j<ffq[i].getFactors().get(0).getCPT().size();j++) {
				String ans=questions.get(i).getQueries().split("\\=")[1];
				if(ffq[i].getFactors().get(0).getCPT().get(j)[0].equals(ans)) {
					answer[i]=ffq[i].getFactors().get(0).getCPT().get(j)[1];
					break;
				}
			}
			op.add(opI);
		}
		output(answer,op);

	}

	public static void output(String [] answer,ArrayList<Oparation> op) throws IOException {
		FileWriter fw = new FileWriter("output.txt");  
		PrintWriter outs = new PrintWriter(fw);
		DecimalFormat df = new DecimalFormat("#.#####");
		df.setRoundingMode(RoundingMode.CEILING);
		for(int i=0;i<answer.length;i++) {
			outs.println(df.format(Double.parseDouble(answer[i]))+","+op.get(i).plus+","+op.get(i).mul);
		}
		outs.close(); 
		fw.close();

	}


	public static void normalize(Variable v,Oparation op) {
		double sum=Double.parseDouble(v.getCPT().get(1)[v.getCPT().get(1).length-1]);
		ArrayList <String []> temp=new ArrayList();
		temp.add(v.getCPT().get(0));
		for(int i=2;i<v.getCPT().size();i++) {
			sum+=Double.parseDouble(v.getCPT().get(i)[v.getCPT().get(i).length-1]);
			op.plus++;
		}
		for(int i=1;i<v.getCPT().size();i++) {
			String[] s=v.getCPT().get(i);
			s[s.length-1]=""+(Double.parseDouble(s[s.length-1])/sum);
			temp.add(s);
		}
	}
	
	public static int countMultiply(Variable v1,Variable v2) {
		//this function is the function "joinTwo" but without the join, it is only count the multiplies.	
		int countMul=0;
		//find all the common var{
		ArrayList<Integer> indexV1=new ArrayList();
		ArrayList<Integer> indexV2=new ArrayList();
		for(int i=0;i<v1.getCPT().get(0).length-1;i++) {
			for(int j=0;j<v2.getCPT().get(0).length-1;j++){
				if(v1.getCPT().get(0)[i].equals(v2.getCPT().get(0)[j])) {
					indexV1.add(i);
					indexV2.add(j);
				}
			}
		}
		//}

		for(int i=1;i<v1.getCPT().size();i++) {//for every line in the cpt of v1
			for(int j=1;j<v2.getCPT().size();j++) {//for every line in the cpt of v2 
				boolean equal=true;
				for(int h=0;h<indexV1.size();h++) {//does all the common var in this line are equal?
					if(!v1.getCPT().get(i)[indexV1.get(h)].equals(v2.getCPT().get(j)[indexV2.get(h)])) {
						equal=false;
						break;
					}
				}
				if(equal==true) {	//if every thing is equal the is going to be a mul
					countMul++;
				}
			}
		}
		return countMul;
	}



	public static void joinTwo(Variable v1,Variable v2,String hidden,Oparation op) {
		//find all the common var{
		ArrayList<Integer> indexV1=new ArrayList();
		ArrayList<Integer> indexV2=new ArrayList();
		for(int i=0;i<v1.getCPT().get(0).length-1;i++) {
			for(int j=0;j<v2.getCPT().get(0).length-1;j++){
				if(v1.getCPT().get(0)[i].equals(v2.getCPT().get(0)[j])) {
					indexV1.add(i);
					indexV2.add(j);
				}
			}
		}
		//}
		boolean equal=true;

		//new title with all the vars
		ArrayList<String[]>newCPT=new ArrayList();
		String[]templine=new String[v1.getCPT().get(0).length+v2.getCPT().get(0).length-indexV1.size()-1];
		int i;
		for( i=0;i<v1.getCPT().get(0).length-1;i++) {
			templine[i]=v1.getCPT().get(0)[i];
		}
		for(int h=0;h<v2.getCPT().get(0).length-1;h++) {
			if(!v1.cptVarContain(v2.getCPT().get(0)[h])) {
				templine[i]=v2.getCPT().get(0)[h];
				i++;
			}
		}	
		templine[i++]="=";
		newCPT.add(templine);
		//}

		for( i=1;i<v1.getCPT().size();i++) {//for every line in the cpt of v1
			for(int j=1;j<v2.getCPT().size();j++) {//for every line in the cpt of v2 
				equal=true;
				for(int h=0;h<indexV1.size();h++) {//does all the common var in this line are equal?
					if(!v1.getCPT().get(i)[indexV1.get(h)].equals(v2.getCPT().get(j)[indexV2.get(h)])) {
						equal=false;
						break;
					}
				}
				if(equal==true) {	
					String []temp=new String[v1.getCPT().get(i).length+v2.getCPT().get(j).length-indexV1.size()-1];
					int index=0;
					for(int k=0;k<v1.getCPT().get(0).length-1;k++) {//copy all the index from v1
						temp[index]=v1.getCPT().get(i)[k];
						index++;
					}
					for(int h=0;h<v2.getCPT().get(0).length-1;h++) {//copy all the index from v2 that are not common to v1
						if(!v1.cptVarContain(v2.getCPT().get(0)[h])) {
							temp[index]=v2.getCPT().get(j)[h];
							index++;
						}
					}
					op.mul++;
					temp[index++]=""+(Double.parseDouble(v1.copyCPT().get(i)[v1.copyCPT().get(i).length-1])*Double.parseDouble(v2.getCPT().get(j)[v2.getCPT().get(j).length-1]));
					newCPT.add(temp);
				}
			}
		}
		v1.setCPT(newCPT);
	}

	public static FactorsForQ joinAndEliminate(FactorsForQ ffq,String hidden,boolean eliminate,Oparation op,Variable[]v) {
		FactorsForQ temp=new FactorsForQ();
		//System.out.println(ffq);
		for(int i=0;i<ffq.getFactors().size();i++) {//enter to temp all the variable that contain the hidden
			if(ffq.getFactors().get(i).getVar().equals(hidden)) {
				temp.add(ffq.getFactors().get(i));
			}
			else {
				for(int j=0;j<ffq.getFactors().get(i).getCPT().get(0).length;j++) {
					if(ffq.getFactors().get(i).getCPT().get(0)[j].equals(hidden)) {
						temp.add(ffq.getFactors().get(i));
						break;
					}
				}
			}		
		}
		//System.out.println(hidden+"before"+temp);
		//join{
		while(temp.getFactors().size()>1) {//join all the variable that contain the hidden to one variable
			int min=countMultiply(temp.getFactors().get(0),temp.getFactors().get(1))+1;
			int v1=-1;
			int v2=-1;
			for(int i=0;i<temp.getFactors().size();i++) {//go over all the options to multiply and take the one that the the least
				for(int j=i+1;j<temp.getFactors().size();j++) {
					if(countMultiply(temp.getFactors().get(i),temp.getFactors().get(j))<min) {
						min=countMultiply(temp.getFactors().get(i),temp.getFactors().get(j));
						v1=i;
						v2=j;
					}
				}

			}

			joinTwo(temp.getFactors().get(v1),temp.getFactors().get(v2),hidden,op);
			temp.getFactors().remove(v2);
		}
		//}

		//Eliminate{
		if(eliminate==true) {

			//find the index of the hidden
			int findIndex=-1;
			for(int i=0;i<temp.getFactors().get(0).getCPT().get(0).length;i++) {
				if(temp.getFactors().get(0).getCPT().get(0)[i].equals(hidden)) {
					findIndex=i;
					break;
				}
			}

			//delete the column of the hidden
			ArrayList <String[]> fixcpt=new ArrayList();
			for(int i=0;i<temp.getFactors().get(0).getCPT().size();i++) {
				String[]newline=new String[temp.getFactors().get(0).getCPT().get(i).length-1] ;
				int j=0,k=0;
				while(j<newline.length) {//copy all the index but the hidden
					if(k!=findIndex) {
						newline[j]=temp.getFactors().get(0).getCPT().get(i)[k];
						j++;
						k++;
					}
					else
						k++;
				}	
				fixcpt.add(newline);
			}
			temp.getFactors().get(0).setCPT(fixcpt);


			boolean equal=true;
			fixcpt=temp.getFactors().get(0).copyCPT();
			ArrayList <String[]> newcpt=new ArrayList();
			newcpt.add(fixcpt.get(0));


			for(int i=1;i<fixcpt.size();i++) {//find the line that compare to this line 
				ArrayList <Integer> index=new ArrayList();
				for(int j=fixcpt.size()-1;j>=1;j--) {
					equal=true;
					for(int h=0;h<fixcpt.get(j).length-1;h++) {//find if all the index are equals
						if(i==j||!fixcpt.get(j)[h].equals(fixcpt.get(i)[h]))
							equal=false;
					}
					if(equal==true) {
						index.add(j);
					}
				}
				//make all the common lines to line i to be one line with the sum of all of them
				String[]line=fixcpt.get(i);
				double sum=Double.parseDouble(fixcpt.get(i)[fixcpt.get(i).length-1]);
				for(int j=0;j<index.size();j++) {
					op.plus++;
					sum+=Double.parseDouble(fixcpt.get(index.get(j))[fixcpt.get(index.get(j)).length-1]);
				}
				line[line.length-1]=""+sum;
				newcpt.add(line);

				//remove all the common line that we fine(enter all the lines that are not common to temp and update fixcpt to temp)
				ArrayList <String[]> tempcpt=new ArrayList();
				for(int j=0;j<fixcpt.size();j++) {
					if(!index.contains(j)) {
						tempcpt.add(fixcpt.get(j));
					}
				}
				fixcpt=tempcpt;
				temp.getFactors().get(0).setCPT(fixcpt);

			}
			temp.getFactors().get(0).setCPT(newcpt);
		}
		//}

		boolean equal=false;
		for(int i=0;i<ffq.getFactors().size();i++) {//enter to temp all the variable that NOT contain the hidden
			equal=false;
			if(ffq.getFactors().get(i).getVar().equals(hidden)) {
				equal=true;
			}
			else {
				for(int j=0;j<ffq.getFactors().get(i).getPerents().length;j++) {
					if(ffq.getFactors().get(i).getPerents()[j].equals(hidden)) {
						equal=true;
					}
				}
			}
			if(ffq.getFactors().get(i).getVar().equals(temp.getFactors().get(0).getVar())) 
				equal=true;
			if (equal==false)
				temp.add(ffq.getFactors().get(i)); 
		}
		return temp;
	}

	public static FactorsForQ createFactorForQuestion (Question q,Variable [] v1) {
		//delete the vars that are not ralevant to the querie (takes only the parents and the parent of the parents)
		ArrayList <String> vars=new ArrayList();
		vars.add(q.getQueries().split("\\=")[0]);
		for(int i=0;i<q.getEvidences().length;i++) {
			vars.add(q.getEvidences()[i].split("\\=")[0]);
		}
		for(int h=0;h<vars.size();h++) {//all the important vars
			for(int y=0;y<v1.length;y++) {//find them
				if(vars.get(h).equals(v1[y].getVar())) {
					for(int x=0;x<v1[y].getPerents().length;x++) {//enter there parents to the important vars
						if(v1[y].getPerents()[0].equals("none"))
							break;
						vars.add(v1[y].getPerents()[x]);
					}
				}
			}
		}
		
		for(int i=0;i<vars.size();i++) {
			for(int j=i+1;j<vars.size();j++) {
				if(vars.get(i).equals(vars.get(j))) {
					vars.remove(j);
					j--;
				}
			}
		}

		Variable [] v=new Variable[vars.size()];
		for(int h=0;h<vars.size();h++) {//enter to v only the important vars
			for (int i=0;i<v1.length;i++) {
				if(vars.get(h).equals(v1[i].getVar())) {
					v[h]=v1[i];
					break;
				}

			}
		}

		
		//update the hidden
		ArrayList <String> fix=new ArrayList();
		for(int j=0;j<q.getHiddens().length;j++) {
			fix.add(q.getHiddens()[j]);
		}
		for(int j=0;j<fix.size();j++) {
			boolean equal=false;
			for(int i=0;i<vars.size();i++) {
				if(fix.get(j).equals(vars.get(i))) {
					equal=true;
				}	
			}
			if(equal==false) {
				fix.remove(j);
				j--;
			}
		}
		String [] newHidden=new String [fix.size()];
		for(int i=0;i<fix.size();i++) {
			newHidden[i]=fix.get(i);
		}
		q.setHidden(newHidden);

		//}




		boolean enter=false;
		FactorsForQ ffq=new FactorsForQ();
		for(int i=0;i<v.length;i++) {//for every variable
			Variable temp=v[i].copyWithoutCPT();
			ArrayList <String[]> newCPT=new ArrayList();
			temp.setCPT(v[i].copyCPT());
			for(int j=0;j<q.getEvidences().length;j++) {//find if it contain evidance
				String[] thisEvidence=q.getEvidences()[j].split("\\=");
				int index=0;
				while(index<temp.getCPT().get(0).length) {
					for(int k=index;k<temp.getCPT().get(0).length;k++) {//check if one of the vars is an evidance
						index++;
						if(temp.getCPT().get(0)[k].equals(thisEvidence[0])) {//if the vars is the evidance
							enter=true;
							newCPT.add(temp.getCPT().get(0));
							for(int h=1;h<temp.getCPT().size();h++) {//take only the line that are equals
								if(temp.getCPT().get(h)[k].equals(thisEvidence[1])) {
									newCPT.add(temp.getCPT().get(h));
								}
							}
							temp.setCPT(newCPT);
							break;
						}
					}
					newCPT=new ArrayList();
					if(index<temp.getCPT().get(0).length) {
						ArrayList <String[]> tempCPT=new ArrayList();
						for(int h=0;h<temp.getCPT().size();h++) {
							String [] tempArray=new String[temp.getCPT().get(0).length-1];
							int plus=0;
							for(int y=0;y<tempArray.length;y++) {
								if(y==index-1&&plus!=1) {
									plus=1;
									y--;
								}
								else
									tempArray[y]=temp.getCPT().get(h)[y+plus];
							}
							newCPT.add(tempArray);
						}
						temp.setCPT(newCPT);
					}
				}
			}
			ffq.add(temp);
		}

		// If an instantiated CPT becomes one-valued, discard the factor
		for(int i=0;i<ffq.getFactors().size();i++) {//
			if(ffq.getFactors().get(i).getCPT().size()==2) {
				if(ffq.getFactors().get(i).getCPT().get(0).length==1) {
					ffq.getFactors().remove(i);
					i--;
				}
			}
		}


		return ffq;
	}
}


