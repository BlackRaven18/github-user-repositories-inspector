package pl.wolski.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wolski.demo.model.BranchDetails;
import pl.wolski.demo.model.RepositoryDetails;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class GitHubService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String REPOS_URL = "https://api.github.com/users/{username}/repos";
    private final String BRANCHES_URL = "https://api.github.com/repos/{owner}/{repo}/branches";

    public List<RepositoryDetails> getUserReposDetails(String username) {
        RepositoryDetails[] repoDetails = restTemplate.getForObject(REPOS_URL, RepositoryDetails[].class, username);
        List<RepositoryDetails> repositoryDetailsList = Arrays.asList(repoDetails);

        for (RepositoryDetails repositoryDetails : repositoryDetailsList) {
            List<BranchDetails> branchDetailsList
                    = getBranchDetails(repositoryDetails.getOwner().getLogin(), repositoryDetails.getName());

            repositoryDetails.setBranches(branchDetailsList);
        }
        return repositoryDetailsList;
    }

    public List<BranchDetails> getBranchDetails(String owner, String repo) {
        BranchDetails[] branchDetails = restTemplate.getForObject(BRANCHES_URL, BranchDetails[].class, owner, repo);
        return Arrays.asList(branchDetails);
    }
}
