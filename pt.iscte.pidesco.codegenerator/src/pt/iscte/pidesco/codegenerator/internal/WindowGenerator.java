package pt.iscte.pidesco.codegenerator.internal;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.codegenerator.AST_Processing.CGAttribute;
import pt.iscte.pidesco.codegenerator.AST_Processing.CGClass;
import pt.iscte.pidesco.codegenerator.AST_Processing.CGMethod;
import pt.iscte.pidesco.codegenerator.codes.SimpleCode;
import pt.iscte.pidesco.codegenerator.features.GettersAndSetters;
import pt.iscte.pidesco.codegenerator.features.ToString;

public class WindowGenerator {

	private Shell shell;
	private static WindowGenerator windowGeneratorSWT;

	public static WindowGenerator getInstance() {
		if (windowGeneratorSWT == null) {
			windowGeneratorSWT = new WindowGenerator();
		}
		return windowGeneratorSWT;
	}

	void generateSimpleCodeAdderWindow() {
		Display display = CodeGeneratorView.getInstance().getDisplay();

		shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);

		shell.addListener(SWT.Close, event -> doShowMessageBox(event));

		shell.setText("Add and Remove Simple Code");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * 0.2);
		int height = (int) (screenSize.getHeight() * 0.5);

		shell.setLayout(new GridLayout(1, false));

		Composite NameArea = new Composite(shell, SWT.NONE);
		NameArea.setLayout(new GridLayout(2, false));
		GridData NameAreaGD = new GridData();
		NameAreaGD.horizontalAlignment = GridData.FILL;
		NameAreaGD.grabExcessHorizontalSpace = true;
		NameArea.setLayoutData(NameAreaGD);

		GridData NameLabelnTextGD = new GridData();
		NameLabelnTextGD.horizontalAlignment = GridData.FILL;
		NameLabelnTextGD.grabExcessHorizontalSpace = true;

		Label nameLabel = new Label(NameArea, SWT.NONE);
		nameLabel.setText("Code Name:");
		nameLabel.setLayoutData(NameLabelnTextGD);

		Text nameField = new Text(NameArea, SWT.NONE);
		nameField.setLayoutData(NameLabelnTextGD);

		Composite CodeArea = new Composite(shell, SWT.NONE);
		CodeArea.setLayout(new GridLayout(1, false));
		GridData CodeAreaGD = new GridData();
		CodeAreaGD.horizontalAlignment = GridData.FILL;
		CodeAreaGD.grabExcessHorizontalSpace = true;
		CodeAreaGD.verticalAlignment = GridData.FILL;
		CodeAreaGD.grabExcessVerticalSpace = true;
		CodeArea.setLayoutData(CodeAreaGD);

		GridData instructionsLabelGD = new GridData();
		instructionsLabelGD.horizontalAlignment = GridData.FILL;
		instructionsLabelGD.grabExcessHorizontalSpace = true;
		instructionsLabelGD.verticalAlignment = GridData.FILL;
		instructionsLabelGD.grabExcessVerticalSpace = true;
		Label instructionsLabel = new Label(CodeArea, SWT.NONE);
		instructionsLabel.setText("Rules to add simple code: " + System.lineSeparator()
				+ "Surround the variables with '@'" + System.lineSeparator() + "Don't forget the tabs to align the code"
				+ System.lineSeparator() + "Example:" + System.lineSeparator()
				+ "	For(int @i@ = 0; @i@<=@limite@; @i@++){" + System.lineSeparator() + System.lineSeparator() + "	}");
		instructionsLabel.setLayoutData(instructionsLabelGD);

		Text codeField = new Text(CodeArea, SWT.MULTI);
		codeField.setLayoutData(instructionsLabelGD);

		GridData generatenRemoveGD = new GridData();
		generatenRemoveGD.horizontalAlignment = GridData.FILL;
		generatenRemoveGD.grabExcessHorizontalSpace = true;
		Button generateSimpleCodeBtn = new Button(shell, SWT.NONE);
		generateSimpleCodeBtn.setText("Generate Simple Code");
		generateSimpleCodeBtn.setLayoutData(generatenRemoveGD);

		generateSimpleCodeBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					CodeGeneratorView.getInstance().addCode(new SimpleCode(nameField.getText(), codeField.getText()));
					CodeGeneratorView.getInstance().saveSimpleCodesToFile();
					shell.dispose();
				}
			}
		});

		Button RemoveSimpleCodeBtn = new Button(shell, SWT.NONE);
		RemoveSimpleCodeBtn.setText("Remove Simple Code");
		RemoveSimpleCodeBtn.setLayoutData(generatenRemoveGD);

		RemoveSimpleCodeBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					shell.dispose();
					shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
					shell.setLayout(new GridLayout(1, false));
					shell.addListener(SWT.Close, eventexit -> doShowMessageBox(event));
					shell.setText("Remove Simple Code");
					shell.setSize(width, height);
					shell.setLocation(screenSize.width / 2 - shell.getSize().x / 2,
							screenSize.height / 2 - shell.getSize().y / 2);
					Composite ListArea = new Composite(shell, SWT.BORDER);
					ListArea.setLayout(new FillLayout());
					GridData ListAreaGD = new GridData();
					ListAreaGD.horizontalAlignment = GridData.FILL;
					ListAreaGD.grabExcessHorizontalSpace = true;
					ListAreaGD.verticalAlignment = GridData.FILL;
					ListAreaGD.grabExcessVerticalSpace = true;
					ListArea.setLayoutData(ListAreaGD);
					ListViewer list = new ListViewer(ListArea, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					TreeSet<String> keys = CodeGeneratorView.getInstance().getSimpleCodeTreeSet();
					for (String key : keys)
						list.add(key);
					list.addDoubleClickListener(new IDoubleClickListener() {
						public void doubleClick(DoubleClickEvent event) {
							IStructuredSelection s = (IStructuredSelection) list.getSelection();
							CodeGeneratorView.getInstance().RemoveSimpleCodeWithName(s.getFirstElement().toString());
							shell.dispose();
						}
					});
					shell.open();
				}
			}
		});

		shell.setSize(width, height);
		shell.setLocation(screenSize.width / 2 - shell.getSize().x / 2, screenSize.height / 2 - shell.getSize().y / 2);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public String generateGettersAndSettersWindow(ArrayList<CGAttribute> attributes) {
		Display display = CodeGeneratorView.getInstance().getDisplay();

		shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);

		shell.addListener(SWT.Close, event -> doShowMessageBox(event));

		shell.setText("Generate Getters and Setters");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * 0.2);
		int height = (int) (screenSize.getHeight() * 0.5);

		shell.setLayout(new GridLayout(1, false));

		GridData gettersnSettersAreaGD = new GridData();
		gettersnSettersAreaGD.horizontalAlignment = GridData.FILL;
		gettersnSettersAreaGD.grabExcessHorizontalSpace = true;
		gettersnSettersAreaGD.verticalAlignment = GridData.FILL;
		gettersnSettersAreaGD.grabExcessVerticalSpace = true;

		Composite GettersArea = new Composite(shell, SWT.NONE);
		GettersArea.setLayout(new GridLayout(1, false));
		GettersArea.setLayoutData(gettersnSettersAreaGD);

		Composite SettersArea = new Composite(shell, SWT.NONE);
		SettersArea.setLayout(new GridLayout(1, false));
		SettersArea.setLayoutData(gettersnSettersAreaGD);

		GridData getternSetterLabelGD = new GridData();
		getternSetterLabelGD.horizontalAlignment = GridData.FILL;
		getternSetterLabelGD.grabExcessHorizontalSpace = true;

		Label getterLabel = new Label(GettersArea, SWT.NONE);
		getterLabel.setText("Select the getters:");
		getterLabel.setLayoutData(getternSetterLabelGD);

		Label setterLabel = new Label(SettersArea, SWT.NONE);
		setterLabel.setText("Select the setters:");
		setterLabel.setLayoutData(getternSetterLabelGD);

		ArrayList<String> Getters = new ArrayList<String>();
		ArrayList<String> Setters = new ArrayList<String>();

		for (CGAttribute attribute : attributes) {
			Button getter = new Button(GettersArea, SWT.CHECK);
			getter.setText(attribute.getAtributeName());
			getter.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					switch (event.type) {
					case SWT.Selection:
						if (Getters.contains(getter.getText()))
							Getters.remove(getter.getText());
						else
							Getters.add(getter.getText());
					}
				}
			});
			Button setter = new Button(SettersArea, SWT.CHECK);
			setter.setText(attribute.getAtributeName());
			setter.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					switch (event.type) {
					case SWT.Selection:
						if (Setters.contains(setter.getText()))
							Setters.remove(setter.getText());
						else
							Setters.add(setter.getText());
					}
				}
			});
		}

		GridData generateSimpleCodeGD = new GridData();
		generateSimpleCodeGD.horizontalAlignment = GridData.FILL;
		generateSimpleCodeGD.grabExcessHorizontalSpace = true;
		Button generateSimpleCodeBtn = new Button(shell, SWT.NONE);
		generateSimpleCodeBtn.setText("Generate Getters and Setters");
		generateSimpleCodeBtn.setLayoutData(generateSimpleCodeGD);

		generateSimpleCodeBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					GettersAndSetters.generateGettersAndSettersFromArrayList(attributes, Getters, Setters);
					shell.dispose();
				}
			}
		});

		shell.setSize(width, height);
		shell.setLocation(screenSize.width / 2 - shell.getSize().x / 2, screenSize.height / 2 - shell.getSize().y / 2);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return "";
	}

	public void generateToStringWindow(CGClass Class) {
		Display display = CodeGeneratorView.getInstance().getDisplay();

		shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);

		shell.addListener(SWT.Close, event -> doShowMessageBox(event));

		shell.setText("Generate toString");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * 0.2);
		int height = (int) (screenSize.getHeight() * 0.5);

		shell.setLayout(new GridLayout(1, false));

		GridData attributesnMethodsAreaGD = new GridData();
		attributesnMethodsAreaGD.horizontalAlignment = GridData.FILL;
		attributesnMethodsAreaGD.grabExcessHorizontalSpace = true;
		attributesnMethodsAreaGD.verticalAlignment = GridData.FILL;
		attributesnMethodsAreaGD.grabExcessVerticalSpace = true;

		Composite AttributesArea = new Composite(shell, SWT.NONE);
		AttributesArea.setLayout(new GridLayout(1, false));
		AttributesArea.setLayoutData(attributesnMethodsAreaGD);

		Composite MethodsArea = new Composite(shell, SWT.NONE);
		MethodsArea.setLayout(new GridLayout(1, false));
		MethodsArea.setLayoutData(attributesnMethodsAreaGD);

		GridData attributesnMethodsLabelGD = new GridData();
		attributesnMethodsLabelGD.horizontalAlignment = GridData.FILL;
		attributesnMethodsLabelGD.grabExcessHorizontalSpace = true;

		Label attributeLabel = new Label(AttributesArea, SWT.NONE);
		attributeLabel.setText("Select the Attributes:");
		attributeLabel.setLayoutData(attributesnMethodsLabelGD);

		Label methodLabel = new Label(MethodsArea, SWT.NONE);
		methodLabel.setText("Select the Methods:");
		methodLabel.setLayoutData(attributesnMethodsLabelGD);

		ArrayList<String> Attributes = new ArrayList<String>();
		ArrayList<String> Methods = new ArrayList<String>();

		for (CGAttribute attribute : Class.getAtributes()) {
			Button attibutebtn = new Button(AttributesArea, SWT.CHECK);
			attibutebtn.setText(attribute.getAtributeName());
			attibutebtn.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					switch (event.type) {
					case SWT.Selection:
						if (Attributes.contains(attibutebtn.getText()))
							Attributes.remove(attibutebtn.getText());
						else
							Attributes.add(attibutebtn.getText());
					}
				}
			});
		}

		for (CGMethod method : Class.getMethods()) {
			if (method.getNumberOfParameters() == 0) {
				Button methodBtn = new Button(MethodsArea, SWT.CHECK);
				methodBtn.setText(method.getMethodName());
				methodBtn.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event event) {
						switch (event.type) {
						case SWT.Selection:
							if (Methods.contains(methodBtn.getText()))
								Methods.remove(methodBtn.getText());
							else
								Methods.add(methodBtn.getText());

						}
					}
				});
			}
		}

		GridData generateToStringGD = new GridData();
		generateToStringGD.horizontalAlignment = GridData.FILL;
		generateToStringGD.grabExcessHorizontalSpace = true;
		Button generateToStringBtn = new Button(shell, SWT.NONE);
		generateToStringBtn.setText("Generate toString");
		generateToStringBtn.setLayoutData(generateToStringGD);

		generateToStringBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					ToString.generateToStringFromArrayList(Class, Attributes, Methods);
					shell.dispose();
				}
			}
		});

		shell.setSize(width, height);
		shell.setLocation(screenSize.width / 2 - shell.getSize().x / 2, screenSize.height / 2 - shell.getSize().y / 2);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	private void doShowMessageBox(Event event) {

		int style = SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.YES | SWT.NO;

		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setText("Information");
		messageBox.setMessage("Really close application?");
		event.doit = messageBox.open() == SWT.YES;
	}

}