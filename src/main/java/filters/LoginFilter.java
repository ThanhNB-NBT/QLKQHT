package filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class LoginFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = 1L;

	public LoginFilter() {
        super();
    }


	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (path.endsWith("/login.jsp") || path.endsWith("/LoginServlet") || 
    	    path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png") 
    	    || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif")) { 
    	    chain.doFilter(request, response); 
    	    return; 
    	}
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
        	res.sendRedirect(req.getContextPath() + "/login.jsp"); 
        } else {
            chain.doFilter(request, response);
        }
	}

	public void init(FilterConfig fConfig) throws ServletException {
   // TODO document why this method is empty
 }

}
