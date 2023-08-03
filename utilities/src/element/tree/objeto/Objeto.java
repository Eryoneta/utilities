package element.tree.objeto;

import java.awt.Point;

public abstract class Objeto{
//TIPO
	public enum Tipo{
		MODULO,
		CONEXAO,
		NODULO,
		SEGMENTO;
		public boolean is(Objeto.Tipo... tipos){
			for(Objeto.Tipo tipo:tipos)if(this.equals(tipo))return true;
			return false;
		}
	}
	protected Objeto.Tipo tipo;
		public Objeto.Tipo getTipo(){return tipo;}
//ID
	protected String id="";
		public String getId(){return id;}
		public void setId(String id){this.id=id;}
//INDEX
	protected int index=0;
		public int getIndex(){return index;}
		public void setIndex(int index){this.index=index;}
//MAIN
	public Objeto(Objeto.Tipo tipo){
		this.tipo=tipo;
	}
//FUNCS
	public abstract boolean contains(Point mouse);
	@Override public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime*result+((tipo==null)?0:tipo.hashCode());	//TIPO
		result=prime*result+index;								//INDEX
		return result;
	}
	@Override public boolean equals(Object objeto){
		if(this==objeto)return true;
		if(objeto==null)return false;
		if(getClass()!=objeto.getClass())return false;
		final Objeto obj=(Objeto)objeto;
		if(tipo!=obj.tipo)return false;							//TIPO
		if(index!=obj.index)return false;						//INDEX
		return true;
	}
}