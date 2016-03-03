package jp.co.powerbeans.gae.taglib.function;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slim3.util.ArrayMap;
import org.slim3.util.HtmlUtil;
import org.slim3.util.RequestLocator;
import org.slim3.util.ServletContextLocator;

/**
 * POIが出力するform parts jspが利用するFunction.
 * poif.tld 経由で利用
 * @author Akihiko MONDEN
 *
 * @version $Id: FormFunction.java 897 2010-09-24 05:24:59Z amonden $
 */
public class FormFunction {

  /**
   * enum_simple_name のEnum型でセレクトボックスを作成する.
   * デフォルト値はリクエストパラメータから取得する
   * @param enum_simple_name
   * @param item_name
   * @param label_field 
   * @return
   */
  public static String getSelectBoxByEnum(String enum_simple_name, String item_name, String label_field, String top_label) {
      StringBuilder s = new StringBuilder();
      
      // 入力エラー表示対応
      {
          ArrayMap<String, String> errors = (ArrayMap<String, String>) RequestLocator.get().getAttribute("errors");
          boolean is_error_item = (errors != null) && StringUtils.isNotEmpty(errors.get(item_name));
          String tag_cls = is_error_item ? "error":"";
          s.append("<select name='"+item_name+"' id='"+item_name+"' class='"+tag_cls+"'>");
      }
      
      // get requst value
      final String req_val = RequestLocator.get().getParameter(item_name);
      
      if (StringUtils.isNotEmpty(top_label)) {
          String val = "";
          String sel = val != null && req_val != null && val.equals(req_val) ? " selected" : "";
          s.append(String.format("<option value='%s'%s>%s</option>",
              "", sel, top_label));
      }
      
      final String class_name = getEnumPackage() + enum_simple_name;
      try {
          Class cls = Class.forName(class_name);
          Method m = cls.getMethod("getAllList", null);
          Field f = cls.getField(label_field);
          Collection col = (Collection) m.invoke(null, null);
          for (Iterator it = col.iterator(); it.hasNext();) {
              Object obj = (Object) it.next();
              Object obj_val = obj.toString();
              String val = obj_val == null ? null : obj_val.toString();
              String sel = val != null && req_val != null && val.equals(req_val) ? " selected" : "";
              s.append(String.format("<option value='%s'%s>%s</option>",
                  obj.toString(), sel, f.get(obj)));
              
          }
          
          
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      s.append("</select>");
      
      return s.toString();
      
  }
  
  /**
	 * @return
	 */
	private static String getEnumPackage() {
    return ServletContextLocator.get().getInitParameter("slim3.rootPackage") + ".entity.";
	}

	/**
   * enum_simple_name のEnum型でセレクトボックスを作成する.
   * デフォルト値はdef_valを利用する
   * @param enum_simple_name
   * @param item_name
   * @param label_field 
   * @return
   */
  public static String getSelectBoxByEnumDef(String enum_simple_name, String item_name, String label_field, String top_label, String def_value) {
      StringBuilder s = new StringBuilder();
      
      // 入力エラー表示対応
      {
          ArrayMap<String, String> errors = (ArrayMap<String, String>) RequestLocator.get().getAttribute("errors");
          boolean is_error_item = (errors != null) && StringUtils.isNotEmpty(errors.get(item_name));
          String tag_cls = is_error_item ? "error":"";
          s.append("<select name='"+item_name+"' id='"+item_name+"' class='"+tag_cls+"'>");
      }
      
      // get requst value
//      final String req_val = RequestLocator.get().getParameter(item_name);
      final String req_val = def_value;
      
      if (StringUtils.isNotEmpty(top_label)) {
          String val = "";
          String sel = val != null && req_val != null && val.equals(req_val) ? " selected" : "";
          s.append(String.format("<option value='%s'%s>%s</option>",
              "", sel, top_label));
      }
      
      final String class_name = getEnumPackage() + enum_simple_name;
      try {
          Class cls = Class.forName(class_name);
          Method m = cls.getMethod("getAllList", null);
          Field f = cls.getField(label_field);
          Collection col = (Collection) m.invoke(null, null);
          for (Iterator it = col.iterator(); it.hasNext();) {
              Object obj = (Object) it.next();
              Object obj_val = obj.toString();
              String val = obj_val == null ? null : obj_val.toString();
              String sel = val != null && req_val != null && val.equals(req_val) ? " selected" : "";
              s.append(String.format("<option value='%s'%s>%s</option>",
                  obj.toString(), sel, f.get(obj)));
              
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      s.append("</select>");
      
      return s.toString();
      
  }
  public static String getCheckboxsByEnum(String enum_simple_name, String item_name, String label_field) {
      return getCheckboxsByEnum(enum_simple_name, item_name, label_field, 100);
  }
  
  public static String getCheckboxsByEnum(String enum_simple_name, String item_name, String label_field, int column) {
      StringBuilder s = new StringBuilder();

      // 入力エラー表示対応
      String tag_cls = "";
      {
          ArrayMap<String, String> errors = (ArrayMap<String, String>) RequestLocator.get().getAttribute("errors");
          boolean is_error_item = (errors != null) && StringUtils.isNotEmpty(errors.get(item_name));
          tag_cls = is_error_item ? "error":"";
//          s.append("<select name='"+item_name+"' id='"+item_name+"' class='"+tag_cls+"'>");
      }
      
      // get requst value
//      final String req_val = RequestLocator.get().getParameter(item_name);
      final HttpServletRequest req = RequestLocator.get();

      final String class_name = getEnumPackage() + enum_simple_name;
      s.append("<table width='100%' class='no_line'>");

      try {
          Class cls = Class.forName(class_name);
          Method m = cls.getMethod("getAllList", null);
          Field f = cls.getField(label_field);
          Collection col = (Collection) m.invoke(null, null);
          int item_in_row = 0;
          for (Iterator it = col.iterator(); it.hasNext();) {
              Object obj = (Object) it.next();
              if ((item_in_row) % column == 0) {
                  s.append("<tr>");
              }
              Object obj_val = obj.toString();
              String val = obj_val == null ? null : obj_val.toString();
              String req_val = req.getParameter(val);
              String sel = "true".equals(req_val) ? " checked" : "";
              
              s.append(String.format(
                  "<td style='text-align:left' class='%s'><label><input type='checkbox' name='%s' value='%s' %s>%s</label></td>",
                  tag_cls, obj.toString(), true, sel, f.get(obj)));

              if ((++item_in_row) % column == 0) {
                  s.append("</tr>");
              }
          }
          
      } catch (Exception e) {
          e.printStackTrace();
      }
      s.append("</table>");
      
      return s.toString();
  }
  
  
  /**
   * @param enum_simple_name
   * @param item_name
   * @param label_field
   * @param column
   * @return
   */
  public static String getRadiosByEnum(String enum_simple_name, String item_name, String label_field, int column) {
      StringBuilder s = new StringBuilder();

      // 入力エラー表示対応
      String tag_cls = "";
      {
          ArrayMap<String, String> errors = (ArrayMap<String, String>) RequestLocator.get().getAttribute("errors");
          boolean is_error_item = (errors != null) && StringUtils.isNotEmpty(errors.get(item_name));
          tag_cls = is_error_item ? "error":"";
//          s.append("<select name='"+item_name+"' id='"+item_name+"' class='"+tag_cls+"'>");
      }
      
      // get requst value
//      final String req_val = RequestLocator.get().getParameter(item_name);
      final HttpServletRequest req = RequestLocator.get();

      final String class_name = getEnumPackage() + enum_simple_name;
      s.append("<table width='100%' class='no_line'>");

      try {
          Class cls = Class.forName(class_name);
          Method m = cls.getMethod("getAllList", null);
          Field f = cls.getField(label_field);
          Collection col = (Collection) m.invoke(null, null);
          int item_in_row = 0;
          for (Iterator it = col.iterator(); it.hasNext();) {
              Object obj = (Object) it.next();
              if ((item_in_row) % column == 0) {
                  s.append("<tr>");
              }
//              Object obj_val = obj.toString();
//              String val = obj_val == null ? null : obj_val.toString();
              String req_val = req.getParameter(item_name);
              if (req_val == null) {
                  req_val = req.getAttribute(item_name) != null ? req.getAttribute(item_name).toString() : null;
//                  req_val = (String) req.getAttribute(item_name);
              }
              String sel = obj.toString().equals(req_val) ? " checked" : "";
              
              s.append(String.format(
                  "<td style='text-align:left' class='%s'><label><input type='radio' name='%s' value='%s' %s>%s</label></td>",
                  tag_cls, item_name, obj.toString(), sel, f.get(obj)));
//              tag_cls, obj.toString(), true, sel, f.get(obj)));
                  
              if ((++item_in_row) % column == 0) {
                  s.append("</tr>");
              }
          }
          
      } catch (Exception e) {
          e.printStackTrace();
      }
      s.append("</table>");
      
      return s.toString();
  }
  /**
   * @param enum_simple_name
   * @param item_name
   * @param label_field
   * @param column
   * @return
   */
  public static String getRadiosByEnumNoBr(String enum_simple_name, String item_name, String label_field) {
      StringBuilder s = new StringBuilder();

      // 入力エラー表示対応
      String tag_cls = "";
      {
          ArrayMap<String, String> errors = (ArrayMap<String, String>) RequestLocator.get().getAttribute("errors");
          boolean is_error_item = (errors != null) && StringUtils.isNotEmpty(errors.get(item_name));
          tag_cls = is_error_item ? "error":"";
//          s.append("<select name='"+item_name+"' id='"+item_name+"' class='"+tag_cls+"'>");
      }
      
      // get requst value
//      final String req_val = RequestLocator.get().getParameter(item_name);
      final HttpServletRequest req = RequestLocator.get();

      final String class_name = getEnumPackage() + enum_simple_name;

      try {
          Class cls = Class.forName(class_name);
          Method m = cls.getMethod("getAllList", null);
          Field f = cls.getField(label_field);
          Collection col = (Collection) m.invoke(null, null);
          int item_in_row = 0;
          for (Iterator it = col.iterator(); it.hasNext();) {
              Object obj = (Object) it.next();
//              if ((item_in_row) % column == 0) {
//                  s.append("<tr>");
//              }
//              Object obj_val = obj.toString();
//              String val = obj_val == null ? null : obj_val.toString();
              String req_val = req.getParameter(item_name);
              if (req_val == null) {
                  req_val = req.getAttribute(item_name) != null ? req.getAttribute(item_name).toString() : null;
//                  req_val = (String) req.getAttribute(item_name);
              }
              String sel = obj.toString().equals(req_val) ? " checked" : "";
              
              s.append(String.format(
                  "<label><input type='radio' class='%s' name='%s' value='%s' %s>%s</label>",
                  tag_cls, item_name, obj.toString(), sel, f.get(obj)));
//              tag_cls, obj.toString(), true, sel, f.get(obj)));
                  
//              if ((++item_in_row) % column == 0) {
//                  s.append("</tr>");
//              }
          }
          
      } catch (Exception e) {
          e.printStackTrace();
      }
//      s.append("</table>");
      
      return s.toString();
  }

  /**
   * Enumクラスの val 値のラベルを返す
   * @param enum_simple_name
   * @param val
   * @return
   */
  public static String getEnumName(String enum_simple_name, Object val) {
      if (val == null || StringUtils.isEmpty(val.toString())) {
          return "";
      }
      
      final String class_name = getEnumPackage() + enum_simple_name;

      try {
          Class cls = Class.forName(class_name);
          Enum en = Enum.valueOf(cls, val.toString());
          // get Name field    Estype2Type > estype2Name
          String label_field = StringUtils.uncapitalize(enum_simple_name).replaceFirst("Type","Name");
          Field f = cls.getField(label_field);
          
          return (String) f.get(en);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return "";
  }
  
  /**
   * true:あり,false:なし を返す
   * @return
   */
  public static String getAriNasi(java.lang.String on) {
      return "on".equals(on) || "true".equals(on) ? "あり":"なし";
  }
  
  /**
   * val==true の場合 true_label, それ以外は false_label を返す
   * @param val
   * @param true_label
   * @param false_label
   * @return
   */
  public static String getBoolLabel(Boolean val, String true_label, String false_label) {
      return val ? true_label : false_label;
  }

  public static String cut(java.lang.String str, java.lang.Integer len) {
      if (StringUtils.isEmpty(str) || str.length() < len) {
          return str;
      }
      return str.substring(0, len);
  }

  public static String aPost(java.lang.String url, 
              java.lang.String name, java.lang.String val) {
      StringBuilder buf = new StringBuilder();
      final String hval = HtmlUtil.escape(val);
      final String id = HtmlUtil.escape("a_post_" + name + "_" +  val);
      
      buf.append("<script type=\"text/javascript\">\r\n");
      buf.append("<!--\r\n");
      buf.append(" $(function(){\r\n");
      buf.append("  $(\"#"+id+"\").click(function() {\r\n");
      buf.append("   var $form = $('<form action=\""+url+"\" method=\"POST\"></form>');\r\n");
      buf.append("   $form.append('<input type=\"hidden\" name=\""+HtmlUtil.escape(name)+"\" value=\""+hval+"\" />');\r\n");
      buf.append("   $form.appendTo(document.body);\r\n");
      buf.append("   $form.submit();\r\n");
      buf.append("   return false;\r\n");
      buf.append("  });\r\n");
      buf.append(" });\r\n");
      buf.append("// -->\r\n");
      buf.append("</script>\r\n");
      buf.append("<a href=\"javascript:void(0);\" id=\""+id+"\">\r\n");
      
      return buf.toString();
  }
  
  public static String endA() {
      return "</a>";
  }
  
  /**
   * wbrタグを入れて半角文字列を強制改行する.タグ自体は変更しない
   * @param string
   * @return
   */
  public static String wbr(String string) {
      if (string == null) {
          return "";
      }
      StringBuilder buf = new StringBuilder();
      boolean in_tag_flag = false;
      for (int i = 0; i < string.length(); i++) {
          char c = string.charAt(i);
          buf.append(c);
          if (c == '<' || c == '&') {
              in_tag_flag = true;
              continue;
          } else if (c == '>' || c == ';') {
              in_tag_flag = false;
              continue;
          }
          if (!in_tag_flag) {
              // 改行コード以外で追加
              if (Character.isLetterOrDigit(c)) {
                  buf.append("<wbr>");
              }
          }
      }
      return buf.toString();
  }

  private FormFunction() {
  }

}
