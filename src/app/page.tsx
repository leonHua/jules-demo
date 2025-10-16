import Image from "next/image";
import ModuleCard from "@/components/ModuleCard";
import ConnectingPath from "@/components/ConnectingPath";

const modules = [
  {
    number: "1",
    title: "Java核心",
    items: [
      "培养目标：能够完成基于面向对象思想的JavaSE项目开发",
      "项目列表：吃饭联盟订餐系统、汽车租赁系统、QuickHit、会员",
      "职场晋升力：掌握Java SE核心，打下坚实基础，能够独立完成面向对象编程思想的程序设计，为后续学习Java EE高级技术、Android移动端开发、大数据技术等奠定坚实基础。",
      "技能点：JavaSE语法、变量、运算符、流程控制、面向对象、数组、集合、常用API、IO流、多线程、网络编程、单元测试、JVM内存模型、设计模式、数据结构与算法、WSOOP方法论、代码版本管理、项目管理、编码规范、团队协作"
    ],
  },
  {
    number: "2",
    title: "Java Web开发",
    items: [
      "培养目标：能够完成B/S架构的网站开发，完成中小型企业管理系统开发",
      "项目列表：图书馆里系统、在线考试系统、1号店、QQ会员页面、使用网络爬虫技术改进新闻发布系统",
      "职场晋升力：岗位认知、团队协作力、换位思考意识",
      "技能点：MySQL数据库高级编程、索引、视图、函数、数据库设计、Web前端技术(HTML、CSS、JavaScript、jQuery、Ajax、JSON)、Servlet、JSP、Filter、Listener、Session、Cookie、网络爬虫、工作流引擎、工作流思想、工作流应用、项目实战、上线下沟通、排期跟进、岗位思考"
    ],
  },
  {
    number: "3",
    title: "企业级应用框架",
    items: [
      "培养目标：能够使用流行的企业级框架SSM、SSH来完成企业真实项目开发",
      "项目列表：APP后台管理平台、易买网、互联网营销信息采集分析平台、SL会员商城、代理商管理系统",
      "职场晋升力：项目型和职场规划意识、细节管理",
      "技能点：MyBatis、Spring、SpringMVC、Hibernate、Struts2、框架原理、MVC设计思想、Spring IOC&AOP思想、RESTful架构、Maven高级应用、SVN&Git高级应用、Linux服务器搭建、ECharts报表分析、Linux常用命令、Linux项目部署、互联网项目整体发布流程、项目管理工具、项目管理流程、项目管理意识"
    ],
  },
  {
    number: "4",
    title: "前端后端分离开发模式",
    items: [
      "培养目标：能够掌握当前互联网行业中最为流行的前后端分离开发模式，深入理解微服务架构设计，可以胜任Java中、高级开发工程师岗位",
      "培养模式：以真实工作场景驱动，在线上完成真实企业级项目，进行迭代式开发",
      "职场晋升力：项目面试、试用期问题、激发提升学习动力",
      "技能点：Git、GitLab、Maven、Nexus、持续集成、Nginx反向代理、前后端分离、Nginx+Lua+GraphQL、前后端接口规范、RPC远程调用、Dubbo、SpringBoot&SpringCloud、Swagger、React.js、Webpack、代码生成器、RESTful&WebService标准、前后端分离思想、SSO、OAuth2授权标准、前后端分离项目部署"
    ],
  },
  {
    number: "5",
    title: "分布式微服务架构",
    items: [
      "培养目标：能够完成基于高并发互联网应用的项目的架构设计、技术选型、框架搭建及挑战开发，成为资深微服务架构的架构师",
      "培养模式：以真实高并发场景驱动学习，全程企业级分布式开发，微服务电商二次项目-闪电侠、二当家、火锅二次元社区项目",
      "职场晋升力：可以胜任一线互联网公司架构师、技术专家岗位",
      "技能点：Dubbo、Spring Cloud Greenwich(SR2)-Eureka、Feign、Ribbon、Hystrix、Zuul、Config、Sleuth、Gateway)、Spring Boot & Docker、Jenkins、CI/CD、分布式事务解决方案、分布式锁解决方案、分布式Session解决方案、分布式任务调度、分布式服务调用链追踪、分布式服务配置中心、分布式服务注册与发现、Sonar、并发编程、MySQL、NoSQL集群、MySQL读写分离、MySQL主从复制、MySQL性能优化、高并发解决方案、高可用解决方案、高扩展解决方案、高安全解决方案、Grafana、Spring Boot Admin、注册中心专题、OAuth2协议以及JWT规范、DDD领域驱动设计、OOP、响应式编程以及函数式编程、数据结构与算法、一线大厂编程范式、面试通道"
    ],
  },
  {
    number: "6",
    title: "CC服务",
    items: [
      "培养目标：终生学习服务，广度&深度，终身职场关怀，不断提升职场规划和职场晋升力，成为领域内当之无愧的专家",
      "技能点：NoSQL数据库解决方案(Redis、MongoDB、Neo4j图数据库、HBase分布式存储)、MQ消息队列解决方案(RabbitMQ、RocketMQ、Kafka)、搜索解决方案(ElasticSearch)、大数据解决方案、Config配置中心解决方案、缓存中间件解决方案、Python、Python网络爬虫、Python数据分析、人工智能、区块链、云计算、云原生、Serverless、一线大厂解决方案、持续集成..."
    ],
  },
];

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-[#0a0f2a] to-[#1a237e] text-white">
      <header className="p-4 flex justify-between items-center">
        <Image
          src="/logo.svg"
          alt="Logo"
          width={100}
          height={40}
        />
      </header>
      <main className="relative container mx-auto px-4 py-16 text-center">
        <ConnectingPath />
        <h1 className="text-3xl md:text-5xl font-bold mb-4">互联网架构师6.0</h1>
        <p className="text-lg md:text-xl text-gray-300">2020 薪火相承 助力职场 再出发</p>
        <p className="text-lg text-gray-400 mt-2">课程出口：系统架构师、Java高级开发工程师、Java中级开发工程师</p>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mt-16 z-10 relative">
          {modules.map((module) => (
            <ModuleCard
              key={module.number}
              number={module.number}
              title={module.title}
              items={module.items}
            />
          ))}
        </div>
      </main>
    </div>
  );
}