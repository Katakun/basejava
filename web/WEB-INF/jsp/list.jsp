<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
    <section>
        <table class="table">
            <tr>
                <th>Имя</th>
                <th>Email</th>
            </tr>
            <%
                for (Resume resume : (List<Resume>) request.getAttribute("resumes")) {
            %>
            <tr>
                <td><a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%></a></td>
                <td><%=resume.getContact(ContactType.MAIL)%></td>
            </tr>
            <%
                }
            %>
        </table>
    </section>
</body>
</html>