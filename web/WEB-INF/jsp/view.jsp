<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
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

    <%--    Имя --%>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>

    <%--    Контакты --%>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br>
        </c:forEach>
    </p>

    <%--   Секции --%>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
        <c:choose>

            <%--TextSection--%>
        <c:when test="${sectionEntry.key==SectionType.PERSONAL || sectionEntry.key==SectionType.OBJECTIVE}">
            ${sectionEntry.key.title} <br>
            ${sectionEntry.value}
        </c:when>

            <%--ListSection--%>
        <c:when test="${sectionEntry.key==SectionType.ACHIEVEMENT || sectionEntry.key==SectionType.QUALIFICATIONS}">
            ${sectionEntry.key.title} <br>
    <ul>
        <c:forEach var="item" items="${sectionEntry.value.getItems()}">
            <li>${item}</li>
        </c:forEach>
    </ul>
    </c:when>

        <%--OrganisationSection--%>
    <c:when test="${sectionEntry.key==SectionType.EXPERIENCE || sectionEntry.key==SectionType.EDUCATION}">
        ${sectionEntry.key.title} <br>
        <ul>
            <c:forEach var="organization" items="${sectionEntry.value.getOrganizations()}">
                <c:if test="${organization.getHomePage().getUrl()==''}">
                    ${organization.getHomePage().getName()}
                </c:if>
                <c:if test="${organization.getHomePage().getUrl()!=''}">
                    <a href="${organization.getHomePage().getUrl()}">${organization.getHomePage().getName()}</a>
                </c:if>
                <c:forEach var="position" items="${organization.getPositions()}">
                    <ul>
                        <li>

                            <c:if test="${DateUtil.isMonthNow(position.getStartDate())}">
                                Сейчас
                            </c:if>
                            <c:if test="${!DateUtil.isMonthNow(position.getStartDate())}">
                                ${position.getStartDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))}
                            </c:if>
                            -
                            <c:if test="${DateUtil.isMonthNow(position.getEndDate())}">
                                Сейчас<br>
                            </c:if>
                            <c:if test="${!DateUtil.isMonthNow(position.getEndDate())}">
                                ${position.getEndDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))}
                            </c:if><br>


<%--                                ${position.getStartDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))}--%>
<%--                            - ${position.getEndDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))}<br>--%>
                                ${position.getTitle()}<br>
                                ${position.getDescription()}<br>
                        </li>
                    </ul>
                </c:forEach>
            </c:forEach>
        </ul>
    </c:when>

    </c:choose>
    <br><br>
    </c:forEach>
</section>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>
