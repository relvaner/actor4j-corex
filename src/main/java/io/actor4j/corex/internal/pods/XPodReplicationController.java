package io.actor4j.corex.internal.pods;

import java.io.File;

import io.actor4j.core.internal.ActorSystemImpl;
import io.actor4j.core.internal.pods.PodReplicationController;
import io.actor4j.core.internal.pods.PodSystemConfiguration;
import io.actor4j.core.pods.PodConfiguration;

public class XPodReplicationController extends PodReplicationController {
	public XPodReplicationController(ActorSystemImpl system) {
		super(system);
	}

	@Override
	protected void deployPods(File jarFile, PodConfiguration podConfiguration, PodSystemConfiguration podSystemConfiguration, ActorSystemImpl system) {
		XPodDeployment.deployPods(jarFile, podConfiguration, podSystemConfiguration, system);
	}
}
