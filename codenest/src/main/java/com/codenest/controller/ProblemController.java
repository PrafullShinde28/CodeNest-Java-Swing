package com.codenest.controller;

import com.codenest.service.ProblemService;
import com.codenest.model.Problem;
import java.sql.SQLException;
import java.util.List;

public class ProblemController {
    private ProblemService problemService;
    
    public ProblemController() {
        this.problemService = new ProblemService();
    }
    
    public List<Problem> getAllProblems() throws SQLException {
        return problemService.getAllProblems();
    }
    
    public boolean markProblemAsSolved(Long studentId, Long problemId) throws SQLException {
        return problemService.markProblemAsSolved(studentId, problemId);
    }
    
    public int getSolvedProblemsCount(Long studentId) throws SQLException {
        return problemService.getSolvedProblemsCount(studentId);
    }
    
    public void addProblem(Problem problem) {
        // Logic to add the problem to the database or in-memory list
        System.out.println("Problem added: " + problem.getTitle());
    }
}
