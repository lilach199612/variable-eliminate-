
public class Question {
	private String Queries;
	private String [] evidences;
	private String [] hiddens;
	
	public Question(String s) {
		s=s.substring(2);//delete p(
		String []str=s.split("\\|");
		this.Queries=str[0];		
		String[]str1=str[1].split("\\),");
		this.evidences=str1[0].split(",");
		
		int lengthOfHidden=0;
		if(str1.length>1) {
			this.hiddens=str1[1].split("-");
			lengthOfHidden=this.hiddens.length;
		}
		//add the querie to the hidden.
		String [] addQueriesToHidden=new String[lengthOfHidden+1];
		for (int i=0;i<lengthOfHidden;i++) 
			addQueriesToHidden[i]=this.hiddens[i];
		addQueriesToHidden[addQueriesToHidden.length-1]=""+this.Queries.charAt(0);
		this.hiddens=addQueriesToHidden;
		
	}
	
	public void setHidden(String[] h) {
		this.hiddens=h;
	}
	
	public String [] getHiddens() {
		return this.hiddens;
	}
	
	public String [] getEvidences() {
		return this.evidences;
	}
	
	public String getQueries() {
		return this.Queries;
	}
	
	public String toString() {
		String str="Queries: "+this.Queries+"\n"+"evidences: ";
		for(int i=0;i<this.evidences.length;i++) {
			str+=this.evidences[i];
			if(i!=this.evidences.length-1)
				str+=",";
		}
		str+="\n"+"hiddens: ";
		for(int i=0;i<this.hiddens.length;i++) {
			str+=this.hiddens[i];
			if(i!=this.hiddens.length-1)
				str+=",";
		}
		return str;
	}
	

}
