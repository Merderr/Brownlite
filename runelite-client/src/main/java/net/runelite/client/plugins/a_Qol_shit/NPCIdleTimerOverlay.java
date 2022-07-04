package net.runelite.client.plugins.a_Qol_shit;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.inject.Inject;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class NPCIdleTimerOverlay extends Overlay
{

    private final NPCIdleTimerPlugin plugin;
    private final NPCIdleTimerConfig config;

    NumberFormat format = new DecimalFormat("#");

    int NPC_IDLE_RESPAWN_TIME = 300;

    @Inject
    NPCIdleTimerOverlay(NPCIdleTimerPlugin plugin, NPCIdleTimerConfig config)
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (config.showOverlay())
        {
            plugin.getWanderingNPCs().forEach((id, npc) -> renderTimer(npc, graphics));
        }
        return null;
    }

    private void renderTimer(final WanderingNPC npc, final Graphics2D graphics)
    {
        if ( config.customTimer())
        {
            NPC_IDLE_RESPAWN_TIME = config.customTiming();
        }
        else
        {
            NPC_IDLE_RESPAWN_TIME = 300;
        }

        double timeLeft = NPC_IDLE_RESPAWN_TIME - npc.getTimeWithoutMoving();

        double lowDisplay = config.lowDisplay();
        double maxDisplay = config.maxDisplay();
        Color timerColor = config.normalTimerColor();


        if (timeLeft < 0)
        {
            timeLeft = 0;
        }

        if (timeLeft <= lowDisplay)
        {
            timerColor = config.lowTimerColor();
        }

        String timeLeftString= String.valueOf(format.format(timeLeft));

        if(config.showTimingType())
        {
            if (config.showOverlayTicks())
            {
                timeLeftString = timeLeftString + ("T");
            }

            else
            {
                timeLeftString= timeLeftString + ("S");
            }
        }

        final Point canvasPoint = npc.getNpc().getCanvasTextLocation(graphics, timeLeftString, npc.getNpc().getLogicalHeight() + config.timerHeight());

        if (canvasPoint != null && (maxDisplay >= timeLeft))
        {
            OverlayUtil.renderTextLocation(graphics, canvasPoint, timeLeftString, timerColor);
        }
    }

}