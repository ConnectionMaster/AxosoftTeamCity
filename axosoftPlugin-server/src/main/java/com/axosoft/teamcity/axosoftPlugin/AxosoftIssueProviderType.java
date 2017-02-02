/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  jetbrains.buildServer.issueTracker.IssueProviderType
 *  jetbrains.buildServer.web.openapi.PluginDescriptor
 *  org.jetbrains.annotations.NotNull
 */
package com.axosoft.teamcity.axosoftPlugin;

import jetbrains.buildServer.issueTracker.IssueProviderType;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

public class AxosoftIssueProviderType
extends IssueProviderType {
    @NotNull
    private final String myConfigUrl;
    @NotNull
    private final String myPopupUrl;

    public AxosoftIssueProviderType(@NotNull PluginDescriptor pluginDescriptor) {
        this.myConfigUrl = pluginDescriptor.getPluginResourcesPath("admin/editIssueProvider.jsp");
        this.myPopupUrl = pluginDescriptor.getPluginResourcesPath("popup-experimental.jsp");
    }

    @NotNull
    public String getType() {
        return "Axosoft";
    }

    @NotNull
    public String getDisplayName() {
        return "Axosoft";
    }

    @NotNull
    public String getEditParametersUrl() {
        return this.myConfigUrl;
    }

    @NotNull
    public String getIssueDetailsUrl() {
        return this.myPopupUrl;
    }
}

