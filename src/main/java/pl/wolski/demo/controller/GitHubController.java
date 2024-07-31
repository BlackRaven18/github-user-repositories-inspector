package pl.wolski.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wolski.demo.model.ErrorDetails;
import pl.wolski.demo.model.RepositoryDetails;
import pl.wolski.demo.service.GitHubService;
import pl.wolski.demo.utils.JsonUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GitHubController {

    private JsonUtils jsonUtils;
    private GitHubService gitHubService;

    @GetMapping("/branches/{username}")
    public ResponseEntity<String> getUserBranches(@PathVariable String username) {

        String response;

        try {
            List<RepositoryDetails> userReposDetails = gitHubService.getUserReposDetails(username);
            response = jsonUtils.convertObjectToJSONString(userReposDetails);

        } catch (Exception e) {

            int errorMessageStartIndex = e.getMessage().indexOf("{");
            String errorMessage = e.getMessage().substring(errorMessageStartIndex);

            ErrorDetails errorDetails = jsonUtils.convertJSONStringToObject(errorMessage, ErrorDetails.class);
            String errorResponse = jsonUtils.convertObjectToJSONString(errorDetails);

            return ResponseEntity.status(errorDetails.getStatus()).body(errorResponse);
        }

        return ResponseEntity.ok(response);
    }
}
