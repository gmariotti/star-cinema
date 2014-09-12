<%@page import="java.time.LocalDate"%>
<%@include file="login_navbar.jsp" %>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement" />

<%
    if (loggedIn) {
        ticketBean.setUsername(username);
        try {
            ticketBean.getTicketList();
        } catch (Exception ex) {
        }
%>
<div class="jumbotron">
    <% if (!ticketBean.getMessage().equals("")) {%>
    <!-- Gestione Errori -->
    <div class="container">
        <div class="alert alert-dismissable <%=ticketBean.getMessagetype()%>">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
            <p class="message"><%=ticketBean.getMessage()%></p>
        </div>
    </div>
    <%}%>
</div>
<div class="container">
    <legend>Lista Ingressi</legend>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Data</th>
                <th>Film</th>
                <th>Sala</th>
                <th>Orario</th>
                <th>Posto</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <%
                int length = ticketBean.ticket_Length();
                for (int j = 0; j < length; j++) {
            %>
            <tr>
                <td><%=ticketBean.ticket_Data(j)%></td>
                <td><%=ticketBean.ticket_Titolo(j)%></td>
                <td><%=ticketBean.ticket_Sala(j)%></td>
                <td><%=ticketBean.ticket_Orario(j)%></td>
                <td><%=ticketBean.ticket_Posto(j)%></td>
                <td>
                    <%
                        LocalDate data = LocalDate.parse(ticketBean.ticket_Data(j));
                        LocalDate today = LocalDate.now();
                        if (data.isEqual(today) || data.isAfter(today)) {
                    %>
                    <!-- Form di modifica ingresso -->
                    <form action="updatetickettime.jsp">
                        <input type="hidden" name="id_ingresso" value="<%=ticketBean.ticket_IdIngresso(j)%>" />
                        <button type="submit" class="btn btn-default">Modifica</button>
                    </form>
                    <%
                        }
                    %>
                </td>
            </tr>
            <%}%>
        </tbody>
    </table>
</div>
<%    } else {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>
