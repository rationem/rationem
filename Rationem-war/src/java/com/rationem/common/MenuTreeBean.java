/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.common;

import com.rationem.util.GenUtil;
import java.io.Serializable;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class MenuTreeBean implements Serializable {
  private static final Logger logger =
            Logger.getLogger("com.rationem.util.TreeBean");

  private TreeNode root;
  private TreeNode selectedNode;

  public MenuTreeBean() {
    root = new DefaultTreeNode("Root", null);
    TreeNode node0 = new DefaultTreeNode("Node 0", root);
    TreeNode node1 = new DefaultTreeNode("Node 1", root);
    TreeNode node2 = new DefaultTreeNode("Node 2", root);

    TreeNode node00 = new DefaultTreeNode( "Node 0.0", node0);
    TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);

    TreeNode node10 = new DefaultTreeNode("Node 1.0", node1);
    TreeNode node11 = new DefaultTreeNode("Node 1.1", node1);

    TreeNode node000 = new DefaultTreeNode("Node 0.0.0", node00);
    TreeNode node001 = new DefaultTreeNode("Node 0.0.1", node00);
    TreeNode node010 = new DefaultTreeNode("Node 0.1.0", node01);
    TreeNode node100 = new DefaultTreeNode("Node 1.0.0", node10);
logger.log(INFO,"Tree bean constructor called");


  }

  public TreeNode getRoot() {
    logger.log(INFO, "getRoot");
    return root;
  }

  public void setRoot(TreeNode root) {
    this.root = root;
  }

  public TreeNode getSelectedNode() {
    logger.log(INFO, "getSelectedNode");
    return selectedNode;
  }

  public void setSelectedNode(TreeNode selectedNode) {
    this.selectedNode = selectedNode;
  }

  public void onNodeSelect(NodeSelectEvent ev){
    GenUtil.addInfoMessage("Selectd "+ev.getTreeNode().getData().toString());
  }





}
