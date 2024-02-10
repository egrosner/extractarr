![Extractarr](https://raw.githubusercontent.com/egrosner/extractarr/main/images/extractarr.png)

# Extractarr
[![Docker Image CI](https://github.com/egrosner/extractarr/actions/workflows/docker-image.yml/badge.svg)](https://github.com/egrosner/extractarr/actions/workflows/docker-image.yml)

Provide the ability to watch a folder and extract rar files. For use with sonarr / radarr.

## Usage

The build artifact is a docker image so you will need to have docker running. The recommended usage is via docker-compose:

```docker-compose
extractarr:
  container_name: extractarr
  image: karby254/extractarr:latest
  ports: 
    - 9595:8080
  volumes:
    - /mnt/nas/sync/in:/watch
    - ./config:/config
```

## Frontend
There is now a frontend hosted by default at port 8080. If you use the docker compose config above you can map this port to any port you would like. It's currently found at the root url so: [https://localhost:9595](https://localhost:9595)

### Volumes
As per the example above, the following volumes must be mounted to wherever your rar files are located.

- ```<your rar file folder>:/watch```
  - Contains the actual rar files to watch and extract.
- ```<your config folder>:/config```
  - Contains the configuration for the application.


With read / write access.
## Environment Variables

To run this project, you can optionally set the following environment variables when running it with docker.

`WATCH_CLEANUP=true`
This tells extractarr to remove the rar files that were used.

`WATCH_TIME=1`
How often should it scan for rar files (in minutes)? The default is 1 minute.

## Development

### Changes to the db model
If you change the db model (via the sql migration scripts), you will need to regenerate the JOOQ pregenerated sql files. In order to do that you must:

1 ```gradle flywayMigrate generateJooq```

## Licenses
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
