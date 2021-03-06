package org.cogaen.box2d;

import org.cogaen.math.Vector2;
import org.cogaen.name.CogaenId;

public interface PhysicsBody {
	
	public static final CogaenId ATTR_ID = new CogaenId("PhysicsBody");
	
	public void applyForce(double fx, double fy, double px, double py);
	public void applyRelativeForce(double fx, double fy, double px, double py);
	public void applyTorque(double torque);
	public void getVelocity(double px, double py, Vector2 result);
	public double getVelocityX(double px, double py);
	public double getVelocityY(double px, double py);
	public void setVelocity(double vx, double vy);
	
	public void getWorldPoint(double px, double py, Vector2 result);
	public void getWorldVector(double vx, double vy, Vector2 result);
}
