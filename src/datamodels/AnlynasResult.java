package datamodels;

public class AnlynasResult {
	static String s0 = "餐▽饮▽服▽务▽█ █ █6█20.75█20.75█1█1.25";
	static String s1 = "朗□宇□无□刷□电□机□█A2212KV980█个█4█45.6311█182.52█3█5.48";
	static String s2 = "餐▽费▽█ █ █ █ █150█6█9";
	static String s00 = "N□上海市黄浦区福佑路8号埃力生国际大厦11层 021-33326100□7□20170313□973700006014028573□招商银行上海分行新客站支行 211681476510001□ □ □43867871250723052121□ □-37.0□□-38.25□661565721225□展讯通信（上海）有限公司□上海麦当劳食品有限公司□-57.75";
	static String s11 = "N▽深圳市南山区前海路1068号心语雅园B座1005 13923767170▽15▽20161208▽22030500DK00800▽000000▽ ▽ ▽79508914674032685135▽ ▽-279.0▽▽-284.48▽ ▽电子科技大学▽深圳市南山区国家税务局代开五十五▽-461.52▽661602519473";
	static String s22 = "N□上海市浦东新区祖冲之路2305号 021-61053137□1□20170323□973707753073798348□农行上海营口支行 03354200040023944□ □ □85008829910791343499□ □100.0□□91.0□ □展讯通信（上海）有限公司□上海群晓深山老屋餐饮有限公司□-50.0□661601584687";
	public static void main(String[] argv)
	{
		String[] StrArray1 = Split(s0,"█","▽");
		System.out.println(StrArray1[0]);
		String[] StrArray2 = Split(s1,"█","□");
		System.out.println(StrArray2[0]);
		String[] StrArray3 = Split(s2,"█","□");
		System.out.println(StrArray3[0]);
		String CheckCode1 = GetCheckCode(s00,"□");
		System.out.println(CheckCode1);
		String CheckCode2 = GetCheckCode(s11,"▽");
		System.out.println(CheckCode2);
		String CheckCode3 = GetCheckCode(s22,"▽");
		System.out.println(CheckCode3);
	}
	
	public static int ordinalIndexOf(String str, String substr, int n) {
	    int pos = str.indexOf(substr);
	    while (--n > 0 && pos != -1)
	        pos = str.indexOf(substr, pos + 1);
	    return pos;
	}
	
	public static String GetCheckCode(String str,String substr)
	{
		int indexhead = ordinalIndexOf(str,substr,4);
		int indexrear = ordinalIndexOf(str,substr,5);
		return str.substring(indexhead+1,indexrear);
	}
	
	public static int CountChar(String str)
	{
		char[] charArray = str.toCharArray();
		int Count = 0;
		for(int i = 0 ; i < charArray.length; i++)
		{
			if(charArray[i] == '█')
			Count++;
		}
		return Count;
	}
	
	public static String[] Split(String str,String substr,String replacestr)
	{
		String[] StrArray = new String[9];
		int indexhead = 0;
		int indexrear = 0;
		for(int i = 0;i < 7;i++)
		{
			indexhead = ordinalIndexOf(str,substr,i);
			indexrear = ordinalIndexOf(str,substr,i+1);
			if(indexhead != -1 && indexrear != -1)
			{
				if(i == 0)	
					StrArray[i] = str.substring(0,indexrear);
				else 
					StrArray[i] = str.substring(indexhead+1,indexrear);
			}
		}
		StrArray[7] = str.substring(indexrear+1,str.length());
		StrArray[0] = StrArray[0].replace(replacestr,"");
		return StrArray;
	}
	
	public static String Mines(String str,String minestr)
	{
		String tempstr = str.replace(minestr,"");
		return tempstr; 
	}
	
	public String[] handle(String[] infosArray) {
		String[] infosArray2 = Split(infosArray[1],"█","□");
		infosArray2[8] = infosArray[2];
		return infosArray2;
	}
	
}

