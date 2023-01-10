package component.window.font_chooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import architecture.rrf_vp.view.ViewJoint;
import tool.language_manager.LanguageManager;
import component.window.font_chooser.FontChooser.Option;

@SuppressWarnings("serial")
public class FontChooserView implements ViewJoint<FontChooserView, FontChooser>{
//VAR GLOBAIS
	private static final String[]FONT_OPTIONS=FontChooserPlan.NAMES;
	private static String[]STYLE_OPTIONS=new String[]{
			"Plain",
			"Bold",
			"Italic",
			"Bold and Italic"
	};
	private static final String[]SIZE_OPTIONS=FontChooserPlan.SIZES;
	private static final String SAMPLE_TEXT="AaBbYyZz";
//LANGUAGE
	private LanguageManager lang=new LanguageManager();
		public LanguageManager getLanguage() {return lang;}
		public void setLanguage(LanguageManager lang) {
			this.lang=lang;
			STYLE_OPTIONS=new String[]{
					getLanguage().get("M_Menu_C_F_S_R","Plain"),
					getLanguage().get("M_Menu_C_F_S_B","Bold"),
					getLanguage().get("M_Menu_C_F_S_I","Italic"),
					getLanguage().get("M_Menu_C_F_S_BI","Bold and Italic")
			};
		}
//NAME_TEXT
	private JTextField nomeTexto;
		private void generateNameText() {
			nomeTexto=new JTextField(){{
				addFocusListener(getRoot().getPlan().new TextoFocusAction(this));
				setFont(getRoot().getFont());
			}};
			nomeTexto.addKeyListener(getRoot().getPlan().new TextoKeyAction(getNomeLista()));
			nomeTexto.getDocument().addDocumentListener(getRoot().getPlan().new ListDocumentAction(getNomeLista()));
		}
		public JTextField getNomeTexto(){
			if(nomeTexto==null)generateNameText();
			return nomeTexto;
		}
//NAME_LIST
	private JList<String> nomeLista;
		private void generateNameList() {
			nomeLista=new JList<String>(FONT_OPTIONS){{
				setCellRenderer(new ListCellRenderer<String>(){
					public Component getListCellRendererComponent(JList<? extends String> list,String value,int index,boolean isSelected,boolean cellHasFocus){
						return new JLabel(){
							private static final long serialVersionUID = 1L;
						{
							setText(value.toString());
							setFont(new Font(value.toString(),Font.PLAIN,12));
							setOpaque(true);
							if(isSelected){
								setBackground(new Color(0,120,215));
								setForeground(Color.WHITE);
							}else{
								setBackground(Color.WHITE);
								setForeground(Color.BLACK);
							}
						}};
					}
				});
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				setFont(getRoot().getFont());
				setFocusable(false);
			}};
			nomeLista.addListSelectionListener(getRoot().getPlan().new ListSelectionAction(getNomeTexto()));
			nomeLista.setSelectedIndex(0);
		}
		public JList<String> getNomeLista(){
			if(nomeLista==null)generateNameList();
			return nomeLista;
		}
		public String getSelectedNome(){
			return (String)getNomeLista().getSelectedValue();
		}
		public void setSelectedNome(String name){
			final String[]nomes=FontChooserView.FONT_OPTIONS;
			for (int i=0;i<nomes.length;i++){
				if(nomes[i].toLowerCase().equals(name.toLowerCase())){
					getNomeLista().setSelectedIndex(i);
					break;
				}
			}
			updateExemplo();
		}
//NAME_PANE
	private JPanel nomePainel;
		private void generateNamePane() {
			nomePainel=new JPanel(){{
				setLayout(new BorderLayout());
				setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				setPreferredSize(new Dimension(180,130));
				add(new JLabel(getLanguage().get("M_Menu_C_F_N","Name")){{
					setHorizontalAlignment(JLabel.LEFT);
					setHorizontalTextPosition(JLabel.LEFT);
					setLabelFor(getNomeTexto());
					setDisplayedMnemonic('F');
				}},BorderLayout.NORTH);
				add(new JPanel(){{
					setLayout(new BorderLayout());
					add(getNomeTexto(),BorderLayout.NORTH);
					add(new JScrollPane(getNomeLista()){{
						getVerticalScrollBar().setFocusable(false);
						setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					}},BorderLayout.CENTER);
				}},BorderLayout.CENTER);
			}};
		}
		protected JPanel getNomePainel(){
			if(nomePainel==null)generateNamePane();
			return nomePainel;
		}
//STYLE_TEXT
	private JTextField estiloTexto;
		private void generateStyleText() {
			estiloTexto=new JTextField(){{
				addFocusListener(getRoot().getPlan().new TextoFocusAction(this));
				setFont(getRoot().getFont());
			}};
			estiloTexto.addKeyListener(getRoot().getPlan().new TextoKeyAction(getEstiloLista()));
			estiloTexto.getDocument().addDocumentListener(getRoot().getPlan().new ListDocumentAction(getEstiloLista()));
		}
		public JTextField getEstiloTexto(){
			if(estiloTexto==null)generateStyleText();
			return estiloTexto;
		}
//STYLE_LIST
	private JList<String> estiloLista;
		private void generateStyleList() {
			estiloLista=new JList<String>(FontChooserView.STYLE_OPTIONS){{
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				setFont(getRoot().getFont());
				setFocusable(false);
			}};
			estiloLista.addListSelectionListener(getRoot().getPlan().new ListSelectionAction(getEstiloTexto()));
			estiloLista.setSelectedIndex(0);
		}
		public JList<String> getEstiloLista(){
			if(estiloLista==null)generateStyleList();
			return estiloLista;
		}
		public int getSelectedEstilo(){
			return FontChooserPlan.STYLES[getEstiloLista().getSelectedIndex()];
		}
		public void setSelectedEstilo(int estilo){
			for(int i=0;i<FontChooserPlan.STYLES.length;i++){
				if(FontChooserPlan.STYLES[i]==estilo){
					getEstiloLista().setSelectedIndex(i);
					break;
				}
			}
			updateExemplo();
		}
//STYLE_PANE
	private JPanel estiloPainel;
		private void generateStylePane(){
			estiloPainel=new JPanel(){{
				setLayout(new BorderLayout());
				setPreferredSize(new Dimension(140,130));
				setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				add(new JLabel(getLanguage().get("M_Menu_C_F_S","Style:")){{
					setHorizontalAlignment(JLabel.LEFT);
					setHorizontalTextPosition(JLabel.LEFT);
					setLabelFor(getEstiloTexto());
					setDisplayedMnemonic('Y');
				}},BorderLayout.NORTH);
				add(new JPanel(){{
					setLayout(new BorderLayout());
					add(getEstiloTexto(),BorderLayout.NORTH);
					add(new JScrollPane(getEstiloLista()){{
						getVerticalScrollBar().setFocusable(false);
						setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					}},BorderLayout.CENTER);
				}},BorderLayout.CENTER);
			}};
		}
		protected JPanel getEstiloPainel(){
			if(estiloPainel==null)generateStylePane();
			return estiloPainel;
		}
//SIZE_TEXT
	private JTextField tamanhoTexto;
		private void generateSizeText() {
			tamanhoTexto=new JTextField(){{
				addFocusListener(getRoot().getPlan().new TextoFocusAction(this));
				setFont(getRoot().getFont());
			}};
			tamanhoTexto.addKeyListener(getRoot().getPlan().new TextoKeyAction(getTamanhoLista()));
			tamanhoTexto.getDocument().addDocumentListener(getRoot().getPlan().new ListDocumentAction(getTamanhoLista()));
		
			
		}
		public JTextField getTamanhoTexto(){
			if(tamanhoTexto==null)generateSizeText();
			return tamanhoTexto;
		}
//SIZE_LIST
	private JList<String> tamanhoLista;
		private void generateSizeList() {
			tamanhoLista=new JList<String>(FontChooserView.SIZE_OPTIONS){{
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				setFont(getRoot().getFont());
				setFocusable(false);
			}};
			tamanhoLista.addListSelectionListener(getRoot().getPlan().new ListSelectionAction(getTamanhoTexto()));
			tamanhoLista.setSelectedIndex(0);
		}
		public JList<String> getTamanhoLista(){
			if(tamanhoLista==null)generateSizeList();
			return tamanhoLista;
		}
		public int getSelectedTamanho(){
			int fontSize=1;
			String fontSizeString=getTamanhoTexto().getText();
			while(true){	//TODO: PARA QUE SERVE???
				try{
					fontSize=Integer.parseInt(fontSizeString);
					break;
				}catch(NumberFormatException erro){
					fontSizeString=(String)getTamanhoLista().getSelectedValue();
					getTamanhoTexto().setText(fontSizeString);
				}
			}
			return fontSize;
		}
		public void setSelectedTamanho(int size){
			final String sizeString=String.valueOf(size);
			for (int i=0;i<FontChooserView.SIZE_OPTIONS.length;i++){
				if (FontChooserView.SIZE_OPTIONS[i].equals(sizeString)){
					getTamanhoLista().setSelectedIndex(i);
					break;
				}
			}
			getTamanhoTexto().setText(sizeString);
			updateExemplo();
		}
//SIZE_PANE
	private JPanel tamanhoPainel;
		private void generateSizepane() {
			tamanhoPainel=new JPanel(){{
				setLayout(new BorderLayout());
				setPreferredSize(new Dimension(70,130));
				setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				add(new JLabel(getLanguage().get("M_Menu_C_F_T","Size:")){{
					setHorizontalAlignment(JLabel.LEFT);
					setHorizontalTextPosition(JLabel.LEFT);
					setLabelFor(getTamanhoTexto());
					setDisplayedMnemonic('S');
				}},BorderLayout.NORTH);
				add(new JPanel(){{
					setLayout(new BorderLayout());
					add(getTamanhoTexto(),BorderLayout.NORTH);
					add(new JScrollPane(getTamanhoLista()){{
						getVerticalScrollBar().setFocusable(false);
						setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					}},BorderLayout.CENTER);
				}},BorderLayout.CENTER);
			}};
		}
		protected JPanel getTamanhoPainel(){
			if(tamanhoPainel==null)generateSizepane();
			return tamanhoPainel;
		}
//SAMPLE_TEXT
	private JTextField exemploTexto;
		private void generateExampleText() {
			exemploTexto=new JTextField(FontChooserView.SAMPLE_TEXT){{
				final Border lowered=BorderFactory.createLoweredBevelBorder();
				setBorder(lowered);
				setPreferredSize(new Dimension(300,100));
			}};
		}
		protected JTextField getSampleTextField(){
			if(exemploTexto==null)generateExampleText();
			return exemploTexto;
		}
//SAMPLE_PANE
	private JPanel exemploPainel;
		private void generateExamplePane() {
			exemploPainel=new JPanel(){{
				setLayout(new BorderLayout());
				final Border titledBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						getLanguage().get("M_Menu_C_F_E","Example"));
				final Border empty=BorderFactory.createEmptyBorder(5,10,10,10);
				final Border border=BorderFactory.createCompoundBorder(titledBorder,empty);
				setBorder(border);
				add(getSampleTextField(),BorderLayout.CENTER);
			}};
		}
		protected JPanel getExemploPainel(){
			if(exemploPainel==null)generateExamplePane();
			return exemploPainel;
		}
		protected void updateExemplo(){
			getSampleTextField().setFont(getRoot().getPlan().getSelectedFont());
		}
//ROOT
	private FontChooser root;
		@Override public FontChooser getRoot() {return root;}
//MAIN
	public FontChooserView(FontChooser root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {
		getRoot().setFont(new Font("Dialog",Font.PLAIN,10));
		getRoot().setLayout(new BoxLayout(getRoot(),BoxLayout.X_AXIS));
		getRoot().add(new JPanel(){{					//PAINEL DO FUNDO
			setLayout(new GridLayout(2,1));
			add(new JPanel(){{								//PAINEL DAS SELEÇÕES
				setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
				add(getNomePainel());							//NOMES
				add(getEstiloPainel());							//ESTILOS
				add(getTamanhoPainel());						//TAMANHOS
			}},BorderLayout.NORTH);
			add(getExemploPainel(),BorderLayout.CENTER);	//EXEMPLO
		}});
		getRoot().setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		getRoot().getPlan().setSelectedFont(FontChooserPlan.DEFAULT_FONT);
	}
	protected void setFont(Font font) {
		getNomeTexto().setFont(font);
		getNomeTexto().setFont(font);
		getNomeLista().setFont(font);
		getEstiloTexto().setFont(font);
		getEstiloLista().setFont(font);
		getTamanhoTexto().setFont(font);
		getTamanhoLista().setFont(font);
	}
	public Option showDialog(Component janela){
		getRoot().getPlan().resposta=Option.ERROR;
		final JDialog dialog=createDialog(janela);
		dialog.addWindowListener(getRoot().getPlan().new WindowClosingAction());
		dialog.setVisible(true);
		dialog.dispose();
		return getRoot().getPlan().resposta;
	}
	private JDialog createDialog(Component janela){
		Frame frame=null;
		if(janela!=null) {
			frame=(janela instanceof Frame?(Frame)SwingUtilities.getAncestorOfClass(Frame.class,janela):(Frame)janela);
		}
		final JDialog dialog=new JDialog(frame,getLanguage().get("M_Menu_C_F_SF","Select Font"),true);
		dialog.getContentPane().add(getRoot(),BorderLayout.CENTER);
		dialog.getContentPane().add(new JPanel(){{
			setLayout(new BorderLayout());
			add(new JPanel(){{
				setLayout(new GridLayout(2,1));
				final ActionMap actionMap=getActionMap();
				final InputMap inputMap=getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
				final Action okAction=getRoot().getPlan().new OKButtonAction(dialog,getLanguage().get("M_Menu_C_F_Ok","OK"));			//ATALHO DE OK
				actionMap.put(okAction.getValue(Action.DEFAULT),okAction);
				inputMap.put(KeyStroke.getKeyStroke("ENTER"),okAction.getValue(Action.DEFAULT));
				add(new JButton(okAction){{				//OK
					setFont(getRoot().getFont());
				}});
				final Action cancelAction=getRoot().getPlan().new CancelButtonAction(dialog,getLanguage().get("M_Menu_C_F_Cl","Cancel"));	//ATALHO DE CANCEL
				actionMap.put(cancelAction.getValue(Action.DEFAULT),cancelAction);
				inputMap.put(KeyStroke.getKeyStroke("ESCAPE"),cancelAction.getValue(Action.DEFAULT));
				add(new JButton(cancelAction){{			//CANCEL
					setFont(getRoot().getFont());
				}});
				setBorder(BorderFactory.createEmptyBorder(25,0,10,10));
			}},BorderLayout.NORTH);
		}},BorderLayout.EAST);
		dialog.pack();
		dialog.setLocationRelativeTo(frame);
		dialog.setMinimumSize(dialog.getSize());
		return dialog;
	}
}