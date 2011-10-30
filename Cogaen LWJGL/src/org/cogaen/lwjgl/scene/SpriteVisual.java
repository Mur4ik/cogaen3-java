package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class SpriteVisual extends Visual {

	private Texture texture;
	private double halfWidth;
	private double halfHeight;
	private boolean flipVertical;
	private int blendMode = GL11.GL_ONE_MINUS_SRC_ALPHA;
	
	SpriteVisual(Texture texture, double width, double height) {
		super(Color.WHITE);
		this.texture = texture;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
	}
	
	private SpriteVisual() {
		// intentionally left empty
	}
	
	public void setAlpha(double alpha) {
		getColor().setAlpha(alpha);
	}
	
	public void setSize(double width, double height) {
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
	}
	
	public boolean isFlipVertical() {
		return flipVertical;
	}

	public void setFlipVertical(boolean upsideDown) {
		this.flipVertical = upsideDown;
	}
	
	@Override
	public void render() {
		getColor().apply();
		
		if (this.flipVertical) {
			GL11.glScaled(1, -1, 1);
		}
    	
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glTexCoord2f(0.0f, this.texture.getHeight());
	    GL11.glVertex2d(-this.halfWidth * getScale(), -this.halfHeight * getScale());
	    
	    GL11.glTexCoord2f(this.texture.getWidth(), this.texture.getHeight());
        GL11.glVertex2d(this.halfWidth * getScale(), -this.halfHeight * getScale());
        
	    GL11.glTexCoord2f(this.texture.getWidth(), 0);
        GL11.glVertex2d(this.halfWidth * getScale(), this.halfHeight * getScale());
        
	    GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2d(-this.halfWidth * getScale(), this.halfHeight * getScale());
	    GL11.glEnd();
	}

	@Override
	public void prolog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, this.blendMode);
    	texture.bind();	// or GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public SpriteVisual newInstance() {
		SpriteVisual instance = new SpriteVisual();
		super.copyFields(instance);
		instance.halfWidth = this.halfWidth;
		instance.halfHeight = this.halfHeight;
		instance.texture = this.texture;
		instance.blendMode = this.blendMode;
		
		return instance;
	}

	public void setAdditive(boolean value) {
		this.blendMode = value ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA;
	}
	
	public boolean isAdditive() {
		return this.blendMode == GL11.GL_ONE;
	}
}
