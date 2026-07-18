package samples.editor.view;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import samples.editor.controller.TreeMouseAdapter;
import samples.editor.model.SdaDocument;
import samples.editor.model.SdaTreeModel;

/**
 * The TreeView class is a scrollable tree view that displays the hierarchical
 * structure of an SdaDocument. It allows users to navigate through the
 * document's nodes and provides methods to refresh the view and handle mouse
 * events.
 */
@SuppressWarnings("serial")
public final class TreeView extends JScrollPane {

	private final JTree tree;
	private final SdaDocument document;


	/**
	 * Constructs a new TreeView for the given SdaDocument. The tree is initialized
	 * with the current structure of the document.
	 *
	 * @param document the SdaDocument to display in this TreeView
	 */
	public TreeView(SdaDocument document) {

		this.document = document;
		tree = new JTree(new SdaTreeModel(document));
		tree.setShowsRootHandles(true);
		tree.setEditable(false);  // edit handled by MouseAdapter, therefore
		tree.setToggleClickCount(0); // disable expand/collapse on mouse click
		//tree.setFont(new Font("Monospaced", Font.PLAIN, 12));
		tree.setFont(tree.getFont().deriveFont(12F)); 
		//System.out.println(UIManager.get("Tree.font"));
		//System.out.println(UIManager.get("TextArea.font"));
		setViewportView(tree);
	}


	/**
	 * Installs a listener on the tree that will handle mouse events and update the
	 * given TextView accordingly.
	 *
	 * @param textView the TextView to be updated based on tree interactions
	 */
	public void installMouseListener(TextView textView) {
		var adapter = new TreeMouseAdapter(tree, textView);
		tree.addMouseListener(adapter);
	}
	
	
	/**
	 * 
	 * Refreshes the tree view with the current content of the document. This method
	 * will collapse the tree, so it should be called only when it is not possible
	 * to update the tree view incrementally (e.g., after a major change or changes
	 * that cannot be easily tracked to specific nodes).
	 */
	public void refresh() {
		tree.setModel(new SdaTreeModel(document));
		((SdaTreeModel) tree.getModel()).reload();
	}
}
