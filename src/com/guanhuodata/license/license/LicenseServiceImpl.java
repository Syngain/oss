package com.guanhuodata.license.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.guanhuodata.license.util.ThreeDES;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.ModuleLoader;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;

public class LicenseServiceImpl implements LicenseService{
    
	private String licensepath; //license.lic,需要解密成license.xml
	private static Map<String,Boolean> moduleMap = new HashMap<String,Boolean>();
    
    public License loadLicense() {
    	String templicensePath="";
        try{
        	templicensePath=licensepath.substring(0, licensepath.lastIndexOf("/"));
        	templicensePath = templicensePath + File.separator + "temp"; //解密后的临时文件
        	File file = new File(licensepath);
			int a = Integer.parseInt(Long.toString(file.length()));
			byte[] src = new byte[a];

			FileInputStream fis = new FileInputStream(licensepath);
			fis.read(src);
    		byte[] decodeBytes = ThreeDES.decryptMode(ThreeDES.keyBytes, src);
			FileOutputStream fos = new FileOutputStream(templicensePath);
			fos.write(decodeBytes);
           
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(templicensePath);
            NodeList nodeList = doc.getChildNodes();
            License license = new License();
            for(int i=0;i<nodeList.getLength();i++){
                if(nodeList.item(i).getNodeName().equals("license")){
                    NodeList subNodeList = nodeList.item(i).getChildNodes();
                    for(int x=0;x<subNodeList.getLength();x++){
                        if(subNodeList.item(x).getNodeName().equals("product-name")){
                            license.setProductName(subNodeList.item(x).getTextContent());
                        }else if(subNodeList.item(x).getNodeName().equals("version")){
                            license.setVersion(subNodeList.item(x).getTextContent());
                        }else if(subNodeList.item(x).getNodeName().equals("customer")){
                            license.setCustomer(subNodeList.item(x).getTextContent());
                        }else if(subNodeList.item(x).getNodeName().equals("issue-date")){
                            license.setIssueDate(subNodeList.item(x).getTextContent());
                        }else if(subNodeList.item(x).getNodeName().equals("expire-date")){
                            license.setExpireDate(Long.parseLong(subNodeList.item(x).getTextContent()));
                        }else if(subNodeList.item(x).getNodeName().equals("copyright")){
                            license.setCopyright(subNodeList.item(x).getTextContent());
                        }else if(subNodeList.item(x).getNodeName().equals("autz")){
                            NodeList moduleNodes = subNodeList.item(x).getChildNodes();
                            license.setLicenseModules(new ArrayList<LicenseModule>());
                            for(int y=0;y<moduleNodes.getLength();y++){
                                LicenseModule licM = parseLicenseModule(moduleNodes.item(y),"");
                                if(licM != null){
                                    license.getLicenseModules().add(licM);
                                }
                            }
                        }else if(subNodeList.item(x).getNodeName().equals("signature")){
                            license.setSignature(subNodeList.item(x).getTextContent());
                        }
                    }
                }
            }
            fis.close();
            fos.close();
            fos=null;
            
            return license;
        }catch(IOException ex){
            Logger.getLogger(LicenseServiceImpl.class).log(LogLevel.ERROR,ex.getMessage());
        }catch(SAXException ex){
            Logger.getLogger(LicenseServiceImpl.class).log(LogLevel.ERROR,ex.getMessage());
        }catch(ParserConfigurationException ex){
            Logger.getLogger(LicenseServiceImpl.class).log(LogLevel.ERROR,ex.getMessage());
        }catch(Exception ex){
            Logger.getLogger(LicenseServiceImpl.class).log(LogLevel.ERROR,ex.getMessage());
        }finally{
        	File file=new File(templicensePath);
        	if(file.isFile()){
        		file.delete();
        	}
        }
        return null;
    }

    public boolean isLicensed(String id) {
        List<Boolean> finalResult = new ArrayList<Boolean>();
        if(id != null && id.trim().length()>0){
            //正则表达式对于 "." Java需要转义处理，需要注意此行代码！一旦CoreConstants.ID_CONNECTOR有变化，
            //并且不用转义，则此行代码存在问题！！！
            String[] ids = id.split("\\" + CoreConstants.ID_CONNECTOR);
            String destId ="";
            for(int i=0;i<ids.length;i++){
                destId = destId + ids[i] + CoreConstants.ID_CONNECTOR;
                if(moduleMap.containsKey(destId.substring(0,destId.lastIndexOf(CoreConstants.ID_CONNECTOR)))){
                    finalResult.add(moduleMap.get(destId.substring(0,destId.lastIndexOf(CoreConstants.ID_CONNECTOR))));
                }
            }
            for(Boolean b:finalResult){
                if(!b) return false;
            }
            return true;
        }
        return false;
    }
    
    private LicenseModule parseLicenseModule(Node node,String moduleId){
        if(node.getNodeName().equals("module")){
            LicenseModule module = new LicenseModule();
            module.setLicenseModules(new ArrayList<LicenseModule>(5));
            module.setLicenseModuleFunctions(new ArrayList<LicenseModuleFunction>(5));
            NamedNodeMap nodeAttrs = node.getAttributes();
            for (int idx = 0; idx < nodeAttrs.getLength(); idx++) {
                Node attr = nodeAttrs.item(idx);
                if (attr.getNodeName().equals("id")) {
                    if(moduleId != null && moduleId.length()>0){
                        moduleId = moduleId + CoreConstants.ID_CONNECTOR + attr.getNodeValue();
                    }else{
                        moduleId = attr.getNodeValue();
                    }
                    module.setId(moduleId);
                }else if (attr.getNodeName().equals("description")) {
                    module.setDescription(attr.getNodeValue());
                }else if(attr.getNodeName().equals("enabled")){
                    String enableValue = attr.getNodeValue();
                    if(enableValue.equalsIgnoreCase("yes")){
                        module.setEnabled(true);
                    }else{
                        module.setEnabled(false);
                    }
                }
            }
            moduleMap.put(module.getId(),module.isEnabled());
            NodeList subNodes = node.getChildNodes();
            for (int j = 0; j < subNodes.getLength(); j++) {
                if (subNodes.item(j).getNodeName().equals("module")) {
                    LicenseModule licM = parseLicenseModule(subNodes.item(j),moduleId);
                    if(licM != null){
                        module.getLicenseModules().add(licM);
                    }
                }else if(subNodes.item(j).getNodeName().equals("function")){
                    LicenseModuleFunction mf = parseLicenseModuleFunction(subNodes.item(j),moduleId);
                    if(mf != null){
                        module.getLicenseModuleFunctions().add(mf);
                    }
                }
                
            }
            return module;
        }
        return null;
    }
    
    private LicenseModuleFunction parseLicenseModuleFunction(Node functionNodes,String fid){
        if(functionNodes.getNodeName().equals("function")){
            LicenseModuleFunction lmf = new LicenseModuleFunction();
            lmf.setLicenseModuleFunctions(new ArrayList<LicenseModuleFunction>(5));
            NamedNodeMap functionAttrs = functionNodes.getAttributes();
            for(int y=0;y<functionAttrs.getLength();y++){
                Node functionAttr = functionAttrs.item(y);
                if(functionAttr.getNodeName().equals("id")){
                    if(fid==null || fid.trim().length()==0){
                        Logger.getLogger(ModuleLoader.class).log(LogLevel.ERROR,"Malformed license format! Check the license.xml.");
                        throw new RuntimeException("Malformed license format! Check the license.xml.");
                    }
                    fid = fid + CoreConstants.ID_CONNECTOR + functionAttr.getNodeValue();
                    lmf.setId(fid);
                }else if(functionAttr.getNodeName().equals("description")){
                    lmf.setDescription(functionAttr.getNodeValue());
                }else if(functionAttr.getNodeName().equals("enabled")){
                    String enableValue = functionAttr.getNodeValue();
                    if(enableValue.equalsIgnoreCase("yes")){
                        lmf.setEnabled(true);
                    }else{
                        lmf.setEnabled(false);
                    }
                }
            }
            moduleMap.put(lmf.getId(),lmf.isEnabled());
            NodeList subFunctionNodes = functionNodes.getChildNodes();
            for(int z=0;z<subFunctionNodes.getLength();z++){
                if(subFunctionNodes.item(z).getNodeName().equals("function")){
                    LicenseModuleFunction licMF = parseLicenseModuleFunction(subFunctionNodes.item(z),fid);
                    if(licMF != null){
                        lmf.getLicenseModuleFunctions().add(licMF);
                    }
                }
            }
            return lmf;
        }
        return null;
    }


	public void setLicensepath(String licensepath) {
		this.licensepath = licensepath;
	}
	
	public static void main(String[] args) {
		LicenseServiceImpl l=new LicenseServiceImpl();
		l.licensepath="E:\\works\\CTOMS\\ctoms\\ctoms-web\\WebContent\\license\\license.lic";
		System.out.println(l.loadLicense().getLicenseModules());
	}
}
