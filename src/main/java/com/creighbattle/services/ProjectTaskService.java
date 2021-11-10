package com.creighbattle.services;

import com.creighbattle.domain.Backlog;
import com.creighbattle.domain.Project;
import com.creighbattle.domain.ProjectTask;
import com.creighbattle.exceptions.ProjectNotFoundException;
import com.creighbattle.repositories.BacklogRepository;
import com.creighbattle.repositories.ProjectRepository;
import com.creighbattle.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try {
            Backlog backlog = backlogRepository.findBacklogByProjectIdentifier(projectIdentifier);

            projectTask.setBacklog(backlog);

            Integer backlogSeq = backlog.getPTSequence();
            backlogSeq++;

            backlog.setPTSequence(backlogSeq);

            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSeq);

            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == 0 || projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }

//            if (projectTask.getPriority() == null) {
//                projectTask.setPriority(3);
//            }

            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }


    }

    public List<ProjectTask> findBacklogById(String backlog_id) {

        Project project = projectRepository.findByProjectIdentifier(backlog_id);

        if (project == null) {
            throw new ProjectNotFoundException("Project with ID: '" + backlog_id +
                    "' does not exist");
        }

        return projectTaskRepository.findProjectTaskByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {

        Backlog backlog = backlogRepository.findBacklogByProjectIdentifier(backlog_id);

        if (backlog == null) {
            throw new ProjectNotFoundException("Project with id: '" + backlog_id + "' does not exist");
        }

        ProjectTask projectTask = projectTaskRepository.findProjectTaskByProjectSequence(pt_id);

        if (projectTask == null) {
            throw new ProjectNotFoundException("Project task '" + pt_id + "' not found");
        }

        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project task does not exist in project: '"
            + backlog_id + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

//        Backlog backlog = projectTask.getBacklog();
//        List<ProjectTask> pts = backlog.getProjectTasks();
//        pts.remove(projectTask);
//        backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask);
    }
}
