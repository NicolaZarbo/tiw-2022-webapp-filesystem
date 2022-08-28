package it.polimi.tiw.utlli;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class Utili {
	private static String[] regexCancel = {"-","\\+","/","\\*","\\[","\\^","\\]",
			"\\{","\\}",".","=","<",">",",","'","\"","%","$","&",")","(","?","|","!"};
	//private static String regexCancel="\"£$%&/()=+-*/<>{}][',.!|";
	private static String[] susWord =
		{"SELECT", "DROP", "INSERT","DELETE","AND","WHERE",
				"OR","HAVING","GROUP"};
	
	public static String cleanString(String userInput) {
		String out=userInput;
		String asl="+";
		for(int i=0;i<regexCancel.length;i++) {
			asl="\\"+regexCancel[i];
			out = out.replace( regexCancel[i],asl );
		}
		for(int i=0;i<susWord.length;i++){
			if(out.contains(susWord[i])) {
				
				out=out.replace(susWord[i],"-"+susWord[i]+"-" );
			}
		}
		return out;
	}
	public static String getOriginalStr(String inputFromDb) {
		String out=inputFromDb;
		for(int i=0;i<susWord.length;i++){		
			out=out.replace("-"+susWord[i]+"-",susWord[i] );
		}
		return out;
	}
	public static boolean sporca(String str) {
		return containsSusCharacters(str)||containsSusWords(str);
		
	}
	public static boolean containsSusCharacters(String str) {
		for(int i=0;i<regexCancel.length;i++) {
			if(str.contains(regexCancel[i]))
				return true;
		}
		return false;
	}
	public static boolean containsSusWords(String str) {
		for (int i = 0; i < susWord.length; i++) {
			if(str.toUpperCase().contains(susWord[i]))
				return true;
		}
		return false;
	}
	public static TemplateEngine getTemplateEngine(ServletContext servletContext) {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		return templateEngine;
	}
	
	
	public static void gestioneCronologia(HttpSession sessione,WebContext ctx, HttpServletRequest request, String actualUrl )  {
		ArrayList<String> cronos=(ArrayList<String>)sessione.getAttribute("cronologia");
		String back=request.getParameter("back");
		String last="";
		if(cronos==null) {
			cronos=new ArrayList<>();
			cronos.add(actualUrl);
		}
		else {
			if(cronos.isEmpty()) {				
				cronos.add(actualUrl);
				last=actualUrl;
			}else {
				if(back!=null) {
					last=cronos.remove(cronos.size()-1);
				}else {  
					last=cronos.get(cronos.size()-1);
					cronos.add(actualUrl);
				}
			}
		}
		//System.out.println(cronos);

		sessione.setAttribute("cronologia",cronos);
		ctx.setVariable("lastPage", last);
	}
	
}
