package openperipheral.addons.client;

import openmods.api.IProxy;
import cpw.mods.fml.common.network.IGuiHandler;

public class ClientProxy implements IProxy {

	@Override
	public IGuiHandler createGuiHandler() {
		return null;
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void registerRenderInformation() {}

}
