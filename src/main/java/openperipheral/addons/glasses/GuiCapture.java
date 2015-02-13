package openperipheral.addons.glasses;

import net.minecraft.client.gui.GuiScreen;
import openmods.network.event.NetworkEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesComponentMouseButtonEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesComponentMouseWheelEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesKeyDownEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesKeyUpEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesMouseButtonEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesMouseWheelEvent;
import openperipheral.addons.glasses.GlassesEvent.GlassesSignalCaptureEvent;
import openperipheral.addons.glasses.TerminalManagerClient.DrawableHitInfo;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiCapture extends GuiScreen {

	private int backgroundColor = 0x2A00FF00;
	private final long guid;

	public GuiCapture(long guid) {
		this.guid = guid;
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();

		int button = Mouse.getEventButton();
		boolean state = Mouse.getEventButtonState();
		int wheel = Mouse.getEventDWheel();

		if (button != -1 || state || wheel != 0) {
			int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

			final DrawableHitInfo hit = TerminalManagerClient.instance.findDrawableHit(guid, x, y);

			if (button != -1) createMouseButtonEvent(button, state, hit).sendToServer();
			if (wheel != 0) createMouseWheelEvent(wheel, hit).sendToServer();
		}
	}

	private NetworkEvent createMouseButtonEvent(int button, boolean state, DrawableHitInfo hit) {
		return hit != null?
				new GlassesComponentMouseButtonEvent(guid, hit.id, hit.isPrivate, hit.dx, hit.dy, button, state) :
				new GlassesMouseButtonEvent(guid, button, state);
	}

	private NetworkEvent createMouseWheelEvent(int wheel, DrawableHitInfo hit) {
		return hit != null?
				new GlassesComponentMouseWheelEvent(guid, hit.id, hit.isPrivate, hit.dx, hit.dy, wheel) :
				new GlassesMouseWheelEvent(guid, wheel);
	}

	@Override
	public void handleKeyboardInput() {
		final int key = Keyboard.getEventKey();

		if (key == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		} else {
			final boolean state = Keyboard.getEventKeyState();
			if (state) {
				final char ch = Keyboard.getEventCharacter();

				final boolean isRepeat = Keyboard.isRepeatEvent();
				new GlassesKeyDownEvent(guid, ch, key, isRepeat).sendToServer();
			} else {
				new GlassesKeyUpEvent(guid, key).sendToServer();
			}
		}

		// looks like twitch controls
		this.mc.func_152348_aa();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTickTime) {
		drawRect(0, 0, width, height, backgroundColor);
	}

	@Override
	public void initGui() {
		new GlassesSignalCaptureEvent(guid, true).sendToServer();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void onGuiClosed() {
		new GlassesSignalCaptureEvent(guid, false).sendToServer();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public long getGuid() {
		return guid;
	}

	public void setBackground(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}