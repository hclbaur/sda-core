package samples.editor.view;

import java.io.File;
import java.util.function.Function;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import samples.editor.controller.FileMenuListener;
import samples.editor.model.SdaDocument;


@SuppressWarnings("serial")
public final class FileMenu extends JMenu {

	private JMenuItem openFile; // the menu item for opening a file
	private JMenuItem saveFile; // the menu item for saving the current document
	private JMenuItem saveAsFile; // the menu item for saving the document with a new name

	private EditView editView; // the main view of the application
	private Function<File, SdaDocument> documentLoader; // a function that takes a File and returns an SdaDocument
	private SdaDocument document; // the current document being edited


	/**
	 * Create a new FileMenu with Open, Save and Save As items. A document loader is
	 * expected to handle loading new documents from file. If an initial file is
	 * supplied, it will be loaded right away.
	 * 
	 * @param editView the main application window
	 * @param documentLoader the document loader that loads the file
	 * @param initialFile the file to be loaded initially, may be null
	 */
	public FileMenu(EditView editView, Function<File, SdaDocument> documentLoader, File initialFile) {

		super("File");
		this.editView = editView;
		this.documentLoader = documentLoader;
		
		openFile = new JMenuItem("Open...");
		openFile.addActionListener(e -> openFileDialog());
		add(openFile);
		
		saveFile = new JMenuItem("Save");
		saveFile.addActionListener(e -> saveDocument());
		saveFile.setEnabled(false); // initially disabled until a document is opened
		add(saveFile);
		
		saveAsFile = new JMenuItem("Save as...");
		saveAsFile.addActionListener(e -> saveAsDocument());
		saveAsFile.setEnabled(false); // initially disabled until a document is opened
		add(saveAsFile);
		
		addMenuListener(new FileMenuListener(this));
		
		if (initialFile != null)
			document = documentLoader.apply(initialFile);
	}

	
	/**
	 * This method is called by the FileMenuListener to enable or disable menu items
	 * based on the document status once the File menu is opened.
	 * 
	 */
	public void selectMenu() {
		
		if (document == null) {
			saveFile.setEnabled(false);
			saveAsFile.setEnabled(false);
		} else {
			saveAsFile.setEnabled(true);
			saveFile.setEnabled(document.isChanged());
		}
	}


	/*
	 * Opens a file chooser dialog to select a file to open. If the current document
	 * has unsaved changes, it prompts the user to save them before opening the new
	 * file. If the user cancels or if saving fails, it does not open a new file.
	 */
	private void openFileDialog() {
		
		if (document != null && document.isChanged()) {

			int option = JOptionPane.showConfirmDialog(editView, 
				"Do you want to save your changes before loading another file?", 
				"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			
			if (option == JOptionPane.YES_OPTION) {
				if (! saveDocument()) {
					return; // Saving failed, do not load the a document
				}
			} else if (option == JOptionPane.CANCEL_OPTION) {
				return; // User canceled the operation, do not load a new document
			}
			// Saved or NO selected; continue to load a new document
		}
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select File");
		
		if (chooser.showOpenDialog(editView) == JFileChooser.APPROVE_OPTION) {
			SdaDocument doc = documentLoader.apply(chooser.getSelectedFile());
			if (doc != null) {
				document = doc;
			}
		}
	}
	
	
	/*
	 * Saves the current document and returns true if successful. There are two
	 * safety checks: if no document is open, or if the document has no changes, it
	 * shows an info dialog instead of saving. Although the "Save" menu item is
	 * disabled when there are no changes, this action might be triggered in another
	 * way, so they are a pre-caution.
	 */
	private boolean saveDocument() {

		if (document == null) {
			UI.showInfoDialog(editView, "Nothing to save", "Open a document first.");
			return false;
		}
		
		if (!document.isChanged()) {
			UI.showInfoDialog(editView, "Nothing to save", "Make some changes first.");
			return false;
		}
		
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
	private boolean saveAsDocument() {

		if (document == null) {
			UI.showInfoDialog(editView, "Nothing to save", "Open a document first.");
			return false;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select File");
		
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

}
