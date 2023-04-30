FROM debian:stable-slim as base

FROM base as export

ENV KOBWEB_CLI_VERSION=0.9.12

# Copy the project code to an arbitrary subdir so we can install stuff in the
# Docker container root without worrying about clobbering project files.
COPY . /project

# Update and install required OS packages to continue
# Note: Playwright is a system for running browsers, and here we use it to install Chromium.
RUN apt-get update \
    && apt-get install -y curl gnupg unzip wget openjdk-11-jdk \
    && curl -sL https://deb.nodesource.com/setup_19.x | bash - \
    && apt-get install -y nodejs \
    && npm init -y \
    && npx playwright install --with-deps chromium

# Fetch the latest version of the Kobweb CLI
RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip \
    && unzip kobweb-${KOBWEB_CLI_VERSION}.zip \
    && rm kobweb-${KOBWEB_CLI_VERSION}.zip

ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

WORKDIR /project/site

# Decrease Gradle memory usage to avoid OOM situations in tight environments.
RUN mkdir ~/.gradle && \
    echo "org.gradle.jvmargs=-Xmx256m" >> ~/.gradle/gradle.properties

RUN kobweb export --notty

FROM base as run

COPY --from=export /project/site/.kobweb .kobweb

RUN apt-get update \
    && apt-get install -y openjdk-11-jre-headless

ENTRYPOINT .kobweb/server/start.sh