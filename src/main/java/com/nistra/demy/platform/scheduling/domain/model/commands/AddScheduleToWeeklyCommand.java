package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to add a schedule to a weekly schedule.
 *
 * @param weeklyScheduleId ID of the weekly schedule
 * @param startTime        Start time of the schedule
 * @param endTime          End time of the schedule
 * @param dayOfWeek        Day of the week for the schedule
 * @param courseId         ID of the course
 * @param classroomId      ID of the classroom
 * @param teacherFirstName First name of the teacher
 * @param teacherLastName  Last name of the teacher
 */
public record AddScheduleToWeeklyCommand(
        Long weeklyScheduleId,
        String startTime,
        String endTime,
        String dayOfWeek,
        Long courseId,
        Long classroomId,
        String teacherFirstName,
        String teacherLastName
) {}