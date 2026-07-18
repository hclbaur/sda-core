package samples.editor.controller;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JOptionPane;

import be.baur.sda.io.ParseException;
import samples.editor.model.SdaDocument;
import samples.editor.view.TextView;
import samples.editor.view.TreeView;
import samples.editor.view.UI;

/**
 * The TextFocusAdapter class listens for focus events on a text area and
 * updates the associated tree view when the text area loses focus. It ensures
 * that any changes made in the text area are reflected in the tree structure,
 * and handles parsing errors gracefully.
 */
public final class TextFocusAdapter extends FocusAdapter {

    private final TextView textView; // the text view that this adapter is associated with
    private final TreeView treeView; // the tree view to refresh when the text area is updated
	private final SdaDocument document; // the document shown or edited in the text view

    
    /**
	 * Constructs a new TextFocusAdapter for the given TreeView and TextView. This adapter
	 * listens for focus events on the text area and updates the tree view accordingly.
	 *
	 * @param treeView the TreeView to refresh when the text area loses focus
	 * @param textView the TextView that this adapter is associated with
	 */
	public TextFocusAdapter(TreeView treeView, TextView textView, SdaDocument document) {
        this.treeView = treeView;
        this.textView = textView;
        this.document = document;
	}


	/**
	 * Called when the text area loses focus. This method refreshes both the text view
	 * and the tree view to ensure that any changes made in the text area are reflected
	 * in the tree structure.
	 *
	 * @param e the FocusEvent that triggered this method
	 */
	@Override
	public void focusLost(FocusEvent e) {
		
		try {
			
			if (document.update(textView.getText())) {
				treeView.refresh(); // Refresh the tree view only if the document changed
			}
			
		} catch (ParseException ex) {

			String message = "Invalid SDA format: " + ex.getLocalizedMessage() + "\n\n" +
				"Press OK to continue editing and fix the text, or Cancel to revert to the last valid state.";
			
			int result = JOptionPane.showConfirmDialog(
					textView, message, "Parsing Error",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE
			);

			if (result == JOptionPane.OK_OPTION) {
				// User pressed OK to continue editing and fix the text
				textView.setFocus(ex.getErrorOffset());
				return;
			}
		}
		catch (Exception ex) {
			// Catch any other unexpected exceptions, implies a revert
			UI.showExceptionDialog(textView, "Unexpected error", ex);
		}

		/*
		 * If we get here, it means either the parsing was successful or the user chose
		 * to revert to the last valid state. In both cases, we refresh the text view to
		 * ensure it reflects the current state of the document.
		 */
		textView.refresh();
	}
}
