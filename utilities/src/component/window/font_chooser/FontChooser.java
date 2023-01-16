package component.window.font_chooser;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import tool.language_manager.LanguageManager;

@SuppressWarnings("serial")
public class FontChooser extends JComponent implements RootJoint<FontChooser, FontChooserRule, FontChooserFlow, FontChooserView, FontChooserPlan>{
//RESPOSTAS
	public enum Option{
		APPROVE,
		CANCEL,
		ERROR;
	}
//RULE
	private FontChooserRule rule=new FontChooserRule(this);
		@Override public FontChooserRule getRule() {return rule;}
//FLOW
	private FontChooserFlow flow=new FontChooserFlow(this);
		@Override public FontChooserFlow getFlow() {return flow;}
//VIEW
	private FontChooserView view=new FontChooserView(this);
		@Override public FontChooserView getView() {return view;}
//PLAN
	private FontChooserPlan plan=new FontChooserPlan(this);
		@Override public FontChooserPlan getPlan() {return plan;}
//MAIN
	public FontChooser() {
		Root.init(this);
	}
	public FontChooser(Font selectedFont) {
		Root.init(this);
		setSelectedFont(selectedFont);
	}
//FUNCS
	@Override public void setFont(Font font) {
		super.setFont(font);
		getView().setFont(font);
	}
	public void getLanguage(){getView().getLanguage();}
	public void setLanguage(LanguageManager lang){getView().setLanguage(lang);}
	public Option showDialog(Component janela){return getView().showDialog(janela);}
	public Font getSelectedFont(){return getPlan().getSelectedFont();}
	public void setSelectedFont(Font fonte){getPlan().setSelectedFont(fonte);}
}