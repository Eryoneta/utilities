package component.window.font_chooser;

import architecture.rrf_vp.rule.RuleJoint;

public class FontChooserRule implements RuleJoint<FontChooserRule, FontChooser>{
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
}