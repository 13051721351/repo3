package hive;


import org.junit.Test;

import java.sql.*;

/**
 * @author: 张鹏飞
 * @company： 软通动力信息技术股份有限公司
 * @Official： www.isoftstone.com
 */
public class HIveDemo {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://192.168.70.110:10000/db_order";
    private static String user = "root";
    private static String password = "123456";

  static   Connection hiveconnection=null;
    static   Connection mysqlconnection=null;
  static   Statement sm=null;
    //1.获取连接对象
    static {
        //加载hive的驱动
        try {
            Class.forName(driverName);
             hiveconnection = DriverManager.getConnection(url, user, password);
            //自己补一下mysql驱动连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//创建数据库，并使用
    @Test
    public void mkDataBase(){
        try {
            sm.execute("create database ceishi");
            System.out.println("数据库创建成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //创建表，并使用
    @Test
    public void mkTable(){
        try {
            sm.execute("use  ceishi");
            sm.execute("CREATE EXTERNAL TABLE t_car(\n" +
                    "province STRING,\n" +
                    "month INT,\n" +
                    "city STRING,\n" +
                    "county STRING,\n" +
                    "year INT,\n" +
                    "cartype STRING,\n" +
                    "productor STRING,\n" +
                    "brand STRING,\n" +
                    "mold STRING,\n" +
                    "owner STRING,\n" +
                    "nature STRING,\n" +
                    "number INT,\n" +
                    "ftype STRING,\n" +
                    "outv INT,\n" +
                    "power DOUBLE,\n" +
                    "fuel STRING,\n" +
                    "length INT,\n" +
                    "width INT,\n" +
                    "height INT,\n" +
                    "xlength INT,\n" +
                    "xwidth INT,\n" +
                    "xheight INT,\n" +
                    "count INT,\n" +
                    "base INT,\n" +
                    "front INT,\n" +
                    "norm STRING,\n" +
                    "tnumber INT,\n" +
                    "total INT,\n" +
                    "curb INT,\n" +
                    "hcurb INT,\n" +
                    "passenger STRING,\n" +
                    "zhcurb INT,\n" +
                    "business STRING,\n" +
                    "dtype STRING,\n" +
                    "fmold STRING,\n" +
                    "fbusiness STRING,\n" +
                    "name STRING,\n" +
                    "age INT,\n" +
                    "sex STRING\n" +
                    ")\n" +
                    "ROW FORMAT DELIMITED\n" +
                    "FIELDS TERMINATED BY '\\t'\n" +
                    "LOCATION '/root/cars'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        try {
            ResultSet resultSet = sm.executeQuery("select province,concat(year,\"年\"),city,county,sum(number) sums\n" +
                    "from t_car\n" +
                    "group by province,year,city,county\n" +
                    "having province ='山西省' \n" +
                    "order by sums desc limit 100");
            //获取hive的查询结果
            while (resultSet.next()){
                //获取mysql的操作
                PreparedStatement ps = mysqlconnection.prepareStatement("insert into cars values (?,?,?,?,?)");
                //设置值
                ps.setString(1,resultSet.getString(1));
                ps.setString(2,resultSet.getString(2));
                ps.setString(3,resultSet.getString(3));
                ps.setString(4,resultSet.getString(4));
                ps.setInt(5,resultSet.getInt(5));
                //提交
                ps.executeUpdate();
            }
            System.out.println("成功！！！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
