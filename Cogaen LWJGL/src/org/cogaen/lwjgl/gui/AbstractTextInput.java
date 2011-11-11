package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.lwjgl.input.KeyCode;
import org.cogaen.lwjgl.input.KeyPressedEvent;
import org.cogaen.lwjgl.input.KeyReleasedEvent;
import org.cogaen.lwjgl.input.MouseButtonPressedEvent;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.TextBlockVisual;
import org.cogaen.name.CogaenId;

public abstract class AbstractTextInput extends FrameGui implements EventListener {

	private static final double DEFAULT_GAP = 0.97;
	public static final CogaenId DEFAULT_PRESSED_EVENT_ID = new CogaenId("TextInputPressed");

	private TextBlockVisual tbv;
	private String fontRes;
	private double gap = DEFAULT_GAP;
	private boolean shift;
	private CogaenId pressedEventId;

	public AbstractTextInput(Core core, String fontRes, double width, double height) {
		super(core, width, height);
		this.fontRes = fontRes;
	}

	protected abstract double getScale();
	protected abstract boolean isHit(double x, double y);
	
	@Override
	public void engage() {
		super.engage();

		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
		evtSrv.addListener(this, KeyReleasedEvent.TYPE_ID);
		evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
		
		this.tbv = new TextBlockVisual(getCore(), fontRes, getWidth() * this.gap / getScale(), getHeight() * this.gap / getScale());
		this.tbv.setScale(getScale());
		
		getBaseNode().addVisual(this.tbv);
	}

	@Override
	public void setDisabled(boolean value) {
		if (isDisabled() == value) {
			return;
		}
		
		EventService evtSrv = EventService.getInstance(getCore());
		if (value) {
			evtSrv.removeListener(this, KeyPressedEvent.TYPE_ID);
			evtSrv.removeListener(this, KeyReleasedEvent.TYPE_ID);
			this.tbv.setShowCursor(false);
		} else {
			evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
			evtSrv.addListener(this, KeyReleasedEvent.TYPE_ID);
			this.tbv.setShowCursor(true);
		}
		super.setDisabled(value);
	}

	@Override
	public void disengage() {
		if (!isDisabled()) {
			EventService.getInstance(getCore()).removeListener(this);
		}
		super.disengage();
	}

	@Override
	public void setVisible(boolean value) {
		if (isVisible() == value) {
			return;
		}
		this.tbv.setMask(value ? 0xFFFF : 0x0000);
		super.setVisible(value);
	}
	
	public void setTextColor(ReadableColor color) {
		this.tbv.setColor(color);
	}
	
	public Color getTextColor() {
		return this.tbv.getColor();
	}
	
	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(KeyPressedEvent.TYPE_ID)) {
			handleKeyPressed((KeyPressedEvent) event);
		} else if (event.isOfType(KeyReleasedEvent.TYPE_ID)) {
			handleKeyReleased((KeyReleasedEvent) event);
		} else if (event.isOfType(MouseButtonPressedEvent.TYPE_ID)) {
			handleMouseButtonPressed((MouseButtonPressedEvent) event);
		}
	}
	
	private void handleMouseButtonPressed(MouseButtonPressedEvent event) {
		if (isHit(event.getPosX(), event.getPosY())) {
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.pressedEventId));
		}
	}
	
	private void handleKeyReleased(KeyReleasedEvent event) {
		switch (event.getKeyCode()) {
		case KeyCode.KEY_LSHIFT:
		case KeyCode.KEY_RSHIFT:
			this.shift = false;
		}
	}

	private void handleKeyPressed(KeyPressedEvent event) {
		if (KeyCode.isPrintable(event.getKeyCode())) {
			this.tbv.addChar(KeyCode.getChar(event.getKeyCode(), this.shift));
			return;
		}
		
		switch (event.getKeyCode()) {
		
		case KeyCode.KEY_LSHIFT:
		case KeyCode.KEY_RSHIFT:
			this.shift = true;
			break;
			
		case KeyCode.KEY_BACK:
			this.tbv.back();
			break;
			
		case KeyCode.KEY_LEFT:
			this.tbv.left();
			break;
			
		case KeyCode.KEY_RIGHT:
			this.tbv.right();
			break;
			
		case KeyCode.KEY_UP:
			this.tbv.up();
			break;
			
		case KeyCode.KEY_DOWN:
			this.tbv.down();
			break;
		}
	}

	public void setText(String text) {
		this.tbv.setText(text);
	}
	
	public String getText() {
		return this.tbv.getText();
	}

	public double getGap() {
		return gap;
	}

	public void setGap(double gap) {
		this.gap = gap;
	}

	public CogaenId getPressedEventId() {
		return pressedEventId;
	}

	public void setPressedEventId(CogaenId pressedEventId) {
		this.pressedEventId = pressedEventId;
	}
}
