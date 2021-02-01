package io.actor4j.corex.pods;

import java.io.File;

import io.actor4j.core.ActorSystemImpl;
import io.actor4j.core.pods.PodConfiguration;
import io.actor4j.core.pods.PodReplicationController;
import io.actor4j.core.pods.PodSystemConfiguration;

public class XPodReplicationController extends PodReplicationController {
	public XPodReplicationController(ActorSystemImpl system) {
		super(system);
	}

	@Override
	protected void deployPods(File jarFile, PodConfiguration podConfiguration, PodSystemConfiguration podSystemConfiguration, ActorSystemImpl system) {
		XPodDeployment.deployPods(jarFile, podConfiguration, podSystemConfiguration, system);
	}
}
