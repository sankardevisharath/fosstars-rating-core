package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Objects;

/**
 * This is a base class for data providers that would like to check the cache
 * before trying to fetch values for a project on GitHub.
 */
public abstract class GitHubCachingDataProvider extends AbstractCachingDataProvider<GitHubProject> {

  /**
   * An interface to GitHub.
   */
  protected final GitHubDataFetcher fetcher;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public GitHubCachingDataProvider(GitHubDataFetcher fetcher) {
    this.fetcher = Objects.requireNonNull(
        fetcher, "Oh no! You gave me a null instead of a GitHub fetcher!");
  }

  @Override
  public final boolean interactive() {
    return false;
  }

  /**
   * Returns an instance of {@link GitHubDataFetcher}.
   */
  protected GitHubDataFetcher gitHubDataFetcher() {
    return fetcher;
  }

}
