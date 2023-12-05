# Annotations

Annotations are not strictly code.
However, usually they configure the framework to change its behavior quite a lot.
Hence, omitting annotations can cause sever bugs and so their effects should be covered by unit tests.

One obvious way to test this is to start application amd check its behavior directly.
Unfortunately, starting the application usually takes quite some time and testing a lot of different cases on this level is probably not a good idea.

As things like validation –e.g. via bean validation annotations–, and JSON parsing –e.g. via Jackson– both should be tested thoroughly and with a lot of example cases, this project demonstrates some ways to work around the need to start the application.
