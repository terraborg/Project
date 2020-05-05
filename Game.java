package com.example.myapplication;

import java.util.ArrayList;
import java.util.Random;

class Game implements Resetable
{
	private ArrayList<Cell> Cells;

	private Map Map;

	private int id;

	private Random random=new Random();

	Game(int mapHeight,int mapWidth)
	{
		Map=new Map(mapHeight,mapWidth);
		Cells=new ArrayList<>(0);
		id=0;
	}
	void putCell(int x,int y,int mode)
	{
		if(Map.getCell(x,y)!=-1)
			return;
		Cell c=new Cell(x,y,mode,id);
		Cells.add(c);
		Map.putCell(x,y,Cells.size());
		id++;
	}
	void putCell(Cell cell)
	{
		if(Map.getCell(cell.getX(),cell.getY())!=-1)
			return;
		Cells.add(cell);
		Map.putCell(cell.getX(),cell.getY(),Cells.size());
		id++;
	}
	void deleteCell(int number)
	{
		Map.putCell(Cells.get(number).getX(), Cells.get(number).getY(),-1);
		Cells.remove(number);
		for(int i=0;i<Cells.size();i++)
			Map.putCell(Cells.get(i).getX(),Cells.get(i).getY(),i);
	}
	public void reset()
	{
		Map.reset();
		Cells.clear();
		id=0;
	}

	Cell getCell(int num)
	{
		if(num<0)
			return null;
		return Cells.get(num);
	}

	int getCellOnMap(int x,int y){
		return Map.getCell(x,y);
	}

	int getCellsSize()
	{
		return Cells.size();
	}

	void eat(int number1,int number2)
	{
		Cells.get(number1).setEnergy(Cells.get(number1).getEnergy()+Cells.get(number2).getEnergy()/15);
		Cells.get(number1).setMineral(Cells.get(number1).getMineral()+Cells.get(number2).getMineral()/15);
		deleteCell(number2);
	}

	void mitoz(int number)
	{
		if(checkCell(Cells.get(number))>0)
		{
			Cells.get(number).incDirection(1);
			return;
		}
		Cell c=Cells.get(number);
		Cell cell = new Cell(c.getX(),c.getY(),c.getEnergy()/2,c.getMineral()/2,c.getFullMind(),c.getDir(),c.getR(),c.getG(),c.getB(),id);
		cell.move();
		cell.setMuts(c.getMuts());
		if(random.nextInt()%5==3)
		{
			cell.setOneMind(random.nextInt()%64,random.nextInt()%128);
			cell.setMuts(c.getMuts()+1);
		}
		putCell(cell);
	}
	private void move(Cell cell)
	{
		if(Map.getCell(cell.getDirection().second,cell.getDirection().first)==-1)
		{
			Map.move(cell);
			cell.move();
		}
	}
	private int checkCell(Cell cell)
	{
		int x=cell.getDirection().first,y=cell.getDirection().second;
		if(x>=Map.width||y>=Map.height||x<0||y<0)
			return -1;
		if(Map.getCell(x,y)==-1)
			return -2;
		return Map.getCell(x,y);
	}
	void tick() {
		for (int i = 0; i < Cells.size(); i++) {
			if (Cells.get(i).getEnergy() <= 0) {
					deleteCell(i);
					i--;
					continue;
				}
			Cell current=Cells.get(i);


			int j = 0;
			while (j < 10)
			{
				current.setCurrent(current.getCurrent() % 64);
				int cur = current.getCurMind();
				if (cur < 8) //set direction
				{
					current.incDirection(cur);
					current.setCurrent(current.getCurrent() + 1);
					continue;
				}
				if (cur >= 8 && cur < 16) //move
				{
					current.setCurrent(current.getCurrent() + cur % 8 + 1);
					if (checkCell(current) == -2)
						move(current);
					break;
				}
				if (cur == 16) //check energy
				{
					int e = current.getEnergy() / 250;
					current.setCurrent(current.getCurrent() + e + 1);
					j++;
					continue;
				}
				if (cur == 17) //check minerals
				{
					int m = current.getMineral() / 250;
					current.setCurrent(current.getCurrent() + m + 1);
					j++;
					continue;
				}
				if (cur >= 18 && cur < 26) //fotosintez
				{
					current.setEnergy(current.getEnergy()+Map.getEnergy(current.getX(), current.getY()));
					current.setCurrent(current.getCurrent() + cur % 8 + 1);
					current.ChangeGreen(Map.getEnergy(current.getX(), current.getY()));
					break;
				}
				if (cur == 34) //check cell
				{
					if (checkCell(current) >= 0)
						current.setCurrent(current.getCurrent() + 1);
				}
				if (cur >= 35 && cur < 43) //kill cell
				{
					int num = checkCell(current);
					current.setCurrent(current.getCurrent() + 1);
					if (num >= 0&&num<Cells.size()) {
						current.ChangeRed(50);
						current.ChangeGreen(-10);
						current.incKills();
						current.setEnergy(current.getEnergy()-5);
						eat(i, num);
					}
					break;
				}
				if (cur >= 43 && cur < 51) //recycle minerals
				{
					current.setEnergy(current.getEnergy()+current.getMineral() * 4);
					current.ChangeBlue(current.getMineral() * 4);
					current.setMineral(0);
					current.setCurrent(current.getCurrent() + 1);
					break;
				}
				if (cur == 51) //mitoz
				{
					mitoz(i);
					current.setCurrent(current.getCurrent() + 1);
					break;
				}
				if (cur > 51)
					current.setCurrent(current.getCurrent() + cur);

				j++;
				current.setCurrent(current.getCurrent() + 1);
			}
			if(i>=Cells.size())
				break;
			if (current.getEnergy() >= 900) {
				mitoz(i);
			}
			if(current.getEnergy()>50)
				current.setMineral(current.getMineral()+Map.getMineral(current.getX(), current.getY()));
			current.tickEnergy();
			current.incAge();
			current.ChangeRed(-2);
			current.ChangeGreen(-5);
			current.ChangeBlue(-2);
			if (current.getEnergy() <= 0) {
					deleteCell(i);
					i--;
			}
		}
	}
}
