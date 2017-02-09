/*     */ package org.apache.commons.httpclient.contrib.ssl;
/*     */ 
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ public class EasyX509TrustManager
/*     */   implements X509TrustManager
/*     */ {
/*  65 */   private X509TrustManager standardTrustManager = null;
/*     */   
/*     */ 
/*  68 */   private static final Log LOG = LogFactory.getLog(EasyX509TrustManager.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public EasyX509TrustManager(KeyStore keystore)
/*     */     throws NoSuchAlgorithmException, KeyStoreException
/*     */   {
/*  75 */     TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*  76 */     factory.init(keystore);
/*  77 */     TrustManager[] trustmanagers = factory.getTrustManagers();
/*  78 */     if (trustmanagers.length == 0) {
/*  79 */       throw new NoSuchAlgorithmException("no trust manager found");
/*     */     }
/*  81 */     this.standardTrustManager = ((X509TrustManager)trustmanagers[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   public void checkClientTrusted(X509Certificate[] certificates, String authType)
/*     */     throws CertificateException
/*     */   {
/*  88 */     this.standardTrustManager.checkClientTrusted(certificates, authType);
/*     */   }
/*     */   
/*     */ 
/*     */   public void checkServerTrusted(X509Certificate[] certificates, String authType)
/*     */     throws CertificateException
/*     */   {
/*  95 */     if ((certificates != null) && (LOG.isDebugEnabled())) {
/*  96 */       LOG.debug("Server certificate chain:");
/*  97 */       for (int i = 0; i < certificates.length; i++) {
/*  98 */         LOG.debug("X509Certificate[" + i + "]=" + certificates[i]);
/*     */       }
/*     */     }
/* 101 */     if ((certificates != null) && (certificates.length == 1)) {
/* 102 */       certificates[0].checkValidity();
/*     */     } else {
/* 104 */       this.standardTrustManager.checkServerTrusted(certificates, authType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public X509Certificate[] getAcceptedIssuers()
/*     */   {
/* 112 */     return this.standardTrustManager.getAcceptedIssuers();
/*     */   }
/*     */ }


/* Location:              C:\Users\behnama\Desktop\server\axosoft-server-1.0.jar!\org\apache\commons\httpclient\contrib\ssl\EasyX509TrustManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */