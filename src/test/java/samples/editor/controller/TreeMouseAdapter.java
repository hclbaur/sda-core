package samples.editor.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import be.baur.sda.DataNode;
import samples.editor.model.SdaTreeNode;
import samples.editor.view.NodeInputDialog;
import samples.editor.view.TextView;

/**
 * The TreeMouseAdapter class listens for mouse events on a JTree and provides
 * context menu options for adding, copying, deleting, and pasting nodes in the
 * tree. It also handles double-click events for editing node names and values.
 */
public final class TreeMouseAdapter extends MouseAdapter {

    private final JTree tree; // the tree that this adapter is associated with
    private final TextView textView; // the text view to refresh when the tree is updated
	
    private DataNode pasteableNode; // the most recently deleted or copied node, can be pasted

       
    /**
	 * Constructs a new TreeMouseAdapter for the given JTree and TextView. This
	 * adapter listens for mouse events on the tree and updates the tree and text
	 * view accordingly.
	 *
	 * @param tree the JTree that this adapter is associated with
	 * @param textView the TextView to refresh when the tree is updated
	 */
    public TreeMouseAdapter(JTree tree, TextView textView) {
        this.tree = tree;
        this.textView = textView;
    }


    /**
	 * Handles mouse pressed events on the tree. If the right mouse button is pressed,
	 * it shows a popup menu with options to add, copy, delete, paste, etc. If the left
	 * mouse button is double-clicked, it opens a dialog to edit the name and value of
	 * the selected node.
	 *
	 * @param event the mouse event that triggered this method
	 */
    @Override
    public void mousePressed(MouseEvent event) {

        if (SwingUtilities.isRightMouseButton(event)) {
            // Determine which node was right-clicked
            int row = tree.getClosestRowForLocation(event.getX(), event.getY());
            tree.setSelectionRow(row);
            
            TreePath path = tree.getPathForLocation(event.getX(), event.getY());
            if (path != null) {
                SdaTreeNode selectedNode = (SdaTreeNode) path.getLastPathComponent();
                showPopupMenu(event, selectedNode);
            }
        }
        else if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 2) {
			// Handle double-click for editing the node name and/or value
			TreePath path = tree.getPathForLocation(event.getX(), event.getY());
			if (path != null) {
				SdaTreeNode selectedNode = (SdaTreeNode) path.getLastPathComponent();
				editNode(selectedNode);
			}
		}
        	
    }


	/*
	 * Convenience method that returns the tree model. We don't cache the model in a
	 * field because it can be replaced at runtime, and we want to always get the
	 * current model.
	 */
    private DefaultTreeModel model() {
		return (DefaultTreeModel) tree.getModel();
	}


    /* Shows a dialog to edit the name and value of the selected node. */
	private void editNode(SdaTreeNode selectedNode) {
		
		var node = (DataNode) selectedNode.getUserObject();
		var dialog = new NodeInputDialog(node.getName(), node.getValue());
		var nvpair = dialog.showNodeInputDialog(tree);
		
		if (nvpair != null) { // user clicked OK and provided a valid name
			node.setName(nvpair.name); node.setValue(nvpair.value);
			model().nodeChanged(selectedNode); // update the visual representation of the node
			textView.refresh(); // update the textual view as well
		}
	}


	/*
	 * Shows a popup menu with options to add, copy, delete, paste, etc. Options are
	 * enabled or disabled based on the context.
	 */
    private void showPopupMenu(MouseEvent event, SdaTreeNode selectedNode) {

        JPopupMenu popup = new JPopupMenu();
        
        JMenuItem addBeforeItem = new JMenuItem("Add Before");
		if (selectedNode.isRoot())
			addBeforeItem.setEnabled(false); // disable if root node is selected
        addBeforeItem.addActionListener( e -> addBefore(selectedNode) );
		popup.add(addBeforeItem);
		
        JMenuItem addChildItem = new JMenuItem("Add Child");
        addChildItem.addActionListener( e -> addChild(selectedNode) );
		popup.add(addChildItem);
		
        JMenuItem addAfterItem = new JMenuItem("Add After");
		if (selectedNode.isRoot())
			addAfterItem.setEnabled(false); // disable if root node is selected
		addAfterItem.addActionListener( e -> addAfter(selectedNode) );
		popup.add(addAfterItem);
		popup.addSeparator();
		
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener( e -> copyNode(selectedNode) );
		popup.add(copyItem);
		
		JMenuItem deleteItem = new JMenuItem("Cut/Delete");
		if (selectedNode.isRoot())
			deleteItem.setEnabled(false); // disable if root node is selected
		deleteItem.addActionListener(e -> deleteNode(selectedNode));
		popup.add(deleteItem);
		popup.addSeparator();
		
		JMenuItem expandAllItem = new JMenuItem("Expand All");
		if (selectedNode.isLeaf())
			expandAllItem.setEnabled(false); // disable if leaf node is selected
		else
			expandAllItem.addActionListener(e -> expandAll(selectedNode));
		popup.add(expandAllItem);
		
		JMenuItem collapseAllItem = new JMenuItem("Collapse All");
		if (selectedNode.isLeaf())
			collapseAllItem.setEnabled(false); // disable if leaf node is selected
		else
			collapseAllItem.addActionListener(e -> collapseAll(selectedNode));
		popup.add(collapseAllItem);
		popup.addSeparator();
		
        JMenuItem pasteBeforeItem = new JMenuItem("Paste Before");
        if (pasteableNode == null || selectedNode.isRoot())
        	pasteBeforeItem.setEnabled(false); // disable if no node to paste or root selected
        pasteBeforeItem.addActionListener( e -> pasteBefore(selectedNode) );
		popup.add(pasteBeforeItem);
		
        JMenuItem pasteItem = new JMenuItem("Paste Child");
        if (pasteableNode == null)
			pasteItem.setEnabled(false); // disable if there is no node to paste
        pasteItem.addActionListener( e -> pasteNode(selectedNode) );
		popup.add(pasteItem);
		
        JMenuItem pasteAfterItem = new JMenuItem("Paste After");
        if (pasteableNode == null || selectedNode.isRoot())
        	pasteAfterItem.setEnabled(false); // disable if no node to paste or root selected
        pasteAfterItem.addActionListener( e -> pasteAfter(selectedNode) );
		popup.add(pasteAfterItem);
		
        popup.show(tree, event.getX(), event.getY());
    }

    
	/*
	 * Adds a new sibling node to a tree node, either before or after it, depending
	 * on the offset (0 = before, 1 = after). The tree node must have a parent (so
	 * cannot be the root). The method accepts a data node to add as a sibling, or
	 * if null, it will prompt the user for input to create a new data node.
	 */
	private void addSibling(SdaTreeNode treeNode, DataNode dataNode, int offset) {

		if (dataNode == null) {
			
			var dialog = new NodeInputDialog("node", "");
			var nvpair = dialog.showNodeInputDialog(tree);
			
			if (nvpair != null) { // user clicked OK and provided a valid name
				dataNode = new DataNode(nvpair.name, nvpair.value);
			} else
				return; // user cancelled the dialog, do not add a sibling
		}
		
		var parentNode = ((DataNode) treeNode.getUserObject()).getParent();
		var parentTreeNode = (MutableTreeNode) treeNode.getParent();
		int index = parentTreeNode.getIndex(treeNode) + offset;
		parentNode.add(dataNode, index);
		model().insertNodeInto(new SdaTreeNode(dataNode), parentTreeNode, index);
		textView.refresh(); // update the textual view as well
	}
 

	/*
	 * Adds a new sibling node before the selected node. The root node cannot have
	 * siblings, so this option is disabled for the root node.
	 */
	private void addBefore(SdaTreeNode selectedTreeNode) {

		if (!selectedTreeNode.isRoot()) { // should always be true, just to be safe
			addSibling(selectedTreeNode, null, 0); // add before selected node
		} else
			throw new AssertionError("Unexpected: selected node is root");
	}


    /* Adds a new child node to the selected node. */
	private void addChild(SdaTreeNode selectedTreeNode) {

		var dialog = new NodeInputDialog("node", "");
		var nvpair = dialog.showNodeInputDialog(tree);
		
		if (nvpair != null) { // user clicked OK and provided a valid name

			var newDataNode = new DataNode(nvpair.name, nvpair.value);
			var parentDataNode = (DataNode) selectedTreeNode.getUserObject();
			if (parentDataNode.add(newDataNode)) { // never fails
				var childTreeNode = new SdaTreeNode(newDataNode);
				model().insertNodeInto(childTreeNode, selectedTreeNode, selectedTreeNode.getChildCount());
				textView.refresh(); // update the textual view as well
			}
		}
	}

	
	/*
	 * Adds a new sibling node after the selected node. The root node cannot have
	 * siblings, so this option is disabled for the root node.
	 */
	private void addAfter(SdaTreeNode selectedTreeNode) {

		if (!selectedTreeNode.isRoot()) { // should always be true, just to be safe
			addSibling(selectedTreeNode, null, 1); // add after selected node
		} else
			throw new AssertionError("Unexpected: selected node is root");
	}


	/* Copies the given node and saves it for potential pasting. */
	private void copyNode(SdaTreeNode selectedTreeNode) {

		var node = (DataNode) selectedTreeNode.getUserObject();
		pasteableNode = node.copy(); // create a copy of the node for pasting
	}


	/*
	 * Deletes the selected node, saving it for future pasting. The root node cannot
	 * be deleted.
	 */
	private void deleteNode(SdaTreeNode selectedTreeNode) {

		if (!selectedTreeNode.isRoot()) { // should always be true, just to be safe

			// Remove from the underlying SDA structure
			var node = (DataNode) selectedTreeNode.getUserObject();
			if (node.getParent() != null) { // should always be true, just to be safe
				if (node.getParent().remove(node)) {
					// Remove from the model (also updates the visual)
					model().removeNodeFromParent(selectedTreeNode);
					textView.refresh(); // update the textual view as well
					pasteableNode = node; // save removed node for potential pasting
				}
			} else
				throw new AssertionError("Unexpected: node has no parent");
		} else
			throw new AssertionError("Unexpected: selected node is root");
	}


	/* Expands the selected node and all its descendant nodes. */
	private void expandAll(SdaTreeNode selectedTreeNode) {
		
		TreePath path = new TreePath(selectedTreeNode.getPath());
		tree.expandPath(path);
		
		for (int i = 0; i < selectedTreeNode.getChildCount(); i++) {
			var child = (SdaTreeNode) selectedTreeNode.getChildAt(i);
			expandAll(child);
		}
	}
	
	
	/* Collapses the selected node and all its descendant nodes.  */
	private void collapseAll(SdaTreeNode selectedTreeNode) {
		
		for (int i = 0; i < selectedTreeNode.getChildCount(); i++) {
			var child = (SdaTreeNode) selectedTreeNode.getChildAt(i);
			collapseAll(child);
		}
		
		TreePath path = new TreePath(selectedTreeNode.getPath());
		tree.collapsePath(path);
	}

	
	/*
	 * Pastes the most recently deleted or copied node as a sibling before the
	 * selected node.
	 */
	private void pasteBefore(SdaTreeNode selectedTreeNode) {

		if (pasteableNode != null) // should always be true, just to be safe
			if (pasteableNode.getParent() == null) { // should always be true, just to be safe
				addSibling(selectedTreeNode, pasteableNode, 0); // add before selected node
				pasteableNode = null; // cannot be pasted again until another cut or copy
			} else
				throw new AssertionError("Unexpected: pasteable node has a parent");
		else
			throw new AssertionError("Unexpected: pasteable node is null");
	}

	
	/*
	 * Pastes the most recently deleted or copied node as a child of the selected
	 * node.
	 */
	private void pasteNode(SdaTreeNode selectedTreeNode) {

		if (pasteableNode != null) { // should always be true, just to be safe
			
			// Add node to the underlying SDA structure
			var node = (DataNode) selectedTreeNode.getUserObject();
			if (pasteableNode.getParent() == null) { // should always be true, just to be safe
				if (node.add(pasteableNode)) {
					// Add to the model (also updates the visual)
					model().insertNodeInto(SdaTreeNode.from(pasteableNode), selectedTreeNode, selectedTreeNode.getChildCount());
					textView.refresh(); // update the textual view as well
					pasteableNode = null; // cannot be pasted again until another cut or copy
				}
			} else
				throw new AssertionError("Unexpected: pasteable node has a parent");
		} else
			throw new AssertionError("Unexpected: pasteable node is null");
	}
	
	
	/*
	 * Pastes the most recently deleted or copied node as a sibling before the
	 * selected node.
	 */
	private void pasteAfter(SdaTreeNode selectedTreeNode) {

		if (pasteableNode != null) // should always be true, just to be safe
			if (pasteableNode.getParent() == null) { // should always be true, just to be safe
				addSibling(selectedTreeNode, pasteableNode, 1); // add after selected node
				pasteableNode = null; // cannot be pasted again until another cut or copy
			} else
				throw new AssertionError("Unexpected: pasteable node has a parent");
		else
			throw new AssertionError("Unexpected: pasteable node is null");
	}
}