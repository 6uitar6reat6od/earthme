package system;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import spaceapps.EarthMe.EarthMeApp;

/**
 * Servlet implementation class UploadServlet
 */

@MultipartConfig(location = "/var/lib/openshift/534cf8185973ca306a0000a5/app-root/data")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private File theImg;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		OutputStream os = new FileOutputStream("/home/david/data.txt");
//		InputStream is = request.getInputStream();
//		int a;
//		while((a = is.read())!=-1) {
//			os.write(a);
//		}
//		os.close();
		if(execute(request,response)){
			response.setStatus(200);
		}else{
			response.setStatus(500);
		}
			
	}

	protected boolean execute(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(ServletFileUpload.isMultipartContent(request)){
            try {
            	HttpSession session = request.getSession();
            	
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        ServletContext ctx = request.getServletContext();
//                        System.out.println("ContextPath: "+ctx.getContextPath());
//                        System.out.println("ContextPath: "+ctx.getRealPath("/"));
                        File uploadFolder = new File(ctx.getRealPath(""),"uploads");
                        File tempFile = File.createTempFile( getServletName(), ".jpg", uploadFolder);
                        item.write(tempFile);
                        uploadFolder = new File("uploads");
                        File resultFolder = new File("results");
                        
//                        File uploadFolder = new File(System.getProperty("user.dir"),"uploads");
//                        File resultFolder = new File(System.getProperty("user.dir"),"results");
                        
                        System.out.println(uploadFolder.exists()+"\t"+uploadFolder);
                        System.out.println(resultFolder.exists()+"\t"+resultFolder);
                        
                        //File source = new File("./img/"+item.getName());
                        //tempFile.renameTo(source);
                        //item.write(source);
                        //File source = new File(uploadFolder,item.getName());
                        //BufferedImage img = ImageIO.read(tempFile);
                        //ImageIO.write(img, "jpg", source);
                        
                        File source = new File(uploadFolder,item.getName());

                        BufferedImage img = ImageIO.read(tempFile);
                        ImageIO.write(img, "jpg", source);

                        ctx.log("UPLOAD: "+uploadFolder.getPath());
                        ctx.log(tempFile.getAbsolutePath());
                        ctx.log(tempFile.getName());
                        //File source_op = new File(uploadFolder.getPath()+tempFile.getName());

                        ctx.log(source+" EXIST?: "+source.exists()+ " THO: "+tempFile.exists());
                        new EarthMeApp().mosaic(source, resultFolder,ctx);
                        ctx.log("GOT MATRIX");
                        
                        //File sourceResized = new File("./resized/"+item.getName());
                        File resultImgSrc = new File(resultFolder,item.getName());
                        
                        theImg = resultImgSrc;
                        
                        System.out.println("RESULTADO "+resultImgSrc.getPath());
                        
                        session.setAttribute("source", source);
                        //session.setAttribute("source_res", sourceResized);
                        session.setAttribute("result", resultImgSrc);
                        response.sendRedirect("./result.jsp");
                        
                    }
                }
                
               return true;
            } catch (Exception e) {
            	e.printStackTrace();
            }          
		}
		return false;
	}

	@Override
	public void destroy(){
		theImg.delete();
		super.destroy();
	}
}
