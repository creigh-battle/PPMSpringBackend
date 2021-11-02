package com.creighbattle.services;

import com.creighbattle.domain.Backlog;
import com.creighbattle.domain.Project;
import com.creighbattle.exceptions.ProjectIdException;
import com.creighbattle.repositories.BacklogRepository;
import com.creighbattle.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            } else {
                project.setBacklog(backlogRepository.findBacklogByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase()
                    + "' Already Exists");
        }

    }

    public Project findProjectByIdentifier(String projectId) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project does not exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Cannot delete project with id '" + projectId + "'. " +
                    "This project does not exist");
        }

        projectRepository.delete(project);
    }


}
