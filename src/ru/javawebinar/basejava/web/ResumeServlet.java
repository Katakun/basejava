package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private SqlStorage sqlStorage;
    private String htmlStart = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<link rel=\"stylesheet\" href=\"css/style.css\">" +
            "<title>Курс JavaSE + Web.</title>" +
            "</head>" +
            "<body>\n" +
            "\n" +
            "<h2>Resumes</h2>\n" +
            "\n" +
            "<table class=\"table\">\n" +
            "  <tr>\n" +
            "    <th>UUID</th>\n" +
            "    <th>Full name</th>    \n" +
            "  </tr>";
    private String htmlEnd = "" +
            "</table>\n" +
            "</body>\n" +
            "</html>\n";

    public ResumeServlet() {
        init();
    }

    public void init() {
        sqlStorage = (SqlStorage) Config.get().getStorage();
    }

    private String tableAllResumes() {
        StringBuilder sb = new StringBuilder();
        List<Resume> allResumes = sqlStorage.getAllSorted();
        for (Resume resume : allResumes) {
            sb.append("" +
                    "  <tr>\n" +
                    "    <td>" + resume.getUuid() + "</td>\n" +
                    "    <td>" + resume.getFullName() + "</td>\n" +
                    "  </tr>\n");
        }
        return htmlStart + sb + htmlEnd;
    }

    private String tableOneResumes(String uuid) {
        Resume resume = sqlStorage.get(uuid);
        String resumeRow = "" +
                "  <tr>\n" +
                "    <td>" + resume.getUuid() + "</td>\n" +
                "    <td>" + resume.getFullName() + "</td>\n" +
                "  </tr>\n";
        return htmlStart + resumeRow + htmlEnd;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        response.getWriter().write(uuid == null ? tableAllResumes() : tableOneResumes(uuid));
    }
}
