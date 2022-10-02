## 异常处理机制

### try-catch-finally

```java
try{
    //可能出现异常的代码
}catch(异常类型1 变量名1){//一段代码可能有多个异常类型
    //处理异常的方式1
}catch(异常类型2 变量名2){
    //处理异常的方式2
}catch(异常类型3 变量名3){
    //处理异常的方式3
}
.....
finally{
    //一定会执行的代码
}
```

### thorws

"throws + 异常类型"写在方法的声明处，指明此方法执行时，可能会抛出的异常类型。

一旦当方法体执行时，出现异常，仍会在异常代码处生成一个异常类的对象，此对象满足throws后异常类型时，就会被抛出。异常代码后续的代码，就不再执行！

```java
public class ExceptionTest2 {
	public static void main(String[] args) {
		try {
			method2();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		method3();// 在method2()里已经解决了异常，所以可以调用
	}
	public static void method3() {
		try {
			method2();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void method2() throws FileNotFoundException, IOException {
		method1();
	}
	public static void method1() throws FileNotFoundException, IOException {//异常代码
		File file = new File("hello.txt");
		FileInputStream fis = new FileInputStream(file);
		int data = fis.read();
		while (data != -1) {
			System.out.print((char) data);
			data = fis.read();
		}
		fis.close();
		System.out.println("hanhan");// 不会被执行！
	}
}
```



### try-catch和throws的区别

1、try-catch-finally是真正的将异常处理掉了，而throws是将异常抛给了方法的调用者，并没有真正将异常处理掉。当上一层仍然没有对异常进行处理时，程序继续将异常抛至更高的一层，依次下去，直到对异常进行处理或者异常被抛给JVM



### 方法重写的规则

1、子类重写的方法抛出的异常类型不大于父类被重写的方法抛出的异常类型。意味着：如果父类中没有出现异常，则子类中也不能有异常。

### 泛型

泛型提供了编译时类型安全检测机制，该机制允许程序员在编译时检测到非法的类型。泛型的本质时参数化类型，即给类型指定一个参数，然后在使用时再指定此参数具体的值，那样这个类型就可以在使用时决定了。这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。

#### 泛型的优点

所有强制转换都是自动和隐式的

1、安全性

2、消除转换

3、提升性能

4、重用性

## 序列化

 Java序列化是指把Java对象转换为字节序列的过程，而Java反序列化是指把字节序列恢复为Java对象的过程。

### 为什么需要序列化

我们知道，当两个进程进行远程通信时，可以相互发送各种类型的数据，包括文本、图片、音频、视频等， 而这些数据都会以二进制序列的形式在网络上传送。那么当两个Java进程进行通信时，能否实现进程间的对象传送呢？答案是可以的。如何做到呢？这就需要Java序列化与反序列化了。换句话说，一方面，发送方需要把这个Java对象转换为字节序列，然后在网络上传送；另一方面，接收方需要从字节序列中恢复出Java对象。

### 好处

其好处一是实现了数据的持久化，通过序列化可以把数据永久地保存到硬盘上（通常存放在文件里），二是，利用序列化实现远程通信，即在网络上传送对象的字节序列。

① 想把内存中的对象保存到一个文件中或者数据库中时候；
② 想用套接字在网络上传送对象的时候；
③ 想通过RMI传输对象的时候

1、实现Serializable接口的子类也是可以被序列化的

2、静态成员变量不能被序列化，序列化针对对象属性的，而静态成员变量是属于类的。

3、transient 标识的对象成员变量不参与序列化

### 序列化ID

可以看到，我们在进行序列化时，加了一个serialVersionUID字段，这便是序列化ID

```Java
 private static final long serialVersionUID = 1L;
```

这个序列化ID起着关键的作用，它决定着是否能够成功反序列化！java的序列化机制是通过判断运行时类的serialVersionUID来验证版本一致性的，在进行反序列化时，JVM会把传进来的字节流中的serialVersionUID与本地实体类中的serialVersionUID进行比较，如果相同则认为是一致的，便可以进行反序列化，否则就会报序列化版本不一致的异常。

即序列化ID是为了保证成功进行反序列化

#### 生成serialVersionUID

使用 AS plugin 插件就可以生成
在JDK中，可以利用 JDK 的 bin 目录下的 serialver 工具产生这个serialVersionUID，对于 Student.class，执行命令：serialver com.example.seriable.Student

#### serialVersionUID 发生改变有三种情况

1、手动去修改导致当前的 serialVersionUID 与序列化前的不一样。
2、我们根本就没有手动去写这个 serialVersionUID 常量，那么 JVM 内部会根据类结构去计算得到这个 serialVersionUID 值，在类结构发生改变时(属性增加，删除或者类型修改了)这种也是会导致 serialVersionUID 发生变化。
3、假如类结构没有发生改变，并且没有定义 serialVersionUID ，但是反序列和序列化操作的虚拟机不一样也可能导致计算出来的 serialVersionUID 不一样。

JVM 规范强烈建议我们手动声明一个版本号，这个数字可以是随机的，只要固定不变就可以。同时最好是 private 和 final 的，尽量保证不变

#### 默认的序列化ID

当我们一个实体类中没有显式的定义一个名为“serialVersionUID”、类型为long的变量时，Java序列化机制会根据编译时的class自动生成一个serialVersionUID作为序列化版本比较，这种情况下，只有同一次编译生成的class才会生成相同的serialVersionUID。譬如，当我们编写一个类时，随着时间的推移，我们因为需求改动，需要在本地类中添加其他的字段，这个时候再反序列化时便会出现serialVersionUID不一致，导致反序列化失败。那么如何解决呢？便是在本地类中添加一个“serialVersionUID”变量，值保持不变，便可以进行序列化和反序列化。

如果没有显示指定serialVersionUID，会自动生成一个。

只有同一次编译生成的class才会生成相同的serialVersionUID


但是如果出现需求变动，Bean类发生改变，则会导致反序列化失败。为了不出现这类的问题，所以我们最好还是显式的指定一个serialVersionUID。

Serializable接口内部序列化是JVM自动实现的，如果想自定义序列化过程，就可以使用Externalizable接口实现

## Java I/O

数据流分为输入流和输出流

输入输出流又分为字节流和字符流

字节流：InputStream(字节输入流)和OutputStream(字节输出流)

字符流：Reader(字符输入流)和Writer(字符输出流)

### 缓冲区

将数据临时存储在内存中

分为字节缓冲流和字符缓冲流。

对文件的操作，示例代码如下：

```java
import java.io.*;

/**
 * 实现对文件的操作
 */
public class FileUtil {
    private File file=null;//文件对象
    private Writer writer=null;//writer流
    private BufferedWriter bufferedWriter=null;//流
    //创建目录,传入需要创建的目录路径.
    //创建目录成功返回0，创建目录失败返回1，目录已存在返回2
    public int createDir(String filePath){
        file=new File(filePath);
        if(!file.exists()){//判断目录是否存在
            boolean flag=file.mkdir();//根据传入的参数，创建新的目录，前提：上级目录必须存在
            if(flag){
                System.out.println("创建目录成功");
                return 0;
            }else{
                System.out.println("创建目录失败");
                return 1;
            }
        }else{
            System.out.println(filePath+"   目录已存在，无需创建");
            return 2;
        }
    }
    //创建文件,传入文件的路径(包含文件名和文件后缀)
    //创建文件成功返回true，文件已存在返回false
    public boolean createPersonFile(String filePath){
        boolean result=true;//默认创建成功
        file=new File(filePath);
        if(!file.exists()){
            try {
               result= file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //将数据写入对应的文件中，字符数据流
    //其中filePath包含路径+文件名+文件后缀
    //data为写入文件的数据
    public void outputToFile(String filePath,String data){
        file = new File(filePath);
        if(file.exists()){
            try {
                writer=new FileWriter(file,true);
                bufferedWriter=new BufferedWriter(writer);
                bufferedWriter.write(data);//将数据写入文件
                bufferedWriter.newLine();//换行
                bufferedWriter.flush();//清空缓冲区数据
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭数据流
    public void close(){
        try {
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(writer!=null){
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```

## Java多线程

### 01 程序、进程、线程

程序是指令和数据的有序集合，其本身没有任何运行的含义，是一个静态的概念。

进程是执行程序的一次执行过程，是一个动态的概念。是系统分配资源的单位。

线程是CPU调度和执行的单位

注意：很多线程都是模拟出来的，真正的多线程是指多个CPU哦，即多核。

对同一份资源操作时，会存在资源抢夺的问题，需要加入并发控制。

线程带来的额外开销，如cpu调度时间，并发控制开销

每个线程在自己的工作内存交互，内存控制不当会造成数据不一致。

### 02 线程创建

线程开启(调用start()方法)不一定立即执行，由cpu调度执行。

1、继承Thread类

步骤：1、继承Thread类

​		    2、重写run方法

​			3、使用start()方法启动线程

```java
//示例代码如下：
public class Thread01 extends Thread{
    //子线程
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("我在学习："+i);
        }
    }
    //主线程
    public static void main(String[] args) {
        //创建线程对象
        Thread01 thread01 = new Thread01();
        //启动子线程
        thread01.start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("我在监督："+i);
        }
    }
}
```

下载网络图片，需要添加commons-io包如：commons-io-2.11.0.jar

```java
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownPhoto extends Thread{
    private String url;//图片路径
    private String name;//保存文件的地址
    public DownPhoto(){

    }
    public DownPhoto(String url,String name){
        this.url=url;
        this.name=name;
    }
    @Override
    public void run() {
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url,name);
        System.out.println("下载的文件名为："+name);
    }
    //主线程
    public static void main(String[] args) {
        DownPhoto downPhoto1 = new DownPhoto("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660879623&t=a72b2ba182a8affce2a9c2b1639318bb","E:\\1.jpg");
        DownPhoto downPhoto2 = new DownPhoto("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660879623&t=a72b2ba182a8affce2a9c2b1639318bb","E:\\2.jpg");
        DownPhoto downPhoto3 = new DownPhoto("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660879623&t=a72b2ba182a8affce2a9c2b1639318bb","E:\\3.jpg");
        downPhoto1.start();
        downPhoto2.start();
        downPhoto3.start();
    }
}
//下载器
class WebDownloader{
    //下载方法
    public void downloader(String url,String name){
        try {
            FileUtils.copyURLToFile(new URL(url),new File(name));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO异常，WebDownloader->downloader");
        }
    }
}

```

2、实现Runnable接口

步骤：1、实现Runnable接口

​			2、实现run()方法，编写线程执行体

​			3、创建线程对象，调用start()方法启动线程

```java
//示例代码如下：
public class Thread_Runnable implements Runnable{
    //子线程
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("我在努力学习："+i);
        }
    }
    //主线程
    public static void main(String[] args) {
        //创建Runnable接口实现类对象
        Thread_Runnable thread_runnable = new Thread_Runnable();
        //创建Thread对象，用于启动线程
        Thread thread = new Thread(thread_runnable);
        //启动线程
        thread.start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("我在监督："+i);
        }
    }
}
```

龟兔赛跑：乌龟一直跑，兔子跑一会儿休息一会儿

```java
/**
 * 模拟龟兔赛跑
 * 实现流程
 * 1、确定赛道距离，离终点越来越近
 * 2、判断比赛是否结束
 * 3、打印胜利者
 * 4、龟兔赛跑开始
 * 5、模拟兔子睡觉
 */
public class Race implements Runnable {
    private static String winner;//标识胜利者
    private int RACE_LENGTH=1000;//标识赛道的长度
    //子线程
    @Override
    public void run() {
        //1、确定赛道距离，离终点越来越近
        for (int i = 0; i <= RACE_LENGTH; i++) {
            //判断比赛是否结束
            if (gameOver(i)) {
                break;
            }else{
                //模拟兔子睡觉,并设置兔子每十步就睡一次觉
                if (Thread.currentThread().getName().equals("兔子") && i % 10 == 0) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "-->跑了" + i + "步");
            }

        }
        if(winner.equals(Thread.currentThread().getName())){
            System.out.println(winner + "赢得了比赛");
        }
    }

    //判断比赛是否结束,比赛结束返回true，未结束返回false
    private boolean gameOver(int steps) {
        if (winner != null) {//已经存在胜利者，比赛结束了。
            return true;
        } else {
            if (steps >= RACE_LENGTH) {
                winner = Thread.currentThread().getName();
                return true;
            }
        }
        return false;
    }

    //主线程
    public static void main(String[] args) {
        //创建Runnable接口实现类对象
        Race race = new Race();
        //创建Thread对象，用于启动线程
        new Thread(race, "兔子-------").start();
        new Thread(race, "乌龟").start();
    }
}
```

总结：通过实现Runnable接口的多线程需要使用Thread对象来启动线程。可以创建一个Runnable接口实现类对象，并将其放入多个不同的Thread对象中，相当于一个相同条件的任务，由多个处理器进行处理，处理器之间是相互独立的。

```java
//以下这种方式启动线程，runnable01中的ticket是共享的
/**
 * 模拟买票
 */
public class Runnable01 implements Runnable {
    private int ticket=100;
    @Override
    public void run() {
        while(true){
            if(ticket<=0){
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"-->拿到了"+ticket--+"票");
        }
    }
    //主线程
    public static void main(String[] args) {
        //创建Runnable接口实现类对象
        Runnable01 runnable01 = new Runnable01();
        //创建Thread对象，用于启动线程
        Thread thread1=new Thread(runnable01,"小明");
        Thread thread2=new Thread(runnable01,"老师");
        Thread thread3=new Thread(runnable01,"黄牛党");
        //启动线程
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```

3、实现Callable接口



Thread和Runnable区别

继承Thread类

- 子类继承Thread类具备多线程能力
- 启动线程：子类对象.start()
- 不建议使用：避免OPP单继承局限性

实现Runnable接口

- 实现Runnable具有多线程能力
- 启动线程：传入目标度线切割+Thread对象.start()
- 推荐使用：避免单继承局限性，灵活方便，方便同一个对象被多个线程使用



静态代理和动态代理

lambda表达式



### 03 线程停止

 * 1、建议线程都正常停止--->利用次数或者等待一会儿，不建议死循环
 * 2、建议使用标志位--->设置一个标志位
 * 3、不要使用stop或者destroy等过时或者JDK不建议使用的方法

使用标志位停止子程序代码如下：

```java
/**
 * 停止线程
 * 1、建议线程都正常停止---》利用次数，不建议死循环
 * 2、建议使用标志位---》设置一个标志位
 * 3、不要使用stop或者destroy等过时或者JDK不建议使用的方法
 *
 * 以下是通过设置标志位停止线程。
 */
public class StopThread implements Runnable{
    private boolean flag=true;
    @Override
    public void run() {
        int i=0;
        while (flag){
            System.out.println("正在执行子线程------->"+i++);
        }
    }
    //停止线程---修改flag
    public void stopThread(){
        this.flag=false;
        System.out.println("线程需要停止了");
    }
    //主线程，程序入口
    public static void main(String[] args) {
        //创建Runnable接口实现类对象
        StopThread stopThread = new StopThread();
        //创建Thread对象，并启动线程
        new Thread(stopThread).start();
        try {
            Thread.sleep(10);//设置10毫秒之后停止子线程
            stopThread.stopThread();
            System.out.println("程序运行结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 04 线程休眠

- sleep(时间)指定当前线程阻塞的毫秒数
- sleep存在异常InterruptedException
- sleep时间达到后线程进入就绪状态
- sleep可以模拟网络延时，倒计时等。
- 每个对象都有一个锁，sleep不会释放锁

```java
//使用方法
Thread.sleep(设置毫秒数);
```

### 05 线程礼让

- 礼让线程，让当前正在执行的线程暂停，但是不阻塞
- 将线程从运行状态转为就绪状态
- 让cpu重新调度，礼让不一定成功！看cpu心情

```java
//示例代码
public class TestYield implements Runnable{
    @Override
    public void run() {
        System.out.println("线程："+Thread.currentThread().getName()+"线程开始执行");
        Thread.yield();//礼让
        System.out.println("线程："+Thread.currentThread().getName()+"线程停止运行");
    }
    //主线程
    public static void main(String[] args) {
        //创建Runnable接口实现类对象
        TestYield testYield = new TestYield();
        //创建Thread对象，启动线程
        new Thread(testYield,"AAA").start();
        new Thread(testYield,"BBB").start();
    }
}
```

### 05 合并线程

Join合并线程，待此线程执行完成之后，在执行其他线程，执行过程中其他线程阻塞。有点类似插队

```java
public class TestJoin implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("我是vip"+i);
        }
    }
    //主线程
    public static void main(String[] args) {
        TestJoin testJoin=new TestJoin();
        Thread thread = new Thread(testJoin);
        thread.start();

        //
        for (int i = 0; i < 5000; i++) {
            if(i==200){
                try {
                    thread.join();//子线程插队
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("main:::::"+i);
        }
    }
}
```

### 06 线程状态观测

```java
/**
 * 测试，Thread.getState()检测线程的状态
 */
public class TestThreadState implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //主线程
    public static void main(String[] args) {
        TestThreadState testThreadState = new TestThreadState();
        Thread thread = new Thread(testThreadState,"子线程");
        Thread.State state=thread.getState();
        System.out.println(state);//NEW
        thread.start();//启动线程
        state=thread.getState();
        System.out.println(state);//RUNNABLE
        while(state!=Thread.State.TERMINATED){
            System.out.println(state);//
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            state=thread.getState();//更新state状态
        }
        System.out.println(state);
    }
}
```

### 07 线程状态转换

![](E:\FUNO_leiweichang\Java学习\线程状态.png)

### 08 线程优先级

主线程的优先级是默认的，不能修改。其中主线程和子线程的默认优先级都是5。

线程优先级高的不一定先执行，具体实际实行顺序还得看cpu具体的调度情况，但是大多数情况下是线程优先级高的先执行。

```java
/**
 * 获取和设置线程的优先级
 * setPriority设置线程的优先级
 * getPriority获取线程的优先级
 * 线程优先级的范围为1~10
 */
public class TestThreadPriority implements Runnable{
    @Override
    public void run() {
        //打印子线程的优先级
        System.out.println(Thread.currentThread().getName()+"--->"+Thread.currentThread().getPriority());
    }

    public static void main(String[] args) {
        //打印主线程的优先级，其中主线程的优先级默认是5，且不可修改
        System.out.println(Thread.currentThread().getName()+"--->"+Thread.currentThread().getPriority());
        TestThreadPriority threadPriority=new TestThreadPriority();
        Thread thread1=new Thread(threadPriority);
        Thread thread2=new Thread(threadPriority);
        Thread thread3=new Thread(threadPriority);
        Thread thread4=new Thread(threadPriority);
        Thread thread5=new Thread(threadPriority);
        Thread thread6=new Thread(threadPriority);

        //先设置线程优先级，再启动
        thread1.start();//默认优先级5
        thread2.setPriority(1);
        thread2.start();

        thread3.setPriority(4);
        thread3.start();

        thread4.setPriority(6);
        thread4.start();

        thread5.setPriority(8);
        thread5.start();

        thread6.setPriority(Thread.MAX_PRIORITY);//最高优先级，MAX_PRIORITY=10
        thread6.start();
    }
}
```

## Java Date类

1. 通过Date类来获取当前时间

   ```java
   Date date=new Date();
   SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   System.out.println(df.format(date));
   
   //分别输出年月日
   Date date = new Date();    
   String year = String.format("%tY", date);   
   String month = String.format("%tB", date);   
   String day = String.format("%te", date);    
   System.out.println("今天是："+year+"-"+month+"-"+day);
   ```

2. 通过System类中的currentTimeMillis方法获取当前时间

   ```java
   SimpleDateFormat df=new SimpledDateFormat("yyyy-MM-dd HH:mm:ss");
   System.out.println(df.format(System.currentTimeMillis));
   ```

3. 通过Calendar类来获取当前时间

   ```java
   Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   
   int year = c.get(Calendar.YEAR);  
    int month = c.get(Calendar.MONTH);   
   int date = c.get(Calendar.DATE);    
   int hour = c.get(Calendar.HOUR_OF_DAY);   
   int minute = c.get(Calendar.MINUTE);   
   int second = c.get(Calendar.SECOND);    
   System.out.println(year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second);
   ```

## Java 数据库

1. Java代码调用存储过程out类型参数

   ```java
   //程序中调用:
   cs=con.preparecall("{Call land(?,?)}");
   //注册输出参数类型
   cs.registerOutParameter(1,java.sql.Types.VARCHAR);
   cs.registerOutParameter(2,java.sql.Types.VARCHAR);
   cs.execate();
   //得到输出参数的值
   String result=cs.getString(1);
   String result=cs.getString(2);
   ```

Java Servlet

1. 原生Servlet示例，其中原生Servlet中的doGet方法是不能使用req.getReader()方法，即不能获取到json格式的数据

   ```java
   package servlet;
   
   import entity.Staff;
   import org.json.JSONObject;
   import service.StaffService;
   import service.impl.StaffServiceImpl;
   
   import javax.servlet.ServletException;
   import javax.servlet.http.HttpServlet;
   import javax.servlet.http.HttpServletRequest;
   import javax.servlet.http.HttpServletResponse;
   import java.io.BufferedReader;
   import java.io.IOException;
   import java.io.PrintWriter;
   
   //@WebServlet("/loginServlet")
   public class LoginServlet extends HttpServlet {
       private StaffService staffService=new StaffServiceImpl();
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           System.out.println("LoginServlet/doPost--->进入");
           //获取前端传递的json字符串
           BufferedReader bufferedReader=req.getReader();
           String str= bufferedReader.readLine();
           //将json字符串解析成JSONObject格式
           JSONObject jsonObject=new JSONObject(str);
           //获取参数值
           Staff staff=new Staff();
           staff.setStaff_phone(jsonObject.getString("staff_phone"));
           staff.setStaff_password(jsonObject.getString("staff_password"));
           //打印staff
           System.out.println("打印staff=="+staff);
           //调用service登录功能
           String responseResult="success";
           if(staffService.userLogin(staff)){
               responseResult="登录成功";
               System.out.println("success");
               //resp.sendRedirect("meritsStatistics.html");
           }else{
               System.out.println("账号密码错误");
               responseResult="fail";
           }
           //返回响应数据
           resp.setContentType("text/plain;charset=utf-8");
           PrintWriter printWriter=resp.getWriter();
           printWriter.write(responseResult);
           printWriter.flush();
           printWriter.close();
   
           System.out.println("LoginServlet/doPost--->结束");
       }
   }
   ```
   
2. url中添加.do用于区分请求是静态资源还是servlet，有.do标识为servlet。

   ```
   localhost:8080/login.do
   ```

Java Filter

1. JDK1.8之前的版本(含1.8)需要实现Filter中的所有方法，否则会报错

```java
package filter;


import entity.Staff;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse=(HttpServletResponse) servletResponse;
        //获取session
        HttpSession session= httpServletRequest.getSession();
        //获取请求的URL
        String path=httpServletRequest.getRequestURI();
        //无需过滤的资源
        if(path.contains("/login")||path.contains("/register")||path.contains("/css/")||path.contains("/js/")){
            filterChain.doFilter(servletRequest,servletResponse);//释放资源
            return;
        } else{
            Staff staff=(Staff) session.getAttribute("loginUser");
            if(staff!=null){
                filterChain.doFilter(servletRequest,servletResponse);//释放资源
            }else{
                //跳转到登录页面
                //servletRequest.getRequestDispatcher("/login.html").forward(servletRequest,servletResponse);
                httpServletResponse.sendRedirect("/login.html");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
```