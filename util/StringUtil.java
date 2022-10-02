package util;

import java.util.HashSet;
import java.util.Iterator;

public class StringUtil {
    //对department_name属性进行去重操作
    public static String duplicateRemoval(String inputStr){
        //分割字符串
        String[] arrayStr=inputStr.split(",");
        //利用HashSet不存在相同元素的特性进行去重
        HashSet<String> hashSet=new HashSet<String>();
        for(int i=0;i<arrayStr.length;i++){
            hashSet.add(arrayStr[i]);
        }
        //遍历HashSet
        Iterator<String> iterator=hashSet.iterator();
        String resultStr="";//去重之后的返回结果
        while(iterator.hasNext()){
            if(!resultStr.equals("")){
                resultStr+="," +iterator.next();
            }else{
                resultStr+=iterator.next();
            }
        }
        return resultStr;
    }
    //判断字符串是否为空
    //字符串为空，返回false
    //字符串不为空，返回true
    public static boolean judgeStringNull(String str){
        if(str==null || str.length()==0){
            return false;
        }else{
            return true;
        }
    }
}
