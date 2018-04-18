package com.bim.util;

import java.io.IOException;

public class XMLUtil {
	/**
	 * 根据节点数据集合，生成XML
	 * @param treeNodes 权节点集合
	 * @return List<BimColor> treeNodes,String path
	 */
	public static void parseNodeToXML() {
		/*StringBuffer xmlnodes = new StringBuffer();
		if (treeNodes != null && treeNodes.size() > 0) {
			xmlnodes.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xmlnodes.append("<ElectronicColor>");
			for (int i = 0; i < treeNodes.size(); i++) {
				xmlnodes.append("<Node>");
				xmlnodes.append("<id>"+treeNodes.get(i).getId()+"</id>");
				xmlnodes.append("<propertyName>"+treeNodes.get(i).getPropertyName()+"</propertyName>");
				xmlnodes.append("<systemType>"+treeNodes.get(i).getSystemType()+"</systemType>");
				xmlnodes.append("<annotation>"+treeNodes.get(i).getAnnotation()+"</annotation>");
				xmlnodes.append("<projectId>"+treeNodes.get(i).getProjectId()+"</projectId>");
				xmlnodes.append("<rgbValue>"+treeNodes.get(i).getRgbValue().replaceAll("，", ",")+"</rgbValue>");
				xmlnodes.append("<professionName>"+treeNodes.get(i).getProfessionName()+"</professionName>");
				xmlnodes.append("<professionCode>"+treeNodes.get(i).getProfessionCode()+"</professionCode>");
				xmlnodes.append("<text></text>");
				xmlnodes.append("</Node>");
			}
			xmlnodes.append("</ElectronicColor>");
			try {
				File distFile = new File(path);
	            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile), "UTF8"));
	            out.write(xmlnodes.toString());
	            out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	public static void main(String[] args) throws IOException {
		/*String xmlFile = new XMLUtil().parseNodeToXML(list);
		FileWriter writer = new FileWriter("E:/node.xml");
		writer.write(xmlFile);
		writer.flush();
		writer.close();
		System.out.println("写入完成！");*/
	}
}
