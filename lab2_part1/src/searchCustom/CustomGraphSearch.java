package searchCustom;

import java.util.ArrayList;
import java.util.HashSet;

import searchShared.NodeQueue;
import searchShared.Problem;
import searchShared.SearchObject;
import searchShared.SearchNode;

import world.GridPos;

public class CustomGraphSearch implements SearchObject {

	private HashSet<SearchNode> explored;
	private NodeQueue frontier;
	protected ArrayList<SearchNode> path;
	private boolean insertFront;

	/**
	 * The constructor tells graph search whether it should insert nodes to front or back of the frontier 
	 */
    public CustomGraphSearch(boolean bInsertFront) {
		insertFront = bInsertFront;
    }

	/**
	 * Implements "graph search", which is the foundation of many search algorithms
	 */
    public ArrayList<SearchNode> search(Problem p) {
        frontier = new NodeQueue();
        explored = new HashSet<SearchNode>();
        GridPos startState = (GridPos) p.getInitialState();
        frontier.addNodeToFront(new SearchNode(startState));
        path = new ArrayList<SearchNode>();

        while (!frontier.isEmpty()) {
            // Remove the node from the front of the frontier
            SearchNode currentNode = frontier.removeFirst();

            // Mark the node as explored
            explored.add(currentNode);

            // Get child states of the current node
            ArrayList<GridPos> childStates = p.getReachableStatesFrom((GridPos) currentNode.getState());

            for (GridPos childState : childStates) {
                SearchNode childNode = new SearchNode(childState, currentNode);

 				// If this node's state is the goal state, build the path and return it
				if (p.isGoalState(childNode.getState())) {
					path = childNode.getPathFromRoot();
					return path;
				}

                // If the child node is not in the frontier and not explored, add it to the frontier
                if (!explored.contains(childNode) && !frontier.contains(childNode)) {
                    if (insertFront) {
                        frontier.addNodeToFront(childNode); // For depth-first
                    } else {
                        frontier.addNodeToBack(childNode);  // For breadth-first
                    }
                }
            }
        }

        return path; // Returning an empty path means no path exists
    }


	/*
	 * Functions below are just getters used externally by the program 
	 */
	public ArrayList<SearchNode> getPath() {
		return path;
	}

	public ArrayList<SearchNode> getFrontierNodes() {
		return new ArrayList<SearchNode>(frontier.toList());
	}
	public ArrayList<SearchNode> getExploredNodes() {
		return new ArrayList<SearchNode>(explored);
	}
	public ArrayList<SearchNode> getAllExpandedNodes() {
		ArrayList<SearchNode> allNodes = new ArrayList<SearchNode>();
		allNodes.addAll(getFrontierNodes());
		allNodes.addAll(getExploredNodes());
		return allNodes;
	}

}
