package com.adamk33n3r.runelite.watchdog;

import com.adamk33n3r.runelite.watchdog.alerts.FlashMode;
import com.adamk33n3r.runelite.watchdog.notifications.tts.Voice;

import net.runelite.api.SoundEffectID;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.FlashNotification;
import net.runelite.client.config.FontType;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.util.ColorUtil;

import java.awt.Color;

@ConfigGroup("watchdog")
public interface WatchdogConfig extends Config {
    String CONFIG_GROUP_NAME = "watchdog";
    Color DEFAULT_NOTIFICATION_COLOR = ColorUtil.fromHex("#46FF0000");

    // Hidden
    String ALERTS = "alerts";
    String PLUGIN_VERSION = "pluginVersion";

    // Core
    String ENABLE_TTS = "enableTTS";

    // Overhead
    String DEFAULT_OVERHEAD_DISPLAY_TIME = "defaultOverheadDisplayTime";

    // Overlay
    String OVERLAY_LAYER = "overlayLayer";
    String OVERLAY_FONT_TYPE = "overlayFontType";
    String OVERLAY_SHOW_TIME = "overlayShowTime";
    String DEFAULT_OVERLAY_STICKY = "defaultOverlaySticky";
    String DEFAULT_OVERLAY_COLOR = "defaultOverlayColor";
    String DEFAULT_OVERLAY_TTL = "defaultOverlayTTL";

    // Screen Flash
    String MOUSE_MOVEMENT_CANCELS_FLASH = "mouseMovementCancelsFlash";
    String DEFAULT_SCREEN_FLASH_COLOR = "defaultScreenFlashColor";
    String DEFAULT_SCREEN_FLASH_TYPE = "defaultScreenFlashType";
    String DEFAULT_SCREEN_FLASH_MODE = "defaultScreenFlashMode";
    String DEFAULT_SCREEN_FLASH_DURATION = "defaultScreenFlashDuration";

    // Sound
    String DEFAULT_SOUND_VOLUME = "defaultSoundVolume";
    String DEFAULT_SOUND_PATH = "defaultSoundPath";

    // Sound Effect
    String DEFAULT_SOUND_EFFECT_ID = "defaultSoundEffectID";
    String DEFAULT_SOUND_EFFECT_VOLUME = "defaultSoundEffectVolume";

    // TTS
    String DEFAULT_TTS_VOLUME = "defaultTTSVolume";
    String DEFAULT_TTS_VOICE = "defaultTTSVoice";
    String DEFAULT_TTS_RATE = "defaultTTSRate";

    //region Hidden
    @ConfigItem(
        keyName = ALERTS,
        name = "Alerts",
        description = "Serialized Alerts as a JSON string",
        hidden = true
    )
    default String alerts() { return "[]"; }

    @ConfigItem(
        keyName = PLUGIN_VERSION,
        name = "Plugin Version",
        description = "Last version of the plugin loaded",
        hidden = true
    )
    default String pluginVersion() { return null; }
    //endregion

    @ConfigItem(
        keyName = ENABLE_TTS,
        name = "Enable TTS",
        description = "Enables the TTS Notification Type",
        warning = "Using TTS will submit your IP address to a 3rd party website not controlled or verified by the RuneLite Developers."
    )
    default boolean ttsEnabled() { return false; }

    //region Overhead
    @ConfigSection(
        name = "Overhead",
        description = "The options that control the overhead notifications",
        position = 0
    )
    String overheadSection = "overheadSection";

    @ConfigItem(
        keyName = DEFAULT_OVERHEAD_DISPLAY_TIME,
        name = "Default Display Time",
        description = "The default display time",
        section = overheadSection
    )
    @Units(Units.SECONDS)
    default int defaultOverHeadDisplayTime() { return 3; };
    //endregion

    //region Overlay
    @ConfigSection(
        name = "Overlay",
        description = "The options that control the overlay notifications",
        position = 1
    )
    String overlaySection = "overlaySection";

    @ConfigItem(
        keyName = OVERLAY_LAYER,
        name = "Overlay Layer",
        description = "Which layer the overlay renders on. ABOVE_WIDGETS is default",
        section = overlaySection,
        position = 0
    )
    default OverlayLayer overlayLayer() { return OverlayLayer.ABOVE_WIDGETS; }

    @ConfigItem(
        keyName = OVERLAY_FONT_TYPE,
        name = "Overlay Font Type",
        description = "Configures which font type is used for the overlay notifications",
        section = overlaySection,
        position = 1
    )
    default FontType overlayFontType() { return FontType.BOLD; }

    @ConfigItem(
        keyName = OVERLAY_SHOW_TIME,
        name = "Overlay Show Time",
        description = "Shows how long ago the notification was fired on the overlay",
        section = overlaySection,
        position = 2
    )
    default boolean overlayShowTime() { return true; }

    @ConfigItem(
        keyName = DEFAULT_OVERLAY_STICKY,
        name = "Default Sticky",
        description = "The default sticky",
        section = overlaySection,
        position = 3
    )
    default boolean defaultOverlaySticky() { return false; }

    @ConfigItem(
        keyName = DEFAULT_OVERLAY_TTL,
        name = "Default Display Time",
        description = "The default time to display",
        section = overlaySection,
        position = 4
    )
    @Units(Units.SECONDS)
    default int defaultOverlayTTL() { return 5; }

    @ConfigItem(
        keyName = DEFAULT_OVERLAY_COLOR,
        name = "Default Color",
        description = "The default color",
        section = overlaySection,
        position = 5
    )
    @Alpha
    default Color defaultOverlayColor() { return DEFAULT_NOTIFICATION_COLOR; }
    //endregion

    //region Screen Flash
    @ConfigSection(
        name = "Screen Flash",
        description = "The options that control the screen flash notifications",
        position = 2
    )
    String screenFlashSection = "screenFlashSection";

    @ConfigItem(
        keyName = MOUSE_MOVEMENT_CANCELS_FLASH,
        name = "Mouse Movement Cancels",
        description = "Cancel the screen flash with mouse movement as well as click and keyboard",
        section = screenFlashSection,
        position = 0
    )
    default boolean mouseMovementCancels() { return true; }

    @ConfigItem(
        keyName = DEFAULT_SCREEN_FLASH_COLOR,
        name = "Default Color",
        description = "The default color",
        section = screenFlashSection,
        position = 1
    )
    @Alpha
    default Color defaultScreenFlashColor() { return DEFAULT_NOTIFICATION_COLOR; }

    @ConfigItem(
        keyName = DEFAULT_SCREEN_FLASH_TYPE,
        name = "Default Flash Type",
        description = "The default flash type",
        section = screenFlashSection,
        position = 2
    )
    default FlashNotification defaultScreenFlashType() { return FlashNotification.SOLID_TWO_SECONDS; }

    @ConfigItem(
        keyName = DEFAULT_SCREEN_FLASH_MODE,
        name = "Default Flash Mode",
        description = "The default flash mode",
        section = screenFlashSection,
        position = 3
    )
    default FlashMode defaultScreenFlashMode() { return FlashMode.FLASH; }

    @ConfigItem(
        keyName = DEFAULT_SCREEN_FLASH_DURATION,
        name = "Default Flash Duration",
        description = "The default flash duration in seconds",
        section = screenFlashSection,
        position = 3
    )
    @Units(Units.SECONDS)
    default int defaultScreenFlashDuration() { return 2; }
    //endregion

    //region Sound
    @ConfigSection(
        name = "Custom Sound",
        description = "The options that control the custom sound notifications",
        position = 3
    )
    String soundSection = "soundSection";

    @ConfigItem(
        keyName = DEFAULT_SOUND_VOLUME,
        name = "Default Volume",
        description = "The default volume",
        section = soundSection
    )
    @Range(min = 0, max = 10)
    default int defaultSoundVolume() { return 8; }

    @ConfigItem(
        keyName = DEFAULT_SOUND_PATH,
        name = "Default Path",
        description = "The default path",
        section = soundSection
    )
    default String defaultSoundPath() { return null; }
    //endregion

    //region Sound Effect
    @ConfigSection(
        name = "Sound Effect",
        description = "The options that control the custom sound notifications",
        position = 4
    )
    String soundEffectSection = "soundEffectSection";

    @ConfigItem(
        keyName = DEFAULT_SOUND_EFFECT_ID,
        name = "Default Sound Effect",
        description = "The default sound effect ID",
        section = soundEffectSection
    )
    default int defaultSoundEffectID() { return SoundEffectID.GE_ADD_OFFER_DINGALING; }

    @ConfigItem(
        keyName = DEFAULT_SOUND_EFFECT_VOLUME,
        name = "Default Volume",
        description = "The default volume",
        section = soundEffectSection
    )
    @Range(min = 0, max = 10)
    default int defaultSoundEffectVolume() { return 8; }
    //endregion

    //region TTS
    @ConfigSection(
        name = "Text to Speech",
        description = "The options that control the text to speech notifications",
        position = 5
    )
    String ttsSection = "ttsSection";

    @ConfigItem(
        keyName = DEFAULT_TTS_VOLUME,
        name = "Default Volume",
        description = "The default volume",
        section = ttsSection
    )
    @Range(min = 0, max = 10)
    default int defaultTTSVolume() { return 5; }

    @ConfigItem(
        keyName = DEFAULT_TTS_VOICE,
        name = "Default Voice",
        description = "The default voice",
        section = ttsSection
    )
    default Voice defaultTTSVoice() { return Voice.GEORGE; }

    @ConfigItem(
        keyName = DEFAULT_TTS_RATE,
        name = "Default Rate",
        description = "The default rate",
        section = ttsSection
    )
    @Range(min = 1, max = 5)
    default int defaultTTSRate() { return 1; }
    //endregion
}
