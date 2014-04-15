package system;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
public class FileUploadServletTest {
 
 @Test
 public void testUpload() throws Exception {
  HttpServletRequest request = createMock(HttpServletRequest.class);
 
  expect(request.getContentType()).andReturn("multipart/form-data; boundary=-----------------------------825660231676525091188156953").anyTimes();
  request.setAttribute((String)anyObject(), anyObject());
 
  expect(request.getMethod()).andReturn("post");
  expect(request.getInputStream()).andReturn(new MockServletInputStream("data.txt"));
  expect(request.getCharacterEncoding()).andReturn("UTF-8");
  expect(request.getHeader("Content-length")).andReturn("1024");
  expect(request.getContentLength()).andReturn(1024);
  replay(request);
 
  UploadServlet testServlet = new UploadServlet();
  assertTrue(testServlet.execute(request,null));
 }
  
 private class MockServletInputStream extends ServletInputStream {
   
  InputStream fis = null;
  public MockServletInputStream(String fileName) {
   try {
    fis = getClass().getResourceAsStream(fileName);
   } catch (Exception genExe) {
    genExe.printStackTrace();
   }
  }
  @Override
  public int read() throws IOException {
   if(fis.available() > 0) {
    return fis.read();
   }
   return 0;
  }
 
  @Override
  public int read(byte[] bytes, int len, int size) throws IOException {
   if(fis.available() > 0) {
    int length = fis.read(bytes, len, size);
    return length;
   }
   return -1;
  }
 }
 
}
