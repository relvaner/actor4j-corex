/*
 * Copyright (c) 2015-2019, David A. Bauer. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.actor4j.corex;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import io.actor4j.core.ActorSystem;
import io.actor4j.core.DefaultActorSystemImpl;

public class XActorSystemImpl extends DefaultActorSystemImpl {
	protected final AtomicBoolean antiFloodingEnabled;
	protected /*quasi final*/ Supplier<XAntiFloodingTimer> factoryAntiFloodingTimer;
	
	public XActorSystemImpl(ActorSystem wrapper) {
		this(null, wrapper, false);
	}
	
	public XActorSystemImpl(ActorSystem wrapper, boolean unbounded) {
		this(null, wrapper, unbounded);
	}
	
	public XActorSystemImpl(String name, ActorSystem wrapper) {
		this(name, wrapper, false);
	}

	public XActorSystemImpl(String name, ActorSystem wrapper, boolean unbounded) {
		super(name, wrapper);
		
		antiFloodingEnabled = new AtomicBoolean(false);
		
		messageDispatcher = new XActorMessageDispatcher(this);
		actorThreadClass = XActorThread.class;
		
		setActorThreadClass(unbounded); // override default
	}
	
	public void setActorThreadClass(boolean unbounded) {
		if (unbounded)
			actorThreadClass  = XUnboundedActorThread.class;
		else
			actorThreadClass  = XBoundedActorThread.class;
	}
	
	public void setFactoryAntiFloodingTimer(Supplier<XAntiFloodingTimer> factoryAntiFloodingTimer) {
		this.factoryAntiFloodingTimer = factoryAntiFloodingTimer;
	}

	@Override
	public void start(Runnable onStartup, Runnable onTermination) {
		if (factoryAntiFloodingTimer==null)
			factoryAntiFloodingTimer =  () -> new XAntiFloodingTimer(queueSize*2, 5_000);
		super.start(onStartup, onTermination);
	}
}
