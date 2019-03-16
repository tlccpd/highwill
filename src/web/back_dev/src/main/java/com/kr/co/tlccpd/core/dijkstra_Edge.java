package kr.ac.gwnu.ar.way.dijkstra;


public class Edge implements Serializable{

	private static int idCounter = 0;
	private Node a, b;
	private int a_id, b_id;
	private int distance = 0;
	private int id;
	private Graph graph;

	public Edge(int a_id, int b_id, int distance) {
		this.id = idCounter++;
		this.distance = distance;
		this.a_id = a_id;
		this.b_id = a_id;
	}

	public Edge(Node a, Node b, int distance) {
		this.id = idCounter++;
		this.a = a;
		this.b = b;
		this.distance = distance;
		this.a_id = a.getId();
		this.b_id = b.getId();
	}

	public Edge(int id, Node a, Node b, int distance) {
		this.id = idCounter++;
		this.a = a;
		this.b = b;
		this.distance = distance;
		this.a_id = a.getId();
		this.b_id = b.getId();
	}

	public Node getA() {
		if (this.a == null) {
			this.a = this.graph.getNode(this.a_id);
		}
		return this.a;
	}

	public Node getB() {
		if (this.b == null) {
			this.b = this.graph.getNode(this.b_id);
		}
		return this.b;
	}

	public int getDistance() {
		return this.distance;
	}

	public int getId() {
		return this.id;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	@Override
	public String toString() {
		return id + "," + a + "," + b + "," + distance;
		//return super.toString();
	}
}