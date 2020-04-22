package com.sap.sgs.phosphor.fosstars.data.interactive;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import org.junit.Test;

public class AskAboutSecurityTeamTest {

  @Test
  public void answerYes() throws IOException {
    testProvider(true, new AskAboutSecurityTeam(), new TestUserCallback("yes"));
  }

  @Test
  public void answerNo() throws IOException {
    testProvider(false, new AskAboutSecurityTeam(), new TestUserCallback("no"));
  }

  private static void testProvider(
      boolean expected, AskAboutSecurityTeam provider, UserCallback callback) throws IOException {

    ValueSet values = new ValueHashSet();
    provider.set(new GitHubProjectValueCache());
    provider.set(callback);
    GitHubProject project = new GitHubProject("org", "test");
    provider.update(project, values);
    assertEquals(1, values.size());
    assertTrue(values.has(HAS_SECURITY_TEAM));
    assertTrue(values.of(HAS_SECURITY_TEAM).isPresent());
    assertEquals(expected, values.of(HAS_SECURITY_TEAM).get().get());
  }

}