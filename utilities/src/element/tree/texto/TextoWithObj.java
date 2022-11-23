package element.tree.texto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import component.text.java_based.plain.PlainTexto;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.modulo.Modulo;
public class TextoWithObj{
//OBJETO
	private Objeto obj;
		public Objeto getObjeto(){return obj;}
		public void setObjeto(Objeto obj){
			triggerObjetoSetListener(this.obj,obj);
			this.obj=obj;
		}
		public void clearObjeto(){setObjeto(null);}
//LISTENER: DISPARA COM O MUDAR DE OBJETO
	private List<ObjetoSetListener>objetoSetListeners=new ArrayList<ObjetoSetListener>();
		public void addObjetoSetListener(ObjetoSetListener objetoSetListener){objetoSetListeners.add(objetoSetListener);}
		public void triggerObjetoSetListener(Objeto oldObj,Objeto newObj){
			for(ObjetoSetListener objetoSetListener:objetoSetListeners)objetoSetListener.objetoModified(oldObj,newObj);
		}
//MAIN
	public TextoWithObj(PlainTexto texto){
		texto.addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent c){
				final int caret=c.getDot();
			//UPDATE CARET DE OBJ
				if(getObjeto()!=null){
					if(getObjeto().getTipo().is(Objeto.Tipo.MODULO)){
						final Modulo mod=(Modulo)getObjeto();
						mod.setCaret(caret);
					}else if(getObjeto().getTipo().is(Objeto.Tipo.CONEXAO)){
						final Conexao cox=(Conexao)getObjeto();
						cox.setCaret(caret);
					}
				}
			}
		});
	}
}