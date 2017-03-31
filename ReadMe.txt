如何创建自己的一个库
1、首先在项目外部的build.gradle里面加入
  dependencies {
        classpath 'com.android.tools.build:gradle:2.1.0'
        classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'//加入Github的引用库
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }

 2、接下来在你的Lib目录下加入
 apply plugin: 'com.android.library'
 apply plugin: 'com.github.dcendents.android-maven'//声明把它作为引用库
 group='com.github.womendeaitaiwunai'//Github的名字

 3、把所有的东西都Share到Github上

 4、设置一个release的版本

 5、登录Jitpack把项目引入进去

 6、完成项目库的搭建