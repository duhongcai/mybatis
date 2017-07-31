package com.duhc.mybatis.plugins;

import com.duhc.mybatis.entity.Pager;
import java.sql.*;
import com.mysql.jdbc.PreparedStatement;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;


import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by duhc on 2017/7/27.
 */

/**
 * 1:mybatis的执行流程
 *          1）：初始化
 *                            mybatis的初始化是从加载配置文件开始的，XmlConfigBuilder类加载mybatis-config.xml配置文件，首先读取dtd定义文档，然后根据定义文档读取内容标签，
 *                     plugins，environment，mappers映射器等。将配置文件加载处理后缓存到Configration的一个全局对象中，供后续各阶段调用，生命周期是伴随mybatis从初始化到销毁；
 *                    SqlSession的创建：Mybatis基于工厂模式提供了一个创建SqlSession对象工厂类，SqlSessionFactory，SqlSessionFactory的创建时基于build模式，
 *                   由SqlSessionFactoryBuild类创建，SqlSessionFactoryBuilder是随着mybatis的初始化创建，并利用Configration对象缓存的信息创建SqlSessionFactory，
 *                   由于创建SqlSessionFactory的过程比较复杂，不适合直接使用new方式创建。SqlSessionFactory创建成功后，SqlSessionFactoryBuilder对象就可以销毁了，
 *                   生命周期是在mybatis的初始化阶段。SqlSessionFactory是创建SqlSession的对象，SqlSession创建时需要事先初始化一个新的Executor、TrancactionIsolation、
 *                   boollean(autoCommit)生命周期从初始化到mybatis销毁。SqlSession是对数据库的一次请求，是使用频率最高的对象，为了保证业务层之间数据库调用互不影响，
 *                   业务层每次调用数据库时mybatis都会为其分配一个SqlSession，用完后回收，生命周期是一次请求过程。
 *          2）：执行
 *                              使用层面上，是使用SqlSession创建一个映射器接口的代理实现类，然后通过该类调用目标方法实现与数据库交互；底层实现上，SqlSession并没有直接与数据库交互。
 *                      SqlSession（一般是DefaultSqlSession实例）调用其getMapper方法，在该方法中使用Configration对象的getMapper方法调用MapperRegistry的getMapper方法
 *                      （Sqlsession-->configration-->MapperRegistry）
 *                      该方法中开始创建动态代理，首先根据入参（映射器接口的全限定类名）查询对应该接口的动态代理工厂对象，然后利用其newInstance方法创建动态代理类。
 *                      创建代理对象时使用的入参：当前SqlSession的对象，接口权限定类名以及缓存参数作为Proxy.newInstance的入参创建该接口的代理对象；
 *                      代理对象的invoke方法中的逻辑：首先判断入参method是否是方法，只有是方法才会执行，然后缓存该方法，并在缓存方法中创建MapperMethod，
 *                      调用MapperMethod的execute方法，根据sql类型选择相应的执行逻辑，调用相应的SqlSession的执行方法。
 *                      进到对应的执行逻辑后开始真正的执行sql语句工作：主要用到Exector、StatementHandler、ParamterHandler、ResultSetHandler四个工具类。
 *                      SqlSession为不同场景的sql提供了对应的方法，方法内并不是实际的执行逻辑，而是调用了
 *                      exector的方法，入参一般是MapperStatement、Parameter、RowBounds、ResultHandler等拼接sql语句所需的内容（query-->queryFromDatabase-->doQuery）
 *                      底层的执行层在Executor的doQuery方法中，通过Configration获取当前SqlSession需要执行的sql语句执行句柄statementhandler，通过执行句柄获取sql语句，
 *                      并调用ParamerHandler为sql语句的进行参数赋值，然后执行语句并将返回值赋值给ResultHandler
 *2：插件设计实现以及运作流程：
 *                1：自定义一个插件：
 *                      1）：自定义一个插件类继承Interceptor接口，需要重写三个方法：intercept、plugin、setProperties;声明拦截的节点
 *                      @Intercepts({@Signatur(method="",type="",args={})})
 *                                setPropeerties:获取配置文件中设置的property参数并赋值到本地变量
 *                                plugin:通过该方法我们可以返回对象本身，（不进行拦截）也可以返回一个代理对象（进行拦截）
 *                                intercept:
 *                                MetaObject meta = SystemMetaObject.forObject(ObjectHandler); 获取、修改handler中的属性
 *
 *                 2：运行流程
 *                      1）：插件初始化
 *                                  插件初始化在mybatis初始化期间，读取注册拦截器的plugins标签，遍历其中的子标签plugin，根据标签中的interceptor属性利用反射
 *                                  获取其插件定义的类并初始化对象，如果plugin标签中有property标签就读取标签值并且通过setProperty方法传递值；初始化完成以后将
 *                                  生成的拦截器对象添加到拦截器链中，保存在Configration的属性中
 *                      2）：插件运行
 *                              插件的运行是在Exector中，将对象放到拦截器链中,Plugin.wrap
 *                              factory.opsession()-->openSessionFromDataSource()-->
 *                              configration.newExecutor(tx,exec,type)-->创建Executor,最后会将exector放到interceptorChain链执行
 *  3:缓存：一级缓存和二级缓存
 *
 */

@Intercepts( {
        @Signature(type = StatementHandler.class,method="prepare",args={java.sql.Connection.class}),
        @Signature(type = ResultSetHandler.class,method = "handleResultSets",args={java.sql.Statement.class})
})
public class PageHelper implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        // 获取被拦截到的statement对象 作为处理对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //使用mybatis提供的
        MetaObject metaObject= SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String selectId = mappedStatement.getId();
        /**
         * 判断 sql是否是需要拦截的语句类型
         */
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String sql = boundSql.getSql();
        Pager pager  = (Pager) boundSql.getParameterObject();
        //重写sql
        String countSql = concatCountSql(sql) ;
        String pageSql = concatPagesql(sql,pager);

        System.out.println("重写的 count sql  ：" + countSql);
        System.out.println("重写的 select sql  ：" + pageSql);

        Connection connection = (Connection) invocation.getArgs()[0];

        PreparedStatement countStmt = null;
        ResultSet resultSet = null;
        int totalCount = 0;
        try {
            countStmt = (PreparedStatement) connection.prepareStatement(countSql);
            resultSet = countStmt.executeQuery();
            if (resultSet.next()){
                totalCount = resultSet.getInt(1);
            }
        }catch (Exception e){
            System.out.println("出错了");
        }finally {
                resultSet.close();
                countStmt.close();
        }

        metaObject.setValue("delegate.boundSql.sql",pageSql);

        pager.setCount(totalCount);

        return invocation.proceed();
    }

    public Object plugin(Object target) {
        /**
         * 校验是否需要拦截
         */
        if (target instanceof StatementHandler){
            return Plugin.wrap(target,this);
        }
        return target;
    }

    public void setProperties(Properties properties) {
    }

    private String concatCountSql(String sql){
        StringBuffer sb = new StringBuffer(" select count(*) from ");
        sql = sql.toLowerCase();
        if (sql.lastIndexOf("order") >sql.lastIndexOf(")") ){
            sb.append(sql.substring(sql.indexOf("from")+4,sql.lastIndexOf("order")));
        }else{
            sb.append(sql.substring(sql.indexOf("from")+4));
        }
        return sb.toString();
    }

    private String concatPagesql(String sql,Pager pager){
        StringBuffer sb = new StringBuffer();
        sb.append(sql);
        sb.append(" limit ").append(pager.getPagebegin()).append(" , ").append(pager.getPagesize());
        return sb.toString();
    }
}
