import java.util.ArrayList;

public class FactorsForQ {
	private ArrayList <Variable> factors;
	
	public void ArrangeCPTForJoin() {
	for(int i=0;i<this.factors.size();i++)
		this.factors.get(i).ArrangeCPT();
	}
	
	public FactorsForQ() {
		this.factors=new ArrayList();
	}
	public FactorsForQ(ArrayList <Variable> f) {
		this.factors=f;
	}
	public FactorsForQ copy() {
		ArrayList <Variable> f=new ArrayList();
		for(int i=0;i<this.factors.size();i++) {
			f.add(this.factors.get(i).copyWithoutCPT());
			f.get(i).setCPT(this.factors.get(i).copyCPT());
		}
		
		return new FactorsForQ(f);
		
	}
	public void add(Variable v) {
		this.factors.add(v);
	}
	public ArrayList <Variable> getFactors(){
		return this.factors;
	}
	public String toString() {
		String str="";
		for(int i=0;i<this.factors.size();i++) {
			str+=this.factors.get(i).toString()+"\n"+"*********"+"\n";
		}
		return str;
	}
}
