package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolicNameToDockerfileMapper {
    public static Map<ImageID, Dockerfile> imageIDDockerfileMap(List<Dockerfile> dockerfiles) {
        Map<ImageID, Dockerfile> mapIDToSourceFile = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            if (dockerfile.getSourceFile().endsWith("ubuntu_16.04.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("ubuntu:16.04"), dockerfile);
                mapIDToSourceFile.put(new ImageID("ubuntu:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("ubuntu"), dockerfile);
                mapIDToSourceFile.put(new ImageID("ubuntu:xenial-20170802"), dockerfile);
                mapIDToSourceFile.put(new ImageID("ubuntu:xenial"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("ubuntu_14.04.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("ubuntu:trusty-20170728"), dockerfile);
                mapIDToSourceFile.put(new ImageID("ubuntu:trusty"), dockerfile);
                mapIDToSourceFile.put(new ImageID("ubuntu:14.04"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("debian-jessie.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("debian:jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:8.9"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:8"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("debian-stretch.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("debian:stretch"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:9"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:9.1"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("golang-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("golang:1.8"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:1-jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:1.8-jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:1.8.3-jessie"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("node-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("node:8.4.0"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node:8.4"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node:8"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("buildpack-stretch-curl.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("buildpack-deps:stretch-curl"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("buildpack-stretch-scm.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("buildpack-deps:stretch-scm"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("buildpack-deps-jessie.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("buildpack-deps:jessie"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("buildpack-deps-jessie-curl.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("buildpack-deps:jessie-curl"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("buildpack-deps-jessie-scm.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("buildpack-deps:jessie-scm"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("openjdk-8-jdk.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("openjdk:8-jdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("openjdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("openjdk:8"), dockerfile);
                mapIDToSourceFile.put(new ImageID("openjdk:jdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("openjdk:latest"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("jenkins-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("jenkins:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("jenkins"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("debian-stretch-slim.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("debian:stretch-slim"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("busybox-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("busybox:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("busybox"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("nginx-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("nginx:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("nginx"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("golang-onbuild.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("golang:onbuild"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:1-ob"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:1.8-ob"), dockerfile);
                mapIDToSourceFile.put(new ImageID("golang:1.83-ob"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("python-2.7.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("python:2"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:2-jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:2.7"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:2.7-jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:2.7.13"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:2.7.13-jessie"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("python-3.5.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("python:3.5"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:3.5.4"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:3.5-jessie"), dockerfile);
                mapIDToSourceFile.put(new ImageID("python:3.5.4-jessie"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-7.0-apache.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:7.0-apache"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.0.22-apache"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-5.6-apache.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:5.6-apache"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:5.6.31-apache"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("java-7.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("java:7"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:7-jdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-7"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-7-jdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-7u111"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-7u111-jdk"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("java-8.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("java:8"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:8-jdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-8"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-8-jdk"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-8u111"), dockerfile);
                mapIDToSourceFile.put(new ImageID("java:openjdk-8u111-jdk"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("alpine-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("alpine:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("alpine:3.6"), dockerfile);
                mapIDToSourceFile.put(new ImageID("alpine"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("debian-wheezy.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("debian:wheezy"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:wheezy-20170123"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:7"), dockerfile);
                mapIDToSourceFile.put(new ImageID("debian:7.11"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("rabbitmq.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("rabbitmq"), dockerfile);
                mapIDToSourceFile.put(new ImageID("rabbitmq:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("rabbitmq:3"), dockerfile);
                mapIDToSourceFile.put(new ImageID("rabbitmq:3.6"), dockerfile);
                mapIDToSourceFile.put(new ImageID("rabbitmq:3.6.11"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("centos-7.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("c§§entos:7"), dockerfile);
                mapIDToSourceFile.put(new ImageID("centos"), dockerfile);
                mapIDToSourceFile.put(new ImageID("centos:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("centos:centos7"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:latest"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7-cli"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1-cli"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1.8"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1.8-cli"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:cli"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("redis-latest.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("redis"), dockerfile);
                mapIDToSourceFile.put(new ImageID("redis:latest"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("debian-jessie-slim.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("debian:jessie-slim"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("debian-sid.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("debian:sid"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("alpine-3.4.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("alpine:3.4"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-7.1-fpm.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:7.1-fpm"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7-fpm"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:fpm"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1.8fpm"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-7.0-fpm.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:7.0-fpm"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.0.22-fpm"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-5.6-fpm.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:5-fpm"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:5.6-fpm"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:5.6.31-fpm"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-7.0-cli.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:7.0-cli"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.0.22-cli"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.0.22"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.0"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("php-7.1-apache.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("php:apache"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7-apache"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1-apache"), dockerfile);
                mapIDToSourceFile.put(new ImageID("php:7.1.8-apache"), dockerfile);
            } else if (dockerfile.getSourceFile().endsWith("node-6.dockerfile")) {
                mapIDToSourceFile.put(new ImageID("node:6"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node:6.11"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node:6.11.2"), dockerfile);
                mapIDToSourceFile.put(new ImageID("node:boron"), dockerfile);
            }

        }

        mapIDToSourceFile.put(new ImageID("scratch"), new Dockerfile());

        return mapIDToSourceFile;
    }

    public static Map<ImageID, ImageID> aliases(List<Dockerfile> dockerfiles) {
        Map<ImageID, ImageID> aliases = new HashMap<>();

        aliases.put(new ImageID("ubuntu:16.04"), new ImageID("ubuntu:16.04"));
        aliases.put(new ImageID("ubuntu:latest"), new ImageID("ubuntu:16.04"));
        aliases.put(new ImageID("ubuntu"), new ImageID("ubuntu:16.04"));
        aliases.put(new ImageID("ubuntu:xenial-20170802"), new ImageID("ubuntu:16.04"));
        aliases.put(new ImageID("ubuntu:xenial"), new ImageID("ubuntu:16.04"));

        aliases.put(new ImageID("ubuntu:14.04"), new ImageID("ubuntu:14.04"));
        aliases.put(new ImageID("ubuntu:trusty-20170728"), new ImageID("ubuntu:14.04"));

        aliases.put(new ImageID("debian:jessie"), new ImageID("debian:jessie"));
        aliases.put(new ImageID("debian:8.9"), new ImageID("debian:jessie"));
        aliases.put(new ImageID("debian:8"), new ImageID("debian:jessie"));

        aliases.put(new ImageID("debian:latest"), new ImageID("debian:latest"));
        aliases.put(new ImageID("debian:stretch"), new ImageID("debian:latest"));
        aliases.put(new ImageID("debian"), new ImageID("debian:latest"));
        aliases.put(new ImageID("debian:9"), new ImageID("debian:latest"));
        aliases.put(new ImageID("debian:9.1"), new ImageID("debian:latest"));

        aliases.put(new ImageID("golang:latest"), new ImageID("golang:latest"));
        aliases.put(new ImageID("golang:1.8"), new ImageID("golang:latest"));
        aliases.put(new ImageID("golang"), new ImageID("golang:latest"));
        aliases.put(new ImageID("golang:jessie"), new ImageID("golang:latest"));
        aliases.put(new ImageID("golang:1-jessie"), new ImageID("golang:latest"));
        aliases.put(new ImageID("golang:1.8-jessie"), new ImageID("golang:latest"));
        aliases.put(new ImageID("golang:1.8.3-jessie"), new ImageID("golang:latest"));

        aliases.put(new ImageID("node:latest"), new ImageID("node:latest"));
        aliases.put(new ImageID("node:8.4.0"), new ImageID("node:latest"));
        aliases.put(new ImageID("node:8.4"), new ImageID("node:latest"));
        aliases.put(new ImageID("node:8"), new ImageID("node:latest"));
        aliases.put(new ImageID("node"), new ImageID("node:latest"));

        aliases.put(new ImageID("openjdk:latest"), new ImageID("openjdk:latest"));
        aliases.put(new ImageID("openjdk:8-jdk"), new ImageID("openjdk:latest"));
        aliases.put(new ImageID("openjdk"), new ImageID("openjdk:latest"));
        aliases.put(new ImageID("openjdk:8"), new ImageID("openjdk:latest"));
        aliases.put(new ImageID("openjdk:jdk"), new ImageID("openjdk:latest"));

        aliases.put(new ImageID("jenkins:latest"), new ImageID("jenkins:latest"));
        aliases.put(new ImageID("jenkins"), new ImageID("jenkins:latest"));

        aliases.put(new ImageID("busybox:latest"), new ImageID("busybox:latest"));
        aliases.put(new ImageID("busybox"), new ImageID("busybox:latest"));

        aliases.put(new ImageID("nginx:latest"), new ImageID("nginx:latest"));
        aliases.put(new ImageID("nginx"), new ImageID("nginx:latest"));

        aliases.put(new ImageID("golang:onbuild"), new ImageID("golang:onbuild"));
        aliases.put(new ImageID("golang:1-ob"), new ImageID("golang:onbuild"));
        aliases.put(new ImageID("golang:1.8-ob"), new ImageID("golang:onbuild"));
        aliases.put(new ImageID("golang:1.83-ob"), new ImageID("golang:onbuild"));

        aliases.put(new ImageID("python:2"), new ImageID("python:2"));
        aliases.put(new ImageID("python:2-jessie"), new ImageID("python:2"));
        aliases.put(new ImageID("python:2.7"), new ImageID("python:2"));
        aliases.put(new ImageID("python:2.7-jessie"), new ImageID("python:2"));
        aliases.put(new ImageID("python:2.7.13"), new ImageID("python:2"));
        aliases.put(new ImageID("python:2.7.13-jessie"), new ImageID("python:2"));

        aliases.put(new ImageID("python:3.5"), new ImageID("python:3.5"));
        aliases.put(new ImageID("python:3.5.4"), new ImageID("python:3.5"));
        aliases.put(new ImageID("python:3.5-jessie"), new ImageID("python:3.5"));
        aliases.put(new ImageID("python:3.5.4-jessie"), new ImageID("python:3.5"));

        aliases.put(new ImageID("php:7.0-apache"), new ImageID("php:7.0-apache"));
        aliases.put(new ImageID("php:7.0.22-apache"), new ImageID("php:7.0-apache"));

        aliases.put(new ImageID("php:5.6-apache"), new ImageID("php:5.6-apache"));
        aliases.put(new ImageID("php:5.6.31-apache"), new ImageID("php:5.6-apache"));

        aliases.put(new ImageID("java:7"), new ImageID("java:7"));
        aliases.put(new ImageID("java:7-jdk"), new ImageID("java:7"));
        aliases.put(new ImageID("java:openjdk-7"), new ImageID("java:7"));
        aliases.put(new ImageID("java:openjdk-7-jdk"), new ImageID("java:7"));
        aliases.put(new ImageID("java:openjdk-7u111"), new ImageID("java:7"));
        aliases.put(new ImageID("java:openjdk-7u111-jdk"), new ImageID("java:7"));

        aliases.put(new ImageID("java:latest"), new ImageID("java:latest"));
        aliases.put(new ImageID("java:8"), new ImageID("java:latest"));
        aliases.put(new ImageID("java"), new ImageID("java:latest"));
        aliases.put(new ImageID("java:8-jdk"), new ImageID("java:latest"));
        aliases.put(new ImageID("java:openjdk-8"), new ImageID("java:latest"));
        aliases.put(new ImageID("java:openjdk-8-jdk"), new ImageID("java:latest"));
        aliases.put(new ImageID("java:openjdk-8u111"), new ImageID("java:latest"));
        aliases.put(new ImageID("java:openjdk-8u111-jdk"), new ImageID("java:latest"));

        aliases.put(new ImageID("alpine:latest"), new ImageID("alpine:latest"));
        aliases.put(new ImageID("alpine:3.6"), new ImageID("alpine:latest"));
        aliases.put(new ImageID("alpine"), new ImageID("alpine:latest"));

        aliases.put(new ImageID("debian:wheezy"), new ImageID("debian:wheezy"));
        aliases.put(new ImageID("debian:wheezy-20170123"), new ImageID("debian:wheezy"));
        aliases.put(new ImageID("debian:7"), new ImageID("debian:wheezy"));
        aliases.put(new ImageID("debian:7.11"), new ImageID("debian:wheezy"));

        aliases.put(new ImageID("rabbitmq:latest"), new ImageID("rabbitmq:latest"));
        aliases.put(new ImageID("rabbitmq"), new ImageID("rabbitmq:latest"));
        aliases.put(new ImageID("rabbitmq:3"), new ImageID("rabbitmq:latest"));
        aliases.put(new ImageID("rabbitmq:3.6"), new ImageID("rabbitmq:latest"));
        aliases.put(new ImageID("rabbitmq:3.6.11"), new ImageID("rabbitmq:latest"));

        aliases.put(new ImageID("centos:latest"), new ImageID("centos:latest"));
        aliases.put(new ImageID("centos:7"), new ImageID("centos:latest"));
        aliases.put(new ImageID("centos"), new ImageID("centos:latest"));
        aliases.put(new ImageID("centos:centos7"), new ImageID("centos:latest"));

        aliases.put(new ImageID("php:latest"), new ImageID("php:latest"));
        aliases.put(new ImageID("php"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:7"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:7-cli"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:7.1"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:7.1-cli"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:7.1.8"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:7.1.8-cli"), new ImageID("php:latest"));
        aliases.put(new ImageID("php:cli"), new ImageID("php:latest"));

        aliases.put(new ImageID("redis:latest"), new ImageID("redis:latest"));
        aliases.put(new ImageID("redis"), new ImageID("redis:latest"));

        aliases.put(new ImageID("php:7.1-fpm"), new ImageID("php:7.1-fpm"));
        aliases.put(new ImageID("php:7-fpm"), new ImageID("php:7.1-fpm"));
        aliases.put(new ImageID("php:fpm"), new ImageID("php:7.1-fpm"));
        aliases.put(new ImageID("php:7.1.8fpm"), new ImageID("php:7.1-fpm"));

        aliases.put(new ImageID("php:7.0-fpm"), new ImageID("php:7.0-fpm"));
        aliases.put(new ImageID("php:7.0.22-fpm"), new ImageID("php:7.0-fpm"));

        aliases.put(new ImageID("php:5-fpm"), new ImageID("php:5-fpm"));
        aliases.put(new ImageID("php:5.6-fpm"), new ImageID("php:5-fpm"));
        aliases.put(new ImageID("php:5.6.31-fpm"), new ImageID("php:5-fpm"));

        aliases.put(new ImageID("php:7.0-cli"), new ImageID("php:7.0-cli"));
        aliases.put(new ImageID("php:7.0.22-cli"), new ImageID("php:7.0-cli"));
        aliases.put(new ImageID("php:7.0.22"), new ImageID("php:7.0-cli"));
        aliases.put(new ImageID("php:7.0"), new ImageID("php:7.0-cli"));

        aliases.put(new ImageID("php:apache"), new ImageID("php:apache"));
        aliases.put(new ImageID("php:7-apache"), new ImageID("php:apache"));
        aliases.put(new ImageID("php:7.1-apache"), new ImageID("php:apache"));
        aliases.put(new ImageID("php:7.1.8-apache"), new ImageID("php:apache"));

        aliases.put(new ImageID("node:6"), new ImageID("node:6"));
        aliases.put(new ImageID("node:6.11"), new ImageID("node:6"));
        aliases.put(new ImageID("node:6.11.2"), new ImageID("node:6"));
        aliases.put(new ImageID("node:boron"), new ImageID("node:6"));

        return aliases;
    }
}
