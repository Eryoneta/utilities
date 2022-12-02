package element;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class Painel extends JPanel{
//JANELA(PAI)
	private Window janela;
		public Window getJanela(){return janela;}
//ELEMENTOS
	private List<Elemento>elems=new ArrayList<Elemento>();
		public List<Elemento>getElems(){return elems;}
		public void add(Elemento elem){elems.add(elem);}
		public void del(Elemento elem){elems.remove(elem);}
//MAIN
	public Painel(Window janela){this.janela=janela;}
//FUNCS
@Override
	public Point getToolTipLocation(MouseEvent m){
		return new Point(0,0);
	}
//DRAW
@Override
	protected void paintComponent(Graphics quadro){
		for(Elemento e:getElems())quadro.drawImage(e.getPrint(),e.getX(),e.getY(),e.getWidth(),e.getHeight(),null);
	}
}