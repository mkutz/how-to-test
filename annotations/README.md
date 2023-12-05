# Annotations

Annotations are not strictly code.
However, usually they configure the framework to change its behavior quite a lot.
Hence, omitting annotations can cause sever bugs and so their effects should be covered by unit tests.

One obvious way to test this is to start application and check its behavior directly.
This is valid and necessary as the framework's behavior is changed by a lot of things beside the annotations (e.g. the classpath, the application.yml/application.properties or other configuration files, configuration classes that may implicitly depend upon each other, …).

E.g.:
https://github.com/mkutz/how-to-test/blob/e86f3d29d475baf026b6d212c14bf594518a309e/annotations/src/integrationTest/java/io/github/mkutz/howtotest/beanvalidation/FriendApiTest.java#L13-L58

Unfortunately, starting the application usually takes quite some time and testing a lot of different cases on this level is probably not a good idea.

As things like validation –e.g. via bean validation annotations–, and JSON parsing –e.g. via Jackson– both should be tested thoroughly and with a lot of example cases, this project demonstrates some ways to work around the need to start the application.
