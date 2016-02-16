/*
 * File created on Feb 16, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.ssl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ConcreteSSLContextBuilder}.
 *
 * @author Carl Harris
 */
public class ConcreteSSLContextBuilderTest {

  private static final String PASSWORD = "changeit";

  private int port;

  @Test
  public void test() throws Exception {
    SSLContext serverContext = createServerContext();
    SSLContext clientContext = createClientContext();

    ServerSocket listenerSocket = createServerSocket(
        serverContext.getServerSocketFactory());

    Socket clientSocket = createClientSocket(clientContext.getSocketFactory(),
        listenerSocket.getLocalPort());

    Socket serverSocket = listenerSocket.accept();
    listenerSocket.close();
    System.out.println("connection accepted");

    ExecutorService executor = Executors.newSingleThreadExecutor();
    final Future<String> future = executor.submit(new MessageReceiver(serverSocket));

    BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
    writer.write("hello");
    writer.newLine();
    writer.flush();
    clientSocket.close();

    assertThat(future.get(), is(equalTo("hello")));
    serverSocket.close();
  }

  static class MessageReceiver implements Callable<String> {

    private final Socket socket;

    MessageReceiver(Socket socket) throws IOException {
      this.socket = socket;
    }

    @Override
    public String call() throws Exception {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), "UTF-8"));
      return reader.readLine();
    }
  }


  private Socket createClientSocket(SocketFactory socketFactory, int port)
      throws IOException {
    return socketFactory.createSocket(
        InetAddress.getLoopbackAddress(), port);
  }

  private ServerSocket createServerSocket(ServerSocketFactory socketFactory)
      throws IOException {
    while (true) {
      int port = randomPort();
      try {
        return socketFactory.createServerSocket(port);
      }
      catch (BindException ex) {
        assert true;  // try again
      }
    }
  }

  private int randomPort() {
    return (int) ((65536 - 1024)*Math.random()) + 1024;
  }

  private SSLContext createClientContext() {
    return SSLContextBuilderFactory.newBuilder()
        .protocol("TLS")
        .credential()
          .type("JKS")
          .location("client-credential.jks", getClass())
          .password(PASSWORD)
          .end()
        .peerTrust()
          .type("JKS")
          .location("server-certificate.jks", getClass())
          .password(PASSWORD)
          .end()
        .build();
  }

  private SSLContext createServerContext() {
    return SSLContextBuilderFactory.newBuilder()
        .protocol("TLS")
        .clientAuthentication(SSLContextBuilder.ClientAuthentication.REQUIRED)
        .credential()
          .type("JKS")
          .location("server-credential.jks", getClass())
          .password(PASSWORD)
          .end()
        .peerTrust()
          .type("JKS")
          .location("client-certificate.jks", getClass())
          .password(PASSWORD)
          .end()
        .build();
  }


}
