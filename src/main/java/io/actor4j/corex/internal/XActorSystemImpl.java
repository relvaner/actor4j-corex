/*
 * Copyright (c) 2015-2021, David A. Bauer. All rights reserved.
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
package io.actor4j.corex.internal;

import static io.actor4j.core.utils.ActorUtils.UUID_ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.actor4j.core.ActorSystem;
import io.actor4j.core.actors.Actor;
import io.actor4j.core.exceptions.ActorInitializationException;
import io.actor4j.core.internal.ActorCell;
import io.actor4j.core.internal.ActorSystemImpl;
import io.actor4j.core.internal.DefaultActorMessageDispatcher;
import io.actor4j.corex.internal.di.DefaultDIContainer;
import io.actor4j.corex.internal.pods.XPodReplicationController;

public class XActorSystemImpl extends ActorSystemImpl {
	public XActorSystemImpl(ActorSystem wrapper) {
		this(null, true, wrapper);
	}

	public XActorSystemImpl(String name, boolean unbounded, ActorSystem wrapper) {
		super(name, wrapper);
		
		container = DefaultDIContainer.create(); // override
		podReplicationController = new XPodReplicationController(this); // override
		
		messageDispatcher = new DefaultActorMessageDispatcher(this);
		setActorThread(unbounded);
	}
	
	public void setActorThread(boolean unbounded) {
		if (unbounded)
			actorThreadFactory = (group, n, system) -> new UnboundedActorThread(group, n, system);
		else
			actorThreadFactory = (group, n, system) -> new BoundedActorThread(group, n, system);
	}
	
	public List<UUID> addActor(int instances, Class<? extends Actor> clazz, Object... args) throws ActorInitializationException {
		List<UUID> result = new ArrayList<>(instances);
		
		for (int i=0; i<instances; i++)
			result.add(addActor(clazz, args));
		
		return result;	
	}
	
	public UUID addActor(Class<? extends Actor> clazz, Object... args) throws ActorInitializationException {
		ActorCell cell = generateCell(clazz);
		((DefaultDIContainer<UUID>)container).registerConstructorInjector(cell.getId(), clazz, args);
		Actor actor = null;
		try {
			actor = (Actor)container.getInstance(cell.getId());
			cell.setActor(actor);
		} catch (Exception e) {
			e.printStackTrace();
			executerService.getFailsafeManager().notifyErrorHandler(new ActorInitializationException(), "initialization", null);
		}
		
		return (actor!=null) ? user_addCell(cell) : UUID_ZERO;
	}
}
