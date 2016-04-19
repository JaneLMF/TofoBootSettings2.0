package com.txbox.settings.common;

public class MboxConst {
	 static final String PREFERENCE_BOX_SETTING = "preference_box_settings";

	 static final String FREQ_DEFAULT = "";
	 static final String FREQ_SETTING = "50hz";

	 static final boolean AutoStartConfig = true;
	 static final String PROP_CEC_LANGUAGE_AUTO_SWITCH = "auto.swtich.language.by.cec";
	 static final String AutoLanguagePreferencesFile = "cec_language_preferences";

	public static final int HDMICHECK_START = 18001;
	public static final int HDMICHECK_STOP = 18002;
	public static final int HDMICHECK_UNPLUGGED = 18003;
	public static final int DISABLE_OUTOUTMODE_SETTING = 18004;
	public static final int ENABLE_OUTOUTMODE_SETTING = 18005;

	public static final int EXECUTE_ONCE = 1;
	public static final int EXECUTE_MANY = 2;
	public static final int EXECUTE_UNPLUGGED = 3;
	static final boolean   isForTopResolution = false;
	static final String[] UsualResolutions = { "1080p", "1080p50hz", "1080i",
			"1080i50hz", "720p", "720p50hz", "576p", "576i", "480p", "480i" };

	static final String ACTION_OUTPUTMODE_CHANGE = "android.intent.action.OUTPUTMODE_CHANGE";
	static final String ACTION_OUTPUTMODE_SAVE = "android.intent.action.OUTPUTMODE_SAVE";
	static final String ACTION_OUTPUTMODE_CANCEL = "android.intent.action.OUTPUTMODE_CANCEL";
	static final String OUTPUT_MODE = "output_mode";
	static final String CVBS_MODE = "cvbs_mode";
	static final String OutputStatusConfig = "/sys/class/amhdmitx/amhdmitx0/disp_cap";
	static final String HDMIStatusConfig = "/sys/class/amhdmitx/amhdmitx0/hpd_state";
	static final String CurrentResolution = "/sys/class/display/mode";
	static final String HdmiUnplugged = "/sys/class/aml_mod/mod_on";
	static final String HdmiPlugged = "/sys/class/aml_mod/mod_off";
	static final String FreescaleFb0File = "/sys/class/graphics/fb0/free_scale";
	static final String blankFb0File = "/sys/class/graphics/fb0/blank";
    static final String STR_1080SCALE = "ro.platform.has.1080scale";
    static final String VideoAxisFile = "/sys/class/video/axis";
    static final String DispFile = "/sys/class/ppmgr/disp";

    static  final String ACTION_DISP_CHANGE = "android.intent.action.DISP_CHANGE";
    static  final String ACTION_REALVIDEO_ON = "android.intent.action.REALVIDEO_ON";
    static  final String ACTION_REALVIDEO_OFF = "android.intent.action.REALVIDEO_OFF";
    static  final String ACTION_VIDEOPOSITION_CHANGE = "android.intent.action.VIDEOPOSITION_CHANGE";
    static  final String ACTION_CVBSMODE_CHANGE = "android.intent.action.CVBSMODE_CHANGE";
    
    
    // system property
    //

}
