package cam72cam.immersiverailroading.render;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import cam72cam.immersiverailroading.ConfigGraphics;
import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.proxy.ClientProxy;
import cam72cam.immersiverailroading.registry.DefinitionManager;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.render.entity.StockModel;
import cam72cam.immersiverailroading.render.item.IconTextureSheet;
import cam72cam.immersiverailroading.util.GLBoolTracker;

public class StockRenderCache {
	private static Map<String, StockModel> render_cache = new HashMap<String, StockModel>();
	private static IconTextureSheet icon_cache;
	private static boolean isCachePrimed = false;

	public static void clearRenderCache() {
		for (StockModel model : render_cache.values()) {
			model.freeGL();
		}
		render_cache = new HashMap<String, StockModel>();
		icon_cache = null;
		isCachePrimed = false;
	}

	private static void tryPrimeRenderCache() {
		if(isCachePrimed) {
			return;
		}
		isCachePrimed = true;
		
		for (String def : DefinitionManager.getDefinitionNames()) {
			ImmersiveRailroading.info("Priming Render Cache: %s", def);;
			StockModel renderer = getRender(def);
			if (ConfigGraphics.enableItemRenderPriming) {
				renderer.bindTexture();
				renderer.draw();
				renderer.restoreTexture();
				ClientProxy.renderCacheLimiter.reset();
			}
		}
	}

	private static void tryPrimeImageCache() {
		if (icon_cache != null) {
			return;
		}
		
		if (ConfigGraphics.enableIconCache) {
			System.out.println("Initializing Icon Cache...");
			icon_cache = new IconTextureSheet();
			for (String defID : DefinitionManager.getDefinitionNames()) {
				StockModel model = getRender(defID);
				icon_cache.add(defID, () -> {
					GLBoolTracker tex = new GLBoolTracker(GL11.GL_TEXTURE_2D, model.hasTexture());
					GLBoolTracker cull = new GLBoolTracker(GL11.GL_CULL_FACE, true);
					GL11.glPushMatrix();
					{
						GL11.glRotated(95, 0, 1, 0);
						GL11.glRotated(180, 0, 1, 0);
						model.bindTexture();
						model.drawDirect();
					}
					GL11.glPopMatrix();
					tex.restore();
					cull.restore();
				});
			}
		}
	}
	
	public static boolean renderIcon(String defID) {
		if (icon_cache == null) {
			return false;
		}
		return icon_cache.renderIcon(defID);
	}

	public static StockModel getRender(String defID) {
		if (!render_cache.containsKey(defID)) {
			EntityRollingStockDefinition def = DefinitionManager.getDefinition(defID);
			if (def != null) {
				render_cache.put(defID, new StockModel(def.getModel()));
			}
		}
		return render_cache.get(defID);
	}

	public static void tryPrime() {
		tryPrimeRenderCache();
		tryPrimeImageCache();
	}
}
