package pt.iscte.pidesco.codegenerator.graphicalsimplecode;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class GraphicalSimpleCodeActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		GraphicalSimpleCodeActivator.context = bundleContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		GraphicalSimpleCodeActivator.context = null;
	}

}
