package com.example.myapplication;

import java.util.ArrayList;
import java.util.Random;

class Game
{
	ArrayList<Cell> Cells;

	Map Map;

	int CellsSize;

	private Random random=new Random();

	Game(int mapHeight,int mapWidth)
	{
		Map=new Map(mapHeight,mapWidth);
		Cells=new ArrayList<>(0);
		CellsSize=0;
	}
	void putCell(int x,int y,int energy,int mineral,int Mind[],int dir)
	{
		if(Map.getCell(x,y)!=-1)
			return;
		Cell c=new Cell(x,y,energy,mineral,Mind,dir);
		Cells.add(c);
		Map.putCell(x,y,CellsSize);
		CellsSize++;

	}
	void putCell(int x,int y,int energy,int mineral,int Mind[],int dir,int r,int g,int b,int muts)
	{
		if(Map.getCell(x,y)!=-1)
			return;
		Cell c=new Cell(x,y,energy,mineral,Mind,dir,r,g,b);
		c.muts = muts;
		Cells.add(c);
		Map.putCell(x,y,CellsSize);
		CellsSize++;
	}
	void putCell(int x,int y,int Mind[])
	{
		if(Map.getCell(x,y)!=-1)
			return;
		Cell c=new Cell(x,y,Mind,0);
		Cells.add(c);
		Map.putCell(x,y,CellsSize);
		CellsSize++;
	}
	void putCell(int x,int y)
	{
		if(Map.getCell(x,y)!=-1)
			return;
		Cell c=new Cell(x,y);
		Cells.add(c);
		Map.putCell(x,y,CellsSize);
		CellsSize++;
	}
	private void equal(Cell c1,Cell c2)
	{
		c1.Mineral=c2.Mineral;
		c1.Energy=c2.Energy;
		c1.Direction=c2.Direction;
		c1.x=c2.x;
		c1.y=c2.y;
		c1.setFullMind(c2.getFullMind());
		c1.setCurrent(c2.getCurrent());
	}
	void deleteCell(int number)
	{
		Map.putCell(Cells.get(number).x, Cells.get(number).y,-1);
		Cells.remove(number);
		for(int i=0;i<Cells.size();i++)
			Map.putCell(Cells.get(i).x,Cells.get(i).y,i);
		CellsSize--;
	}
	void eat(int number1,int number2)
	{
		Cells.get(number1).Energy+=Cells.get(number2).Energy/10;
		Cells.get(number1).Mineral+=Cells.get(number2).Mineral/10;
		deleteCell(number2);
	}
	void mitoz(int number)
	{
		if(checkCell(Cells.get(number))>0)
		{
			Cells.get(number).Direction=(Cells.get(number).Direction+1)%8;
			return;
		}
		Cell cell= Cells.get(number);
		Cell cell2=new Cell(cell.x,cell.y,cell.Energy/2-50,cell.Mineral/2,cell.getFullMind(),cell.Direction);
		switch(cell.Direction)
		{
			case 0:
				cell2.y++;
				break;
			case 1:
				cell2.y++;
				cell2.x++;
				break;
			case 2:
				cell2.x++;
				break;
			case 3:
				cell2.y--;
				cell2.x++;
				break;
			case 4:
				cell2.y--;
				break;
			case 5:
				cell2.y--;
				cell2.x--;
				break;
			case 6:
				cell2.x--;
				break;
			case 7:
				cell2.x--;
				cell2.y++;
				break;
		}
		cell2.muts=cell.muts;
		if(random.nextInt()%10==3)
		{
			cell2.setOneMind(random.nextInt()%64,random.nextInt()%128);
			cell2.muts=cell.muts+1;
		}
		putCell(cell2.x,cell2.y,cell2.Energy,cell2.Mineral,cell2.getFullMind(),cell2.Direction,cell.r,cell.g,cell.b,cell2.muts);
	}
	private void move(Cell cell)
	{
		switch(cell.Direction)
		{
			case 0:
				if(Map.getCell(cell.x,cell.y+1)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.y++;
				}
				break;
			case 1:
				if(Map.getCell(cell.x-1,cell.y+1)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.y++;
					cell.x++;
				}
				break;
			case 2:
				if(Map.getCell(cell.x+1,cell.y)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.x++;
				}

				break;
			case 3:
				if(Map.getCell(cell.x+1,cell.y-1)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.x++;
					cell.y--;
				}

				break;
			case 4:
				if(Map.getCell(cell.x,cell.y-1)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.y--;
				}
				break;
			case 5:
				if(Map.getCell(cell.x-1,cell.y-1)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.y--;
					cell.x--;
				}

				break;
			case 6:
				if(Map.getCell(cell.x-1,cell.y)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.x--;
				}
				break;
			case 7:
				if(Map.getCell(cell.x-1,cell.y+1)==-1)
				{
					Map.move(cell.x,cell.y,cell.Direction);
					cell.y++;
					cell.x--;
				}
				break;
		}
	}
	private int checkCell(Cell cell)
	{
		int x=cell.x,y=cell.y;
		switch(cell.Direction)
		{
			case 0:
				y=y+1;
				break;
			case 1:
				y=y+1;
				x=x+1;
				break;
			case 2:
				x=x+1;
				break;
			case 3:
				x++;
				y--;
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
		if(Map.getCell(x,y)==-1)
			return 0;
		else if(x>=Map.width||y>=Map.height||x<0||y<0)
			return -1;
		return Map.getCell(x,y);
	}
	public void tick() {
		for (int i = 0; i < Cells.size(); i++) {
			if (Cells.get(i).Energy <= 0) {
					deleteCell(i);
					i--;
					continue;
				}

			int j = 0;
			while (j < 10)
			{
				Cells.get(i).setCurrent(Cells.get(i).getCurrent() % 64);
				int cur = Cells.get(i).getCurMind();
				if (cur < 8) //set direction
				{
					Cells.get(i).Direction = cur;
					j++;
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + 1);
					continue;
				}
				if (cur >= 8 && cur < 16) //move
				{
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + cur % 8 + 1);
					if (checkCell(Cells.get(i)) != 0)
						break;
					move(Cells.get(i));
					break;
				}
				if (cur == 16) //check energy
				{
					int e = Cells.get(i).Energy / 250;
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + e + 1);
					j++;
					continue;
				}
				if (cur == 17) //check minerals
				{
					int m = Cells.get(i).Mineral / 250;
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + m + 1);
					j++;
					continue;
				}
				if (cur >= 18 && cur < 26) //fotosintez
				{
					Cells.get(i).Energy += Map.getEnergy(Cells.get(i).x, Cells.get(i).y);
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + cur % 8 + 1);
					Cells.get(i).g += Map.getEnergy(Cells.get(i).x, Cells.get(i).y);
					if (Cells.get(i).g > 255)
						Cells.get(i).g = 255;
					break;
				}
				if (cur == 34) //check cell
				{
					if (checkCell(Cells.get(i)) != 0)
						Cells.get(i).setCurrent(Cells.get(i).getCurrent() + 1);
				}
				if (cur >= 35 && cur < 43) //kill cell
				{
					int num = checkCell(Cells.get(i));
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + 1);
					if (num > 0) {
						Cells.get(i).r += 50;
						if (Cells.get(i).r > 255)
							Cells.get(i).r = 255;
						Cells.get(i).g -= 10;
						if (Cells.get(i).g < 0)
							Cells.get(i).g = 0;
						Cells.get(i).kills++;
						Cells.get(i).Energy-=5;
						eat(i, num);
					}
					break;
				}
				if (cur >= 43 && cur < 51) //recycle minerals
				{
					Cells.get(i).Energy += Cells.get(i).Mineral * 4;
					Cells.get(i).b += Cells.get(i).Mineral * 4;
					Cells.get(i).Mineral = 0;
					if (Cells.get(i).b > 255)
						Cells.get(i).b = 255;
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + 1);
					break;
				}
				if (cur == 51) //mitoz
				{
					mitoz(i);
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + 1);
					break;
				}
				if (cur > 51)
					Cells.get(i).setCurrent(Cells.get(i).getCurrent() + cur);

				j++;
				Cells.get(i).setCurrent(Cells.get(i).getCurrent() + 1);
			}
			if(i>=Cells.size())
				break;
			if (Cells.get(i).Energy >= 900) {
				mitoz(i);
			}
			if (Cells.get(i).Energy >= 1000) {
				Cells.get(i).Energy=1000;
			}
			if (Cells.get(i).Mineral > 1000)
				Cells.get(i).Mineral = 1000;
			if(Cells.get(i).Energy>100)
				Cells.get(i).Mineral += Map.getMineral(Cells.get(i).x, Cells.get(i).y);
			Cells.get(i).Energy -=1+Cells.get(i).age/20;
			Cells.get(i).age++;
			if (Cells.get(i).r > 4)
				Cells.get(i).r -= 2;
			if (Cells.get(i).g > 4)
				Cells.get(i).g -= 5;
			if (Cells.get(i).b > 4)
				Cells.get(i).b -= 2;
			if (Cells.get(i).Energy <= 0) {
					deleteCell(i);
					i--;
					continue;
			}
		}
	}
}
