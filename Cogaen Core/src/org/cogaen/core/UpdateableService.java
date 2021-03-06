/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2012 Roman Divotkey

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

package org.cogaen.core;

/**
 * This class provides a default implementation for a service that needs
 * to be updated within the game loop.
 * 
 * <p>Developer need only subclass this abstract class and optionally 
 * override the following methods:
 * <ul>
 * <li>{@code doStart}</li>
 * <li>{@code doPause}</li>
 * <li>{@code doResume}</li>
 * <li>{@code doStop}</li>
 * </ul>
 * </p> 
 * 
 * @see AbstractService
 */
public abstract class UpdateableService extends AbstractService implements Updatable {

	@Override
	protected void doPause() {
		getCore().removeUpdateable(this);
		super.doPause();
	}

	@Override
	protected void doResume() {
		getCore().addUpdatable(this);
		super.doResume();
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		getCore().addUpdatable(this);
	}

	@Override
	protected void doStop() {
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
		super.doStop();
	}
}
