package com.jy.xxh.util;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;


public class Captcha 
{
//	private static final char[] CHARS =
//	{
//		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
//		'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
//		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
//		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
//	};
	
	private static final char[] CHARS =
	{
		/*'0', *//*'1',*/ '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', /*'l',*/ 'm', 
		'n', /*'o',*/ 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', /*'I',*/ 'J', 'K', 'L', 'M', 
		'N', /*'O',*/ 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};	
	
	private static Captcha s_instance;
	
	private Captcha()
	{
	};
	
	public static Captcha getInstance()
	{
		if(s_instance == null)
		{
			s_instance = new Captcha();
		}
		return s_instance;
	}
	
	private static final int DEFAULT_BG_COLOR = 0xdf;	//默认背景颜色值ֵ
	
	private static final int DEFAULT_CHAR_COUNT = 4;	// 验证码的长度  这里是4位
	private static final int DEFAULT_LINE_COUNT = 0;	// 多少条干扰线
	
	private static final boolean m_bAllowOblique = false;// 是否允许文字倾斜显示
	
	//canvas width and height
	private int m_nWidth = 0;
	private int m_nHeight = 0;
	
	//number of chars, lines; font size
	private int m_nCharCount = 0;
	private int m_nLineCount = 0;
	private int m_nFontSize = 0;
	
	private String m_strChars;	//保存生成的验证码
	private int m_nPaddingLeft = 0;
	private int m_nPaddingTop = 0;
	
	//random word space and pading_top
	private int m_nPaddingLeftBase = 0;
	private int m_nPaddingLeftRandomMax = 0;
	private int m_nPaddingTopBase = 0;
	private int m_nPaddingTopRandomMax = 0;
	
	private Random m_random = new Random();
	
	public Bitmap getBitmap(int nWidth, int nHeight, int nCharCount, int nLineCount)
	{
		m_nWidth = nWidth;
		m_nHeight = nHeight;
		m_nCharCount = nCharCount;
		m_nLineCount = nLineCount;
		
		init();
		
		return createBitmap();
	}
	
	public Bitmap getBitmap(int nWidth, int nHeight)
	{
		
		return getBitmap(nWidth, nHeight, DEFAULT_CHAR_COUNT, DEFAULT_LINE_COUNT);
	}
	
	public String getCode()
	{
		return m_strChars.toLowerCase();
	}	
	
	private Bitmap createBitmap() 
	{
		m_nPaddingLeft = 0;
		
		Bitmap bp = Bitmap.createBitmap(m_nWidth, m_nHeight, Config.ARGB_8888); 
		Canvas canvas = new Canvas(bp);

		m_strChars = createCode();
		
		canvas.drawColor(Color.rgb(DEFAULT_BG_COLOR, DEFAULT_BG_COLOR, DEFAULT_BG_COLOR));
		
		Paint paint = new Paint();
		paint.setTextSize(m_nFontSize);
		
		FontMetrics fontMetrics = paint.getFontMetrics(); 
		
		for(int i=0; i<m_strChars.length(); i++) 
		{
			randomTextStyle(paint);
			randomPadding();
			
			if(paint.getTextSkewX() > 0.1)			// 左倾
			{
				m_nPaddingLeft += m_nPaddingLeftBase;
			}
			else if(paint.getTextSkewX() < -0.1)	// 右倾
			{
				m_nPaddingLeft -= m_nPaddingLeftBase;
			}
			
			//Log.e("Captcha", m_strChars.charAt(i) + "(" + m_nPaddingLeft + " , " + m_nPaddingTop + ") " + paint.getTextSkewX());
			
			float arrWidth[] = new float[1];
			paint.getTextWidths(m_strChars.charAt(i) + "", arrWidth);
			
			if(i == m_strChars.length()-1)	// 最后一个字符
			{
				if((m_nPaddingLeft + arrWidth[0] > m_nWidth)
				|| (paint.getTextSkewX() < -0.1 && m_nPaddingLeft + arrWidth[0] + m_nPaddingLeftBase > m_nWidth))
				{
					// 生成的文字内容右边越界，重新生成
					canvas.drawColor(Color.rgb(DEFAULT_BG_COLOR, DEFAULT_BG_COLOR, DEFAULT_BG_COLOR));
					i = -1;
					m_nPaddingLeft = 0;
					
					//Log.e("Captcha", "recreate-----------------------------");
					continue;
				}
			}
			
			canvas.drawText(m_strChars.charAt(i) + "", m_nPaddingLeft, m_nPaddingTop + fontMetrics.descent, paint);
		}

		for(int i=0; i<m_nLineCount; i++) 
		{
			drawLine(canvas, paint);
		}
		
		canvas.save( Canvas.ALL_SAVE_FLAG );//保存  
		canvas.restore();
		
		return bp;
	}
	
	private String createCode() 
	{
		StringBuilder buffer = new StringBuilder(m_nCharCount);
		for(int i=0; i<m_nCharCount; i++) 
		{
			buffer.append(CHARS[m_random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}
	
	private void drawLine(Canvas canvas, Paint paint)
	{
		int nColor = randomColor(1);
		int nStartX = m_random.nextInt(m_nWidth);
		int nStartY = m_random.nextInt(m_nHeight);
		int nStopX = m_random.nextInt(m_nWidth);
		int nStopY = m_random.nextInt(m_nHeight);
		
		paint.setStrokeWidth(1);
		paint.setColor(nColor);
		canvas.drawLine(nStartX, nStartY, nStopX, nStopY, paint);
	}

	private int randomColor(int nRate)
	{
		int nRed = m_random.nextInt(256) / nRate;
		int nGreen = m_random.nextInt(256) / nRate;
		int nBlue = m_random.nextInt(256) / nRate;
		
		// 避免文字颜色和底色过于接近
		int n = (int) Math.sqrt(Math.pow(nRed-DEFAULT_BG_COLOR, 2) + Math.pow(nGreen-DEFAULT_BG_COLOR, 2) + Math.pow(nBlue-DEFAULT_BG_COLOR, 2));
		if(n < 60)
		{
			return randomColor(nRate);
		}
		
		return Color.rgb(nRed, nGreen, nBlue);
	}
	
	private void randomTextStyle(Paint paint)
	{
		int nColor = randomColor(1);
		paint.setColor(nColor);
		paint.setFakeBoldText(m_random.nextBoolean());  //true为粗体，false为非粗体
		
		if(m_bAllowOblique)
		{
			float fSkewX = m_random.nextInt(11) / 10;
			fSkewX = m_random.nextBoolean() ? fSkewX : -fSkewX;
			paint.setTextSkewX(fSkewX); //float类型参数，负数表示右斜，整数左斜
		}
		
//		paint.setUnderlineText(true); //true为下划线，false为非下划线
//		paint.setStrikeThruText(true); //true为删除线，false为非删除线
	}
	
	private void randomPadding()
	{
		int nRandomX = 0;
		if(m_nPaddingLeft == 0)
		{
			nRandomX = m_random.nextInt(m_nPaddingLeftRandomMax/2);
		}
		else
		{
			nRandomX = m_nPaddingLeftRandomMax/2 + m_random.nextInt(m_nPaddingLeftRandomMax/2);
		}

		//Log.e("Captcha", "nRandomX = " + nRandomX);
		
		m_nPaddingLeft += m_nPaddingLeftBase + nRandomX;
		m_nPaddingTop = m_nPaddingTopBase + m_random.nextInt(m_nPaddingTopRandomMax);
	}
	
	private void init()
	{
		int nFontBaseWidth = m_nWidth / (m_nCharCount + 1);
		m_nPaddingLeftBase = nFontBaseWidth / 2;
		m_nFontSize =  (int) ((nFontBaseWidth * 0.8) * 2);
		m_nPaddingLeftRandomMax = (int) Math.round(m_nFontSize / 2.0);
		
		m_nPaddingTopBase = (int) Math.round(m_nHeight * 0.6); 
		m_nPaddingTopRandomMax = (m_nHeight - m_nFontSize) / 4;
	}
}
