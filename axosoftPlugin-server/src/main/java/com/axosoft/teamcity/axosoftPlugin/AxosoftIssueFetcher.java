/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.diagnostic.Logger
 *  jetbrains.buildServer.issueTracker.AbstractIssueFetcher
 *  jetbrains.buildServer.issueTracker.AbstractIssueFetcher$FetchFunction
 *  jetbrains.buildServer.issueTracker.AbstractIssueProvider
 *  jetbrains.buildServer.issueTracker.IssueData
 *  jetbrains.buildServer.util.cache.EhCacheUtil
 *  org.apache.commons.httpclient.Credentials
 *  org.apache.commons.httpclient.HostConfiguration
 *  org.apache.commons.httpclient.HttpClient
 *  org.apache.commons.httpclient.HttpMethod
 *  org.apache.commons.httpclient.HttpsURL
 *  org.apache.commons.httpclient.URIException
 *  org.apache.commons.httpclient.methods.GetMethod
 *  org.apache.commons.httpclient.protocol.Protocol
 *  org.apache.commons.httpclient.protocol.ProtocolSocketFactory
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.axosoft.teamcity.axosoftPlugin;

import com.axosoft.teamcity.axosoftPlugin.AxosoftIssueProvider;
import com.intellij.openapi.diagnostic.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jetbrains.buildServer.issueTracker.AbstractIssueFetcher;
import jetbrains.buildServer.issueTracker.AbstractIssueProvider;
import jetbrains.buildServer.issueTracker.IssueData;
import jetbrains.buildServer.util.cache.EhCacheUtil;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AxosoftIssueFetcher
extends AbstractIssueFetcher {
    private static final Logger LOG = Logger.getInstance((String)AbstractIssueProvider.class.getName());
    private Pattern myPattern;
    private String apiToken;
    private boolean ignoreSSL;
    private String accessToken;

    public void setPattern(Pattern _myPattern) {
        this.myPattern = _myPattern;
    }

    public void ignoreSSL(boolean _doIgnore) {
        this.ignoreSSL = _doIgnore;
    }

    public void setApiToken(String _apiToken) {
        this.apiToken = _apiToken;
    }

    public AxosoftIssueFetcher(@NotNull EhCacheUtil cacheUtil) {
        super(cacheUtil);
    }

    @NotNull
    public IssueData getIssue(@NotNull String _host, @NotNull String _id, @Nullable Credentials _credentials) throws Exception {
        String url = this.getUrl(_host, _id);
        return this.getFromCacheOrFetch((Object)url, (AbstractIssueFetcher.FetchFunction)new AxosoftFetchFunction(_host, _id, this.apiToken));
    }

    public String getAPIURL(@NotNull String _host, @NotNull String _id) {
        String accessToken = AxosoftIssueProvider.accessToken;
        LOG.info("This should be the accessToken which is shown from getAPI method" + accessToken);
        StringBuilder url = new StringBuilder();
        url.append(_host);
        if (!_host.endsWith("/")) {
            url.append("/");
        }
        String letter = this.retrieveID(_id).substring(0, 1);
        url.append("api/v5/" + this.getKind(letter) + "?");
        url.append("access_token=" + accessToken + "&filters=id%3D");
        url.append(this.retrieveID(_id).substring(1));
        url.append("&columns=id%2Cname%2Cworkflow_step%2Cpercent_complete");
        return url.toString();
    }

    private String retrieveID(@NotNull String _id) {
        String Id = _id;
        Matcher matcher = this.myPattern.matcher(_id);
        if (matcher.find()) {
            Id = matcher.group(1);
        }
        return Id;
    }

    private String getKind(@NotNull String param) {
        StringBuilder returnValue = new StringBuilder();
        if (param.toUpperCase().equals("D")) {
            returnValue.append("defects");
        } else if (param.toUpperCase().equals("F")) {
            returnValue.append("features");
        } else if (param.toUpperCase().equals("I")) {
            returnValue.append("incidents");
        }
        return returnValue.toString();
    }

    public String getUrl(@NotNull String _host, @NotNull String _id) {
        String Id = this.retrieveID(_id);
        String letter = Id.substring(0, 1);
        StringBuilder url = new StringBuilder();
        url.append(_host);
        if (!_host.endsWith("/")) {
            url.append("/");
        }
        url.append("viewitem?id=" + Id.substring(1) + "&type=");
        url.append(this.getKind(letter));
        url.append("&force_use_number=true");
        return url.toString();
    }

    public class AxosoftFetchFunction
    implements AbstractIssueFetcher.FetchFunction {
        private String host;
        private String id;
        private String apiToken;

        public AxosoftFetchFunction(String _host, String _id, String _apiToken) {
            this.host = _host;
            this.id = _id;
            this.apiToken = _apiToken;
        }

        @NotNull
        public IssueData fetch() {
            String url = AxosoftIssueFetcher.this.getAPIURL(this.host, this.id);
            try {
                InputStream inputStream = this.fetchUrlWithAxosoftHeader(url);
                IssueData result = this.parseIssue(inputStream);
                return result;
            }
            catch (IOException e) {
                throw new RuntimeException("Error fetching issue data", e);
            }
        }

        private InputStream fetchUrlWithAxosoftHeader(String _url) throws IOException {
            try {
                HttpClient httpClient = new HttpClient();
                GetMethod httpMethod = null;
                if (_url.toLowerCase().startsWith("https:") && AxosoftIssueFetcher.this.ignoreSSL) {
                    HttpsURL url = new HttpsURL(_url);
                    Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory)new EasySSLProtocolSocketFactory(), url.getPort());
                    httpClient.getHostConfiguration().setHost(url.getHost(), url.getPort(), easyhttps);
                    httpMethod = new GetMethod(url.getPath());
                } else {
                    httpMethod = new GetMethod(_url);
                }
                httpMethod.setRequestHeader("X-Axosoft-API-Key", this.apiToken);
                httpClient.executeMethod((HttpMethod)httpMethod);
                return httpMethod.getResponseBodyAsStream();
            }
            catch (URIException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        private IssueData parseIssue(InputStream inputStream) {
            String test = "";
            String percent = "";
            String summary = "";
            String state = "";
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            try {
                String line;
                while ((line = bReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                test = builder.toString();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(test);
                percent = jsonObject.getJSONArray("data").getJSONObject(0).getString("percent_complete");
                summary = jsonObject.getJSONArray("data").getJSONObject(0).getString("name");
                state = jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("workflow_step").getString("name");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            boolean resolved = percent.equals("100");
            String url = AxosoftIssueFetcher.this.getUrl(this.host, this.id);
            String ID = AxosoftIssueFetcher.this.retrieveID(this.id);
            IssueData result = new IssueData(ID, summary, state, url, resolved);
            return result;
        }
    }

}

