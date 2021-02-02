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
package io.actor4j.corex;

import java.util.List;
import java.util.UUID;

import io.actor4j.core.ActorService;
import io.actor4j.core.actors.Actor;
import io.actor4j.core.exceptions.ActorInitializationException;

public class XActorService extends ActorService {
	public XActorService() {
		this(null, true);
	}
	
	public XActorService(String name) {
		this(name, true);
	}

	public XActorService(boolean unbounded) {
		this(null, unbounded);
	}

	public XActorService(String name, boolean unbounded) {
		super(name, (n, wrapper) -> new XActorSystemImpl(n, unbounded, wrapper));
		serverMode();
	}
	
	public List<UUID> addActor(int instances, Class<? extends Actor> clazz, Object... args) throws ActorInitializationException {
		return ((XActorSystemImpl)system).addActor(instances, clazz, args);
	}
	
	public UUID addActor(Class<? extends Actor> clazz, Object... args) throws ActorInitializationException {
		return ((XActorSystemImpl)system).addActor(clazz, args);
	}
}
