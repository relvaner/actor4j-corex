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

public class AntiFloodingActorSystemImpl extends DefaultActorSystemImpl {
	protected final AtomicBoolean peakLoadHandlingEnabled;
	protected /*quasi final*/ Supplier<AntiFloodingTimer> factoryAntiFloodingTimer;
	
	public AntiFloodingActorSystemImpl(ActorSystem wrapper) {
		this(null, wrapper);
	}
	
	public AntiFloodingActorSystemImpl(String name, ActorSystem wrapper) {
		super(name, wrapper);
		
		peakLoadHandlingEnabled = new AtomicBoolean(true);
		
		messageDispatcher = new AntiFloodingActorMessageDispatcher(this);
		actorThreadClass = AntiFloodingActorThread.class;
	}
	
	public void setFactoryAntiFloodingTimer(Supplier<AntiFloodingTimer> factoryAntiFloodingTimer) {
		this.factoryAntiFloodingTimer = factoryAntiFloodingTimer;
	}

	@Override
	public void start(Runnable onStartup, Runnable onTermination) {
		if (factoryAntiFloodingTimer==null)
			factoryAntiFloodingTimer =  () -> new AntiFloodingTimer(queueSize*2, 5_000);
		super.start(onStartup, onTermination);
	}
}
