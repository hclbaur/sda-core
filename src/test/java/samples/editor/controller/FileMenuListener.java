package samples.editor.controller;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import samples.editor.view.FileMenu;

/**
 * The FileMenuListener class listens for events on the File menu in the
 * EditView. It enables or disables the Save (As) menu items based on the
 * current document state.
 */
public class FileMenuListener  implements MenuListener {

	
	private FileMenu fileMenu;

	/**
	 * Constructs a new FileMenuListener.
	 *
	 * @param fileMenu the menu this listener is associated with
	 */
	public FileMenuListener(FileMenu fileMenu) {
		this.fileMenu = fileMenu;
	}

	@Override
	public void menuSelected(MenuEvent e) {
		fileMenu.selectMenu();
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
