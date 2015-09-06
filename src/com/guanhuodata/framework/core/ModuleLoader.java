package com.guanhuodata.framework.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;

public class ModuleLoader {
	public List<Module> loadModules(){
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    List<Module> moduleList = new ArrayList<Module>();
	    try{
	        DocumentBuilder builder = dbf.newDocumentBuilder();
	        Enumeration<URL> resourceUrls = Thread.currentThread().getContextClassLoader().getResources("conf/module.xml");
	        while (resourceUrls.hasMoreElements()) {  
                URL url = resourceUrls.nextElement();
                InputStream in = url.openStream();
                Document doc = builder.parse(in);
                NodeList nodeList = doc.getChildNodes();
                for(int i=0;i<nodeList.getLength();i++){
                    if(nodeList.item(i).getNodeName().equals("modules")){
                        NodeList moduleNodeList = nodeList.item(i).getChildNodes();
                        for(int x=0;x<moduleNodeList.getLength();x++){
                            Module module = parseModule(moduleNodeList.item(x),"");
                            if(module != null){
                                moduleList.add(module);
                            }
                        }
                    }
                }
	        }
	    }catch(IOException ex){
	        Logger.getLogger(ModuleLoader.class).log(LogLevel.ERROR,ex.getMessage());
	    }catch(SAXException ex){
            Logger.getLogger(ModuleLoader.class).log(LogLevel.ERROR,ex.getMessage());
        }catch(ParserConfigurationException ex){
            Logger.getLogger(ModuleLoader.class).log(LogLevel.ERROR,ex.getMessage());
        }
	    return moduleList;
	}
	
	private Module parseModule(Node node,String moduleId){
	    if(node.getNodeName().equals("module")){
	        Module module = new Module();
	        module.setModuleList(new ArrayList<Module>(5));
	        NamedNodeMap nodeAttrs = node.getAttributes();
	        for (int idx = 0; idx < nodeAttrs.getLength(); idx++) {
                Node attr = nodeAttrs.item(idx);
                if (attr.getNodeName().equals("id")) {
                    //sub module id and it's parent module id must be catenated;
                    if(moduleId != null && moduleId.length()>0){
                        moduleId = moduleId + CoreConstants.ID_CONNECTOR + attr.getNodeValue();
                    }else{
                        moduleId = attr.getNodeValue();
                    }
                    module.setId(moduleId);
                } else if (attr.getNodeName().equals("name")) {
                    module.setName(attr.getNodeValue());
                }else if(attr.getNodeName().equals("version")){
                    module.setVersion(attr.getNodeValue());
                }
            }
	        NodeList subNodes = node.getChildNodes();
	        for (int j = 0; j < subNodes.getLength(); j++) {
	            //parse properties
                if (subNodes.item(j).getNodeName().equals("properties")) {
                    module.setModuleProperties(new Properties());
                    NodeList propertySubNodes = subNodes.item(j).getChildNodes();
                    for(int x=0;x<propertySubNodes.getLength();x++){
                        String key="",value="";
                        if(propertySubNodes.item(x).getNodeName().equals("property")){
                            NamedNodeMap propAttrs = propertySubNodes.item(x).getAttributes();
                            for(int y=0;y<propAttrs.getLength();y++){
                                Node propAttr = propAttrs.item(y);
                                if(propAttr.getNodeName().equals("name")){
                                    key = propAttr.getNodeValue();
                                }else if(propAttr.getNodeName().equals("value")){
                                    value = propAttr.getNodeValue();
                                }
                            }
                            module.getModuleProperties().put(key,value);
                        }
                    }
                }
                //parse authority-functions.
                if(subNodes.item(j).getNodeName().equals("authority-functions")){
                    module.setFunctions(new ArrayList<ModuleFunction>(5));
                    NodeList functionNodes = subNodes.item(j).getChildNodes();
                    for(int x=0;x<functionNodes.getLength();x++){
                        //recursive function
                        ModuleFunction mf = parseModuleFunction(functionNodes.item(x),moduleId);
                        if(mf != null){
                            module.getFunctions().add(mf);
                        }
                    }
                }
                //recursive module
                if (subNodes.item(j).getNodeName().equals("module")) {
                    module.getModuleList().add(parseModule(subNodes.item(j),moduleId));
                }
            }
            return module;
	    }
	    return null;
	}
	
	private ModuleFunction parseModuleFunction(Node functionNodes,String fid){
	    if(functionNodes.getNodeName().equals("function")){
            ModuleFunction mf = new ModuleFunction();
            mf.setFunctionList(new ArrayList<ModuleFunction>(5));
            NamedNodeMap functionAttrs = functionNodes.getAttributes();
            for(int y=0;y<functionAttrs.getLength();y++){
                Node functionAttr = functionAttrs.item(y);
                if(functionAttr.getNodeName().equals("id")){
                    //id: Must add the module id;
                    if(fid==null || fid.trim().length()==0){
                        Logger.getLogger(ModuleLoader.class).log(LogLevel.ERROR,"Malformed module format! Check the module.xml.");
                        throw new RuntimeException("Malformed module format! Check the module.xml.");
                    }
                    fid = fid + CoreConstants.ID_CONNECTOR + functionAttr.getNodeValue();
                    mf.setId(fid);
                }else if(functionAttr.getNodeName().equals("name")){
                    mf.setName(functionAttr.getNodeValue());
                }else if(functionAttr.getNodeName().equals("license-id")){
                    mf.setLicenseId(functionAttr.getNodeValue());
                }else if(functionAttr.getNodeName().equals("type")){
                	mf.setType(functionAttr.getNodeValue());
                }
            }
            NodeList functionList = functionNodes.getChildNodes();
            for(int x=0;x<functionList.getLength();x++){
                ModuleFunction subMf = parseModuleFunction(functionList.item(x),fid);
                if(subMf != null){
                    mf.getFunctionList().add(subMf);
                }
            }
            return mf;
        }
	    return null;
	}
}
