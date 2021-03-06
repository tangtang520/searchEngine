package com.liujun.search.engine.collect.operation;

import com.liujun.search.engine.collect.operation.DocIdproc;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于测试链接与与id的对应关系
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/03/05
 */
// Junit测试顺序：@FixMethodOrder
// ** MethodSorters.DEFAULT **（默认）
// 默认顺序由方法名hashcode值来决定，如果hash值大小一致，则按名字的字典顺序确定。
// ** MethodSorters.NAME_ASCENDING （推荐） **
//   按方法名称的进行排序，由于是按字符的字典顺序，所以以这种方式指定执行顺序会始终保持一致；
// ** MethodSorters.JVM **
//    按JVM返回的方法名的顺序执行，此种方式下测试方法的执行顺序是不可预测的，即每次运行的顺序可能
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDocIdProc {

  /** 进行开始前的的打开文件操作 */
  @Before
  public void before() {
    DocIdproc.INSTANCE.openFile();
  }

  /** 测试数据放入操作 */
  @Test
  public void test01putDocId() {
    // 内容信息
    String url = "http://www.baidu.com";
    // id信息
    long id = 0;
    // 网页与对应的id的关系
    boolean rsp = DocIdproc.INSTANCE.putDoc(url, id);
    Assert.assertEquals(true, rsp);

    // 网页与对应的id的关系
    boolean rsp1 = DocIdproc.INSTANCE.putDoc("http://www.163.com", 1);
    Assert.assertEquals(true, rsp1);

    // 网页与对应的id的关系
    boolean rsp2 = DocIdproc.INSTANCE.putDoc("http://www.taobao.com", 2);
    Assert.assertEquals(true, rsp2);

    // 写入完成关闭文件
    DocIdproc.INSTANCE.close();
  }

  /** 测试数据的获取操作 */
  @Test
  public void test02putAndMatch() {
    DocIdproc.INSTANCE.openFile();
    DocIdproc.INSTANCE.putDoc("http://www.baidu.com", 100000);
    DocIdproc.INSTANCE.putDoc("http://www.163.com", 100001);
    DocIdproc.INSTANCE.putDoc("http://www.taobao.com", 100002);
    DocIdproc.INSTANCE.putDoc("http://www.qq.com", 100003);
    DocIdproc.INSTANCE.close();

    List<String> ids = new ArrayList<>(2);
    ids.add("100000");
    ids.add("100002");

    Map<String, String> machMap = DocIdproc.INSTANCE.getDoc(ids);

    System.out.println(machMap);

    Assert.assertNotNull(machMap);
  }

  /** 查找最后一个seqid */
  @Test
  public void findlasturl() {
    int maxNum = 10000;

    for (int i = 100; i < maxNum; i++) {
      // 1,插入1000个序列号
      String address = "http://" + String.valueOf(i);
      DocIdproc.INSTANCE.putDoc(address, i);
    }

    // 进行落盘操作
    DocIdproc.INSTANCE.writeDisk();

    // 提取最一个序列值
    long lastId = DocIdproc.INSTANCE.getLastHrefId();
    Assert.assertEquals(maxNum - 1, lastId);
  }
  /** 执行清理操作 */
  @After
  public void finish() {
    DocIdproc.INSTANCE.close();
  }
}
