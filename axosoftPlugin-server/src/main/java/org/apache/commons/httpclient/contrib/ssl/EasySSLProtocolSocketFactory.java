/*     */ package org.apache.commons.httpclient.contrib.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import org.apache.commons.httpclient.ConnectTimeoutException;
/*     */ import org.apache.commons.httpclient.HttpClientError;
/*     */ import org.apache.commons.httpclient.params.HttpConnectionParams;
/*     */ import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EasySSLProtocolSocketFactory
/*     */   implements ProtocolSocketFactory
/*     */ {
/* 102 */   private static final Log LOG = LogFactory.getLog(EasySSLProtocolSocketFactory.class);
/*     */   
/* 104 */   private SSLContext sslcontext = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SSLContext createEasySSLContext()
/*     */   {
/*     */     try
/*     */     {
/* 115 */       SSLContext context = SSLContext.getInstance("SSL");
/* 116 */       context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
/*     */       
/*     */ 
/*     */ 
/* 120 */       return context;
/*     */     } catch (Exception e) {
/* 122 */       LOG.error(e.getMessage(), e);
/* 123 */       throw new HttpClientError(e.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private SSLContext getSSLContext() {
/* 128 */     if (this.sslcontext == null) {
/* 129 */       this.sslcontext = createEasySSLContext();
/*     */     }
/* 131 */     return this.sslcontext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 144 */     return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
/*     */     throws IOException, UnknownHostException, ConnectTimeoutException
/*     */   {
/* 180 */     if (params == null) {
/* 181 */       throw new IllegalArgumentException("Parameters may not be null");
/*     */     }
/* 183 */     int timeout = params.getConnectionTimeout();
/* 184 */     SocketFactory socketfactory = getSSLContext().getSocketFactory();
/* 185 */     if (timeout == 0) {
/* 186 */       return socketfactory.createSocket(host, port, localAddress, localPort);
/*     */     }
/* 188 */     Socket socket = socketfactory.createSocket();
/* 189 */     SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
/* 190 */     SocketAddress remoteaddr = new InetSocketAddress(host, port);
/* 191 */     socket.bind(localaddr);
/* 192 */     socket.connect(remoteaddr, timeout);
/* 193 */     return socket;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Socket createSocket(String host, int port)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 202 */     return getSSLContext().getSocketFactory().createSocket(host, port);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 217 */     return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 226 */     return (obj != null) && (obj.getClass().equals(EasySSLProtocolSocketFactory.class));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 230 */     return EasySSLProtocolSocketFactory.class.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\behnama\Desktop\server\axosoft-server-1.0.jar!\org\apache\commons\httpclient\contrib\ssl\EasySSLProtocolSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */