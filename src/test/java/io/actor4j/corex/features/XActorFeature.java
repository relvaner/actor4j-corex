/*
 * Copyright (c) 2015-2018, David A. Bauer. All rights reserved.
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
package io.actor4j.corex.features;

import static io.actor4j.corex.logging.ActorLogger.logger;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import io.actor4j.core.actors.Actor;
import io.actor4j.core.features.ActorFeature;
import io.actor4j.core.features.actor.ReflectionActor;
import io.actor4j.corex.XActorSystem;

public class XActorFeature extends ActorFeature {
	@Before
	public void before() {
		system = new XActorSystem();
	}
	
	@Test(timeout=5000)
	public void test_instantiation_reflection() {
		logger().debug("workaround - FATAL StatusLogger Interrupted before Log4j Providers could be loaded.");
		
		UUID id1 = ((XActorSystem)system).addActor(ReflectionActor.class, 42, "43", true, Arrays.asList("44", "45", 46), 99);
		UUID id2 = ((XActorSystem)system).addActor(ReflectionActor.class, 45, "49");
		Map<Integer, String> map = new HashMap<>();
		map.put(99, "98");
		UUID id3 = ((XActorSystem)system).addActor(ReflectionActor.class, map, 42.345);
		
		system.start();
		
		try {
			ReflectionActor.testDone.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Actor actor1 = system.underlyingImpl().getCells().get(id1).getActor();
		Actor actor2 = system.underlyingImpl().getCells().get(id2).getActor();
		Actor actor3 = system.underlyingImpl().getCells().get(id3).getActor();
		
		assertEquals(42, ((ReflectionActor)actor1).value1);
		assertEquals("43", ((ReflectionActor)actor1).value2);
		assertEquals(true, ((ReflectionActor)actor1).value3);
		assertEquals(Arrays.asList("44", "45", 46), ((ReflectionActor)actor1).value4);
		assertEquals(Integer.valueOf(99), ((ReflectionActor)actor1).value5);
		
		assertEquals(45, ((ReflectionActor)actor2).value1);
		assertEquals("49", ((ReflectionActor)actor2).value2);
		
		assertEquals("98", ((ReflectionActor)actor3).value6.get(99));
		assertEquals(42.345, ((ReflectionActor)actor3).value7, 0.001);
		
		system.shutdownWithActors(true);
	}
}
