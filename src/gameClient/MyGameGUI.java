package gameClient;

import java.awt.Color;
import java.util.Iterator;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.StdDraw;

public class MyGameGUI implements Runnable  
{
	private game_service game;
	private DGraph g;
	private int mc;
	public MyGameGUI(game_service game1) 
	{
		this.game=game1;
		g = new DGraph();
		g.init(game1.getGraph());
		this.mc=g.getMC();
		Thread change=new Thread(this);
		change.start();
		this.paint();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() 
	{
		while(game.isRunning())
		{
				synchronized(this) 
				{
					if(mc!=g.getMC())
					{
						mc=g.getMC();
						this.paint();
					}
				}
				try
				{
					Thread.sleep(500);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
		}
		
	}
	private void paint() 
	{	

		double maxX=Double.NEGATIVE_INFINITY;
		double maxY=Double.NEGATIVE_INFINITY;
		double minX=Double.POSITIVE_INFINITY;
		double minY=Double.POSITIVE_INFINITY;
		for(Iterator<node_data> verIter=g.getV().iterator();verIter.hasNext();)
		{
			node_data point=verIter.next();
			if(point.getLocation().x()>maxX)
				maxX=point.getLocation().x();
			if(point.getLocation().y()>maxY)
				maxY=point.getLocation().y();
			if(point.getLocation().x()<minX)
				minX=point.getLocation().x();
			if(point.getLocation().y()<minY)
				minY=point.getLocation().y();	
		} 
		double epsilon=0.0025;
		StdDraw.setCanvasSize(600,600);
		StdDraw.setXscale(minX-epsilon,maxX+epsilon);
		StdDraw.setYscale(minY-epsilon,epsilon+maxY);

		for(Iterator<node_data> verIter=g.getV().iterator();verIter.hasNext();) 
		{
			node_data point=verIter.next();
			StdDraw.setPenColor(Color.BLUE);
			StdDraw.setPenRadius(0.020);
			StdDraw.point(point.getLocation().x(),point.getLocation().y());
			StdDraw.text(point.getLocation().x(),point.getLocation().y()+1, (""+point.getKey()));
			
			try {//in case point does not have edge the function getE return exception, and we do not want exception we just do not want it to paint
				for(Iterator<edge_data> edgeIter=g.getE(point.getKey()).iterator();edgeIter.hasNext();) 
				{
					edge_data line=edgeIter.next();
					node_data dest=g.getNode(line.getDest());
					node_data src=point;
					StdDraw.setPenColor(Color.RED);
					StdDraw.setPenRadius(0.005);
					StdDraw.line(src.getLocation().x(),src.getLocation().y(), dest.getLocation().x(),dest.getLocation().y());
					StdDraw.text(((src.getLocation().x()+dest.getLocation().x())/2),((src.getLocation().y()+dest.getLocation().y())/2),(""+line.getWeight()));
					
					StdDraw.setPenColor(Color.YELLOW);
					StdDraw.setPenRadius(0.015);
					double x=Math.abs(src.getLocation().x()-dest.getLocation().x())/10.0;
					double y=Math.abs(src.getLocation().y()-dest.getLocation().y())/10.0;
					
					if(src.getLocation().x()<dest.getLocation().x()) {
						
						if(src.getLocation().y()<dest.getLocation().y())
							StdDraw.point((int)dest.getLocation().x()-x,(int)dest.getLocation().y()-y);
						else
							StdDraw.point((int)dest.getLocation().x()-x,(int)dest.getLocation().y()+y);
					}
					else {
						if(src.getLocation().y()<dest.getLocation().y())
							StdDraw.point((int)dest.getLocation().x()+x,(int)dest.getLocation().y()-y);
						else
							StdDraw.point((int)dest.getLocation().x()+x,(int)dest.getLocation().y()+y);
					}
					
				}
			}
			catch (Exception e) {}
		}
	}

}
