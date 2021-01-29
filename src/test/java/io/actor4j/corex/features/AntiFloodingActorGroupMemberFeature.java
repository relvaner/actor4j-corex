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

import org.junit.Before;

import io.actor4j.core.ActorSystem;
import io.actor4j.core.features.ActorGroupMemberFeature;
import io.actor4j.corex.AntiFloodingActorSystemImpl;

public class AntiFloodingActorGroupMemberFeature extends ActorGroupMemberFeature {
	@Before
	public void before() {
		system = new ActorSystem("actor4j-antiflooding", (name, wrapper) -> new AntiFloodingActorSystemImpl(name, wrapper));
	}
}
