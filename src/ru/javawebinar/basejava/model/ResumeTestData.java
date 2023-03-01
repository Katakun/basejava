package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResumeTestData {

    private static Random random = new Random();

    public static Resume getRandomResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        resume.setContact(ContactType.MOBILE, randomPhoneNumber());
        resume.setContact(ContactType.HOME_PHONE, randomPhoneNumber());
        resume.setContact(ContactType.PHONE, randomPhoneNumber());
        resume.setContact(ContactType.HOME_PAGE, randomWord(7));
        resume.setContact(ContactType.MAIL, randomWord(10));
        resume.setContact(ContactType.LINKEDIN, randomWord(15));
        resume.setContact(ContactType.GITHUB, randomWord(15));
        resume.setContact(ContactType.SKYPE, randomWord(5));
        resume.setContact(ContactType.STATCKOVERFLOW, randomWord(15));

        // TextSection
        Section personal = new TextSection(randomSentence(40));
        Section objective = new TextSection(randomSentence(40));
        resume.setSection(SectionType.PERSONAL, personal);
        resume.setSection(SectionType.OBJECTIVE, objective);

        // ListSection
        List<String> achievement = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            achievement.add(randomSentence(30));
        }
        Section achievementSection = new ListSection(achievement);
        resume.setSection(SectionType.ACHIEVEMENT, achievementSection);

        List<String> qualifications = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            qualifications.add(randomSentence(30));
        }
        Section qualificationsSection = new ListSection(qualifications);
        resume.setSection(SectionType.QUALIFICATIONS, qualificationsSection);

        // OrganizationSection
        Section experienceSection = new OrganizationSection(randomOrganization(5));
        Section educationSection = new OrganizationSection(randomOrganization(5));
        resume.setSection(SectionType.EXPERIENCE, experienceSection);
        resume.setSection(SectionType.EDUCATION, educationSection);

        return resume;
    }

    private static List<Organization> randomOrganization(int count) {
        List<Organization> organizations = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            List<Period> periods = new ArrayList<>();
            for (int i = 1; i < 3; i++) {
                LocalDate startDate = LocalDate.of(2000 + i, i, 1);
                LocalDate endDate = LocalDate.of(2001 + i, i, 1);
                Period period = new Period(randomWord(5), randomSentence(3),
                        startDate, endDate);
                periods.add(period);
            }
            Link link = new Link(randomWord(7), "https://www." + randomWord(7) + ".ru");
            Organization organization = new Organization(randomWord(6), link, periods);
            organizations.add(organization);
        }
        return organizations;
    }

    private static String randomPhoneNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("+7");
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt());
        }
        return sb.toString();
    }

    private static String randomSentence(int wordCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            int wordlength = random.nextInt(7) + 2;
            sb.append(randomWord(wordlength) + " ");
        }
        sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
        return sb.toString().trim() + ".";
    }

    private static String randomWord(int length) {
        String letterts = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(letterts.length());
            sb.append(letterts.charAt(index));
        }
        return sb.toString();
    }
}
