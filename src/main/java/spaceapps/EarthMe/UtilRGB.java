package spaceapps.EarthMe;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//import org.apache.commons.io.FileUtils;

public class UtilRGB {
    
    public static Logger log = Logger.getLogger(EarthMeApp.class.getName());

    /**
     * Transforms a 8 bits color into a 24 bits color(RGB)
     * @param RGB_256 8 bits color
     * @return 24 bits color
     */
    public static int int256toRGB(int RGB_256){
    	int r = (RGB_256>>5) << 21;
    	r = r | ((RGB_256>>2) & 0x7) << 13;
    	r = r | ((RGB_256) & 0x3) << 6;
    	return r; 
    }
	
    /**
     * Transforms a 24 bits color to the closest 8 bits color
     * @param RGB 24 bits color
     * @return 8 bits color
     */
	public static int intRGBto256(int RGB){
    	int r = RGB >> 21;
    	int g = (RGB >> 13) & 0x7;
    	int b = (RGB >> 6) & 0x3;
    	return (r << 5) | (g << 2) | b;
    }
	

	/**
	 * Gets the most common color from a BufferedImage
	 * @param bi the BufferedImage
	 * @return a 24-bit coded color
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getMostCommonColourBI(BufferedImage bi){
		int width = bi.getWidth();
		int height = bi.getHeight();
		
        Map m = new HashMap();
        for(int i=0; i < width ; i++)
        {
            for(int j=0; j < height ; j++)
            {
                int rgb = bi.getRGB(i, j);
                // Filter out grays....    9                    
                    Integer counter = (Integer) m.get(rgb);   
                    if (counter == null)
                        counter = 0;
                    counter++;                                
                    m.put(rgb, counter);    
            }
        }   
		return getMostCommonColour(m);
	}   
	
	/**
	 * Gets the most commmon color from a Map of pixels
	 * @param map the Map
	 * @return a 24-bit coded color
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int getMostCommonColour(Map map) {
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
	
	/**
	 * Parse a RGB color to a int array
	 * @param pixel the RGB color
	 * @return int array with the colors which are int[0]=blue, 
	 * int[1]=green and int[2]=red
	 */
	private static int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};

  }
}
