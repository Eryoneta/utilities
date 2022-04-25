package utilitarios.visual.text.custom.markdown;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
@SuppressWarnings({"serial"})
public class Markdown{
	final JFrame janela=new JFrame(){{
		setTitle("Markdown Text");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(400,400);
		setVisible(true);
	}};
	private Texto texto=new Texto();
	public static void main(String[]args){
		new Markdown();
	}
	public Markdown(){
		final JScrollPane scroll=new JScrollPane();
		scroll.setViewportView(texto);
		janela.getContentPane().add(scroll);
		set();
	}
	private void set(){
		texto.setFont(new Font("Comic Sans MS",Font.PLAIN,12));
		texto.setWrap(true);
		texto.setText("Teste1 para ver a linha1\nTeste2 para ver a linha2\nTeste3 para ver a linha3");
//		final StringBuilder dados=new StringBuilder();
//		try {
////			final String filename="C:\\Users\\CASA\\Pictures\\Gravity Falls\\Alternate Universes [AUs]\\Timestuck\\Site\\Fanfic.html";
//			final String filename="C:\\Users\\CASA\\Downloads\\DECEMBER’S WRATH.txt";
//			final BufferedReader leitor=new BufferedReader(new FileReader(filename));
//			String linha;
//			while((linha=leitor.readLine())!=null){
//				dados.append(linha+"\n");
//			}
//		}catch(IOException e){e.printStackTrace();}
//		texto.setText(dados.toString());
		System.out.println("DONE!");
	}
}