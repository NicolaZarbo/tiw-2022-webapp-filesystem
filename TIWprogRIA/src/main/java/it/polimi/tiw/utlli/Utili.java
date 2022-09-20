package it.polimi.tiw.utlli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	public static boolean isMail(String mail) {
		String []segms=mail.split("@");
		if(segms.length!=2)
			return false;
		if(sporca(segms[0]))
			return false;
		String[] dom=segms[1].split("\\.");
		if(dom.length!=2)
			return false;
		if(sporca(dom[0])||sporca(dom[1]))
			return false;
		
		return true;
	}
	public static void errorMessage(HttpServletResponse response, int staus, String message) throws IOException {
		response.setStatus(staus);
		response.getWriter().append(message);
		response.setContentType("text/plain");
	}
	public static String readFileAsString(String path) throws FileNotFoundException {
		File f=new File(path);
		Scanner sc=new Scanner(f);
		StringBuilder out=new StringBuilder();
		while (sc.hasNextLine()) {
            out.append(sc.nextLine());
        }
		sc.close();
		return out.toString();
	}
	
}
