package com.example.myapplication;

import android.graphics.Rect;

public class ModeButton extends CanvasButton {

	private int mode;

	ModeButton(Rect rect, String msg, DrawThread drawThread,int mode) {
		super(rect, msg, drawThread);
		this.mode=mode;
	}

	@Override
	void onTouch() {
		super.onTouch();
		super.getDrawThread().setMode(mode);
	}

}
