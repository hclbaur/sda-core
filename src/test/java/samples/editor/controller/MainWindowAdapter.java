package samples.editor.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import samples.editor.model.SdaDocument;
import samples.editor.view.UI;

/**
 * The EditWindowAdapter class is a custom WindowAdapter that handles the
 * closing event of the EditView window. It checks if the current document has
 * unsaved changes and prompts the user to save them before closing the window.
 */
public class MainWindowAdapter extends WindowAdapter {

	private SdaDocument document; // the current document being edited
	private JFrame frame; // the frame that is closed

	
	/**
	 * Constructs a new EditWindowAdapter with the specified document and frame.
	 *
	 * @param document the document being edited
	 * @param frame    the frame that is closed
	 */
	public MainWindowAdapter(SdaDocument document, JFrame frame) {
		super(); 
		this.document = document; this.frame = frame; 
	}

	@Override
	public void windowClosing(WindowEvent e) {
		
		if (!document.isChanged()) {
			frame.dispose();
			return;
		}
		
		int option = JOptionPane.showConfirmDialog(frame, 
			"Do you want to save your changes before closing the editor?", 
			"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if (option == JOptionPane.YES_OPTION) {
			
			try {
				document.save();
			} catch (Exception x) {
				UI.showExceptionDialog(frame, "Failed to save " + document.getPath(), x);
				return; // Keep the window open if saving fails
			}
			frame.dispose();
			
		} else if (option == JOptionPane.NO_OPTION) {
			frame.dispose();
		} // If CANCEL_OPTION, do nothing and keep the window open
	}

}
