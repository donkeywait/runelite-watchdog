package com.adamk33n3r.runelite.watchdog;

import com.adamk33n3r.runelite.watchdog.notifications.Overlay;

import net.runelite.api.Client;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentLinkedQueue;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class NotificationOverlay extends OverlayPanel {
    @Inject
    private Client client;
    @Inject
    private ClientUI clientUI;
    @Inject
    private WatchdogConfig config;

    private final ConcurrentLinkedQueue<OverlayNotificationData> overlayNotificationQueue = new ConcurrentLinkedQueue<>();
    static final private Dimension DEFAULT_SIZE = new Dimension(250, 60);
    static final String CLEAR = "Clear All";

    private class OverlayNotificationData extends PanelComponent {
        private final Instant timeStarted;
        private final Overlay overlayNotification;
        private final String message;

        public OverlayNotificationData(Overlay overlayNotification, String message) {
            this.overlayNotification = overlayNotification;
            this.message = message;
            this.timeStarted = Instant.now();
            this.setWrap(false);
        }

        public boolean isExpired() {
            return !this.overlayNotification.isSticky() && this.timeStarted.plus(Duration.ofSeconds(this.overlayNotification.getTimeToLive())).isBefore(Instant.now());
        }

        @Override
        public Dimension render(Graphics2D graphics) {
            this.setBackgroundColor(this.overlayNotification.getColor());
            this.getChildren().clear();
            this.getChildren().add(WrappedTitleComponent.builder()
                .text(this.message)
                .build());
            if (config.overlayShowTime()) {
                this.getChildren().add(WrappedTitleComponent.builder()
                    .text(formatDuration(ChronoUnit.MILLIS.between(this.timeStarted, Instant.now()), "m'm' s's' 'ago'"))
                    .build());
            }

            return super.render(graphics);
        }
    }

    @Inject
    public NotificationOverlay(WatchdogPlugin plugin) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.setResizable(true);
        this.setPriority(OverlayPriority.LOW);
        this.setClearChildren(true);
        this.setPreferredSize(DEFAULT_SIZE);

        this.panelComponent.setWrap(false);
        this.panelComponent.setBorder(new Rectangle(0, 0, 0, 0));
        this.panelComponent.setGap(new Point(0, 6));
        this.panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));

        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Watchdog Notification overlay"));
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY, CLEAR, "Watchdog Notification overlay"));
    }

    public void add(Overlay overlayNotification, String message) {
        this.overlayNotificationQueue.add(new OverlayNotificationData(overlayNotification, message));
    }

    public void clear() {
        this.overlayNotificationQueue.clear();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        this.setLayer(this.config.overlayLayer());
        graphics.setFont(this.config.overlayFontType().getFont());

        this.panelComponent.getChildren().add(TitleComponent.builder().text("").build());
        if (this.overlayNotificationQueue.isEmpty()) {
            return super.render(graphics);
        }

        // Keep default width
        if (getPreferredSize() == null) {
            this.setPreferredSize(DEFAULT_SIZE);
        }
        this.overlayNotificationQueue.removeIf(OverlayNotificationData::isExpired);

        while (this.overlayNotificationQueue.size() > 5) {
            this.overlayNotificationQueue.poll();
        }

        this.overlayNotificationQueue.forEach(notif -> this.panelComponent.getChildren().add(notif));

        return super.render(graphics);
    }
}
