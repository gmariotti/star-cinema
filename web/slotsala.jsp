<%@include file="login_navbar.jsp" %>

<jsp:useBean id="nowShowingManagement" scope="page" class="bflows.NowShowingManagement" />
<jsp:setProperty name="nowShowingManagement" property="*" />

<%
    String id_film = request.getParameter("id_film");
    if (id_film == null) {
        String redirect = new String("home.jsp");
        response.sendRedirect(redirect);
    }
    if (status.equals("addDate")) {
        nowShowingManagement.addShow();
    }
    nowShowingManagement.populateTheater();
    int num_sale = nowShowingManagement.numberOfTheater();
    String[] week = nowShowingManagement.getWeek();
%>

<% if (isAdmin) {%>
<!-- Jumbotron -->
<div class="jumbotron">
    <div class="container">
        <h2>Sala - Data - Ora</h2>
        <p>
            Seleziona la sala, la data, l'ora di inizio e l'ora di fine del film che vuoi inserire.
            <br/>
            Il film � <strong><%=nowShowingManagement.getTitolo_film()%></strong> con durata <strong><%=nowShowingManagement.getDurata_film()%></strong>
        </p>
    </div>
</div>
<div class="container">
    <div class="row">
        <form>
            <!-- Gestione Sale -->
            <div class="col-lg-12 col-md-12">
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Sala</label>
                    <select id="sala" name="sala" class="form-control" required="required">
                        <option selected="selected" disabled="true">Seleziona una Sala...</option>
                        <%for (int j = 1; j <= num_sale; j++) {%>
                        <option value="<%=j%>">Sala <%=j%></option>
                        <%}%>
                    </select>
                </div>
                <!-- Gestione Date -->
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Data</label>
                    <select id="data" name="data" class="form-control" required="required">
                        <option selected="selected" disabled="true">Seleziona una Data...</option>
                        <% for (int j = 0; j < week.length; j++) {%>
                        <option value="<%=week[j]%>"><%=week[j]%></option>
                        <%}%>
                    </select>
                </div>
            </div>
            <br/>
            <!-- Gestione Orario -->
            <div class="col-lg-12 col-md-12">
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Ora Inizio</label>
                    <div class="controls">
                        <input type="time" name="ora_inizio" class="form-control" required="required"/>
                    </div>
                </div>
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Ora Fine</label>
                    <div class="controls">
                        <input type="time" name="ora_fine" class="form-control" required="required"/>
                    </div>
                </div>
            </div>
            <input type="hidden" name="id_film" value="<%=id_film%>" />
            <input type="hidden" name="status" value="addDate" />
            <br/>
            <div class="col-lg-12 col-md-12">
                <button type="submit" class="btn btn-primary">Conferma</button>
                <a href="home.jsp" class="btn btn-warning">Annulla</a>
            </div>
        </form>
    </div>
</div>
<br/>
<div class="jumbotron">
    <!-- Gestione Occupazione Sale nella Settimana -->
    <div class="container">
        <%
            // creo gli href per la tabella
            String[] href = new String[week.length];
            for (int j = 0; j < href.length; j++) {
                href[j] = "#" + week[j];
            }
        %>
        <h3>Controlla per ogni sala e per ogni data, gli orari di occupazione.</h3>
        <br/>
        <div class="col-lg-10 col-md-10">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="<%=href[0]%>" data-toggle="tab"><%=week[0]%></a>
                </li>
                <% for (int j = 1; j < week.length; j++) {%>
                <li>
                    <a href="<%=href[j]%>" data-toggle="tab"><%=week[j]%></a>
                </li>
                <%}%>
            </ul>
        </div>
        <!-- Tab panes -->  
        <div class="col-lg-10 col-md-10">
            <div class="tab-content">
                <div class="tab-pane active" id="<%=week[0]%>">
                    <table class="table table-bordered table-hover">
                        <thead>
                            <tr class="info">
                                <th>
                                    Numero Sala
                                </th>
                                <th>
                                    Orari in cui � occupata
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int j = 0; j < num_sale; j++) {
                                    String[] inizio = nowShowingManagement.oraInizioTheater(j + 1, week[0]);
                                    String[] fine = nowShowingManagement.oraFineTheater(j + 1, week[0]);
                            %>
                            <tr>
                                <td>
                                    Sala <%=j + 1%>
                                </td>
                                <td>
                                    <% for (int p = 0; p < inizio.length; p++) {%>
                                    <%=inizio[p]%> - <%=fine[p]%> |
                                    <%}%>
                                </td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                </div>
                <% for (int j = 1; j < week.length; j++) {%>        
                <div class="tab-pane" id="<%=week[j]%>">
                    <table class="table table-bordered table-hover">
                        <thead>
                            <tr class="info">
                                <th>
                                    Numero Sala
                                </th>
                                <th>
                                    Orari in cui � occupata
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int m = 0; m < num_sale; m++) {
                                    String[] inizio = nowShowingManagement.oraInizioTheater(m + 1, week[j]);
                                    String[] fine = nowShowingManagement.oraFineTheater(m + 1, week[j]);
                            %>
                            <tr>
                                <td>
                                    Sala <%=m + 1%>
                                </td>
                                <td>
                                    <% for (int p = 0; p < inizio.length; p++) {%>
                                    <%=inizio[p]%> - <%=fine[p]%> |
                                    <%}%>
                                </td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                </div>
                <%}%>
            </div>
        </div>
    </div>
</div>
<%}%>
</body>
</html>