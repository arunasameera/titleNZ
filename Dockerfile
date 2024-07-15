FROM alpine:latest

RUN apk install nano tmux python3

ADD README.md   /workdir/README.md

RUN cd workdir && cat README.md | base64 > readme_encoded.txt
RUN cp /workdir/readme_encoded.txt /srv/index.html
# RUN chmod -R 777 /srv/

ENTRYPOINT [ "python3", "-m", "http.server", "--directory", "/srv", "1447" ]

RUN cd workdir && rm readme_encoded.txt
