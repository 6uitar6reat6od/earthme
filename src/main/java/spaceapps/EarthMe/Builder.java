package spaceapps.EarthMe;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.imgscalr.Scalr;

import com.jhlabs.image.MaskFilter;

public class Builder {
    
    
//	public static void main(String[] args) throws IOException{
//		new Builder().build(new File("gato2.jpg"),new File("results"));
//	}
	
	/**
	 * Generates the mosaic and saves it into results
	 * @param image input image
	 * @param results output dir
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void build(File image, File results) throws IOException, FileNotFoundException{

		try{
			ImageInputStream is = ImageIO.createImageInputStream(image);
			Iterator iter = ImageIO.getImageReaders(is);
			
			if (!iter.hasNext())
			{
				UtilRGB.log.log(Level.SEVERE,"Cannot load the specified file "+ image);
				System.exit(1);
			}
			ImageReader imageReader = (ImageReader)iter.next();
			imageReader.setInput(is);
			
			BufferedImage biImage = imageReader.read(0);
			
			// Setting the area to explore
			int area = biImage.getHeight()*biImage.getWidth();
			if(area<=67600)
				EarthMeApp.edge=2;
			else if(area<=210000)
				EarthMeApp.edge=3;
			else
				EarthMeApp.edge=5;
			
			//A list with the colors we already got
			ArrayList availableColors = new ArrayList(); 
			String[] dummy = EarthMeApp.SOURCE_FOLDER.list();
			Collections.addAll(availableColors, dummy); 
			
			int output[][] = searchColors(biImage, availableColors);
			
			BufferedImage bi = generateMatrix(output);
			
			UtilRGB.log.log(Level.INFO,"Superpose");
			/*
			 * SUPERPOSICION, mejora el efecto¿?¿?
			 */
			int w = bi.getWidth();
			int h = bi.getHeight();
//        bi = Scalr.resize(bi, Scalr.Mode.FIT_TO_HEIGHT, w, h, Scalr.OP_ANTIALIAS);
			biImage = Scalr.resize(biImage, Scalr.Mode.FIT_TO_HEIGHT, w, h, Scalr.OP_ANTIALIAS);
			BufferedImage temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
			
			Graphics2D g = temp.createGraphics();
			g.drawImage(bi, null, 0, 0);
			g.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_OVER, (float) 0.3));
			g.drawImage(biImage, null, 0, 0);
			
			g.dispose ();
			
			UtilRGB.log.log(Level.INFO,"Saving image");
			
			ImageIO.write(temp, "jpg", new File(results,image.getName()));
			
			UtilRGB.log.log(Level.INFO,"Saved new image on "+results);
		}
		catch(FileNotFoundException e){
			throw e;
		}
	}    
	
	/**
	 * Using params declared on {@link EarthMeApp} looks over [bi] for the colors needed
	 * to generate the mosaic saving these into a matrix, in case the color
	 * isn't listed on [colors] it's saved as it's negative value to be treated
	 * later on
	 * @param bi the image to analyze
	 * @param colors a list with the available colors
	 * @return a matrix with the colors needed
	 */
	@SuppressWarnings("rawtypes")
	private int[][] searchColors(BufferedImage bi, ArrayList colors){

		/*
		 * offsets in case the image input isn't a multiple of the edge to split
		 */
       	int offsetx = 0;
       	if(bi.getWidth()%EarthMeApp.edge!=0)
       		offsetx = 1;
       	
       	int offsety = 0;
       	if(bi.getHeight()%EarthMeApp.edge!=0)
       		offsety = 1;
       	
       	int output[][] = new int[bi.getWidth()/EarthMeApp.edge+offsetx][bi.getHeight()/EarthMeApp.edge+offsety];
       	
    	for(int y = 0; y<bi.getHeight(); y+=EarthMeApp.edge){
            for(int x = 0; x<bi.getWidth(); x+=EarthMeApp.edge){
            	int w,h;
        		if(x+EarthMeApp.edge>bi.getWidth()) w = bi.getWidth()-x;
        		else w = EarthMeApp.edge;
        		if(y+EarthMeApp.edge>bi.getHeight()) h = bi.getHeight()-y;
        		else h = EarthMeApp.edge;
        		int rgb = UtilRGB.getMostCommonColourBI(bi.getSubimage(x, y, w, h));
        		rgb = UtilRGB.intRGBto256(rgb);
        		
        		if(colors.contains(""+rgb)) {
        			output[x/EarthMeApp.edge][y/EarthMeApp.edge] = rgb;
        		}
        		else
        			output[x/EarthMeApp.edge][y/EarthMeApp.edge] = -1*rgb;
        	}
        }
    	
    	return output;
	}
	
	/**
	 * Generates the BufferedImage with the mosaic based on the colors on
	 * rgb[][] the color must be negative if it doesn't exist on the source dir
	 * @param rgb matrix with the colors
	 * @return final BufferedImage with the mosaic on it
	 * @throws IOException
	 */
	private BufferedImage generateMatrix(int rgb[][]) throws IOException{
		ArrayList<Integer> treated = new ArrayList<Integer>();
		BufferedImage r = new BufferedImage((int) rgb.length*EarthMeApp.SCALE, (int) rgb[0].length*EarthMeApp.SCALE, BufferedImage.TYPE_INT_RGB);
		BufferedImage dummy = null;
		for(int i = 0; i<rgb.length; i++){
			UtilRGB.log.log(Level.INFO,"Generating row "+(i+1)+" of "+rgb.length);
			
			for(int j = 0; j<rgb[0].length; j++){
				
				if(rgb[i][j] < 0 && !treated.contains(rgb[i][j])){
					treated.add(rgb[i][j]);
					UtilRGB.log.log(Level.INFO,"Creating color "+(rgb[i][j]*-1));
					
					MaskFilter mj = new MaskFilter();
					mj.setMask(UtilRGB.int256toRGB(rgb[i][j]*-1));
					//Get random image from raw data
					File[] temp2 = EarthMeApp.DATA_FOLDER.listFiles();
					File f = temp2[new Random().nextInt(temp2.length)];
					BufferedImage temp = ImageIO.read(f);
					dummy = new BufferedImage(temp.getWidth(), temp.getHeight(), temp.getType());
					mj.filter(temp, dummy);
					
					// Repeat until the color generated is the one we are looking for
					// (applying a mask won't always generate the color of the mask as the predominant one)
					File dir = new File(EarthMeApp.SOURCE_FOLDER, ""+(rgb[i][j]*-1));
					dir.mkdir();
					
					for(int count = 0; count<EarthMeApp.GENERATE; count++ ){
						f = temp2[new Random().nextInt(temp2.length)];
						temp = ImageIO.read(f);
						dummy = new BufferedImage(temp.getWidth(), temp.getHeight(), temp.getType());
						mj.filter(temp, dummy);
						boolean repeat = false;
						do{
							int n = UtilRGB.intRGBto256(UtilRGB.getMostCommonColourBI(dummy));
							repeat = (rgb[i][j]*-1)!=n;
							if(repeat){
								f = temp2[new Random().nextInt(temp2.length)];
								temp = ImageIO.read(f);
								dummy = new BufferedImage(temp.getWidth(), temp.getHeight(), temp.getType());
								mj.filter(temp, dummy);
							}
						}while(repeat);
						ImageIO.write(dummy, "jpg", new File(dir.getPath(), "_"+count+"_"+f.getName()));
					}
					

				}
				else{
					File[] files = new File(EarthMeApp.SOURCE_FOLDER, ""+Math.abs(rgb[i][j])).listFiles();
					dummy = ImageIO.read(files[new Random().nextInt(files.length)]);
				}
				
				// Copy rgb by rgb

				for(int k = 0; k<dummy.getWidth();k++){
					for(int l = 0; l<dummy.getHeight();l++){
						try{
							r.setRGB(i*EarthMeApp.SCALE+k, j*EarthMeApp.SCALE+l, dummy.getRGB(k, l));
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
				}
			}
		}
		return r;
	}
}
