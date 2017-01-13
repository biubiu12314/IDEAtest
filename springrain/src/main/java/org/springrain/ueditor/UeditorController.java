package org.springrain.ueditor;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springrain.frame.controller.BaseController;
import org.springrain.frame.util.FileUtils;
import org.springrain.frame.util.JsonUtils;


@Controller
@RequestMapping("/ueditor")
public class UeditorController extends BaseController {
	
	
	private static final String UEFILEPATH="/upload";
	
	
	@RequestMapping(value = "/init")
    public @ResponseBody Object config(HttpServletRequest requestfile,HttpServletRequest request, Model model) throws Exception {
 
    	String action=request.getParameter("action");
    	if(StringUtils.isBlank(action)){
    		return getResultMap(false);
    	}
    	
    	
       	String path = request.getContextPath();
       	String fileuploadpath=UEFILEPATH+"/siteId/ueditor/";
    	UeditorConfig config=new UeditorConfig(path+fileuploadpath);
    	
    	Object obj=null;
    	
    	if("config".equalsIgnoreCase(action)){
    		obj=config;
    	}else if(UeditorConfig.ACTION_UPLOAD_IMAGE.equalsIgnoreCase(action)||UeditorConfig.ACTION_CATCHIMAGE.equalsIgnoreCase(action)){
    		fileuploadpath=fileuploadpath+"image/";
    		obj=upload(requestfile, fileuploadpath);
    	}else if(UeditorConfig.ACTION_UPLOAD_FILE.equalsIgnoreCase(action)){
    		fileuploadpath=fileuploadpath+"file/";
    		obj=upload(requestfile, fileuploadpath);
    	}else if(UeditorConfig.ACTION_UPLOAD_VIDEO.equalsIgnoreCase(action)){
    		fileuploadpath=fileuploadpath+"video/";
    		obj=upload(requestfile, fileuploadpath);
    	}else if(UeditorConfig.ACTION_UPLOAD_SCRAWL.equalsIgnoreCase(action)){
       		fileuploadpath=fileuploadpath+"scrawl/";
    		obj=uploadScrawl(requestfile, fileuploadpath);
    	}else if(UeditorConfig.ACTION_LISTFILE.equalsIgnoreCase(action)){
       		fileuploadpath=fileuploadpath+"file/";
    		obj=listFile(requestfile, fileuploadpath,config);
    	}else if(UeditorConfig.ACTION_LISTIMAGE.equalsIgnoreCase(action)){
       		fileuploadpath=fileuploadpath+"image/";
    		obj=listFile(requestfile, fileuploadpath,config);
    	}else{
    		return getResultMap(false);
    	}
    	
    	
    	
    	String callbackName = request.getParameter("callback");
    	
    	if(StringUtils.isBlank(callbackName)){
    		return obj;
    	}
    	callbackName=URLEncoder.encode(callbackName,"UTF-8");
    	
    	if(validCallbackName(callbackName)){
    		return  callbackName+"("+JsonUtils.writeValueAsString(obj)+");";
    	}
    	return  obj;
    }
    
    
    public Map<String, Object> upload(HttpServletRequest request,String fileuploadpath) throws Exception {
    	
        MultipartHttpServletRequest requestfile = (MultipartHttpServletRequest) request;  

    	
    	MultipartFile file = requestfile.getFile(UeditorConfig.FIELD_NAME);
        String originalName = file.getOriginalFilename();
        String suffix = FileUtils.getSuffix(originalName);
        String fileName = FileUtils.reSetFileName(suffix);
        
    	//保存到文件
        FileUtils.upload(file, FileUtils.getRootDir()+fileuploadpath+fileName);
        
        Map<String, Object> map = getResultMap(true);
        map.put("size", file.getSize());
        map.put("title", originalName);
        map.put("url", fileName);
        map.put("type", suffix);
        map.put("original", originalName);
    	return  map;
    }
    
    public Map<String, Object> uploadScrawl(HttpServletRequest request,String fileuploadpath) throws Exception {
    	
    	String fileStr = request.getParameter(UeditorConfig.FIELD_NAME);
    	
    	if(StringUtils.isBlank(fileStr)){
    		return getResultMap(false);
    	}
        String fileName = FileUtils.reSetFileName(UeditorConfig.SCRAWL_TYPE);
        File file=new File(FileUtils.getRootDir()+fileuploadpath+fileName);
        byte[] decodeBase64 = Base64.decodeBase64(fileStr);
        
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, decodeBase64);
        
        Map<String, Object> map = getResultMap(true);
        map.put("size", decodeBase64.length);
        map.put("title", file.getName());
        map.put("url", fileName);
        map.put("type", UeditorConfig.SCRAWL_TYPE);
        map.put("original", "scrawl"+UeditorConfig.SCRAWL_TYPE);
    	return  map;
    }
    
    
  public Map<String, Object> listFile(HttpServletRequest request,String fileuploadpath,UeditorConfig config) throws Exception {
	  
	  
	    String start_str = request.getParameter("start");
	    
	    if(StringUtils.isBlank(start_str)){
	    	return getResultMap(false);
	    }
	    
	    int index=Integer.valueOf(start_str);
	    
	    File dir=new  File(FileUtils.getRootDir()+fileuploadpath);

		File[] list = dir.listFiles();
		
		if(list==null||list.length<1){
			return getResultMap(false);
		}
		
		Map<String, Object> resultMap = getResultMap(true);
		
		Integer count = config.getListFileSize();
		
		if ( index < 0 || index > list.length ) {
			return resultMap;
		}
	     
		Object[] fileList = Arrays.copyOfRange(list, index, index + count);
		if(fileList==null||fileList.length<1){
			return resultMap;
		}
		
		List<Map<String,String>> listUrl=new ArrayList<Map<String,String>>(fileList.length);
		
		for(Object o:fileList){
			if(o==null){
				break;
			}
			
			File f=(File) o;
			
			String url=f.getName();
			Map<String,String> map=new HashMap<String,String>();
			map.put("url", url);
			listUrl.add(map);
		}
		
		resultMap.put("list",listUrl);
		resultMap.put("start", index );
		resultMap.put("total", list.length );
	    
    	return  resultMap;
    }
    
	
    private Map<String, Object> getResultMap(boolean success) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (success) {
            map.put("state", "SUCCESS");
        } else {
            map.put("state", "ERROR");
        }
        return map;
    }
    
    /**
	 * callback参数验证
	 */
	public boolean validCallbackName ( String name ) {
		
		if ( name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" ) ) {
			return true;
		}
		
		return false;
		
	}

}