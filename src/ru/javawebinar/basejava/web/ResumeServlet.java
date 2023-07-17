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
import java.time.Month;
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
                        section = new TextSection(value);
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        section = new ListSection(value);
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        // TODO add empty section
                        List<Organization> orgList = new ArrayList<>();
                        Integer countOrg = Integer.valueOf(request.getParameter(type.name() + "countOrg"));
                        for (int i = 0; i < countOrg; i++) {
                            String organization = request.getParameter(type.name() + i);
                            String url = request.getParameter(type.name()+ i + "url");

                            String countPosition = request.getParameter(
                                    type.name() + i + "countPosition");
                            countPosition = countPosition == null ? "0" :countPosition;
                            List<Organization.Position> posList = new ArrayList<>();
                            // Position
                            // TODO add empty position
                            for (int j = 0; j < Integer.valueOf(countPosition); j++) {
                                String startDate = request.getParameter(type.name() + i + j + "startDate");
                                String finishDate = request.getParameter(type.name()+ i + j + "finishDate");
                                String position = request.getParameter(type.name() + i + j + "position");
                                String description = request.getParameter(type.name() + i + j +"description");

                                int startYear = Integer.valueOf(startDate.split("-")[0]);
                                int endYear = Integer.valueOf(finishDate.split("-")[0]);
                                Month startMonth = Month.of(Integer.valueOf(startDate.split("-")[1]));
                                Month endMonth = Month.of(Integer.valueOf(startDate.split("-")[1]));
                                posList.add(new Organization.Position(
                                        startYear, startMonth, endYear, endMonth,
                                        position, description));
                            }
                            Organization org = new Organization(new Link(organization, url), posList);
                            orgList.add(org);
                        }
                        section = new OrganizationSection(orgList);
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong section type");
                }
                r.addSection(type, section);
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
}
