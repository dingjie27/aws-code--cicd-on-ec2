# codebuild系列java+ec2使用指南及buildspec.yml详解
https://github.com/dingjie27/demoforcicd/blob/master/buildspec.yml
1、在codebuild中选择新建application，填写“Project name”。
2、在source的选择中，可以选择git也可以选择code commit。第一次选择git的时候会让用户登陆git账户并对codebuild授权。git中的repository是自动生成的下来框，可以从中选择具体要build哪个项目。
3、定义是否需要每次代码变动都进行build操作。

4、选择打包环境，java、nodejs、go在codebuild中都有自带的打包环境，如果有个性化需求，也可以自己上传docker基础镜像用来打包。本例中选择codebuild自带java：11镜像。在同一次build中可以同时打出jar包和docker镜像。如果需要docker要勾选priviledged选项。
同时codebuild需要有服务权限，如果是第一次使用，可以选择new一个role，如果已经使用过，可以选择以前的service role。
5、git上的文件目录结构
—file（root folder）
  -buildspec.yml
  -src
  -pom.xml
  -appspec.ym（这个是用于跟codedeploy集成的，如果单纯的只是打包，并不需要这个文件）
  -scripts/*（在codedeploy时需要用到的脚本， build时并不需要。里边是一些在部署时候用到的脚本）
    -start_server.sh
    -stop_servier.sh
    -...
6、这个地方用来指定buildspec.yml文件。如果是使用已经编写好的buildspec.yml可以选择左侧，如果没有编写可以选择右侧输入buildspec.yml中的命令。

buildspec.yml文件示例：

7、Artiifacts定义了要输出的文件放在哪里，一般是放在s3上。其中name默认是project name，path和name均可以自定义。如果需要输出打好的jar包，可以选择输出到s3.如果没有jar包，只是打出docker镜像并推到ecr，那么可以选择无arfifacts。
8、之后可以执行两个操作，一种是保留这个build，等待和codepipeline集成。一种是直接build。


#codedeploy系列-java代码打包部署到ec2
1、创建一个role，service选codedeploy，然后permission选codedeploy
参考：https://docs.aws.amazon.com/codedeploy/latest/userguide/getting-started-create-service-role.html#getting-started-create-service-role-console

2、创建一个instanceprofile，profile内容是s3的全部读权限，然后创建一个ec2的role，把这个profile attach到一个ec2的role上。
参考：https://docs.aws.amazon.com/codedeploy/latest/userguide/getting-started-create-iam-instance-profile.html
这个功能是为了让ec2可以拉取s3上的jar包，如果不想相信设置，在demo中可以直接给admin-access

3、在ec2上安装code-deploy的agent
https://docs.aws.amazon.com/codedeploy/latest/userguide/codedeploy-agent-operations-install-linux.html
检查agent状态：https://docs.aws.amazon.com/codedeploy/latest/userguide/codedeploy-agent-operations-verify.html

4、如果是java的部署，在linux2下开发时需要把java配置为全局环境，配置方式如下：
4.1 vim /etc/profile 
4.2 增加配置信息，
export JAVA_HOME=/home/ec2-user/java/jdk-11.0.2
export JRE_HOME=$JAVA_HOME/jre
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
4.3 然后source /etc/profile使之生效
4.4 的确当前终端环境下的确是生效了，但是打开新的终端会发现，怎么没有生效？是我配置没成功吗？不对呀，之前明明可以的。这个时候，你只需要执行下面操作即可。
4.5 vim ~/.bashrc 
4.6 最后一行添加 source /etc/profile
参考链接：https://blog.csdn.net/qq_35571554/article/details/82850563

5、然后根据codedelpoy的页面上的流程来即可
appspec.yml的编写脚本可以参考：https://github.com/skyisle/springboot-codedeploy-script-sample
shell脚本的编写可以参考：https://blog.csdn.net/baidu_33850454/article/details/78568392

6、异常问题的排查，
6.1 想要看日志可以参考：https://docs.aws.amazon.com/codedeploy/latest/userguide/deployments-view-logs.html
6.2 发现application stop阶段的脚本一直不更新，
