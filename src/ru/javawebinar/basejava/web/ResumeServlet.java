package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.model.TextSection;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    // TODO add new resume
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                Section section;
                switch (type.name()) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        section = new TextSection(value.trim());
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        section = new ListSection(value.trim());
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        List<Organization> orgList = new ArrayList<>();
                        int countOrg = Integer.parseInt(request.getParameter(type.name() + "countOrg"));
                        for (int i = 0; i < countOrg; i++) {
                            String organization = request.getParameter(type.name() + i).trim();
                            String url = request.getParameter(type.name() + i + "url").trim();
                            String countPosition = request.getParameter(type.name() + i + "countPosition");
                            List<Organization.Position> posList = new ArrayList<>();
                            if (countPosition != null) {
                                for (int j = 0; j < Integer.parseInt(countPosition); j++) {
                                    String startDate = request.getParameter(type.name() + i + j + "startDate");
                                    String finishDate = request.getParameter(type.name() + i + j + "finishDate");
                                    String position = request.getParameter(type.name() + i + j + "position").trim();
                                    String description = request.getParameter(type.name() + i + j + "description").trim();
                                    if (!position.isEmpty()) {
                                        posList.add(new Organization.Position(startDate, finishDate, position, description));
                                    }
                                }
                            }
                            if (request.getParameter(type.name() + i + "newPosPosition").length() > 0) {
                                String startDate = request.getParameter(type.name() + i + "newPosStartDate");
                                String finishDate = request.getParameter(type.name() + i + "newPosFinishDate");
                                String position = request.getParameter(type.name() + i + "newPosPosition").trim();
                                String description = request.getParameter(type.name() + i + "newPosDescription").trim();
                                if (!position.isEmpty()) {
                                    posList.add(new Organization.Position(startDate, finishDate, position, description));
                                }
                            }
                            if (!organization.isEmpty()) {
                                Organization org = new Organization(new Link(organization, url), posList);
                                orgList.add(org);
                            }
                        }
                        Organization newOrg = getOrganization(request, type);
                        if (newOrg != null) {
                            orgList.add(newOrg);
                        }
                        if (orgList.isEmpty()) {
                            r.getSections().remove(type);
                            section = null;
                        } else {
                            section = new OrganizationSection(orgList);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong section type");
                }
                if (section != null) {
                    r.addSection(type, section);
                }
            } else if (request.getParameter(type.name() + "newOrg") != null &&
                    request.getParameter(type.name() + "newOrg").length() > 0) {
                r.addSection(type, new OrganizationSection(getOrganization(request, type)));
            } else {
                r.getSections().remove(type);
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ?
                        "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    private Organization getOrganization(HttpServletRequest request, SectionType type) {
        String organization = request.getParameter(type.name() + "newOrg").trim();
        if (organization.trim().length() > 0) {
            String url = request.getParameter(type.name() + "newOrgUrl").trim();
            String startDate = request.getParameter(type.name() + "newOrgStartDate");
            String finishDate = request.getParameter(type.name() + "newOrgFinishDate");
            String position = request.getParameter(type.name() + "newOrgPosition").trim();
            String description = request.getParameter(type.name() + "newOrgDescription").trim();
            Organization.Position pos = new Organization.Position(startDate, finishDate, position, description);
            return new Organization(organization, url, pos);
        }
        return null;
    }
}
