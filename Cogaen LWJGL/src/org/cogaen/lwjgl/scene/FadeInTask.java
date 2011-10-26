package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;
import org.cogaen.task.AbstractTask;
import org.cogaen.task.TaskService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class FadeInTask extends AbstractTask {

	public static final CogaenId FADE_IN_FINISHED_EVENT_ID = new CogaenId("FadeInFinished");
	
	private Visual visual;
	private double fadeTime;
	private Timer timer;
	private double startTime;
	private CogaenId finishedEventId = FADE_IN_FINISHED_EVENT_ID;
	
	public FadeInTask(Core core, Visual visual, double fadeTime) {
		super(core, "Fade-in");
		this.visual = visual;
		this.visual.getColor().setAlpha(0.0);
		this.fadeTime = fadeTime;
		
		this.timer = TimeService.getInstance(core).getTimer();
		this.startTime = this.timer.getTime();
	}

	@Override
	public void update() {
		double elapsed = this.timer.getTime() - this.startTime;
		if (elapsed >= fadeTime) {
			TaskService.getInstance(getCore()).destroyTask(this);
			return;
		}
		
		double alpha = elapsed / this.fadeTime;
		this.visual.getColor().setAlpha(alpha);
	}

	@Override
	public void destroy() {
		this.visual.getColor().setAlpha(1.0);
		if (this.finishedEventId != null) {
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.finishedEventId));
		}
	}

	public CogaenId getFinishedEventId() {
		return finishedEventId;
	}

	public void setFinishedEventId(CogaenId finishedEventId) {
		this.finishedEventId = finishedEventId;
	}
}