package element.tree.objeto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.Nodulo;
import element.tree.objeto.modulo.Modulo;
public class ListaObjeto{
//LISTAS
	private HashMap<Integer,Objeto>listaAll=new HashMap<Integer,Objeto>();
		public HashMap<Integer,Objeto>getAll(){return listaAll;}
		public List<Objeto>getAllOrdened(){
			final List<Objeto>lista=new ArrayList<Objeto>();
			lista.addAll(listaMods);
			lista.addAll(listaCoxs);
			lista.addAll(listaNods);
			return lista;
		}
		public List<Objeto>getAllOrdenedInverted(){
			final List<Objeto>lista=new ArrayList<Objeto>();
			lista.addAll(listaNods);
			lista.addAll(listaCoxs);
			lista.addAll(listaMods);
			return lista;
		}
	private List<Modulo>listaMods=new ArrayList<Modulo>();
		public List<Modulo>getModulos(){return listaMods;}
	private List<Conexao>listaCoxs=new ArrayList<Conexao>();
		public List<Conexao>getConexoes(){return listaCoxs;}
	private List<Nodulo>listaNods=new ArrayList<Nodulo>();
		public List<Nodulo>getNodulos(){return listaNods;}
//MAIN
	public ListaObjeto(){}
	public ListaObjeto(ListaObjeto lista){
		listaAll=new HashMap<Integer,Objeto>(lista.getAll());
		listaMods=new ArrayList<Modulo>(lista.getModulos());
		listaCoxs=new ArrayList<Conexao>(lista.getConexoes());
		listaNods=new ArrayList<Nodulo>(lista.getNodulos());
	}
//FUNCS
	public boolean has(Objeto.Tipo...tipos){
		for(Objeto.Tipo tipo:tipos){
			switch(tipo){
				case MODULO:	if(!listaMods.isEmpty())return true;else break;
				case CONEXAO:	if(!listaCoxs.isEmpty())return true;else break;
				case NODULO:	if(!listaNods.isEmpty())return true;else break;
				case SEGMENTO:	break;
			}
		}
		return false;
	}
	public static ListaObjeto toList(Objeto[]objs){
		final ListaObjeto lista=new ListaObjeto();
		for(Objeto obj:objs)lista.add(obj);
		return lista;
	}
	public static ListaObjeto toList(List<Objeto>objs){
		final ListaObjeto lista=new ListaObjeto();
		for(Objeto obj:objs)lista.add(obj);
		return lista;
	}
	public boolean isEmpty(){return listaAll.isEmpty();}
	public boolean contains(Objeto obj){
		switch(obj.getTipo()){
			case MODULO:	return listaMods.contains(obj);
			case CONEXAO:	return listaCoxs.contains(obj);
			case NODULO:	return listaNods.contains(obj);
			case SEGMENTO:	break;
		}
		return false;
	}
	public void clear(){
		listaAll.clear();
		listaMods.clear();
		listaCoxs.clear();
		listaNods.clear();
	}
	public int size(){return listaAll.size();}
//ADD
	public boolean add(Objeto obj){
		if(obj==null||obj.getIndex()<0)return false;
		if(listaAll.containsValue(obj))return false;
		listaAll.put(obj.getIndex(),obj);
		switch(obj.getTipo()){
			case MODULO:
				final Modulo mod=(Modulo)obj;
				listaMods.add(mod);
				return true;
			case CONEXAO:
				final Conexao cox=(Conexao)obj;
				listaCoxs.add(cox);
				return true;
			case NODULO:
				final Nodulo nod=(Nodulo)obj;
				listaNods.add(nod);
				return true;
			case SEGMENTO:break;
		}
		return false;
	}
	public boolean add(int index,Objeto obj){
		if(obj==null||index<0)return false;
		if(listaAll.containsValue(obj))return false;
		listaAll.put(index,obj);
		switch(obj.getTipo()){
			case MODULO:
				final Modulo mod=(Modulo)obj;
				int indexMod=0;
				for(Objeto objAnt:listaAll.values()){
					if(objAnt==obj)break;
					if(objAnt.getTipo().is(Objeto.Tipo.MODULO))indexMod++;
				}
				listaMods.add(indexMod,mod);
				return true;
			case CONEXAO:
				final Conexao cox=(Conexao)obj;
				int indexCox=0;
				for(Objeto objAnt:listaAll.values()){
					if(objAnt==obj)break;
					if(objAnt.getTipo().is(Objeto.Tipo.CONEXAO))indexCox++;
				}
				listaCoxs.add(indexCox,cox);
				return true;
			case NODULO:
				final Nodulo nod=(Nodulo)obj;
				int indexNod=0;
				for(Objeto objAnt:listaAll.values()){
					if(objAnt==obj)break;
					if(objAnt.getTipo().is(Objeto.Tipo.NODULO))indexNod++;
				}
				listaNods.add(indexNod,nod);
				return true;
			case SEGMENTO:break;
		}
		return false;
	}
//DEL
	public boolean del(Objeto obj){
		if(obj==null||obj.getIndex()<0)return false;
		if(!listaAll.containsValue(obj))return false;
		listaAll.remove(obj.getIndex());
		switch(obj.getTipo()){
			case MODULO:
				final Modulo mod=(Modulo)obj;
				listaMods.remove(mod);
				return true;
			case CONEXAO:
				final Conexao cox=(Conexao)obj;
				listaCoxs.remove(cox);
				return true;
			case NODULO:
				final Nodulo nod=(Nodulo)obj;
				listaNods.remove(nod);
				return true;
			case SEGMENTO:break;
		}
		return false;
	}
}