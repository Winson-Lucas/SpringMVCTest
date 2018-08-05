package main.springmvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mvc")
public class TestControl {

	@RequestMapping("/hello.do")
	public String hello(HttpServletRequest req, HttpServletResponse res){
		Enumeration<String> enu = req.getParameterNames();
		System.out.println("---------------xx");
		System.out.println(req.getSession().getAttribute("sessionBB"));
		System.out.println("---------------xx");
		while(enu.hasMoreElements()){
			if("bb".equals(enu.nextElement())){
				System.out.println("bb "+req.getParameter("bb"));
				req.getSession().setAttribute("sessionBB", req.getParameter("bb"));
				break;
			}else{
				System.out.println(req.getParameter("aa"));
			}
		}
		System.out.println("Hellot");
		return "NewFile";
	}
	
	@RequestMapping("/test.do")
	public void test(HttpServletRequest req, HttpServletResponse res) throws IOException{
		System.out.println(req.getParameter("name"));
		System.out.println("---------------");
		System.out.println(req.getSession().getAttribute("sessionBB"));
		System.out.println("---------------");
		System.out.println("test");
		PrintWriter out = res.getWriter();
        out.print("xxxx");
        out.flush();
        out.close();
	}
	
	@RequestMapping("/open.do")
	public ModelAndView open(HttpServletRequest req, HttpServletResponse res) throws IOException{
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("xxx", "yyy");
		ModelAndView view = new ModelAndView("openWin", mp);
		return view;
	}
}
