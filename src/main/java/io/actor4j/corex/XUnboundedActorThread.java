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
package io.actor4j.corex;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.concurrent.PriorityBlockingQueue;

import org.jctools.queues.MpscLinkedQueue;

import io.actor4j.core.ActorSystemImpl;
import io.actor4j.core.DefaultActorThread;

public class XUnboundedActorThread extends DefaultActorThread {
	public XUnboundedActorThread(ThreadGroup group, String name, ActorSystemImpl system) {
		super(group, name, system);
	}

	@Override
	public void configQueues() {
		directiveQueue = new MpscLinkedQueue<>(); /* unbounded */
		priorityQueue  = new PriorityBlockingQueue<>(system.getQueueSize()); /* unbounded */
		
		serverQueueL2  = new MpscLinkedQueue<>(); /* unbounded */
		serverQueueL1  = new ArrayDeque<>(system.getBufferQueueSize()); /* unbounded */
		
		outerQueueL2   = new MpscLinkedQueue<>(); /* unbounded */
		outerQueueL1   = new ArrayDeque<>(system.getBufferQueueSize()); /* unbounded */
		
		innerQueue     = new LinkedList<>(); /* unbounded */
	}
}
