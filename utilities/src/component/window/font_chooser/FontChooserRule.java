package component.window.font_chooser;

import javax.swing.JList;
import javax.swing.text.Position;

import architecture.rrf_vp.rule.RuleJoint;

public class FontChooserRule implements RuleJoint<FontChooserRule, FontChooser>{
//SIZE_LIMIT
	public static int MIN_FONTSIZE=1;
	public static int MAX_FONTSIZE=80;
//ROOT
	private FontChooser root;
		@Override public FontChooser getRoot() {return root;}
//MAIN
	public FontChooserRule(FontChooser root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
	public boolean isFontNameValid(String fontName) {
		final JList<String>lista=getRoot().getView().getNomeLista();
		final int index=lista.getNextMatch(fontName,0,Position.Bias.Forward);
		if(index==-1)return false;						//DEVE PERTENCER A LISTA
		final String listedFontName=lista.getModel().getElementAt(index).toString();
		return fontName.equals(listedFontName);			//DEVE SER IGUAL A UM ITEM DA LISTA
	}
	public boolean isFontStyleValid(String fontStyle) {
		final JList<String>lista=getRoot().getView().getEstiloLista();
		final int index=lista.getNextMatch(fontStyle,0,Position.Bias.Forward);
		if(index==-1)return false;						//DEVE PERTENCER A LISTA
		final String listedFontStyle=lista.getModel().getElementAt(index).toString();
		return fontStyle.equals(listedFontStyle);		//DEVE SER IGUAL A UM ITEM DA LISTA
	}
	public boolean isFontSizeValid(int fontSize) {
		return fontSize>=FontChooserRule.MIN_FONTSIZE && fontSize<=FontChooserRule.MAX_FONTSIZE;
	}
}