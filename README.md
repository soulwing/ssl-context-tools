ssl-context-tools
=================

[![Build Status](https://travis-ci.org/soulwing/ssl-context-tools.svg?branch=master)](https://travis-ci.org/soulwing/ssl-context-tools)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.soulwing/ssl-context-tools/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.soulwing%20a%3Assl-context-tools*)

This small library provides some convenient tools for configuring and creating
`SSLContext` objects.  

Custom SSL context objects are often needed when connecting to a server that 
uses a self-signed SSL certificate or when SSL mutual authentication is 
required. Creating an `SSLContext` requires many steps and fairly extensive 
knowledge of the Java Secure Socket Extension (JSSE) APIs. This library provides 
a convenient and easy to use `SSLContextBuilder` that handles the most common 
cases of SSL context configuration and creation.

The JSSE includes support for customizing the allowable SSL/TLS protocols and
cipher suites by configuring `SSLSocket` and `SSLServerSocket` instances.  A
system administrator can configure the allowed protocols and cipher suites for
all SSL sockets by editing the `java.security` properties file.  However, JSSE
provides little support that would allow an application to ensure that all SSL
sockets use a configured set of allowable protocols and cipher suites.

The `SSLContextBuilder` allows you to specify protocols and cipher suites to
include (or exclude) among those supported by the underlying JSSE.  
An `SSLContext` created from the builder will produce `SSLSocketFactory`, 
`SSLServerSocketFactory`, and `SSLEngine` instances that ensure that the
configured constraints on protocols and cipher suites are consistently applied.

This library is available via Maven Central and can be used in your project
by including the following dependency.

```
<dependency>
  <groupId>org.soulwing.ssl</groupId>
  <artifactId>ssl-context-tools</artifactId>
  <version>1.0.0</version>
</dependency>
```

Creating an `SSLContext`
------------------------

The basic steps in creating an `SSLContext` are as follows:

1. Obtain an `SSLContextBuilder` from `SSLContextBuilderFactory`
2. Invoke methods on the builder to configure the context according to your
   needs.
3. Invoke the `build` method to create the context.

This example creates an SSL context using default configuration.

```
SSLContext sslContext = SSLContextBuilderFactory.newBuilder()
    .protocol("TLS")
    .build();
```

Of course, this produces an SSL context that is equivalent to a context
produced by `SSLContext.getInstance("TLS")` so this doesn't look like much of
an improvement.

### Dealing with Self-Signed Certificates

Suppose we want an `SSLSocketFactory` that we'll use to create socket
connections to a server that uses a self-signed certificate. While you're 
probably not going to write all that socket handling code yourself, if you're
using a library such as Apache HTTP Components, it allows you to specify an 
SSL socket factory for just this sort of need.

Of course, we could ask our system administrator to add the server's certificate 
to the JVM's `cacerts` -- sysadmins love doing that sort of thing! Then we don't
need to do any custom configuration at all. But when using `ssl-tools` the 
necessary custom configuration is so easy, you won't want to bother your sysadmin!

First, we need to put the server's certificate into a key store as a trusted
certificate. If the certificate is in a file named `server.crt`, we can load it
into a key store using the following command at a shell prompt.

```
keytool -noprompt -import -file server.crt -keystore server-certificate.jks -storepass changeit
```

Put the key store into the root of your application's class path.

Next, we create a custom SSL context that specifies the trust store containing 
the server's certificate and then get an `SSLSocketFactory` from it that can be
used to connect to the server.

```
SSLContext sslContext = SSLContextBuilderFactory.newBuilder()
    .peerTrust()
        .type("JKS")
        .location("classpath:server-certificate.jks")
        .password("changeit")
        .end()
    .build();
    
SSLSocketFactory socketFactory = sslContext.getSocketFactory();
```

### Specifying a Credential

Web services exposed over HTTP commonly use SSL mutual authentication. When
connecting a client to such a web service, the client must present a certificate
credential. In order to access such a service, you'll need an `SSLSocketFactory`
that is configured with the client's credential.

When starting the JVM you can specify the `javax.net.ssl.keyStore` system 
property and its related properties to identify a key store that will be used
to present a credential when negotiating a session with an SSL peer. Of course,
this means that the credential will be presented to _every_ SSL peer, which is 
not always what you want. For example, you might need to present different
credentials to different peers when accessing more than one web service.

Instead of configuring the credential through system properties, you can create
a custom SSL context configured to use a given credential.

Suppose that your client's credential (certificate and corresponding private key)
is stored in a PKCS 12 key store named `client-credential.p12`. You can create a 
custom SSL context for this credential as follows.

```
SSLContext sslContext = SSLContextBuilderFactory.newBuilder()
    .credential()
        .type("PKCS12")
        .location("classpath:client-credential.p12")
        .password("changeit")
        .end()
    .build();
    
SSLSocketFactory socketFactory = sslContext.getSocketFactory();
```

If the password for the private key differs from the one used to access the
key store, you can specify it as the argument the `credential` builder method.

 
