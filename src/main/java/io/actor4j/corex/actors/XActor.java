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
package io.actor4j.corex.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.actor4j.core.ActorCell;
import io.actor4j.core.actors.Actor;
import io.actor4j.core.exceptions.ActorInitializationException;
import io.actor4j.corex.di.DefaultDIContainer;

public abstract class XActor extends Actor {
	public XActor() {
		super();
	}

	public XActor(String name) {
		super(name);
	}
	
	public UUID addChild(Class<? extends Actor> clazz, Object... args) throws ActorInitializationException {
		ActorCell cell = getSystem().underlyingImpl().generateCell(clazz);
		if (getSystem().underlyingImpl().getContainer() instanceof DefaultDIContainer)
			((DefaultDIContainer<UUID>)getSystem().underlyingImpl().getContainer()).registerConstructorInjector(cell.getId(), clazz, args);
		else
			throw new ActorInitializationException();
		try {
			Actor child = (Actor)getSystem().underlyingImpl().getContainer().getInstance(cell.getId());
			cell.setActor(child);
		} catch (Exception e) {
			throw new ActorInitializationException();
		}
		
		return getCell().internal_addChild(cell);
	}
	
	public List<UUID> addChild(int instances, Class<? extends Actor> clazz, Object... args) throws ActorInitializationException {
		List<UUID> result = new ArrayList<>(instances);
		
		for (int i=0; i<instances; i++)
			result.add(addChild(clazz, args));
		
		return result;
	}
}
