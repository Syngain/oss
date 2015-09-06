package com.guanhuodata.framework.core;

import java.util.List;
import java.util.Properties;

public class Module implements java.io.Serializable {
	
	private static final long serialVersionUID = 8392239250089313698L;
	private String id;
	private String name;
	private String version;
	private List<ModuleFunction> functions;
	private Properties moduleProperties;
	private List<Module> moduleList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<ModuleFunction> getFunctions() {
		return functions;
	}

	public void setFunctions(List<ModuleFunction> functions) {
		this.functions = functions;
	}

	public Properties getModuleProperties() {
		return moduleProperties;
	}

	public void setModuleProperties(Properties moduleProperties) {
		this.moduleProperties = moduleProperties;
	}

	public List<Module> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<Module> moduleList) {
		this.moduleList = moduleList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Module other = (Module) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
