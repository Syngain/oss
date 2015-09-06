package com.guanhuodata.framework.core;

public class DefaultActionLookup implements ActionLookup {

	@Override
	public Action getAction(String actionid,ServiceContext ctx) {
		return ctx.getService(actionid,Action.class);
	}
}
