package samples.editor.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import be.baur.sda.SDA;

/**
 * A dialog for inputting a node name and value when adding a new node to the
 * tree. The dialog contains two text fields for the name and value, and
 * validates the name using SDA's isNodeName method. If the user clicks OK and
 * the name is valid, a NameValuePair object containing the name and value is
 * returned. If the user cancels or if the name is invalid, null is returned.
 */
@SuppressWarnings("serial")
public final class NodeInputDialog extends JPanel {
	
	private static final Color LIGHT_RED = new Color(255, 192, 192);
	
	private JTextField nameField;
	private JTextField valueField;

	private JButton okButton;


	/** A simple class to hold a name and value pair. */
	public class NameValuePair {

	    public final String name;
	    public final String value;

	    public NameValuePair(String name, String value) {
	        this.name = name; 
	        this.value = value;
	    }
	}


	/** Helper method to create GridBagConstraints with common settings. */
	private GridBagConstraints constraints(int gridx, int gridy) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx; c.gridy = gridy;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new java.awt.Insets(5, 5, 5, 5);
		return c;
	}


	/**
	 * Constructs a NodeInputDialog with the given initial name and value. The name
	 * is validated on typing, and the OK button is enabled only if a name is valid.
	 * 
	 * @param name  the initial name to display in the name field
	 * @param value the initial value to display in the value field
	 */
	public NodeInputDialog(String name, String value) {

	    nameField = new JTextField(name, 16);
	    valueField = new JTextField(value, 16);

	    setLayout(new GridBagLayout());
	    add(new JLabel("Name:"), constraints(0, 0));
	    add(nameField, constraints(1, 0));
	    add(new JLabel("Value:"), constraints(0, 1));
	    add(valueField, constraints(1, 1));
	    
	    nameField.getDocument().addDocumentListener(
	    	new DocumentListener() {

				@Override public void insertUpdate(DocumentEvent e) { validateName(); }
				@Override public void removeUpdate(DocumentEvent e) { validateName(); }
				@Override public void changedUpdate(DocumentEvent e) { validateName(); }

				private void validateName() {
				    String name = nameField.getText();
				    boolean valid = SDA.isNodeName(name);
				    nameField.setBackground(valid ? Color.WHITE : LIGHT_RED);
				    if (okButton != null) {
				        okButton.setEnabled(valid);
				    }
				}
			}
	    );
	}

	
	/**
	 * Shows a dialog to input a node name and value, and returns them as a
	 * NameValuePair if the user clicks OK. Returns null if the user cancels or if
	 * the name is invalid.
	 * 
	 * @param parent the parent component for the dialog
	 */
	public NameValuePair showNodeInputDialog(Component parent) {
		
		this.okButton = new JButton("OK");
		var cancelButton = new JButton("Cancel");

		JOptionPane pane = new JOptionPane(
		    this, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION,
		    null, new Object[] { okButton, cancelButton }, okButton
		);
		JDialog dialog = pane.createDialog(parent, "Add Node");
		dialog.setLocation(MouseInfo.getPointerInfo().getLocation());
		
	    okButton.addActionListener(e -> {
	        pane.setValue(JOptionPane.OK_OPTION);
	        dialog.dispose();
	    });

	    cancelButton.addActionListener(e -> {
	        pane.setValue(JOptionPane.CANCEL_OPTION);
	        dialog.dispose();
	    });
	    
		dialog.setVisible(true);

		// if user clicked cancel or closed the dialog, return null
		Object result = pane.getValue();
		if (result == null || Objects.equals(result, JOptionPane.CANCEL_OPTION))
			return null;

		// return pre-validated name and value from the text fields
		return new NameValuePair(nameField.getText(), valueField.getText());
	}
}
