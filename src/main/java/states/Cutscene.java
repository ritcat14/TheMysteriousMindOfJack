package states;

import handler.StateHandler;
import handler.Tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import events.Event;
import events.EventDispatcher;
import events.EventHandler;
import events.types.KeyPressedEvent;

public class Cutscene extends State {
    
    private BufferedImage[] frames;
    private BufferedImage currentFrame;
    private int time = 0;
    private int timeSec = 0;
    private int[] times;
    private int frame = 0;
    
    public Cutscene(int scene) {
        String[] lines = Tools.getData("/scenes/sceneData.gme");
        String data = lines[scene];
        String[] parts = data.split(":");
        frames = Tools.splitImage("/scenes/" + parts[0] + ".png", Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        this.times = Tools.StringArrayToIntArray(parts[3].split(","));
    }
    
    private boolean keyPressed(KeyPressedEvent e) {
        switch (e.getKey()) {
            case KeyEvent.VK_SPACE:
                StateHandler.nextChapter();
                return true;
        }
        return false;
    }

    @Override
    public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(Event.Type.KEY_PRESSED, new EventHandler() {
			public boolean onEvent(Event event) {
				return keyPressed((KeyPressedEvent)event);
			}
		});
    }

    @Override
    public void update() {
        time++;
        if (time % 120 == 0) timeSec++;
        
        currentFrame = frames[frame];
        
        if (timeSec == times[frame]) {
            if (frame == frames.length - 1) {
                StateHandler.nextChapter();
            }
        frame++;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(currentFrame, 0, 0, null);
        g.setColor(Color.WHITE);
        g.drawString("Press Space to skip", StateHandler.WIDTH - 150, StateHandler.HEIGHT - 50);
    }
    
}
