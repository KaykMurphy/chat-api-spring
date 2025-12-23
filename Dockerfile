FROM ubuntu:latest
LABEL authors="kayks"

ENTRYPOINT ["top", "-b"]