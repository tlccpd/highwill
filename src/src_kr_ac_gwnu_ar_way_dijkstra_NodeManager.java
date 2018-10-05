package kr.ac.gwnu.ar.way.dijkstra;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

public class NodeManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private int nodeStartId = 1;
	private HashMap<String, Node> nodeMap = new HashMap<String, Node>();

	public void addNode(String name, double x, double y) {
		Node node = new Node(nodeStartId++, name, x, y);

		nodeMap.put(name, node);
	}

	public Node getNode(String name) {
		return nodeMap.get(name);
	}

	public void addEdge(String name, String name1, int distance) {
		getNode(name).addEdge(getNode(name1), distance);
	}

	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();

		Iterator<String> iter = nodeMap.keySet().iterator();

		while (iter.hasNext()) {
			nodes.add(nodeMap.get(iter.next()));
		}

		return nodes;
	}

	public HashMap<String, Node> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(HashMap<String, Node> nodeMap) {
		this.nodeMap = nodeMap;
	}

	public List<String> getNodeNames() {
		List<String> lists = new ArrayList<String>();

		List<Node> n = getNodes();

		for (Node node : n) {
			lists.add(node.getName());
		}

		return lists;
	}

	public List<Edge> getEdges() {
		List<Edge> edgelist = new ArrayList<Edge>();

		List<Node> nodes = getNodes();

		for (Node node : nodes) {
			for (Edge e : node.getEdgeList()) {
				edgelist.add(e);
			}
		}

		return edgelist;
	}

	public Graph getGraph() {
		return new Graph(getNodes(), getEdges());
	}

	public LinkedList<Node> getPath(String start, String end) {
		Graph graph = getGraph();

		LinkedList<Node> path = DijkstraAlgorithm.calcPath(getNode(start),
				getNode(end), graph);

		return path;
	}

	public void getRemoteNodes(Context context,String url) throws Exception {
		URLConnection u = new URL(url).openConnection();

		InputStream is = u.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String str;

		FileOutputStream fos = context.openFileOutput("node", Context.MODE_PRIVATE);
		
		while ((str = br.readLine()) != null) {
			if ("".equals(str.trim()))
				continue;

			fos.write(str.getBytes());
			fos.write("\r\n".getBytes());
			
			String strs[] = str.split(",");

			String name = strs[0].trim();
			double x = Double.valueOf(strs[1].trim());
			double y = Double.valueOf(strs[2].trim());

			addNode(name, x, y);
		}
	}

	public void getRemoteEdges(Context context,String url) throws Exception {
		URLConnection u = new URL(url).openConnection();
		InputStream is = u.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String str;
		
		FileOutputStream fos = context.openFileOutput("edge", Context.MODE_PRIVATE);
		
		while ((str = br.readLine()) != null) {
			if ("".equals(str.trim()))
				continue;
			
			fos.write(str.getBytes());
			fos.write("\r\n".getBytes());
			
			String strs[] = str.split(",");
			
			String name = strs[0].trim();
			String name1 = strs[1].trim();

			int distance = Integer.valueOf(strs[2].trim());

			addEdge(name, name1, distance);
		}
		
		fos.close();
	}
	
	public void getLocalEdges(Context context) throws Exception{
		FileInputStream fis = context.openFileInput("edge");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String str;

		while ((str = br.readLine()) != null) {
			if ("".equals(str.trim()))
				continue;
			
			Log.e("sss",str+"");
			
			String strs[] = str.split(",");
			
			String name = strs[0].trim();
			String name1 = strs[1].trim();
			
			int distance = Integer.valueOf(strs[2].trim());

			addEdge(name, name1, distance);
		}
		
		fis.close();
	}
	
	public void getLocalNodes(Context context) throws Exception{
		FileInputStream fis = context.openFileInput("node");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String str;
		
		while ((str = br.readLine()) != null) {
			if ("".equals(str.trim()))
				continue;

			String strs[] = str.split(",");

			String name = strs[0].trim();
			double x = Double.valueOf(strs[1].trim());
			double y = Double.valueOf(strs[2].trim());

			addNode(name, x, y);
		}
		
		fis.close();
	}
}