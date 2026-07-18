package samples.editor.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import samples.editor.controller.MainWindowAdapter;
import samples.editor.controller.FileMenuListener;
import samples.editor.model.SdaDocument;

/**
 * The EditView class represents the main window of the SdaEdit application. It provides
 * a user interface for editing SDA documents, including a menu bar with options to open
 * and save files, as well as a split view containing a tree view and a text view.
 */
@SuppressWarnings("serial")
public final class EditView extends JFrame {

	private static final Dimension SIZE = new Dimension(900,600);
	private static final int LOCATION_X = (UI.SCREEN_SIZE.width-SIZE.width)/2;
	private static final int LOCATION_Y = (UI.SCREEN_SIZE.height-SIZE.height)/2;

	private JMenu fileMenu;     // the "File" menu in the menu bar
	private JMenuItem openFile; // the menu item for opening a file
	private JMenuItem saveFile; // the menu item for saving the current document
	private JMenuItem saveAsFile; // the menu item for saving the document with a new name


	/** Constructs a new EditView with a menu bar and sets its size and location on the screen. */
	public EditView() {

		setBounds(LOCATION_X, LOCATION_Y, SIZE.width, SIZE.height);

		JMenuBar menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		
		openFile = new JMenuItem("Open...");
		fileMenu.add(openFile);
		
		saveFile = new JMenuItem("Save");
		saveFile.setEnabled(false); // initially disabled until a document is opened
		fileMenu.add(saveFile);
		
		saveAsFile = new JMenuItem("Save as...");
		saveAsFile.setEnabled(false); // initially disabled until a document is opened
		fileMenu.add(saveAsFile);
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	

	/** Sets the action to be performed when the "Open ..." menu item is selected. */
    public void setOpenFileAction(Runnable action) {
        openFile.addActionListener(e -> action.run());
    }
    
    /** Sets the action to be performed when the "Save" menu item is selected. */
    public void setSaveFileAction(Runnable action) {
        saveFile.addActionListener(e -> action.run());
    }
	

    /** Sets the action to be performed when the "Save As" menu item is selected. */
    public void setSaveAsFileAction(Runnable action) {
        saveAsFile.addActionListener(e -> action.run());
    }
    

	/*
	 * Installs a FileMenuListener on the "File" menu to enable or disable the Save
	 * (As) menu items based on the current document state.
	 *
	 * @param document the SdaDocument to monitor for changes
	 */
	private void installFileMenuListener(SdaDocument document) {
		
		for (var listener : fileMenu.getMenuListeners()) {
			fileMenu.removeMenuListener(listener);
		} // Remove any existing listeners to avoid duplicates
		fileMenu.addMenuListener(new FileMenuListener(document, saveFile, saveAsFile));
	}
	
	
	/*
	 * Installs an EditWindowAdapter on the EditView to handle window closing events.
	 * When the window is closed, it checks if the given document has unsaved changes
	 * and prompts the user to save them before closing.
	 *
	 * @param document the SdaDocument to monitor for unsaved changes
	 */
	private void installWindowListener(SdaDocument document) {
		
		for (var listener : getWindowListeners()) {
			removeWindowListener(listener);
		} // Remove any existing listeners to avoid duplicates
		addWindowListener(new MainWindowAdapter(document, this));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}


	/**
	 * Displays the given SdaDocument in the EditView, creating a TreeView on the
	 * left and a TextView on the right. It also installs the necessary listeners 
	 * for interaction with the views.
	 * 
	 * @param document the SdaDocument to be displayed
	 */
	public void showDocument(SdaDocument document) {

	    // Reset the main view
	    setTitle(document.getPath());
	    getContentPane().removeAll();
	    
	    // Create the TreeView on the left
	    var treeview = new TreeView(document);
	    treeview.setPreferredSize(UI.scaledDim(SIZE, 0.3333, 1.0));
	    add(treeview, BorderLayout.WEST);

	    // Create the TextView on the right  
		var textview = new TextView(document);
	    textview.setPreferredSize(UI.scaledDim(SIZE, 0.6667, 1.0));
	    add(textview, BorderLayout.CENTER);
	    
	    // Install all listeners
	    treeview.installMouseListener(textview);
	    textview.installFocusListener(treeview);
	    installFileMenuListener(document);
	    installWindowListener(document);
	    
	    // Finally (re)paint the view
	    getContentPane().revalidate();
	    getContentPane().repaint();
	}

}
