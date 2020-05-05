package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;

import java.util.Random;

public class Cell
{
	private int energy;
	private int mineral;
	private int Direction;
	private int Mind[]=new int [64];
	private int current;
	private int x,y;
	private int r,g,b;
	private int age;
	private Random rdm = new Random();
	private int kills;
	private int muts;
	private int id;

	Cell(int x,int y,int mode,int id)
	{
		energy=100;
		mineral=0;
		Direction=rdm.nextInt()%8;
		for(int i=0;i<64;i++)
			if(mode==1)
				Mind[i]=18;
			else if(mode==2)
				Mind[i]=abs(rdm.nextInt()%65);
			else if(mode==3)
				Mind[i]=43;
		current=0;
		this.x=x;
		this.y=y;
		r=255;
		g=255;
		b=255;
		age=0;
		kills=0;
		muts=0;
		this.id=id;
	}
	Cell(int x, int y, int energy, int mineral, int Mind[], int dir,int id)
	{
		Direction=dir;
		this.energy=energy;
		this.mineral=mineral;
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
		this.id=id;
	}
	Cell(int x, int y, int energy, int mineral, int Mind[], int dir,int r,int g,int b,int id)
	{
		Direction=dir;
		this.energy=energy;
		this.mineral=mineral;
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
		this.id=id;
	}
	int[] getFullMind()
	{
		return this.Mind;
	}

	int getCurMind()
	{
		current=(current+64)%64;
		return this.Mind[current];
	}
	
	void incKills()
	{
		kills++;
	}
	
	int getKills()
	{
		return kills;
	}

	int getAge()
	{
		return age;
	}

	int getR()
	{
		return r;
	}
	int getB()
	{
		return b;
	}
	int getG()
	{
		return g;
	}

	void setId(int id)
	{
		this.id=id;
	}

	int getDir()
	{
		return this.Direction;
	}

	Pair<Integer, Integer> getDirection()
	{
		int x=this.x,y=this.y;
		switch(this.Direction)
		{
			case 0:
				y++;
				break;
			case 1:
				y++;
				x++;
				break;
			case 2:
				x++;
				break;
			case 3:
				y--;
				x++;
				break;
			case 4:
				y--;
				break;
			case 5:
				y--;
				x--;
				break;
			case 6:
				x--;
				break;
			case 7:
				x--;
				y++;
				break;
		}
		return new Pair<>(x,y);
	}

	void move()
	{
		x=getDirection().first;
		y=getDirection().second;
	}

	int getX()
	{
		return x;
	}

	int getY()
	{
		return y;
	}

	int getId()
	{
		return id;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
		if(this.energy>=1000)
			this.energy=1000;
	}

	public int getMineral() {
		return mineral;
	}

	public void setMineral(int mineral) {
		this.mineral = mineral;
		if(this.mineral>=1000)
			this.mineral=1000;
	}

	public int getMuts() {
		return muts;
	}

	public void setMuts(int muts) {
		this.muts = muts;
	}

	void ChangeRed(int delta)
	{
		r+=delta;
		if(r>255)
			r=255;
		if(r<0)
			r=0;
	}

	void ChangeBlue(int delta)
	{
		b+=delta;
		if(b>255)
			b=255;
		if(b<0)
			b=0;
	}

	void ChangeGreen(int delta)
	{
		g+=delta;
		if(g>255)
			g=255;
		if(g<0)
			g=0;
	}
	
	void tickEnergy()
	{
		setEnergy(energy-1-age/20);
	}
	
	void incAge()
	{
		age++;
	}

	void incDirection(int value)
	{
		Direction=(Direction+value)%8;
	}

	public int getColor()
	{
		return Color.argb(255,r,g,b);
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	int abs(int n)
	{
		if(n<0)
			return -n;
		return n;
	}

	void setOneMind(int i, int x)
	{
		if(x<0||i<0)
			return ;
		Mind[i]=x;
	}
	static Cell clone(Cell c)
	{
		Cell c2 = new Cell(c.x,c.y,c.energy,c.mineral,c.getFullMind(),c.Direction,c.r,c.g,c.b,c.id);
		c2.age=c.age;
		c2.kills=c.kills;
		c2.muts = c.muts;
		return c2;
	}
	void drawCell(Canvas canvas, Rect rect)
	{
		Paint paint=new Paint();
		paint.setColor(getColor());
		canvas.drawRect(x*10+rect.left, y*10+rect.top, x*10+10+rect.left, y*10+10+rect.top,paint);
	}
	void drawCell(Canvas canvas, Rect rect, Paint paint)
	{
		canvas.drawRect(x*10+rect.left, y*10+rect.top, x*10+10+rect.left, y*10+10+rect.top,paint);
	}

}
