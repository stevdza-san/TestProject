FROM debian:stable-slim
USER root

# Copy the project code to app dir
COPY . /app

# Install OpenJDK-11 (earliest JDK kobweb can run on)
RUN apt-get update \
    && apt-get install -y openjdk-11-jdk \
    && apt-get install -y ant \
    && apt-get clean

# Fix certificate issues
RUN apt-get update \
    && apt-get install ca-certificates-java \
    && apt-get clean \
    && update-ca-certificates -f

# Setup JAVA_HOME -- needed by kobweb / gradle
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64/
RUN export JAVA_HOME
RUN java -version

# Install kobweb
RUN apt-get update && apt-get install -y wget unzip

ARG KOBWEB_CLI_VERSION=0.9.11

RUN wget https://github.com/varabyte/kobweb/releases/download/cli-v$KOBWEB_CLI_VERSION/kobweb-$KOBWEB_CLI_VERSION.zip \
    && unzip kobweb-$KOBWEB_CLI_VERSION.zip \
    && rm -r kobweb-$KOBWEB_CLI_VERSION.zip
ENV PATH="/kobweb-$KOBWEB_CLI_VERSION/bin:${PATH}"

WORKDIR /app

# Decrease memory usage to avoid OOM situations in
# tight environments
RUN echo "" >> gradle.properties # add a newline
RUN echo "org.gradle.jvmargs=-Xmx256m" >> gradle.properties

RUN kobweb export --mode dumb

RUN export PORT=$(kobweb conf server.port)
EXPOSE $PORT

# Purge all the things we don't need anymore

RUN apt-get purge --auto-remove -y curl gnupg wget unzip \
    && rm -rf /var/lib/apt/lists/*

# Keep container running because `kobweb run --mode dumb` doesn't block
CMD kobweb run --mode dumb --env prod && tail -f /dev/null