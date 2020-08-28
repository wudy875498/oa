package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("\t<head>\n");
      out.write("\t\t<meta charset=\"UTF-8\">\n");
      out.write("\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n");
      out.write("\t\t<title>欢迎光临</title>\n");
      out.write("\t\t<link href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("/jspFile/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
      out.write("\t</head>\n");
      out.write("\t<body style=\"background-color:#5bc0de;\">\n");
      out.write("\t\t<div id=\"all\" style=\"position: absolute;\">\n");
      out.write("\t\t\t<div><h1 class=\"text-center \" style=\"font-size:66px;\">OA管理系统</h1></div>\n");
      out.write("\t\t\t<br/>\n");
      out.write("\t\t\t<div class=\"text-center\"><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("/welcome.do\" class=\"btn btn-success\" style=\"font-size:35px;\">点击进入</a></div>\n");
      out.write("\t\t\t<div class=\"text-center\">\n");
      out.write("\t\t\t\t<p>作者：软件15001班&nbsp;&nbsp;陈锡鑫</p>\n");
      out.write("\t\t\t\t<p>时间：2017年9-12月</p>\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<script src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("/jspFile/js/jquery-1.12.4.min.js\"></script>\n");
      out.write("\t\t<script src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("/jspFile/bootstrap/js/bootstrap.min.js\"></script>\n");
      out.write("\t\t<script>\n");
      out.write("\t\t$(document).ready(function(){\n");
      out.write("\t\t\tchange();\n");
      out.write("\t\t});\n");
      out.write("\t\t$(window).resize(function(){\n");
      out.write("\t\t\tchange();\n");
      out.write("\t\t});\n");
      out.write("\t\tfunction change(){\n");
      out.write("\t\t\tvar h = ($(window).height()-$('#all').height())/2;\n");
      out.write("\t\t\tvar w = ($(window).width()-$('#all').width())/2;\n");
      out.write("\t\t\t$('#all').css(\"top\",h+\"px\");\n");
      out.write("\t\t\t$('#all').css(\"left\",w+\"px\");\n");
      out.write("\t\t}\n");
      out.write("\t</script>\n");
      out.write("\t</body>\n");
      out.write("\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
