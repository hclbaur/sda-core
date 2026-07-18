package samples.editor.controller;

import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import samples.editor.model.SdaDocument;

/**
 * The FileMenuListener class listens for events on the File menu in the
 * EditView. It enables or disables the Save (As) menu items based on the
 * current document state.
 */
public class FileMenuListener  implements MenuListener {

	private SdaDocument document; // the current document being edited
	private JMenuItem saveFile; // the Save menu item to enable/disable
	private JMenuItem saveAsFile; // the Save As menu item to enable/disable

	
	/**
	 * Constructs a new FileMenuListener with the given SdaDocument and JMenuItem.
	 *
	 * @param document the current SdaDocument being edited
	 * @param saveFile the "Save" JMenuItem to enable or disable
	 */
	public FileMenuListener(SdaDocument document, JMenuItem saveFile, JMenuItem saveAsFile) {
		this.document = document;
		this.saveFile = saveFile;
		this.saveAsFile = saveAsFile;
	}

	@Override
	public void menuSelected(MenuEvent e) {
		
		if (document == null) {
			saveFile.setEnabled(false);
			saveAsFile.setEnabled(false);
		} else {
			saveAsFile.setEnabled(true);
			saveFile.setEnabled(document.isChanged());
		}
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// do nothing
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		// do nothing
	}

}
