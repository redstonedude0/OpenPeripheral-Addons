package openperipheral.addons.glasses.drawable;

import openmods.structured.StructureField;
import openperipheral.addons.glasses.utils.IPointListBuilder;
import openperipheral.addons.glasses.utils.IPolygonBuilder;
import openperipheral.addons.glasses.utils.Point2d;
import openperipheral.addons.glasses.utils.PointListBuilder;
import openperipheral.addons.glasses.utils.RenderState;
import openperipheral.addons.glasses.utils.SolidPolygonBuilder;
import openperipheral.api.adapter.AdapterSourceName;
import openperipheral.api.adapter.Property;
import openperipheral.api.adapter.method.ScriptObject;

@ScriptObject
@AdapterSourceName("glasses_polygon")
public class SolidPolygon extends Polygon<Point2d> {

	@Property
	@StructureField
	public int color = 0xFFFFFF;

	@Property
	@StructureField
	public float opacity = 1.0f;

	public SolidPolygon() {}

	public SolidPolygon(int color, float opacity, Point2d... points) {
		super(points);

		this.color = color;
		this.opacity = opacity;
	}

	@Override
	protected IPointListBuilder<Point2d> createBuilder() {
		return new PointListBuilder();
	}

	@Override
	protected IPolygonBuilder<Point2d> createPolygonBuilder() {
		return new SolidPolygonBuilder();
	}

	@Override
	protected DrawableType getTypeEnum() {
		return DrawableType.POLYGON;
	}

	@Override
	protected void drawContents(RenderState renderState, float partialTicks) {
		if (canRender()) {
			super.drawContents(renderState, partialTicks);
			renderState.setColor(color, opacity);
			renderPolygon(renderState);
		}
	}

}
