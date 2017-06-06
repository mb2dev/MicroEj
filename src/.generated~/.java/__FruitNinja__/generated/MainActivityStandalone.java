package __FruitNinja__.generated;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleRegistry;
import ej.wadapps.management.ActivitiesScheduler;
import ej.wadapps.registry.SharedRegistryFactory;

public class MainActivityStandalone {

	public static void main(String[] args) {
		SharedRegistryFactory.getSharedRegistry().register(BundleRegistry.class, new StandaloneRegistry());

		// Start entry point.
		new FruitNinjaEntryPoint().start();

		// MainActivity
		ActivitiesScheduler activitiesScheduler = ServiceLoaderFactory.getServiceLoader()
				.getService(ActivitiesScheduler.class);
		moc.esgi.MainActivity activity = new moc.esgi.MainActivity();
		activitiesScheduler.show(activity);
	}

}