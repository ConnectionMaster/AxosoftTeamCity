/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  jetbrains.buildServer.issueTracker.AbstractIssueProviderFactory
 *  jetbrains.buildServer.issueTracker.IssueFetcher
 *  jetbrains.buildServer.issueTracker.IssueProvider
 *  jetbrains.buildServer.issueTracker.IssueProviderType
 *  org.jetbrains.annotations.NotNull
 */
package com.axosoft.teamcity.axosoftPlugin;

import com.axosoft.teamcity.axosoftPlugin.AxosoftIssueProvider;
import jetbrains.buildServer.issueTracker.AbstractIssueProviderFactory;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import jetbrains.buildServer.issueTracker.IssueProvider;
import jetbrains.buildServer.issueTracker.IssueProviderType;
import org.jetbrains.annotations.NotNull;

public class AxosoftIssueProviderFactory
extends AbstractIssueProviderFactory {
    public AxosoftIssueProviderFactory(@NotNull IssueProviderType type, @NotNull IssueFetcher fetcher) {
        super(type, fetcher);
    }

    @NotNull
    public IssueProvider createProvider() {
        return new AxosoftIssueProvider(this.myType, this.myFetcher);
    }
}

