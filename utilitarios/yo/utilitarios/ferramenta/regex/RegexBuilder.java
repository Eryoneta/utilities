package utilitarios.ferramenta.regex;
public interface RegexBuilder{
//CHARACTERS
	//SIMPLE
		public default String allExceptLineBreak(){return ".";}
		public default String word(){return "\\w";}
		public default String nonWord(){return "\\W";}
		public default String digit(){return "\\d";}
		public default String nonDigit(){return "\\D";}
		public default String space(){return "\\s";}
		public default String nonSpace(){return "\\S";}
		public default String empty(){return "\\0";}
		public default String tab(){return "\\t";}
		public default String lineBreak(){return "\\n";}
	//BOUNDARIES
		public default String startOrEndOfWord(){return "\\b";}
		public default String nonStartOrEndOfWord(){return "\\B";}
		public default String startOfText(){return "^";}
		public default String endOfText(){return "$";}
	//VARIOUS
		//INCLUDES
			public default String characters(String...words){		//WORDS
				final StringBuilder result=new StringBuilder("[");
				for(String word:words)result.append(word);
				return result+"]";
			}
			public default String characters(Range...ranges){		//RANGE
				final StringBuilder result=new StringBuilder("[");
				for(Range range:ranges)result.append(range);
				return result+"]";
			}
			public default String characters(Object...characters){	//WORD & RANGES
				final StringBuilder result=new StringBuilder("[");
				for(Object obj:characters){
					if(obj instanceof String)result.append((String)obj);
					if(obj instanceof Range)result.append((Range)obj);
				}
				return result+"]";
			}
		//EXCLUDES
			public default String allExcept(String...words){		//WORDS
				final StringBuilder result=new StringBuilder("[^");
				for(String word:words)result.append(word);
				return result+"]";
			}
			public default String allExcept(Range...ranges){		//RANGE
				final StringBuilder result=new StringBuilder("[^");
				for(Range range:ranges)result.append(range);
				return result+"]";
			}
			public default String allExcept(Object...characters){	//WORD & RANGES
				final StringBuilder result=new StringBuilder("[^");
				for(Object obj:characters){
					if(obj instanceof String)result.append((String)obj);
					if(obj instanceof Range)result.append((Range)obj);
				}
				return result+"]";
			}
//QUANTITY
	public default String oneOrMore(){return "+";}
	public default String zeroOrMore(){return "*";}
	public default String zeroOrOne(){return "?";}
	public default String occurs(int quantity){return "{"+quantity+"}";}
	public default String occursBetween(int minQuantity,int maxQuantity){return "{"+minQuantity+","+maxQuantity+"}";}
	public default String occursAtLeast(int minQuantity){return "{"+minQuantity+",}";}
	public default String butInTheSmallestAmount(){return "?";}
//COMPUTATION
	public default String oneOrOther(String...words){
		final StringBuilder result=new StringBuilder("");
		for(String word:words)result.append("|"+word);	//EX: |WORD|WORD|WORD
		return result.substring(1);				//EX: WORD|WORD|WORD
	}
//PRECEDED/FOLLOWED
	public default String precededBy(String word){return "(?<="+word+")";}
	public default String followedBy(String word){return "(?="+word+")";}
	public default String notPrecededBy(String word){return "(?<!"+word+")";}
	public default String notFollowedBy(String word){return "(?!"+word+")";}
//GROUPS
	public default String group(String group){return "("+group+")";}
	public default String namedGroup(String name,String group){return "(?<"+name+">"+group+")";}
	public default String pseudoGroup(String pseudoGroup){return "(?:"+pseudoGroup+")";}
	public default String matchOfGroup(int groupId){return "\\"+groupId;}	//EX: "(\w)a\1" == (= "WOW" && != "WOA")
//	public default String oneGroupOrOther(String possibleGroups){return "(?|"+possibleGroups+")";}
//RANGE
	public static class Range{
		private String start;
		private String end;
		public Range(String start,String end){
			this.start=start;
			this.end=end;
		}
		public Range(int start,int end){
			this.start=String.valueOf(start);
			this.end=String.valueOf(end);
		}
	@Override
		public String toString(){return start+"-"+end;}
	}
	public default Range range(String startLetter,String endLetter){
		return new Range(startLetter,endLetter);
	}
	public default Range range(int startDigit,int endDigit){
		return new Range(startDigit,endDigit);
	}
}