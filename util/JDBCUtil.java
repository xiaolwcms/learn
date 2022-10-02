package util;

import dto.StaffDepartmentDto;

import javax.xml.crypto.Data;
import java.sql.*;
import java.text.SimpleDateFormat;

public class JDBCUtil {
    private static String url="jdbc:mysql://localhost:3306/db_database_practise";
    private static String username="root";
    private static String password="root";
    private static Connection con=null;
    private static PreparedStatement ps=null;
    private static ResultSet rs=null;
    private static CallableStatement callSt=null;
    //获取数据库连接
    public static void getConnection() {
        try {
            //1、导入驱动jar包
            //2、注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //3、获取数据库连接对象
            con = DriverManager.getConnection(url, username,  password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //使用数据代替sql语句中的占位符，并返回处理之后的SQL语句
    public static ResultSet query(String sql,Object[] params){
        //发送sql
        try {
            ps=con.prepareStatement(sql);
            //参数赋值
            for(int i=0;i< params.length;i++){
                ps.setObject(i+1,params[i]);
            }
            //执行SQL语句
            rs=ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("query语法错误");
            e.printStackTrace();
        }
        return rs;
    }
    //调用存储过程
    public static boolean callProcedure(StaffDepartmentDto staffDepartmentDto) {
        int result1=0;//调用存储过程返回的第一个参数
        int result2=0;//调用存储过程返回的第二个参数
        try {
            callSt=con.prepareCall("{call insertNewStaff(?,?,?,?,?,?,?,?,?,?)}");
            //传入参数
            callSt.setInt(1,staffDepartmentDto.getStaff_id());
            callSt.setString(2,staffDepartmentDto.getStaff_name());
            callSt.setString(3,staffDepartmentDto.getStaff_phone());
            callSt.setString(4,staffDepartmentDto.getStaff_password());
            callSt.setString(5,staffDepartmentDto.getStaff_email());
            callSt.setString(6,staffDepartmentDto.getStaff_post());
            //
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            //simpleDateFormat.format(staffDepartmentDto.getStaff_createtime());
            callSt.setDate(7,java.sql.Date.valueOf(simpleDateFormat.format(staffDepartmentDto.getStaff_createtime())));
            callSt.setInt(8,staffDepartmentDto.getDepartment_id());
            callSt.registerOutParameter(9, Types.INTEGER);
            callSt.registerOutParameter(10, Types.INTEGER);
            //执行execute
            callSt.execute();
            //获取返回值
            result1=callSt.getInt(9);
            result2=callSt.getInt(10);
            //System.out.println("result1==="+result1);
            //System.out.println("result2==="+result2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(result1==1&&result2==1){
            return true;
        }else{
            return false;
        }
    }
    //查询所有数据
    public static ResultSet selectAll(String sql){
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("selectAll查询语法错误");
            e.printStackTrace();
        }
        return rs;
    }
    //关闭数据库连接
    public static void close(){
        try {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(con!=null){
                con.close();
            }
            if(callSt!=null){
                callSt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
