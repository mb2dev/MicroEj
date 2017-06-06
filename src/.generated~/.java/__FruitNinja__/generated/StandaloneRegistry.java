package __FruitNinja__.generated;

import java.util.ArrayList;
import java.util.Collection;

import ej.components.registry.BundleActivator;
import ej.components.registry.impl.AbstractRegistry;

public class StandaloneRegistry extends AbstractRegistry {

	private static final String[] BUNDLES = {
			"__FruitNinja__.generated.FruitNinjaActivator",
"ej.wadapps.management.activators.DefaultManagementActivator"
	};

	@Override
	protected BundleActivator[] loadBundles() {
		Collection<BundleActivator> bundles = new ArrayList<>();
		for (String bundleName : BUNDLES) {
			try {
				BundleActivator bundle = (BundleActivator) Class.forName(bundleName).newInstance();
				bundles.add(bundle);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
				throw new RuntimeException("Cannot instantiate " + bundleName, e);
			}
		}
		return bundles.toArray(new BundleActivator[bundles.size()]);
	}

}