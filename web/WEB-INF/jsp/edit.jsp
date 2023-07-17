<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<jsp:include page="fragments/header.jsp"/>
<body>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">

        <%--        Имя--%>
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>

        <%--        Котакты--%>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <%--        Секции--%>
        <h3>Секции:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:choose>

                <%--                TextSection--%>
                <c:when test="${type==SectionType.PERSONAL || type==SectionType.OBJECTIVE}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input type="text" name="${type.name()}" size="30" value="${resume.getSection(type)}"></dd>
                    </dl>
                </c:when>

                <%--                ListSection--%>
                <c:when test="${type==SectionType.ACHIEVEMENT || type==SectionType.QUALIFICATIONS}">
                    <dt>${type.title}</dt>
                    <br>
                    <textarea id="" name="${type.name()}" rows="10"
                              cols="100">${resume.getSection(type).getAllItemsAsString()}</textarea>
                    <br>
                </c:when>

                <%--                OrganizatonSection--%>
                <c:when test="${type==SectionType.EXPERIENCE || type==SectionType.EDUCATION}">
                    <h4>${type.title}</h4>

                    <input type="hidden" name="${type.name()}" value="${resume.getSection(type)}">
                    <input type="hidden" name="${type}countOrg"
                           value="${resume.getSection(type).getOrganizations().size()}">

                    <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy"); %>
                    <c:forEach var="organization"
                               items="${resume.getSection(type).getOrganizations()}" varStatus="orgIndex">
                        <input type="text" name="${type}${orgIndex.index}" placeholder="Организация" size="60"
                               value="${organization.getHomePage().getName()}"><br>
                        <input type="text" name="${type}${orgIndex.index}url" placeholder="URL" size="60"
                               value="${organization.getHomePage().getUrl()}"><br>

                        <c:forEach var="position" items="${organization.getPositions()}" varStatus="posIndex">
                            <input type="hidden"
                                   name="${type}${orgIndex.index}countPosition"
                                   value="${organization.getPositions().size()}">
                            <ul>
                                <li>
                                    <input type="month"
                                           name="${type}${orgIndex.index}${posIndex.index}startDate"
                                           value="${position.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))}">
                                    <input type="month"
                                           name="${type}${orgIndex.index}${posIndex.index}finishDate"
                                           placeholder="finish" size="30"
                                           value="${position.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))}"><br>
                                    <input type="text"
                                           name="${type}${orgIndex.index}${posIndex.index}position"
                                           placeholder="Должность" size="30"
                                           value="${position.getTitle()}"><br>
                                    <textarea name="${type}${orgIndex.index}${posIndex.index}description"
                                              rows="3" cols="60"> ${position.getDescription()}</textarea>
                                </li>
                            </ul>
                            <br>
                            <hr>
                            <br>
                        </c:forEach>
                        <p></p>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>
