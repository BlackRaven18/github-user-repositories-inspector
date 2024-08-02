package pl.wolski.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.wolski.demo.model.BranchDetails;
import pl.wolski.demo.model.RepositoryDetails;
import pl.wolski.demo.model.RepositoryOwner;
import pl.wolski.demo.utils.JsonUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubService gitHubService = new GitHubService(new RestTemplate(), new JsonUtils());

    @Test
    public void testGetUserReposDetails_Success() {
        String username = "testUser";
        RepositoryDetails repo1 = new RepositoryDetails();
        repo1.setName("repo1");
        repo1.setOwner(new RepositoryOwner("testUser"));

        RepositoryDetails repo2 = new RepositoryDetails();
        repo2.setName("repo2");
        repo2.setOwner(new RepositoryOwner("testUser"));

        String repoDetailsAsString = """
                [ {
                  "name" : "repo1",
                  "fork": false,
                  "owner" : {
                    "login" : "testUser"
                  }
                }, {
                  "name" : "repo2",
                  "fork": false,
                  "owner" : {
                    "login" : "testUser"
                  }
                } ]""";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(repoDetailsAsString);

        when(restTemplate.getForEntity("https://api.github.com/users/{username}/repos", String.class, username))
                .thenReturn(responseEntity);

        BranchDetails branch1 = new BranchDetails();
        branch1.setName("branch1");
        BranchDetails branch2 = new BranchDetails();
        branch2.setName("branch2");

        when(restTemplate.getForObject("https://api.github.com/repos/{owner}/{repo}/branches", BranchDetails[].class, "testUser", "repo1"))
                .thenReturn(new BranchDetails[]{branch1, branch2});

        when(restTemplate.getForObject("https://api.github.com/repos/{owner}/{repo}/branches", BranchDetails[].class, "testUser", "repo2"))
                .thenReturn(new BranchDetails[]{branch1});

        List<RepositoryDetails> result = gitHubService.getUserReposDetails(username);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getBranches().size());
        assertEquals(1, result.get(1).getBranches().size());
    }

    @Test
    public void testGetUserReposDetails_FilterForkRepos() {
        String username = "testUser";
        RepositoryDetails repo1 = new RepositoryDetails();
        repo1.setName("repo1");
        repo1.setOwner(new RepositoryOwner("testUser"));

        RepositoryDetails repo2 = new RepositoryDetails();
        repo2.setName("repo2");
        repo2.setOwner(new RepositoryOwner("testUser"));

        String repoDetailsAsString = """
                [ {
                  "name" : "repo1",
                  "fork": false,
                  "owner" : {
                    "login" : "testUser"
                  }
                }, {
                  "name" : "repo2",
                  "fork": true,
                  "owner" : {
                    "login" : "testUser"
                  }
                } ]""";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(repoDetailsAsString);

        when(restTemplate.getForEntity("https://api.github.com/users/{username}/repos", String.class, username))
                .thenReturn(responseEntity);

        when(restTemplate.getForObject("https://api.github.com/repos/{owner}/{repo}/branches", BranchDetails[].class, "testUser", "repo1"))
                .thenReturn(new BranchDetails[]{});

        List<RepositoryDetails> result = gitHubService.getUserReposDetails(username);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}