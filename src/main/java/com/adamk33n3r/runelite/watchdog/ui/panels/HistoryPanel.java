package com.adamk33n3r.runelite.watchdog.ui.panels;

import com.adamk33n3r.runelite.watchdog.alerts.Alert;
import com.adamk33n3r.runelite.watchdog.notifications.IMessageNotification;
import com.adamk33n3r.runelite.watchdog.ui.PlaceholderTextField;
import com.adamk33n3r.runelite.watchdog.ui.StretchedStackedLayout;

import net.runelite.client.ui.MultiplexingPluginPanel;
import net.runelite.client.ui.PluginPanel;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import static com.adamk33n3r.runelite.watchdog.ui.panels.AlertPanel.BACK_ICON;
import static com.adamk33n3r.runelite.watchdog.ui.panels.AlertPanel.BACK_ICON_HOVER;

@Slf4j
@Singleton
public class HistoryPanel extends PluginPanel {
    private final Provider<MultiplexingPluginPanel> muxer;
    private final ScrollablePanel historyItems;
    private final List<HistoryEntryPanel> previousAlerts = new ArrayList<>();

    private static final int MAX_HISTORY_ITEMS = 100;

    @Inject
    public HistoryPanel(Provider<MultiplexingPluginPanel> muxer) {
        super(false);
        this.muxer = muxer;

        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        JButton backButton = PanelUtils.createActionButton(
            BACK_ICON,
            BACK_ICON_HOVER,
            "Back",
            (btn, modifiers) -> this.muxer.get().popState()
        );
        backButton.setPreferredSize(new Dimension(22, 16));
        backButton.setBorder(new EmptyBorder(0, 0, 0, 5));
        topPanel.add(backButton, BorderLayout.WEST);
        PlaceholderTextField filterTextField = new PlaceholderTextField();
        filterTextField.setPlaceholder("Filter");
        filterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilter(filterTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilter(filterTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter(filterTextField.getText());
            }
        });
        topPanel.add(filterTextField);
        this.add(topPanel, BorderLayout.NORTH);

        this.historyItems = new ScrollablePanel(new StretchedStackedLayout(3, 3));
        this.historyItems.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.historyItems.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
        this.historyItems.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);
        this.historyItems.setScrollableBlockIncrement(ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 10);
        JScrollPane scroll = new JScrollPane(this.historyItems, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scroll, BorderLayout.CENTER);
    }

    public void addEntry(Alert alert, String[] triggerValues) {
        HistoryEntryPanel historyEntryPanel = new HistoryEntryPanel(alert, triggerValues);
        this.previousAlerts.add(0, historyEntryPanel);
        this.historyItems.add(historyEntryPanel, 0);
        if (this.historyItems.getComponents().length > MAX_HISTORY_ITEMS) {
            this.previousAlerts.remove(this.previousAlerts.size() - 1);
            this.historyItems.remove(this.historyItems.getComponents().length - 1);
        }
        this.revalidate();
        this.repaint();
    }

    private void updateFilter(String search) {
        this.historyItems.removeAll();
        String upperSearch = search.toUpperCase();
        this.previousAlerts.stream().filter(historyEntryPanel -> {
            Alert alert = historyEntryPanel.getAlert();
            boolean alertName = alert.getName().toUpperCase().contains(upperSearch);
            boolean typeName = alert.getType().getName().toUpperCase().contains(upperSearch);
            boolean notifications = alert.getNotifications().stream().anyMatch(notification -> {
                boolean notifName = notification.getType().getName().toUpperCase().contains(upperSearch);
                boolean message = false;
                if (notification instanceof IMessageNotification) {
                    message = ((IMessageNotification) notification).getMessage().toUpperCase().contains(upperSearch);
                }

                return notifName || message;
            });
            return alertName || typeName || notifications;
        }).forEach(this.historyItems::add);
        this.revalidate();
        // Idk why I need to repaint sometimes and the PluginListPanel doesn't
        this.repaint();
    }
}
