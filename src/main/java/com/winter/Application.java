package com.winter;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Application {


    public static void main(String[] args) throws Exception {

        GlobalValue.properties = loadData();


        String resource = "mybatis.cfg.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory writeSessionFactory = new SqlSessionFactoryBuilder().build(reader,GlobalValue.properties);

        for (int i = 0; i < 5000; i++){
            List<String> list = new ArrayList<>();
            for (int j = 0; j < 20; j++){
                list.add(UUID.randomUUID().toString());
            }
            try (SqlSession session = writeSessionFactory.openSession(ExecutorType.BATCH)){
                MainMapper mapper = session.getMapper(MainMapper.class);
                for (String str : list){
                    mapper.insertBatchTestData1(str, "test1");
                }
                session.commit();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Thread.sleep(1000 * 5);
    }

    private static Properties loadData() throws Exception{
        // 读取配置文件
        Properties prop = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream( "conf.properties"));
        prop.load(in);

        //实际运作的环境配置
        String runConfFileName =  "conf-"+ prop.getProperty("runFile","dev") +".properties";

        //清空配置文件，重新加载
        prop.clear();
        in.close();
        //in = null;
        in = new BufferedInputStream(new FileInputStream(runConfFileName));
        prop.load(in);
        in.close();

        return prop;
    }
}
