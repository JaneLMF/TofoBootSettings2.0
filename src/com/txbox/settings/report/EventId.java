package com.txbox.settings.report;


/**
 * WDK上报事件ID定义
 * @author hardyxin
 */
public class EventId {
	/**
	 * omg mta 启动上报 
	 * */
	public static final class app {
		public static final String APP_ACTION_COOL_START = "app_action_cool_start";
		public static final String APP_ACTION_START = "app_action_start";
	}

	/**
	 *  omg mta cgi上报 
	 *  */
	public static final class cgi {
		public static final String ITIL_CGI_ACTION_REQUEST = "itil_cgi_action_request";
	}

	/**
	 * 每一个apk拉起统计上报
	 *
	 */
	public static final class launch {
		public static final String APP_ACTION_LAUNCH = "app_action_launch";
	}

	/**
     * 每一个apk拉起统计上报
     *
     */
    public static final class layer {
        public static final String APP_ACTION_POWEROFF_LAYER = "app_action_poweroff_layer";
        public static final String APP_ACTION_POWEROFF_BTN = "app_action_poweroff_btn";
        public static final String APP_ACTION_REBOOT_BTN = "app_action_reboot_btn";
    }
    /**
     * 系统设置上报事件
     *
     */
    public static final class systemsetting {
    	public static final String SYSTEMSETTING_ACTION_ENTER_NETWORK_SET = "boss_app_action_setting_enter_network_set";
    	public static final String SYSTEMSETTING_ACTION_ENTER_GENERAL_SET = "boss_app_action_setting_enter_general_set";
    	public static final String SYSTEMSETTING_ACTION_ENTER_SCREEN_SET = "boss_app_action_setting_enter_screen_set";
    	public static final String SYSTEMSETTING_ACTION_ENTER_PLAY_SET = "boss_app_action_setting_enter_play_set";
    	public static final String SYSTEMSETTING_ACTION_ENTER_SYS_UPGRADE = "boss_app_action_setting_enter_sys_upgrade";
    	public static final String SYSTEMSETTING_ACTION_ENTER_SAFE_SET = "boss_app_action_setting_safe_set";
    	public static final String SYSTEMSETTING_ACTION_ENTER_FEEDBACK = "boss_app_action_setting_feedback";
    	public static final String SYSTEMSETTING_ACTION_ENTER_ABOUT = "boss_app_action_setting_enter_about";
    	public static final String SYSTEMSETTING_ACTION_NETWORK_TEST = "boss_app_action_setting_network_test";
    	public static final String SYSTEMSETTING_ACTION_NETWORK_SPEED = "boss_app_action_setting_network_speed";
    	public static final String SYSTEMSETTING_ACTION_UPGRADE_CLICK = "boss_app_action_setting_upgrade_click";
    	public static final String SYSTEMSETTING_ACTION_RESET_FACTORY = "boss_app_action_setting_reset_factory";
    }
    /**
     * 升级上报事件
     *
     */
    public static final class upgrade {
    	public static final String UPGRADE_ACTION_NEED_UPGRADE = "need_upgrade_app";
    	public static final String UPGRADE_ACTION_DOWNLOADED = "download_upgrade_apk";
    	public static final String UPGRADE_ACTION_SHOW_UPGRADE_TIP = "show_upgrade_tip";
    	public static final String UPGRADE_ACTION_START_TRIGGER_UPGRADE = "start_trigger_upgrade";
    	public static final String UPGRADE_ACTION_UPGRADE_SUCCESS = "upgrade_success_app";
    }
    
    /**
     * 登录付费上报事件
     *
     */
    public static final class login {
    	public static final String LOGIN_ACTION_LOGIN_PAGE_LOAD_FINISHED = "login_page_load_finished";
    	public static final String LOGIN_ACTION_LOGIN_SUCCEED = "login_succeed";
    	public static final String LOGIN_ACTION_CHARGEVIP_PAGE_LOAD_FINISHED = "charge_vip_page_load_finished";
    	public static final String LOGIN_ACTION_CHARGEVIP_SUCCEED = "charge_pay_vip_succeed";
    	public static final String LOGIN_ACTION_CHARGECOVER_PAGE_LOAD_FINISHED = "charge_pay_cover_page_load_finished";
    	public static final String LOGIN_ACTION_CHARGETICKET_PAGE_LOAD_FINISHED = "charge_cover_with_ticket_page_load_finished";
    	public static final String LOGIN_ACTION_CHARGECOVER_SUCCEED = "charge_pay_cover_succeed";
    }
    
    /**
     * cgi质量上报事件
     *
     */
    public static final class cgiquality {
    	public static final String CGI_ACTION_ACCESS_QUALITY = "cgi_access_quality";
    }
    /**
     * push消息上报事件
     *
     */
    public static final class pushmsg {
    	public static final String PUSH_ACTION_MSG_PROCESS = "push_msg_process";
    }
    
    /**
     * 游戏/应用上报事件
     *
     */
    public static final class game_app {
    	public static final String GAME_ACTION_DOWNLOAD_RESULT = "download_game";
    	public static final String GAME_ACTION_INSTALL_RESULT = "install_game_result";
    	public static final String APP_ACTION_DOWNLOAD_RESULT = "download_app";
    	public static final String APP_ACTION_INSTALL_RESULT = "install_app_result";
    } 
}
