package samples.editor.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import samples.editor.controller.MainWindowAdapter;
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


	/**
	 * Constructs a new EditView with a menu bar and sets its size and location on
	 * the screen. The initial close action is to exit the application, but this is
	 * changed after a document has been loaded, to prevent loss of unsaved changes.
	 * 
	 * @param file the file to be opened initially, may be null
	 */
	public EditView(File file) {

		setBounds(LOCATION_X, LOCATION_Y, SIZE.width, SIZE.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		var fileMenu = new FileMenu(this, this::loadDocument, file);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}


	/*
	 * This method is called by the FileMenu to load an initial or selected file.
	 */
	private SdaDocument loadDocument(File file) {
		
		SdaDocument document = null;
		try {
			document = new SdaDocument(file);
		} catch (Exception e) {
			UI.showExceptionDialog(this, "Failed to load " + file.toString(), e);
		}
		
		if (document != null)
			showDocument(document);
		return document;
	}

	
	/*
	 * Installs an EditWindowAdapter on the EditView to handle window closing events.
	 * When the window is closed, it checks if the given document has unsaved changes
	 * and prompts the user to save them before closing.
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
	    installWindowListener(document);
	    
	    // Finally (re)paint the view
	    getContentPane().revalidate();
	    getContentPane().repaint();
	}

}
