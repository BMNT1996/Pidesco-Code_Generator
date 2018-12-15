package pt.iscte.pidesco.codegenerator.graphicalsimplecode;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.codes.SimpleCode;
import pt.iscte.pidesco.codegenerator.extensibility.CGCode;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;

public class GraphicalSimpleCodeView implements CGCode {

	private Shell shell;

	private String output = "";

	private SimpleCode selected = null;

	@Override
	public String getCodeName() {
		return "Graphical Simple Code";
	}

	@Override
	public String resultCodeToWrite() {
		BundleContext context = GraphicalSimpleCodeActivator.getContext();
		ServiceReference<CodeGeneratorServices> serviceReference = context
				.getServiceReference(CodeGeneratorServices.class);
		CodeGeneratorServices codeGeneratorServ = context.getService(serviceReference);

		Display display = codeGeneratorServ.getDisplay();

		shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);

		shell.addListener(SWT.Close, event -> doShowMessageBox(event));

		shell.setText("Graphical Simple Code");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * 0.2);
		int height = (int) (screenSize.getHeight() * 0.5);

		shell.setLayout(new GridLayout(1, false));

		ArrayList<SimpleCode> Codes = codeGeneratorServ.getSimpleCodes();

		Composite selecterArea = new Composite(shell, SWT.NONE);
		selecterArea.setLayout(new GridLayout(1, false));
		GridData selecterAreaGD = new GridData();
		selecterAreaGD.horizontalAlignment = GridData.FILL;
		selecterAreaGD.grabExcessHorizontalSpace = true;
		selecterArea.setLayoutData(selecterAreaGD);

		Combo combo = new Combo(selecterArea, SWT.READ_ONLY);
		GridData comboGD = new GridData();
		comboGD.horizontalAlignment = GridData.FILL;
		comboGD.grabExcessHorizontalSpace = true;
		combo.setLayoutData(comboGD);

		for (SimpleCode code : Codes) {
			combo.add(code.getCodeName());
		}

		Composite variablesArea = new Composite(shell, SWT.NONE);
		variablesArea.setLayout(new GridLayout(2, false));
		GridData variablesAreaGD = new GridData();
		variablesAreaGD.horizontalAlignment = GridData.FILL;
		variablesAreaGD.grabExcessHorizontalSpace = true;
		variablesAreaGD.verticalAlignment = GridData.FILL;
		variablesAreaGD.grabExcessVerticalSpace = true;
		variablesArea.setLayoutData(variablesAreaGD);

		Button confirmCodeBtn = new Button(selecterArea, SWT.PUSH);
		confirmCodeBtn.setText("Confirm Code");
		confirmCodeBtn.setLayoutData(comboGD);
		confirmCodeBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					if (combo.getSelectionIndex() == -1)
						MessageDialog.openInformation(shell, "Empty Combo", "Please select a code in the combo box");
					else {
						for (SimpleCode sp : Codes) {
							if (sp.getCodeName().equals(combo.getItem(combo.getSelectionIndex()))) {
								selected = sp;
								ArrayList<String> listOfVariables = new ArrayList<String>();
								String spOutput = sp.resultCodeToWrite();
								String[] outputSplit = spOutput.split("@");
								for (int i = 0; i < outputSplit.length; i++) {
									if (i % 2 == 1)
										if (!listOfVariables.contains(outputSplit[i]))
											listOfVariables.add(outputSplit[i]);
								}
								for (Control control : variablesArea.getChildren()) {
									control.dispose();
								}
								GridData variablesGD = new GridData();
								variablesGD.horizontalAlignment = GridData.FILL;
								variablesGD.grabExcessHorizontalSpace = true;

								for (String variableName : listOfVariables) {
									System.out.println(variableName);
									Label varNameLB = new Label(variablesArea, SWT.NONE);
									varNameLB.setText(variableName);
									varNameLB.setLayoutData(variablesGD);
									Text VarNameTX = new Text(variablesArea, SWT.NONE);
									VarNameTX.setLayoutData(variablesGD);
								}
								break;
							}
						}
						variablesArea.layout();
					}
				}
			}
		});

		Button generateCodeBtn = new Button(shell, SWT.PUSH);
		generateCodeBtn.setText("Generate Code");
		generateCodeBtn.setLayoutData(comboGD);
		generateCodeBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					if (combo.getSelectionIndex() == -1)
						MessageDialog.openInformation(shell, "Empty Combo", "Please select a code in the combo box");
					else {
						Control[] children = variablesArea.getChildren();
						output = selected.resultCodeToWrite();
						for (int i = 0; i < children.length / 2; i++) {
							System.out.println(
									((Label) children[i * 2]).getText() + " " + ((Text) children[i * 2 + 1]).getText());
							output = output.replaceAll("@" + ((Label) children[i * 2]).getText() + "@",
									((Text) children[i * 2 + 1]).getText());
						}
						System.out.println(output);
						shell.dispose();
					}
				}
			}
		});
		shell.setSize(width, height);
		shell.setLocation(screenSize.width / 2 - shell.getSize().x / 2, screenSize.height / 2 - shell.getSize().y / 2);
		shell.layout();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return output;
	}

	private void doShowMessageBox(Event event) {

		int style = SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.YES | SWT.NO;

		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setText("Information");
		messageBox.setMessage("Really close application?");
		event.doit = messageBox.open() == SWT.YES;
	}

}
