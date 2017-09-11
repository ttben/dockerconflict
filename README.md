
# Guidelines
---
###### 1. `FROM` command first
the `FROM` command must be the first to appear in a dockerfile

---
###### 2. `RUN` Exec form
`RUN` commands have two syntaxes, one with brackets and one without. Interpretation of arguments differ from the two syntaxes. The one with brackets must be used.

---
###### 3. Multiple `CMD`
`CMD` commands allows one to start a service when booting up a container. Docker allows only **a single service** to be specified, therefore multiple `CMD` are useless since only the last one will be run.

---
###### 4. Provides default to `CMD`
One has to provide default parametervia `CMD` to start a service. If an `EntryPoint` command is specified, `CMD` and `EntryPoint` commands should be specified in `JSON` format.

---
###### 5. Variables in exec form of `CMD`
Variables used in `CMD` commands in its exec form are not interpreted.
`CMD [ "echo", "$HOME" ]` won't output the `$HOME` variable value.

---
###### 6. Merge `LABEL` commands
When possible, merge labels commands.

---
###### 7. Avoid `apt-get upgrade`
You should avoid `RUN apt-get upgrade` or `dist-upgrade`, as many of the “essential” packages from the base images won’t upgrade inside an unprivileged container

---
###### 8. Combine `install` with `update`

Always combine `RUN apt-get update` with `apt-get install` in the same `RUN` statement. Ommiting this can lead to unexpected behaviour since `apt-get update` can be not run.

---
###### 9. Packages, version pinning

Always fully specify the version of the package to install.

---
###### 10. `FROM`, version pinning

Always fully specify the version of the parent dockerfile to use (**i.e.** __latest__ tag is therefore not permitted).

---
###### 11. `CMD` exec form
`CMD` commands have two syntaxes, one with brackets and one without. Interpretation of arguments differ from the two syntaxes. The one with brackets must be used if parameters are specified.

---
###### 12. Prefer `COPY` 
Although `ADD` and `COPY` are functionally similar, generally speaking, `COPY` is preferred.

---
###### 13. `ADD <http>` discouraged
Because image size matters, using `ADD` to fetch packages from remote `URL`s is strongly discouraged; you should use `curl` or `wget`.

---
###### 14. User `root` discouraged
You should avoid installing or using `sudo` since it has unpredictable. `TTY` and signal-forwarding behavior that can cause more problems than it solves. If you absolutely need functionality similar to `sudo` (e.g., initializing the daemon as `root` but running it as non-root), you may be able to use `gosu`.

---
###### 15. Less `USER` commands as possible
To reduce layers and complexity, avoid switching `USER` back and forth frequently.

---
###### 16. `WORKDIR` must have absolute path 
For clarity and reliability, you should always use absolute paths for your `WORKDIR`.

---
###### 17. `cd` in `RUN` should be avoided
Don’t use `cd` in `RUN` commands, use `WORKDIR` instead.

---
###### 18. Sort installation alphanumerically
Installation of multiple softwares must be written in alphanumerical order.

---
###### 19. Add `–no-install-recommend`
Add `–no-install-recommend` when installing with `apt-get`, this will avoid installation not explicitly specified.

Guidelines can be found [here](https://github.com/ttben/dockerconflict/tree/master/src/main/java/fr/unice/i3s/sparks/docker/core/guidelines). Guidelines `4` and `11` are not implemented since they are too domain-specific.


# Collecting of dockerfiles

Our main requirement was to  avoid \textbf{downloading} images since **(i)** a docker image can easily weight more than 500MB (the official version 3.5 of python image  weights ~680MB, the official java image weights ~640MB and node ~655MB) the amount of data to store would be too large, and **(ii)** a docker image does not contain all the information originally written by the user.

We first targeted the largest collections of docker files we known: the DockerHub. This hub hosts both official images (around 120 images)\footnote{\url{http://www.slideshare.net/Docker/dockercon-16-general-session-day-2-63497745}} and open non-official repositories (around 150 000 repositories\footnote{\url{https://www.ctl.io/developers/blog/post/docker-hub-top-10/}}). This hub is based on a `registry` that list all available images (and therefore, dockerfile)\footnote{\url{http://54.71.194.30:4014/reference/api/docker-io_api/}} through a \textit{catalogue} endpoint. This specification is currently __not implemented by the docker company__ itself\footnote{https://github.com/docker/distribution/pull/653} therefore we can not list all available images or dockerfiles in the hub. 

The second biggest source of dockerfiles was GitHub. We decided to crawl a set of dockerfiles from GitHub platform. Again, GitHub does not provide an `API endpoint` to list all files by type. We had to `web-crawl` dockerfiles as a physical user would do. Due to GitHub restrictions, one can not look for a specific type of file and have to specify information about the content of the dockerfile.
This communal platform allowed us to perform more or less specific request on the content of the \textit{dockerfiles} and gave us a random sample of it. This has must been done by crawling too.
We use a chrome-extension to crawl github content. We perform request on those kind or URLs:

`https://github.com/search?p=100&q=language%3ADockerfile+FROM&ref=searchresults&type=Code&utf8=%E2%9C%93`

to be able to find Dockerfile that, at least, contains a FROM code inside.

We then filter the result to delete duplicates since the `API` returned a `random` sample.

In order to have a fair set of files, we iteratively looked for docker commands from the docker DSL. This way, we had an homogenous amount of each docker commands and let statistics do the remaining work.


Moreover, the parent-child relationship still needs to be established since the layer do not store explicitly the parent image ID.
