FROM ghcr.io/graalvm/graalvm-ce:java11-21.0.0.2 as builder
WORKDIR builder
COPY project/*.properties project/*.sbt project/
RUN (SBT_VERSION=$(cat project/build.properties | cut -d '=' -f 2 | tr -d '[:space:]') \
      && curl -L -O https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz \
      && tar -xzf sbt-${SBT_VERSION}.tgz \
      && ./sbt/bin/sbt -mem 4096 sbtVersion)

COPY project/*.scala project/
COPY src/ src/
COPY build.sbt ./
RUN ./sbt/bin/sbt -mem 4096 clean stage

FROM ghcr.io/graalvm/graalvm-ce:java11-21.0.0.2 as packager_graal
WORKDIR packager
RUN gu install native-image
COPY --from=builder /builder/target/universal/stage/lib/ ./lib/
COPY --from=builder /builder/target/universal/stage/bin/ ./bin/
RUN mkdir -p app && mv lib/com.superkonduktr.challenge-*.jar app

FROM ghcr.io/graalvm/graalvm-ce:java11-21.0.0.2
WORKDIR app
COPY --from=packager_graal /packager/lib/ ./lib/
COPY --from=packager_graal /packager/bin/ ./bin/
COPY --from=packager_graal /packager/app/ ./lib/
EXPOSE 8080
ENTRYPOINT ["./bin/challenge"]
