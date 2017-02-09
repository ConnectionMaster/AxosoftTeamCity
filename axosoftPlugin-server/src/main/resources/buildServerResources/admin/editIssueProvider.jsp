<%@ page import="com.axosoft.teamcity.axosoftPlugin.AxosoftIssueProvider" %>
<%@ include file="/include.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>
  <table class="editProviderTable">
    <c:if test="${showType}">
      <tr>
        <th><label class="shortLabel">Connection Type:</label></th>
        <td>Axosoft</td>
      </tr>
    </c:if>

    <tr>
      <th><label for="name" class="shortLabel">Display Name: <l:star/></label></th>
      <td>
        <props:textProperty name="name" maxlength="100" style="width: 16em;"/>
        <span id="error_name" class="error"></span>
      </td>
    </tr>

    <tr>
      <th><label for="host" class="shortLabel">Server URL: <l:star/></label></th>
      <td>
        <props:textProperty name="host" maxlength="100" style="width: 16em;"/>
        <span id="error_host" class="error"></span>
      </td>
    </tr>

    <tr>
      <th><label for="accessToken" class="shortLabel">Access Token: <l:star/></label></th>
      <td>
        <props:textProperty name="accessToken" maxlength="100" style="width: 16em;"/>
        <span id="error_accessToken" class="error"></span>
      </td>
    </tr>
  </table>
</div>
