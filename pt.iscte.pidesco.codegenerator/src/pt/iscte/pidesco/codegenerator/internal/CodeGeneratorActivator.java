package pt.iscte.pidesco.codegenerator.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodeGeneratorActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "pt.iscte.pidesco.codegenerator"; //$NON-NLS-1$

	public static BundleContext context;

	private ServiceRegistration<CodeGeneratorServices> service;

	// The shared instance
	private static CodeGeneratorActivator plugin;

	/**
	 * The constructor
	 */
	public CodeGeneratorActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		CodeGeneratorActivator.context = context;
		service = context.registerService(CodeGeneratorServices.class, new CodeGeneratorServicesImpl(), null);
	}

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		service.unregister();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CodeGeneratorActivator getDefault() {
		return plugin;
	}

}
