package samples.editor.model;

import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public final class SdaTreeModel extends DefaultTreeModel {

	// not really useful unless I add functionality
	
	/**
	 * Constructs a new SdaTreeModel based on the given SdaDocument. The model is
	 * initialized with the root node of the document's tree structure.
	 */
    public SdaTreeModel(SdaDocument document) {
        super(document.getSdaTreeNode());
    }
}
