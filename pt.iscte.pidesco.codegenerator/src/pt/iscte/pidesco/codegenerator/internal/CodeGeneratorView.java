package pt.iscte.pidesco.codegenerator.internal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.eclipse.core.runtime.Platform;

import pt.iscte.pidesco.codegenerator.codes.SimpleCode;
import pt.iscte.pidesco.codegenerator.extensibility.CGCode;
import pt.iscte.pidesco.codegenerator.extensibility.CGCode.CodeType;
import pt.iscte.pidesco.codegenerator.features.GettersAndSetters;
import pt.iscte.pidesco.codegenerator.features.ToString;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class CodeGeneratorView implements PidescoView {
	private Composite ListArea;
	private ListViewer list;
	private boolean isOnSimplesCodeList;
	private Map<String, SimpleCode> SimpleCodeMap;
	private Map<String, CGCode> ComplexAndPluginCodeMap;

	static CodeGeneratorView instance;

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		instance = this;
		SimpleCodeMap = new HashMap<String, SimpleCode>();
		ComplexAndPluginCodeMap = new HashMap<String, CGCode>();
		isOnSimplesCodeList = true;

		viewArea.setLayout(new GridLayout(1, false));
		setViewButtons(viewArea);
		setViewList(viewArea);
		addDoubleClickListener();

		loadSimpleCodesFromFile();

		loadInternalCodes();

		loadPlugins();
	}

	static CodeGeneratorView getInstance() {
		return instance;
	}

	private void setViewButtons(Composite viewArea) {
		Composite ButtonArea = new Composite(viewArea, SWT.FILL);
		ButtonArea.setLayout(new GridLayout(3, false));
		GridData GridDataForButtonArea = new GridData();
		GridDataForButtonArea.horizontalAlignment = GridData.FILL;
		GridDataForButtonArea.grabExcessHorizontalSpace = true;
		ButtonArea.setLayoutData(GridDataForButtonArea);

		GridData GridDataForButtons = new GridData();
		GridDataForButtons.horizontalAlignment = GridData.FILL;
		GridDataForButtons.grabExcessHorizontalSpace = true;

		Button SimpleCodeButton = new Button(ButtonArea, SWT.PUSH);
		SimpleCodeButton.setText("Simple Code");
		SimpleCodeButton.setLayoutData(GridDataForButtons);
		SimpleCodeButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					if (!isOnSimplesCodeList) {
						isOnSimplesCodeList = true;
						for (String key : ComplexAndPluginCodeMap.keySet())
							list.remove(key);
						TreeSet<String> keys = new TreeSet<String>(SimpleCodeMap.keySet());
						for (String key : keys)
							list.add(key);
						;
						break;
					}
				}
				ListArea.layout();
			}
		});

		Button ComplexCodeAndPlugInsButton = new Button(ButtonArea, SWT.PUSH);
		ComplexCodeAndPlugInsButton.setText("Complex Code and Plug-ins");
		ComplexCodeAndPlugInsButton.setLayoutData(GridDataForButtons);
		ComplexCodeAndPlugInsButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					if (isOnSimplesCodeList) {
						isOnSimplesCodeList = false;
						for (String key : SimpleCodeMap.keySet())
							list.remove(key);
						TreeSet<String> keys = new TreeSet<String>(ComplexAndPluginCodeMap.keySet());
						for (String key : keys)
							list.add(key);
						;
						break;
					}
				}
				ListArea.layout();
			}
		});

		Button AddSimpleCodeButton = new Button(ButtonArea, SWT.PUSH);
		AddSimpleCodeButton.setText("Add and Remove Simple Code");
		AddSimpleCodeButton.setLayoutData(GridDataForButtons);
		AddSimpleCodeButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					WindowGenerator.getInstance().generateSimpleCodeAdderWindow();
				}
				ListArea.layout();
			}
		});
	}

	private void setViewList(Composite viewArea) {
		ListArea = new Composite(viewArea, SWT.BORDER);
		ListArea.setLayout(new FillLayout());
		GridData GridDataForListArea = new GridData();
		GridDataForListArea.horizontalAlignment = GridData.FILL;
		GridDataForListArea.grabExcessHorizontalSpace = true;
		GridDataForListArea.verticalAlignment = GridData.FILL;
		GridDataForListArea.grabExcessVerticalSpace = true;
		ListArea.setLayoutData(GridDataForListArea);
		list = new ListViewer(ListArea, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		TreeSet<String> keys = new TreeSet<String>(SimpleCodeMap.keySet());
		for (String key : keys)
			list.add(key);
	}

	private void addDoubleClickListener() {
		list.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection s = (IStructuredSelection) list.getSelection();

				BundleContext context = CodeGeneratorActivator.getContext();
				ServiceReference<JavaEditorServices> serviceReference = context
						.getServiceReference(JavaEditorServices.class);
				JavaEditorServices javaEditorServ = context.getService(serviceReference);

				if (SimpleCodeMap.containsKey(s.getFirstElement()))
					javaEditorServ.insertTextAtCursor(SimpleCodeMap.get(s.getFirstElement()).resultCodeToWrite());
				else if (ComplexAndPluginCodeMap.containsKey(s.getFirstElement()))
					javaEditorServ
							.insertTextAtCursor(ComplexAndPluginCodeMap.get(s.getFirstElement()).resultCodeToWrite());
				else {
					MessageDialog.openError(ListArea.getShell(), "Error",
							"This code is not avaiable, could it be removed");
				}
			}
		});
	}

	private void loadInternalCodes() {
		GettersAndSetters gas = new GettersAndSetters();
		ComplexAndPluginCodeMap.put(gas.getCodeName(), gas);
		ToString ts = new ToString();
		ComplexAndPluginCodeMap.put(ts.getCodeName(), ts);
	}
	
	
	private void loadPlugins() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] codes = reg.getConfigurationElementsFor("pt.iscte.pidesco.codegenerator.code");
		for (IConfigurationElement ext : codes) {
			try {
				CGCode code = (CGCode) ext.createExecutableExtension("class");
				ComplexAndPluginCodeMap.put(code.getCodeName(), code);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	boolean isCreated() {
		return list != null;
	}

	void addCode(CGCode code) {
		if (code.getCodeType().equals(CodeType.SIMPLE))
			if (!SimpleCodeMap.containsKey(code.getCodeName()))
				SimpleCodeMap.put(code.getCodeName(), (SimpleCode) code);
			else
				MessageDialog.openError(ListArea.getShell(), "Error", "There are another code with the same name");
		else if (!ComplexAndPluginCodeMap.containsKey(code.getCodeName()))
			ComplexAndPluginCodeMap.put(code.getCodeName(), (SimpleCode) code);
		else
			MessageDialog.openError(ListArea.getShell(), "Error", "There are another code with the same name");

		refreshList();
	}

	void RemoveSimpleCodeWithName(String name) {
		SimpleCodeMap.remove(name);
		list.remove(name);
		refreshList();
		saveSimpleCodesToFile();
	}

	private void refreshList() {
		if (!isOnSimplesCodeList) {
			isOnSimplesCodeList = true;
			for (String key : ComplexAndPluginCodeMap.keySet())
				list.remove(key);
		} else {
			isOnSimplesCodeList = true;
			for (String key : SimpleCodeMap.keySet())
				list.remove(key);
		}
		TreeSet<String> keys = new TreeSet<String>(SimpleCodeMap.keySet());
		for (String key : keys)
			list.add(key);
		ListArea.layout();
	}

	Display getDisplay() {
		return ListArea.getShell().getDisplay();
	}

	TreeSet<String> getSimpleCodeTreeSet() {
		return new TreeSet<String>(SimpleCodeMap.keySet());
	}

	public ArrayList<SimpleCode> getSimpleCodes() {
		return new ArrayList<SimpleCode>(SimpleCodeMap.values());
	}

	void saveSimpleCodesToFile() {
		IPath location = Platform.getLocation();
		String pathStr = location.toOSString();
		PrintWriter writer;
		try {
			//writer = new PrintWriter(pathStr + "/../pt.iscte.pidesco.codegenerator/Settings/Code.cg", "UTF-8");
			writer = new PrintWriter("Code.cg", "UTF-8");
			for (SimpleCode sc : SimpleCodeMap.values()) {
				writer.print(sc.getCodeName() + "-CGSeparator-" + sc.resultCodeToWrite());
				writer.print("-CGCodeSeparator-");
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	void loadSimpleCodesFromFile() {
		IPath location = Platform.getLocation();
		String pathStr = location.toOSString();
		Reader reader;
		try {
			//reader = new FileReader(pathStr + "/../pt.iscte.pidesco.codegenerator/Settings/Code.cg");
			reader = new FileReader("Code.cg");
			int data = reader.read();
			String output = "";
			while (data != -1) {
				char dataChar = (char) data;
				output += dataChar;
				data = reader.read();
			}
			String[] Codes = output.split("-CGCodeSeparator-");
			for (String code : Codes) {
				String[] Code = code.split("-CGSeparator-");
				if (Code.length > 1)
					SimpleCodeMap.put(Code[0], new SimpleCode(Code[0], Code[1]));
			}

			reader.close();
			refreshList();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
}