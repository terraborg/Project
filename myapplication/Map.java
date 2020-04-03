package com.example.myapplication;

public class Map
{
	int height,width;
	private int[][] EMap;
	private int[][] MMap;
	private int[][] CMap;
	Map(int height,int width)
	{
		this.height=height/10;
		this.width=width/10;
		EMap = new int[this.height][this.width];
		MMap = new int[this.height][this.width];
		CMap = new int[this.height][this.width];
		int step=this.height/20;
		int mstep=this.height/10;
		for(int i=0;i<this.width;i++)
			for(int j=0;j<this.height;j++)
			{
				if(j>=step*8)
				{
					EMap[j][i]=(j/step)-2;
				}
				else
				{
					EMap[j][i]=0;
				}
				if(j<=mstep*4)
				{
					MMap[j][i]=5-j/mstep;
				}
				else
				{
					MMap[j][i]=0;
				}
				CMap[j][i]=-1;
			}
	}
	int getEnergy(int x,int y)
	{
		if(x>=width||y>=height||x<0||y<0)
			return -2;
		return EMap[y][x];
	}
	int getMineral(int x,int y)
	{
		if(x>=width||y>=height||x<0||y<0)
			return -2;
		return MMap[y][x];
	}
	int getCell(int x,int y)
	{
		if(x>=width||y>=height||x<0||y<0)
			return -2;
		return CMap[y][x];
	}
	void putCell(int x,int y,int number)
	{
		if(x>=width||y>=height||x<0||y<0)
			return ;
		CMap[y][x]=number;
	}
	void move(int x, int y, int dir)
	{
		switch(dir)
		{
			case 0:
				CMap[y+1][x]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 1:
				CMap[y+1][x+1]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 2:
				CMap[y][x+1]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 3:
				CMap[y-1][x+1]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 4:
				CMap[y-1][x]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 5:
				CMap[y-1][x-1]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 6:
				CMap[y][x-1]=CMap[y][x];
				CMap[y][x]=-1;
				break;
			case 7:
				CMap[y+1][x-1]=CMap[y][x];
				CMap[y][x]=-1;
				break;
		}
	}
}
