package spaceapps.EarthMe;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

public class EarthMeApp {

	// The square edge to analyze the input image [edge x edge]
    public static int edge = 3;
    
	// The directory where the data is classified
    public static File SOURCE_FOLDER;// = new File ("resources/class_100");

	// The directory with the data
    public static File DATA_FOLDER;// = new File ("resources/data_100");
    
    /*/
    public static String SOURCE_FOLDER_STR = SOURCE_FOLDER.getAbsolutePath();
    public static String DATA_FOLDER_STR = DATA_FOLDER.getAbsolutePath();
    /*/
	
    // The width & height in pixels from the data
    public static final int SCALE = 50;
    
    // The image to mosaic
    public static File IMG = new File("gato2.jpg");
    
    // Points if the data is already classified (it's enough to be classified once)
    // set to true after the first use
    public static boolean CLASSIFIED = true;
    public static int GENERATE = 200;

    public static void main( String[] args ) throws IOException    {
    	new EarthMeApp().mosaic(IMG, new File("results"),null);
    }
    
    public void mosaic(File img, File output, ServletContext ctx) throws IOException{
    	SOURCE_FOLDER = new File (ctx.getRealPath("/class_50"));
    	DATA_FOLDER = new File (ctx.getRealPath("/data_50"));
    	if(!CLASSIFIED)
    		new Classifier().classifyDir(DATA_FOLDER, SOURCE_FOLDER);

    	img = new File(ctx.getRealPath(""),img.getPath());
    	output = new File(ctx.getRealPath(""),output.getPath());
    	
    	new Builder().build(img, output);
    }
}
