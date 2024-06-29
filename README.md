# Important statement！！！
This is part of my graduation project and dissertation. It is for study and communication only. Plagiarism and Reprinting  is strictly prohibited！！！
这是我的毕业设计和学士论文的一部分，仅供学习交流使用，严禁抄袭！！！严禁搬运！！！（抄了你查重也过不去啊！！！！！别抄了别抄了/(ㄒoㄒ)/~~）

# Selenium based automated Web UI testing platform --kagayaki backend 
## business analysis
![image](https://github.com/amemiyakiyoshi1114/hikari/assets/72334251/14c7106f-95ab-4796-9c7c-3014610158c8)
## Platform architecture
![image](https://github.com/amemiyakiyoshi1114/hikari/assets/72334251/351eb359-c111-41b1-9ca0-f4e94ae60452)
## front-end 
see https://github.com/amemiyakiyoshi1114/hikaru.git

# environment and configuration
java17 + maven + springboot3 + mybatisplus + mvc + mysql8
Jenkins + Allure Report + Selenium4

# functiion and design
Automatic web UI test -> Selenium4,JAVA based script
CI/CD -> Jenkins,Declarative pipeline
Automatic report generation -> Allure Report，code in selenium script, report by Jenkins
Resource creation and scheduling -> The platform itself，Jenkins RESTful API

# point
Level-based resource partitioning and scheduling strategies 
Field redundancy replaces multi-table related queries

# Instructions
## test java project
see https://github.com/amemiyakiyoshi1114/test001.git for help~
## hikari Deployment and installation
suggest that using IntelliJ IDEA Ultimate!
1.make sure that java and maven is available in your environment,and the version of java is 17 and higher(11 will not adapt to Jenkins in 2024-11） and the version of maven is 3.9.5 or higher.
2.clone hikari, and create mysql database, modify the config in src/main/resources/application.properties with your own info.
3.take src/main/java/kiyoshi/webtest/hikari/domain as reference, create tables in createde database.
4.download maven library.
5.run the hikari in your env.
## Jenkins instance creation
1.install jenkins.war at https://www.jenkins.io/download/
2.java -jar jenkins.war ,run the jenkins,with guide create your account and download suggested plugins.
3.with your account, in your dashboard, create pipeline project using declarative pipeline,download Jenkins extesion plugin in follow.
### Jenkins Pipeline
pipeline {
    agent any
    triggers {
        GenericTrigger(
            genericVariables: [
              [key: 'ref', value: '$.ref'],
              [key: 'repositoryURL', value: '$.repositoryURL'],
              [key: 'branch', value: '$.branch'],
              [key: 'credentialsId',value: '$.credentialsId'],
              [key: 'testClass',value: '$.testClass']
            ],
            token: 'webhookTry' ,
            causeString: '$ref' ,
            printContributedVariables: true,
            printPostContent: true
        )
    }
    
    stages {
        stage('show-param') {
            steps {
                echo 'token参数：$token'
                echo '代码仓库：$repositoryURL'
                echo '代码分支：$branch'
            }
        }
        
        stage('down-sourcecode') {
            steps {
                echo '开始下载源码'
                checkout([$class: 'GitSCM', 
                    branches: [[name: '*/$branch']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [], 
                    submoduleCfg: [], 
                    userRemoteConfigs: [[credentialsId:'$credentialsId',url: '$repositoryURL']]])
                
                //开始测试
                //bat "mvn -Dmaven.test.failure.ignore=true clean package"
                //bat "mvn test -D test=$testClass"
            }
        }

        stage('test') {
            steps {
                // echo '开始下载源码'
                // checkout([$class: 'GitSCM', 
                //     branches: [[name: '*/$branch']], 
                //     doGenerateSubmoduleConfigurations: false, 
                //     extensions: [], 
                //     submoduleCfg: [], 
                //     userRemoteConfigs: [[credentialsId:'$credentialsId',url: '$repositoryURL']]])
                
                //开始测试
                //bat "mvn -Dmaven.test.failure.ignore=true clean package"
                bat "mvn test -D test=$testClass"
            }
             post {
                 always {
                 // 生成Allure报告
                    allure([
                      includeProperties: false,
                      jdk: '',
                      properties: [],
                      reportBuildPolicy: 'ALWAYS',
                      results: [[path: 'allure-results']]
                     ])
                 }
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                
            }
        }            
    }
}
### Jenkins extension plugin
name	version	usage frequency
Allure	2.31.1	5.97%
Build Authorization Token Root	151.va_e52fe3215fc	3.69%
Build Name and Description Setter	2.4.2	11.10%
Build Timeout	1.32	80.50%
Build Token Trigger	1.0.0	0.86%
Generic Webhook Trigger	2.2.0	11.50%
Git Parameter	0.9.19	20.70%
HTML Publisher	1.33	34.40%
Oracle Java SE Development Kit Installer	73.vddf737284550	76.30%
Pipeline	596.v8c21c963d92d	88.10%
Pipeline: Stage View	2.34	86.30%
