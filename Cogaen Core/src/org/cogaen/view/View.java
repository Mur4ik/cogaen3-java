package org.cogaen.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.event.Event;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;

public class View extends CogaenBase {

	private Map<CogaenId, EntityRepresentation> representationsMap = new HashMap<CogaenId, EntityRepresentation>();
	private List<EntityRepresentation> representations = new ArrayList<EntityRepresentation>();
	private List<Engageable> engageables = new ArrayList<Engageable>();
	private EventService evtSrv;
	
	public View(Core core) {
		super(core);
	}
	
	public void registerResources(CogaenId groupId) {
		// intentionally left empty
	}
	
	public final void addRepresentation(EntityRepresentation er) {
		CogaenId entityId = er.getEntityId();
		EntityRepresentation old = this.representationsMap.put(entityId, er);
		if (old != null) {
			this.representationsMap.put(entityId, old);
			throw new RuntimeException("ambiguous entity id " + entityId);
		}
		assert(!this.representations.contains(er));
		this.representations.add(er);
		er.engage();
	}

	@Deprecated
	public final void addRepresentation(CogaenId entityId, EntityRepresentation er) {
		EntityRepresentation old = this.representationsMap.put(entityId, er);
		if (old != null) {
			this.representationsMap.put(entityId, old);
			throw new RuntimeException("ambiguous entity id " + entityId);
		}
		assert(!this.representations.contains(er));
		this.representations.add(er);
		er.engage();
	}
	
	public final void removeRepresentation(CogaenId entityId) {
		EntityRepresentation er = this.representationsMap.remove(entityId);
		if (er == null) {
			throw new RuntimeException("unknown entity id" + entityId);			
		}
		this.representations.remove(er);
		er.disengage();
	}
	
	public final boolean hasRepresentation(CogaenId entityId) {
		return this.representationsMap.containsKey(entityId);
	}
	
	public final EntityRepresentation getRepresentation(CogaenId entityId) {
		return this.representationsMap.get(entityId);
	}
	
	public final void removeAllRepresentations() {
		for (EntityRepresentation er : this.representationsMap.values()) {
			er.disengage();
		}
		this.representationsMap.clear();
		this.representations.clear();
	}
		
	public final int numOfRepresentation() {
		return this.representations.size();
	}
	
	public final EntityRepresentation getRepresentation(int idx) {
		return this.representations.get(idx);
	}
	
	public final void addEngageable(Engageable engageable) {
		this.engageables.add(engageable);
	}
	
	public final void removeEngageable(Engageable engageable) {
		this.engageables.remove(engageable);
	}
	
	public final void dispatchEvent(CogaenId eventType) {
		dispatchEvent(eventType, 0);
	}
	
	public final void dispatchEvent(CogaenId eventType, double delay) {
		dispatchEvent(new SimpleEvent(eventType), delay);
	}
	
	public final void dispatchEvent(Event event) {
		dispatchEvent(event, 0);
	}
	
	public final void dispatchEvent(Event event, double delay) {
		if (delay == 0) {
			this.evtSrv.dispatchEvent(event);
		} else {
			this.evtSrv.dispatchEvent(event, delay);
		}
	}
	
	@Override
	public void engage() {
		super.engage();
		this.evtSrv = EventService.getInstance(getCore());
		for (Engageable engageable : this.engageables) {
			engageable.engage();
		}
	}

	@Override
	public void disengage() {
		removeAllRepresentations();
		for (Engageable engageable : this.engageables) {
			engageable.disengage();
		}
		this.evtSrv = null;
		super.disengage();
	}
}
