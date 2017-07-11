package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Created by admin on 2017/6/22.
 */
@WebFilter(
        filterName = "WordFilter",
        value ={"/LoginServlet"},
        initParams ={
                @WebInitParam(name = "encoding",value = "UTF-8")
        }
)
public class WordFilter implements Filter {
    //非法字符数组
    private String words[];
    //字符编码
    private String encoding;
    public void destroy() {
        this.words=null;
        this.encoding=null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        if(encoding!=null){
            req.setCharacterEncoding(encoding);
            req=new Request((HttpServletRequest)req);
            resp.setContentType("text/html;charset="+encoding);
        }
        chain.doFilter(req,resp);
    }

    public void init(FilterConfig config) throws ServletException {
        //初始化非法字符数组
        words=new String[]{"混蛋","智障"};
        //初始化字符编码
        encoding=config.getInitParameter("encoding");

    }

    public String filter(String param){
        if(words!=null&&words.length>0){
            for(int i=0;i<words.length;i++){
                if(param.indexOf(words[i])!=-1){
                    param=param.replaceAll(words[i],"***");
                }
            }
        }
        return param;
    }
    class Request extends HttpServletRequestWrapper{

        public Request(HttpServletRequest request) {
            super(request);
        }
        @Override
        public String getParameter(String name){
            String param=filter(super.getRequest().getParameter(name));
            return  param;
        }
        @Override
        public  String[] getParameterValues(String name){
            String param[]=super.getRequest().getParameterValues(name);
            for(int i=0;i<param.length;i++){
                param[i]=filter(param[i]);
            }
            return param;
        }
    }

}
