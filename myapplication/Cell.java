package com.example.myapplication;

import java.util.Random;

public class Cell
{
	int Energy;
	int Mineral;
	int Direction;
	private int Mind[]=new int [64];
	private int current;
	int x,y;
	int r,g,b;
	int age;
	Random rdm = new Random();
	int kills;
	int muts;

	Cell(int x,int y)
	{
		Energy=100;
		Mineral=0;
		Direction=rdm.nextInt()%8;
		for(int i=0;i<64;i++)
			Mind[i]=18;
		current=0;
		this.x=x;
		this.y=y;
		r=0;
		g=255;
		b=0;
		age=0;
		kills=0;
		muts=0;
	}
	Cell(int x,int y,int Mind[],int dir)
	{
		Direction=dir;
		this.Energy=100;
		this.Mineral=0;
		for(int i=0;i<64;i++)
			this.Mind[i]=Mind[i];
		this.current=0;
		this.x=x;
		this.y=y;
		r=255;
		g=255;
		b=255;
		age=0;
		kills=0;
		muts=0;
	}
	Cell(int x, int y, int energy, int mineral, int Mind[], int dir)
	{
		Direction=dir;
		this.Energy=energy;
		this.Mineral=mineral;
		for(int i=0;i<64;i++)
			this.Mind[i]=Mind[i];
		this.current=0;
		this.x=x;
		this.y=y;
		r=255;
		g=255;
		b=255;
		age=0;
		kills=0;
		muts=0;
	}
	Cell(int x, int y, int energy, int mineral, int Mind[], int dir,int r,int g,int b)
	{
		Direction=dir;
		this.Energy=energy;
		this.Mineral=mineral;
		for(int i=0;i<64;i++)
			this.Mind[i]=Mind[i];
		this.current=0;
		this.x=x;
		this.y=y;
		this.r=r;
		this.b=b;
		this.g=g;
		age=0;
		kills=0;
		muts=0;
	}
	int[] getFullMind()
	{
		return this.Mind;
	}
	void setFullMind(int []Mind)
	{
		for(int i=0;i<64;i++)
			this.Mind[i]=Mind[i];
	}
	int getCurMind()
	{
		current=(current+64)%64;
		return this.Mind[current];
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	void setOneMind(int i, int x)
	{
		if(x<0||i<0)
			return ;
		Mind[i]=x;
	}
	Cell clone(Cell c)
	{
		Cell c2 = new Cell(c.x,c.y,c.Energy,c.Mineral,c.getFullMind(),c.Direction,c.r,c.g,c.b);
		c2.age=c.age;
		c2.kills=c.kills;
		c2.muts = c.muts;
		return c2;
	}
}
