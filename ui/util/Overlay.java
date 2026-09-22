package autofighter.ui.util;

import com.osrsbots.orb.api.RuneLite;
import com.osrsbots.orb.api.interactables.entities.Npcs;
import com.osrsbots.orb.api.interactables.types.RSNpc;
import com.osrsbots.orb.api.queries.actors.query.NpcQuery;
import com.osrsbots.orb.api.util.ColorScheme;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import autofighter.AutoFighter;

import java.awt.*;

@Slf4j
@SuppressWarnings("deprecation")
public class Overlay extends net.runelite.client.ui.overlay.Overlay {

    final Color transGreen = new Color(158, 178, 93, 64);

    final Stroke stroke = new BasicStroke((float) 1);

    final AutoFighter script;

    Rectangle bounds;

    public boolean started = false;

    public Overlay(final AutoFighter script) {
        this.script = script;
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPosition(OverlayPosition.DYNAMIC);

        setResettable(false);
        setResizable(false);


        // Bounds	java.awt.Rectangle[x=4,y=4,width=512,height=334]
        final Widget widget = RuneLite.client.getWidget(WidgetInfo.FIXED_VIEWPORT);

        if (widget != null) {
            bounds = widget.getBounds();
            ;
        } else {
            log.info("Failed to get VIEWPORT bounds, using default!");
            bounds = new Rectangle(4, 4, 512, 334);
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (started) {
            drawTickCounter(graphics, script.attackSpeedInTicks, script.tickCount);
        } else {
            drawCombatTargets(graphics);
            drawCombatZone(graphics);
        }

        return null;
    }

    private void drawTickCounter(final Graphics2D graphics, final int boxCount, final int tickCount) {
        if (bounds == null) return;

        int rectWidth = 25;
        int rectHeight = 25;
        int spacing = 25; // space between boxes

        int totalWidth = boxCount * rectWidth + (boxCount - 1) * spacing;
        int startX = (int) ((bounds.getWidth() - totalWidth) / 2);
        int centerY = (int) (bounds.getHeight() - (bounds.getHeight() / 8 - (double) rectHeight / 8));

        for (int i = 0; i < boxCount; i++) {
            final int x = startX + i * (rectWidth + spacing);

            if (i == tickCount) {
                graphics.setColor(ColorScheme.DARK_SLATE_BLUE);
                graphics.fillRect(x, centerY, rectWidth, rectHeight);
            }

            graphics.setColor(Color.WHITE);
            graphics.drawRect(x, centerY, rectWidth, rectHeight);
        }
    }

    private void drawCombatTargets(final Graphics2D graphics) {
        final int dis = script.ui.getCombatZoneDistance();

        script.ui.getCombatTargets().forEach(t -> {

            NpcQuery query = Npcs.query().names(t.name).levels(t.level);

            if (script.ui.location != null)
                query = query.filter(n -> n.getWorldLocation().distanceTo(script.ui.location) <= dis);

            for (RSNpc rsn : query.results().get()) {
                final Shape clickbox = rsn.getClickbox();
                if (clickbox != null) OverlayUtil.renderPolygon(graphics, clickbox, ColorScheme.RUSTY_RED);
            }
        });
    }

    private void drawCombatZone(final Graphics2D graphics) {
        // Draw combat zone
        final WorldPoint combatZone = script.ui.location;
        final int zoneDistance = script.ui.getCombatZoneDistance();

        if (combatZone != null) {
            final WorldView view = RuneLite.client.getTopLevelWorldView();
            final Scene scene = view.getScene();
            final int z = view.getPlane();
            final Tile[][][] tiles = scene.getTiles();

            Tile tile;
            Polygon poly;

            for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
                for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                    if ((tile = tiles[z][x][y]) == null) continue;

                    if (tile.getWorldLocation().distanceTo(combatZone) == zoneDistance) {
                        poly = Perspective.getCanvasTilePoly(RuneLite.client, tile.getLocalLocation());

                        if (poly != null) {
                            OverlayUtil.renderPolygon(
                                    graphics, poly, ColorScheme.DARK_SLATE_BLUE, transGreen, stroke
                            );
                        }
                    }
                }
            }
        }
    }
}
