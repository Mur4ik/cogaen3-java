/* 
-----------------------------------------------------------------------------
                   Cogaen - Component-based Game Engine V3
-----------------------------------------------------------------------------
This software is developed by the Cogaen Development Team. Please have a 
look at our project home page for further details: http://www.cogaen.org
   
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Copyright (c) 2010-2011 Roman Divotkey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
*/

package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceService;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class MultiLineLabelVisual extends Visual implements Label {

	private static final double LINE_SPACE = 1.0;
	private TrueTypeFont ttf;
	private StringBuffer lines[];
	private double width;
	private double height;
	private int curX;
	private int curY;
	private Alignment alignment = Alignment.LEFT;
	
	public MultiLineLabelVisual(Core core, String fontRes, double width, double height) {
		
		
		this.ttf = (TrueTypeFont) ResourceService.getInstance(core).getResource(fontRes);
		this.width = width;
		this.height = height;
		
		int nLines = (int) (this.height / (ttf.getHeight() * LINE_SPACE));
		this.lines = new StringBuffer[nLines];
		for (int i = 0; i < nLines; ++i) {
			this.lines[i] = new StringBuffer();
		}
		
		if (this.lines.length == 0) {
			throw new RuntimeException("dimension to small for text output");
		}
		
		this.curX = 0;
		this.curY = 0;
	}
	
	MultiLineLabelVisual() {
		// intentionally left empty
	}
	
	@Override
	public void prolog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render() {
		GL11.glPushMatrix();
		GL11.glScaled(getScale(), -getScale(), 1);
		org.newdawn.slick.Color utilColor = new org.newdawn.slick.Color((int) (getColor().getRed() * 255), (int) (getColor().getGreen() * 255), (int) (getColor().getBlue() * 255), (int) (getColor().getAlpha() * 255));
		
		for (int i = 0; i < this.lines.length; ++i) {
			switch (this.alignment) {
			
			case LEFT:
				this.ttf.drawString((float) (-this.width / 2), (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString(), utilColor);						
				break;
				
			case CENTER: {
				float textWidth = this.ttf.getWidth(this.lines[i].toString());
				this.ttf.drawString(-textWidth / 2, (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString(), utilColor);										
				break;
			}
				
			case RIGHT: {
				float textWidth = this.ttf.getWidth(this.lines[i].toString());
				this.ttf.drawString((float) (this.width / 2) - textWidth, (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString(), utilColor);						
			}
			
			}
		}
		GL11.glPopMatrix();
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public MultiLineLabelVisual newInstance() {
		MultiLineLabelVisual instance = new MultiLineLabelVisual();
		super.copyFields(instance);
		instance.ttf = ttf;
		instance.width = this.width;
		instance.height = this.height;
		instance.alignment = this.alignment;
		
		for (int i = 0; i < this.lines.length; ++i) {
			instance.lines[i] = new StringBuffer(this.lines[i]);
		}
		
		return instance;
	}

	@Override
	public void addChar(char ch) {
		if (ch == '\n' && this.curY < this.lines.length - 1) {
			this.curY++;
			this.curX = 0;
			return;
		}
		
		if (this.lines[this.curY].length() <= this.curX) {
			this.lines[this.curY].append(ch);
		} else {
			this.lines[this.curY].insert(this.curX, ch);
		}
		this.curX++;
		
		if (!adjust(this.curY)) {
			this.lines[this.curY].deleteCharAt(this.curX - 1);
			this.curX--;
		}
	}
	
	@Override
	public void setText(String str) {
		for (int i = 0; i < this.lines.length; ++i) {
			this.lines[i].setLength(0);
			this.curX = 0;
			this.curY = 0;
		}
		
		for (int i = 0; i < str.length(); ++i) {
			addChar(str.charAt(i));
		}
	}
	
	private boolean adjust(int idx) {
		if(this.ttf.getWidth(this.lines[idx].toString()) > this.width) {
			if (idx + 1 >= this.lines.length) {
				return false;
			}
			
			int i = this.lines[idx].length() - 1;
			while (i > 0 && this.lines[idx].charAt(i) != ' ') {
				--i;
			}
			if (i == 0) {
				return false;
			}
			
			String word = this.lines[idx].substring(i + 1, this.lines[idx].length());
			this.lines[idx].delete(i, this.lines[idx].length());
			this.lines[idx + 1].insert(0, word);
			boolean needSpace = this.lines[idx + 1].length() > word.length();
			if (needSpace) {
				this.lines[idx + 1].insert(word.length(), ' ');
			}
			
			int oldCurX = -1;
			if (idx == this.curY && this.curX > this.lines[idx].length()) {
				this.curY++;
				oldCurX = this.curX;
				this.curX = word.length();
			}
			
			if (!adjust(idx + 1)) {
				this.lines[idx].append(word);
				this.lines[idx + 1].delete(0, word.length());
				if (needSpace) {
					this.lines[idx + 1].delete(0, 1);					
				}
				if (oldCurX != -1) {
					this.curY--;
					this.curX = oldCurX;
				}
				return false;
			}
			return adjust(idx);
		}
		
		return true;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment allignment) {
		this.alignment = allignment;
	}
}
