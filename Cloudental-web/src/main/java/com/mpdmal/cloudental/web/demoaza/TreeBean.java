package com.mpdmal.cloudental.web.demoaza;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class TreeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private TreeNode root;

	private TreeNode selectedNode;

	public TreeBean() {
		root = new DefaultTreeNode("Root", null);
//		TreeNode node0 = new DefaultTreeNode("Activity 0", root);
//		TreeNode node1 = new DefaultTreeNode("Activity 1", root);
//		TreeNode node2 = new DefaultTreeNode("Activity 2", root);
//		TreeNode node3 = new DefaultTreeNode("Activity 3", root);
//		TreeNode node4 = new DefaultTreeNode("Activity 4", root);
//		TreeNode node00 = new DefaultTreeNode("Visit 1", node0);
//		TreeNode node01 = new DefaultTreeNode("Visit 2", node0);
//
//		TreeNode node10 = new DefaultTreeNode("Visit 1", node1);
//		TreeNode node11 = new DefaultTreeNode("Visit 2", node1);
	}
	public TreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	public void displaySelectedSingle(ActionEvent event) {
        if(selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());

            FacesContext.getCurrentInstance().addMessage(null, message);
        }
	}
}
					