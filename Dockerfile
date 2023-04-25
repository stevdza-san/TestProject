FROM debian:stable-slim as base

FROM base as fetch

ARG GITHUB_OWNER=stevdza-san
ARG GITHUB_REPO=TestProject

# Secret "github-token.txt" should have been added in Environment > Secret Files!
COPY github-token.txt github-token.txt

RUN apt-get update \
    && apt-get install -y curl jq unzip

# Fetch the latest artifact metadata from GitHub \
RUN curl -GL \
      -H "Accept: application/vnd.github+json" \
      -H "Authorization: Bearer $(cat github-token.txt)" \
      -H "X-GitHub-Api-Version: 2022-11-28" \
      "https://api.github.com/repos/${GITHUB_OWNER}/${GITHUB_REPO}/actions/artifacts" \
        -d 'per_page=1' -d 'name=kobweb-folder' \
      > latest-release.json \
    && cat latest-release.json

# Use jq to parse the JSON response and extract the download URL for our target release artifact
RUN jq -r '.artifacts[0].archive_download_url' latest-release.json > download-url.txt \
    && cat download-url.txt

# Download the latest artifact
RUN curl -GL \
      -H "Authorization: Bearer $(cat github-token.txt)" \
      $(cat download-url.txt) \
      > kobweb-folder.zip \
    # Unzip the artifact. Its payload contains everything we need to run our site. The folder must be called `.kobweb`.
    && unzip kobweb-folder.zip -d .kobweb

FROM base as run

COPY --from=fetch .kobweb .kobweb

RUN apt-get update \
    && apt-get install -y openjdk-11-jre-headless

CMD java -Dkobweb.server.environment=PROD -Dkobweb.site.layout=KOBWEB -Dio.ktor.development=false -jar .kobweb/server/server.jar

