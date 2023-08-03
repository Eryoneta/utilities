package component.text.from_scratch.plain;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
public class Texto{
	private BufferedImage moldura=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
	private JFrame janela;
	private Rectangle size=new Rectangle(0,0,0,0);
	private int maiorLinha=1;
	private int textoSize=1;
	private Font fonte=new Font("Courier New",Font.PLAIN,12);
	private Dimension fonteSize=new Dimension(moldura.getGraphics().getFontMetrics(fonte).charWidth(' '),fonte.getSize());
	private Dimension letraTela=new Dimension(0,0);
	
	private String nome="";
	private String field="";
	private boolean foco=true;
	private boolean visible=true;
	private boolean bloqueado=false;
	private boolean linhaQuebra=false;
	private boolean antialias=false;
	private int linhaEspaco=2;
	private Rectangle selecao=new Rectangle(0,0,0,0);
	private Dimension scroll=new Dimension(0,0);
	private int blinkRate=5;
	private boolean cursorVisible=true;
	private String ctrlLimE="",ctrlLimD="";
	private Runnable paste=new Runnable(){public void run(){try{
		insert(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
	}catch(HeadlessException|UnsupportedFlavorException|IOException e){}}};

	private Color corCursor=new Color(0,0,0);
	private Color corSelecao=new Color(0,120,215);
	private Color corDestaque=new Color(232,242,254);
	private Color corDesfoco=new Color(255,255,255);
	private Color corTexto=new Color(0,0,0);
	private Color corFundo=new Color(255,255,255);
	private Color corSearch=new Color(240,216,168);
	
	private int scrollWidth=20;
	private Color corScroll=new Color(205,205,205);
	private Color corTrilho=new Color(240,240,240);

	private Dimension scrollPress=new Dimension(0,0);
	private Rectangle scrollH=new Rectangle(0,0,0,0),scrollW=new Rectangle(0,0,0,0);
	private Rectangle botaoPress=new Rectangle(0,0,0,0);
	private Rectangle botaoC=new Rectangle(0,0,0,0),botaoB=new Rectangle(0,0,0,0),botaoD=new Rectangle(0,0,0,0),botaoE=new Rectangle(0,0,0,0);
	
	private List<String>texto=new ArrayList<String>();
	private List<Destaque>destaques=new ArrayList<Destaque>();
	private List<DestaqueSelecao>realces=new ArrayList<DestaqueSelecao>();

	private List<String>undo=new ArrayList<String>();
	private List<String>redo=new ArrayList<String>();
	private int doLimite=50;
	
	boolean shift=false,ctrl=false;

	public Texto(JFrame janela){
		this.janela=janela;
		texto.add("");
		janela.addWindowListener(new WindowListener(){//DETECTA JANELA
			public void windowOpened(WindowEvent w){}//AO ABRIR
			public void windowIconified(WindowEvent w){}//AO COLOCAR ÍCONE
			public void windowDeiconified(WindowEvent w){}//AO TIRAR ÍCONE
			public void windowClosing(WindowEvent w){}//AO FECHANDO
			public void windowClosed(WindowEvent w){}//AO FECHADO
			public void windowActivated(WindowEvent w){//AO FOCAR
				foco=true;
				if(!bloqueado)blink();
			}
			public void windowDeactivated(WindowEvent w){//AO DESFOCAR
				foco=ctrl=shift=false;
				if(!bloqueado)update();
			}
		});
		janela.addComponentListener(new ComponentAdapter(){//DETECTA AÇÃO
			public void componentResized(ComponentEvent r){//AO REDIMENCIONAR
				try{Thread.sleep(10);}catch(InterruptedException erro){}
				if(!bloqueado)scroll(0);
				if(!bloqueado)blink();
			}
		});
		janela.addMouseWheelListener(new MouseWheelListener(){//DETECTA SCROLL
			public void mouseWheelMoved(MouseWheelEvent w){//AO RODAR
				if(!bloqueado)scroll(w.getWheelRotation()>0?1:-1);
			}
		});
		janela.addKeyListener(new KeyAdapter(){//DETECTA TECLA
			public void keyPressed(KeyEvent k){//AO PRESSIONAR
				if(!bloqueado)if(k.getKeyCode()==KeyEvent.VK_SHIFT)shift=true;else if(k.getKeyCode()==KeyEvent.VK_CONTROL)ctrl=true;else comandos(k);
			}
			public void keyReleased(KeyEvent k){//AO SOLTAR
				if(bloqueado)return;
				if(k.getKeyCode()==KeyEvent.VK_SHIFT)shift=false;else if(k.getKeyCode()==KeyEvent.VK_CONTROL)ctrl=false;
				if(k.getKeyCode()==KeyEvent.VK_CONTROL||k.getKeyCode()==KeyEvent.VK_F){
					searched=false;
					for(int i=0,size=destaques.size();i<size;i++)if(destaques.get(i).pintaFundo){destaques.remove(i--);size--;}
				}
				update();
			}
		});
		janela.addMouseMotionListener(new MouseAdapter(){//DETECTA MOVIMENTO DO MOUSE
			Dimension OldLinhaScroll=new Dimension(0,0);
			Dimension OldSselecao=new Dimension(0,0);
			public void mouseDragged(MouseEvent m){//AO ARRASTAR
				if(bloqueado)return;
				final int press=m.getModifiersEx(),mousePress=MouseEvent.BUTTON1_DOWN_MASK;
				if(press==mousePress||press==mousePress+64||press==mousePress+128){//DRAG, DRAG COM SHIFT, DRAG COM CTRL+F
					if(scrollPress.height!=0||scrollPress.width!=0){
						OldLinhaScroll.height=scroll.height;
						OldLinhaScroll.width=scroll.width;
						if(scrollPress.height!=0){
							scrollH.y=Math.max(0,Math.min(size.height-scrollH.height,m.getPoint().y-scrollPress.height));
							scroll.height=Math.max(0,Math.min(textoSize-letraTela.height,(scrollH.y*textoSize)/(size.height-(scrollH.height<=20?20:0))));
						}else if(scrollPress.width!=0){
							scrollW.x=Math.max(0,Math.min(size.width-scrollW.width,m.getPoint().x-scrollPress.width));
							scroll.width=Math.max(0,Math.min(maiorLinha-letraTela.width,(scrollW.x*maiorLinha)/(size.width-(scrollW.width<=20?20:0))));
						}
						if(!OldLinhaScroll.equals(scroll))update();
					}else if(!IF(1,botaoPress.x,botaoPress.y,botaoPress.width,botaoPress.height))
						if((m.getX()-size.x<scrollH.x||m.getX()-size.x>scrollH.x+scrollH.width)&&(m.getY()-size.y<scrollW.y||m.getY()-size.y>scrollW.y+scrollW.height)){
							OldSselecao.width=Math.min(selecao.y,selecao.height);
							OldSselecao.height=Math.max(selecao.y,selecao.height);
							setCursor(m.getPoint());
							blink();
						}
				}
			}
		});
		janela.addMouseListener(new MouseAdapter(){//DETECTA MOUSE
			Date pressedTime;
			public void mousePressed(MouseEvent m){//AO PRESSIONAR
				if(bloqueado)return;
				pressedTime=new Date();
				if(m.getButton()==1){
					if(scrollH.contains(m.getX()-size.x,m.getY()-size.y))scrollPress.height=m.getPoint().y-scrollH.y;
					else if(scrollW.contains(m.getX()-size.x,m.getY()-size.y))scrollPress.width=m.getPoint().x-scrollW.x;
					else if(botaoC.contains(m.getX()-size.x,m.getY()-size.y))scroll.height=Math.max(0,scroll.height-(botaoPress.x=1));
					else if(botaoB.contains(m.getX()-size.x,m.getY()-size.y))scroll.height=Math.min(textoSize-letraTela.height,scroll.height+(botaoPress.y=1));
					else if(botaoD.contains(m.getX()-size.x,m.getY()-size.y))scroll.width=Math.min(maiorLinha-letraTela.width,scroll.width+(botaoPress.width=1)+2);
					else if(botaoE.contains(m.getX()-size.x,m.getY()-size.y))scroll.width=Math.max(0,scroll.width-(botaoPress.height=1)-2);
					else if(m.getX()-size.x>=scrollH.x){
						scrollH.y=Math.max(0,Math.min(size.height-scrollH.height,m.getY()-size.y-scrollPress.height));
						scroll.height=Math.max(0,Math.min(textoSize-letraTela.height,(scrollH.y*textoSize)/(size.height-(scrollH.height<=20?20:0))));
					}else if(m.getY()-size.y>=scrollW.y){
						scrollW.x=Math.max(0,Math.min(size.width-scrollW.width,m.getX()-size.x-scrollPress.width));
						scroll.width=Math.max(0,Math.min(maiorLinha-letraTela.width,(scrollW.x*maiorLinha)/(size.width-(scrollW.width<=20?20:0))));
					}else if(size.contains(m.getPoint())){
						setCursor(m.getPoint());
						if(!shift)selecao.height=selecao.y;
						if(!shift)selecao.width=selecao.x;
					}
					blink();
					if(botaoPress.x==1||botaoPress.y==1||botaoPress.width==1||botaoPress.height==1){
						Thread run=new Thread(new Runnable(){
							public void run(){
								try{Thread.sleep(300);}catch(InterruptedException erro){}
								if(new Date().getTime()-pressedTime.getTime()<=300)return;
								while(botaoPress.x==1||botaoPress.y==1||botaoPress.width==1||botaoPress.height==1){
									if(botaoPress.x==1)scroll.height=Math.max(0,scroll.height-1);
										else if(botaoPress.y==1)scroll.height=Math.min(textoSize-letraTela.height,scroll.height+1);
											else if(botaoPress.width==1)scroll.width=Math.min(maiorLinha-letraTela.width,scroll.width+3);
												else if(botaoPress.height==1)scroll.width=Math.max(0,scroll.width-3);
									blink();
									try{Thread.sleep(40);}catch(InterruptedException erro){}
								}
							}
						});
						run.start();
					}
				}else if(m.getButton()==3)ctrl=true;
			}
			public void mouseReleased(MouseEvent m){//AO SOLTAR
				if(bloqueado)return;
				scrollPress.height=scrollPress.width=0;
				botaoPress.x=botaoPress.y=botaoPress.width=botaoPress.height=0;
				update();
				pressedTime=new Date();
				if(m.getButton()==3)ctrl=false;
			}
			public void mouseClicked(MouseEvent m){//AO SOLTAR
				if(bloqueado)return;
				if(m.getButton()==1&&m.getClickCount()==2){
					String linha=texto.get(selecao.y);
					if(ctrl){
						Matcher searchEnd=Pattern.compile(ctrlLimD).matcher(linha.substring(selecao.x,linha.length()));
						Matcher searchStart=Pattern.compile(ctrlLimE).matcher(new StringBuilder(linha.substring(0,selecao.x)).reverse());
						boolean startFind=searchStart.find(),endFind=searchEnd.find();
						if(startFind&&endFind){selecao.width-=searchStart.start();selecao.x+=searchEnd.start();}
							else if(startFind){selecao.width-=searchStart.start();selecao.x=linha.length();}
								else if(endFind){selecao.width=0;selecao.x+=searchEnd.start();}
					}else{
						Matcher searchEnd=Pattern.compile("[^a-zA-Z0-9À-ÿ]").matcher(linha.substring(selecao.x,linha.length()));
						Matcher searchStart=Pattern.compile("[^a-zA-Z0-9À-ÿ]").matcher(new StringBuilder(linha.substring(0,selecao.x)).reverse());
						boolean startFind=searchStart.find(),endFind=searchEnd.find();
						if(startFind&&endFind){selecao.width-=searchStart.start();selecao.x+=searchEnd.start();}
							else if(startFind){selecao.width-=searchStart.start();selecao.x=linha.length();}
								else if(endFind){selecao.width=0;selecao.x+=searchEnd.start();}
									else{selecao.width=0;selecao.x=linha.length();}
					}
					viewMouse();
					blink();
				}
			}
		});
	}
	public void setPosicao(int x,int y){size.x=x;size.y=y;}
	public void setSize(int width,int height){
		size.width=Math.max(0,width-size.x-scrollWidth);
		size.height=Math.max(0,height-size.y-scrollWidth);
		moldura=new BufferedImage(width-size.x,height-size.y,BufferedImage.TYPE_INT_RGB);
		scrollH.x=size.width;
		scrollH.y=scrollWidth;
		scrollW.x=scrollWidth;
		scrollW.y=size.height;
		scrollH.width=scrollW.height=scrollWidth;
		botaoC.x=botaoB.x=size.width;
		botaoC.y=0;
		botaoB.y=size.height-scrollWidth;
		botaoE.x=0;
		botaoD.x=size.width-scrollWidth;
		botaoE.y=botaoD.y=size.height;
		botaoC.width=botaoC.height=botaoB.width=botaoB.height=botaoD.height=botaoD.width=botaoE.height=botaoE.width=scrollWidth;
		setLetraTela();
	}
	public void setBounds(int x,int y,int width,int height){setPosicao(x,y);setSize(width,height);}
	public void setText(List<String>texto){
		this.texto=texto;
		for(String linha:texto)maiorLinha=Math.max(linha.length(),maiorLinha);
		textoSize=texto.size();
		QuebraTexto(linhaQuebra);
		update();
	}
	public void setCorBackground(Color cor){corFundo=cor;}
	public void setCorForeground(Color cor){corTexto=cor;}
	public void setCorCursor(Color cor){corCursor=cor;}
	public void setCorSelecao(Color cor){corSelecao=cor;}
	public void setCorDestaque(Color cor){corDestaque=cor;}
	public void setCorSearch(Color cor){corSearch=cor;}
	public void setCorDesfoco(Color cor){corDesfoco=cor;}
	public void setCorTrilho(Color cor){corTrilho=cor;}
	public void setCorScroll(Color cor){corScroll=cor;}
	public void setScrollWidth(int scrollWidth){this.scrollWidth=scrollWidth;setSize(size.width+size.x,size.height+size.y);}
	public void setFont(Font fonte){this.fonte=fonte;fonteSize=new Dimension(moldura.getGraphics().getFontMetrics(fonte).charWidth(' '),fonte.getSize());setLetraTela();}
	public void setSpaceLine(int linhaEspaco){this.linhaEspaco=linhaEspaco;setLetraTela();}
	public void setCaret(int linha,int coluna){selecao.y=selecao.height=linha;selecao.x=selecao.width=coluna;viewMouse();}
	public void setSelecao(int linha1,int coluna1,int linha2,int coluna2){selecao.y=linha1;selecao.x=coluna1;selecao.height=linha2;selecao.width=coluna2;}
	public void setLineBreak(boolean linhaQuebra){this.linhaQuebra=linhaQuebra;QuebraTexto(linhaQuebra);}
	public void setEnabled(boolean bloqueado){this.bloqueado=!bloqueado;}
	public void setAntialias(boolean antialias){this.antialias=antialias;}
	public void setCtrlSelecaoLim(String ctrlLimE,String ctrlLimD){this.ctrlLimE=ctrlLimE;this.ctrlLimD=ctrlLimD;}
	public void setDoLimite(int doLimite){this.doLimite=doLimite;}
	public void addFontBackground(String palavraIni,String palavraFim,Color cor){destaques.add(new Destaque(palavraIni,palavraFim,cor,true));}
	public void addFontBackground(String palavraIni,Color cor){addFontBackground(palavraIni,"",cor);}
	public void addFontForeground(String palavraIni,String palavraFim,Color cor){destaques.add(new Destaque(palavraIni,palavraFim,cor,false));}
	public void addFontForeground(String palavraIni,Color cor){addFontForeground(palavraIni,"",cor);}
	public void addDestaque(int linha,int coluna1,int coluna2,Color cor){realces.add(new DestaqueSelecao(linha,coluna1,coluna2,cor));}
	public void limparRedo(){undo.clear();redo.clear();}
	public int size(){return(textoSize==1&&texto.get(0).isEmpty()?0:textoSize);}
	public List<String>getText(){return texto;}
	public void setVisible(boolean visible){this.visible=visible;bloqueado=!visible;update();}
	public boolean isVisible(){return visible;}
	public void setName(String nome){this.nome=nome;}
	public String getName(){return nome;}
	public void setField(String nome){this.field=nome;}
	public String getField(){return field;}
	public int getX(){return size.x;}
	public int getY(){return size.y;}
	public int getWidth(){return size.width;}
	public int getHeight(){return size.height;}
	public Point getCaret(){return new Point(selecao.x,selecao.y);}
	public void limparDestaques(){realces.clear();}
	private boolean searched=false;
	private void comandos(KeyEvent k){
		int tecla=k.getKeyCode();
		boolean ctrl=k.isControlDown(),shift=k.isShiftDown(),alt=k.isAltDown();
		if(!ctrl&&!alt&&tecla==KeyEvent.VK_UP){//SETA CIMA
			selecao.y=Math.max(0,selecao.y-1);
			int tam=texto.get(selecao.y).length();
			if(selecao.x>tam)selecao.x=tam;
			if(!shift){selecao.height=selecao.y;selecao.width=selecao.x;}
		}
		if(!ctrl&&!alt&&tecla==KeyEvent.VK_DOWN){//SETA BAIXO
			selecao.y=Math.min(textoSize-1,selecao.y+1);
			int tam=texto.get(selecao.y).length();
			if(selecao.x>tam)selecao.x=tam;
			if(!shift){selecao.height=selecao.y;selecao.width=selecao.x;}
		}
		if(!alt&&tecla==KeyEvent.VK_RIGHT){//SETA DIREITA
			if(selecao.y!=textoSize-1||selecao.x!=texto.get(textoSize-1).length())selecao.x=selecao.x+1;
			if(selecao.y<textoSize-1&&selecao.x>=texto.get(selecao.y).length()+1){selecao.y=Math.min(textoSize-1,selecao.y+1);selecao.x=0;}
			if(ctrl){
				String linha=texto.get(selecao.y);
				Matcher searchEnd=Pattern.compile("[^a-zA-Z0-9À-ÿ]").matcher(linha.substring(Math.max(0,selecao.x-1),linha.length()));
				if(searchEnd.find())selecao.x+=(searchEnd.start()>0?searchEnd.start()-1:0);else selecao.x=texto.get(selecao.y).length();
			}
			if(!shift){selecao.height=selecao.y;selecao.width=selecao.x;}
		}
		if(!alt&&tecla==KeyEvent.VK_LEFT){//SETA ESQUERDA
			if(selecao.y!=0||selecao.x!=0)selecao.x=selecao.x-1;
			if(selecao.y>0&&selecao.x<0){selecao.y=Math.max(0,selecao.y-1);selecao.x=texto.get(selecao.y).length();}
			if(ctrl){
				String linha=texto.get(selecao.y);
				Matcher searchStart=Pattern.compile("[^a-zA-Z0-9À-ÿ]").matcher(new StringBuilder(linha.substring(0,selecao.x+1)).reverse());
				if(searchStart.find())selecao.x-=(searchStart.start()>0?searchStart.start()-1:0);else selecao.x=0;
			}
			if(!shift){selecao.height=selecao.y;selecao.width=selecao.x;}
		}
		if(!alt&&tecla==KeyEvent.VK_HOME){//HOME
			if(ctrl)selecao.y=0;
			selecao.x=0;
			if(!shift){selecao.height=selecao.y;selecao.width=selecao.x;}
		}
		if(!alt&&tecla==KeyEvent.VK_END){//END
			if(ctrl)selecao.y=textoSize-1;
			selecao.x=texto.get(selecao.y).length();
			if(!shift){selecao.height=selecao.y;selecao.width=selecao.x;}
		}
		if(!ctrl&&!alt&&tecla==KeyEvent.VK_PAGE_UP)scroll.height=Math.max(0,scroll.height-letraTela.height);//PAGE UP
		if(!ctrl&&!alt&&tecla==KeyEvent.VK_PAGE_DOWN)scroll.height=Math.min(textoSize-letraTela.height,scroll.height+letraTela.height);//PAGE DOWN
		if(!ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_BACK_SPACE){if(acento.isEmpty())delete();else acento="";redo.clear();}//BACKSPACE
		if(!ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_DELETE){//DELETE
			if(selecao.y!=textoSize-1||selecao.x!=texto.get(textoSize-1).length())selecao.x=selecao.x+1;
			if(selecao.y<textoSize-1&&selecao.x>=texto.get(selecao.y).length()){selecao.y=Math.min(textoSize-1,selecao.y+1);selecao.x=0;}
			if(!((selecao.x==selecao.width&&selecao.y==selecao.height)&&selecao.y==size()-1&&selecao.x==texto.get(selecao.y).length()))delete();
			redo.clear();
		}
		if(!ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_ENTER){insert("\n");redo.clear();}//ENTER
		if(!ctrl&&!alt&&(!IF(tecla,8,10,16,17,18,19,20,27,33,34,35,36,37,38,39,40,112,113,114,115,116,117,118,119,120,121,122,123,127,155,524,525))||k.getKeyChar()=='/'||k.getKeyChar()=='?'){
			//GET_CHAR NÃO CARREGA MAIS ACENTOS...?
			//GAMBIARRA
			String key="";
			switch (k.getKeyCode()) {
				case KeyEvent.VK_6:	//54
					if(k.getModifiersEx()==KeyEvent.SHIFT_DOWN_MASK) {
						key="¨";
					}else key="6";
				break;
				case KeyEvent.VK_DEAD_ACUTE:	//129
					if(k.getModifiersEx()==KeyEvent.SHIFT_DOWN_MASK) {
						key="`";
					}else key="´";
				break;
				case KeyEvent.VK_DEAD_TILDE:	//131
					if(k.getModifiersEx()==KeyEvent.SHIFT_DOWN_MASK) {
						key="^";
					}else key="~";
				break;
				default:
					key=Character.toString(k.getKeyChar());
				break;
			}
			insert(key);//LETRA
			//GAMBIARRA - FIM
			
//			insert(Character.toString(k.getKeyChar()));//LETRA
			redo.clear();
		}
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_X){copy();if(selecao.x!=selecao.width||selecao.y!=selecao.height)delete();}//CTRL+X
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_C)copy();//CTRL+C
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_V)paste();//CTRL+V
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_A)selectAll();//CTRL+A
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_D){//CTRL+D
			int x=selecao.x;
			if(selecao.x!=selecao.width||selecao.y!=selecao.height)
				setSelecao(Math.min(selecao.y,selecao.height),0,Math.max(selecao.y,selecao.height),texto.get(Math.max(selecao.y,selecao.height)).length());
			else setSelecao(selecao.y,0,selecao.y,texto.get(selecao.y).length());
			delete();
			delete();
			int y=Math.min(textoSize-1,selecao.y+1);
			setCaret(y,Math.min(getText().get(y).length(),x));
			redo.clear();
		}
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_F&&!searched){//CTRL+F
			searched=true;
			search();
			this.ctrl=false;
		}
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_Z)desfazer();//CTRL+Z
		if(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_Y)refazer();//CTRL+Y
		if(!(ctrl&&!shift&&!alt&&tecla==KeyEvent.VK_F)&&tecla!=KeyEvent.VK_PAGE_UP&&tecla!=KeyEvent.VK_PAGE_DOWN)viewMouse();
		blink();
	}
	private static boolean IF(int x,int...y){//(VALOR A COMPARAR, VALORES...)
		boolean igual=false;//VERIFICADOR
		for(int yy:y)if(x==yy)igual=true;//if(x==y1||x==y2||x==y3...);
		return igual;//RETORNO
	}
	public void clear(){for(int i=0;i<textoSize&&textoSize>1;)if(getText().get(i).isEmpty()){setCaret(Math.min(textoSize-1,i+1),0);delete();}else i++;}//LIMPA LINHA VAZIA
	public void selectAll(){selecao.width=selecao.height=0;selecao.y=textoSize-1;selecao.x=texto.get(selecao.y).length();}//SELECIONA TUDO
	public void search(){String selecao=getSelecao();if(selecao.isEmpty())return;addFontBackground(selecao,corSearch);}//DESTACA PESQUISA
	public void replaceSelecao(String texto){if(selecao.x!=selecao.width||selecao.y!=selecao.height)delete();insert(texto);}//SUBSTITÚI AREA SELECIONADA
	private int match(String letra,String[]lista){int index=-1;for(int i=0;i<lista.length;i++)if(letra.equals(lista[i])){index=i;break;}return index;}
	private boolean E(String s1,String s2){return s1.equals(s2);}
	private String acento="";
	public void insert(String letras){//INSERE TEXTO^
		if(selecao.x!=selecao.width||selecao.y!=selecao.height)delete();
		if(letras.length()==1&&(letras.equals("´")||letras.equals("`")||letras.equals("~")||letras.equals("^")||letras.equals("¨"))){
			acento=letras;
			return;
		}else if(!acento.isEmpty())if(letras.length()==1){
			String[][]acentuadosL=new String[][]{
				new String[]{"á","é","í","ó","ú","Á","É","Í","Ó","Ú"},
				new String[]{"à","è","ì","ò","ù","À","È","Ì","Ò","Ù"},
				new String[]{"â","ê","î","ô","û","Â","Ê","Î","Ô","Û"},
				new String[]{"ä","ë","ï","ï","ü","Ä","Ë","Ï","Ö","Ü"}
			};
			String[]letrasL=new String[]{"a","e","i","o","u","A","E","I","O","U"},acentosL=new String[]{"´","`","^","¨"};
			int a=match(acento,acentosL),l=match(letras,letrasL);
			if(a>=0&&l>=0)letras=acentuadosL[a][l];else{
				letras=(E(acento,"~")?(E(letras,"a")?"ã":(E(letras,"o")?"õ":(E(letras,"A")?"Ã":(E(letras,"O")?"Õ":(E(letras,"n")?"ñ":(E(letras,"N")?"Ñ":acento+letras)))))
					):E(acento,"´")?(E(letras,"y")?"ý":E(letras,"Y")?"Ý":acento+letras
						):E(acento,"¨")?(E(letras,"y")?"ÿ":acento+letras
							):acento+letras);
			}
			acento="";
		}
		if(letras.contains("\n")){//COLAR COM ENTER
			String linha=texto.get(selecao.y);
			int enterIndex=letras.indexOf("\n");
			int tam=linha.length();//TAMANHO DA LINHA
			int index=selecao.y+1;
			char[]buffer1=new char[selecao.x+enterIndex];
			linha.getChars(0,selecao.x,buffer1,0);//COMEÇO DA LINHA
			letras.getChars(0,enterIndex,buffer1,selecao.x);//FRASE ATÉ ENTER
			texto.set(selecao.y,new String(buffer1));
			for(int i=selecao.y+1;(enterIndex=letras.indexOf("\n",enterIndex+1))!=-1;i++,index++,textoSize++){
				int enterIndexAnt=letras.lastIndexOf("\n",enterIndex-1);
				char[]buffer=new char[enterIndex-enterIndexAnt-1];
				letras.getChars(enterIndexAnt+1,enterIndex,buffer,0);
				texto.add(i,new String(buffer));
			}
			int tam2=letras.length();//TAMANHO DA LINHA
			enterIndex=letras.lastIndexOf("\n",tam2);
			char[]buffer2=new char[tam-selecao.x+(tam2-enterIndex)-1];
			letras.getChars(enterIndex+1,tam2,buffer2,0);//FRASE ATÉ ENTER
			linha.getChars(selecao.x,tam,buffer2,tam2-enterIndex-1);//RESTO DA LINHA
			texto.set(selecao.y,new String(buffer1));
			texto.add(index,new String(buffer2));
			textoSize++;
			setUndo("del",selecao.x,selecao.y,tam2-enterIndex-1,index,"");
			selecao.y=selecao.height=index;
			selecao.x=selecao.width=tam2-enterIndex-1;
		}else{//NORMAL
			int conteudotTam=letras.length();//TAMANHO DO TEXTO
			String linha=texto.get(selecao.y);
			int tam=linha.length();//TAMANHO DA LINHA
			if(selecao.x==0)texto.set(selecao.y,letras.concat(linha));else if(selecao.x==tam)texto.set(selecao.y,linha.concat(letras));else{
				char[]buffer=new char[tam+conteudotTam];
				linha.getChars(0,selecao.x,buffer,0);//COMEÇO DA LINHA
				letras.getChars(0,conteudotTam,buffer,selecao.x);//FRASE
				linha.getChars(selecao.x,tam,buffer,selecao.x+conteudotTam);//RESTO DA LINHA
				texto.set(selecao.y,new String(buffer));
			}
			selecao.x=(selecao.width+=conteudotTam);
			setUndo("del",selecao.x-conteudotTam,selecao.y,selecao.width,selecao.height,"");
		}
		viewMouse();
	}
	public void delete(){//DELETA TEXTO
		if(selecao.y<=0&&selecao.x<=0&&selecao.height<=0&&selecao.width<=0)return;
		int minY=Math.min(selecao.y,selecao.height),maxY=Math.max(selecao.y,selecao.height);
		if(minY==maxY){//MESMA LINHA
			int minX=Math.min(selecao.x,selecao.width),maxX=Math.max(selecao.x,selecao.width);
			boolean igual=(minX==maxX);//BACKSPACE
			if(igual&&minX==0){//SE COMEÇO DE LINHA
				String linha1=texto.get(minY-1),linha2=texto.get(minY);
				int tam1=linha1.length(),tam2=linha2.length();
				char[]buffer=new char[tam1+tam2];
				linha1.getChars(0,tam1,buffer,0);//COMEÇO DA LINHA
				linha2.getChars(0,tam2,buffer,tam1);//FIM DA LINHA
				texto.set(minY-1,new String(buffer));
				texto.remove(minY);
				textoSize--;
				selecao.x=selecao.width=tam1;
				selecao.y=selecao.height=minY-1;
				setUndo("ins",selecao.x,selecao.y,selecao.width,selecao.height,"\n");
			}else{//MEIO DA LINHA
				String linha=texto.get(minY);
				int tam=linha.length();
				char[]buffer=new char[tam-(igual?1:maxX-minX)];
				linha.getChars(0,minX-(igual?1:0),buffer,0);//COMEÇO DA LINHA
				linha.getChars(maxX,tam,buffer,minX-(igual?1:0));//FIM DA LINHA
				if(igual)selecao.x=Math.max(0,selecao.x-1);
				setUndo("ins",minX-(igual?1:0),minY,minX-(igual?1:0),minY,getSelecao());
				texto.set(minY,new String(buffer));
				selecao.x=selecao.width=minX-(igual?1:0);
				selecao.y=selecao.height=minY;
			}
		}else{//LINHAS DIFERENTES
			int minX=(minY==selecao.y?selecao.x:selecao.width),maxX=(maxY==selecao.y?selecao.x:selecao.width);
			String linha1=texto.get(minY),linha2=texto.get(maxY);
			int tam2=linha2.length();
			char[]buffer=new char[minX+(tam2-maxX)];
			linha1.getChars(0,minX,buffer,0);//COMEÇO DA LINHA
			linha2.getChars(maxX,tam2,buffer,minX);//FIM DA LINHA
			setUndo("ins",minX,minY,minX,minY,getSelecao());
			texto.set(minY,new String(buffer));
			for(int l=0;l<maxY-minY;texto.remove(minY+1),textoSize--,l++);
			selecao.x=selecao.width=minX;
			selecao.y=selecao.height=minY;
		}
		viewMouse();
		scroll(0);
	}
	public String getSelecao(){
		String trecho="";
		int minY=Math.min(selecao.y,selecao.height),maxY=Math.max(selecao.y,selecao.height),minX=Math.min(selecao.x,selecao.width),maxX=Math.max(selecao.x,selecao.width);
		if(minY==maxY)trecho=texto.get(minY).substring(minX,maxX);else for(int i=minY;i<=maxY;i++)
			trecho+=(i==minY?texto.get(i).substring(minY==selecao.y?selecao.x:selecao.width):
				(i==maxY?texto.get(i).substring(0,maxY==selecao.y?selecao.x:selecao.width):texto.get(i)))+(i==maxY?"":"\n");
		return trecho;
	}
	public void copy(){String trecho=getSelecao();if(!trecho.isEmpty())Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(trecho),null);}//COPIAR
	public void paste(){if(selecao.x!=selecao.width||selecao.y!=selecao.height)delete();paste.run();}//COLAR
	public void desfazer(){
		if(undo.isEmpty())return;
		String[]comando=undo.get(0).split("\\]\\|\\[");
		setSelecao(Integer.parseInt(comando[2]),Integer.parseInt(comando[1]),Integer.parseInt(comando[4]),Integer.parseInt(comando[3]));
		if(comando[0].equals("del")){
			setRedo("ins",selecao.x,selecao.y,selecao.x,selecao.y,getSelecao());
			delete();
		}else if(comando[0].equals("ins")){
			int x=selecao.x,y=selecao.y;
			insert(comando[5]);
			setRedo("del",x,y,selecao.x,selecao.y,"");
		}
		undo.remove(0);//REMOVE AÇÃO DO DESFAZER
		undo.remove(0);//REMOVE O DESFAZER
	}
	public void refazer(){
		if(redo.isEmpty())return;
		String[]comando=redo.get(0).split("\\]\\|\\[");
		setSelecao(Integer.parseInt(comando[2]),Integer.parseInt(comando[1]),Integer.parseInt(comando[4]),Integer.parseInt(comando[3]));
		if(comando[0].equals("del"))delete();else if(comando[0].equals("ins"))insert(comando[5]);
		redo.remove(0);
	}
	public void limparTexto(){
		selectAll();
		delete();
		scroll.width=scroll.height=0;
		textoSize=maiorLinha=1;
		update();
	}
	public void setCustomPaste(Runnable paste){this.paste=paste;}
	private void setUndo(String comm,int x1,int y1,int x2,int y2,String letras){
		if(undo.size()>doLimite)undo.remove(doLimite);
		undo.add(0,comm+"]|["+x1+"]|["+y1+"]|["+x2+"]|["+y2+"]|["+letras);
	}
	private void setRedo(String comm,int x1,int y1,int x2,int y2,String letras){
		if(redo.size()>doLimite)redo.remove(doLimite);
		redo.add(0,comm+"]|["+x1+"]|["+y1+"]|["+x2+"]|["+y2+"]|["+letras);
	}
	private void viewMouse(){
		if(selecao.y<scroll.height)scroll.height=selecao.y;else if(selecao.y>scroll.height+(letraTela.height-1))scroll.height=selecao.y-(letraTela.height-1);
		if(selecao.x<scroll.width)scroll.width=selecao.x;else if(selecao.x>scroll.width+letraTela.width)scroll.width=selecao.x-letraTela.width;
	}
	private void setLetraTela(){
		letraTela.width=(size.width/fonteSize.width);
		letraTela.height=(size.height/(fonteSize.height+linhaEspaco));
	}
	public void update(){
		if(!visible)return;
		Graphics2D quadro=(Graphics2D)moldura.getGraphics();
		quadro.setRenderingHint(RenderingHints.KEY_ANTIALIASING,antialias?RenderingHints.VALUE_ANTIALIAS_ON:RenderingHints.VALUE_ANTIALIAS_OFF);
		int minY=Math.min(selecao.y,selecao.height),maxY=Math.max(selecao.y,selecao.height),minX=Math.min(selecao.x,selecao.width),maxX=Math.max(selecao.x,selecao.width);
		boolean selecionado=(selecao.x!=selecao.width||selecao.y!=selecao.height);
		int fonteHeight=fonteSize.height+linhaEspaco;
		quadro.setColor(corFundo);
		quadro.fillRect(0,0,size.width,linhaEspaco);//MÁSCARA DE CIMA
		quadro.setColor(corSelecao);
		if(selecionado)if(minY==scroll.height)quadro.fillRect(
			(minY==maxY?minX:minY==selecao.y?selecao.x:selecao.width)*fonteSize.width,0,
			((maxY==minY?maxX:Math.min(texto.get(minY).length(),letraTela.width))-(minY==maxY?minX:minY==selecao.y?selecao.x:selecao.width))*fonteSize.width,linhaEspaco
		);else if(minY<scroll.height)quadro.fillRect(0,0,size.width,linhaEspaco);//LINHA SELECIONADA DE CIMA
		quadro.setFont(fonte);
		for(int i=scroll.height,linha=fonteSize.height;i<textoSize&&i<scroll.height+letraTela.height&&linha<size.height;i++,linha+=fonteHeight){//LINHA
			quadro.setColor(i==selecao.y&&foco?corDestaque:corFundo);
			quadro.fillRect(0,linha-(fonteSize.height-linhaEspaco),size.width,fonteHeight);//LINHA DESTACADA
			quadro.setColor(i>=minY&&i<=maxY?corSelecao:corFundo);
			String texto=this.texto.get(i);
			if(linhaQuebra)texto=texto.replace("\r","");
			int tam=texto.length();
			maiorLinha=Math.max(tam,maiorLinha);
			if(selecionado)quadro.fillRect(
				(i==minY?Math.max(0,(selecao.y==selecao.height?minX:(selecao.y<selecao.height?selecao.x:selecao.width))-scroll.width)*fonteSize.width:0),
				linha-(fonteSize.height-linhaEspaco),
				(i==maxY?Math.min(letraTela.width+1,(selecao.y==selecao.height?maxX-Math.max(minX,scroll.width):(selecao.y>selecao.height?selecao.x:selecao.width))
					-(i==minY?0:scroll.width))*fonteSize.width:Math.max(0,tam-(i==minY?(minY==selecao.y?selecao.x:selecao.width):scroll.width))*fonteSize.width),
				fonteHeight
			);//LINHA SELECIONADA
			if(!realces.isEmpty())for(int r=0,size=realces.size();r<size;r++)if(realces.get(r).linha==i){//REALÇE DE TEXTO
				quadro.setColor(realces.get(r).cor);
				int x=realces.get(r).coluna1-scroll.width,width=realces.get(r).coluna2-realces.get(r).coluna1;
				quadro.fillRect(x*fonteSize.width,linha-(fonteSize.height-linhaEspaco),width*fonteSize.width,fonteHeight);
			}
			quadro.setColor(corTexto);
			quadro.drawString(texto.substring(Math.min(tam,scroll.width),Math.min(tam,scroll.width+(size.width/fonteSize.width)+1)),0,linha);//TEXTO
			if(!destaques.isEmpty())for(int d=0,size=destaques.size();d<size;d++){//DESTAQUE DE FUNDO OU PALAVRA
				String wordIni=destaques.get(d).wordIni,wordFim=destaques.get(d).wordFim;
				for(int index=-1;(index=texto.indexOf(wordIni,index+1))!=-1&&tam-scroll.width>0;){
					quadro.setColor(destaques.get(d).cor);
					int destaqueTam=(wordFim==""?wordIni.length():(texto.indexOf(wordFim,index+1)-index)+wordFim.length());
					if(destaqueTam<0)break;
					if(index+destaqueTam-scroll.width>=0&&index-scroll.width<=scroll.width+letraTela.width){
						if(destaques.get(d).pintaFundo){
							quadro.fillRect((index-scroll.width)*fonteSize.width,linha-(fonteSize.height-linhaEspaco),destaqueTam*fonteSize.width,fonteHeight);//LINHA SELECIONADA
							quadro.setColor(corTexto);
						}
						quadro.drawString(texto.substring(Math.max(scroll.width,index),index+destaqueTam),Math.max(0,(index-scroll.width)*fonteSize.width),linha);//TEXTO
					}
				}
			}
		}
		quadro.setColor(foco?corCursor:corDesfoco);//COR DO MOUSE OU DO DESFOCO
		if(foco)if(cursorVisible)
			quadro.fillRect((selecao.x-scroll.width//ERROR!
					-(texto.get(selecao.y).startsWith("\r")?1:0))*fonteSize.width,((selecao.y-scroll.height)*fonteHeight)+linhaEspaco,2,fonteHeight);//MOUSE
		else;else quadro.fillRect(0,0,size.width,size.height);//DESFOCO
		int posicao=fonteHeight*Math.min(textoSize,letraTela.height);
		quadro.setColor(corFundo);
		quadro.fillRect(0,posicao,size.width,size.height-posicao);//MÁSCARA DE BAIXO
		quadro.setColor(corSelecao);
		if(selecionado)if(maxY==scroll.height+letraTela.height-1)quadro.fillRect(
			(minY==maxY?minX:0)*fonteSize.width,posicao,
			((maxY==minY?maxX-minX:Math.min(maxY==selecao.y?selecao.x:selecao.width,letraTela.width)))*fonteSize.width,size.height-posicao
		);else if(minY<scroll.height)quadro.fillRect(0,posicao,size.width,size.height-posicao);//LINHA SELECIONADA DE CIMA
		scrollH.height=Math.max(((size.height-(2*scrollWidth))*letraTela.height)/textoSize,20);
		scrollH.y=scrollWidth+((scroll.height*(size.height-(2*scrollWidth)-(scrollH.height<=20?20:0)))/textoSize);
		scrollW.width=Math.max(((size.width-(2*scrollWidth))*letraTela.width)/maiorLinha,20);
		scrollW.x=scrollWidth+((scroll.width*(size.width-(2*scrollWidth)-(scrollW.width<=20?20:0)))/maiorLinha);
		quadro.setColor(corTrilho);
		quadro.fillRect(size.width,0,scrollWidth,size.height+scrollWidth);//TRACK HEIGHT
		quadro.fillRect(0,size.height,size.width,scrollWidth);//TRACK WIDTH
		quadro.setColor(corScroll);
		if(maiorLinha>letraTela.width)quadro.fillRect(scrollW.x,scrollW.y,scrollW.width+1,scrollW.height);//SCROLL WIDTH
		if(textoSize>letraTela.height)quadro.fillRect(scrollH.x,scrollH.y,scrollH.width,scrollH.height+1);//SCROLL HEIGHT
		drawBotao(quadro,botaoC,botaoPress.x==1,135);
		drawBotao(quadro,botaoB,botaoPress.y==1,315);
		drawBotao(quadro,botaoD,botaoPress.width==1,225);
		drawBotao(quadro,botaoE,botaoPress.height==1,45);
		janela.getGraphics().drawImage(moldura,size.x,size.y,janela);
	}
	private void drawBotao(Graphics2D quadro,Rectangle botao,boolean botaoPress,int angle){
		quadro.setColor(botaoPress?corScroll:corTrilho);
		quadro.fillRect(botao.x,botao.y,botao.width,botao.height);//BOTÃO HEIGHT DOWN
		int fonteSize=scrollWidth-(scrollWidth/3);
		quadro.setFont(new Font(Font.MONOSPACED,Font.PLAIN,fonteSize));
		quadro.setColor(botaoPress?corTrilho:corScroll);
		int ajusteX=(angle==135?(fonteSize/2)+(scrollWidth/4):angle==315?(fonteSize/2)+(scrollWidth/7):angle==225?fonteSize+(scrollWidth/5):angle==45?(scrollWidth/5):0);
		int ajusteY=(angle==135?fonteSize+(scrollWidth/5):angle==315?(scrollWidth/5):angle==225?(fonteSize/2)+(scrollWidth/8):angle==45?(fonteSize/2)+(scrollWidth/4):0);
		drawRotate(quadro,botao.x+ajusteX,botao.y+scrollWidth-ajusteY,angle,"◣");
	}
	private void drawRotate(Graphics2D quadro,int x,int y,int angle,String text){
		quadro.translate(x,y);
		quadro.rotate(Math.toRadians(angle));
		quadro.drawString(text,0,0);
		quadro.rotate(-Math.toRadians(angle));
		quadro.translate(-x,-y);
	} 
	private void setCursor(Point mouse){
		selecao.y=Math.max(0,((mouse.y-size.y-linhaEspaco)/(fonteSize.height+linhaEspaco))+scroll.height);//ALINHA Y
		selecao.x=Math.max(0,((mouse.x-size.x+(fonteSize.width/2))/fonteSize.width)+scroll.width);//ALINHA X
		int linhaWidth=texto.get(Math.min(selecao.y,textoSize-1)).length()//ERROR!
				-(texto.get(Math.min(selecao.y,textoSize-1)).startsWith("\r")?1:0);//TAMANHO DA LINHA
		if(selecao.x>linhaWidth)selecao.x=linhaWidth;//CURSOR NO FIM DA LINHA
		if(selecao.y>textoSize-1){//CURSOR NO FIM DO TEXTO
			selecao.y=textoSize-1;
			selecao.x=linhaWidth;
		}
		viewMouse();
	}
	private void scroll(int direcao){
		int speedH=3,speedW=5;
		Dimension OldLinhaScroll=new Dimension(scroll.width,scroll.height);
		if(ctrl)scroll.width=Math.max(0,(scroll.width+letraTela.width+direcao*speedW>=maiorLinha&&direcao>=0?maiorLinha-letraTela.width:scroll.width+direcao*speedW));
		else scroll.height=Math.max(0,(scroll.height+letraTela.height+direcao*speedH>=textoSize&&direcao>=0?textoSize-letraTela.height:scroll.height+direcao*speedH));
		if(!OldLinhaScroll.equals(scroll))update();
	}
	private Date blinkTime=new Date(0);
	private void blink(){
		blinkTime=new Date();
		cursorVisible=true;
		Thread run=new Thread(new Runnable(){
			public void run(){
				Date blinkTimeNow=blinkTime;
				for(int i=0,l=(blinkRate*2);i<=l;i++){
					cursorVisible=(i%2==0);
					update();
					try{Thread.sleep(500);}catch(InterruptedException erro){}
					if(blinkTime.equals(new Date(0))||!blinkTimeNow.equals(blinkTime))break;
				}
			}
		});
		run.start();
	}
	private void QuebraTexto(boolean quebra){
		if((quebra&&maiorLinha<letraTela.width)||(!quebra&&maiorLinha>letraTela.width+1))return;
		Point oldMouse=getCaret();
		int height=scroll.height;
		boolean alinhado=false;
		for(int index=0;index<textoSize;index++){
			String linha=texto.get(selecao.y=selecao.height=index);
			if(quebra)if(linha.length()>letraTela.width){
				selecao.x=selecao.width=linha.lastIndexOf(" ",letraTela.width)+1;
				if(selecao.x==0)selecao.x=selecao.width=letraTela.width;
				if(oldMouse.y==selecao.y&&oldMouse.x>selecao.x){oldMouse.x-=selecao.x-1;oldMouse.y++;}
				insert("\n\r");
			}else;else if(linha.startsWith("\r")){
				selecao.x=0;
				selecao.width=1;
				if(oldMouse.y==selecao.y&&oldMouse.x<letraTela.width)alinhado=true;
				delete();
				delete();
				if(selecao.y<oldMouse.y)oldMouse.y--;
				if(alinhado){oldMouse.x+=selecao.x-1;alinhado=false;}
				index--;
			}
		}
		setCaret(oldMouse.y,oldMouse.x);
		if(quebra){maiorLinha=letraTela.width;scroll.width=0;}
		scroll.height=height;
	}
}
class Destaque{
	String wordIni,wordFim;Color cor;boolean pintaFundo;
	public Destaque(String wordIni,String wordFim,Color cor,boolean pintaFundo){this.wordIni=wordIni;this.wordFim=wordFim;this.cor=cor;this.pintaFundo=pintaFundo;}
}
class DestaqueSelecao{
	int linha,coluna1,coluna2;Color cor;
	public DestaqueSelecao(int linha,int coluna1,int coluna2,Color cor){this.linha=linha;this.coluna1=coluna1;this.coluna2=coluna2;this.cor=cor;}
}