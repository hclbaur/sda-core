package samples.editor.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import samples.editor.controller.FileMenuListener;
import samples.editor.model.SdaDocument;


@SuppressWarnings("serial")
public final class FileMenu extends JMenu {

	//private JMenuItem newFile; // the menu item for creating a new document
	//private JMenuItem openFile; // the menu item for opening a document from file
	private JMenuItem saveFile; // the menu item for saving the current document to file
	private JMenuItem saveAsFile; // the menu item for saving the document to a new file

	private EditView editView; // the main view of the application
	private SdaDocument document; // the document being edited


	/**
	 * Create a new FileMenu with New, Open, Save and Save As items. If an initial
	 * file is supplied, it will be loaded right away.
	 * 
	 * @param editView    the main application window
	 * @param initialFile the file to be loaded initially, may be null
	 */
	public FileMenu(EditView editView, File initialFile) {

		super("File");
		this.editView = editView;
		
		var newFile = new JMenuItem("New");
		newFile.addActionListener(e -> newDocument());
		add(newFile);
		
		var openFile = new JMenuItem("Open...");
		openFile.addActionListener(e -> openFile());
		add(openFile);
		
		saveFile = new JMenuItem("Save");
		saveFile.addActionListener(e -> saveFile());
		add(saveFile);
		
		saveAsFile = new JMenuItem("Save as...");
		saveAsFile.addActionListener(e -> saveAsFile());
		add(saveAsFile);
		
		addMenuListener(new FileMenuListener(this));
		
		if (initialFile != null) {
			document = loadDocument(initialFile);
		}
	}

	
	/**
	 * This method is called by the FileMenuListener to enable or disable some menu
	 * items based on the document status once the File menu is opened.
	 */
	public void selectMenu() {
		
		if (document == null) {
			saveFile.setEnabled(false);
			saveAsFile.setEnabled(false);
		} else {
			saveFile.setEnabled(document.hasChanges() && document.isFile());
			saveAsFile.setEnabled(true);
		}
	}

	
	/*
	 * Creates a new document and shows it in the main view. If the current document
	 * has unsaved changes, it prompts the user to save them before creating a new
	 * document. If the user cancels or if saving fails, it does not create a new
	 * document.
	 */
	private void newDocument() {
		
		if (document != null && document.hasChanges()) {

			int option = JOptionPane.showConfirmDialog(editView,
				"Do you want to save your changes before starting a new document?", 
				"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

			if (option == JOptionPane.YES_OPTION) {
				
				if (! saveFile()) {
					return; // Saving failed, do not create a new document
				}

			} else if (option == JOptionPane.CANCEL_OPTION) {
				return; // User canceled the operation, do not create a new document
			}
			// Saved or NO selected; continue to create a new document
		}
		
		document = new SdaDocument();
		editView.showDocument(document);
	}


	/*
	 * This method is called by the FileMenu to load an initial or user selected
	 * file. The document is immediately shown in the main view.
	 */
	private SdaDocument loadDocument(File file) {
		
		SdaDocument document = null;
		try {
			document = new SdaDocument(file);
		} catch (Exception e) {
			UI.showExceptionDialog(this, "Failed to load " + file.toString(), e);
		}
		
		if (document != null)
			editView.showDocument(document);
		
		return document;
	}


	/*
	 * Shows a file chooser dialog to select a file to open. If the current document
	 * has unsaved changes, it prompts the user to save them before opening the new
	 * file. If the user cancels or if saving fails, it does not open a new file.
	 */
	private void openFile() {
		
		if (document != null && document.hasChanges()) {

			int option = JOptionPane.showConfirmDialog(editView,
				"Do you want to save your changes before loading another file?", 
				"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

			if (option == JOptionPane.YES_OPTION) {
				
				if (! saveFile()) {
					return; // Saving failed, do not create a new document
				}
				
			} else if (option == JOptionPane.CANCEL_OPTION) {
				return; // User canceled the operation, do not load a new document
			}
			// Saved or NO selected; continue to load a new document
		}
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select File");
		
		if (chooser.showOpenDialog(editView) == JFileChooser.APPROVE_OPTION) {

			SdaDocument newdoc = loadDocument(chooser.getSelectedFile());
			if (newdoc != null)
				document = newdoc; // only update if successfully loaded
		}
	}


	/**
	 * Saves the current document and returns true if successful. If the document is
	 * not yet associated with a file, it calls saveAsFile() to prompt the user to
	 * select a file. If saving fails, it shows an error dialog and returns false.
	 */
	public boolean saveFile() {

		/*
		 * There are two safety checks: if no document is open, or if the document has
		 * no changes, it shows an info dialog instead of saving. Although the Save menu
		 * item is disabled when there are no changes, this action may be triggered in
		 * another way.
		 */
		 
		if (document == null) {
			UI.showInfoDialog(editView, "Nothing to save", "Open a document first.");
			return false;
		}
		
		if (! document.hasChanges()) {
			UI.showInfoDialog(editView, "Nothing to save", "Make some changes first.");
			return false;
		}

		if (! document.isFile())
			return saveAsFile();

		try {
			document.save();
		} catch (Exception e) {
			UI.showExceptionDialog(editView, "Failed to save " + document.getPath(), e);
			return false;
		}
		return true;
	}
	
	
	/*
	 * Saves the current document to a new file selected by the user and returns
	 * true if successful. If the user cancels the file chooser or chooses not to
	 * overwrite an existing file, it does not save. If saving fails, it shows an
	 * error dialog.
	 */
	private boolean saveAsFile() {

		if (document == null) {
			UI.showInfoDialog(editView, "Nothing to save", "Open a document first.");
			return false;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save As");
		chooser.setApproveButtonText("Save");
		chooser.setSelectedFile(new File("document.sda"));

		if (chooser.showOpenDialog(editView) == JFileChooser.APPROVE_OPTION) {

			if (chooser.getSelectedFile().exists()) {

				int option = JOptionPane.showConfirmDialog(editView,
					"File already exists. Do you want to overwrite it?", "Overwrite File",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (option != JOptionPane.YES_OPTION)
					return false; // User chose not to overwrite the file
			}
		} else
			return false; // User canceled the file chooser dialog, do not save the document

		try {
			document.save(chooser.getSelectedFile());
		} catch (Exception e) {
			UI.showExceptionDialog(editView, "Failed to save " + document.getPath(), e);
			return false;
		}
		
		editView.setTitle(document.getPath());	
		return true;
	}


	/**
	 * Returns the current document being edited.
	 * 
	 * @return the current SdaDocument, or null if no document is open
	 */
	public SdaDocument getDocument() {
		return document;
	}
}
