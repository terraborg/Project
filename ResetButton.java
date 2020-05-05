package com.example.myapplication;

import android.graphics.Rect;

public class ResetButton extends CanvasButton {
	ResetButton(Rect rect, String msg, DrawThread drawThread) {
		super(rect, msg, drawThread);
	}

	@Override
	void onTouch() {
		super.onTouch();
		super.getDrawThread().reset();
	}
}
