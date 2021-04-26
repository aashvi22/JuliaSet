import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class JuliaSet extends JPanel implements AdjustmentListener 
{
	
	JFrame frame;
	
	JScrollBar aBar, bBar, multiplierBar, hueBar, zoomBar;
	JPanel scrollPanel,labelPanel,bigPanel,buttonPanel,sidePanel;
	JLabel multiplierLabel, aLabel,bLabel, hueLabel, zoomLabel;
	double a,b, multiplier, hue,zoom;
	int pixelsize;
	BufferedImage juliaImage;
	JFileChooser fileChooser;  
	JButton clear=new JButton("Clear");
	JButton save=new JButton("Save");
	JButton surprise=new JButton("Surprise!");
	JButton bookmark=new JButton("Bookmark");
	JButton bookmark1=new JButton("Bookmark 1");
	JButton bookmark2=new JButton("Bookmark 2");
	JButton bookmark3=new JButton("Bookmark 3");
	ArrayList<Julia>storedImages = new ArrayList<Julia>();
	int bookmarkcount = 0;
	public JuliaSet()
	{
		frame=new JFrame("Julia Set Program");
		frame.add(this);
		frame.setSize(1000,600);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//orientation,starting value,doodad size,min value,max value
		aBar=new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
		a=aBar.getValue();
		aBar.addAdjustmentListener(this);
		
		bBar=new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
		b=bBar.getValue();
		bBar.addAdjustmentListener(this);
		
		multiplierBar=new JScrollBar(JScrollBar.HORIZONTAL,20,0, 0,100);
		multiplier=multiplierBar.getValue()/10.0;
		multiplierBar.addAdjustmentListener(this);
		
		hueBar=new JScrollBar(JScrollBar.HORIZONTAL,10,0,0,500);
		hue=hueBar.getValue()/10.0;
		hueBar.addAdjustmentListener(this);
		
		zoomBar=new JScrollBar(JScrollBar.HORIZONTAL,100,0,0,200);
		zoom=zoomBar.getValue()/100.0;
		zoomBar.addAdjustmentListener(this);

		GridLayout grid=new GridLayout(5,1);
		aLabel=new JLabel("a");
		bLabel=new JLabel("b");
		multiplierLabel = new JLabel("m");
		hueLabel=new JLabel("Hue");
		zoomLabel=new JLabel("Zoom");

		labelPanel=new JPanel();
		labelPanel.setLayout(grid);
		labelPanel.add(aLabel);
		labelPanel.add(bLabel);
		labelPanel.add(multiplierLabel);
		labelPanel.add(hueLabel);
		labelPanel.add(zoomLabel);

		scrollPanel=new JPanel();
		scrollPanel.setLayout(grid);
		scrollPanel.add(aBar);
		scrollPanel.add(bBar);
		scrollPanel.add(multiplierBar);
		scrollPanel.add(hueBar);
		scrollPanel.add(zoomBar);
		
		bigPanel=new JPanel();
		bigPanel.setLayout(new BorderLayout());
		bigPanel.add(labelPanel,BorderLayout.WEST);
		bigPanel.add(scrollPanel,BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		save.setPreferredSize(new Dimension(75, 75));
		clear.setPreferredSize(new Dimension(75, 75));
		surprise.setPreferredSize(new Dimension(75, 75));
		bookmark.setPreferredSize(new Dimension(75, 75));
		buttonPanel.add(save);
		buttonPanel.add(clear);
		buttonPanel.add(surprise);
		buttonPanel.add(bookmark);
		bigPanel.add(buttonPanel,BorderLayout.EAST);
		
		sidePanel=new JPanel();
		//sidePanel.setLayout(new BorderLayout());
		sidePanel.setLayout(new GridLayout(3,1));
		save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            	saveImage();
	            	System.out.println("save is pressed");
            }
        });
		clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		clear();
    				System.out.println("clear is pressed");
            }
        });
		surprise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		
            		aBar.setValue((int)(Math.random()*4000-2000));
            		bBar.setValue((int)(Math.random()*4000-2000));
            		multiplierBar.setValue((int)(Math.random()*100));
            		hueBar.setValue((int)(Math.random()*500));
            		System.out.println("hue: " + hue + " m: " + multiplier + " a: " + a + " b: " + b); 
            }
        });
		bookmark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		
            		switch(bookmarkcount) {
	            		case 0: sidePanel.add(bookmark1);
	            				storedImages.add(new Julia(aBar.getValue(),bBar.getValue(), multiplierBar.getValue(), hueBar.getValue(),zoomBar.getValue()));
	            				System.out.println("case 0");
	            				sidePanel.revalidate(); 
	            				sidePanel.repaint();
	            				bookmarkcount++;
	            			break;
	            		case 1: sidePanel.add(bookmark2);
	            				storedImages.add(new Julia(aBar.getValue(),bBar.getValue(), multiplierBar.getValue(), hueBar.getValue(),zoomBar.getValue()));
	            				System.out.println("case 1");
	            				bookmarkcount++;
	            				sidePanel.revalidate(); 
	            				sidePanel.repaint(); 
	        				break;
	            		case 2: sidePanel.add(bookmark3);
	            				storedImages.add(new Julia(aBar.getValue(),bBar.getValue(), multiplierBar.getValue(), hueBar.getValue(),zoomBar.getValue()));
	            				System.out.println("case 2");
	            				bookmarkcount++;
	            				sidePanel.revalidate(); 
	            				sidePanel.repaint();
            				break;
	            		case 3: storedImages.remove(0);
	            				storedImages.add(new Julia(aBar.getValue(),bBar.getValue(), multiplierBar.getValue(), hueBar.getValue(),zoomBar.getValue()));
	            				System.out.println("case 3");
	            			break;
            			}
            		
            		//else, remove button then add new button
            }
        });
		bookmark1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		bookmarkedJuliaUpdate(storedImages.get(0));
	            	System.out.println("bookmark1 is pressed");
            }
        });
		bookmark2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		bookmarkedJuliaUpdate(storedImages.get(1));
            		System.out.println("bookmark2 is pressed");
            }
        });
		bookmark3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		bookmarkedJuliaUpdate(storedImages.get(2));
            		System.out.println("bookmark3 is pressed");
            }
        });
		frame.add(bigPanel,BorderLayout.SOUTH);
		frame.add(sidePanel,BorderLayout.EAST);

		frame.setVisible(true);
		
		String currDir=System.getProperty("user.dir");
		fileChooser=new JFileChooser(currDir);

	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(drawJulia(g),0,0,null);

	}
	public void bookmarkedJuliaUpdate(Julia j) {
		
		aBar.setValue(j.getValues()[0]);
		bBar.setValue(j.getValues()[1]);
		multiplierBar.setValue(j.getValues()[2]);
		hueBar.setValue(j.getValues()[3]);
		zoomBar.setValue(j.getValues()[4]);
	}
	public BufferedImage drawJulia(Graphics g)
	{
		
		int w = frame.getWidth();
		int h = frame.getHeight();
		juliaImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				double zx = 1.5*(x-0.5*w)/(0.5*zoom*w);
				double zy = (y-0.5*h)/(0.5*zoom*h);
				float i=300;
				while(zx*zx+zy*zy < 6 && i>0) {
					double d = zx*zx-zy*zy + a;
					zy = multiplier*zx*zy + b; //multiplier used to be 2.0
					zx= d;
					i--;
				}
				int cc;
				if(i>0)
					cc = Color.HSBtoRGB((300f*(int)hue/i % 1), 1, 1);
				else cc = Color.HSBtoRGB((300f*(int)hue/i), 1, 0);
				
				juliaImage.setRGB(x,y,cc);
			}
		}
		
		return juliaImage;
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		if(e.getSource()==aBar)
			a=aBar.getValue()/1000.0;
		if(e.getSource()==bBar)
			b=bBar.getValue()/1000.0;
		if(e.getSource()==multiplierBar)
			multiplier=multiplierBar.getValue()/10.0;
		if(e.getSource()==hueBar)
			hue=hueBar.getValue()/10.0;
		if(e.getSource()==zoomBar)
			zoom=zoomBar.getValue()/100.0;
		repaint();
	}

	public static void main(String[] args)
	{
		JuliaSet app=new JuliaSet();
	}

	public void saveImage()
	{
		if(juliaImage!=null)   
		{
			FileNameExtensionFilter filter=new FileNameExtensionFilter("*.png","png");
		        fileChooser.setFileFilter(filter);
			if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			{
				File file=fileChooser.getSelectedFile();
				try
				{
					String st=file.getAbsolutePath();
					
					if(st.indexOf(".png")>=0)
						st=st.substring(0,st.length()-4);
					ImageIO.write(juliaImage,"png",new File(st+".png"));
				}catch(IOException e)
				{
				}

			}
		}
	}
	public void clear() {
		
		aBar.setValue(0);
		bBar.setValue(0);
		multiplierBar.setValue(20);
		hueBar.setValue(10);
		zoomBar.setValue(100);
	}

}
