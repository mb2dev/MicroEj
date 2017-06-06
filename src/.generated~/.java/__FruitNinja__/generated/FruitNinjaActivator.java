package __FruitNinja__.generated;

import ej.wadapps.management.ActivitiesList;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleActivator;

public class FruitNinjaActivator implements BundleActivator {

	moc.esgi.MainActivity moc__esgi__MainActivity;

	@Override
	public void initialize() {
		this.moc__esgi__MainActivity = new moc.esgi.MainActivity();
	}

	@Override
	public void link() {
		ActivitiesList activitieslist = ServiceLoaderFactory.getServiceLoader().getService(ActivitiesList.class);
		activitieslist.add(this.moc__esgi__MainActivity);

	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		ActivitiesList activitieslist = ServiceLoaderFactory.getServiceLoader().getService(ActivitiesList.class);
		activitieslist.remove(this.moc__esgi__MainActivity);

	}

}