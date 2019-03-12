package com.sl.pmpapp.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sl.pmpapp.utils.CodeMsg;
import com.sl.pmpapp.utils.JwtToken;
import com.sl.pmpapp.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
	 
	/**
	 * 拦截器
	 * @ 用来判断用户的
	 *1. 当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。也就是说，请求不继续往下传了，直接沿着来的链往回跑。
	 2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。然后进入拦截器链，运
	 行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
	 */
	 
	//@component （把普通pojo实例化到spring容器中，相当于配置文件中的
	@Component
	public class TokenInterceptor implements HandlerInterceptor {
//	    @Autowired
//	    LoginTicketDao lTicketsDAO;
	//
//	    @Autowired
//	    userDAO uDAO;
	//
//	    @Autowired
//	    HostHolder hostHolder;
	 
	    /**
	     * 在请求处理之前进行调用（Controller方法调用之前）
	     *
	     * @param request
	     * @param response
	     * @param handler
	     * @return
	     * @throws Exception
	     */
	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    	response.setCharacterEncoding("UTF-8");  
	    	response.setContentType("application/json; charset=utf-8");
	    	String token = request.getParameter("token");
	        PrintWriter out = null ;
	        if(token==null){
	        	JSONObject res = new JSONObject();
	        	res.put("code", "1001");
	        	res.put("msg", "token认证失败");
	        	out = response.getWriter();
	            out.append(res.toString());
	        	   return false;
	        }else{
	        	if(JwtToken.verifyToken(token)==null){
	        		JSONObject res = new JSONObject();
	        		res.put("code", "1002");
		        	res.put("msg", "授权过期");
		        	 out = response.getWriter();
		        	 out.append(res.toString());
	        		return false;
	        	}
	        }
	 
//	        String username = request.getParameter("username");
//	        System.out.println("拦截器拦截username："+username);
	 
//	            request.getSession().setAttribute("msg", "你还没有登录，请登录后在访问");
//	            request.setAttribute("msg", "你还没有登录，请登录后在访问");
	 
//	        if(tickets != null ){
//	            //根据拦截到的cookie往数据库里查询
//	            loginTickets loginTickets  = lTicketsDAO.selectByTicket(tickets);
//	            if(loginTickets == null || loginTickets.getExpired().before(new Date()) || loginTickets.getStatus() != 0){
//	                return true;
//	            }
	//
//	            User user = uDAO.selectById(loginTickets.getUserId());
//	            hostHolder.setUser(user);
//	        }
	        return true;
	    }
	    private void error(CodeMsg aUTH_EXPIRES) {
			// TODO Auto-generated method stub
			
		}
		/**
	     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	     * @param modelAndView
	     * @throws Exception
	     */
	    @Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	      //  System.out.println("拦截器postHandle方法"+modelAndView);
//	        //就是为了能够在渲染之前所有的freemaker模板能够访问这个对象user，就是在所有的controller渲染之前将这个user加进去
//	        if(modelAndView != null){
//	            //这个其实就和model.addAttribute一样的功能，就是把这个变量与前端视图进行交互 //就是与header.html页面的user对应
//	            //modelAndView.addObject("user",hostHolder.getUser());
//	        }
	    }
	 
	    @Override
	    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	        //hostHolder.clear();   //当执行完成之后呢需要将变量清空
	    }
	 
	}
	


