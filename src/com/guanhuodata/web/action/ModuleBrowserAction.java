package com.guanhuodata.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.core.ModuleFunction;
import com.guanhuodata.framework.core.ModuleRegistry;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;

public class ModuleBrowserAction implements Action {
    
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Logger.getLogger(ModuleBrowserAction.class).log(LogLevel.DEBUG,"ModuleBrowser Action Invoked.");
        String moduleId = request.getParameter("mid");
        if(moduleId != null){
            Module m = ModuleRegistry.getModule(moduleId);
            if(m != null){
                printModule(m,out);
            }else{
                out.println("Can not find specified module with moduleid: " + moduleId);
            }
        }else{
            for(Iterator<Module> it = ModuleRegistry.getRegisteredModules().iterator();it.hasNext();){
                printModule(it.next(),out);
            }
        }
        out.flush();
        out.close();
    }
    private void printModule(Module m,PrintWriter out){
        out.print("============ Module " + m.getName() + " ============<br/>");
        out.print("Detail:<br/>");
        out.print( "Essentail:");
        out.print( "[id]: " + m.getId() + " [name]: " + m.getName() + " [version]: " + m.getVersion() + "<br/>");
        Properties p = m.getModuleProperties();
        if(p != null){
            out.println("Properties: <br/>");
            Set<Entry<Object,Object>> pSet = p.entrySet();
            for (Iterator<Entry<Object,Object>> it = pSet.iterator(); it.hasNext();) {
                Entry<Object,Object> entry = it.next();
                out.print("name=" + entry.getKey() + "\tvalue=" + entry.getValue() + "<br/>");
            }
        }
        List<ModuleFunction> functions = m.getFunctions();
        if (functions != null) {
            out.println("Functions: <br/>");
            for (ModuleFunction mf : functions) {
                printFunction(mf,out);
            }
        }
        List<Module> mlist = m.getModuleList();
        for (Module sm : mlist) {
            printModule(sm,out);
        }
    }
    private void printFunction(ModuleFunction f, PrintWriter out) {
        out.print("[id]: " + (f.getId() == null ? "NULL" : f.getId()) + " [name]: " + (f.getName() == null ? "NULL" : f
                .getName()) + " [license-id]:" + (f.getLicenseId() == null ? "NULL" : f.getLicenseId()) + "<br/>");
        List<ModuleFunction> flist = f.getFunctionList();
        for (ModuleFunction mf : flist) {
            printFunction(mf,out);
        }
    }
}
