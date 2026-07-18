package samples.editor.controller;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import samples.editor.model.SdaDocument;
import samples.editor.view.EditView;
import samples.editor.view.UI;

/**
 * The MainController class manages the main application logic for the SdaEdit editor.
 * It handles opening and saving documents, as well as coordinating between the model
 * (SdaDocument) and the view (EditView).
 */
public final class MainController {
	
	private final EditView editView; // the main view of the edit application
	private SdaDocument document; // the current document (or null if none is open)

	
	/**
	 * Constructs a new MainController with the given file. If the file is not null,
	 * it attempts to load it into a new SdaDocument and display it in the view.
	 */
	public MainController(File file) {

		editView = new EditView();
		editView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editView.setOpenFileAction(this::openFileDialog);
		editView.setSaveFileAction(this::saveDocument);
		editView.setSaveAsFileAction(this::saveAsDocument);

		if (file != null && loadDocument(file)) {
			editView.showDocument(document);
		}
	}
	
	
	/**
	 * Returns the main EditView of the application.
	 *
	 * @return the EditView instance
	 */
	public EditView getEditView() {
		return editView;
	}

	
	/*
	 * Loads a document from the specified file. If loading fails, it shows an error
	 * dialog and returns false. If successful, it updates the current document and
	 * returns true.
	 */
	private boolean loadDocument(File file) {
		
		try {
			document = new SdaDocument(file);
		} catch (Exception e) {
			UI.showExceptionDialog(editView, "Failed to load " + file.toString(), e);
			return false;
		}
		return true;
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
			
			if (loadDocument(chooser.getSelectedFile())) {
				editView.showDocument(document);
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
