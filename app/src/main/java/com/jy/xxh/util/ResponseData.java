package com.jy.xxh.util;

import com.jy.xxh.bean.base.TeacherDetailsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/2/8.
 */

public class ResponseData {
//
//
//    public static List<LiveBean> initLiveBean(){
//        List<LiveBean> arrClassOpenBean = new ArrayList<>();
//        LiveBean classOpenBean = new LiveBean(R.mipmap.yang,"直击市场热点把握投资机会 铸就财富梦想","杨导师",true,"1396人参与","123456","42273151713281");
//        arrClassOpenBean.add(classOpenBean);
//        LiveBean classOpenBean1 = new LiveBean(R.mipmap.leizhijie,"直击市场热点把握投资机会 铸就财富梦想","雷智杰导师",false,"1396人参与","","42268051439617");
//        arrClassOpenBean.add(classOpenBean1);
////        LiveBean classOpenBean2 = new LiveBean(R.mipmap.wolei,"直击市场热点把握投资机会 铸就财富梦想","吴磊导师",false,"1396人参与","","42273177927683");
//                LiveBean classOpenBean2 = new LiveBean(R.mipmap.wolei,"直击市场热点把握投资机会 铸就财富梦想","吴磊导师",false,"1396人参与","","42268051439617");
//
//        arrClassOpenBean.add(classOpenBean2);
//        LiveBean classOpenBean3 = new LiveBean(R.mipmap.yangchuntao,"直击市场热点把握投资机会 铸就财富梦想","汤春浩导师",false,"1396人参与","","42273166393345");
//        arrClassOpenBean.add(classOpenBean3);
//
//        return arrClassOpenBean;
//    }

//    public static List<MessageBean> initMessageBean(){
//        List<MessageBean> arrClassOpenBean = new ArrayList<>();
//        MessageBean classOpenBean = new MessageBean("消息标题1行","12:20","消息详细内容 不限制行数 15平台 #888888");
//        arrClassOpenBean.add(classOpenBean);
//        MessageBean classOpenBean1 = new MessageBean("消息标题1行","2018/02/15","消息详细内容 不限制行数 15pt #888888 左右间距15pt 行间距22 上间距8pt 底部15pt");
//        arrClassOpenBean.add(classOpenBean1);
//        MessageBean classOpenBean2 = new MessageBean("消息标题1行","2018/02/14","消息详细内容 不限制行数 15平台 #888888");
//        arrClassOpenBean.add(classOpenBean2);
//        MessageBean classOpenBean3 = new MessageBean("消息标题1行","2018/02/13","消息详细内容 不限制行数 15pt #888888 左右间距15pt 行间距22 上间距8pt 底部15pt");
//        arrClassOpenBean.add(classOpenBean3);
//
//        return arrClassOpenBean;
//    }

//    public static List<AttentionBean> initAttentionBean(){
//        List<AttentionBean> arrClassOpenBean = new ArrayList<>();
//        AttentionBean classOpenBean = new AttentionBean(R.mipmap.gxt_icon,"吴磊导师","消息详细内容 不限制行数 15平台 #888888");
//        arrClassOpenBean.add(classOpenBean);
//        AttentionBean classOpenBean1 = new AttentionBean(R.mipmap.gxt_icon,"雷智杰导师","消息详细内容 不限制行数 15pt #888888 左右间距15pt 行间距22 上间距8pt 底部15pt");
//        arrClassOpenBean.add(classOpenBean1);
//        AttentionBean classOpenBean2 = new AttentionBean(R.mipmap.gxt_icon,"汤春浩导师","消息详细内容 不限制行数 15平台 #888888");
//        arrClassOpenBean.add(classOpenBean2);
//        AttentionBean classOpenBean3 = new AttentionBean(R.mipmap.gxt_icon,"杨导师","消息详细内容 不限制行数 15pt #888888 左右间距15pt 行间距22 上间距8pt 底部15pt");
//        arrClassOpenBean.add(classOpenBean3);
//
//        return arrClassOpenBean;
//    }

    public static List<TeacherDetailsBean> initTeacherDetailsBean(){
        List<TeacherDetailsBean> arrClassOpenBean = new ArrayList<>();
        TeacherDetailsBean classOpenBean = new TeacherDetailsBean("导师简介","毕业于澳大利亚著名金融学府，毕业后，供职高达利亚知名投资机构Blackbird Ventures，从事全球产业发展研究工作。2014年回国，在国内私募机构研究部门任职\n" +
                "多次在金融界、和讯等国内知名财经网站发表行业分析文章，多次担任财富天下电视栏目特邀嘉宾。");
        arrClassOpenBean.add(classOpenBean);
        TeacherDetailsBean classOpenBean1 = new TeacherDetailsBean("独门战法","专注于追踪、分析中国宏观经济走势、宏观政策走向，并以此为基础深度剖析行业发展走向，晚觉优质上市公司。在多年的宏观行业研究和证券市场交易经验中，总结出独特的三部为赢交易系统，能有效敏锐捕捉到市场主流热点板块及个股。");
        arrClassOpenBean.add(classOpenBean1);


        return arrClassOpenBean;
    }

//    private void callHttpForLogin(String userPhone,String strPassword){
//        progressShow = true;
//        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
//        pd.setCanceledOnTouchOutside(false);
//        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                Log.d(TAG, "EMClient.getInstance().onCancel");
//                progressShow = false;
//            }
//        });
//        pd.setMessage(getString(R.string.Is_landing));
//        pd.show();
//
//        DemoDBManager.getInstance().closeDB();
//
//        DemoHelper.getInstance().setCurrentUserName(userPhone);
//
//        final long start = System.currentTimeMillis();
//
//        String urlDataString = "?u_telphone="+userPhone+"&pwd="+strPassword;
//        HttpClient.get(ApiStores.user_send_login + urlDataString, new HttpCallback<ResponseLoginBean>() {
//            @Override
//            public void OnSuccess(ResponseLoginBean response) {
//                Log.d("",response.toString());
//                if(response.getResult()){
//                    if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
//                        pd.dismiss();
//                    }
//
//                    // ** manually load all local groups and conversation
//                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserId, response.getContent().getInfo().getU_id());
//                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserIcon, response.getContent().getInfo().getU_photo());
//                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserTelphone, response.getContent().getInfo().getU_telphone());
//                    SPUtils.getInstance(GlobalVariables.serverSp).put(GlobalVariables.serverUserNickame, response.getContent().getInfo().getU_name());
//                    EMClient.getInstance().groupManager().loadAllGroups();
//                    EMClient.getInstance().chatManager().loadAllConversations();
//
//                    EMClient.getInstance().chatManager().loadAllConversations();
//                    DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//
//                    finish();
//                }else{
//                    Utils.showToast(LoginActivity.this,response.getMessage());
//                }
//            }
//
//            @Override
//            public void OnFailure(String message) {
//                messageCenter("错误",message);
//            }
//
//            @Override
//            public void OnRequestStart() {
//                kProgressHUD.show();
//            }
//
//            @Override
//            public void OnRequestFinish() {
//                kProgressHUD.dismiss();
//            }
//        });
//    }

}
