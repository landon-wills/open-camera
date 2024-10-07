package ca.landonjw;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenCamera implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("open-camera");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}
