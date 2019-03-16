package kr.ac.gwnu.ar.way.dijkstra;

public class DijkstraAlgorithm {

	private final List<Edge> edges;
	private Set<Node> settledNodes;
	private Set<Node> unSettledNodes;
	private Map<Node, Node> predecessors;
	private Map<Node, Integer> distance;

	public static LinkedList<Node> calcPath(Node source, Node destination,Graph graph) {
		DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);

		algorithm.execute(source);
		
		return algorithm.getPath(destination);
	}

	public DijkstraAlgorithm(Graph graph) {
		new ArrayList<Node>(graph.getNodes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(Node source) {
		settledNodes = new HashSet<Node>();
		unSettledNodes = new HashSet<Node>();
		distance = new HashMap<Node, Integer>();
		predecessors = new HashMap<Node, Node>();
		
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Node node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Node node) {
		List<Node> adjacentNodes = getNeighbors(node);
		
		for (Node target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)+ getDistance(node, target)) {
				distance.put(target,getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
		
		
	}

	private int getDistance(Node node, Node target) {
		for (Edge edge : edges) {
			if ((edge.getA().equals(node) || edge.getB().equals(node))&& (edge.getA().equals(target) || edge.getB().equals(target))) {
				return edge.getDistance();
			}
		}
		
		throw new RuntimeException("Should not happen");
	}

	private List<Node> getNeighbors(Node node) {
		List<Node> neighbors = new ArrayList<Node>();
		
		for (Edge edge : edges) {
			if (edge.getA().equals(node) && !isSettled(edge.getB())) {
				neighbors.add(edge.getB());
				
			} else if (edge.getB().equals(node) && !isSettled(edge.getA())) {
				
				neighbors.add(edge.getA());
			}
		}
		
		return neighbors;
	}

	private Node getMinimum(Set<Node> vertexes) {
		Node minimum = null;
		for (Node vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Node vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Node destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	public LinkedList<Node> getPath(Node target) {
		LinkedList<Node> path = new LinkedList<Node>();
		Node step = target;
		
		if (predecessors.get(step) == null) {
			return null;
		}
		
		path.add(step);
		
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		
		Collections.reverse(path);
		return path;
	}
}
