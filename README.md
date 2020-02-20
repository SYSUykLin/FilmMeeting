# 传统业务
传统业务应用基本把所有的东西都集合在一起，传统应用带来的问题，**单一业务的开发和迭代困难**，这个时候牵扯到两个部分，第一是有可能只是针对用户模块增加了许多需求，其他模块没有变更，这种情况下，第一不谈开发难度，我们把所以的用户模块的内容都开发完了，在测试的时候有两种测试，一种是冒烟测试，一种是回归测试，除了要测试用户还要测试其他的。这样问他来了，如果用户有一点要修改，那么其他的测试也工作量也很大。而且用户模块修改也有可能修改公共类了。第三当我们有一种新的技术准备应用的时候，比如发现数据库是瓶颈了，想要提升系统，这个时候就可能要用到缓存了，这个时候就要用原有代码进行全量修改了，在调整一个模块的时候可能会与其他的包进行冲突。其次，**扩容困难**，现在有一个业务系统，这个业务系统包含了我们的其他模块，比如影院模块，订单模块等等，这种情况下，用户模块的并发量不大，影院的也不大，但是订单模块就不一定了，比如说内容可能是256G，订单模块不够了，要512G，但是要知道，传统业务他的内存是统一分配的，在部署一台256G内存机器，那么订单可能就只有一点点内存，所以这个时候扩容可能要1T才有可能达到要求。  **部署和回滚**，传统在部署是，比如用户模块要ES，影院模块要redis，那么部署的时候就要全部都部署上才能跑的起来，回滚就很简单了，比如现在针对订单模块的一修改，这种情况下订单模块出问题，其他模块没有问题，这个时候全部做回滚。
#微服务发展历程
很久以前就有提出过面向服务开发——SOA，在EJB的时代就提出了，然后到微服务开发。SOA，原先可能有一个大的系统，现在拆了他，拆成权限系统和用户系统，现在他们需要通信，可以用webservice，当然这个技术是很老的技术了。微服务和soa最主要就差在微字，**首先，微服务是一种将业务系统进一步拆分的架构风格，将业务系统进一步拆分的架构风格，微服务强调每一个单一业务都独立运行，比如原来有一个系统，有登录，退出等等一系列业务，这个用户底下有很多个业务模块，每一个业务模块占用一个进程，或者说一个JVM，这样就做了一个资源拆分，这个业务就是一个应用，这就是微服务强调的，资源独立，业务独立，这就有点像进程进化到线程一样，共享相同资源，但是又有自己独立的栈地址；同时每一个单一服务都应该使用更轻量级的机制保持通信。在微服务里面一般会使用更轻量级的协议而不是像webservice这样这么沉重的。而且每一个服务不强调环境，可以用不同语言或数据源，只是要求提供好的服务即可。**
#微服务核心概念
Provider：服务提供者，提供服务实现。Consumer：服务调用者，调用Provider提供服务的人。  **同一个服务可以既是Provider也可以是Consumer。** 
#环境搭建
####Spring + dubbo
在idea选择quickstart，只是服务之间的调用，完全可以满足了。
![](https://upload-images.jianshu.io/upload_images/10624272-7f40fe6815716684.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
接下来还要两个子工程，一个Provider，一个Consumer，  至少一个吧。按照相同的方法建立两个子工程。在src下面建立resource文件夹，设置成resource root文件。然后把依赖引进了，依赖这两个Provider和Consumer都需要用，所以直接引近父工程的即可，由于是spring，不是springboot，还要application.xml配置文件，所以引入application-hello.xml：
![](https://upload-images.jianshu.io/upload_images/10624272-a74aef8c209cfd66.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意第七行和第十三行就是引入了dubbo的命名空间。这个时候环境基本完成，现在就是要引入dubbo集成了。简单写下测试，在Provider里面新建立一个服务接口以及服务实现：
![](https://upload-images.jianshu.io/upload_images/10624272-d399f7ea5c54f3ce.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
public class QuickStartServiceImpl implements ServiceAPI {
    @Override
    public String sendMessage(String message) {
        return "quickstart-provider-message=" + message;
    }
}
```
实现的接口。这就是Provider要提供的一个接口，这个接口是要Consumer来调用的，然后就要在application-hello-Provider.xml里面进行配置：
```
	<dubbo:application name="hello-world-app"/>


	<!-- use dubbo protocol to export service on port 20880 -->
	<dubbo:protocol name="dubbo" port="20880"/>
	<!-- service implementation, as same as regular local bean -->
	<bean id="providerService" class="org.greenarrow.quickstart.QuickStartServiceImpl"/>
	<!-- declare the service interface to be exported -->
	<dubbo:service
			registry="N/A"
			interface="org.greenarrow.ServiceAPI"
			ref="providerService"/>
```
name就是服务的名称，也是唯一标识，protocol就是服务的地址，bean其实就是接口的实现类，service就是接口本身，通过这个接口本身去调用服务。
接着就是Consumer的配置：
```

	<dubbo:application name="demo-consumer"/>
	<!-- generate proxy for the remote service, then demoService can be used in the same way as the
    local regular interface -->
	<dubbo:reference
			id="consumerService"
			interface="org.greenarrow.ServiceAPI"
			url="dubbo://localhost:20880"
	/>
```
这里意思是装配上Provider的服务，URL为dubbo://localhost:20880，和
```
<dubbo:protocol name="dubbo" port="20880"/>
```
相对应，可能serviceAPI会报错，但是创建一个就好了。
```
public class ConsumerClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-hello-consumer.xml");
        context.start();
        while (true){
            Scanner scanner = new Scanner(System.in);
            String message = scanner.next();
            //获取接口
            ServiceAPI serviceAPI = (ServiceAPI)context.getBean("consumerService");
            System.out.println(serviceAPI.sendMessage(message));
        }
    }
}

```
启动的时候两个都要启动。运行的时候遇到一个问题：会发现dubbo://localhost:20880找不到，显示这个服务已经关闭，调了好久，然后重启一下idea，完了好了，啥事都没有。![](https://upload-images.jianshu.io/upload_images/10624272-e819f5554c88748d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)为什么会出现这个问题，看到网上大多数问题都是ZX没有注册或者是内外网的问题，所以不了了之了。其实整个流程是这样：Consumer先在xml里面配置得到了Provider的bean，quickstart，然后用Provider的这个服务给自己返回了一个消息，再输出。
####SpringBoot + dubbo
这个就比较简单了，idea就自带了spring initial，直接建立即可，不需要任何依赖。![](https://upload-images.jianshu.io/upload_images/10624272-06fc9a66192e6f48.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)这就是基本目录。和spring是一样的，只不过把大部分配置变成了注解。接口按照spring的一样，但是实现类需要把配置文件的信息变成注解：
![](https://upload-images.jianshu.io/upload_images/10624272-2764bdcd1008a186.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意Service不要倒错包。然后在Provider启动类加上另外一个注解：
![](https://upload-images.jianshu.io/upload_images/10624272-e546b43f61cd630c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这就是Provider的编码，Consumer就简单许多了：
![](https://upload-images.jianshu.io/upload_images/10624272-dd4e7a8d4b161b38.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意ServiceAPI的路径在Provider和Consumer的路径要一样，刚刚就犯了这个错误。![Provider](https://upload-images.jianshu.io/upload_images/10624272-fefac01e45decefd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![Consumer](https://upload-images.jianshu.io/upload_images/10624272-856343899d57b5d7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
路径要一样。
结果：
![](https://upload-images.jianshu.io/upload_images/10624272-efb7370568925a3c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**还是出现是配置spring+dubbo的问题，超时问题，这个问题是不定时出现的，上一次的配置是重启了idea就好了，这次是重启电脑好的。怀疑的电脑配置问题导致的超时问题，因为dubbo默认是1000ms就会报超时，于是把他调到了50000ms，可能与内存电脑配置有关，具体原因尚未知。**
#Zookeeper
上面这种就是直连提供者了，要求Consumer知道Provider的服务地址，直接找到服务，这种方式太固定了，不利于扩展。所以延伸出了用一个注册中心来注册所有的服务，zookeeper就是这样一个注册中心。
![](https://upload-images.jianshu.io/upload_images/10624272-291952d78eaa0908.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
invoke就所用的直连提供者。安装zookeeper还是很简单的，下载下来解压即可，然后进入bin，./zkServer.sh start运行即可。![](https://upload-images.jianshu.io/upload_images/10624272-5330a0fa127cedb6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在spring+dubbo中的配置比较简单，加上几个依赖即可，把registry=N/A改成zookeeper://localhost:2181即可，N/A就是什么都不用。
springboot+dubbo配置需要在父工程加上依赖：
![](https://upload-images.jianshu.io/upload_images/10624272-5934c3ecc9a6efdd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意子工程的parent要改成父工程，否则依赖是无法引入的。
父工程加上：
![](https://upload-images.jianshu.io/upload_images/10624272-625b135c0bc3d620.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)表明这两个是parent的儿子。
![](https://upload-images.jianshu.io/upload_images/10624272-edc8adeb23620ddb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
子工程加上，表面父母坐标，这样才能继承依赖。
springboot的依赖最好不要加在子工程上，会出现日志冲突。接着和原来一样：
```
spring.application.name=dubbo-spring-boot-starter
spring.dubbo.server=true
spring.dubbo.registry=zookeeper://localhost:2181

```
N/A去掉，接上服务器的位置。Provider就改完了。Consumer也是改一样的地方，把注解改了：
```
@Component
public class QuickstartConsumer  {
    @Reference(interfaceClass = ServiceAPI.class)
    private ServiceAPI serviceAPI;

    public void sendMessage(String message){
        System.out.println(serviceAPI.sendMessage(message));
    }
}

```
**还有一个需要注意的问题，springboot的application只会默认扫描同路径下或者是子路径的包：**
![](https://upload-images.jianshu.io/upload_images/10624272-76e4b12e9d545a96.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
application在Provider下面，那么它只会扫描和她同级的包和Provider的子路径，所以impl放在quickstart放在了Provider下面是可以扫描到的，如果把implement放在dubbo就扫码不到了，这个时候就要加注解：
```
@SpringBootApplication(scanBasePackages = "com.greenarrow.springboot.dubbo")

```
就会从dubbo这个包下面开始扫描。
#构建业务环境
#####API网关
首先**介绍**一下API网关，API网关有点像设计模式中的Facade模式，比如现在有很多个服务，用户服务，产品服务，订单服务，如果一个网站，想要访问产品服务，这个时候可能就要检查是不是登录了，或者权限等等，那么就要先访问用户服务看看你登录了没有，登录了，才访问产品，但是这样会带来问题，因为客户端本身不安全，所以微服务这块对外相当于是暴露了，都知道是什么参数了，对外来说相当于是透明，所以安全比较难做；其次，在提交订单的时候要做三步操作，检查登录，产品是否足够，下订单，其实是在点击下订单的时候，这三个步骤要一起完成，而这三个操作是三个不同的微服务，有顺序性的了，所以时间长。打个比方，现在想看新闻，一会儿想看搜狐新闻，一会儿想看头条新闻，每次都要输入就很麻烦，那么这个时候就会出现一个公共网站，比如hao123，这些网站，既可以看到搜狐，也可以看到头条新闻，其他的新闻网站对于我们老师都是透明的，甚至有时候新闻来源都是不知道的。介于这种情况，就会出现一个网关的东西，gateway，这东西就相当于后台服务与前端客户端的一个接口，那么至于怎么访问，异步同步等等前端都不需要管。都只需要面向一个接口，其他都只是后端的，**所以API网关就相当于是微服务中的一个门面。**
![](https://upload-images.jianshu.io/upload_images/10624272-9db1a79035c5156e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
API网关**作用**：既然已经充当门面了，首要就是需要验证你身份合不合格了，就相当于一个防火墙；审查与监察，网关有一个比较特殊的作用，类似于之前的拦截器，审查和分发，回来还要经过网关，再返回。这种情况下，就可以把边缘信息统计一下，比如执行时间，调用了啥服务，响应时间等等。其次还可以做动态路由，dubbo做好了，但是springcloud没有，springcloud需要处理。压力测试也有可能，一般是接替测试，负载均衡，静态响应分离。整个业务结构大概就是**客户端访问API网关（服务聚合，熔断降级，身份安全），然后网关再把请求分发下去。**
#guns环境搭建
guns这里使用还是v3.1，现在已经到了v6了，很早之前就下载过3.1版本，懒得换了。直接启动会有一些错误：
![](https://upload-images.jianshu.io/upload_images/10624272-4e34cef3022d1b00.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
首先是log4j问题，下一个包就好了。
![](https://upload-images.jianshu.io/upload_images/10624272-1939d5d2dbee178d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这个是时区的问题，另外他也没有识别出关键字zeroDateTimeBehavior，但是查了一下这个确实是MySQL的一个参数，也不知道为什么，修改一下连接路径：
```
      url: jdbc:mysql://127.0.0.1:3306/guns_rest?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8

```
去掉即可。
![](https://upload-images.jianshu.io/upload_images/10624272-0c178ee23b75aa1b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
admin是主要操作，也是主要业务，core是核心实现，generator是代码生成，rest是连接数据库的。原来还有一个parent，我把他搞在外面了。启动guns-rest，在application.yml里面有一个auth-path，就是路径，[http://localhost/auth?userName=admin&password=admin](http://localhost/auth?userName=admin&password=admin)访问，默认账号密码admin（这个要看官网，我自己改的），出现：
![](https://upload-images.jianshu.io/upload_images/10624272-996f60fe1472c924.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
就表示成功了。接下来就是配置dubbo环境了，和springboot一样，加依赖即可，使用guns-rest作为后端模板往常网关，所以直接复制一个就好了，对于zookeeper这些和原来的一样，启动之后./zkServer.sh start，./zkCli.sh打开客户端查看zookeeper注册符号：
![](https://upload-images.jianshu.io/upload_images/10624272-242c12750be0515b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到当前服务已经注册进去了。那么环境基本就是这样了，剩下就是业务开发了。 
#抽离业务接口
就我们现在的工程，每一个模块一个实现类就一个接口，在微服务中这些接口各个工程都要有，就如前面实现的spring/springboot+dubbo直连一样，serviceAPI各个工程都要有，provide要有，Consumer也要有，很麻烦。如果是这样的话，为什么不可以把这些接口全部独立出来，做成一个工程呢？然后把实现类也扔到一个子工程里面，然后就当成是一个依赖一样引入pom.xml文件中即可。所以复制一份guns-core改名，在project structure改名，可能会出现can not contain source root这些错误，这个时候要把原来的module的重复source root删除：
![](https://upload-images.jianshu.io/upload_images/10624272-7e85fb623d261497.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
复制完成后右边那一列（Add Content Root）会出现两个source root 删除一个即可change name了。之前测试使用的UserAPI移到这里。
![](https://upload-images.jianshu.io/upload_images/10624272-93f8cd77d7336842.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
打包这个模块，maven里面lifecycle install，打包完成之后引入到guns-gateway做测试：
```
@Component
@Service(interfaceClass = UserAPI.class)
public class UserImpl implements UserAPI {
    @Override
    public boolean login(String userName, String password) {
        return true;
    }
}

```
这里的UserAPI导入guns-api里面即可。
![](https://upload-images.jianshu.io/upload_images/10624272-933947dbe759efa0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到，这个时候注册的又是guns.api.user.UserAPI的接口了。
#Dubbo调用流程
dubbo有两种调用方式，直连提供者和注册中心，两种都在刚刚的环境搭建中就简单测试过了。**首先是直连提供者：在开发和测试环境，常常是需要绕过注册中心，直接指定提供者是谁，其实就是点对点直连，类似数据链路层吧，如果需要动态扩容，每一个地址都要让Consumer知道，缺点就是全写在代码里面了，写的好点的可以扔在配置文件，但是都要重新启动，所以这种方式仅仅用于开发和测试，如果使用注册中心，就简单许多了。基于注册中心：首先先要有Provider，dubbo提供了一个容器，用这个容器来装载Provider，当启动系统时，这个Provider就会在注册中心留下地址，其实就是发现服务的过程，也叫register；而Consumer也会subscribe一下注册中心，把服务地址下载下来，同时注册中心一有变化就notify Consumer，Consumer又更新一次，dubbo本身就有moniter，用于监控检测等等**
#dubbo多协议
dubbo默认支持阿里开源的dubbo协议，同时也支持rmi，hessian等等协议。**Dubbo协议特点： 传入传出参数数据包较小（建议小于100K），消费者比提供者个数多，单一消费者无法压满提供者，尽量不要用dubbo协议传输大文件或超大字符串，基于以上描述，我们一般建议Dubbo用于小数据量大并发的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况。
RMI协议特点： 传入传出参数数据包大小混合，消费者与提供者个数差不多，可传文件。基于以上描述，我们一般对传输管道和效率没有那么高的要求，同时又有传输文件这一类的要求时，可以尝试采用RMI协议。
Hessian协议特点： 传入传出参数数据包大小混合，提供者比消费者个数多，可用浏览器查看，可用表单或URL传入参数，暂不支持传文件。比较适用于需同时给应用程序和浏览器JS使用的服务，Hessian协议的相关内容与HTTP基本差不多，这里就不再赘述了。
WebService协议特点： 基于CXF的frontend-simple和transports-http实现，适用于系统集成，跨语言调用。 不过如非必要，强烈不推荐使用这个方式，WebService是一个相对比较重的协议传输类型，无论从性能、效率和安全性上都不太能满足微服务的需要，如果确实存在异构系统的调用，建议可以采用其他的形式。http协议也支持。**
#用户模块开发
#####JWT验证
远离Java太久了，之前使用的方式都是使用cookie+session，或者用上sso单点登录吧，jwt有点不一样，不需要存储session，用户从客户端输入账号密码访问服务器，服务器给他个token，结构如下图所示：
![](https://upload-images.jianshu.io/upload_images/10624272-39f0baacef256887.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
header是加密方式，就是用什么方式加密把，payload就是承载信息了，用户还是管理员等等，签名就是利用前面两个信息进行两次哈希加密得到的。然后每一次客户端就带着这个token，服务器只需要验证这个token是不是正确的就好了。
还是先要建表：
![](https://upload-images.jianshu.io/upload_images/10624272-d9c84523177be371.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)user_t的字段属性。guns_gateway基本已经完成了，只需要相互调通即可。重新复制一个guns-gateway修改名字为guns-user，注意是gateway这个门户去调用服务，所以这两个东西都要启动起来，所以端口肯定要不一样，所以dubbo端口要变一下，而且权限验证jwt这是在gateway门户做的，通过之后再跑到gate-user调用服务。所以端口和jwt要设置一下：
![](https://upload-images.jianshu.io/upload_images/10624272-acfcac9ffe782aca.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/10624272-e06db2c26e203152.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
启动测试一下。
现在的流程是，客户端调用服务，是直接调用gateway门户里面的api，gateway再根据各种需求调用后面各种模块的服务，测试一把。
登录进去一定会调用jwt做验证，就直接在jwt上面做测试吧。
```
@Component
@Service(interfaceClass = UserAPI.class)
public class UserImpl implements UserAPI {
    @Override
    public boolean login(String userName, String password) {
        System.out.println("this is user service!" + userName + " " + password);
        return false;
    }
}

```
这是guns-users模块的服务，也就是gateway要调用的服务，如果能打印出来语句就OK了。
![](https://upload-images.jianshu.io/upload_images/10624272-3776f2670396cb3e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在gateway的auth的controller加上测试。
![](https://upload-images.jianshu.io/upload_images/10624272-1fb8387752a72b65.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意，打印是打印在UserApplication里面，因为这个服务是在user模块完成的。出现打印那么说明互相调用就OK了。
#配置可以忽略的URL
有些URL是可以忽略的，比如注册，/user/register，登录，/user/login，这些很明显是不需要的，所以还是需要配置一下。首先理解一下guns的jwt：
![](https://upload-images.jianshu.io/upload_images/10624272-2f69b84c0b7dcfcd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
gateway里面的配置文件有一项就是jwt，在springboot中有一项就是要把其内容全部读进去。![](https://upload-images.jianshu.io/upload_images/10624272-d2f5dca707aa4835.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这是属于springboot的注解配置，configuration就是读取在yml配置文件所有前缀是JWT_PREFIX，下面配置了JWT_PREFIX = "jwt"，就是读取yml中jwt:中的内容。所以在yml配置文件jwt中我配置了ignore-url，作为忽略的URL，那么在JwrProperties就要读进来了。![](https://upload-images.jianshu.io/upload_images/10624272-99d9539ca73344bc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**注意在springboot中，读取配置这里默认会把-u变成大写U，ignore-url就变成ignoreUrl，读取进来记得加上getset方法。**既然添加完路径了，首要就是做处理了，处理这种东西肯定是在读取进来的权限做限制，那么在module auth filter里面修改即可，有一个AuthFilter：
![](https://upload-images.jianshu.io/upload_images/10624272-72a8004528a1d41b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里的chain.doFilter是按照链式过滤的意思，如果多个filter，那么按照filter1->filter2->filter3......以此类推，但是下面没有其他的filter了，所以直接返回页面，所以这里也代表着直接通过的意思。那么仿照它把ignore-url路径加上：
![](https://upload-images.jianshu.io/upload_images/10624272-ee3a19dad54862d6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这样就配置好。
#用户模块API以及相应的类
用户模块api肯定是添加在guns-api这块，在添加两个类，一个类是UserModel，这是用户注册的类，这个类是不能被修改的，仅仅作为注册使用，因为注册的内容有一些敏感内容，所以需要一个新类，也就是UserInfoModel作为真正的可以被修改的类：
```
public class UserInfoModel {
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private int sex;
    private String birthday;
    private String liftState;
    private String biography;
    private String address;
    private String headAddress;
    private Long beginTime;
    private Long updateTime;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLiftState() {
        return liftState;
    }

    public void setLiftState(String liftState) {
        this.liftState = liftState;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadAddress() {
        return headAddress;
    }

    public void setHeadAddress(String headAddress) {
        this.headAddress = headAddress;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}

```
按照返回格式来修改登录之后要返回的数据：
![](https://upload-images.jianshu.io/upload_images/10624272-0acd66fd7f894cdf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
修改api中的返回数据，然后让其返回用户的uid，因为返回的token使用的就是用户的UID来构建，构建一个返回model，泛型使用M，因为这里的data是其他类型：
![](https://upload-images.jianshu.io/upload_images/10624272-9dd911bb5f60c734.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
然后在AuthController修改一下返回类型和内容即可。
#用户信息保存
ThredLocal用户信息保存的方法代替把信息保存到session中，threadlocal每一个线程分开使用，不同线程的threadlocal不一样，好比线程之间的栈地址不可共享，同一个进程的线程资源与空间可以共享。可以使用threadlocal直接保存用户信息，也可以只保存UID或者某些关键信息，这里使用保存UID的方法：
![](https://upload-images.jianshu.io/upload_images/10624272-02e6ed1ab531ed48.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
有了方法还得维护他，每一次登陆或者是注册完之后，都会发放一个jwt，每一次客户端进入页面带来点jwt中也有uid，也要把UID拿出来放在threadlocal中，也就是再filter那里，在登陆进来，访问URL进来的时候都需要进过的一个过滤器，在AuthFilter里面修改：
![](https://upload-images.jianshu.io/upload_images/10624272-9bec6229a252779d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里使用getUsernameFromToken是因为在AuthController中我们保存的就是uid，只不过uid替代了username这个位置。那么用户的jwt就修改完成了。既然都改完了，做一个测试吧！
首先准备一个控制器：
![](https://upload-images.jianshu.io/upload_images/10624272-797e92e084040cdf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
如果能通过这个控制器验证成功就可以打印出uid，返回请求成功。
回看一下请求流程，首先登陆，登陆成功就会返回一个uid，每一次客户端就会拿着这个Uid组成的token登陆，查看一下filter的AuthController：
![](https://upload-images.jianshu.io/upload_images/10624272-78b7c035ae3b186d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
显示把前面7个固定字符取出来，然后再验证后面的token，使用postman测试：![](https://upload-images.jianshu.io/upload_images/10624272-ed50d0319947e4ea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
先登陆得到token，然后用token登陆，注意token在header里面：
![](https://upload-images.jianshu.io/upload_images/10624272-ee02091740fc1600.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
测试的时候还是遇到了一些问题，一开始启动不了gateway，后来尝试了很多遍，把gateway的target删掉就可以运行了。既然都已经调通了，那么开始把数据层对接上吧，启用代码生成工具直接生成就好了。
额，直接生成就好了，guns这个框架很快的。
![](https://upload-images.jianshu.io/upload_images/10624272-6add4d644524eada.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意区别一下目前存在的三个模型:
![](https://upload-images.jianshu.io/upload_images/10624272-8bbcb2fa276144d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
UserModel和UserInfoModel都是模型之间交互的，但是区别就在于UserModel只是用于注册，而UserInfoModel是用于各模块之间的交互以及修改，UserT就是guns-user自用的Dao层而已，不会跨越模块。然后就实现各种服务了，这个很简单，没得说，注意密码不能明文存储即可。**需要注意的主要就是用户退出这个功能，一般会把用户的信息存两份，前端先存jwt，一般存7天，在这种情况下就会存在一个问题，jwt的刷新；那么这个时候后端就起着刷新作用，服务端就存储活动用户信息，一般30分钟，如果在30分钟之内能查到用户，那么就认为是活跃用户，如果没有，哪怕你有jwt也认为你需要重新登录，所以logout要做两件事，首先删除前端jwt，然后删除后端活动缓存即可。**
接着就是测试了，遇到一个很牛逼的bug：
![](https://upload-images.jianshu.io/upload_images/10624272-7b2ebfc66904e026.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**这个问题吧，就是前面提到好几次的问题，我当时解决对了一半，确实是机器问题，但是不是性能超时时延的问题，而是WiFi问题，说我信息发不出去，通道关闭了，那就是链路问题，但是我ping了一下127.0.0.1，可以通，那么tcp/ip就没有问题了，ping了一下另外一台电脑，可以通，那么网卡或者说网关就没有问题了，然后看到网上很多人说WiFi问题，然后我把WiFi关了，然后就可以通了。不过打开WiFi在测试的过程中还是有某几次是可以连上的，但关闭WiFi就一定可以连上。关键是他这个错误，也就是cause：message can not be send,channal is closed.这个错误不是一下就提出来了，还是我把check=false设置了之后才出现。**接着就是测试接口了，注意一些model里面的值最好使用对象，比如使用Integer或者String对象，不要使用int这样的，因为有可能会出现null值。
**测试完成后，基本上用户模块差不多了，但是现在还有一个小问题，就是启动的时候必须要有顺序，要不然gateway会找不到服务，其次还有负载均衡的问题。启动顺序那个就是check=false的问题，负载均衡策略dubbo有四种，Random，按权重设置随机概率，RoundRobin，按公约后的权重设置轮循比率，LeastActive，最少活跃调度数，如果活跃数相同，那么随机，不同就按照排序，ConsistentHash，相同参数就到同一个提供者，不同参数到另外一个。一般多用轮循，但是有可能受到机器影响，如果三台机器的效率并不相同，如果第三个请求到了第三台机器，但是第三台机器炸毛了，没有返回，那么就一直卡在这，因为轮循第三个request一定是到第三个，但是第三个一直不能返回，就造成了dubbo的雪崩。**负载均衡这里有两种配置方式：
![](https://upload-images.jianshu.io/upload_images/10624272-a34cca7f1b24dc11.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
客户端端配置意思是访问服务提供者是一种什么形式，而服务端服务是所有的客户端访问这个服务的访问形式是什么样的，简单点说，就是客户端级别就是当前这个客户端访问是怎么来的，其他客户端没有影响，而服务端是影响到了所有客户端，所以在Impl配上roundrobin就好了。
**dubbo的多协议之前提到过，简单再提一下，dubbo支持多协议中，最主要的区别就是链接方式，dubbo协议建立的是长链接，一旦建立就会建立一个管道，不需要每一次都要进行建立，类似HTTP的长短链接，但是dubbo本身不是一种协议，只是封装了TCP，然后在TCP的基础上变成了dubbo这个协议，那么dubbo的传输协议就是TCP了，另外，dubbo用到是NIO的异步传输。**
#影片模块
影片模块有点复杂，数据库设计也有点多，首页内容比较多，首页每一个人都是一样的，所以直接搞了一个数据库表给他，这样直接取出来就好了，首页需要实现：
![](https://upload-images.jianshu.io/upload_images/10624272-124bac32f352b2bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这么多功能需要用一个功能完成，这就是网关的**功能聚合**，前端只调用一次接口，全部加载出来，不需要这么多次HTTP请求。
![](https://upload-images.jianshu.io/upload_images/10624272-07f291870b632870.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
actor_t是演员表，banner是首页图片的表，其实就是滑动窗口的图片，cat_dict_t就是字典了，分类字典，比如悬疑，犯罪，动作，爱情等等，film_info电影信息表，这个表存储电影的少量核心信息，这类信息是经常要使用的，信息量不大，可以加快查询速度；film_t就是电影信息全的表了，所有信息都在里面了，接下来就来源表，年份表。这些表都没有进行外键关联。
```
@Data
public class FilmindexVO {
    private List<BannerVO> banners;
    private FilmVO hotFilms;
    private FilmVO soonFilms;
    private List<FilmInfo> boxRanking;
    private List<FilmInfo> expectRanking;
    private List<FilmInfo> top100;
}

```
前端准备要返回的模型，通过一个接口全部装进去。
```
public interface FilmServiceAPI {
    //get banners info
    List<BannerVO> getBanners();
    //get hot films
    FilmVO getHotFilms(boolean isLimit, Integer nums);
    //get films displayed soon
    FilmVO getSoonFilms(boolean isLimit, Integer nums);
    //get boxRanking
    List<FilmInfo> getBoxRanking();
    //population Ranking
    List<FilmInfo> getExpectRanking();
    //get top
    List<FilmInfo> getTop();

}

```
要实现的接口。里面很多交互的模型都有共同点，直接使用同一个模型即可。实现网关里面的首页控制器，别忘了把前端地址加到ignore_url上面：
```
    @RequestMapping(value = "getIndex", method = RequestMethod.GET)
    public ResponseVO getIndex() {

        /**
         * banner信息
         * 正在热映影片
         * 即将上映
         * 票房排行
         * 人气榜单
         * 前100
         */

        FilmindexVO filmindexVO = new FilmindexVO();
        filmindexVO.setBanners(filmServiceAPI.getBanners());
        filmindexVO.setHotFilms(filmServiceAPI.getHotFilms(true, 8));
        filmindexVO.setSoonFilms(filmServiceAPI.getSoonFilms(true, 8));
        filmindexVO.setBoxRanking(filmServiceAPI.getBoxRanking());
        filmindexVO.setExpectRanking(filmServiceAPI.getExpectRanking());
        filmindexVO.setTop100(filmServiceAPI.getTop());
        return ResponseVO.success(IMG_PRE, filmindexVO);
    }

```
很简单，就是装进去直接返回即可。测试一下：
![](https://upload-images.jianshu.io/upload_images/10624272-9fc088ded68a35d7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
写到这里基本调通了。接下来就是快速的业务开发。业务开发这块复杂点的其实也就按条件查询这里有点复杂，其他还算OK。类似于电影天堂里面那些按照国籍查询，按照演员查询等等：
![](https://upload-images.jianshu.io/upload_images/10624272-c66d25f4330c1362.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)前端接口如此，isActive就是是否是选中的，如果是选中就是true，其他的就是false了，前端会返回选中的分类，接着后端会对比选中的分类，选中的返回true，其他返回false，如果是'全选'，标识是99：
```
    @RequestMapping(value = "getConditionList", method = RequestMethod.GET)
    public ResponseVO getConditionList(@RequestParam(name = "catId", required = false, defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId", required = false, defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId", required = false, defaultValue = "99") String yearId) {

        boolean flag = false;
        FilmConditionVO filmConditionVO = new FilmConditionVO();
        List<CatVO> cats = filmServiceAPI.getCats();
        List<CatVO> catResult = new ArrayList<>();
        CatVO cat = new CatVO();
        for (CatVO catVO : cats) {
            if (catVO.getCatId().equals("99")) {
                cat = catVO;
                continue;
            }
            if (catVO.getCatId().equals(catId)) {
                flag = true;
                catVO.setActive(true);
            } else {
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        if (!flag) {
            cat.setActive(true);
            catResult.add(cat);
        } else {
            cat.setActive(false);
            catResult.add(cat);

        }

        flag = false;
        List<SourceVO> sources = filmServiceAPI.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO source = new SourceVO();
        for (SourceVO sourceVO : sources) {
            if (sourceVO.getSourceId().equals("99")) {
                source = sourceVO;
                continue;
            }
            if (sourceVO.getSourceId().equals(sourceId)) {
                flag = true;
                sourceVO.setActive(true);
            } else {
                sourceVO.setActive(false);
            }
            sourceResult.add(sourceVO);

        }
        if (!flag) {
            source.setActive(true);
            sourceResult.add(source);
        } else {
            source.setActive(false);
            sourceResult.add(source);

        }

        flag = false;
        List<YearVO> years = filmServiceAPI.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO year = new YearVO();
        for (YearVO yearVO : years) {
            if (yearVO.getYearId().equals("99")) {
                year = yearVO;
                continue;
            }
            if (yearVO.getYearId().equals(yearId)) {
                flag = true;
                yearVO.setActive(true);
            } else {
                yearVO.setActive(false);
            }
            yearResult.add(yearVO);

        }
        if (!flag) {
            year.setActive(true);
            yearResult.add(year);
        } else {
            year.setActive(false);
            yearResult.add(year);

        }

        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);

        return ResponseVO.success(filmConditionVO);
    }


```
有点复杂，先遍历一次，遇到99了先存下来，如果没有匹配到的，说明传回来的就是99，那么把99全选传回去就好了。没有用到什么特别的算法，如果想快点用上二分可能好点。测试结果：
![](https://upload-images.jianshu.io/upload_images/10624272-5d2f5bd2b6cf81fc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这样就测试成功了。**（WiFi断掉才能练上这个问题还是存在，WiFi连着，可能可以找到服务，WiFi断开是一定可以找到服务，问题还是message can not send，channel is closed.）**
####  影片查询接口
影片查询接口顺便实现一次重构， 比如说在首页的时候：
![](https://upload-images.jianshu.io/upload_images/10624272-092048257a45eb94.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
islimit = true，即为首页，islimit为false则为列表，但是这样是完全不能满足功能需求的。影片查询需要7个参数，影片类型，排序方式，来源，分类，年份，当前第几页，总页数，如果使用参数直接散开传到控制器是可以的，但是很麻烦，所以使用一个模型来接收。那么控制器主要做几个事情：首先是根据showType判断类型，接着根据sortID排序，然后添加各种查询条件，判断当前是第几页。之前有实现过geHotFilms等等类似功能的函数，重构这些函数，改成可用的，先修改API：
```
    FilmVO getHotFilms(boolean isLimit, Integer nums, Integer nowPage, Integer sortId, Integer sourceId,Integer yearId, Integer catId);
    //get films displayed soon
    FilmVO getSoonFilms(boolean isLimit, Integer nums, Integer nowPage, Integer sortId, Integer sourceId,Integer yearId, Integer catId);
    FilmVO getClassicFilms(Integer nums, Integer nowPage, Integer sortId, Integer sourceId,Integer yearId, Integer catId);

```
修改查询接口的时候sourceId和yearId是一样的，catId可能有点麻烦，一个电影可能有多个标签，可能既是动作，又是爱情，所以数据库里面是2#4#5#7这样存放。如果查询3，那么可以查询#3#，以此类推：
```
            Page<FilmT> page = new Page<>(nowPage, nums);
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<FilmT> films = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfo(films);
            filmVO.setFilmNum(films.size());

            int totalCounts = filmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts / nums) + 1;


            filmVO.setFilmInfo(filmInfos);
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);

```
别忘了还有排序，排序需要按照不同的需要对影片排序：
```
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
            }

```
把/film/getFilms加入忽略列表里面，首页也是可以查询的，并且不需要登录处理。如果出现一下：
![](https://upload-images.jianshu.io/upload_images/10624272-88010c5bf4b424ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
那么这就调通了。
#### 影片详情接口
套路都很简单，首先在控制器添加一个方法，查询详细信息本身是很简单的，只是简单的调用接口就好了，但是这里会使用到dubbo的一个特性——异步特性，那么这个时候就需要重新定义API了。影片这里的详细信息查询关系到关联查询，在Mappering里面添加API：
```
public interface FilmTMapper extends BaseMapper<FilmT> {
    FilmDetailVO getFilmDetailByName(@Param("filmName") String filmName);
    FilmDetailVO getFilmDetailById(@Param("uuid") String uuid);


}

```
然后在Mapper添加基本语句，然后重写SQL语句了。主要是在film_t和film_info_t这两张表进行查询，查询的结果后还需要进行拼接，其实还是挺简单的。
![](https://upload-images.jianshu.io/upload_images/10624272-f32fc475f1ce8847.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
version1版本，简单实现一下。
![](https://upload-images.jianshu.io/upload_images/10624272-0f3dccc7bfa23b19.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
拼接拼的差不多了，info1那里的转换还有点问题，还有上映时间日期也没有弄好：
![](https://upload-images.jianshu.io/upload_images/10624272-ccc4cadb755eacd4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
组织好后面的上映日期。现在就是要处理#1#2#4#5#这类字符串了，先去掉收尾的#号再把中间的#变成，号：
```
select trim(both '#' from film_cats) from film_t;

```
both就是收尾都要去掉。接着就是替换：
```
select replace(trim(both '#' from film_cats), '#', ',') from film_t;

```
这样就替换成功了。那么只需要看看哪一个是IN (选择语句)就好了，按道理：
```
select * from cat_dict_t where UUID in (select replace(trim(both '#' from film_cats), '#', ',') from film_t);

```
这样就好了，但是结果只是出现一个：
![](https://upload-images.jianshu.io/upload_images/10624272-13e2046f161f5408.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
所以这种做法是不可以的，FIND_IN_SET函数可以做到，可以用find_in_set函数把他们套起来，find_in_set(字段，子集)==》
```
select * from cat_dict_t t where  FIND_IN_SET(t.uuid, (select replace(trim(both '#' from film_cats), '#', ',') from film_t));

```
![](https://upload-images.jianshu.io/upload_images/10624272-fbbf9a478ce00f58.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
差不多了，但是还有一个问题，就是如何拼接的问题，自己需要在中间加上group_concat(字段 separator ',')即可：
```
select group_concat(show_name separator ',') from cat_dict_t t where  FIND_IN_SET(t.uuid, (select replace(trim(both '#' from film_cats), '#', ',') from film_t));

```
![](https://upload-images.jianshu.io/upload_images/10624272-ff341dbf209da3c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
那么接下来组织一下就好了，先简单的读出数据：
```
select film.film_name                             as filmName,
       info.film_en_name                          as filmEnName,
       film.img_address                           as imgAddress,
       info.film_score                            as score,
       info.film_score_num                        as scoreNum,
       film.film_box_office                       as totalBox,
       film_cats                                  as info1,
       concat(film.film_source, info.film_length) as info02,
       concat(film.film_time, (select show_name from source_dict_t where film_source = source_dict_t.UUID),
              '上映')                               as info03
from film_t film,
     film_info_t info
where film.UUID = info.film_id
  and film.UUID = 2;
```
接下来就是按照上面的做法先解析分类：
```
select film.film_name                                       as filmName,
       info.film_en_name                                    as filmEnName,
       film.img_address                                     as imgAddress,
       info.film_score                                      as score,
       info.film_score_num                                  as scoreNum,
       film.film_box_office                                 as totalBox,
       (select group_concat(show_name separator ',')
        from cat_dict_t t
        where FIND_IN_SET(t.UUID,
                          (select replace(trim(both '#' from film_cats), '#', ',')
                           from film_t
                           where film_t.UUID = film.UUID))) as info1,
       concat(film.film_source, info.film_length)           as info02,
       concat(film.film_time, (select show_name from source_dict_t where film_source = source_dict_t.UUID),
              '上映')                                         as info03
from film_t film,
     film_info_t info
where film.UUID = info.film_id
  and film.UUID = 2;
```
接下来就是后面的拼接：
![](https://upload-images.jianshu.io/upload_images/10624272-49a4870d4e3b64d4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](https://upload-images.jianshu.io/upload_images/10624272-8860c2f61db12e6d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这玩意有点吓人，讲道理，还没写过这么长的，如果把那些分类全部放程序里面的话有不好改，不灵活。接下来就是补充基本的控制器即可，但是这上面完成的只是电影基本信息，电影的介绍，演员表，截图等等还没有完成。接下来就是电影的演员，电影与演员的对应关系是一对多的关系，如果直接写不太好些，所以用一个演员映射表来实现:
```
CREATE TABLE mooc_film_actor_t(
  UUID INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键编号',
  film_id INT COMMENT '影片编号,对应mooc_film_t',
  actor_id INT COMMENT '演员编号,对应mooc_actor_t',
  role_name VARCHAR(100) COMMENT '角色名称'
) COMMENT '影片与演员映射表' ENGINE = INNODB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


```
role_name不是演员名字，是角色，比如在电影里面的导演还是演员，这里也需要联合查询：
```

select actor.actor_name as directorName, actor_img as imgAddress, rela.role_name as roleName
from actor_t actor,
     film_actor_t rela
where actor.UUID = rela.actor_id
  and rela.film_id = 2;
```
这个查询比上面的要简单多了，联合几个表，也不需要嵌套查询。这里要返回的json串：
![](https://upload-images.jianshu.io/upload_images/10624272-45c9ee2969dd4052.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这整个json作为一个对象FilmRequestVO，这个FilmRequestVO又包含几个对象，status和imgPre都是原来Respose就有的，只需要把剩下的加上就好，data那块是一个对象，而data里面info04又做成一个对象，director和actor又是一个对象，以此类推，贼多。
![](https://upload-images.jianshu.io/upload_images/10624272-44584331cfb7ab1c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
接下来就是控制器访问的问题了：
![](https://upload-images.jianshu.io/upload_images/10624272-ace568472b9bfdac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
ignore_url里面直接加film/films是不行的，因为在Authfilter里面是equal，相等在能匹配，只需要把Authfilter里面改成startwith即可：
![](https://upload-images.jianshu.io/upload_images/10624272-8432bc4fb890fc4e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/10624272-a22030c750431c61.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
调试成功。现在还有一个小小的问题，目前这种情况很快，数据量小一下子就出来了，假设每个接口都是200ms，那么这个请求接口一共就演示1秒了，真是很大的延迟。而dubbo与一个特性，即异步调用。
# 异步调用
dubbo的异步调用特性基于NIO的非阻塞实现并进行调用，客户端是不需要启动多线程。![](https://upload-images.jianshu.io/upload_images/10624272-241ba99a9d16703f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
首先用户线程调用服务，进入IOThread，IOthread请求服务器获取返回，然后不等待服务器返回，立刻设置一个future对象，future对象标识一个可能还没有完成的任务结果，等到完成了在通知future，然后接受返回。原本是发出请求，等待执行完成后返回，现在是已发出请求就返回，用future来接受，立刻返回后就跑去执行其他的，这就做到异步了。
比如启动异步调用之后，执行方法：
```
fooService.findFoo(fooId);
```
这个方法直接返回一个null，因为不等待结果了，接着dubbo会自动生成一个funture对象，因为需要用future对象存储返回数据，所以需要获取到Future：
```
Future<Foo> fooFuture = RpcContext.getContext().getFuture();
```
再从这个future对象获取信息：
```
Foo foo = fooFuture.get();
```
明白原理之后就要把异步调用用到业务上了。刚刚返回电影信息详细可以使用
```
    @Reference(interfaceClass = FilmServiceAPI.class, async = true)
    private FilmServiceAPI filmServiceAPI;

```
如果这样使用，那么但凡是跟future没有关系的都会报错，比如在刚刚获取详细信息过程中：
```
        FilmDetailVO filmDetail = filmServiceAPI.getFilmDetail(searchType, searchParam);

        if (filmDetail == null) {
            return ResponseVO.serviceFail("没有可查询影片");
        } else if (filmDetail.getFilmId() == null || filmDetail.getFilmId().trim().length() == 0) {
            return ResponseVO.serviceFail("没有可查询影片");

        }

        String filmId = filmDetail.getFilmId();

```
前面这部分是获取ID的，后面都是根据ID去查找其他的信息，那么如果异步处理后面的ID可能null值了。把需要异步的方法全部封装成另外一个API进行处理，剩下的方法留着。
```
    @Reference(interfaceClass = FilmServiceAPI.class)
    private FilmServiceAPI filmServiceAPI;

    @Reference(interfaceClass = FilmAsyncServiceAPI.class, async = true)
    private FilmAsyncServiceAPI filmAsyncServiceAPI;
```
异步调用需要加上async = true。
```


        filmAsyncServiceAPI.getFilmDesc(filmId);
        Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();

        filmAsyncServiceAPI.getImgs(filmId);
        Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();

        filmAsyncServiceAPI.getDectInfo(filmId);
        Future<ActorVO> directorVOFuture = RpcContext.getContext().getFuture();

        filmAsyncServiceAPI.getActors(filmId);
        Future<List<ActorVO>> actorVOFuture = RpcContext.getContext().getFuture();

        InfoRequestVO infoRequestVO = new InfoRequestVO();

        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setActors(actorVOFuture.get());
        actorRequestVO.setDirector(directorVOFuture.get());

        infoRequestVO.setActors(actorRequestVO);
        infoRequestVO.setBiography(filmDescVOFuture.get().getBiography());
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setImgVO(imgVOFuture.get());

```
需要用future对象来接收数据。和示例一样，注意异步需要手动开启，在启动类加上@EnableAsync 
# 影院模块
影院这块相对简单一些，6张表，品牌字典表，其实就是类似于map这样的映射表，地域字典表，影厅字典表，影院主表，影院的详细信息，在热映电影字典表，放映场次信息表。热映电影表其实是不需要的，但是为了减少数据查询的负荷，还是以空间换时间，数据库往往是系统的瓶颈。
首先先给出接口大致框架：
```
@RestController
@RequestMapping("/cinema/")
public class CinemaController {
    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @RequestMapping(value = "getCinemas", method = RequestMethod.GET)
    public ResponseVO getCinemas(CinemaQueryVO cinemaQueryVO) {
        return null;
    }

    @RequestMapping(value = "getCondition", method = RequestMethod.GET)
    public ResponseVO getCondition(CinemaQueryVO cinemaQueryVO) {
        return null;
    }

    @RequestMapping(value = "getFields")
    public ResponseVO getFields(Integer cinemaId) {
        return null;
    }

    @RequestMapping(value = "getFieldInfo", method = RequestMethod.POST)
    public ResponseVO getFieldInfo(Integer cinemaId, Integer fieldId) {
        return null;
    }

}


```
接着还按照返回报文建立响应的数据结构。需要注意的就是已售座位这里，只有下了订单之后才能填上已售座位，所以只能先写死，等到订单完成后在回来补齐。下面先分析一下所需要的接口，首先第一个接口getCinemas接口，就是入参出参即可，按照五个条件进行帅选，判断是否有满足条件的影院，出现异常应该怎么处理。**入口参数：CinemaQueryVO，出参就是cinemaVO对象；然后是getCondition按照条件查询，1.需要获取品牌列表，也就是影院是哪个公司的，2.所在区域，3.影厅又是什么类型；getFields根据影院变化获取影院信息，获取所有电影信息和对应的场次信息，影院编号。接下来就是获取场次详细信息，根据编号获取场次详细信息，根据反映场次ID获取反映信息，根据反映场次查询播放电影，根据电影编号获取电影信息，还有一个售卖座位的信息是需要通过订单实现的，所以待实现。**
列出实现接口：
```
public interface CinemaServiceAPI {
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);

    List<BrandVO> getBrands(Integer brandId);

    List<AreaVO> getAreas(Integer areaId);

    List<HallTypeVO> getHallTypes(Integer hallType);

    CinemaInfoVO getCinemaInfoById(Integer cinemaId);

    FilmInfoVO getFilmInfoByCinemaId(Integer cinemaId);

    FilmFieldVO getFilmFieldInfo(Integer fieldId);

    FilmInfoVO getFilmInfoByFieldId(Integer fieldId);
}

```
这里吗有点复杂的其实就是getFilmInfoByCinemaId()；首先要根据影院ID去field_t表把这个影院的场次全部查出，还要去hall_film_t表把对于电影名字给出并筛选。这里可能有些困难的是hall_film_info_t和field_t的关系，hall_film_info_t是影厅播放的电影，而field是场次信息，一个hall_film_t会对应多个field，这个时候如果用联合查询只能查出一个，查不出整个集合，所以只能用联合查询了：
```
select info.film_id,
       info.film_name,
       info.film_length,
       info.film_language,
       info.film_cats,
       info.actors,
       info.img_address,
       f.UUID,
       f.begin_time,
       f.begin_time,
       f.hall_name,
       f.price
from hall_film_info_t info
         left join field_t f
                   on f.film_id = info.film_id
                       and f.cinema_id = '1';


```
但是问题来了：
```
@Data
public class FilmInfoVO implements Serializable {
    private String filmId;
    private String filmName;
    private String filmLength;
    private String filmType;
    private String filmCats;
    private String actors;
    private String imgAddress;
    private List<FilmFieldVO> filmFields;
}

```
最后一个是一个集合对象，怎么返回？只能使用mybatis SQL自定义来处理了，首先mapper设置一个接口，在mapper的xml文件里面进行实现即可。
```
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="com.stylefeng.guns.rest.common.persistence.model.FieldT">
        <id column="UUID" property="uuid"/>
        <result column="cinema_id" property="cinemaId"/>
        <result column="film_id" property="filmId"/>
        <result column="begin_time" property="beginTime"/>
        <result column="end_time" property="endTime"/>
        <result column="hall_id" property="hallId"/>
        <result column="hall_name" property="hallName"/>
        <result column="price" property="price"/>
    </resultMap>

    <resultMap id="getFilmInfoMap" type="com.stylefeng.guns.api.cinema.vo.FilmInfoVO">
        <result column="film_id" property="filmId"></result>
        <result column="film_name" property="filmName"></result>
        <result column="film_length" property="filmLength"></result>
        <result column="film_language" property="filmType"></result>
        <result column="film_cats" property="filmCats"></result>
        <result column="actors" property="actors"></result>
        <result column="img_address" property="imgAddress"></result>
        <collection property="filmFields" ofType="com.stylefeng.guns.api.cinema.vo.FilmFieldVO">
            <result column="UUID" property="fieldId"></result>
            <result column="begin_time" property="beginTime"></result>
            <result column="end_time" property="endTime"></result>
            <result column="film_language" property="language"></result>
            <result column="hall_name" property="hallName"></result>
            <result column="price" property="price"></result>
        </collection>
    </resultMap>
    <select id="getFilmInfos" parameterType="java.lang.Integer" resultMap="getFilmInfoMap">
select info.film_id,
       info.film_name,
       info.film_length,
       info.film_language,
       info.film_cats,
       info.actors,
       info.img_address,
       f.UUID,
       f.begin_time,
       f.end_time,
       f.hall_name,
       f.price
from hall_film_info_t info
         left join field_t f
                   on f.film_id = info.film_id
                       and f.cinema_id = #{cinemaId}
    </select>
</mapper>

```
resultMap定义自定义对象，这样就可以返回自定义类型了。接着实现业务层什么的都很简单。简要提一下lombok做日志，平时做日志都是要private Logger log......，可以加上@SLf4j注释，就可以省略掉上述的过程。然后就是测试了。
# dubbo结果缓存
对于getCondition这个方法，一般是热点数据，这个数据会被频繁的使用，这种热点数据一般处理都很简单，就是放到缓存，对于dubbo提供的结果缓存，是针对已经存在的大量频繁访问的数据，存储在本地缓存中，存在当前是jvm里面，所以可能会存有多份缓存，访问也更快。缓存类型有三种，lru缓存，和os的页面置换类似，最近最少使用缓存，threadlocal当前线程缓存，还有一种是jcache，这种比较少见，基本都是前面两种，但是注意threadlocal不适合大量数据。配置其实很简单，在接口加上配置即可：
```
    @Reference(interfaceClass = CinemaServiceAPI.class, check = false, cache = "lru")
    private CinemaServiceAPI cinemaServiceAPI;

```
#### 并发连接控制
同时，dubbo可以对并发和连接数量进行控制，可以在配置文件设置并发控制数量等等。首先明确，如果并发与连接数量超出了，并不会等待，会以错误的形式进行返回，dubbo本身虽然有服务降级，但服务降级这个东西实现的并没有特别好，其次，dubbo本身是有服务守恒的问题，但是在以前dubbo防止雪崩是通过控制并发与连接数量来控制的，尤其是连接。雪崩：目前3个服务，其中一个服务不知道为什么原因并发量非常大，超出了他本身所能承受的力度，然后这个服务就只能被冲崩了，然后这些请求又全部被送到了二号，二号又雪崩了以此类推。所以就叫雪崩。以往对于这种控制是用控制连接与并发数。
# 订单模块
订单模块这玩意，之前都没有碰过，订单模块主要是涉及一些dubbo特性或者是服务配置的问题，订单本身的业务很简单，就两个，下订单，查看订单，没了，服务配置比如限流，服务降级，熔断等等，dubbo本身有熔断，但是这个实现不太好，所以使用其他的熔断器来进行处理。首先是安装ftp，10.13版本前的ISO是自带的，Mac往后版本是没有的了，我的恰好是10.14的，ftp没有带上自带了sftp，sftp是ftp的变体，FTP另外一种是TFTP，FTP，TFTP，SFTP都是三种文件传输，区别就在于，FTP是需要在可信赖网络上传输，他没有很高的安全加密，SFTP有，如果信息很铭感，那就需要用SFTP了，增加了安全层进行信息加密，TFTP即是简单文件传输，基本适用于局域网，而且与其他两种协议不同就在于，FTP和SFTP使用TCP协议，而TFTP使用UDP协议，既然自带了那就使用sftp充当FTP吧。
首先对于购票业务，后端要有一个原则，永远都不能相信前端给你的东西，因为是可以通过前端进行更改的，所以要验证是否为真。影院列表是使用json文件，判断座位id是不是正确，判断在订单里面有没有座位id，既然售出自然不能再买了，验证完这些后才能创建订单信息。对于订单业务，获取当前登录信息，获取订单即可。由于座位信息是通过json文件传输，需要从ftp服务器获取，但是我的Mac没有ftp，就去阿里云找了一个。**使用FileZilla用户root发现登录成功，但是获取目录失败了，密码也没有错，错误原因有可能就是端口了，但是21  20端口开了，21传输命令，20端口传输文件。这里的问题确实实在端口，但是是在ftp的传输方式上，ftp有两种传输方式，一种是主动，一种是被动，主动：服务端来找客户端，通过21传输命令，通过20传输数据；被动方式：客户端找服务端，命令还是用21端口，但是文件使用1025-65535随机一个，而FileZilla默认使用passive mode，自然要开启全部了。所以当输入账户密码的时候，即是命令登录，使用21，没有问题，但是文件目录是使用随机端口了，所以出现问题。408历年真题有一个选项就是这玩意，ftp任何情况下传输文件使用20端口，错误的，主动才是。**
然后就是配置阿里云服务器了，配置很简单，读取信息的那些stream可能有点烦：
```

    public String getFileStrByAddress(String fileAddress) {
        initFTPClient();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(fileAddress)));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String lnStr = bufferedReader.readLine();
                if (lnStr == null) {
                    break;
                }
                stringBuffer.append(lnStr);
            }
            ftpClient.logout();
            return stringBuffer.toString();
        } catch (Exception e) {
            log.error("获取文件选项失败");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
```
测试通过之后，把ftp的相关信息配置到application.yml，如果出现-u会自动转换成大写。
##### 订单模块业务实现
业务实现这块下单有点复杂，存储没有什么问题，返回的时候需要多个表的信息，影院名称，电影名称，电影价格，订单总价等等信息，但是还是很好写。因为通常购买完成之后，需要返回已经插入的订单：
![](https://upload-images.jianshu.io/upload_images/10624272-49039f1365b7e5d9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
fieldTime的组织有点麻烦，首先要把orderTimestamp变成格式化拼接，然后加上今天即可:
```
select o.UUID                                                                as orderId,
       h.film_name                                                           as filmName,
       concat('今天 ', DATE_FORMAT(o.order_time, '%m月%d日'), ' ', f.begin_time) as fieldTime,
       c.cinema_name                                                         as cinemaName,
       o.seats_name                                                          as seatsName,
       o.order_price                                                         as orderPrice,
       UNIX_TIMESTAMP(o.order_time)                                                          as orderTimestamp
from order_t o,
     field_t f,
     hall_film_info_t h,
     cinema_t c
where o.cinema_id = c.UUID
  and o.field_id = f.UUID
  and o.film_id = h.film_id
  and o.UUID = "415sdf58ew12ds5fe1";
	

```
业务层的实现都很简单，没有什么问题。然后是实现得到已售出的座位，concat和group_concat的区别，concat的实现对象是字符串，group_concat是可以在分组或不同表时间实现的，可以和group by和起来使用。注意之前哎cinema模块也要把原来没有实现的已售出座位修改一下。
```
            hallFieldInfo.setSoldSeats(orderServiceAPI.getSoldSeatsByFieldId(fieldId));

```
cinema这块信息加上，postman测试一下：
![](https://upload-images.jianshu.io/upload_images/10624272-922844a3ce8eb8b3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**对于WiFi打开就连接不上的问题，初步怀疑是虚拟ip的原因，因为在网上有人是因为电脑有两张网卡，一张有线以太网网卡，一张是无线网卡，结果接到无线网卡去了，但是我的Mac又没有以太网网卡，设置里面也查看了确实是802.11无线局域网，使用CSMA/CA协议，应该是连接WiFi导致是ip发生变化。**
测试完成后基本上业务这块就完成了，但是订单模块的拓展还是比较多的。首先，一般来说在订单这块，业务量是比较大的，每一部电影的订单量是很大的，比如战狼2卖了50多个亿，在这种情况下，订单不会再数据库用一张表存，这就涉及到了**订单模块的横向和纵向的拆分；**其次，还有**服务限流**，服务限流不仅仅是局限于订单系统，其他的也有可能包含，就比如LOL比赛的观看人数等等都需要限流。还有**熔断和降级**，这类主要解决服务器雪崩的情况。
# 订单横向纵向拆分
比如像京东天猫，天猫一开始是卖小商品为主，那么运行十年之后，有一部分是服装，有一部分是家电，这些在订单表里面是混淆在一起的，一亿条订单表里面7000条服装，3000条小商品，如果混在一起，对于分析查找都是不方便的，这个时候就会引入横向拆分，比如家电表，比如小商品表，纵向拆分就是按照时间拆分，比如2017年一张表，2018年一张表等等。但是这样拆分，业务也会复杂，比如现在有两张表，order_2017_t，order_2018_t两张表。这里涉及到一个dubbo的特性，**服务分组，**当一个  接口有几个实现，可以用group来区分，**分组聚合，**从每一个group中调用返回结果，并合并返回结果。group关键字进行分组，merge关键子进行合并，合并也需要注意，只有返回了list或者collection一类的才能合并，但是当前使用的dubbo并不支持，所以要合并也只能手动合并。但是如果这样分组和并就要改业务了，所以知道怎么回事，但是就不改了。
# 服务限流
首先服务限流是系统高可用的一种手段，对于业务上面没有任何帮助，完全是为了高并发。dubbo也有并发和控制连接数来限流控制。主要是用于服务之间的限流，限流算法有两种，**漏桶法和令牌桶法**。
漏桶法：![](https://upload-images.jianshu.io/upload_images/10624272-35c48ab5d8bbe67c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
水龙头的请求，下面的整个是业务系统，无论来多少请求都会先装载这个桶里面然后然后再处理。所有的请求进来都会被排列成一个队列，然后按照相同的速度进行处理。
令牌桶算法：
![](https://upload-images.jianshu.io/upload_images/10624272-72a7ac1da48484a2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
arrival即请求，在请求进入的同时还有一个保护现场，这个保护线程拥有令牌，线程进入之后只有拿到令牌才能被处理，否则会被丢弃或返回，他与漏桶算法的最大区别就在于这玩意的**业务请求峰值是有一定承载能力的**，比如桶里面有1000个令牌，1000个业务进来可以并发全部执行完，但是对于漏桶算法无论还有多少内存或者是挤压空间，处理速度都还是这么快。另外，令牌桶算法可以通过改变添加令牌的速度来控制请求的处理，防止业务崩掉。
简单实现一下令牌桶算法：
首先需要准备桶的数量：
```
    private int bucketNums = 100;
    private int rate = 1;
    private int nowTokens = 0;
    private long timestamp = getNowTime();

```
按照每毫秒添加一个令牌的速度进行业务请求控制。
```

    public boolean getToken() {
        long nowTime = getNowTime();
        nowTokens = nowTokens + (int) ((nowTime - timestamp) * rate);
        nowTokens = min(nowTokens);
        setTimestamp(nowTime);
        System.out.println("当前令牌数：" + nowTokens);
        if (nowTokens < 1) {
            return false;
        } else {
            nowTokens--;
            return true;
        }
    }

```
每一次请求看看时间离上一次申请令牌过去了多久，按照毫秒把令牌数补上，如果令牌数是大于0的，允许运行，然后减一。
![](https://upload-images.jianshu.io/upload_images/10624272-1c14ba79202a5f17.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这样就实现了，加入到工程里面。在订单系统里面，getOrderInfo这个频率不太高，主要是下单的频率很高，在下单处增加：
![](https://upload-images.jianshu.io/upload_images/10624272-b0a52b1cf83bc148.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
# 熔断降级
服务的稳定是公司可持续发展的重要基石，随着业务量的快速发展，一些平时正常运行的服务，会出现各种突发状况，而且在分布式系统中，每个服务本身又存在很多不可控的因素，比如线程池处理缓慢，导致请求超时，资源不足，导致请求被拒绝，又甚至直接服务不可用、宕机、数据库挂了、缓存挂了、消息系统挂了...对于一些非核心服务，如果出现大量的异常，可以通过技术手段，对服务进行降级并提供有损服务，保证服务的柔性可用，避免引起雪崩效应。实时监控接口的健康值，在达到熔断条件时，自动开启熔断，开启熔断之后，如何实现自动恢复？每隔一段时间，释放一个请求到服务端进行探测，如果后端服务已经恢复，则自动恢复。比如，如果ServiceA调用ServiceD一直失败，或者失败率很高，就可以采用“一种机制”确保后续请求不会调用ServiceD，而是执行降级逻辑。
使用Hystrix作为熔断降级工具，Hystrix主要有两种命令模式：
![](https://upload-images.jianshu.io/upload_images/10624272-848b725559d19274.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
hystrix command模式有主要是单线程进行，而Observable Command可以使用线程池等等。请求从command进入。
![](https://upload-images.jianshu.io/upload_images/10624272-d2be02192d562988.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
经过toObservable之后进入第三步，判断目前的结果是不是在缓存里，不在的话继续往下做，判断断路器是否开启（第四步）。本来的业务线是A调用B，这是一条通路，熔断就是把A到B切断，熔断器开启的意思就是是不是把这条路切断了，切断了那就简单了，直接走返回。接下来判断线程池状态，如果都是OK，那么继续往下走，到达第六步，如果执行成功了，啥事没有，失败了或者是超时了，注意在熔断器机制下，不仅仅是执行失败的，超时也算是失败的。到达第8步，是失败调用的，这一步就叫降级返回，也叫服务降级。服务熔断是判断要不要把这条路干掉，一旦出现业务异常，就调用服务降级，把业务返回。比如之前是A调用B，降级就是不调用B，使用一种折中的方法返回，比如今天想打游戏，电脑宕机了，不会直接告诉你我炸机了，hystrix会返回电脑没电了，游戏倒闭了等等比较折中的方案。
首先导包了：
```
	  <dependency>
		    <groupId>org.springframework.cloud</groupId>
		    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
		    <version>2.0.0.RELEASE</version>
		</dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
		    <groupId>org.springframework.cloud</groupId>
		    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
		    <version>2.0.0.RELEASE</version>
		</dependency>
```
接着设置注解开启熔断器：
```
	@EnableHystrixDashboard
	@EnableCircuitBreaker
	@EnableHystrix
```
在需要熔断的方法上加上注解：
```
@HystrixCommand(fallbackMethod = "error", commandProperties = {
@HystrixProperty(name="execution.isolation.strategy", value = "THREAD"),
@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value
= "4000"),//超时时间
@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//出现10次例外
@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
}, threadPoolProperties = {
@HystrixProperty(name = "coreSize", value = "1"),
@HystrixProperty(name = "maxQueueSize", value = "10"),
@HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
@HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
})
```
加上熔断器之后需要右一个一模一样参数返回值的方法，对应fallbackMethod即可。注解上的配置名字都很明显了，没有上面需要解释的，超过4秒即为超时，10次例外就开启你熔断机制等等。
**添加了Hystrix注解之后，会发现Threadlocal用不了了，这是因为Hystrix本身有线程隔离，线程池保护和信号量机制，所以会切换线程，这个时候ThreadLocal就有用了，那就找一个可以缓存之前信息的，就是InheritableThreadLocal。**
```
public class CurrentUser {
    private static final InheritableThreadLocal<String> threadlocal = new InheritableThreadLocal<>();

    public static void saveUserId(String userId) {
        threadlocal.set(userId);
    }

    public static String getCurrentUser() {
        return threadlocal.get();
    }

}

```
这样就可以保存之前的信息。忘记开WiFi了：
![](https://upload-images.jianshu.io/upload_images/10624272-18b3d4abf3fd547b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
服务降级成功了。感觉服务降级好像不止这些功能，上面实现的大概类似于安抚的一种做法，当然，也可以设计一个队列，把请求追加在队列里面。
# 支付模块
支付模块做简单点，对接支付宝即可。**支付流程：首先要获取二维码，用户扫描支付二维码之后，系统后台是不知道，只能等支付宝回调，然后修改订单状态，最后还需要定期对账。**但是等待支付宝回调有点麻烦，现在还没有一个公网的地址，所以只能启动另外一个流程，首先Consumer是服务端消费，客户端就是前端操作系统，Consumer获取二维码送到前端客户端上，前端进行扫描支付，这个时候后端会启用请求调用来查询前端支付状态，同时修改数据库，把订单修改成已支付，当然了，支付流程不够严谨，但是已经能够完成了。简单看一下开发文档，本身支付宝是需要身份验证，营业执照等等，这里肯定没有了，所以只能用沙箱版的支付宝，也就是沙箱环境做测试，全部都是假的。然后配置一下沙箱环境，按照文档：
![](https://upload-images.jianshu.io/upload_images/10624272-eab5be5fa7b81047.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
填写配置文件信息。properties里面有公钥和私钥。**现在有两个环境，一个是商户，一个是支付宝，相互都持有一个公钥，这个公钥是以明文传输，没有安全性，比如近代战争通信进程会有一个秘密本，按照密码本来进行加密，这个密码本就是公钥，商户还有一个私钥，根据私钥决定怎么读密码本，这个公钥和私钥是一对的，接着如果有数据，那么就用私钥把数据加密，当然是根据公钥加密了，而支付宝也会有一个私钥，私钥是一样的，那么支付宝也会用私钥来找公钥，进行解密即可。**
![](https://upload-images.jianshu.io/upload_images/10624272-29975e42eebaffc9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
简单弄一个demo玩一下，首先要下载一个沙箱app，把蚂蚁金服上面给的demo搞下来，把包全部导入，使用阿里云提供公钥生成：
![](https://upload-images.jianshu.io/upload_images/10624272-21a10042744a015d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
有两种秘钥的验证方式，RSA和RSA2，代码里面默认选用了RSA2，那么把对应的秘钥填写上去。
![](https://upload-images.jianshu.io/upload_images/10624272-1fb86d19fe97b13d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/10624272-43a0aab58bc6b4a5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这句不去掉注释是没有图片生成的。然后运行就可以了。
接下来就是业务环境搭建，把SDK复制过来改好包。支付模块的业务其实很简单，获取二维码，存在FTP服务器，返回前端，获取支付结果。**现在整个流程测试一下，首先是登录，获取jwt，然后使用jwt进行购票操作，发现居然触发了熔断器，但是这也发现熔断器设置的一个不足，没有配置是error日志的打印，但是项目赶工在即，后面高可用或者项目维护再说其他的吧。这个错误显示**
![](https://upload-images.jianshu.io/upload_images/10624272-5482f9b0845aee18.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**updateById这个方法出现异常，mybatis plus的update更新这里是用自带的，默认会根据主键来进行更新，然而order表忘记建立主键了，所以找不到主键自然也就不存在什么根据主键更新了，更新表结构为带主键方式，更新mapper文件。更新完成之后还是出现异常**
![](https://upload-images.jianshu.io/upload_images/10624272-58b10685f31ce3f5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**这条语句异常，这条语句是插入了之后再从database读出来，查阅数据库发现，插入的uuid是null，也就是说主键是没有被插入的，查看log打印的日志，insert里面没有插入uuid，我使用insert默认提供的插入方法，orderT里面哪个字段不是null就插入进去，但是很明显uuid有的，问题就出现在配置文件mapper或者是orderT自己生成的模型中，orderT可能性不大，mapper文件查阅后发现确实没有上面问题，百度发现问题在orderT生成的模型上面**
![](https://upload-images.jianshu.io/upload_images/10624272-f73b89730b7e0be9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**主键不添加策略，默认是auto自增方式，但是String又不能自增，就填不进去了，加上type，变成手动input方式，这样理论上应该是OK的了，然而还是触发了熔断器，去掉熔断器发现是没有问题的，那么就是熔断器的时间了，算了一下，至少要10s，一个请求10秒有点过分了，所以后面改进可能要进行分布式或者异步改进，因为这个请求涉及到了FTP的数据传送，但是总算还是OK了。**
![](https://upload-images.jianshu.io/upload_images/10624272-3e09393cc7a886be.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**然后就是生成订单二维码了**
![](https://upload-images.jianshu.io/upload_images/10624272-40d99f5121a3d4db.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**用sandbox版的支付宝支付一下**
![](https://upload-images.jianshu.io/upload_images/10624272-5d2c49bc5a42bef9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**显示支付成功，那么这样这个下单支付的后台基本没有问题了，下面就是要把二维码上传到FTP上去了，这个时候生成二维码服务可能会更加慢。另外打开WiFi就找不到服务的问题，今天突然可以了，这也是为什么今晚测试这么快的原因。练着WiFi能找到服务可能是因为我去了家里另外一处房子，那里的WiFi突然就可以了；以往这个bug得到的结论是WiFi的开关和Provider能否被Consumer找到互相有因果关系，现在原因可能会与WiFi路由器的不同相关，仍在观查——2020.1.23  2:12分凌晨**
配置上传到ftp也很简单，首先加入上传的目录：
![](https://upload-images.jianshu.io/upload_images/10624272-4e88d903146c5157.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
配置路径
```
    public boolean uploadFile(String fileName, File file){
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            initFTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            boolean change = ftpClient.changeWorkingDirectory(this.getUploadPath());
            System.out.println(this.getUploadPath());
            System.out.println("切换目录是否成功：" + change);
            ftpClient.storeFile(fileName, fileInputStream);
            return true;
        }catch (Exception e){
            log.error("上传文件失败！", e);
            return false;

        }finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
                ftpClient.logout();
            } catch (IOException e) {
                log.error("关闭流异常");
                e.printStackTrace();
            }
        }

```
设置编码，上传的是图片，配置上传格式二进制，配置被动模式，改变上传目录。**启动的时候发现，上传是成功了，但是上传的路径不对，上传到了根目录，也就是说，改变目录的位置失败了，突然这个changeWorkDirectory类似于Linux里面的cd，那么配置文件里面写的/qrcode应该要去掉/，然后就可以了。
**
# Dubbo特性——本地存根
本地存根其实就类似于静态代理，按照以往的方式，接口在客户端，实现在服务端，那么客户端很受限制，比如参数的验证等等；在拿到返回调用结果之后，用户可能需要缓存结果；或者是在调用失败的时候构造容错数据，而不是简单的抛出异常。找一个接口，做一个代理类，代理类访问目标对象，当用户要访问目标对象需要先访问代理。dubbo使用本地存根的时候会在客户端生成一个代理，处理部分业务，而且Stub必须传入proxy函数。
![](https://upload-images.jianshu.io/upload_images/10624272-b57a53a05699b04e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
Consumer和Provider就是消费者和服务提供者，api就是之前写的服务接口，以往的操作是略过dubbo，action访问Service接口部分，接口通过Provider注入进来，**现在不一样了，如果使用本地存根，首先先创建一个Stub，类似一个静态代理，来实现service接口，消费者访问的时候，先访问Stub，既然Stub是代理，那么action就不会直接访问api接口，先访问代理Stub，但是在逻辑上还是去访问Servic接口，当Stub接到请求之后，转发请求到ServiceProxy对象，如果Proxy满足条件，就会路由到实现类Impl上，如果不满足就到Mock中做返回，Mock也叫伪装，proxy是远程服务的代理实例，保护目标对象，提供间接访问途径。**但是这么一看，其实我感觉用拦截器也可以的，而且Mock那个伪装有点类似于Hystrix降级，还不是很能理解这玩意作用在哪里。
我们现在的项目，客户端是guns-gateway->API，服务端是guns-alipay还有其他的模块订单->Impl，这个时候对于用户的orderId的验证，就可以放在Stub里面，这样做的好处很多，首先可以保护目标对象，其次也可以减少一次缓存。本地存根理解为一种比较特殊的静态代理模式， 用于对真实目标的一种保护，或者额外增加功能， 拦截器更适合进行切面编程， 但是存根更适合对目标对象进行精准打击，或者其实可以把这两个内容变相理解为静态代理和动态代理之间的区别。这里的容错，也就是降级返回有点类似于Hystrix，**但是本地存根的核心在于服务端反向调用客户端获取一些信息， 但是熔断的目标是容错，本质上来讲不是一个东西，服务端在调用的时候需要客户端的一些信息就可以用本地存根， 这个是Hystrix完全做不到的，有点像aop。**
另外，本地存根还有一个本地伪装的概念，本地伪装是本地存根的一个子集，其实就是Mock，当失败的时候就会走mock，但是用途反而是更多的。通常会使用本地伪装来完成服务降级，前面Hystrix也是可以做服务降级，但是Hystrix是在springboot和dubbo才能用，如果不用springboot是使用不了的。一般在客户端就可以实现。所以这玩意算作是一种补充把，使用到业务上体验一下。比如想要在返回支付结果上做降级处理，只需要继承这个类，然后service加上配置即可。
![](https://upload-images.jianshu.io/upload_images/10624272-82d213abf6460fe1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这样一个接口的方法都可以降级了，**而Hystrix相比之下只能是方法做降级，一个个方法的填上，本地伪装相对简单一点，但是也有本地伪装只能是捕获RPC的异常，RpcException，其他的不行，比如超时，网络问题，找不到服务等等，而计算问题，除0异常等等都无法捕获，所以各有优劣把。**
**dubbo还有隐式参数的特性，把参数放在RpcContext里面可以通过getAttachment获取，有些比较敏感的数据等等，正式业务系统里面，往往会有一个requestId，这个requestId是request唯一，而分布式锁也是根据requestId生成，比如在获取订单状态或者下单，可以把userId取出来对比防止伪造，这有点像spring里面getAttribute，类似于一个全局变量，但是dubbo没有全局变量这个说法。**

# 分布式事务
首先，事务是用来保证一组数据操作的完整性和一致性。事务的四种特性，Atomicity原子性，consistency一致性，isolation隔离性，durability持久性。
分布式事务大体可以分成两部分，首先是事务，以前的分布式是一个单体性的事务，其次就是分布式，分布式事务就是将多个节点的事务看成是一个整体。现在有十个节点，每台机器部署了不同的应用，有订单支付影片等等都部署在不同机器，如果在同一个事务里，处理很简单，但是如果是在不同的事务，不同机器上，其他的事务怎么知道其他事务发生失败了，失败了又怎么处理。分布式事务一般由事务参与者，资源服务器，事务管理器组成。事务参与者类比于机器和服务，资源服务器就是用来控制比如库存数量，金额等等，最后是事务管理器，是用来辅助，比如刚刚的例子，一个事务出事了，其他的事务怎么知道，那么这个事务管理就会通知其他事务。最常见的分布式事务就是支付，下订单等。
****
**分布式实现一般有两种，两段式事务和三段式事务，基于XA的分布式事务，基于消息的最终一致性方案，这里的信息一般是指信息队列或者是Redis一致性缓存，还有TCC编程式补偿性事务。**
![image](https://upload-images.jianshu.io/upload_images/10624272-eeeca1ae603b37fd?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#####  两段式事务和三段式事务
TC就是事务管理器，先准备好数据交流，然后双方开始提交，提交完成后告诉事务管理器，现在有两个，如果只有一个，那么久提交失败，回退即可。**但是这种两段式还是有点问题的，都是在事务管理器要求你干什么就干什么，比如准备就绪了，事务管理器要你提交，结果有一台机器出问题，你怎么知道这个问题是在提交前还是提交后出现的，提交后出现的那就不用回滚了，提交前的那就要回滚。基于上述缺点，出现了三段式事务，但是都是基于两段式，只不过把第一段分成了两部分，第三段和两段式的第二段一样。三段式的第一阶段是canCommit阶段，事务管理器会给所有事务参与者发送canCommit，各个事务管理者根据自己的状态查看能否提交，如果可以则回执OK，否者返回fail，并不开启本地事务并执行。如果所有的都正常，则进入第二阶段，否则停止；第二阶段即是preCommit，事务协调器向所有参与者发起准备事务请求，参与者接受到后，开启本地事务并执行，但是不提交。第三阶段则是提交了。**
##### 基于XA的分布式事务
![image](https://upload-images.jianshu.io/upload_images/10624272-716531f70ada1ab8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
本质上讲还是一个两段式的提交。这个流程和前面的二三段其实是差不多的，首先询问准备好没有，准备好回个OK，然后提交执行。流程差不多，但是调用方式出现了变化，但是不常见，应用场景多，MySQL，DB2这些关系型数据库绝大多数都是基于XA来的。
##### 基于消息一致性方案的分布式事务
![image](https://upload-images.jianshu.io/upload_images/10624272-84ce2b4641657f8e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里和前面介绍的几种方式有所不同，参与者有两个A系统和消息中间件两个，首先系统A发送预备消息，中间件保存预备消息后返回说我已经收到了。接着执行本地事务，执行后把执行结果告诉消息中间件，不一定是成功的，可能成功也可能失败，消息中间件保存消息回调。可能会觉得回调和保存消息很多余，我看到这个图也是这么想的，因为执行事务无非就是成功和失败，为什么要回调保存？但是别忘了，分布式事务是多个事务之间的关系，我这里只是一个事务，将多个事务结合在一起：
![image](https://upload-images.jianshu.io/upload_images/10624272-ae04b3df5b633129?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
如果有一个B系统，那么当A系统执行完成了，消息中间件通知了B：系统A执行完成了，然后B再执行。**在整个售票系统中有一个支付系统，钱到账了修改订单状态，但是如果钱到账了修改订单状态失败了怎么办？这个时候就可以使用消息一致性了，可以在支付宝下订单支付的时候暂停线程，启动另外一个线程进行修改订单操作。修改成功了才启用支付宝线程。这种方案是强一致性方案，同一个时刻成功一定成功，失败一起失败，不会存在其他情况，但是缺点也很明显，会存在等待时期，会使得支付宝线程等待，这样会影响性能。**
##### TCC补偿性事务
![image](https://upload-images.jianshu.io/upload_images/10624272-77b1d9186f777ea8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
主要进行的三个操作依次是Try接口，Confirm接口，cancel接口，confirm接口和cancel接口只能使用一个，事务要么成功要么失败。**首先启动事务协调器，告诉事务协调器要开始工作了，接着调用不同的服务，尝试进行操作，比如扣减库存和创建订单，结果A成功了，B失败了，那么业务就会调用cancel接口，成功了调用从confirm接口。try，confirm，cancel接口都是在服务里面实现，业务只是去调用这些接口，关心返回结果，根据返回结果确定是调用confirm还是cancel接口。cancel接口把try做过的东西全部取消，confirm确认提交，所以也称为是补偿式。**
**基于消息一致性的事务是一种强一致性的事务，很大程度上会造成资源的浪费，尤其是对于时间的浪费，上面的例子是两个事务，如果发展到多个事务，等待的时间就会更多了。但是他的优点也很明显，就是强一致性，缺点也是强一致性。在实际工程中经常会有对接京东支付，阿里支付等等的场景，假设使用TCC，那么问题来了，钱打进去是回不来的，想要调用cancel接口那只能自己掏腰包，而消息一致性就没有这种问题。TCC补偿性事务是柔性事务，在try阶段要对资源做预留，在确认和取消阶段释放资源，confirm没有什么，cancel做反向操作。相比基于消息一致性来说TCC的时效性更强。**
##### 主流的分布式框架
全局事务服务，GTS
蚂蚁金服分布式事务，DTX
开源TCC框架，TCC-Transaction
开源TCC框架，ByteTCC
这里使用TCC-Transaction开源框架。
![image](https://upload-images.jianshu.io/upload_images/10624272-9b1e061db217d0a3?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
api就是接口，core为核心包，类似于guns-core的核心，server是事务监控工具，有多少事务，事务状态，spring即是spring的支持，然后dubbo的支持，unit即为测试，tutorial教程。简要测试一下，打开简要教程，dbscripts里面执行SQL语句，会生成四个数据库：
![image](https://upload-images.jianshu.io/upload_images/10624272-e782378db7a28d7d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
第一个tcc的库是必须要建立的，只要使用就需要建立；下面的cap,ord,red分布模拟了业务场景，cap为资金账户，ord订单，red红包。tutorial里面有两个例子，一个是HTTP的例子，一个就是整合了dubbo的例子，HTTP的例子没有什么问题，很简单。看看dubbo的例子，首先先要部署Tomcat：
![image](https://upload-images.jianshu.io/upload_images/10624272-977fbe0addfc99d8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
order模块的前缀/即可。打开web.xml，发现三个模块的web.xml都没有报错，是由于执行顺序的问题，错误提示**{**The content of element type "web-app" must match "(icon?,display-name?,description?,distributable?,context-param*,filter*,filter-mapping*,listener*,servlet*,servlet-mapping*,session-config?,mime-mapping*,welcome-file-list?,error-page*,taglib*,resource-env-ref*,resource-ref*,security-constraint*,login-config?,security-role*,env-entry*,ejb-ref*,ejb-local-ref*)".- No grammar constraints (DTD or XML schema) detected for the document**}**，listener要再filter-mappering和servlet之间，调整位置就好了，启动跑起来环境就搭建好了。现在就是要按照案例里面的代码把自己的项目改造一下。首先查看一下工程结构：
![image](https://upload-images.jianshu.io/upload_images/10624272-02b812b472d43e67?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
api和之前业务里面的api接口没有什么区别，注意有一个接口：
![image](https://upload-images.jianshu.io/upload_images/10624272-82e4ce878d53d5d1?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
带有compensable标签，即是需要进行事务处理的接口，RedPacketAccountServiceImpl这个接口实现是查询红包信息而已，不需要事务支持。
![image](https://upload-images.jianshu.io/upload_images/10624272-4e3ccfbf8d543f89?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
当前注解的方法就是try方法，就是业务方法，注解提到的confirmMethod，cancelMethod就是TCC，最后一个参数是事务上下文支持，这里使用的是隐式上下文的支持，可能这里需要用到隐式参数传递，接下来下面就要实现两个confirm和cancel两个方法，而且必须在同一个类里，因为注解绝大多数是要通过放射和AOP来读取，如果不在同一个类里面，是没有办法找到的，类是通过包名加上类名找到的，只返回一个字符串就能找到是不可能的，所以只有放在同一个类里面才能找到
![image](https://upload-images.jianshu.io/upload_images/10624272-958f631dbacdf5b1?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
record也就是try业务，注意catch里面不是捕获所有的异常，业务TCC是根据异常的有无来判断业务执行成功或者失败，有异常才会回滚，对于业务返回的结果不关心。这里的订单多出两种状态，draft草稿和cancel取消，draft即是try完但是没有confirm的订单，confirm完成就是真正支付完成的订单了。
![image](https://upload-images.jianshu.io/upload_images/10624272-fb98a8d4801db4c7?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
注意在cancel和confirm里面都需要判断订单是空而且订单状态为draft草稿状态。**但是这里涉及到一个服务幂等性的问题，即是一个服务重复多次执行和一次执行的结果相同，幂等性还不是理解的很好，做完这个服务再去看看。**所以TCC的分布式事务需要注意两个部分，**1、分布式事务里，不要轻易在业务层捕获所有异常，2、使用TCC-Transaction时，confirm和cancel的幂等性需要自己代码支持。**然后部署Tomcat，运行即可，注意TCC-trancation-order这个模块，在部署artificial的时候路径一个斜杠就好了。
测试完成之后就可以仿造加到这个项目上了。首先是打包，idea里面maven的package功能打包：
![image](https://upload-images.jianshu.io/upload_images/10624272-3cdfc57530f05e78?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
控制台用mvn install进去，当然了，最简单的还是直接接idea里maven install进去就好了。需要把刚刚的几个tcc-transaction打包成jar加进原来的工程里面才能使用tcc事务。**（2020.1.29.凌晨1:20）**
##### 部署环境
部署环境有点麻烦，比springboot麻烦多了，再加上文档东写一点西写一点的，调试的头都大了。install进去之后设置自己的tcc-transaction版本，我的设置是1.2.11版本，引入包之后出现了些问题：
![image](https://upload-images.jianshu.io/upload_images/10624272-44a0ccca84910af8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**这个问题之前也遇到过，一般就几种情况，jdk版本不一致（这个可能性最高）；pom包之间互相引用，但是调用的版本不一致；环境不一致；log4j的包没有导入;之前的原因是因为Tomcat使用jdk1.7，可是项目环境1.8，导致的问题，这里gteLogger方法上网百度了一下，com.alibaba.dubbo.common这个包一直以来的重大更新都没有去掉这个方法，LoggerAdapter这个类下一个版本将会淘汰，但是现在还有，不应该出现问题。那可能就是我打包的tcc-transaction的jar有问题了，翻了一下tcc-transaction的几个项目，com.alibaba.dubbo这个以来全都依赖到，但是版本也都一致的，而我本来项目环境也没有用到这个包，是直接引入alibobo-springboot的dubbo包的，很可能就是springboot的dubbo包的冲突导致。上网看了一下好像没有找到有出过这样的问题，但是有出现成功的例子，他的版本是1.2.4.23，拿来试了一下，结果就好了。应该是整合了nutz所导致的。在package tcctransaction的时候可能有些错误，需要在server目录下面建立config/dev目录，为什么要建立我也不知道，不创建他就提示错误，找不到目录。**
接下来就是按照部署文档把项目部署上去，首先是把tcc-transaction-dubbo和tcc-transaction-spring目录下两个xml的文件拷贝过来。还要读到springboot容器里面，而主要要进行分布式事务的就是支付模块了，所以在guns-order加上一个config类，用于读取配置文件：
![image](https://upload-images.jianshu.io/upload_images/10624272-d505c39c4a8dba3d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
启动项目，出现错误![image](https://upload-images.jianshu.io/upload_images/10624272-fba9c8913ba245e0?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
就说明是读取成功了，还需要配置一个数据源的支持，transaction repository。按照文档把bean拿过来：
```
<bean id="transactionRepository"
      class="org.mengyun.tcctransaction.spring.repository.SpringJdbcTransactionRepository">
    <property name="dataSource" ref="dataSource"/>
</bean>

<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
      destroy-method="close">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://127.0.0.1:3306/test"/>
    <property name="username" value="root"/>
    <property name="password" value=""/>
</bean>
```
BasicDataSource改成自己的数据源Alibaba那个，想用其他的也可以。dataSource需要单独配置，不能和业务里使用的dataSource复用,即使使用的是同一个数据库。JOB恢复也要配置上，还有一些表空间等等，配置完成之后还需要创建表，tcc-transaction需要使用自己的一张表来存储数据，需要自己创建。
![image](https://upload-images.jianshu.io/upload_images/10624272-f66ff65e08d148af?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
tbSuffix后缀，这个模块是订单模块，那么后缀就是order，创建的表就是tcc_transaction_order，还需要加上数据源配置：
![image](https://upload-images.jianshu.io/upload_images/10624272-b3ca2a48bf37bf06?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
还有一些零零散散的配置加上即可，启动一下没有问题即可。这是服务提供者的配置，消费者的配置也是一样的，gateway写上相同的配置即可。
*****
**在gateway启用相同配置并且启动的时候，问题来了，在启动的时候出现了问题：**
```
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/C:/Users/zhanggong004/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/C:/Users/zhanggong004/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
Exception in thread "main" java.lang.IllegalArgumentException: LoggerFactory is not a Logback LoggerContext but Logback is on the classpath. Either remove Logback or the competing implementation (class org.slf4j.impl.Log4jLoggerFactory loaded from file:/C:/Users/zhanggong004/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar). If you are using WebLogic you will need to add 'org.slf4j' to prefer-application-packages in WEB-INF/weblogic.xml: org.slf4j.impl.Log4jLoggerFactory
	at org.springframework.util.Assert.instanceCheckFailed(Assert.java:655)
	at org.springframework.util.Assert.isInstanceOf(Assert.java:555)
	at org.springframework.boot.logging.logback.LogbackLoggingSystem.getLoggerContext(LogbackLoggingSystem.java:286)
	at org.springframework.boot.logging.logback.LogbackLoggingSystem.beforeInitialize(LogbackLoggingSystem.java:102)
	at org.springframework.boot.context.logging.LoggingApplicationListener.onApplicationStartingEvent(LoggingApplicationListener.java:220)
	at org.springframework.boot.context.logging.LoggingApplicationListener.onApplicationEvent(LoggingApplicationListener.java:199)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:127)
	at org.springframework.boot.context.event.EventPublishingRunListener.starting(EventPublishingRunListener.java:69)
	at org.springframework.boot.SpringApplicationRunListeners.starting(SpringApplicationRunListeners.java:48)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:302)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1260)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1248)
	at com.example.dubboconsumer.DubboConsumerApplication.main(DubboConsumerApplication.java:13)
 
```
**因为上面那两个binding一直都有，我也就没有在意，觉得应该不是那两玩意的问题，可能是新加入的pom依赖导致的，把依赖去掉，就没有这个问题了，那么剩下就是在依赖里面找找到底是引入哪个依赖导致，一个一个尝试发现是tcc-transaction-dubbo，tcc-transaction-core，tcc-transaction-spring这三个依赖导致，就是tcc-transaction三个项目导致，看错误提示应该是log4j日志包冲突导致的，由于dubbo，spring这两包都依赖了core包，那么把core包的log4j的依赖exclude即可：**
![image](https://upload-images.jianshu.io/upload_images/10624272-83ce4b3104e74e89?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**我不知道是哪个包有slf4j，所以全部加上了，还是没用。然后仔细读了一下错误提示，大概意思是说有两个类重复了，StaticLoggerBinder.class，slf4j和loggback都有，但是找错了，找到了slf4j的，错误已经提示了：**
```
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
```
**自动绑定了slf4j的，其实就是包冲突，那么把logback的包删掉就好了。然而这个项目引用了大量的依赖，每一个依赖都有可能自带了logback，到这里还是懵懵懂懂的，于是只好查阅了一下源码，既然是日志，那肯定与监听相关，直接找LoggingApplicationListener相关，直接找过去：**
![image](https://upload-images.jianshu.io/upload_images/10624272-61d1cada67c95b54.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
启动时会通过LoggingSystem加载，查看LoggingSystem，![image](https://upload-images.jianshu.io/upload_images/10624272-0114b105fc728122?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**这个systems里面有三个日志类，默认读取的是第一个logback，但是logback和log4j-slf4j都有项目的implement function，即StaticLoggerBinder方法，结果项目自动绑定了StaticLoggerBinder方法，导致加载logback的时候找不到，所以报错。确认一下，查找一下第一次出错的地方：**
![image](https://upload-images.jianshu.io/upload_images/10624272-a5005b4e7347ebcc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**果然，类型不匹配之后就断言出错了。那么现在的问题就只需要把log4j-slj4f包exclude掉即可。然而各个包纵横交错，要全部删掉或者exclude掉很麻烦，使用maven提供的工具，右键maven-》show dependencies**


![image](https://upload-images.jianshu.io/upload_images/10624272-423435f44537a1de?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**红线的即是版本不一致冗余的包，找到log4j-slf4j的包exclude就好了，还有一个maven helper也可以。再启动项目：**
![image](https://upload-images.jianshu.io/upload_images/10624272-e97a763399ae80ce?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**好了。测试一下之前的业务，发现又出现新问题了，这次是数据库问题：**
![image](https://upload-images.jianshu.io/upload_images/10624272-ed0e90f9095d3591?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**显示连接到了tcc.order_t的库上面，很明显，是加载到错误的数据源了，就是刚刚的配置的数据源bean的问题：**
![image](https://upload-images.jianshu.io/upload_images/10624272-b53aec35e9b71931?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**显示id重复，已经有一个了，修改id号。修改完又有问题，tcc-transaction倒是找不到了，所以还是得读源码，把tcc-transaction的数据源改了。其实有一个更简单的方法，使用同一个数据库即可，但是为了方便，还是改读取的bean吧。bean的读取方式记得有好几种，可以直接读xml文件再用getbean方法，也可以用WebApplicationContextUnil来获取。然而我读了半天，根本没有读入的操作，可能是自动转配，注意到**![image](https://upload-images.jianshu.io/upload_images/10624272-1b553f80cd0939cd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/10624272-15e11035ae4a8eff?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**这就一目了然了，先引进了TransactionRepository，TransactionRepositor再被自动转配进程序里面。然后改成dataSource_transaction即可。**
接下来就是编写事务程序了，tcc-transaction由三部分组成，try，confirm，cancel三部分组成，try就是本身的业务：
![image](https://upload-images.jianshu.io/upload_images/10624272-d3bfd11aadf4bacf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
仿造tcc-transaction的订单状态，多添加一个草稿状态，那么在下订单的时候，订单状态设置成草稿状态
![image](https://upload-images.jianshu.io/upload_images/10624272-6a8e4d3d0e5cf614.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在confirm的时候再改成其他的状态
![image](https://upload-images.jianshu.io/upload_images/10624272-abe7c4ff5e1c3ace?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**要注意服务幂等性，服务的幂等性需要confirm，cancel自己实现，所以在确认之前需要判断是不是草稿状态，其他就都按部就班了，cancel也是。如果是草稿状态，那就变成已关闭或者是未支付状态，如果刚下好的订单不是草稿状态，那就是系统出问题了。测试一下出现问题了：**
```
java.lang.RuntimeException: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.transaction.PlatformTransactionManager' available
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.transaction.PlatformTransactionManager' available

```
**提示没有指定事务管理器，之前是不需要指定的，因为默认transactionManager就是事务管理器bean的id，不需要指定，可能是事务冲突了，tcc有一个，springboot本身自带也有一个，那么就指定一个名字吧**
![image](https://upload-images.jianshu.io/upload_images/10624272-5424d5bfea8a72b6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**但是还是不行，还是相同的错误，后来去找了文档发现，和dubbo结合是不需要@transactional注解的，去掉就没事了。再测试就没有问题了。**
![image](https://upload-images.jianshu.io/upload_images/10624272-62c88a2fdf308b1d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/10624272-b7cb48fac288d687.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/10624272-1f9dd15eefece4ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**日志都提示成功了。**
到这来差不多就实现完成了，总的来说，tcc包含四个组成部分，事务拦截器，事务管理器，事务存储器，事务处理job![image](https://upload-images.jianshu.io/upload_images/10624272-689943369f1114f2?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
核心就是事务处理器，当事务存储器存储了数据之后，事务管理和事务处理只和事务存储器有关，job对事务数据进行恢复。不管是什么业务，但凡是需要事务，就会被事务拦截器拦截到，处理后给到事务管理器，事务管理器存储数据，然后JOB会针对不同事务对事务存储器的内容做处理，一个处理事务前，一个处理事务后。
# 阅读源码
首先看一下事务存储器，这个应该是最简单的了![image](https://upload-images.jianshu.io/upload_images/10624272-f3333be42d306a21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在core核心包的repository就是存储器了，提供了缓存，文件，jdbc，zookeeper五种分布式存储，上面使用的是jdbc方式，但是FileSystem是很明显不可取的，因为分布式事务是分布式组件体系里面的一部分，如果存在本地，那就意味着这个只能在同一个机器里面的，cache差不多也是本地的意思，Redis可以考虑，zookeeper也不建议，因为transaction数据变动很大的，zookeeper是强一致性的组件，如果频繁读取，那么对集群压力很大。所以一般就是jdbc和redis。
然后其次看一下事务拦截器![image](https://upload-images.jianshu.io/upload_images/10624272-f71a5f251087b280?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
首先，拦截器分成两种，Compensable和Resource两种，Compensable是注解事务拦截器，resource是资源拦截器，**资源是事务里面很重要的东西，在TCC中try就是用来预留资源的，比如在处理业务的时候，try不会把所有问题都解决掉，会把一部分不能解决的问题的相关数据资源存在库里面，加上版本号或者是状态。地下面的都是事务参与者了。**![image](https://upload-images.jianshu.io/upload_images/10624272-945ba44f0405caa9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
compensable有两个，CompensableTransactionAspect和CompensableTransactionInterceptor，CompensableTransactionAspect是一个aop的切面
![image](https://upload-images.jianshu.io/upload_images/10624272-eb097a2e682500a4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在带有compensable注解的方法上切面下去，接下来就是Around方法了
![image](https://upload-images.jianshu.io/upload_images/10624272-11976f05080bfb13.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/10624272-7932842b87c25c0f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在切点开始和结束都要经过这个Compensable的拦截器。这些注解都是spring的注解。在切点开始结束都会调用        compensableTransactionInterceptor.interceptCompensableMethod(pjp);方法，进去看看
![image](https://upload-images.jianshu.io/upload_images/10624272-346ce69cc93e3887?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
首先传入一个代理对象，pjp就是代理的目标，然后用getCompensableMethod方法获取对象的名字，这里的pjp可以抽象成很多对象，只要带有了compensable的就是可以被拦截，在本次业务可能就是方法了，那么getCompensableMethod就是获得方法的名字，然后getAnnotation获得注解，之前还以为是用反射获取注解，因为spring基本都是用反射取得包名什么的。Compensable是事务对象，里面有一个事务传播级别，默认是Request，发现如果有事务就用已经有的事务，如果没有就重启一个事务。接下来就是compensable.propagation()获取这个事务的传播级别了。接下来那句有点看不懂：
```
        TransactionContext transactionContext = FactoryBuilder.factoryOf(compensable.transactionContextEditor()).getInstance().get(pjp.getTarget(), method, pjp.getArgs());

```
![image](https://upload-images.jianshu.io/upload_images/10624272-37da731f6db8ff03?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
serialVersionUID是序列化版本号，TransactionXid为事务ID，attachments存储子事务，所以这个方法就是获取事务上下文，也可以说是获取事务本身。第一次知道事务居然是这样存储，还以为是全部存储在一个数据库里面。然后就是这句代码了，FactoryOf有点像是工厂模式，剩下就是判断是否是异步提交。事务处理都说通过事务管理器，而且不同事务之间是有隔离性的，所以接下来就是判断是不是已经存在一个事务
```
        boolean isTransactionActive = transactionManager.isTransactionActive();

```
这一句就是判断是否有事务存在![image](https://upload-images.jianshu.io/upload_images/10624272-ef61da1a103ae4de.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
具体实现也很简单，CURRENT就是threadlocal，只要保证线程安全就能保证主从事务的隔离性。接下来这一段很重要，是用来判断用户角色的：
```
        MethodType methodType = CompensableMethodUtils.calculateMethodType(propagation, isTransactionActive, transactionContext);
```
角色有4种：
![image](https://upload-images.jianshu.io/upload_images/10624272-69c1cac7b921590e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
但实际上只关心root和Provider两个，root是主事务，Provider即为事务参与者，事务参与者也叫分支事务，比如在tcc的sample例子，order就是主事务，redPacket就是分支事务了；我售票系统里面，buyticket就主事务，里面的判断和下订单都是分支事务。因为每一个注解了compensable的方法都会进来，所以是需要判断主从事务。
![image](https://upload-images.jianshu.io/upload_images/10624272-5f0dac0b005497f0?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/10624272-67711037a25dfd3e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
进入root主事务执行的方法
```
            transaction = transactionManager.begin();
```
既然是主事务，那就直接开启一个全新的事务。**首先创建一个事务对象，然后存储到数据库，然后把事务把他放在threadlocal里面，也就是刚刚的CURRENT对象里面，这个过程也叫注册。**
```
                returnValue = pjp.proceed();
```
然后就是执行目标方法了。如果有异常，那就rollback，没有异常就commit提交，最后别注意要清除，因为这个是主事务，主事务都执行完了，那么分支事务肯定也执行完了，所以要在队列中清除事务。**到这里root要执行的步骤都执行完了，总的来说就那么几件事：开启全局事务，持久化全局事务，注册全局事务，判断应该是confirm还是cancel，清除事务。注意commit只是调用自己本身的confirm，不调用子事务的。**
分支事务也是一样：
![image](https://upload-images.jianshu.io/upload_images/10624272-a836a7153264c307?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这个比较简单，判断是try，confirm还是cancel，分别执行不同的事情。propagationExitBegin其实就是修改事务状态。
然后就是Resource资源拦截器了，Resource的资源拦截器的 ResourceCoordinatorAspect也是和compensable一样![image](https://upload-images.jianshu.io/upload_images/10624272-f0a65bfb7c675a3d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
直接看切面后执行的程序，首先从事务管理器获取当前事务，注意，两个intercept之间是不能直接传递参数的，这里的intercept也就是compensable和Resource这两个，所以只能从事务管理器获取事务对象。confirm和cancel都不执行，只是执行try的数据，**到目前为止，两个处理器都没有执行cancel和confirm的方法，前面compensable里面的confirm和cancel只是改变事务的状态而已，正在执行我们自定义的confirm和cancel还没有执行。进入到方法**
![image](https://upload-images.jianshu.io/upload_images/10624272-4bff4d3aaf33552b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这确认了事务是try状态之后要执行的方法，传入的自然是方法对象了，然后获取注解，前面有一模一样的调用：
![image](https://upload-images.jianshu.io/upload_images/10624272-32bcdcb1af6c292b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
获取compensable对象，接着下面就是获取方法名，而在Java里面获取对应的方法唯一的方法是全限定名，也就是类名+包名+方法名，注意在compensable注解里面只是配置了一个方法名字，所以这两个confirm和cancel方法只能是和try同一个类下面，否则找不到包名和类名。
![image](https://upload-images.jianshu.io/upload_images/10624272-2b0c5297813b5be7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
然后是获取事务和事务的编号
![image](https://upload-images.jianshu.io/upload_images/10624272-ed91327799cdd954.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
判断是否有一个全局的事务上下文对象了，没有创建一个新的
![image](https://upload-images.jianshu.io/upload_images/10624272-dec44963db8c8f2f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
接着获取目标对象的class类，使用反射机制，其实就是一些实现类，比如一些业务里面的Impl类
![image](https://upload-images.jianshu.io/upload_images/10624272-6efdd425bfccce22.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
接下来就是要准备执行了，前面两句是保存confirm和cancel执行的上下文，大概就是要给某个人发消息，首先要告诉那个人的电话号码吧。Participant也是一个事务，上面也提到过另一个类型的事务，差不多，但是这里要传入cancel和confirm上下文以及事务的编号，很明显不是自己执行用的，自己执行直接就执行了，为什么要收集这么多信息，而且也不需要xid，当前事务就能获取xid的。
![image](https://upload-images.jianshu.io/upload_images/10624272-04d971a96dbe5d5b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
果不其然，下面就把所有的信息都给了事务管理器：
![image](https://upload-images.jianshu.io/upload_images/10624272-496f7c27ac62214e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**总起来，主要就是处理try阶段的事情，并把所有资源封装，包括了confirm和cancel的上下文信息，分支事务的信息，提交给事务管理器。enlistParticipant不用看了，就是把资源写进数据库里面，所以这个拦截器也没干啥，就是把资源放数据库里面，更新状态。**到这里基本上流程一半完成了，原来的流程是这样：
**CompensableTransactionInterceptor -> ResourceCoordinatorInterceptor -> 事务参与者 -> ResourceCoordinatorInterceptor -> CompensableTransactionInterceptor，又绕回来是因为两个拦截器都使用了Around，现在还差最后一个CompensableTransactionInterceptor没有走，回来看一下Compensable**
![image](https://upload-images.jianshu.io/upload_images/10624272-de8f1846ef15c032?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
进来拦截器的时候略过了rollback和commit，前面的流程和进来拦截器的是一样的，直接看看rollback
![image](https://upload-images.jianshu.io/upload_images/10624272-18ee4bad34f29f4c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
获取当前事务，改变事务状态为cancel，更新事务状态，接下来就是rollback了，cancel方法害得分异步和同步，但是无论是哪个都会执行rollbackTransaction，直接看rollbackTransaction
![image](https://upload-images.jianshu.io/upload_images/10624272-de8bffab0041d3ac?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
进去rollback看看
![image](https://upload-images.jianshu.io/upload_images/10624272-beb60c30fc3a53a6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
找到所有的子事务，进行rollback操作![image](https://upload-images.jianshu.io/upload_images/10624272-c77d711a782f0804.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在这个方法就执行了我们的cancel或者是commit方法，所有这两句话是真的去执行了两个预设的方法。**总的来说，CompensableTransactionInterceptor（组织了事务上下文，注册初始化事务） -> ResourceCoordinatorInterceptor（组织事务参与者等资源） -> 事务参与者 -> ResourceCoordinatorInterceptor（这里什么也没做，因为Resource只是在try阶段做了东西） -> CompensableTransactionInterceptor（执行cancel和confirm）**





























































































































































