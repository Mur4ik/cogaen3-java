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

import org.lwjgl.opengl.GL11;

public class RectangleVisual extends Visual {

	private double halfWidth;
	private double halfHeight;
	private int glMode;
	
	public RectangleVisual(double width, double height) {
		setDimension(width,height);
		this.glMode = GL11.GL_QUADS;
	}
	
	private RectangleVisual() {
		// intentionally left empty
	}
	
	@Override
	public void render() {
		getColor().apply();
		
	    GL11.glBegin(this.glMode);
        GL11.glVertex2d(-this.halfWidth * getScale(), -this.halfHeight * getScale());
        GL11.glVertex2d(this.halfWidth * getScale(), -this.halfHeight * getScale());
        GL11.glVertex2d(this.halfWidth * getScale(), this.halfHeight * getScale());
		GL11.glVertex2d(-this.halfWidth * getScale(), this.halfHeight * getScale());
	    GL11.glEnd();
	}

	@Override
	public void prolog() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public RectangleVisual newInstance() {
		RectangleVisual instance = new RectangleVisual();
		super.copyFields(instance);
		instance.halfWidth = this.halfWidth;
		instance.halfHeight = this.halfHeight;
		instance.glMode = this.glMode;
		
		return instance;
	}

	public void setFilled(boolean filled) {
		this.glMode = filled ? GL11.GL_QUADS : GL11.GL_LINE_LOOP;
	}
	
	public boolean isFilled() {
		return this.glMode == GL11.GL_QUADS;
	}

	public void setWidth(double width) {
		this.halfWidth = width / 2;
	}
	
	public void setHeight(double height) {
		this.halfHeight = height / 2;
	}

	public void setDimension(double width, double height) {
		this.halfWidth = width * 0.5;
		this.halfHeight = height * 0.5;
	}
}
