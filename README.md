# extractarr
[![Docker Image CI](https://github.com/egrosner/extractarr/actions/workflows/docker-image.yml/badge.svg)](https://github.com/egrosner/extractarr/actions/workflows/docker-image.yml)
Provide the ability to watch a folder and extract rar files. For use with sonarr / radarr.

## Docker Compose

```
extractarr:
  container_name: extractarr
  image: karby254/extractarr:latest
  volumes:
    - /mnt/nas/sync/in:/watch
```
