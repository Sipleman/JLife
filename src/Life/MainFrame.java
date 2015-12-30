package Life;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import java.awt.FlowLayout;
import java.awt.Button;
import javax.swing.SpringLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainFrame extends JFrame {

	private JPanel contentPane;

	int width=150;
	int height=75;
	
	private boolean[][] currentField = new boolean[width][height];
	private boolean[][] nextField = new boolean[width][height];
	boolean isPlay;
	
	private Image srcImage;
	private Graphics srcGraphics;
	
	private JPanel panel1;
	
	/**
	 * Launch the application.
	 */
	
	private boolean decide(int x,int y){
		int n=0;
		if(x>0){
			if(currentField[x-1][y]) n++;
			if(y<height-1){
				if(currentField[x][y+1]) n++;
				if(currentField[x-1][y+1]) n++;
			
		
			if(y>0){
				if(currentField[x][y-1]) n++;
				if(x<width-1){
					if(currentField[x+1][y-1]) n++;
					if(currentField[x+1][y]) n++;
					if(currentField[x+1][y+1]) n++;
				}
				if(currentField[x-1][y-1]) n++;
			}
			}
		}
		if(n==3) return true;
		if(n==2 && currentField[x][y]) return true;
		
		return false;
	}
	private void redraw(){
		
		srcGraphics.setColor(panel1.getBackground());
		srcGraphics.fillRect(0, 0, panel1.getWidth(), panel1.getHeight());
		
		srcGraphics.setColor(Color.BLACK);
		
		for(int x=1;x<panel1.getWidth();x++){
			
			srcGraphics.drawLine(x*panel1.getWidth()/width, 0, x*panel1.getWidth()/width, panel1.getHeight());
		}
		for(int y=1;y<panel1.getHeight();y++){
			srcGraphics.drawLine(0, y*panel1.getHeight()/height, panel1.getWidth(), y*panel1.getHeight()/height);
		}
		srcGraphics.setColor(Color.BLUE);
		for(int i=0;i<currentField.length;i++){
			for(int j=0;j<currentField[i].length;j++){
				if(currentField[i][j]){
					int x=i*width/panel1.getWidth();
					int y=j*height/panel1.getHeight();
					srcGraphics.fillRect(i*panel1.getWidth()/width, j*panel1.getHeight()/height, panel1.getWidth()/width, panel1.getHeight()/height);
				}
			}
		}
		panel1.getGraphics().drawImage(srcImage, 0, 0, this);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				srcImage = createImage(panel1.getWidth(), panel1.getHeight());
		        srcGraphics = srcImage.getGraphics();
		        redraw();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 341, 248);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				mouseDraggedHandler(arg0);
			}
		});
		panel.setBackground(Color.LIGHT_GRAY);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				redraw();
			}
		});
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!isPlay)
					isPlay=true;
				else isPlay=false;
			}
		});
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isPlay=false;
				for(int i=0;i<currentField.length;i++)
					for(int j=0;j<currentField[i].length;j++){
						currentField[i][j]=false;
					}
				nextField=currentField;
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnStart)
					.addPreferredGap(ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
					.addComponent(btnReset))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addGap(25)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnStart)
						.addComponent(btnReset)))
		);
		contentPane.setLayout(gl_contentPane);
		
		panel1 = panel;
		
		//super.addNotify();
		
		pack();
		
		srcImage = panel.createImage(panel.getWidth(),panel.getHeight());
		srcGraphics = srcImage.getGraphics();
		
		Timer time = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(isPlay){
					for(int i=0;i<height;i++){
						for(int j=0;j<width;j++){
							nextField[j][i]=decide(j,i);
						}
					}
					currentField=nextField;
				}
				redraw();

			}
		};

		time.scheduleAtFixedRate(task, 0, 100);
		redraw();
		
	}
	private void mouseDraggedHandler(MouseEvent ev){
		int y = ev.getY();
		y=y*height/panel1.getHeight();
		int x = ev.getX()*width/panel1.getWidth();
		if(y>=0&&x>=0){
			currentField[x][y]=true;
		}
		else currentField[x][y]=false;

		redraw();
	}
}
