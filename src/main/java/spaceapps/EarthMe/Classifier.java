package spaceapps.EarthMe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;


public class Classifier {

    public static void main(String args[]) throws Exception {
    	new Classifier().classifyDir(EarthMeApp.DATA_FOLDER, EarthMeApp.SOURCE_FOLDER);   	
    }
    
    /**
     * Copy the files on the input directory to the output directory classifying them
     * into their predominant color
     * @param inputDir the input dir (1 lvl deep search)
     * @param outputDir the output dir, if it doesn't exist the method creates it
     * @throws IOException
     */
    public void classifyDir(File inputDir, File outputDir) throws IOException{
    	int size =  EarthMeApp.DATA_FOLDER.listFiles().length;
    	int i = 0;
    	
    	if(!outputDir.exists())
    		outputDir.mkdir();
    	
    	for(File f : EarthMeApp.DATA_FOLDER.listFiles()){
    		i++;
    		UtilRGB.log.log(Level.INFO,"Processing file "+f.getPath()+" ["+i+" of "+size+"]");
    		classifyImg(f,EarthMeApp.DATA_FOLDER,EarthMeApp.SOURCE_FOLDER);
    	}
    	
    	UtilRGB.log.log(Level.INFO,"All data classified");
    }
    
    /**
     * Classifies the images stored on path depending on their predominant
     * 8-bit color equivalent, 
     * @param theImage a image
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes" })
	public void classifyImg(File theImage, File inputDir, File outputDir) throws IOException{
    	
        ImageInputStream is = ImageIO.createImageInputStream(theImage);
        Iterator iter = ImageIO.getImageReaders(is);

        if (!iter.hasNext())
        {
        	UtilRGB.log.log(Level.SEVERE,"Cannot load the specified file "+ theImage);
            System.exit(1);
        }
        ImageReader imageReader = (ImageReader)iter.next();
        imageReader.setInput(is);

        BufferedImage image = imageReader.read(0);

//        int height = image.getHeight();
//        int width = image.getWidth();

//        Map m = new HashMap();
//        for(int i=0; i < width ; i++)
//        {
//            for(int j=0; j < height ; j++)
//            {
//            	//Add pixles to a map (needed to get the predominant color)
//                int rgb = image.getRGB(i, j);
//                Integer counter = (Integer) m.get(rgb);   
//                if (counter == null)
//                    counter = 0;
//                counter++;                                
//                m.put(rgb, counter);    
//            }
//        }   
        int colourHex = -1;     
        
//    	colourHex = getMostCommonColour(m);
        colourHex = UtilRGB.getMostCommonColourBI(image);
    	colourHex = UtilRGB.intRGBto256(colourHex);
        
        File folder = new File(outputDir,""+colourHex);
        
        // Creating the directory for the color
        if(!folder.exists())
        	folder.mkdir();
        
        ImageIO.write(image, "jpg", new File(folder, theImage.getName()+".jpg"));
    }

    /**
     * get the most common color from a map of pixels
     * 
     * source: stackoverflow.com, many posts with the same code, can't
     * give all the credit to one person
     * @param map
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public int getMostCommonColour(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
              public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
              }
        });  
        Map.Entry me = (Map.Entry )list.get(list.size()-1);
        int[] rgb= getRGBArr((Integer)me.getKey());
        return (rgb[0]<<16) | (rgb[1]<<8) | rgb[2];        
    }    

    public int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};

  }
}