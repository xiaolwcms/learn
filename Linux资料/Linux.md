1、查看指定文件命令

命令：find / -name wtr4.war

说明：查看wtr4文件路径

![1660010835371](.\查看文件命令.png)

2、查看文件目录

命令：ls

说明：查看当前文件目录

![](.\查看文件目录.png)

3、备份文件

命令：mv wtr4.war /wtr/apache-tomcat-7.0.109/

说明：将wtr4.war文件备份到/wtr/apache-tomcat-7.0.109/文件中

![](.\移动文件到指定目录.png)

4、上传文件

命令：rz

说明：回车之后，会出现一个弹框，在弹框中选中需要上传的文件

5、启动tomcat

命令：startup.sh

说明：启动tomcat的bin中的startup.sh命令，运行tomcat服务器

![](.\启动tomcat.png)

6、查看文件上传时间

命令：ls -l 文件名

说明：查看文件最新上传时间

```linux
例子：命令：ls -l test.txt
说明：查看test.txt文件的最新上传时间
```

7、删除文件夹命令

命令：rm -rf 文件名

说明：删除文件