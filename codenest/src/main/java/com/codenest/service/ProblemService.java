package com.codenest.service;

import com.codenest.dao.ProblemDAO;
import com.codenest.model.Problem;
import java.sql.SQLException;
import java.util.List;

public class ProblemService {
    private ProblemDAO problemDAO;
    
    public ProblemService() {
        this.problemDAO = new ProblemDAO();
    }
    
    public List<Problem> getAllProblems() throws SQLException {
        return problemDAO.findAll();
    }
    
    public List<Problem> getProblemsByDifficulty(Problem.Difficulty difficulty) throws SQLException {
        return problemDAO.findByDifficulty(difficulty);
    }
    
    public List<Problem> getProblemsByPattern(Problem.Pattern pattern) throws SQLException {
        return problemDAO.findByPattern(pattern);
    }
    
    public boolean addProblem(Problem problem) throws SQLException {
        return problemDAO.save(problem);
    }
    
    public boolean markProblemAsSolved(Long studentId, Long problemId) throws SQLException {
        return problemDAO.markAsSolved(studentId, problemId);
    }
    
    public int getSolvedProblemsCount(Long studentId) throws SQLException {
        return problemDAO.getSolvedCount(studentId);
    }
}