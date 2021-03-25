package servlet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * @author: 张鹏飞
 * @company： 西安尚观科技IT教育学院
 * @Official： www.uplookedu.com
 */
public class SecKill_redis {

    public static boolean doSecKill(String uid,String prodid) throws IOException {

        String qtKey="sk:"+prodid+":qt";  //

        String usrKey="sk:"+prodid+":usr";

        Jedis jedis =new Jedis("192.168.70.110",6379);

        //判断是否已经抢到
        if(jedis.sismember(usrKey, uid)){
            System.err.println(uid+"已抢到!");
            jedis.close();
            return false;
        }
       jedis.watch(qtKey);

        //判断剩余库存
        String qtStr = jedis.get(qtKey);

        if(qtStr==null){
            System.err.println("未初始化");
            jedis.close();
            return false;
        }
        int qt=Integer.parseInt(qtStr) ;

        if(qt<=0){
            System.err.println("已抢空！！！！！！");
            jedis.close();
            return false;
        }

        Transaction transaction= jedis.multi();

        // 减库存
        transaction.decr(qtKey);

        //加人
        transaction.sadd(usrKey, uid);

       List<Object> result = transaction.exec();

        if(result==null||result.size()==0){
            System.err.println("抢购失败！！！");
            jedis.close();
            return false;
        }


        jedis.close();

        System.out.println(uid+"秒杀成功!");
        return true;
    }


}
