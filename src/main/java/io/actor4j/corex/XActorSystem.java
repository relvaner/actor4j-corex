package io.actor4j.corex;

import io.actor4j.core.ActorSystem;

public class XActorSystem extends ActorSystem {
	public XActorSystem() {
		this("x-actor4j", true);
	}
	
	public XActorSystem(boolean unbounded) {
		this("x-actor4j", unbounded);
	}

	public XActorSystem(String name, boolean unbounded) {
		super(name);
		setActorThreadClass(unbounded);
	}

	public void setActorThreadClass(boolean unbounded) {
		if (unbounded)
			system.setActorThreadClass(UnboundedActorThread.class);
		else
			system.setActorThreadClass(BoundedActorThread.class);
	}
}
