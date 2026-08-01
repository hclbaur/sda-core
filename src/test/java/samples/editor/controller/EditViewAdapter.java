package samples.editor.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import samples.editor.view.EditView;

/**
 * The EditViewAdapter class is a custom WindowAdapter that handles the
 * closing event of the EditView window. It checks if the current document has
 * unsaved changes and prompts the user to save them before closing the window.
 */
public class EditViewAdapter extends WindowAdapter {

	private EditView editView; // the main view of the application

	
	/**
	 * Constructs a new EditViewAdapter.
	 *
	 * @param editView the main view of the application
	 */
	public EditViewAdapter(EditView editView) {
		super(); 
		this.editView = editView;
	}


	@Override
	public void windowClosing(WindowEvent e) {

		var fileMenu = editView.getFileMenu();
		var document = fileMenu.getDocument();

		if (document == null || !document.hasChanges()) {
			editView.dispose(); // No unsaved changes, close the window
			return;
		}
		
		int option = JOptionPane.showConfirmDialog(editView, 
			"Do you want to save your changes before closing the editor?", 
			"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if (option == JOptionPane.NO_OPTION) {
			editView.dispose(); // User chose not to save, close the window
			return;
		}

		if (option == JOptionPane.YES_OPTION && fileMenu.saveFile()) {
			editView.dispose(); // User chose to save and was successful, close the window
			return;
		}

		// If the user canceled or saving failed, do not close the window
	}

}
