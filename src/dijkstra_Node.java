package kr.ac.gwnu.ar.way.dijkstra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Node implements Serializable{
	private static int idCounter = 0;

	private String name;
	private int id;
	private double position_x = 0;
	private double position_y = 0;
	private List<Edge> edgeList = new ArrayList<Edge>();

	public Node(String name) {
		this.id = idCounter++;
		this.name = name;
	}

	public Node(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Node(int id, String name, double x, double y) {
		this.id = id;
		this.name = name;
		this.position_x = x;
		this.position_y = y;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public double getPosition_y() {
		return position_y;
	}

	public void setPosition_y(int position_y) {
		this.position_y = position_y;
	}

	public double getPosition_x() {
		return position_x;
	}

	public void setPosition_x(int position_x) {
		this.position_x = position_x;
	}

	public void addEdge(Node b_node, int distance) {
		edgeList.add(new Edge(this, b_node, distance));
	}

	public List<Edge> getEdgeList() {
		return edgeList;
	}

	public void setEdgeList(List<Edge> edgeList) {
		this.edgeList = edgeList;
	}

	@Override
	public String toString() {
		return id + "," + name + "," + position_x + "," + position_y;
	}
}