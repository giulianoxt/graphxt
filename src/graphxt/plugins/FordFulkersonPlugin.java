package graphxt.plugins;

import java.util.*;
import graphxt.gui.*;
import graphxt.view.*;
import graphxt.model.*;
import java.awt.event.*;
import javax.swing.Timer;

public class FordFulkersonPlugin extends BasePlugin implements ActionListener {
  public static final int DFS_PATH = 0;
  public static final int BFS_PATH = 1;

  public FordFulkersonPlugin(
          int search,
          Vertex<String> s, Vertex<String> t,
          Graph<String,Integer> graph, GraphView graph_view,
          GraphWindow window, Timer timer) {
      super(null, graph, graph_view, window, timer);
      source = s;
      destiny = t;
      searchType = search;
  }

  public void setInitialVertex(Vertex<String> cur) {
    flow = new HashMap<Vertex<String>,Map<Vertex<String>,Integer>>();
    for (Vertex<String> v1 : graph.getVertexSet()) {
      Map<Vertex<String>,Integer> line = new HashMap<Vertex<String>,Integer>();
      for (Vertex<String> v2 : graph.getVertexSet())
        line.put(v2, 0);

      flow.put(v1, line);
    }

    capacity = new HashMap<Vertex<String>,Map<Vertex<String>,Integer>>();
    for (Vertex<String> v1 : graph.getVertexSet()) {
      Map<Vertex<String>,Integer> line = new HashMap<Vertex<String>,Integer>();
      for (Vertex<String> v2 : graph.getVertexSet())
        line.put(v2, getCost(v1, v2));

      capacity.put(v1, line);
    }

    graph_view.deselectAll();
    graph_view.repaint();
    window.postMessage("FordFulkerson initialized");
  }

  private int getFlow(Vertex<String> v1, Vertex<String> v2) {
    return flow.get(v1).get(v2);
  }

  private void setFlow(Vertex<String> v1, Vertex<String> v2, int f) {
    flow.get(v1).put(v2, f);
  }

  private int getCapacity(Vertex<String> v1, Vertex<String> v2) {
    return capacity.get(v1).get(v2);
  }

  private int getCost(Vertex<String> v1, Vertex<String> v2) {
    for (Edge<String,Integer> e : graph.getConnectedEdges(v1))
      if (e.getEnd().equals(v2))
        return e.getData();

    return 0;
  }

  private boolean search(Vertex<String> source, Vertex<String> dest) {
    switch (searchType) {
      case DFS_PATH:
        return dfs(source, dest);
      case BFS_PATH:
        return bfs(source, dest);
    }
    return false;
  }

  private boolean bfs(Vertex<String> source, Vertex<String> dest) {
    Queue<Vertex<String>> queue = new ArrayDeque<Vertex<String>>();
    Set<Vertex<String>> visited = new HashSet<Vertex<String>>();
    parent = new HashMap<Vertex<String>,Vertex<String>>();

    queue.add(source);
    visited.add(source);
    parent.put(source, null);

    while (!queue.isEmpty()) {
      Vertex<String> u = queue.remove();

      for (Vertex<String> v : graph.getVertexSet()) {
        if (visited.contains(v))
          continue;

        int f_uv = getFlow(u, v);
        int c_uv = getCapacity(u, v);

        if (f_uv < c_uv) {
          visited.add(v);
          parent.put(v, u);
          queue.add(v);

          if (v.equals(dest))
            return true;
        }
      }
    }

    return false;
  }

  private boolean dfs(Vertex<String> source, Vertex<String> dest) {
    Stack<Vertex<String>> stack = new Stack<Vertex<String>>();
    Set<Vertex<String>> visited = new HashSet<Vertex<String>>();
    parent = new HashMap<Vertex<String>,Vertex<String>>();

    stack.push(source);
    visited.add(source);
    parent.put(source, null);

    while (!stack.isEmpty()) {
      Vertex<String> u = stack.pop();

      for (Vertex<String> v : graph.getVertexSet()) {
        if (visited.contains(v))
          continue;

        int f_uv = getFlow(u, v);
        int c_uv = getCapacity(u, v);

        if (f_uv < c_uv) {
          visited.add(v);
          parent.put(v, u);
          stack.push(v);

          if (v.equals(dest))
            return true;
        }
      }
    }

    return false;
  }

  private EdgeView<String,Integer> getEdgeView(Vertex<String> a, Vertex<String> b) {
    for (EdgeView<String,Integer> e : graph_view.getEdgeViewSet()) {
      if (!(e.getEdge().contains(a) && e.getEdge().contains(b)))
        continue;
      return e;
    }
    return null;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    graph_view.deselectAll();
    
    if (!search(source, destiny)) {
      window.postMessage("FordFulkerson finished.");
      graph_view.repaint();
      stop();
      return;
    }

    for (EdgeView<String,Integer> ev : graph_view.getEdgeViewSet())
      ev.setEdgeInfo(null);

    Vertex<String> v = destiny;
    int max_flow = Integer.MAX_VALUE;
    Vertex<String> _parent = parent.get(v);
    
    while (_parent != null) {
      int c = getCost(_parent, v);
      int f = getFlow(_parent, v);
     
      max_flow = Math.min(max_flow, c - f);

      getEdgeView(_parent, v).setSelected(true);
      graph_view.getVertexView(v).setSelected(true);
      graph_view.getVertexView(_parent).setSelected(true);

      v = _parent;
      _parent = parent.get(v);
    }

    window.postMessage("Augmenting flow of " + max_flow);

    v = destiny;
    _parent = parent.get(v);

    while (_parent != null) {
      int new_flow = getFlow(_parent, v) + max_flow;

      setFlow(_parent, v, new_flow);
      setFlow(v, _parent, -new_flow);

      v = _parent;
      _parent = parent.get(v);
    }
    
    for (EdgeView<String,Integer> ev : graph_view.getEdgeViewSet()) {
      Vertex<String> a = ev.getEdge().getStart();
      Vertex<String> b = ev.getEdge().getEnd();
      int f = getFlow(a, b);
      int c = getCapacity(a, b);
      ev.setEdgeInfo("[" + f + " / " + c + "]");
    }

    graph_view.repaint();
  }

  private int searchType;
  private Vertex<String> source, destiny;

  private Map<Vertex<String>,Vertex<String>> parent;
  private Map<Vertex<String>,Map<Vertex<String>,Integer>> flow, capacity;
}
