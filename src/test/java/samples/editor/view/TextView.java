package samples.editor.view;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import samples.editor.controller.TextFocusAdapter;
import samples.editor.model.SdaDocument;

/**
 * The TextView class is a scrollable text area that displays the content of an
 * SdaDocument. It allows users to view and edit the text representation of the
 * document, and provides methods to refresh the view and handle focus events.
 */
@SuppressWarnings("serial")
public final class TextView extends JScrollPane {

	private final JTextArea textArea;
	private final SdaDocument document;
	

	/**
	 * Constructs a new TextView for the given SdaDocument. The text area is
	 * initialized with the current content of the document.
	 *
	 * @param document the SdaDocument to display in this TextView
	 */
	public TextView(SdaDocument document) {
		
		this.document = document;
		this.textArea = UI.newTextArea(true, false);
		setViewportView(textArea);
		refresh();
	}


	/**
	 * Refreshes the text area with the current content of the document. This method
	 * should be called whenever the document is modified to ensure the text view
	 * stays up-to-date.
	 */
	public void refresh() {
		textArea.setText(document.toText());
	}
	
	
	/**
	 * Installs a focus listener on the text area that will update the given TreeView
	 * when the text area loses focus. This ensures that any changes made in the text
	 * area are reflected in the tree structure.
	 *
	 * @param treeView the TreeView to be refreshed when the text area loses focus
	 */
	public void installFocusListener(TreeView treeView) {
		var adapter = new TextFocusAdapter(treeView, this, document);
		textArea.addFocusListener(adapter);
	}
	
	
	/**
	 * Returns the current text content of the text area. This method can be used to
	 * retrieve the user's input or any changes made to the document's text
	 * representation.
	 *
	 * @return the current text content of the text area
	 */
	public String getText() {
		return textArea.getText();
	}


	/**
	 * Sets the focus to the text area and positions the caret at the specified error
	 * offset. This is useful for guiding the user to the location of a parsing error
	 * or any other issue in the text.
	 *
	 * @param errorOffset the offset in the text where the caret should be positioned
	 */
	public void setFocus(int errorOffset) {
		textArea.requestFocusInWindow();
		textArea.setCaretPosition(errorOffset);	
	}
}
