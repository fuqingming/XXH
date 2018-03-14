package com.jy.xxh.http;

/**
 * Created by HH
 * Date: 2017/11/21
 */

public class ApiStores {

    static final String urlVersion = "index/User/";
    static final String indexVersion = "index/index/";
    static final String noticeVersion = "index/notice/";
    static final String teacherVersion = "index/Teacher/";
    static final String chatVersion = "Chat/Chat/";

    /** 注册 */
    public static final String user_register = urlVersion + "register";

    /** 发送验证码 */
    public static final String user_send_yzm = urlVersion + "sendYzm";

    /** 登录 */
    public static final String user_send_login = urlVersion + "Login";

    /** banner  聊天室列表 */
    public static final String banner = indexVersion + "banner";

    /** 修改昵称 */
    public static final String changeName = urlVersion + "changeName";

    /** 修改头像 */
    public static final String changePhoto = urlVersion + "changePhoto";

    /** 忘记密码 */
    public static final String chengePwd = urlVersion + "chengePwd";

    /** 我的消息 */
    public static final String my_Notice = noticeVersion + "my_Notice";

    /** 我的关注 */
    public static final String my_Attension = teacherVersion + "my_Attension";

    /** 查询关注状态 */
    public static final String Search_attension = teacherVersion + "Search_attension";

    /** 关注 取消关注 */
    public static final String add_attension = teacherVersion + "add_attension";

    /** 导师简介 */
    public static final String info_Teach = teacherVersion + "info_Teach";

    /** 意见反馈 */
    public static final String User_Feedback = noticeVersion + "User_Feedback";

    /** 用户的发言 */
    public static final String student_say = chatVersion + "student_say";

    /**  获取聊天记录 */
    public static final String seacher_record = chatVersion + "seacher_record";
}
