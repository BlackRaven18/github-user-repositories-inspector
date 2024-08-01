package pl.wolski.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wolski.demo.model.BranchDetails;
import pl.wolski.demo.model.RepositoryDetails;
import pl.wolski.demo.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class GitHubService {

    private RestTemplate restTemplate;
    private JsonUtils jsonUtils;
    private final String REPOS_URL = "https://api.github.com/users/{username}/repos";
    private final String BRANCHES_URL = "https://api.github.com/repos/{owner}/{repo}/branches";

    public List<RepositoryDetails> getUserReposDetails(String username) {
        ResponseEntity<String> reposDetailsAsString = restTemplate.getForEntity(REPOS_URL, String.class, username);
        JsonNode[] reposDetails = jsonUtils.convertJsonStringToObject(reposDetailsAsString.getBody(), JsonNode[].class);

        List<JsonNode> filteredRepos = filterNotForkRepos(reposDetails);
        List<RepositoryDetails> repositoryDetailsList = convertRepos(filteredRepos);

        for (RepositoryDetails repositoryDetails : repositoryDetailsList) {
            List<BranchDetails> branchDetailsList
                    = getBranchDetails(repositoryDetails.getOwner().getLogin(), repositoryDetails.getName());

            repositoryDetails.setBranches(branchDetailsList);
        }
        return repositoryDetailsList;
    }

    private List<JsonNode> filterNotForkRepos(JsonNode[] repos) {
        List<JsonNode> filteredRepos = new ArrayList<>();

        for (JsonNode repoDetails : repos) {
            if (!repoDetails.get("fork").asBoolean()) {
                filteredRepos.add(repoDetails);
            }
        }
        return filteredRepos;
    }

    private List<RepositoryDetails> convertRepos(List<JsonNode> repos) {
        List<RepositoryDetails> repositoryDetailsList = new ArrayList<>();

        for (JsonNode repo : repos) {
            RepositoryDetails repositoryDetails = jsonUtils.convertJsonNodeToObject(repo, RepositoryDetails.class);
            repositoryDetailsList.add(repositoryDetails);

        }
        return repositoryDetailsList;
    }

    private List<BranchDetails> getBranchDetails(String owner, String repo) {
        BranchDetails[] branchDetails = restTemplate.getForObject(BRANCHES_URL, BranchDetails[].class, owner, repo);
        if(branchDetails == null){
            return new ArrayList<>();
        }
        return Arrays.asList(branchDetails);
    }
}
