<jsp:useBean id="issue" scope="request" type="jetbrains.buildServer.issueTracker.Issue"/>
<script type='text/javascript'>
    BS.LoadStyleSheetDynamically(base_uri + "/plugins/axosoftPlugin/style.css");
</script>

<div class="axosoftContainer">
 <h4>${issue.summary}</h4>
 <table cellspacing="1" cellpadding="3" >
   <tbody>
     <tr>
         <td><b>Id:</b></td>
         <td>${issue.id}</td>
     </tr>

     <tr>
         <td><b>Resolved:</b></td>
         <td>${issue.resolved}</td>
     </tr>

     <tr>
       <td><b>Status:</b></td>
       <td>${issue.state}</td>
     </tr>
    </tbody>
 </table>
</div>
