package com.jy.xxh.data;

import java.util.ArrayList;


/**
 * @author 付庆明
 *
 */
public class UserInfo
{
	public String m_strLoginName = "";
	public String m_strUid = "";
	public String m_strIdNum = "";			//身份证号
	public String m_strHeaderImgId = "";
	public String m_strHeaderPicUrl = "";
	public String m_strNickName = "";
	public String m_strRealName = "";
	public String m_strPreLoginTime = "";
	public String m_strGesturePwd = "";	//手势密码
	
	public String m_strBankNo = "";			// 银行账号
	public String m_strZytAccount = "";		// 中银通账号
	public String m_strZytBalance = "";		// 中银通账号余额(每次从网络获取后更新此数据)

	public String m_strMobile = "";			// 电话号码
	
	public int m_nAvailablePoints = 0;		// 可用积分
	
	public int m_payLimitAmt = 0;			// 支付限额
	
	public ArrayList<String> getInfoAsStringList()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(m_strLoginName);
		list.add(m_strUid);
		list.add(m_strIdNum);
		list.add(m_strHeaderImgId);
		list.add(m_strHeaderPicUrl);
		list.add(m_strNickName);
		list.add(m_strRealName);
		list.add(m_strPreLoginTime);
		
		list.add(m_strBankNo);
		list.add(m_strZytAccount);
		list.add(m_strZytBalance);
		
		list.add(m_strGesturePwd);
		
		list.add(m_strMobile);
		
		list.add(String.valueOf(m_nAvailablePoints));

		list.add(String.valueOf(m_payLimitAmt));
		
		return list;
	}
	
	public void setInfoFromStringList(ArrayList<String> list)
	{
		int i = 0;
		
		m_strLoginName = list.get(i++);
		m_strUid = list.get(i++);
		m_strHeaderImgId = list.get(i++);
		m_strIdNum = list.get(i++);
		m_strHeaderPicUrl = list.get(i++);
		m_strNickName = list.get(i++);
		m_strRealName = list.get(i++);
		m_strPreLoginTime = list.get(i++);	
		
		m_strBankNo = list.get(i++);
		m_strZytAccount = list.get(i++);
		m_strZytBalance = list.get(i++);
		
		m_strGesturePwd = list.get(i++);
		
		m_strMobile = list.get(i++);
		
		m_nAvailablePoints = Integer.parseInt(list.get(i++));
		
		m_payLimitAmt = Integer.parseInt(list.get(i++));
	}	
}