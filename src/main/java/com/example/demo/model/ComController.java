package com.example.demo.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ComController extends WebObject {

	private static final long serialVersionUID = -2104510560222014738L;

	private static final boolean debug = true;

	boolean loginRequire = false;
	boolean adminRequire = false;
	
	@Autowired public SysConfig sysConfig ;
	
	//@Autowired public CodeService codeService;
	//@Autowired public DbFileService dbFileService;
	//@Autowired public DbFileLogService dbFileLogService;
	//@Autowired public ArticleService articleService;

	//@Autowired public CodeRepository codeRepository;
	//@Autowired public PropRepository propRepository;
	//@Autowired public DbFileRepository dbFileRepository;
	//@Autowired public DbFileLogRepository dbFileLogRepository;
	
	//@Autowired public BoardRepository boardRepository; 
	//@Autowired public ArticleRepository articleRepository; 

	// constructor
	public ComController() {
	}
	// -- constructor

	// init binder
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomTimestampEditor());
		binder.registerCustomEditor(Timestamp.class, new CustomTimestampEditor());
	}
	// -- init binder 

	// getQueryParams
	public String getQueryParams(HttpServletRequest request) {
		String params = "";

		if (true) {
			// Analyze the parameter values
			Map<String, String[]> map = request.getParameterMap();

			if (map != null && map.size() > 0) {
				Set<String> keys = map.keySet();
				Iterator<String> keysIt = keys.iterator();
				String key;
				String[] vals;
				int idx = 0;
				while (keysIt.hasNext()) {
					key = keysIt.next();
					vals = map.get(key);
					if (key == null || key.trim().length() < 1) {
						// do nothing
					} else if (vals == null || vals.length < 1) {
						params += (idx > 0 ? "&" : "?") + key + "=";
						idx++;
						// logger.info( "key: " + key + " = " + ";" );
					} else {
						for (int i = 0, iLen = vals.length; i < iLen; i++) {
							params += (idx > 0 ? "&" : "?") + key + "=" + vals[i];
							idx++;
							// logger.info( "key: " + key + " = " + vals[i] + ";" );
						}
					}
				}
			}
		}

		return params;
	}
	// -- getQueryParams

	public boolean isGetRequest(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("GET");
	}

	public boolean isPostRequest(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("POST");
	}

	public void setLoginUser(HttpServletRequest request, User user) {
		this.getSession(request).setAttribute(LOGIN_USER_ATTR_NAME, user);
	}

	public boolean hasUserLoggedIn(HttpServletRequest request) {
		return null != this.getLoginUser(request);
	}

	public String getCurrUrlPath(HttpServletRequest request) {
		String currUrlPath = request.getRequestURI().substring(request.getContextPath().length());
		return currUrlPath;
	}

	// showRequestInfo
	public boolean showRequestInfo(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String currUri = request.getRequestURI();
		String currUrl = request.getRequestURL().toString();
		String currUrlPath = request.getRequestURI().substring(request.getContextPath().length());
		String queryString = request.getQueryString();
		String referer = request.getHeader("referer");

		referer = null == referer ? "" : referer;
		queryString = null == queryString ? "" : queryString;

		log.info("");
		log.info(LINE);
		log.info("");

		log.info("contextPath  = " + contextPath);
		log.info("currUri      = " + currUri);
		log.info("currUrl      = " + currUrl);
		log.info("currUrlPath  = " + currUrlPath);
		log.info("referer      = " + referer);
		log.info("queryString  = " + queryString);

		log.info("");
		log.info(LINE);
		log.info("");

		return true;
	}
	// -- showRequestInfo

	// isRequestUrlSameAsReferer
	public boolean isRequestUrlSameAsReferer(HttpServletRequest request) {
		String currUrl = request.getRequestURL().toString();
		String referer = request.getHeader("referer");

		currUrl = null == currUrl ? "" : currUrl.trim();
		referer = null == referer ? "" : referer.trim();
		;

		log.info("");
		log.info(LINE);
		log.info("");

		log.info("currUrl      = " + currUrl);
		log.info("referer      = " + referer);

		log.info("");
		log.info(LINE);
		log.info("");

		if (isEmpty(currUrl) || isEmpty(referer)) {
			return false;
		} else if (currUrl.equalsIgnoreCase(referer)) {
			return true;
		}

		return false;
	}
	// -- isRequestUrlSameAsReferer

	// isFirstRequest
	public boolean isFirstVisit(HttpServletRequest request) {
		String referer = request.getHeader("referer");

		if (isEmpty(referer)) {
			return true;
		} else if (isRequestUrlSameAsReferer(request)) {
			return false;
		} else {
			return true;
		}
	}
	// -- isFirstRequest

	public void setCommonAttributes(HttpServletRequest request) {
		Html html = null;

		this.setCommonAttributes(request, html);
	}

	// setCommonAttributes
	public void setCommonAttributes(HttpServletRequest request, Html html) {

		boolean debug = ComController.debug && true;

		html = null == html ? new Html() : html;

		// debug context path
		debug = debug && this.showRequestInfo(request);
		// -- debug context path

		// user login sid

		final User sessionLoginUser = this.getLoginUser(request);

		// -- user login sid

		// set current url path

		String currUrlPath = request.getRequestURI().substring(request.getContextPath().length());
		html.setCurrUrlPath(currUrlPath);

		// -- set current url path

		html.setLoginUser(sessionLoginUser);

		// crud and editable
		String crud = html.getCrud();

		crud = null == crud ? "" : crud.trim();

		boolean editable = false;

		if ("new".equalsIgnoreCase(crud) || "edit".equalsIgnoreCase(crud)) {
			editable = true;
		}

		html.setEditable(editable);

		// -- crud and editable

		request.setAttribute("editable", editable);

		request.setAttribute("readonly", !editable);

		request.setAttribute("crud", crud);

		request.setAttribute("html", html);

	}
	// setCommonAttributes

	// getObjectFromJsonText
	public <T> T getObjectFromJsonText(String jsonText, Class<?> klass) {

		String funName = "getParsedObjectFromJsonText()";

		log.info("");
		log.info(LINE);
		log.info("" + funName);
		log.info("");

		T jsonObject = null;

		if (isValid(jsonText)) {

			jsonText = jsonText.trim();

			if (jsonText.startsWith("\"")) {
				jsonText = jsonText.substring(1);
			}

			if (jsonText.endsWith("\"")) {
				jsonText = jsonText.substring(0, jsonText.length() - 1);
			}

			// appText = appText.replace( "\\\"", "\"" );

			// jsonText = jsonText.replace( "\"{", "{" );
			// jsonText = jsonText.replace( "}\"", "}" );

			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
			gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());

			Gson gson = gsonBuilder.create();

			try {
				jsonObject = (T) gson.fromJson(jsonText, klass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.info("" + (null == jsonObject ? "fail" : "success"));

		log.info("");
		log.info("// " + funName);
		log.info(LINE);
		log.info("");

		return jsonObject;
	}
	// -- getObjectFromJsonText

	// show binding errors
	public void showBindingErrors(BindingResult bindingResult) {
		if (null != bindingResult && bindingResult.hasErrors()) {
			log.info("");
			log.info("bindingResult = " + bindingResult.toString());

			List<ObjectError> errors = bindingResult.getAllErrors();
			if (null != errors) {
				int index = 0;
				for (ObjectError error : errors) {
					log.info(String.format("binding error[%03d]: %s", index, error.toString()));
					index++;
				}
			}

			log.info("");
			log.info("");
		}
	}
	// -- show binding errors

	@Autowired public PropService propService;
	
	public Prop getTotConnUserNo() { 
		Prop connUserNo = propService.getProp("CONN_USER_NO", "1"); 

		return connUserNo;
	}
	
	public Prop getTodayConnUserNo() { 
		String propId = "CONN_USER_NO-" + this.getTodayText() ;
		
		Prop connUserNo = propService.getProp( propId, "1" ); 

		return connUserNo;
	}

	public Prop getTotDownNo() { 
		Prop totDownNo = propService.getProp( "TOT_DOWN_NO", "0" ); 

		return totDownNo;
	}
	
	public String processRequest(UserService userService, HttpServletRequest request, boolean loginRequire ) {
		RedirectAttributes ra = null ; 
		var adminRequire = false ; 
		return this.processRequest(userService, request, loginRequire, adminRequire, ra);
	}

	public String processRequest(UserService userService, HttpServletRequest request, boolean loginRequire, RedirectAttributes ra ) {
		var adminRequire = false ; 
		return this.processRequest(userService, request, loginRequire, adminRequire, ra );
	}
	
	public String processRequest(UserService userService, HttpServletRequest request, boolean loginRequire, boolean adminRequire ) {
		RedirectAttributes ra = null ; 
		return this.processRequest(userService, request, loginRequire, adminRequire, ra);
	}
	
	
	public String processRequest(UserService userService, HttpServletRequest request, boolean loginRequire, boolean adminRequire, RedirectAttributes ra ) {

		boolean debug = ComController.debug && true;

		if (debug) {
			this.showRequestInfo(request);
		}

		String forward = null;
		String error_message = null;
		
		if( null != ra ) {
			// redirect default parameter values
			String [] paramNames = { "invalid_access" } ;
			
			for( String paramName : paramNames ) { 
				Object paramValue = request.getParameter( paramName );
				if( isEmpty( paramValue) ) { 
					paramValue = request.getAttribute( paramName );
				}
				
				if( isValid( paramValue ) ) {
					ra.addFlashAttribute( paramName, paramValue );
				}
			}
			
			// -- redirect default parameter values
		}

		// system name properties
		Prop sysName_01 = propService.getProp("SYS_NAME_01", "경기 지역 본부");
		Prop sysName_02 = propService.getProp("SYS_NAME_02", "성남 전력 지사");
		Prop sysName_03 = propService.getProp("SYS_NAME_03", "154KV 중원변전소");

		Prop totConnUserNo = this.getTotConnUserNo();
		Prop todayConnUserNo = this.getTodayConnUserNo() ; 
		Prop totDownNo = this.getTotDownNo();

		request.setAttribute("controller", this);
		request.setAttribute("cont", this);

		request.setAttribute("sysName_01", sysName_01);
		request.setAttribute("sysName_02", sysName_02);
		request.setAttribute("sysName_03", sysName_03);

		request.setAttribute("totConnUserNo", totConnUserNo);
		request.setAttribute("todayConnUserNo", todayConnUserNo);
		request.setAttribute("totDownNo", totDownNo);

		HttpSession session = request.getSession(true);

		if (null != session) {
			session.setAttribute("sysName_01", sysName_01);
			session.setAttribute("sysName_02", sysName_02);
			session.setAttribute("sysName_03", sysName_03);

			session.setAttribute("totConnUserNo", totConnUserNo);
			session.setAttribute("todayConnUserNo", todayConnUserNo);
			session.setAttribute("totDownNo", totDownNo);
		}

		var application = request.getServletContext();

		if (null != application) {
			application.setAttribute("totConnUserNo", totConnUserNo);
			application.setAttribute("todayConnUserNo", todayConnUserNo);
			application.setAttribute("totDownNo", totDownNo);
		}

		// system name properties

		User loginUser = this.getLoginUser(request);

		if (loginRequire) {

			if (null == loginUser) {
				loginUser = userService.getLoginUserCreateIfNotExist(request);

				if (null != loginUser) {
					this.setLoginUser(request, loginUser);

					// set total connection user number
					if (null != totConnUserNo) {
						totConnUserNo.increaseBy(1);
						this.propService.saveProp(totConnUserNo);
					}
					
					// set today connection user number
					if (null != todayConnUserNo) {
						todayConnUserNo.increaseBy(1);
						this.propService.saveProp(todayConnUserNo);
					}
					
					forward = "redirect:" + this.getCurrUrlPath(request);
				}
			}
		}
		
		var createTestData = false ;
		if( createTestData ) { 
			this.createTestData( userService, request ) ; 
		}

		if( loginRequire && null == loginUser ) {
			forward = "312_user_login.html";
			forward = "forward:/user/login.html";
		} else if( adminRequire && ( null == loginUser || ! loginUser.isAdmin() ) ) { 
			forward = "040_error_message.html" ;  
			error_message = "관리자 권한이 필요한 페이지입니다.";
		}

		if (null != loginUser) {
			request.setAttribute("loginUser", loginUser);
			request.setAttribute("loginUser_id", loginUser.userId);
			
			request.setAttribute("isAdmin", loginUser.isAdmin() );
		}
		
		String referrer = request.getHeader("referer");
		
		log.info( "referrer = " + referrer );
		
		request.setAttribute( "referrer", referrer );
		
		request.setAttribute( "error_message", error_message );
		
		request.setAttribute( "req", request );

		if (debug) {
			log.info( LINE );
			log.info( "adminRequire = " + adminRequire );
			log.info( "forward = " + forward ); 
			log.info( LINE );
		}

		return forward;
	}
	// -- processRequest
	
	public String getBrowserName(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		
		log.info( "header = " + header ); 

		if (header.indexOf("Edge") > -1) {
			return "Edge";
		}else if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Trident") > -1) {
			return "Trident";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		} else if (header.indexOf("Safari") > -1) {
			return "Safari";
		}

		return "Firefox";
	}
	
	protected String getEncodedDownLoadFileName( HttpServletRequest request, String fileName ) {
		String encodedFileName = fileName ;
		try {
			encodedFileName = this.getEncodedDownLoadFileNameImpl( request, fileName );
		} catch( Exception e ) {
			try {
				encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) { 
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return encodedFileName;
	}
	
	protected String getEncodedDownLoadFileNameImpl( HttpServletRequest request, String fileName ) throws Exception {
		String browser = this.getBrowserName(request);
		String encodedFileName = null; 
	      
         if ( browser.equalsIgnoreCase("MSIE") || browser.equalsIgnoreCase("Edge")  ) {
        	 encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
         } else if (browser.equalsIgnoreCase("Trident")) {       
        	 encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
         } else if (browser.equalsIgnoreCase("Firefox")) {
        	 encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        	 encodedFileName = URLDecoder.decode(encodedFileName);
         } else if (browser.equalsIgnoreCase("Opera")) {
        	 encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
         } else if (browser.equalsIgnoreCase("Chrome")) { 
        	 encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
         } else if (browser.equalsIgnoreCase("Safari")){
        	 encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        	 encodedFileName = URLDecoder.decode(encodedFileName);
         } else if (browser.equalsIgnoreCase("Mozilla")){
        	 encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1" )+ "\"";
        	 encodedFileName = URLDecoder.decode(encodedFileName);
         } else {
        	 encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
         }
         
         log.info( "browser = " + browser );
         log.info( "encodedFileName = " + encodedFileName );
         
         return encodedFileName ; 
	}
	
	private void createTestData( UserService userService, HttpServletRequest request ) {
		var test = true ;
		if( ! test ) {
			return ; 
		}
		
		userService.createTestData( request );
		
	}	

}