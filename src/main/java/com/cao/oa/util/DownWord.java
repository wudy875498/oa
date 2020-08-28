package com.cao.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class DownWord {
	
	public static final String REPLACE_ALL_TITLE = "n0";
	public static final String REPLACE_PROJECT_TITLE_NAME = "n6";
	public static final String REPLACE_PROJECT_NAME = "n1";
	public static final String REPLACE_SUBMIT_PERSON = "n2";
	public static final String REPLACE_SUBMIT_TIME = "n3";
	public static final String REPLACE_SUBMIT_PART = "n4";
	public static final String REPLACE_SUBMIT_GROUP = "n5";
	public static final String REPLACE_ID = "nid";
	public static final String REPLACE_FU_JIAN = "fuJian";
	public static final String REPLACE_BEI_ZHU = "beiZhu";
	
	public static final int PASS_OK = 0;
	public static final int PASS_NO = 1;
	public static final int PASS_WORKING = 2;
	public static final int PASS_OTHER = 3;
	
	/**
	 * �����ļ�
	 * @param filePath
	 * @param name
	 * @param info
	 * @param opt
	 * @param shen
	 * @param isPass
	 * @return
	 */
	@SuppressWarnings("finally")
	public boolean createFile(String filePath, String name, Map<String,String> info,Map<Integer,Map<String,String>> opt,Map<Integer,Map<String,String>> shen,int isPass) {
		boolean isOk = false;
		String fileName = "example3-1.xml";
		String newFileName = name+".xml";
		File file = new File(filePath,fileName);
		File newFile = new File(filePath,newFileName);
		System.out.println("==>1:newFile"+newFile.getAbsolutePath());
		System.out.println("==>1:file"+file.getAbsolutePath());
		FileInputStream fis = null;
		try {
			if(file.exists() && file.isFile()) {
				String fileStr = "";
				fis = new FileInputStream(file);
				byte[] b = new byte[1024];
				int len = 0;
				while((len=fis.read(b))!=-1) {
					fileStr += new String(b,0,len,"utf-8");
				}
				System.out.println(fileStr);
				
				//�滻��������
				fileStr = changeContent(fileStr,REPLACE_ALL_TITLE,info.get(REPLACE_ALL_TITLE));
				fileStr = changeContent(fileStr,REPLACE_PROJECT_TITLE_NAME,info.get(REPLACE_PROJECT_TITLE_NAME));
				fileStr = changeContent(fileStr,REPLACE_PROJECT_NAME,info.get(REPLACE_PROJECT_NAME));
				fileStr = changeContent(fileStr,REPLACE_SUBMIT_PERSON,info.get(REPLACE_SUBMIT_PERSON));
				fileStr = changeContent(fileStr,REPLACE_SUBMIT_TIME,info.get(REPLACE_SUBMIT_TIME));
				fileStr = changeContent(fileStr,REPLACE_SUBMIT_PART,info.get(REPLACE_SUBMIT_PART));
				fileStr = changeContent(fileStr,REPLACE_SUBMIT_GROUP,info.get(REPLACE_SUBMIT_GROUP));
				fileStr = changeContent(fileStr,REPLACE_ID,info.get(REPLACE_ID));
				fileStr = changeContent(fileStr,REPLACE_FU_JIAN,info.get(REPLACE_FU_JIAN));
				fileStr = changeContent(fileStr,REPLACE_BEI_ZHU,info.get(REPLACE_BEI_ZHU));
				System.out.println("++0+");
				//�滻��ѡ��
				String option = "";
				for(int i=1;i<=opt.size();i++) {
					String opt1 = "";
					String opt2 = "";
					opt1 = createOptin(opt.get(i).get("title"),opt.get(i).get("content"));
					i++;
					System.out.println("++"+i);
					if(i<=opt.size()) {
						//����
						opt2 = createOptin(opt.get(i).get("title"),opt.get(i).get("content"));
					}else {
						//û��
						opt2 = createOptionBlank();
					}
					option += createOptionLine(opt1+opt2);
				}
				System.out.println("+++");
				System.out.println(option);
				System.out.println("+++");
				fileStr = replaceOption(fileStr,option);
				
				//�滻����
				String shenStr = "";
				for(int i=1;i<=shen.size();i++) {
					shenStr += createShen(shen.get(i).get("name"), shen.get(i).get("time"), 
							shen.get(i).get("title"), shen.get(i).get("content"));
				}
				fileStr = replaceShen(fileStr, shenStr);
				if(isPass==PASS_OK) {
					fileStr = changeResult(fileStr,"green","����ͨ��");
				}else if(isPass==PASS_NO) {
					fileStr = changeResult(fileStr,"red","��������");
				}else if(isPass==PASS_WORKING) {
					fileStr = changeResult(fileStr,"yellow","������");
				}else {
					fileStr = changeResult(fileStr,"black","����");
				}
				
				//�����ļ�
				newFile.createNewFile();
				System.out.println("DW->"+newFile.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(newFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
				osw.write(fileStr);
				osw.flush();
				osw.close();
				System.out.println("ok");
				DocUtil du = new DocUtil();
				
				
				Map<String,Object> dataMap = new HashMap<>();
				dataMap.put("ot12", "123");
				du.createDoc(filePath,dataMap,name);
				isOk = true;
			}else {
				System.out.println("ģ���ļ�������");
			}
			System.out.println("newFileName:"+newFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return isOk;
		}
	}
	
	/**
	 * �滻�ض�����
	 * @param source
	 * @param find
	 * @param last
	 * @return
	 */
	public String changeContent(String source,String find,String last) {
		if(last==null){
			last = "";
		}
		String toChange = last;//�ĳ����
		String toFind = "${"+find+"}";//�޸����
		return source.replace(toFind, toChange);
	}
	
	/**
	 * ������һ��ѡ��
	 * @param title
	 * @param content
	 * @return
	 */
	public String createOptin(String title,String content) {
		return "<w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"000C0FCF\"><w:pPr><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr>"
				+ "<w:t></w:t></w:r><w:r>"
				+ "<w:t>"+title+"</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"000C0FCF\"><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr>"
				+ "<w:t></w:t></w:r><w:r><w:t>"+content+"</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r></w:p></w:tc>";
	}
	/**
	 * ����һ����ѡ����
	 * @param source
	 * @return
	 */
	public String createOptionLine(String source) {
		String modelBegin = "<w:tr w:rsidR=\"0002119F\" w:rsidTr=\"00D27BB8\">";
		String modelEnd = "</w:tr>";
			return modelBegin+source+modelEnd;
	}
	/**
	 * ����һ����ѡ��հ�
	 * @return
	 */
	public String createOptionBlank() {
		return "<w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"00C51DD4\" w:rsidRDefault=\"00C51DD4\" w:rsidP=\"000C0FCF\"><w:r><w:t></w:t></w:r><w:r><w:t></w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"00C51DD4\" w:rsidRDefault=\"00C51DD4\" w:rsidP=\"000C0FCF\"><w:r><w:t></w:t></w:r><w:r><w:t></w:t></w:r></w:p></w:tc>";
	}
	
	
	/**
	 * ��������
	 * @param sname
	 * @param stime
	 * @param stitle
	 * @param scontent
	 * @return
	 */
	public String createShen(String sname,String stime,String stitle,String scontent) {
		return "<w:tr w:rsidR=\"0002119F\" w:rsidTr=\"0002119F\">"
				+ "<w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/>"
				+ "</w:tcPr><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:r>"
				+ "<w:t>"+stitle+"</w:t>"
				+ "</w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"6222\" w:type=\"dxa\"/><w:gridSpan w:val=\"3\"/></w:tcPr><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:pPr><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr></w:pPr><w:r>"
				+ "<w:t>"+scontent+"</w:t>"
				+ "</w:r><w:bookmarkStart w:id=\"0\" w:name=\"_GoBack\"/><w:bookmarkEnd w:id=\"0\"/></w:p><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:pPr><w:jc w:val=\"right\"/></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t>ǩ����</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr>"
				+ "<w:t></w:t></w:r><w:r><w:t>"+sname+"</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t xml:space=\"preserve\">   "
				+ "</w:t>"
				+ "</w:r></w:p><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:pPr><w:jc w:val=\"right\"/></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr>"
				+ "<w:t>ʱ�䣺</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr>"
				+ "<w:t></w:t></w:r><w:r><w:t>"+stime+"</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t xml:space=\"preserve\">  </w:t></w:r></w:p></w:tc></w:tr>";
	}
	
	/**
	 * �滻��ѡ��
	 * @param source
	 * @param replace
	 * @return
	 */
	public String replaceOption(String source,String replace) {
		String model = "<w:tr w:rsidR=\"0097405A\" w:rsidTr=\"0002119F\"><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0097405A\" w:rsidRDefault=\"0002119F\" w:rsidP=\"000C0FCF\"><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r><w:r><w:t>${ot1}</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0097405A\" w:rsidRDefault=\"0002119F\" w:rsidP=\"000C0FCF\"><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r><w:r><w:t>${oc1}</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0097405A\" w:rsidRDefault=\"0002119F\" w:rsidP=\"000C0FCF\"><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r><w:r><w:t>${ot2}</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0097405A\" w:rsidRDefault=\"0002119F\" w:rsidP=\"000C0FCF\"><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r><w:r><w:t>${oc2}</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r></w:p></w:tc></w:tr>";
		return source.replace(model, replace);
	}
	
	/**
	 * �滻����
	 * @param source
	 * @param replace
	 * @return
	 */
	public String replaceShen(String source,String replace) {
		String model = "<w:tr w:rsidR=\"0002119F\" w:rsidTr=\"0002119F\"><w:tc><w:tcPr><w:tcW w:w=\"2074\" w:type=\"dxa\"/></w:tcPr><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:r><w:t>${stitle1}</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"6222\" w:type=\"dxa\"/><w:gridSpan w:val=\"3\"/></w:tcPr><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:r><w:t>${scontent1}</w:t></w:r></w:p><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:pPr><w:jc w:val=\"right\"/></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t>ǩ����</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r><w:r><w:t>${sname1}</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t xml:space=\"preserve\">   </w:t></w:r></w:p><w:p w:rsidR=\"0002119F\" w:rsidRDefault=\"0002119F\" w:rsidP=\"0097405A\"><w:pPr><w:jc w:val=\"right\"/></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t>ʱ�䣺</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t></w:t></w:r><w:r><w:t>${stime1}</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\"/></w:rPr><w:t xml:space=\"preserve\">  </w:t></w:r></w:p></w:tc></w:tr>";
		return source.replace(model, replace);
	}
	
	/**
	 * ˮӡ�������ս��
	 * @param source
	 * @param color
	 * @param str
	 * @return
	 */
	public String changeResult(String source,String color,String str) {
		String model = "fillcolor=\"red\" stroked=\"f\"><v:fill opacity=\".5\"/><v:textpath style=\"font-family:&quot;����&quot;;font-size:1pt\" string=\"����ͨ��\"";
		String change = "fillcolor=\""+color+"\" stroked=\"f\"><v:fill opacity=\".5\"/><v:textpath style=\""
				+ "font-family:&quot;����&quot;;font-size:1pt\" "
				+ "string=\""+str+"\"";
		return source.replaceAll(model, change);
	}
}
