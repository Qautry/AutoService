# AutoService

# AutoService源码解析结合APT和SPI，参考：

对于java.util.ServiceLoader-style服务提供程序的配置/元数据生成器

## AutoWhat‽

[Java][java] 注释处理器和其他系统使用
[java.util.ServiceLoader][sl] 注册已知类型的实现使用META-INF元数据。然而，开发人员很容易忘记更新或正确指定服务描述符。\
AutoService为开发人员生成任何注释类的元数据具有 `@AutoService`, 避免打字错误，防止错误重构等。

## Example

例子:

```java
package com.zlj.autoservice;

import com.zlj.autoservice.MyServiceImpl;

@AutoService(MyServiceImpl.class)
public class MyServiceImplOne implements MyServiceImpl {
    
}
```

AutoService将生成文件`META-INF/services/javax.annotation.processing.Processor` 在输出类中

文件夹该文件将包含：

```
foo.bar.MyProcessor
```

对于javax.annotation.processing.Processor，如果此元数据文件是包含在一个jar中，
并且该jar位于javac的类路径中，则“javac”将 自动加载它，并将其包含在正常注释处理中
环境java.util.ServiceLoader的其他用户可以使用基础结构，但该元数据将适当地提供自动加载。

## Getting Started
你需要 `auto-service-annotations-${version}.jar` 在编译时
类路径，您将需要 `auto-service-${version}.jar` 在您的
注释处理器类路径。

[java]: https://en.wikipedia.org/wiki/Java_(programming_language)
[sl]: http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html
# https://github.com/google/auto/tree/master/service