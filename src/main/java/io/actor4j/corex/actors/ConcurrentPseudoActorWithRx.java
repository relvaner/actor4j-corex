/*
 * Copyright (c) 2015-2020, David A. Bauer. All rights reserved.
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

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import io.actor4j.core.ActorServiceNode;
import io.actor4j.core.ActorSystem;
import io.actor4j.core.internal.PseudoActorCell;
import io.actor4j.core.messages.ActorMessage;
import io.actor4j.corex.utils.ActorMessageFlowable;
import io.reactivex.Flowable;

public abstract class ConcurrentPseudoActorWithRx {
	protected PseudoActorWithRx actor;
	
	public ConcurrentPseudoActorWithRx(String name, ActorSystem system) {
		this(name, system, false);
	}
	
	public ConcurrentPseudoActorWithRx(ActorSystem system, boolean blocking) {
		this(null, system, blocking);
	}
	
	public ConcurrentPseudoActorWithRx(String name, ActorSystem system, boolean blocking) {
		actor = new PseudoActorWithRx(name, system, blocking) {
			@Override
			public void receive(ActorMessage<?> message) {
				ConcurrentPseudoActorWithRx.this.receive(message);
			}
		};
	}
	
	public String getName() {
		return actor.getName();
	}
	
	public UUID getId() {
		return actor.getId();
	}
	
	public UUID self() {
		return actor.getId();
	}
	
	protected void failsafeMethod(ActorMessage<?> message) {
		try {
			receive(message);
		}
		catch(Exception e) {
			actor.getCell().getSystem().getExecuterService().getFailsafeManager().notifyErrorHandler(e, "pseudo", actor.getId());
			actor.getCell().getSystem().getActorStrategyOnFailure().handle(actor.getCell(), e);
		}	
	}
	
	protected boolean poll(Queue<ActorMessage<?>> queue) {
		boolean result = false;
		
		ActorMessage<?> message = queue.poll();
		if (message!=null) {
			failsafeMethod(message);
			result = true;
		} 
		
		return result;
	}
	
	public abstract void receive(ActorMessage<?> message);
	
	public boolean run() {
		boolean result = false;
		
		for (int j=0; poll(getOuterQueue()) && j<actor.getCell().getSystem().getBufferQueueSize(); j++)
			result = true;
		
		return result;
	}
	
	public boolean runAll() {
		boolean result = false;
		
		while (poll(getOuterQueue()))
			result = true;
		
		return result;
	}
	
	public boolean runOnce() {
		return poll(getOuterQueue());
	}
	
	public Stream<ActorMessage<?>> stream() {
		return getOuterQueue().stream();
	}
	
	public Flowable<ActorMessage<?>> runWithRx() {
		return ActorMessageFlowable.getMessages(getOuterQueue());
	}
	
	public ActorMessage<?> await() {
		return actor.await();
	}
	
	public ActorMessage<?> await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		return actor.await(timeout, unit);
	}
	
	public <T> T await(Predicate<ActorMessage<?>> predicate, Function<ActorMessage<?>, T> action, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		return actor.await(predicate, action, timeout, unit);
	}
	
	public void send(ActorMessage<?> message) {
		actor.send(message);
	}
	
	public void sendViaPath(ActorMessage<?> message, String path) {
		actor.sendViaPath(message, path);
	}
	
	public void sendViaPath(ActorMessage<?> message, String nodeName, String path) {
		actor.sendViaPath(message, nodeName, path);
	}
	
	public void sendViaPath(ActorMessage<?> message, ActorServiceNode node, String path) {
		actor.sendViaPath(message, node, path);
	}
	
	public void sendViaAlias(ActorMessage<?> message, String alias) {
		actor.sendViaAlias(message, alias);
	}
	
	public void send(ActorMessage<?> message, UUID dest) {
		actor.send(message, dest);
	}
	
	public <T> void tell(T value, int tag, UUID dest) {
		actor.tell(value, tag, dest);
	}
	
	public <T> void tell(T value, int tag, String alias) {
		actor.tell(value, tag, alias);
	}
	
	public void forward(ActorMessage<?> message, UUID dest) {
		actor.forward(message, dest);
	}
	
	public void setAlias(String alias) {
		actor.setAlias(alias);
	}
	
	public Queue<ActorMessage<?>> getOuterQueue() {
		return ((PseudoActorCell)actor.getCell()).getOuterQueue();
	}
	
	public void reset() {
		((PseudoActorCell)actor.getCell()).getOuterQueue().clear();
	}
}
