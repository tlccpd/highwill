package kr.ac.gwnu.ar.way.dijkstra;

public class Graph {
	private List<Node> nodes;
	private List<Edge> edges;

	public Graph(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		
		addGraphToEdges();
	}

	public Graph(Node[] nodes, Edge[] edges) {
		List<Node> nodes_list = new ArrayList<Node>();

		for (int i = 0; i < nodes.length; i++) {
			nodes_list.add(nodes[i]);
		}

		this.nodes = nodes_list;

		List<Edge> edges_list = new ArrayList<Edge>();
		
		for (int i = 0; i < edges.length; i++) {
			edges_list.add(edges[i]);
		}

		this.edges = edges_list;

		addGraphToEdges();
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public Node getNode(int id) {
		Iterator<Node> nodes_iterator = this.nodes.iterator();
		Node the_node = null;
		while (nodes_iterator.hasNext()) {
			Node node = nodes_iterator.next();
			if (node.getId() == id) {
				the_node = node;
			}
		}
		return the_node;
	}

	public List<Edge> getEdges() {
		return this.edges;
	}

	public void addGraphToEdges() {
		Iterator<Edge> edges_iterator = this.edges.iterator();
		
		while (edges_iterator.hasNext()) {
			Edge edge = edges_iterator.next();
			edge.setGraph(this);
		}
		
	}
}
