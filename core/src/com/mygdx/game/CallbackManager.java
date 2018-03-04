package com.mygdx.game;
import java.util.*;

public class CallbackManager
{
	private float time;
	private PriorityQueue<ExecutionBlockEvent > queue;
	
	public CallbackManager() {
		time = 0f;
		queue = new PriorityQueue<ExecutionBlockEvent >();
	}
	
	public void registerEventFromNow(float futureTime, ExecutioBlockInterface block) {
		queue.add(new ExecutionBlockEvent(futureTime, block));
	}
	
	public void registerEventAtTime(float waitTime, ExecutioBlockInterface block) {
		registerEventFromNow(time + waitTime, block);
	}
	
	public void update(float delta) {
		time += delta;
		for (int i = 0; i < queue.size(); i++) {
			ExecutionBlockEvent nextBlock = queue.peek();
			
			// If the next block is expected to run in the future then
			// we know that we can stop checking for more events.
			if (nextBlock.time > time) {
				break;
			}
			
			queue.poll();
			nextBlock.executeBlock();
		}
	}

	/** 
	 * This object will wrap the callback interface and the time it needs to be called.
	 * The time is represents the game time when the block will be called.
	 */
	private class ExecutionBlockEvent implements Comparable
	{
		private ExecutioBlockInterface block;
		private float time;

		public ExecutionBlockEvent(float time, ExecutioBlockInterface block) {
			this.block = block;
			this.time = time;
		}
		
		public void executeBlock() {
			block.execute();
			block = null;
		}
		
		@Override
		public int compareTo(Object p1)
		{
			if (p1.getClass() == ExecutionBlockEvent.class) {
				return Float.compare(time, ((ExecutionBlockEvent)p1).time);
			}
			throw new RuntimeException("Other object is not of type " + this.getClass());
		}
	}

	public interface ExecutioBlockInterface {
		void execute();
	}
}
