/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.intellij.openapi.diagnostic.Logger
 *  jetbrains.buildServer.issueTracker.AbstractIssueProvider
 *  jetbrains.buildServer.issueTracker.IssueData
 *  jetbrains.buildServer.issueTracker.IssueFetcher
 *  jetbrains.buildServer.issueTracker.IssueFetcherAuthenticator
 *  jetbrains.buildServer.issueTracker.IssueMention
 *  jetbrains.buildServer.issueTracker.IssueProvider
 *  jetbrains.buildServer.issueTracker.IssueProviderType
 *  jetbrains.buildServer.issueTracker.errors.RetrieveIssueException
 *  jetbrains.buildServer.serverSide.InvalidProperty
 *  jetbrains.buildServer.serverSide.PropertiesProcessor
 *  jetbrains.buildServer.serverSide.impl.LogUtil
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.axosoft.teamcity.axosoftPlugin;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.issueTracker.*;
import jetbrains.buildServer.issueTracker.errors.RetrieveIssueException;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.impl.LogUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class AxosoftIssueProvider
extends AbstractIssueProvider {
    public static String accessToken;
    private static final Logger LOG;
    private static final ReadWriteLock LOCK;

    public AxosoftIssueProvider(@NotNull IssueProviderType type, @NotNull IssueFetcher fetcher) {
        super(type.getType(), fetcher);
    }

    @NotNull
    protected Pattern compilePattern(@NotNull Map<String, String> properties) {
        properties.put("pattern", "\\[\\s*(ot|axo)(d|f|t|i)\\s*:\\s*([a-zA-Z0-9]+)\\s*(wl\\s*:\\s*(([0-9]+(\\.[0-9]+)?)\\s*([a-zA-Z]+)|(0*\\.[0-9]+)\\s*([a-zA-Z]+)))?\\s*\\]");
        Pattern result = super.compilePattern(properties);
        if (this.myFetcher instanceof AxosoftIssueFetcher) {
            AxosoftIssueFetcher fetcher = (AxosoftIssueFetcher)this.myFetcher;
            fetcher.setPattern(result);
            fetcher.setApiToken(properties.get("apiToken"));
        }
        return result;
    }

    @NotNull
    protected String extractId(@NotNull String match) {
        Matcher matcher = this.myPattern.matcher(match);
        matcher.find();
        return matcher.group(2) + matcher.group(3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    public IssueData findIssueById(@NotNull String id) {
        LOCK.readLock().lock();
        try {
            IssueData issueData = this.myFetcher.fetchIssue(this.myFetchHost, id, this.getAuthenticator());
            return issueData;
        }
        catch (RetrieveIssueException e) {
            LOG.info("Failed to fetch the some random useless texts: " + id + ", reason: " + e.getMessage() + ", " + "connection: " + LogUtil.describe((IssueProvider)this));
            LOG.debug(e.getMessage(), (Throwable)e);
            throw e;
        }
        catch (Exception e) {
            LOG.info("Failed to fetch the issue some random useless texts: " + id + ", reason: " + e.getMessage() + ", " + "connection: " + LogUtil.describe((IssueProvider)this));
            LOG.debug(e.getMessage(), (Throwable)e);
            IssueData issueData = null;
            return issueData;
        }
        finally {
            LOCK.readLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public Collection<IssueMention> getRelatedIssues(@NotNull String comment) {
        if (this.myPattern.equals(EMPTY_PATTERN)) {
            //LOG.warn("Invalid issue-tracker connection " + LogUtil.describe((IssueProvider)this) + " settings: " + "the pattern should not match empty string");
            return Collections.emptyList();
        }
        if (!this.quickCheck(comment)) {
            return Collections.emptyList();
        }
        LOCK.readLock().lock();
        try {
            Object foundGroup;
            Matcher matcher = this.myPattern.matcher(comment);
            ArrayList<String> processed = new ArrayList<String>(3);
            ArrayList<IssueMention> result = new ArrayList<IssueMention>(3);
            int lastMatched = 0;
            while (matcher.find(lastMatched)) {
                foundGroup = matcher.group();
                String id = this.extractId((String)foundGroup);
                id.substring(1);
                if (!processed.contains(id)) {
                    processed.add(id);
                    result.add(new IssueMention(id, this.myFetcher.getUrl(this.myHost, id)));
                }
                lastMatched = matcher.end();
            }
//            foundGroup = result;
            return result;
        }
        finally {
            LOCK.readLock().unlock();
        }
    }

    @NotNull
    public PropertiesProcessor getPropertiesProcessor() {
        return new PropertiesProcessor(){

            public Collection<InvalidProperty> process(Map<String, String> properties) {
                ArrayList<InvalidProperty> result = new ArrayList<InvalidProperty>();
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    String key = entry.getKey();
                    if (key.equals("accessToken")) {
                        AxosoftIssueProvider.accessToken = entry.getValue();
                    }
                    String value = entry.getValue();
                    if (key.equals("host")) {
                        if (value.length() == 0) {
                            result.add(new InvalidProperty(key, "Server URL must be specified"));
                            continue;
                        }
                        try {
                            new URL(value);
                        }
                        catch (MalformedURLException var8) {
                            result.add(new InvalidProperty(key, "A correct URL must be specified"));
                        }
                    }
                    if (key.equals("accessToken") && value.length() == 0) {
                        result.add(new InvalidProperty(key, "The Access Token must be specified"));
                    }
                    if (key.equals("name")) {
                        if (value.length() != 0) continue;
                        result.add(new InvalidProperty(key, "The connection name must be specified"));
                        continue;
                    }
                    if (!key.equals("pattern")) continue;
                    if (value.length() == 0) {
                        result.add(new InvalidProperty(key, "The pattern must be specified"));
                        continue;
                    }
                    if (!AxosoftIssueProvider.safeCompile((String)value).equals(EMPTY_PATTERN)) continue;
                    result.add(new InvalidProperty(key, "A correct reg.exp. pattern must be specified"));
                }
                return result;
            }
        };
    }

    static {
        LOG = Logger.getInstance((String)AbstractIssueProvider.class.getName());
        LOCK = new ReentrantReadWriteLock();
    }

}

