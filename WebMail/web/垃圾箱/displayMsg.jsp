
<frameset rows="25%,*">
     <frame src="/WebMail/DisplayHead?msgnum=<%=request.getParameter("msgnum")%>" scrolling="no">
     <frame src="/WebMail/DisplayContent?msgnum=<%=request.getParameter("msgnum")%>" scrolling="no">
 </frameset>