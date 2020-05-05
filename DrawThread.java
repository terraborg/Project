package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

public class DrawThread extends Thread implements Resetable{

	private SurfaceHolder surfaceHolder;

	private Game game;

	private int x1,x2,y1,y2;

	private boolean running = true,pause=true;

	private int speed;

	private int width, height;

	Rect rect;

	private Canvas gameCanvas,canvas;

	int choosedCell;

	private int k=0;

	private CellColor avgColor;

	private int mode;

	private int minMuts=0,maxMuts=0,maxAge=0,maxKills=0;
	private float avgEnergy=0,avgMineral=0,avgMuts=0,avgAge=0,avgKills=0;

	private Cell choosed;
	private PauseButton pauseButton;
	private ModeButton fotoButton;
	private ModeButton rdmButton;
	private ModeButton hemoButton;
	private ChangeSpeedButton plusButton;
	private ChangeSpeedButton minusButton;
	private ResetButton resetButton;

	public DrawThread(Context context, SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		canvas = this.surfaceHolder.lockCanvas();
		width = canvas.getWidth();
		height = canvas.getHeight();
		choosedCell=-1;
		mode=1;
		avgColor=new CellColor(0,127,65);
		speed=30;
		x1 = (int) (width*0.05)/10*10;
		x2 = (int) (width*0.65)/10*10;
		y1 = (int) (height*0.05)/10*10;
		y2 = (int) (height*0.75)/10*10;
		pauseButton = new PauseButton(new Rect((int)(x2+width*0.03),(int)(y1+height*0.35),(int)(x2+width*0.15),(int)(y1+height*0.45)),"Pause",this);
		fotoButton = new ModeButton(new Rect((int)(x2+width*0.22),(int)(y1),(int)(x2+width*0.28),(int)(y1+height*0.06)),"foto",this,1);
		rdmButton = new ModeButton(new Rect((int)(x2+width*0.22),(int)(y1+height*0.08),(int)(x2+width*0.28),(int)(y1+height*0.14)),"rdm",this,2);
		hemoButton = new ModeButton(new Rect((int)(x2+width*0.22),(int)(y1+height*0.16),(int)(x2+width*0.28),(int)(y1+height*0.22)),"hemo",this,3);
		plusButton = new ChangeSpeedButton(new Rect((int)(x2+width*0.17),(int)(y1+height*0.40),(int)(x2+width*0.22),(int)(y1+height*0.45)),"+",this,1);
		minusButton = new ChangeSpeedButton(new Rect((int)(x2+width*0.26),(int)(y1+height*0.40),(int)(x2+width*0.31),(int)(y1+height*0.45)),"-",this,-1);
		resetButton = new ResetButton(new Rect((int)(x2+width*0.03),(int)(y1+height*0.50),(int)(x2+width*0.15),(int)(y1+height*0.60)),"Reset",this);
		pauseButton.setTextSize(40);
		resetButton.setTextSize(40);
		fotoButton.setTextSize(25);
		rdmButton.setTextSize(25);
		hemoButton.setTextSize(25);
		minusButton.setTextSize(30);
		plusButton.setTextSize(30);
		rect = new Rect(x1,y1,x2,y2);
		game = new Game(y2-y1, x2-x1);
		this.surfaceHolder.unlockCanvasAndPost(canvas);
	}

	void pleaseStop()
	{
		running = false;
	}

	void setSpeed(int speed) {
		this.speed = speed;
	}

	int getSpeed(){
		return speed;
	}

	int getCell(int x,int y)
	{
		return game.getCellOnMap(x,y);
	}

	void putCell(int x,int y)
	{
		try {
			game.putCell(x, y, mode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	void onTouch(int x,int y)
	{
		pauseButton.checkForTouch(x,y);
		plusButton.checkForTouch(x,y);
		minusButton.checkForTouch(x,y);
		fotoButton.checkForTouch(x,y);
		rdmButton.checkForTouch(x,y);
		hemoButton.checkForTouch(x,y);
		resetButton.checkForTouch(x,y);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void run() {


		while(running)
		{
			Paint paint = new Paint();

			canvas = surfaceHolder.lockCanvas();
			try
			{
				DrawBack(canvas);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			surfaceHolder.unlockCanvasAndPost(canvas);
			try {
				Thread.sleep(1000/speed);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameCanvas = surfaceHolder.lockCanvas(rect);
			rect.left=x1;
			rect.top=y1;
			rect.right=x2;
			rect.bottom=y2;
			if(pause)
				try
				{
					game.tick();
					if(game.getCellsSize()!=0)
						k++;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			try
			{
				int r=0,g=0,b=0;
				paint.setARGB(255,0,0,0);
				gameCanvas.drawARGB(255,230,230,255);
				int mnMuts=-1,mxMuts=0,sumE=0,sumMin=0,sumMuts=0,countMin=0,mxAge=0,mxKills=0,sumAge=0,sumKills=0;
				for(int i=0; i<game.getCellsSize();i++)
				{
					Cell cell = game.getCell(i);
					if(cell.getMuts()>mxMuts)
						mxMuts=cell.getMuts();
					if(cell.getMuts()<mnMuts||mnMuts==-1)
						mnMuts=cell.getMuts();
					sumE+=cell.getEnergy();
					if(cell.getAge()>mxAge)
						mxAge=cell.getAge();
					if(cell.getKills()>mxKills)
						mxKills=cell.getKills();
					sumAge+=cell.getAge();
					sumKills+=cell.getKills();
					sumMuts+=cell.getMuts();
					if(cell.getMineral()!=0)
					{
						sumMin+=cell.getMineral();
						countMin++;
					}
					cell.drawCell(gameCanvas,rect);
					try {
						if(choosed!=null&&cell.getId()==choosed.getId())
						{
							choosed=Cell.clone(cell);
							paint.setStyle(Paint.Style.STROKE);
							paint.setStrokeWidth(2);
							paint.setARGB(255,0 ,0,0);
							cell.drawCell(gameCanvas,rect,paint);
							paint.setStyle(Paint.Style.FILL);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					r+=cell.getR();
					g+=cell.getG();
					b+=cell.getB();
				}
				int cellsSize=game.getCellsSize();
				if(cellsSize==0)
					cellsSize=1;
				if(countMin==0)
					countMin=1;
				avgAge= (float) (1.*sumAge/cellsSize);
				avgKills= (float) (1.*sumKills/cellsSize);
				avgEnergy= (float) (1.*sumE/cellsSize);
				avgMineral= (float) (1.*sumMin/countMin);
				avgMuts= (float) (1.*sumMuts/cellsSize);
				minMuts=mnMuts;
				maxMuts=mxMuts;
				maxAge=mxAge;
				maxKills=mxKills;
				if(game.getCellsSize()>=50)
				{
					avgColor.r=r/cellsSize;
					avgColor.b=b/cellsSize;
					avgColor.g=g/cellsSize;
				}
				paint.setColor(Color.BLACK);
				gameCanvas.drawText(String.valueOf(k),rect.right-40,rect.bottom-40,paint);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally {
				surfaceHolder.unlockCanvasAndPost(gameCanvas);
			}
		}
	}
	public void reset()
	{
		try {
			game.reset();
			k=0;
			choosedCell=-1;
			avgColor.setColor(0,127,65);
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
	}
	boolean OnPause()
	{
		return !pause;
	}

	void pleaseStart()
	{
		pause = true;
	}

	void pleasePause()
	{
		pause = false;
	}

	void setMode(int mode)
	{
		fotoButton.setMode(mode==1);
		hemoButton.setMode(mode==3);
		rdmButton.setMode(mode==2);
		this.mode=mode;
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	void DrawBack(Canvas canvas)
	{
		Paint paint =new Paint();
		paint.setARGB(255,0,127,65);
		canvas.drawRect(0,0,width,y1,paint);
		canvas.drawRect(0,y1,x1,y2,paint);
		canvas.drawRect(x2,y1,width,y2,paint);
		canvas.drawRect(0,y2,width,height,paint);
		paint.setColor(Color.GRAY);
		canvas.drawRoundRect((float)(x2+width*0.03),(float)(y1),(float)(x2+width*0.20),(float)(y1+height*0.30),25,25,paint);
		if(choosedCell>=game.getCellsSize())
			choosedCell=-1;
		if(choosedCell!=-1)
		{
			choosed=game.getCell(choosedCell);
			choosedCell=-1;
		}
		if(choosed!=null)
			drawCellStats(choosed,canvas);
		pauseButton.draw(canvas);
		resetButton.draw(canvas);
		minusButton.draw(canvas);
		plusButton.draw(canvas);
		fotoButton.draw(canvas);
		hemoButton.draw(canvas);
		rdmButton.draw(canvas);
		drawGameStats(canvas);
		paint.setColor(Color.BLACK);
		paint.setTextSize(25);
		canvas.drawText(String.valueOf(speed),(int)(x2+width*0.23),(int)(y1+height*0.435),paint);
		canvas.drawText("Speed",(int)(x2+width*0.22),(int)(y1+height*0.38),paint);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@SuppressLint("DefaultLocale")
	void drawGameStats(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setTextSize(20);
		canvas.drawRoundRect((float)x1,(float)(y2+height*0.03),(float)x2,(float)(y2+height*0.23),25,25,paint);
		paint.setARGB(255,255,200,0);
		canvas.drawText("Average Energy: "+ String.format("%.1f",avgEnergy),(float)(x1+width*0.02),(float)(y2+height*0.06),paint);
		paint.setColor(Color.BLUE);
		canvas.drawText("Average Mineral: "+String.format("%.1f",avgMineral),(float)(x1+width*0.02),(float)(y2+height*0.10),paint);
		paint.setColor(Color.GREEN);
		canvas.drawText("Max Mutations: "+maxMuts,(float)(x1+width*0.15),(float)(y2+height*0.06),paint);
		canvas.drawText("Min Mutations: "+minMuts,(float)(x1+width*0.15),(float)(y2+height*0.10),paint);
		canvas.drawText("Average Mutations: "+String.format("%.2f",avgMuts),(float)(x1+width*0.15),(float)(y2+height*0.14),paint);
		paint.setColor(Color.MAGENTA);
		canvas.drawText("Max Age: "+maxAge,(float)(x1+width*0.26),(float)(y2+height*0.06),paint);
		canvas.drawText("Average Age: "+String.format("%.1f",avgAge),(float)(x1+width*0.26),(float)(y2+height*0.10),paint);
		paint.setColor(Color.RED);
		canvas.drawText("Max Kills: "+maxKills,(float)(x1+width*0.37),(float)(y2+height*0.06),paint);
		canvas.drawText("Average Kills: "+String.format("%.2f",avgKills),(float)(x1+width*0.37),(float)(y2+height*0.10),paint);
		paint.setColor(Color.BLACK);
		canvas.drawText("Average Color:",(float)(x1+width*0.50),(float)(y2+height*0.06),paint);
		if(avgColor==null)
			paint.setColor(Color.WHITE);
		else
			paint.setARGB(255,avgColor.r,avgColor.g,avgColor.b);
		canvas.drawRoundRect((float)(x1+width*0.51),(float)(y2+height*0.09),(float)(x1+width*0.57),(float)(y2+height*0.09+width*0.06),20,20,paint);
	}

	void drawCellStats(Cell c,Canvas canvas) {
		Paint paint = new Paint();
		paint.setARGB(255,255,200,0);
		canvas.drawText("E:", (float) (x2+width*0.05),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.getEnergy()), (float) (x2+width*0.06),(float)(y1+height*0.03),paint);
		paint.setARGB(255,0,0,255);
		canvas.drawText("M:",(float) (x2+width*0.08),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.getMineral()), (float) (x2+width*0.09),(float)(y1+height*0.03),paint);
		paint.setARGB(255,255,0,0);
		canvas.drawText("K:",(float) (x2+width*0.11),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.getKills()), (float) (x2+width*0.12),(float)(y1+height*0.03),paint);
		paint.setARGB(255,255,0,255);
		canvas.drawText("Age:",(float) (x2+width*0.13),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.getAge()), (float) (x2+width*0.15),(float)(y1+height*0.03),paint);
		paint.setARGB(255,0,255,127);
		canvas.drawText("Muts:",(float) (x2+width*0.17),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.getMuts()), (float) (x2+width*0.19),(float)(y1+height*0.03),paint);
		paint.setARGB(255,255,0,0);
		canvas.drawText("R:",(float) (x2+width*0.05),(float)(y1+height*0.03+235),paint);
		canvas.drawText(String.valueOf(c.getR()), (float) (x2+width*0.06),(float)(y1+height*0.03+235),paint);
		paint.setARGB(255,0,255,0);
		canvas.drawText("G:",(float) (x2+width*0.08),(float)(y1+height*0.03+235),paint);
		canvas.drawText(String.valueOf(c.getG()), (float) (x2+width*0.09),(float)(y1+height*0.03+235),paint);
		paint.setARGB(255,0,0,255);
		canvas.drawText("B:",(float) (x2+width*0.11),(float)(y1+height*0.03+235),paint);
		canvas.drawText(String.valueOf(c.getB()), (float) (x2+width*0.12),(float)(y1+height*0.03+235),paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				c.setCurrent(i*8+j);
				paint.setColor(Color.BLACK);
				canvas.drawRect((float)(x2+width*0.05+i*25),(float)(y1+height*0.05+j*25),(float)(x2+width*0.05+i*25+25),(float)(y1+height*0.05+j*25+25),paint);
				if(c.getCurMind()==16||c.getCurMind()==17||c.getCurMind()==34)
					paint.setARGB(255,255,200,0);
				if(c.getCurMind()>=18&&c.getCurMind()<26)
					paint.setColor(Color.GREEN);
				if(c.getCurMind()>=43&&c.getCurMind()<51)
					paint.setColor(Color.BLUE);
				if(c.getCurMind()==51)
					paint.setARGB(255,255,0,255);
				if(c.getCurMind()>=35&&c.getCurMind()<43)
					paint.setColor(Color.RED);
				if(c.getCurMind()>=8&&c.getCurMind()<16)
					paint.setColor(Color.WHITE);
				canvas.drawText(String.valueOf(c.getCurMind()),(float)(x2+width*0.05+i*25+5),(float)(y1+height*0.05+j*25+15),paint);
			}
	}
}
