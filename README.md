# Extractarr
[![Docker Image CI](https://github.com/egrosner/extractarr/actions/workflows/docker-image.yml/badge.svg)](https://github.com/egrosner/extractarr/actions/workflows/docker-image.yml)

Provide the ability to watch a folder and extract rar files. For use with sonarr / radarr.

## Usage

The build artifact is a docker image so you will need to have docker running. The recommended usage is via docker-compose:

```docker-compose
extractarr:
  container_name: extractarr
  image: karby254/extractarr:latest
  volumes:
    - /mnt/nas/sync/in:/watch
```

### Volumes
As per the example above, the following volumes must be mounted to wherever your rar files are located.

```<your rar file folder>:/watch```

With read / write access.
## Environment Variables

To run this project, you can optionally set the following environment variables when running it with docker.

`WATCH_CLEANUP=true`
This tells extractarr to remove the rar files that were used.

`WATCH_TIME=1`
How often should it scan for rar files (in minutes)? The default is 1 minute.

## Development

### Changes to the db model
If you change the db model (via the liquibase yml file), you will need to regenerate the JOOQ pregenerated sql files. In order to do that you must:

1. Delete the ```extractarr.db``` file if it exists.
2. ```gradle liquibaseLocalSqliteUpdate generateJooq```
3. Delete the ```extractarr.db``` file again and you can now run the program locally.

## Licenses
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
