package samples.editor.model;

import javax.swing.tree.DefaultMutableTreeNode;

import be.baur.sda.DataNode;
import be.baur.sda.SDA;

/**
 * The SdaTreeNode class is a specialized tree node that wraps a DataNode from
 * the SDA structure. It provides methods to create a tree representation of the
 * SDA data, including child nodes, and overrides the toString method to render
 * the tree node in a human-readable format.
 */
@SuppressWarnings("serial")
public final class SdaTreeNode extends DefaultMutableTreeNode {

	private final DataNode dataNode; // for quick internal reference
	
	/**
	 * Creates a MutableDataTreeNode from a DataNode. The resulting node does not
	 * have child nodes. To include descendant nodes, use {@link #from(DataNode)}
	 * 
	 * @param node the DataNode to create a MutableDataTreeNode from
	 */
	public SdaTreeNode(DataNode node) {
		this.dataNode = node;
		setUserObject(node);
	}


	/**
	 * Returns a MutableDataTreeNode from a DataNode. Unlike the constructor method,
	 * this will recursively add a child MutableDataTreeNode for every child in the
	 * supplied DataNode.
	 * 
	 * @param node a DataNode
	 * @return a MutableDataTreeNode
	 */
	public static SdaTreeNode from(DataNode node) {
		
		var mdtNode = new SdaTreeNode(node);
		for (DataNode child : node.nodes()) {
			mdtNode.add( from(child) );
		}
		return mdtNode;	
	}


	@Override
	public boolean isLeaf() {
		return dataNode.isLeaf();
		
	}

	
	@Override
	public String toString() {

		final String value = dataNode.getValue();
		final boolean nodeIsLeaf = dataNode.isLeaf();
		final StringBuilder sb = new StringBuilder(dataNode.getName());
		
		if (! value.isEmpty() || nodeIsLeaf) 
			sb.append(" ").append((char) SDA.QUOTE)
				.append(SDA.encode(value)).append((char) SDA.QUOTE);
		
		if (! nodeIsLeaf) {
			sb.append(" ").append((char) SDA.LBRACE)
				.append(dataNode.isParent() ? "..." : " ")
				.append((char) SDA.RBRACE);
		}

		return sb.toString();
	}

}
