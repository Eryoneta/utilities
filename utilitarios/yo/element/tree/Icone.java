package element.tree;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
public class Icone{
//ICONE
	private Image icone;
		public Image getIcone(){return icone;}
		public void setIcone(Image icone){this.icone=icone;}
//LINK
	private File link;
		public String getNome(){
			return relativeLink.substring(relativeLink.lastIndexOf("/")+1,relativeLink.lastIndexOf("."));
		}
		public String getLink(){return link.toString();}
		public void setLink(File link){this.link=link;}
	private String relativeLink;
		public String getRelativeLink(){return relativeLink;}
		public void setRelativeLink(String relativeLink){this.relativeLink=relativeLink;}
//MAIN
	public Icone(Image icone,File link,String relativeLink){
		setIcone(icone);
		setLink(link);
		setRelativeLink(relativeLink);
	}
//FUNCS
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+((icone==null)?0:icone.hashCode());
		result=prime*result+((link==null)?0:link.hashCode());
		result=prime*result+((relativeLink==null)?0:relativeLink.hashCode());
		
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Icone icon=(Icone)obj;
		if(icone!=icon.icone)return false;
		if(link!=icon.link)return false;
		if(relativeLink!=icon.relativeLink)return false;
		return true;
	}
//SIZE
	public static int getSize(){return 2*Tree.UNIT;}
	public static int getRelativeSize(int unit){return 2*unit;}
//DRAW
	public void draw(Graphics2D imagemEdit,int x,int y){
		imagemEdit.drawImage(getIcone(),x,y,getSize(),getSize(),null);
	}
}