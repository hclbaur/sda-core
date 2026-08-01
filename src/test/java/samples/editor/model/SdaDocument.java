package samples.editor.model;

import java.io.File;
import java.io.IOException;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;
import be.baur.sda.io.FileParseException;
import be.baur.sda.io.ParseException;

/**
 * The SdaDocument class represents a document in the editor. It encapsulates the
 * internal data structure of the document (a tree of DataNodes) and provides
 * methods for loading, saving, and converting the document to different formats.
 */
public final class SdaDocument {

    private DataNode root; // the root of the document's data structure (a tree of DataNodes)
    private File file; // the file associated with this document (can be null if not saved)
    private String canonical; // canonical text representation (used for change detection)

    
	/**
	 * Constructs a new SdaDocument with a default root node.
	 */
	public SdaDocument() {
		root = new DataNode("root"); root.expand();
		canonical = root.toString();
	}


	/**
	 * Constructs a new SdaDocument by parsing the given file in SDA format.
	 *
	 * @param file the file to parse and load into the document
	 * @throws FileParseException if the file cannot be parsed into a valid SDA
	 *                            structure
	 * @throws IOException        if an I/O error occurs during parsing
	 */
	public SdaDocument(File file) throws FileParseException, IOException {
		root = SDA.parse(file);
		canonical = root.toString();
		this.file = file;
	}

    
	/**
	 * This method converts the internal DataNode structure of the SdaDocument into
	 * a SdaTreeNode structure, which is more suitable for use in the TreeView.
	 * 
	 * @return an SdaTreeNode representing the document
	 */
    public SdaTreeNode getSdaTreeNode() {
        return SdaTreeNode.from(root);
    }


	/**
	 * Returns a text representation of the document in human readable SDA format.
	 *
	 * @return a text representation of the document
	 */
	public String toText() {
		try {
			return SDA.format(root);
		} catch (IOException e) { // never happens
			return "Error converting document to text: " + e.getMessage();
		}
	}
	
	
	/**
	 * Checks if the document has been associated with a file that it can be saved
	 * to when changes are made.
	 * 
	 * @return true if the document is backed by a file, false otherwise
	 */
	public boolean isFile() {
		return file != null;
	}


	/**
	 * Checks if the document has been changed since it was last saved or loaded.
	 * 
	 * @return true if the document has been changed, false otherwise
	 */
	public boolean hasChanges() {
		return !root.toString().equals(canonical);
	}


	/**
	 * Saves the current document in SDA format (to the associated file).
	 * 
	 * @throws IOException           if an I/O error occurs during saving
	 * @throws IllegalStateException if the document is not backed by a file
	 */
	public void save() throws IOException {
		
		if (file == null)
			throw new IllegalStateException("document is not backed by a file");

		SDA.format(file, root);
		canonical = root.toString(); // update canonical representation after saving
	}


	/**
	 * Saves the current document to the specified file in SDA format.
	 *
	 * @param file the file to save the document to
	 * @throws IOException if an I/O error occurs during saving
	 */
	public void save(File file) throws IOException {
		this.file = file;
		SDA.format(file, root);
		canonical = root.toString(); // update canonical representation after saving
	}
	

	/**
	 * Returns the file path of the current document.
	 * 
	 * @return the file path or "Untitled" if no file is associated.
	 */
	public String getPath() {
		return file != null ? file.getPath() : "Untitled";
	}
	
	
	/**
	 * Updates the document's internal structure by parsing the given text in SDA
	 * format. If the new structure is different from the current one, it replaces
	 * the current root with the new one and returns true. If there are no changes
	 * other than in non-functional whitespace, the root is unchanged and the method
	 * returns false.
	 *
	 * @param text the text to parse and update the document with, not null
	 * @return true if the document was updated, false if there were no changes
	 * @throws ParseException if the text cannot be parsed into a valid SDA
	 *                        structure
	 * @throws IOException    if an I/O error occurs during parsing
	 */
	public boolean update(String text) throws ParseException, IOException {

		var newRoot = SDA.parse(text);
		if (newRoot.toString().equals(root.toString()))
			return false; // no changes
		root = newRoot;
		return true;
	}
}

